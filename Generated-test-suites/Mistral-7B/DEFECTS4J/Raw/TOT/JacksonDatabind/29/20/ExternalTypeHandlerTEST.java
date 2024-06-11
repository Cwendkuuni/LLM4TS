import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExtTypedProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.PropertyNameSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ObjectNode rootNode;
 private ExternalTypeHandler handler;

 @Before
 public void setup() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 GenericSerializableTypeResolver typeResolver = new GenericSerializableTypeResolver();
 mapper.setTypeResolver(typeResolver);
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.registerModule(new SimpleModule() {{
 setSerializerFactory(this.constructorArgs(), new PropertyNameSerializer(null));
 }} );
 PropertyNameSerializer propertyNameSerializer = new PropertyNameSerializer(null);
 mapper.setSerializerFactory(PropertyNameSerializer.class, propertyNameSerializer);
 ObjectNode rootNode = mapper.createObjectNode();
 // Create a test class with external type handler
 class TestClass {
 private String prop1;
 private List<String> prop2;
 @JsonTypeIdResolver
 public static String idFromValueAndType(Object value, Class<?> type) {
 return type.getName();
 }
 }
 Builder builder = new Builder();
 SettableBeanProperty prop1 = new SettableBeanProperty("prop1", TestClass.class, null);
 TypeDeserializer typeDeserializer1 = mapper.deserializerForClass(TestClass.class);
 builder.addExternal(prop1, typeDeserializer1);
 SettableBeanProperty prop2 = new SettableBeanProperty("prop2", List.class, null);
 TypeDeserializer typeDeserializer2 = mapper.deserializerForCollectionType(List.class, String.class);
 builder.addExternal(prop2, typeDeserializer2);
 handler = builder.build();
 }

 @Test
 public void testHandleTypePropertyValue() throws JsonProcessingException {
 ObjectNode node = mapper.createObjectNode();
 node.put("prop1", "TestClass");
 node.put("prop2", "[\"test1\",\"test2\"]");
 JsonParser parser = mapper.parserForObject(node);
 handler.handleTypePropertyValue(parser, null, "prop1", null);
 assertEquals("TestClass", handler.getProperties()[0].getProperty().getValue(handler.getBean()));
 handler.handleTypePropertyValue(parser, null, "prop2", null);
 assertEquals("[test1, test2]", handler.getProperties()[1].getProperty().getValue(handler.getBean()));
 }

 @Test
 public void testHandlePropertyValue() throws JsonProcessingException {
 ObjectNode node = mapper.createObjectNode();
 node.put("prop1", "TestClass");
 node.put("prop2", "[\"test1\",\"test2\"]");
 JsonParser parser = mapper.parserForObject(node);
 handler.handlePropertyValue(parser, null, "prop1", null);
 assertNull(handler.getProperties()[0].getProperty().getValue(handler.getBean()));
 handler.handlePropertyValue(parser, null, "prop2", null);
 assertEquals("[test1, test2]", handler.getProperties()[1].getProperty().getValue(handler.getBean()));
 }

 @Test
 public void testComplete() throws JsonProcessingException {
 ObjectNode node = mapper.createObjectNode();
 node.put("prop1", null);
 node.put("prop2", "[\"test1\",\"test2\"]");
 JsonParser parser = mapper.parserForObject(node);
 handler.start();
 handler.handlePropertyValue(parser, null, "prop1", null);
 handler.handlePropertyValue(parser, null, "prop2", null);
 Object bean = handler.complete(parser, null, null);
 assertEquals("TestClass", handler.getProperties()[0].getProperty().getValue(bean));
 assertEquals("[test1, test2]", handler.getProperties()[1].getProperty().getValue(bean));
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingTypeIdProperty() throws JsonProcessingException {
 ObjectNode node = mapper.createObjectNode();
 node.put("prop1", "TestClass");
 JsonParser parser = mapper.parserForObject(node);
 handler.start();
 handler.handlePropertyValue(parser, null, "prop1", null);
 handler.complete(parser, null, null);
 fail("Expected JsonMappingException but got none");
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingPropertyForTypeId() throws JsonProcessingException {
 ObjectNode node = mapper.createObjectNode();
 node.put("prop1", "TestClass");
 node.put("prop2", "{});"); // empty JSON object
 JsonParser parser = mapper.parserForObject(node);
 handler.start();
 handler.handlePropertyValue(parser, null, "prop1", null);
 handler.complete(parser, null, null);
 fail("Expected JsonMappingException but got none");
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingExternalTypeId() throws JsonProcessingException {
 ObjectNode node = mapper.createObjectNode();
 node.put("prop1", "TestClass");
 JsonParser parser = mapper.parserForObject(node);
 handler.start();
 handler.handlePropertyValue(parser, null, "prop1", null);
 handler.handlePropertyValue(parser, null, "prop2", null);
 Object bean = handler.complete(parser, null, null);
 SettableBeanProperty prop = new SettableBeanProperty("prop3", String.class, null);
 prop.set(bean, "test");
 fail("Expected JsonMappingException but got none");
 }

 private ExternalTypeHandler.Property[] getProperties() {
 return handler.getProperties();
 }
}