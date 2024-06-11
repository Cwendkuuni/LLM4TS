import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonFactory jsonFactory;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws JsonProcessingException {
 jsonFactory = new JsonFactory();
 objectMapper = new ObjectMapper(jsonFactory);
 handler = new ExternalTypeHandler.Builder()
 .addExternal(new SimplePojo().getClass().getDeclaredField("property1"), null)
 .addExternal(new SimplePojo().getClass().getDeclaredField("property2"), null)
 .build();
 }

 @Test
 public void testStart() {
 ExternalTypeHandler newHandler = handler.start();
 assertNotNull(newHandler);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHandleTypePropertyValueWithInvalidPropertyName() throws IOException {
 JsonParser parser = jsonFactory.createParser("{\"invalidProperty\": \"typeId\"}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 handler.handleTypePropertyValue(parser, ctxt, "invalidProperty", new Object());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHandleTypePropertyValueWithNoMatchingProperty() throws IOException {
 JsonParser parser = jsonFactory.createParser("{\"property1\": \"typeId\"}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 handler.handleTypePropertyValue(parser, ctxt, "property1", new Object());
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 JsonParser parser = jsonFactory.createParser("{\"property1\": \"typeId1\"}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 handler.handleTypePropertyValue(parser, ctxt, "property1", new Object());
 assertTrue(parser.nextToken() == JsonToken.END_OBJECT);
 }

 @Test
 public void testHandlePropertyValueWithNullValue() throws IOException {
 JsonParser parser = jsonFactory.createParser("{\"property1\": null}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 handler.handlePropertyValue(parser, ctxt, "property1", new Object());
 assertTrue(parser.nextToken() == JsonToken.END_OBJECT);
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 JsonParser parser = jsonFactory.createParser("{\"property1\": \"value1\"}");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 handler.handlePropertyValue(parser, ctxt, "property1", new Object());
 assertTrue(parser.nextToken() == JsonToken.END_OBJECT);
 }

 @Test(expected = IllegalStateException.class)
 public void testCompleteWithMissingTypeId() throws IOException {
 JsonParser parser = jsonFactory.createParser("{ \"property1\": \"value1\" }");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 handler.complete(parser, ctxt, new Object());
 }

 @Test(expected = IllegalStateException.class)
 public void testCompleteWithMissingProperty() throws IOException {
 JsonParser parser = jsonFactory.createParser("{ \"typeId\": \"value1\" }");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 handler.complete(parser, ctxt, new Object());
 }

 @Test
 public void testComplete() throws IOException {
 JsonParser parser = jsonFactory.createParser("{ \"property1\": \"typeId1\", \"property2\": \"typeId2\" }");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 Object result = handler.complete(parser, ctxt, new Object());
 assertNotNull(result);
 }

 @Test
 public void testCompleteWithCreator() throws IOException {
 JsonParser parser = jsonFactory.createParser("{ \"property1\": { \"creatorProperty\": \"typeId1\" }, \"property2\": \"typeId2\" }");
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 Object result = handler.complete(parser, ctxt, buffer, null);
 assertNotNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNullTypeId() throws IOException {
 JsonParser parser = jsonFactory.createParser("{ \"typeId\": null }");
 handler._deserialize(parser, objectMapper.getDeserializationContext(), 0, null);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithEmptyTypeId() throws IOException {
 JsonParser parser = jsonFactory.createParser("{ \"typeId\": \"\" }");
 handler._deserialize(parser, objectMapper.getDeserializationContext(), 0, "");
 }

 @Test
 public void testDeserialize() throws IOException {
 JsonParser parser = jsonFactory.createParser("{ \"typeId\": \"com.foo.Foo\" }");
 Object result = handler._deserialize(parser, objectMapper.getDeserializationContext(), 0, "com.foo.Foo");
 assertNotNull(result);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonParser parser = jsonFactory.createParser("{ \"typeId\": \"com.foo.Foo\", \"property1\": \"value1\" }");
 Object result = new Object();
 handler._deserializeAndSet(parser, objectMapper.getDeserializationContext(), result, 0, "com.foo.Foo");
 assertEquals("value1", result.getClass().getDeclaredField("property1").get(result));
 }
}