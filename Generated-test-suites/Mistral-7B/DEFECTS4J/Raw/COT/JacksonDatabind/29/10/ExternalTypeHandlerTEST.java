import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser jsonParser;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(new byte[]{}));
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 SettableBeanProperty property = new SettableBeanProperty(new Object(), "testProperty");
 TypeDeserializer typeDeserializer = new StdTypeDeserializer(); // replace this with an actual TypeDeserializer
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, typeDeserializer);
 handler = builder.build();
 handler.start();

 handler.handleTypePropertyValue(jsonParser, null, "testProperty", null);
 Assert.assertNotNull(handler._typeIds);
 Assert.assertEquals("testProperty", handler._typeIds[0]);
 }

 @Test
 public void testHandlePropertyValueWithNullBean() throws Exception {
 SettableBeanProperty property = new SettableBeanProperty(null, "testProperty");
 TypeDeserializer typeDeserializer = new StdTypeDeserializer(); // replace this with an actual TypeDeserializer
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, typeDeserializer);
 handler = builder.build();
 handler.start();

 handler.handlePropertyValue(jsonParser, null, "testProperty", null);
 Assert.assertNull(handler._typeIds);
 Assert.assertNull(handler._tokens[0]);
 }

 @Test
 public void testHandlePropertyValueWithNullTokens() throws Exception {
 SettableBeanProperty property = new SettableBeanProperty(new Object(), "testProperty");
 TypeDeserializer typeDeserializer = new StdTypeDeserializer(); // replace this with an actual TypeDeserializer
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, typeDeserializer);
 handler = builder.build();
 handler.start();
 handler._tokens[0] = null;

 handler.handlePropertyValue(jsonParser, null, "testProperty", null);
 Assert.assertNull(handler._typeIds[0]);
 Assert.assertNull(handler._tokens[0]);
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValueWithInvalidTypeId() throws Exception {
 SettableBeanProperty property = new SettableBeanProperty(new Object(), "testProperty");
 TypeDeserializer typeDeserializer = new StdTypeDeserializer(); // replace this with an actual TypeDeserializer
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, typeDeserializer);
 handler = builder.build();
 handler.start();

 jsonParser.nextToken(); // consume START_OBJECT
 jsonParser.nextToken(); // consume PROPERTY_NAME
 jsonParser.nextToken(); // consume VALUE_NULL
 jsonParser.nextToken(); // consume END_OBJECT
 jsonParser.nextToken(); // consume START_ARRAY
 jsonParser.nextToken(); // consume STRING
 jsonParser.nextToken(); // consume STRING "InvalidTypeId"
 jsonParser.nextToken(); // consume END_ARRAY

 handler.handleTypePropertyValue(jsonParser, null, "testProperty", null);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueWithInvalidPropertyName() throws Exception {
 SettableBeanProperty property = new SettableBeanProperty(new Object(), "testProperty");
 TypeDeserializer typeDeserializer = new StdTypeDeserializer(); // replace this with an actual TypeDeserializer
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, typeDeserializer);
 handler = builder.build();
 handler.start();

 jsonParser.nextToken(); // consume START_OBJECT
 jsonParser.nextToken(); // consume PROPERTY_NAME
 jsonParser.nextToken(); // consume VALUE_STRING "InvalidPropertyName"

 handler.handlePropertyValue(jsonParser, null, "testProperty", null);
 }

 @Test
 public void testComplete() throws Exception {
 SettableBeanProperty property = new SettableBeanProperty(new Object(), "testProperty");
 TypeDeserializer typeDeserializer = new StdTypeDeserializer(); // replace this with an actual TypeDeserializer
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, typeDeserializer);
 handler = builder.build();
 handler.start();
 handler.handleTypePropertyValue(jsonParser, null, "testProperty", null);
 Object result = handler.complete(jsonParser, null, null);
 Assert.assertNotNull(result);
 }

 @Test(expected = IOException.class)
 public void testCompleteWithMissingTypeId() throws Exception {
 SettableBeanProperty property = new SettableBeanProperty(new Object(), "testProperty");
 TypeDeserializer typeDeserializer = new StdTypeDeserializer(); // replace this with an actual TypeDeserializer
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, typeDeserializer);
 handler = builder.build();
 handler.start();
 handler.handlePropertyValue(jsonParser, null, "testProperty", null);
 handler._typeIds = new String[1];

 Object result = handler.complete(jsonParser, null, null);
 }

 @Test(expected = IOException.class)
 public void testCompleteWithMissingProperty() throws Exception {
 SettableBeanProperty property = new SettableBeanProperty(new Object(), "testProperty");
 TypeDeserializer typeDeserializer = new StdTypeDeserializer(); // replace this with an actual TypeDeserializer
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 builder.addExternal(property, typeDeserializer);
 handler = builder.build();
 handler.start();
 handler._typeIds = new String[1];
 handler._tokens = new TokenBuffer[1];
 handler._tokens[0] = new TokenBuffer(jsonParser);
 handler._tokens[0].nextToken();
 handler._tokens[0].nextToken();
 handler._tokens[0].nextToken();
 handler._tokens[0].copyCurrentStructure(jsonParser);
 handler._tokens[0].writeEndObject();
 handler.handleTypePropertyValue(jsonParser, null, "testProperty", null);

 Object result = handler.complete(jsonParser, null, null);
 }
}