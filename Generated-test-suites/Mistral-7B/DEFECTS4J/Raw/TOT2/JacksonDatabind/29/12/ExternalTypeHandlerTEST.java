import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.fasterxml.jackson.databind.deser.impl.ExtTypedProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private Builder builder;

 @Before
 public void setup() {
 builder = new Builder();
 ObjectNode jsonNode = ObjectMapper.getDefaultInstance().createObjectNode();
 jsonNode.put("name", "John Doe");
 jsonNode.put("type", "Person");
 handler = builder.addExternal(JsonDeserializer.findBeanDeserializer(Person.class), new PersonDeserializer())
 .addExternal(JsonDeserializer.findBeanDeserializer(Address.class), new AddressDeserializer())
 .build();
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 JsonParser jp = ObjectMapper.getDefaultInstance().getFactory().createParser(handler.getClass().getResource("/test.json"));
 handler.handleTypePropertyValue(jp, new TestContext(), "type", null);
 assertEquals("Person", handler.getTypeIds()[0]);
 }

 @Test
 public void testHandlePropertyValue_PropertyExists() throws Exception {
 JsonParser jp = ObjectMapper.getDefaultInstance().getFactory().createParser(handler.getClass().getResource("/test.json"));
 handler.handlePropertyValue(jp, new TestContext(), "name", new Object());
 assertEquals("John Doe", handler.getProperty(0).get(new Object()));
 }

 @Test
 public void testHandlePropertyValue_PropertyDoesNotExist() throws Exception {
 JsonParser jp = ObjectMapper.getDefaultInstance().getFactory().createParser(handler.getClass().getResource("/test.json"));
 handler.handlePropertyValue(jp, new TestContext(), "age", new Object());
 assertNull(handler.getProperty(0).get(new Object()));
 }

 @Test
 public void testHandlePropertyValue_TokenIsNull() throws Exception {
 JsonParser jp = ObjectMapper.getDefaultInstance().getFactory().createParser(handler.getClass().getResource("/test.json"));
 handler.handlePropertyValue(jp, new TestContext(), "type", null);
 assertNull(handler.getTypeIds()[0]);
 }

 @Test
 public void testHandlePropertyValue_BeanIsNull() throws Exception {
 JsonParser jp = ObjectMapper.getDefaultInstance().getFactory().createParser(handler.getClass().getResource("/test.json"));
 handler.handlePropertyValue(jp, new TestContext(), "type", null);
 assertNull(handler.getTypeIds()[0]);
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValue_TypeDeserializerThrowsException() throws Exception {
 builder.addExternal(JsonDeserializer.findBeanDeserializer(Person.class), new PersonDeserializerWithException());
 handler = builder.build();
 JsonParser jp = ObjectMapper.getDefaultInstance().getFactory().createParser(handler.getClass().getResource("/test.json"));
 handler.handlePropertyValue(jp, new TestContext(), "type", new Object());
 }

 @Test
 public void testComplete() throws Exception {
 Object result = handler.complete(ObjectMapper.getDefaultInstance().getFactory().createParser(handler.getClass().getResource("/test.json")), new TestContext(), new Object());
 assertNotNull(result);
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingExternalTypeId() throws Exception {
 JsonParser jp = ObjectMapper.getDefaultInstance().getFactory().createParser(new ByteArrayInputStream("{\"name\": \"John Doe\"}".getBytes()));
 handler.complete(jp, new TestContext(), new Object());
 }

 @Test(expected = IOException.class)
 public void testComplete_MissingProperty() throws Exception {
 JsonParser jp = ObjectMapper.getDefaultInstance().getFactory().createParser(new ByteArrayInputStream("{\"type\": \"Person\"}".getBytes()));
 handler.complete(jp, new TestContext(), new Object());
 }

 @Test(expected = IOException.class)
 public void testComplete_NullExternalTypeId() throws Exception {
 JsonParser jp = ObjectMapper.getDefaultInstance().getFactory().createParser(new ByteArrayInputStream("{\"type\": null}".getBytes()));
 handler.complete(jp, new TestContext(), new Object());
 }

 private class TestContext extends DeserializationContext {
 @Override public ObjectDeserializer getDeserializer(Class<?> c) { return null; }
 @Override public ObjectDeserializer getDeserializer(JsonDeserializer deserializer) { return deserializer; }
 @Override public ObjectDeserializer getRootNameDeserializer() { return null; }
 @Override public ObjectDeserializer getValueDeserializer(Class<?> type, JsonDeserializer deserializer) { return deserializer; }
 @Override public BeanDescription getActiveContext() { return null; }
 @Override public void reportError(String msg) { }
 @Override public void reportPluginError(String msg) { }
 @Override public void reportError(Throwable e) { }
 @Override public void reportPluginError(Throwable e) { }
 }

 private static class PersonDeserializer extends StdDeserializer<Person> {
 @Override public Person deserialize(JsonParser p, DeserializationContext ctxt) throws IOException { return new Person(); }
 }

 private static class PersonDeserializerWithException extends StdDeserializer<Person> {
 @Override public Person deserialize(JsonParser p, DeserializationContext ctxt) throws IOException { throw new IOException("Test exception"); }
 }

 private static class PersonDeserializerWithNullValue extends StdDeserializer<Person> {
 @Override public Person deserialize(JsonParser p, DeserializationContext ctxt) throws IOException { return null; }
 }

 private static class PersonDeserializerWithDefaultValue extends StdDeserializer<Person> {
 @Override public Person deserialize(JsonParser p, DeserializationContext ctxt) throws IOException { return new Person(); }

 @Override public PropertyName getPropertyName() { return PropertyName.find("defaultValue", Person.class); }
 }

 private static class Person {
 public static final PropertyName TYPE_PROPERTY = PropertyName.find("type", Person.class);
 public static final PropertyName NAME_PROPERTY = PropertyName.find("name", Person.class);
 }

 private static class Address {
 public static final PropertyName TYPE_PROPERTY = PropertyName.find("type", Address.class);
 public static final PropertyName STREET_PROPERTY = PropertyName.find("street", Address.class);
 }

 private static class PersonDeserializerWithException extends JsonDeserializer<Person> {
 @Override public Person deserialize(JsonParser p, DeserializationContext ctxt) throws IOException { throw new IOException("Test exception"); }
 }

 private static class PersonDeserializerWithNullValue extends JsonDeserializer<Person> {
 @Override public Person deserialize(JsonParser p, DeserializationContext ctxt) throws IOException { return null; }
 }

 private static class PersonDeserializerWithDefaultValue extends JsonDeserializer<Person> {
 @Override public Person deserialize(JsonParser p, DeserializationContext ctxt) throws IOException { return new Person(); }

 @Override public PropertyName getPropertyName() { return PropertyName.construct("defaultValue", Person.class); }
 }
}