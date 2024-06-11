import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ObjectMapper mapper;
 private ExternalTypeHandler handler;
 private JsonNode jsonNode;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder(), null, null, null);
 }

 @Test
 public void testHandleTypePropertyValue_ValidTypeId() throws JsonProcessingException {
 String jsonString = "{\"typeId\": \"StringType\", \"property1\": \"value1\"}";
 jsonNode = mapper.readTree(jsonString);
 handler.start();
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId", null);
 assertEquals("StringType", handler._typeIds[0]);
 }

 @Test
 public void testHandleTypePropertyValue_InvalidTypeId() throws JsonProcessingException {
 String jsonString = "{\"typeId\": \"InvalidTypeId\", \"property1\": \"value1\"}";
 jsonNode = mapper.readTree(jsonString);
 handler.start();
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId", null);
 assertTrue(handler._typeIds[0] == null);
 }

 @Test
 public void testHandlePropertyValue_ValidValue() throws JsonProcessingException {
 String jsonString = "{\"property1\": \"value1\"}";
 jsonNode = mapper.readTree(jsonString);
 handler.start();
 SettableBeanProperty property = new SettableBeanProperty(JsonTestPojo.class, "property1");
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property1", new JsonTestPojo());
 assertEquals("value1", handler.getTestPojo().getProperty1());
 }

 @Test
 public void testHandlePropertyValue_MissingValue() throws JsonProcessingException {
 String jsonString = "{\"property1\": \"\"}";
 jsonNode = mapper.readTree(jsonString);
 handler.start();
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property1", new JsonTestPojo());
 assertNull(handler.getTestPojo().getProperty1());
 }

 @Test
 public void testHandlePropertyValue_MissingProperty() throws JsonProcessingException {
 String jsonString = "{\"typeId\": \"StringType\"}";
 jsonNode = mapper.readTree(jsonString);
 handler.start();
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "nonExistentProperty", new JsonTestPojo());
 assertNull(handler.getTestPojo());
 }

 @Test
 public void testComplete_ValidTypeIds() throws JsonProcessingException {
 String jsonString = "{\"property1\": \"value1\", \"property2\": \"value2\"}";
 jsonNode = mapper.readTree(jsonString);
 handler.start();
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId1", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property1", new JsonTestPojo());
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId2", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property2", new JsonTestPojo());
 Object pojo = handler.complete(jsonNode.asParser(), mapper.getDeserializationContext(), null, null);
 assertEquals("value1", ((JsonTestPojo) pojo).getProperty1());
 assertEquals("value2", ((JsonTestPojo) pojo).getProperty2());
 assertEquals("StringType", handler._typeIds[0]);
 assertEquals("AnotherType", handler._typeIds[1]);
 }

 @Test
 public void testComplete_MissingTypeId() throws JsonProcessingException {
 String jsonString = "{\"property1\": \"value1\", \"property2\": \"value2\"}";
 jsonNode = mapper.readTree(jsonString);
 handler.start();
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property1", new JsonTestPojo());
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property2", new JsonTestPojo());
 Object pojo = handler.complete(jsonNode.asParser(), mapper.getDeserializationContext(), null, null);
 assertNull(pojo);
 }

 @Test
 public void testComplete_MissingProperty() throws JsonProcessingException {
 String jsonString = "{\"typeId1\": \"StringType1\", \"typeId2\": \"StringType2\"}";
 jsonNode = mapper.readTree(jsonString);
 handler.start();
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId1", null);
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId2", null);
 Object pojo = handler.complete(jsonNode.asParser(), mapper.getDeserializationContext(), null, null);
 assertNull(pojo);
 }

 @Test
 public void testComplete_NullValues() throws JsonProcessingException {
 String jsonString = "{\"property1\": null, \"property2\": null}";
 jsonNode = mapper.readTree(jsonString);
 handler.start();
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId1", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property1", new JsonTestPojo());
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId2", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property2", new JsonTestPojo());
 Object pojo = handler.complete(jsonNode.asParser(), mapper.getDeserializationContext(), null, null);
 assertNull(handler.getTestPojo().getProperty1());
 assertNull(handler.getTestPojo().getProperty2());
 }

 @Test
 public void testComplete_ValidValuesWithCreator() throws JsonProcessingException {
 String jsonString = "{\"property1\": { \"creator\": \"Creator1\", \"value\": \"value1\" }, \"property2\": { \"creator\": \"Creator2\", \"value\": \"value2\" }}";
 jsonNode = mapper.readTree(jsonString);
 handler.start();
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId1", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property1.creator", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property1.value", new JsonTestPojo());
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId2", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property2.creator", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property2.value", new JsonTestPojo());
 Object pojo = handler.complete(jsonNode.asParser(), mapper.getDeserializationContext(), null, null);
 assertEquals("value1", handler.getCreator1());
 assertEquals("value2", handler.getCreator2());
 assertEquals("value1", ((JsonTestPojo) pojo).getProperty1());
 assertEquals("value2", ((JsonTestPojo) pojo).getProperty2());
 }

 @Test
 public void testComplete_NullCreator() throws JsonProcessingException {
 String jsonString = "{\"property1\": { \"creator\": null, \"value\": \"value1\" }, \"property2\": { \"creator\": \"Creator2\", \"value\": \"value2\" }}";
 jsonNode = mapper.readTree(jsonString);
 handler.start();
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId1", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property1.value", new JsonTestPojo());
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId2", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property2.creator", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property2.value", new JsonTestPojo());
 Object pojo = handler.complete(jsonNode.asParser(), mapper.getDeserializationContext(), null, null);
 assertNull(handler.getCreator1());
 assertEquals("value2", handler.getCreator2());
 assertEquals("value1", ((JsonTestPojo) pojo).getProperty1());
 assertEquals("value2", ((JsonTestPojo) pojo).getProperty2());
 }

 @Test
 public void testComplete_MissingTypeIdCreator() throws JsonProcessingException {
 String jsonString = "{\"property1\": { \"creator\": \"Creator1\", \"value\": \"value1\" }, \"property2\": \"value2\"}";
 jsonNode = mapper.readTree(jsonString);
 handler.start();
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId1", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property1.creator", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property1.value", new JsonTestPojo());
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId2", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property2", new JsonTestPojo());
 Object pojo = handler.complete(jsonNode.asParser(), mapper.getDeserializationContext(), null, null);
 assertEquals("value2", handler.getCreator2());
 assertEquals("value1", ((JsonTestPojo) pojo).getProperty1());
 assertNull(((JsonTestPojo) pojo).getProperty2());
 }

 @Test
 public void testComplete_MissingPropertyCreator() throws JsonProcessingException {
 String jsonString = "{\"property1\": \"value1\", \"property2\": { \"creator\": \"Creator2\", \"value\": \"value2\" }}";
 jsonNode = mapper.readTree(jsonString);
 handler.start();
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId1", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property1", new JsonTestPojo());
 handler.handleTypePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "typeId2", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property1", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property2.creator", null);
 handler.handlePropertyValue(jsonNode.asParser(), mapper.getDeserializationContext(), "property2.value", new JsonTestPojo());
 Object pojo = handler.complete(jsonNode.asParser(), mapper.getDeserializationContext(), null, null);
 assertEquals("value2", handler.getCreator2());
 assertNull(((JsonTestPojo) pojo).getProperty1());
 assertEquals("value2", ((JsonTestPojo) pojo).getProperty2());
 }

 private JsonTestPojo getTestPojo() {
 return (JsonTestPojo) handler.getBean();
 }

 private String getCreator1() {
 return handler.getTypeDeserializer1().getPropertyName();
 }

 private TypeDeserializer getTypeDeserializer1() {
 return handler._typeDeserializers[0];
 }

 static class JsonTestPojo {
 private String property1;
 private String property2;

 @JsonProperty
 public String getProperty1() {
 return property1;
 }

 @JsonProperty
 public void setProperty1(String property1) {
 this.property1 = property1;
 }

 @JsonProperty
 public String getProperty2() {
 return property2;
 }

 @JsonProperty
 public void setProperty2(String property2) {
 this.property2 = property2;
 }
 }
}