import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializePojoNode() throws Exception {
 Object pojo = new MyPojo("test");
 POJONode node = new POJONode(pojo);

 // Test serialization to JSON
 String json = mapper.writeValueAsString(node);
 JsonNode jsonNode = mapper.readTree(json);
 assertEquals(JsonNodeType.POJO, jsonNode.getNodeType());
 assertEquals(MyPojo.class, mapper.readValue(jsonNode.toString(), MyPojo.class));

 // Test serialization and deserialization roundtrip
 MyPojo deserializedPojo = mapper.readValue(json, MyPojo.class);
 POJONode deserializedNode = mapper.valueToTree(deserializedPojo).deepCopy();
 assertEquals(pojo, deserializedPojo);
 assertEquals(node, deserializedNode);
 }

 @Test
 public void testSerializeNullPojoNode() throws Exception {
 POJONode nullNode = new POJONode(null);

 // Test serialization to JSON
 String json = mapper.writeValueAsString(nullNode);
 JsonNode jsonNode = mapper.readTree(json);
 assertEquals(JsonNodeType.NULL, jsonNode.getNodeType());

 // Test deserialization to null
 assertNull(mapper.readValue(jsonNode.toString(), MyPojo.class));
 }

 @Test
 public void testSerializeByteArrayPojoNode() throws Exception {
 byte[] byteArray = new byte[] { 1, 2, 3 };
 POJONode node = new POJONode(byteArray);

 // Test serialization to JSON
 String json = mapper.writeValueAsString(node);
 JsonNode jsonNode = mapper.readTree(json);
 assertEquals(JsonNodeType.BINARY, jsonNode.getNodeType());
 byte[] deserializedByteArray = jsonNode.binaryValue();
 assertArrayEqual(byteArray, deserializedByteArray);
 }

 @Test(expected = IOException.class)
 public void testSerializeNonSupportedTypePojoNode() throws Exception {
 POJONode node = new POJONode(new Object()); // Not a supported type

 // Test serialization fails
 mapper.writeValueAsString(node); // Should throw an IOException
 }

 // Add test cases for other methods (asText, asText(String), asBoolean, asInt, asLong, asDouble)

}