import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser parser;
 private DeserializationContext context;

 @Before
 public void setup() throws Exception {
 JsonFactory factory = new JsonFactory();
 parser = factory.createParser("{\"a\":1,\"b\":{\"typeId\":\"String\",\"c\":\"Hello\"}}");
 context = new DeserializationContext(factory);
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().addExternal(new SettableBeanProperty("a", String.class), null)
 .addExternal(new SettableBeanProperty("b.c", String.class), new SimpleTypeDeserializer(String.class))
 .build());
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 assertTrue(handler.handleTypePropertyValue(parser, context, "b", null));
 assertEquals("String", handler.getTypeIds()[1]);
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValueWhenPropertyNotFound() throws Exception {
 handler.handleTypePropertyValue(parser, context, "x", null);
 }

 @Test
 public void testHandleTypePropertyValueWhenTypePropertyNameMismatch() throws Exception {
 parser = parser.skipChildren();
 parser.nextToken(); // "{"
 assertTrue(parser.nextToken() == JsonToken.FIELD_NAME);
 parser.nextToken(); // "a"
 parser.nextToken(); // "1"
 parser.nextToken(); // ","
 parser.nextToken(); // "typeId"
 parser.nextToken(); // "String"
 parser.nextToken(); // ","
 parser.nextToken(); // "c"
 parser.nextToken(); // "Hello"
 parser.nextToken(); // "}"
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().addExternal(new SettableBeanProperty("a", String.class), null)
 .addExternal(new SettableBeanProperty("d.c", String.class), new SimpleTypeDeserializer(String.class))
 .build());
 assertFalse(handler.handleTypePropertyValue(parser, context, "b", null));
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 handler = handler.start();
 parser.nextToken(); // "{"
 assertTrue(parser.nextToken() == JsonToken.FIELD_NAME);
 parser.nextToken(); // "a"
 parser.nextToken(); // "1"
 parser.nextToken(); // ","
 assertTrue(parser.nextToken() == JsonToken.FIELD_NAME);
 parser.nextToken(); // "b"
 handler.handlePropertyValue(parser, context, "b", null);
 assertNull(handler.getTypeIds()[0]);
 assertNotNull(handler.getTokens()[0]);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueWhenPropertyNotFound() throws Exception {
 handler = handler.start();
 parser.nextToken(); // "{"
 assertTrue(parser.nextToken() == JsonToken.FIELD_NAME);
 parser.nextToken(); // "x"
 handler.handlePropertyValue(parser, context, "x", null);
 }

 @Test
 public void testHandlePropertyValueWhenTypePropertyNameMismatch() throws Exception {
 handler = handler.start();
 parser.nextToken(); // "{"
 assertTrue(parser.nextToken() == JsonToken.FIELD_NAME);
 parser.nextToken(); // "a"
 parser.nextToken(); // "1"
 parser.nextToken(); // ","
 assertTrue(parser.nextToken() == JsonToken.FIELD_NAME);
 parser.nextToken(); // "b"
 parser.nextToken(); // "{"
 parser.nextToken(); // "typeId"
 parser.nextToken(); // "IntType"
 parser.nextToken(); // ","
 parser.nextToken(); // "c"
 parser.nextToken(); // "Hello"
 parser.nextToken(); // "}"
 handler.handlePropertyValue(parser, context, "b", null);
 assertNull(handler.getTypeIds()[0]);
 assertNotNull(handler.getTokens()[0]);
 }

 @Test
 public void testComplete() throws Exception {
 Object bean = handler.complete(parser, context, null);
 assertEquals("1", handler.getProperties()[0].getProperty().getValue(bean));
 assertEquals("Hello", handler.getProperties()[1].getProperty().getValue(bean));
 }

 @Test(expected = IOException.class)
 public void testCompleteWhenPropertyNotFound() throws Exception {
 handler = handler.start();
 handler.setTypeIds(new String[]{null});
 handler.setTokens(new TokenBuffer[1]);
 Object bean = handler.complete(parser, context, null);
 }

 @Test
 public void testCompleteWhenPropertyValueNotFound() throws Exception {
 handler = handler.start();
 handler.setTypeIds(new String[1]);
 handler.setTokens(new TokenBuffer[1]);
 Object bean = handler.complete(parser, context, null);
 assertNull(handler.getProperties()[0].getProperty().getValue(bean));
 }

 @Test
 public void testCompleteWithCreator() throws Exception {
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().addExternal(new SettableBeanProperty("a", String.class), null)
 .addExternal(new SettableBeanProperty("b.c", String.class), new SimpleTypeDeserializer(String.class))
 .build());
 Object bean = handler.complete(parser, context, null, new SimpleBeanCreator());
 assertEquals("1", handler.getProperties()[0].getProperty().getValue(bean));
 assertEquals("Hello", handler.getProperties()[1].getProperty().getValue(bean));
 }

 private Object[] getProperties() {
 return handler.getProperties();
 }

}