import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

public class POJONodeTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testgetNodeType() {
 POJONode pojoNode = new POJONode("testString");
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 @Test
 public void testAsToken() {
 POJONode pojoNode = new POJONode("testString");
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
 }

 @Test
 public void testBinaryValue() throws IOException {
 byte[] byteArray = {1, 2, 3};
 POJONode pojoNodeWithByteArray = new POJONode(byteArray);
 byte[] result = pojoNodeWithByteArray.binaryValue();
 assertArrayEquals(byteArray, result);
 }

 @Test
 public void testAsText() {
 POJONode pojoNodeWithString = new POJONode("testString");
 assertEquals("testString", pojoNodeWithString.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode pojoNodeWithNullValue = new POJONode(null);
 assertEquals("null", pojoNodeWithNullValue.asText("defaultValue"));
 }

 @Test
 public void testAsBoolean() {
 POJONode pojoNodeWithBooleanTrue = new POJONode(true);
 assertTrue(pojoNodeWithBooleanTrue.asBoolean(false));
 POJONode pojoNodeWithBooleanFalse = new POJONode(false);
 assertFalse(pojoNodeWithBooleanFalse.asBoolean(true));
 }

 @Test
 public void testAsInt() {
 POJONode pojoNodeWithIntValue = new POJONode(123);
 assertEquals(123, pojoNodeWithIntValue.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode pojoNodeWithLongValue = new POJONode(123L);
 assertEquals(123L, pojoNodeWithLongValue.asLong(0L));
 }

 @Test
 public void testAsDouble() {
 POJONode pojoNodeWithDoubleValue = new POJONode(123.45);
 assertEquals(123.45, pojoNodeWithDoubleValue.asDouble(0.0), 0.001);
 }

 @Test
 public void testSerialize() throws IOException {
 POJONode pojoNodeWithStringValue = new POJONode("testString");
 String jsonString = objectMapper.writeValueAsString(pojoNodeWithStringValue);
 assertEquals("\"testString\"", jsonString);
 }

 @Test
 public void testGetPojo() {
 POJONode pojoNodeWithStringValue = new POJONode("testString");
 assertEquals("testString", pojoNodeWithStringValue.getPojo());
 }

 @Test(expected = IOException.class)
 public void testBinaryValueThrowsIOException() throws IOException {
 POJONode pojoNodeWithNonByteArray = new POJONode("nonByteArray");
 pojoNodeWithNonByteArray.binaryValue();
 }

 @Test
 public void testEquals() {
 POJONode pojoNode1 = new POJONode("testString1");
 POJONode pojoNode2 = new POJONode("testString2");
 POJONode pojoNode3 = new POJONode("testString1");
 assertTrue(pojoNode1.equals(pojoNode3));
 assertFalse(pojoNode1.equals(pojoNode2));
 }

 @Test
 public void testHashCode() {
 POJONode pojoNode1 = new POJONode("testString1");
 POJONode pojoNode2 = new POJONode("testString2");
 int expectedHashCode1 = "testString1".hashCode();
 int expectedHashCode2 = "testString2".hashCode();
 assertEquals(expectedHashCode1, pojoNode1.hashCode());
 assertEquals(expectedHashCode2, pojoNode2.hashCode());
 }

 @Test
 public void testToString() {
 POJONode pojoNodeWithStringValue = new POJONode("testString");
 assertEquals("(value of testString)", pojoNodeWithStringValue.toString());
 POJONode pojoNodeWithByteArray = new POJONode(new byte[] {1, 2, 3});
 assertEquals("(binary value of 3 bytes)", pojoNodeWithByteArray.toString());
 POJONode pojoNodeWithRawValue = new POJONode(new RawValue("testString"));
 assertEquals("(raw value 'testString')", pojoNodeWithRawValue.toString());
 }
}