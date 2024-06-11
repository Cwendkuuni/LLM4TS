import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer number = 123;
 ObjectNode result = mapper.writeValueAsTree(number);
 JsonNode jsonNode = result.get("1");
 assertEquals("123", jsonNode.asText());
 }

 @Test
 public void testIntegerSerializerWithNegativeNumber() throws JsonProcessingException {
 Integer negativeNumber = -123;
 ObjectNode result = mapper.writeValueAsTree(negativeNumber);
 JsonNode jsonNode = result.get("1");
 assertEquals("-123", jsonNode.asText());
 }

 @Test
 public void testIntegerSerializerWithLargeNumber() throws JsonProcessingException {
 Long largeNumber = Long.MAX_VALUE;
 mapper.configure(SerializationFeature.FAIL_ON_EmptyBeans, false);
 ObjectNode result = mapper.writeValueAsTree(largeNumber);
 JsonNode jsonNode = result.get("1");
 assertEquals(Long.toString(largeNumber), jsonNode.asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerWithString() throws JsonProcessingException {
 String string = "notANumber";
 ObjectNode result = mapper.writeValueAsTree(string);
 mapper.readValue(result.toString(), Integer.class);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short shortNumber = 127;
 ObjectNode result = mapper.writeValueAsTree(shortNumber);
 JsonNode jsonNode = result.get("1");
 assertEquals("127", jsonNode.asText());
 }

 // Add other test cases for LongSerializer, FloatSerializer, DoubleSerializer
 // and other methods in the NumberSerializers class as needed

 @Test
 public void testIntegerSerializerWithJsonFormat() throws JsonProcessingException {
 Integer number = 123;
 ObjectNode rootNode = mapper.createObjectNode();
 ObjectNode numberNode = mapper.valueToTree(number);
 rootNode.set("intNumber", numberNode);
 JsonNode schemaNode = mapper.readTree("{\"type\":\"number\",\"format\":\"int32\"}");
 ObjectNode node = mapper.readTree(mapper.writerWithView(JsonFormat.Std.INT_32)
 .writeValueAsString(rootNode));
 JsonNode actualFormatNode = node.get("intNumber");
 ObjectNode expectedFormatNode = mapper.readerWithView(schemaNode)
 .readValueAsTree("{\"int\":123}");
 assertNodesEqual(actualFormatNode, expectedFormatNode);
 }

 private void assertNodesEqual(JsonNode actual, JsonNode expected) {
 if (!actual.isMissingNode() && !expected.isMissingNode()) {
 assertEquals("Nodes differ: " + actual.toString() + " vs " + expected.toString(),
 expected.deepEqual(actual));
 }
 }

}