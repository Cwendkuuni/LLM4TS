import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private Object testObject;

 @Before
 public void setUp() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ExtTypedProperty property = new SimplePojo().getClass().getDeclaredField("property")
 .getAnnotation(JsonTypeInfo.As.class);
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(property.typeOnTheServer());
 handler = new ExternalTypeHandler.Builder()
 .addExternal(new SimplePojo().getClass().getDeclaredField("property"), typeDeserializer)
 .build();
 testObject = new SimplePojo();
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("{\"name\":\"typeId\"}");
 DeserializationContext context = new StdDeserializationContext();
 handler.handleTypePropertyValue(jsonParser, context, "name", testObject);
 assertEquals("typeId", handler._typeIds[0]);
 jsonParser.close();
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValue_NullBean() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("{\"name\":\"typeId\"}");
 DeserializationContext context = new StdDeserializationContext();
 handler.handleTypePropertyValue(jsonParser, context, "name", null);
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValue_NullTokens() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("{\"name\":\"typeId\"}");
 DeserializationContext context = new StdDeserializationContext();
 handler.handleTypePropertyValue(jsonParser, context, "name", testObject);
 handler._tokens[0] = null;
 jsonParser.nextToken(); // consume next token to simulate parser progress
 handler.handleTypePropertyValue(jsonParser, context, "name", testObject);
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("{\"property\":\"value\"}");
 DeserializationContext context = new StdDeserializationContext();
 handler.handlePropertyValue(jsonParser, context, "property", testObject);
 assertEquals("value", handler._properties[0].getProperty().get(testObject));
 jsonParser.close();
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValue_NullBean() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("{\"property\":\"value\"}");
 DeserializationContext context = new StdDeserializationContext();
 handler.handlePropertyValue(jsonParser, context, "property", null);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValue_NullTokens() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("{\"property\":\"value\"}");
 DeserializationContext context = new StdDeserializationContext();
 handler.handlePropertyValue(jsonParser, context, "property", testObject);
 handler._tokens[0] = null;
 jsonParser.nextToken(); // consume next token to simulate parser progress
 handler.handlePropertyValue(jsonParser, context, "property", testObject);
 }

 @Test
 public void testComplete() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("{\"typeId1\":\"value1\"}");
 DeserializationContext context = new StdDeserializationContext();
 handler.complete(jsonParser, context, testObject);
 assertEquals("value1", handler._properties[0].getProperty().get(testObject));
 jsonParser.close();
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingTypeId() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("{\"property\":\"value\"}");
 DeserializationContext context = new StdDeserializationContext();
 handler.complete(jsonParser, context, testObject);
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingProperty() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("{\"typeId\":\"value\"}");
 DeserializationContext context = new StdDeserializationContext();
 handler.complete(jsonParser, context, testObject);
 }

 @Test
 public void testComplete_WithCreator() throws Exception {
 PropertyBasedCreator creator = new Creator();
 JsonParser jsonParser = mapper.getFactory().createParser("{" +
         "{\"typeId1\":\"value1\"}," +
         "{\"typeId2\":\"value2\"}" +
         "}");
 DeserializationContext context = new StdDeserializationContext();
 Object result = handler.complete(jsonParser, context, context.constructorArgs(), creator);
 assertEquals(value1.class, result.getClass().getComponentType());
 assertEquals(value2.class, result.getClass().getDeclaredField("value2").getType());
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingTypeIdWithCreator() throws Exception {
 PropertyBasedCreator creator = new Creator();
 JsonParser jsonParser = mapper.getFactory().createParser("{" +
         "{\"property1\":\"value1\"}," +
         "{\"property2\":\"value2\"}" +
         "}");
 DeserializationContext context = new StdDeserializationContext();
 handler.complete(jsonParser, context, context.constructorArgs(), creator);
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingPropertyWithCreator() throws Exception {
 PropertyBasedCreator creator = new Creator();
 JsonParser jsonParser = mapper.getFactory().createParser("{" +
         "{\"typeId1\":\"value1\"}," +
         "{\"typeId2\":\"value2\"}" +
         "}");
 DeserializationContext context = new StdDeserializationContext();
 handler.complete(jsonParser, context, context.constructorArgs(), creator);
 }

 @Test
 public void testComplete_NullParser() throws Exception {
 Object result = handler.complete(null, new StdDeserializationContext(), testObject);
 assertNull(result);
 }
}

class SimplePojo {
 String property;
}

class Creator extends PropertyBasedCreator<Object> {
 @Override
 public Object buildInstance(DeserializationContext ctxt, PropertyName propertyName,
         JsonParser p) throws IOException {
 Object result = new Object[2];
 result[0] = mapper.readValue(p, value1.class);
 result[1] = mapper.readValue(p, value2.class);
 return result;
 }
}