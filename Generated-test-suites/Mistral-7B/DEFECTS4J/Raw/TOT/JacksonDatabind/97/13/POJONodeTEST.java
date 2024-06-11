import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class POJONodeTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testPOJONodeSerialization() throws JsonProcessingException {
 Object pojo = new MyPojo("Test");
 POJONode node = new POJONode(pojo);
 JsonNode jsonNode = mapper.valueToTree(node);
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
 assertEquals("{\"_class\":\"com.fasterxml.jackson.databind.node.POJONode\",\"_value\":{\"value\":\"Test\"}}", jsonString);
 }

 @Test
 public void testPOJONodeSerializationWithJsonSerializable() {
 MySerializablePojo jsonSerializablePojo = new MySerializablePojo("Test");
 POJONode node = new POJONode(jsonSerializablePojo);
 JsonNode jsonNode = mapper.valueToTree(node);
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
 assertEquals("{\"_class\":\"com.fasterxml.jackson.databind.node.POJONode\",\"_value\":{\"value\":{\"Test\":\"Test\"}}}", jsonString);
 }

 @Test
 public void testPOJONodeBinaryValue() throws IOException {
 byte[] binaryData = new byte[] { 0x00, 0x01, 0x02 };
 POJONode node = new POJONode(binaryData);
 byte[] actualBinaryData = node.binaryValue();
 assertArrayEquals(binaryData, actualBinaryData);
 }

 @Test
 public void testPOJONodeNullValue() {
 POJONode node = new POJONode(null);
 assertNull(node.asText());
 assertNull(node.asBoolean(false));
 assertNull(node.asInt(0));
 assertNull(node.asLong(0L));
 assertNull(node.asDouble(0.0));
 }

 @Test
 public void testPOJONodeAsText() {
 String pojoString = "Test";
 POJONode node = new POJONode(pojoString);
 assertEquals("Test", node.asText());
 }

 @Test
 public void testPOJONodeAsTextWithDefaultValue() {
 String pojoString = null;
 POJONode node = new POJONode(pojoString);
 assertEquals("null", node.asText("defaultValue"));
 }

 @Test
 public void testPOJONodeAsBoolean() {
 boolean booleanValue = true;
 POJONode node = new POJONode(booleanValue);
 assertTrue(node.asBoolean(false));
 }

 @Test
 public void testPOJONodeAsBooleanWithDefaultValue() {
 POJONode node = new POJONode(null);
 assertFalse(node.asBoolean(true));
 }

 @Test
 public void testPOJONodeAsInt() {
 int intValue = 1;
 POJONode node = new POJONode(intValue);
 assertEquals(intValue, node.asInt(0));
 }

 @Test
 public void testPOJONodeAsIntWithDefaultValue() {
 POJONode node = new POJONode(null);
 assertEquals(0, node.asInt(1));
 }

 @Test
 public void testPOJONodeAsLong() {
 long longValue = 1L;
 POJONode node = new POJONode(longValue);
 assertEquals(longValue, node.asLong(0L));
 }

 @Test
 public void testPOJONodeAsLongWithDefaultValue() {
 POJONode node = new POJONode(null);
 assertEquals(0L, node.asLong(1L));
 }

 @Test
 public void testPOJONodeAsDouble() {
 double doubleValue = 1.0;
 POJONode node = new POJONode(doubleValue);
 assertEquals(doubleValue, node.asDouble(0.0), 0.01);
 }

 @Test
 public void testPOJONodeAsDoubleWithDefaultValue() {
 POJONode node = new POJONode(null);
 assertEquals(0.0, node.asDouble(1.0), 0.01);
 }
}

class MyPojo {
 private String value;

 MyPojo(String value) {
 this.value = value;
 }

 @Override
 public String toString() {
 return value;
 }
}

class MySerializablePojo implements JsonSerializable {
 private String value;

 MySerializablePojo(String value) {
 this.value = value;
 }

 @Override
 public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeFieldName("value");
 gen.writeString(value);
 }
}