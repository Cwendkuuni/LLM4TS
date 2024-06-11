import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private Object testBean;

 @Before
 public void setup() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 String jsonString = "{\"prop1\":1,\"prop2\":[{\"typeId\":\"MyType\",\"value\":2}]}";
 JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(jsonString.getBytes()));
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), null)
 .addExternal(new SettableBeanProperty("prop2.$[0].value"), Number.class, new MyNumberTypeDeserializer())
 .build()).start();
 testBean = new Object();
 handler.handlePropertyValue(jsonParser, null, "prop1", testBean);
 handler.handlePropertyValue(jsonParser, null, "prop2[0].value", testBean);
 handler.complete(jsonParser, null, testBean);
 }

 @Test
 public void testHandlePropertyValueWithStringType() throws IOException {
 String propName = "prop1";
 String expectedValue = "testString";
 JsonParser jsonParser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(("{\"" + propName + "\":\"" + expectedValue + "\"}").getBytes()));
 handler.handlePropertyValue(jsonParser, null, propName, testBean);
 assertEquals(expectedValue, handler._properties[0].getProperty().getValue(testBean));
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueWithMissingProperty() throws IOException {
 String propName = "missingProperty";
 JsonParser jsonParser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(("{\"prop1\":1}").getBytes()));
 handler.handlePropertyValue(jsonParser, null, propName, testBean);
 fail("Expected IOException but no exception was thrown.");
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueWithMissingTypeIdProperty() throws IOException {
 String propName = "prop2.value";
 JsonParser jsonParser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(("{\"prop1\":1,\"prop2\":1}").getBytes()));
 handler.handlePropertyValue(jsonParser, null, propName, testBean);
 fail("Expected IOException but no exception was thrown.");
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueWithInvalidTypeId() throws IOException {
 String propName = "prop2.value";
 String invalidTypeId = "InvalidTypeId";
 JsonParser jsonParser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(("{\"prop1\":1,\"prop2\":{\"typeId\":\"" + invalidTypeId + "\"}}").getBytes()));
 handler.handlePropertyValue(jsonParser, null, propName, testBean);
 fail("Expected IOException but no exception was thrown.");
 }

 @Test(expected = NullPointerException.class)
 public void testHandlePropertyValueWithNullBean() throws IOException {
 String propName = "prop1";
 JsonParser jsonParser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(("{\"" + propName + "\":\"" + "testString" + "\"}").getBytes()));
 handler.handlePropertyValue(jsonParser, null, propName, null);
 fail("Expected NullPointerException but no exception was thrown.");
 }

 @Test(expected = NullPointerException.class)
 public void testHandlePropertyValueWithNullTokens() throws IOException {
 String propName = "prop2.value";
 handler._tokens[1] = null;
 JsonParser jsonParser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(("{\"prop1\":1,\"prop2\":{\"typeId\":\"MyType\",\"value\":2}}").getBytes()));
 handler.handlePropertyValue(jsonParser, null, propName, testBean);
 fail("Expected NullPointerException but no exception was thrown.");
 }

 @Test
 public void testCompleteWithNoMissingData() throws IOException {
 JsonParser jsonParser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(("{\"prop1\":1,\"prop2\":[{\"typeId\":\"MyType\",\"value\":2}]}").getBytes()));
 handler.complete(jsonParser, null, testBean);
 // Verify that all properties have been assigned values
 for (ExtTypedProperty property : handler._properties) {
 assertEquals(property.getProperty().getName(), testBean.getClass().getDeclaredField(property.getProperty().getName()).get(testBean));
 }
 }

 @Test(expected = IOException.class)
 public void testCompleteWithMissingData() throws IOException {
 JsonParser jsonParser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(("{\"prop1\":1}").getBytes()));
 handler.complete(jsonParser, null, testBean);
 fail("Expected IOException but no exception was thrown.");
 }

 @Test
 public void testCompleteWithNaturalType() throws IOException {
 JsonParser jsonParser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(("{\"prop1\":1,\"prop2\":null}").getBytes()));
 handler.complete(jsonParser, null, testBean);
 assertEquals(null, handler._properties[1].getProperty().getValue(testBean));
 }

 @Test(expected = IOException.class)
 public void testCompleteWithUnassignedTypeId() throws IOException {
 JsonParser jsonParser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(("{\"prop1\":1,\"prop2\":null}").getBytes()));
 handler._typeIds[1] = null;
 handler.complete(jsonParser, null, testBean);
 fail("Expected IOException but no exception was thrown.");
 }

 @Test(expected = IOException.class)
 public void testCompleteWithUnassignedProperty() throws IOException {
 JsonParser jsonParser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(("{\"prop1\":1}").getBytes()));
 handler._tokens[0] = null;
 handler.complete(jsonParser, null, testBean);
 fail("Expected IOException but no exception was thrown.");
 }

 @Test
 public void testCompleteWithCreator() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 MyCreator creator = new MyCreator();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SettableBeanProperty("prop1", String.class), null)
 .addExternal(new SettableBeanProperty("prop2.$[0].value"), Number.class, new MyNumberTypeDeserializer())
 .addCreator(new PropertyBasedCreator() {
 @Override
 public Object createUsingGuid(DeserializationContext ctxt) throws IOException {
 return creator.create(ctxt);
 }
 })
 .build()).start();
 handler.handlePropertyValue(factory.createParser(new ByteArrayInputStream(("{\"prop1\":1,\"prop2\":[{\"typeId\":\"MyType\",\"value\":2}]}").getBytes())), null, "prop1", testBean);
 handler.handlePropertyValue(factory.createParser(new ByteArrayInputStream(("{\"prop1\":1,\"prop2\":[{\"typeId\":\"MyType\",\"value\":2}],\"creator\":{\"creatorClass\":\"com.test.MyCreator\"}}").getBytes())), null, "creator", testBean);
 handler.complete(factory.createParser(new ByteArrayInputStream(("{\"prop1\":1,\"prop2\":[{\"typeId\":\"MyType\",\"value\":2}],\"creator\":null}").getBytes())), null, testBean);
 assertEquals(creator.create(null), handler._properties[2].getProperty().getValue(testBean));
 }

 @Test(expected = IOException.class)
 public void testCompleteWithUnassignedCreator() throws IOException {
 JsonParser jsonParser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(("{\"prop1\":1,\"creator\":{\"creatorClass\":\"com.test.MyCreator\"}}").getBytes()));
 handler.complete(jsonParser, null, testBean);
 fail("Expected IOException but no exception was thrown.");
 }
}

class MyNumberTypeDeserializer extends StdScalarDeserializer {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return NumberDeserializers.parseNumber(p, ctxt);
 }
}

class MyCreator implements PropertyBasedCreator {
 @Override
 public Object createUsingGuid(DeserializationContext ctxt) throws IOException {
 return new Object();
 }

 @Override
 public Object create(DeserializationContext ctxt) throws IOException {
 return new Object();
 }
}