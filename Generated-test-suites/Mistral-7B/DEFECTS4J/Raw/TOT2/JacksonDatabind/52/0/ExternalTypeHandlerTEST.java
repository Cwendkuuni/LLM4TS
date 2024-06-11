import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import static com.fasterxml.jackson.databind.JsonTest.*;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private Object testObject;
 private DeserializationContext context;
 private JsonParser parser;

 @Before
 public void setup() throws IOException {
 testObject = new Object();
 context = mock(DeserializationContext.class);
 parser = createParser("[]");
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntegerTypeDeserializer())
 .build());
 }

 @Test
 public void testStart() {
 handler = handler.start();
 // no assertions needed here as start method does not return anything.
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 String typeId = "StringType";
 handler.handleTypePropertyValue(parser, context, "prop1", testObject);
 assertEquals("prop1", parser.getCurrentName());
 assertEquals("StringType", handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValue_MissingTypeProperty() throws IOException {
 handler.handleTypePropertyValue(parser, context, "nonExistentProp", testObject);
 assertFalse(parser.hasCurrentName());
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValue_MissingProperty() throws IOException {
 handler.handleTypePropertyValue(parser, context, "prop1", null);
 assertFalse(parser.hasCurrentName());
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 String value = "testValue";
 handler.handlePropertyValue(parser, context, "prop1", testObject);
 assertEquals("prop1", parser.getCurrentName());
 assertEquals(value, handler._typeIds[0]);
 }

 @Test
 public void testHandlePropertyValue_MissingProperty() throws IOException {
 handler.handlePropertyValue(parser, context, "nonExistentProp", testObject);
 assertFalse(parser.hasCurrentName());
 assertNull(handler._typeIds[0]);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValue_ParsingError() throws IOException {
 parser = createParser("{'prop1': {}}"); // invalid JSON
 handler.handlePropertyValue(parser, context, "prop1", testObject);
 }

 @Test
 public void testComplete() throws IOException {
 handler.complete(parser, context, testObject);
 assertNull(handler._typeIds);
 assertNull(handler._tokens);
 }

 @Test
 public void testComplete_NullValue() throws IOException {
 handler.complete(parser, context, null);
 assertNull(handler._typeIds);
 assertNull(handler._tokens);
 }

 @Test
 public void testComplete_MissingTypeId() throws IOException {
 parser = createParser("{\"prop1\": null, \"prop2\": \"testValue\"}");
 handler.complete(parser, context, testObject);
 assertEquals("testValue", handler._typeIds[1]);
 }

 @Test
 public void testComplete_MissingProperty() throws IOException {
 parser = createParser("{\"prop1\": \"testValue\"}");
 handler.complete(parser, context, testObject);
 assertNull(handler._typeIds[0]);
 }

 @Test
 public void testComplete_MissingBoth() throws IOException {
 parser = createParser("[]");
 handler.complete(parser, context, testObject);
 assertNull(handler._typeIds);
 assertNull(handler._tokens);
 }

 @Test
 public void testComplete_CustomCreator() throws IOException {
 Object testObject2 = new Object();
 ContextResolver<?> resolver = mock(ContextResolver.class);
 JsonFactory factory = new JsonFactory();
 factory.setDefaultUseWrapper(false);
 parser = factory.createParser("[{'prop1': null, 'prop2': {}}]");
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
 .addExternal(new SettableBeanProperty("prop2", CustomCreator.class), new CustomCreator())
 .build());
 CustomCreator creator = new CustomCreator();
 when(resolver.findValueDeserializer(String.class, context)).thenReturn(new StringDeserializer());
 when(resolver.findValueDeserializer(CustomCreator.class, context)).thenReturn(creator);
 Object result = handler.complete(parser, context, testObject2, resolver);
 assertInstanceOf(CustomCreator.class, result);
 verify(creator).build(eq(context), any(PropertyValueBuffer.class));
 }

}

class StringTypeDeserializer extends StdTypeDeserializer<String> {
 StringTypeDeserializer() { super(String.class); }
}

class IntegerTypeDeserializer extends StdTypeDeserializer<Integer> {
 IntegerTypeDeserializer() { super(Integer.class); }
}

class CustomCreator implements PropertyBasedCreator<Object> {
 @Override
 public Object build(DeserializationContext ctxt, PropertyValueBuffer pvb) {
 return new Object();
 }
}