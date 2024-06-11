import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer input = 123;
 String json = "123";
 String expectedJson = "\"123\"";

 // Test serialization
 String output = objectMapper.writeValueAsString(input);
 assertEquals(expectedJson, output);

 // Test deserialization
 Integer deserializedInput = objectMapper.readValue(json, Integer.class);
 assertEquals(input, deserializedInput);
 }

 @Test
 public void testIntegerSerializerNegativeNumber() throws JsonProcessingException {
 Integer input = -123;
 String json = "-123";
 String expectedJson = "\"-123\"";

 // Test serialization
 String output = objectMapper.writeValueAsString(input);
 assertEquals(expectedJson, output);

 // Test deserialization
 Integer deserializedInput = objectMapper.readValue(json, Integer.class);
 assertEquals(input, deserializedInput);
 }

 @Test
 public void testIntegerSerializerZero() throws JsonProcessingException {
 Integer input = 0;
 String json = "0";
 String expectedJson = "\"0\"";

 // Test serialization
 String output = objectMapper.writeValueAsString(input);
 assertEquals(expectedJson, output);

 // Test deserialization
 Integer deserializedInput = objectMapper.readValue(json, Integer.class);
 assertEquals(input, deserializedInput);
 }

 @Test(expected = JsonProcessingException.class)
 public void testIntegerSerializerInvalidInput() throws JsonProcessingException {
 Integer input = 123;
 String json = "[\"invalid\"]";

 // Test serialization (expecting exception)
 String output = objectMapper.writeValueAsString(input);
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short input = (short) 123;
 String json = "123";
 String expectedJson = "\"123\"";

 // Test serialization
 String output = objectMapper.writeValueAsString(input);
 assertEquals(expectedJson, output);

 // Test deserialization
 Short deserializedInput = objectMapper.readValue(json, Short.class);
 assertEquals(input, deserializedInput);
 }

 // ... Add other test cases for LongSerializer, FloatSerializer, DoubleSerializer, etc.

}