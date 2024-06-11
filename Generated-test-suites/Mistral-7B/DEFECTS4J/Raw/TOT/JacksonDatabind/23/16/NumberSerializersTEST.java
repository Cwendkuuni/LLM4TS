import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testIntegerSerialization() throws JsonProcessingException {
 Integer value = 123;
 String json = mapper.writeValueAsString(value);
 JsonNode jsonNode = mapper.readTree(json);
 JsonNode expectedNode = mapper.valueToTree(123);
 assertEquals(expectedNode, jsonNode);
 }

 @Test
 public void testLongSerialization() throws JsonProcessingException {
 Long value = 456L;
 String json = mapper.writeValueAsString(value);
 JsonNode jsonNode = mapper.readTree(json);
 JsonNode expectedNode = mapper.valueToTree(456);
 assertEquals(expectedNode, jsonNode);
 }

 @Test
 public void testFloatSerialization() throws JsonProcessingException {
 Float value = 123.45f;
 String json = mapper.writeValueAsString(value);
 JsonNode jsonNode = mapper.readTree(json);
 JsonNode expectedNode = mapper.valueToTree(123.45f);
 assertEquals(expectedNode, jsonNode);
 }

 @Test
 public void testDoubleSerialization() throws JsonProcessingException {
 Double value = 456.789;
 String json = mapper.writeValueAsString(value);
 JsonNode jsonNode = mapper.readTree(json);
 JsonNode expectedNode = mapper.valueToTree(456.789);
 assertEquals(expectedNode, jsonNode);
 }

 @Test
 public void testShortSerialization() throws JsonProcessingException {
 Short value = 127;
 String json = mapper.writeValueAsString(value);
 JsonNode jsonNode = mapper.readTree(json);
 JsonNode expectedNode = mapper.valueToTree(127);
 assertEquals(expectedNode, jsonNode);
 }

 @Test
 public void testNegativeNumberSerialization() throws JsonProcessingException {
 Integer value = -123;
 String json = mapper.writeValueAsString(value);
 JsonNode jsonNode = mapper.readTree(json);
 JsonNode expectedNode = mapper.valueToTree(-123);
 assertEquals(expectedNode, jsonNode);
 }

 @Test
 public void testZeroSerialization() throws JsonProcessingException {
 Integer value = 0;
 String json = mapper.writeValueAsString(value);
 JsonNode jsonNode = mapper.readTree(json);
 JsonNode expectedNode = mapper.valueToTree(0);
 assertEquals(expectedNode, jsonNode);
 }
}