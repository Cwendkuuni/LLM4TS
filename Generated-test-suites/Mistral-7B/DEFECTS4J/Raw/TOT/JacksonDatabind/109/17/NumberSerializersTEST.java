import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 /**
 * Test for IntegerSerializer
 */
 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer integer = 123;
 String json = "{\"number\":123}";

 // Test serialization
 JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(integer));
 assertEquals(json, actual.toString());

 // Test deserialization
 Integer deserialized = objectMapper.readValue(json, Integer.class);
 assertEquals(integer, deserialized);
 }

 /**
 * Test for IntegerSerializer with negative number
 */
 @Test
 public void testIntegerSerializerNegative() throws JsonProcessingException {
 Integer negativeNumber = -123;
 String json = "{\"number\":-123}";

 // Test serialization
 JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(negativeNumber));
 assertEquals(json, actual.toString());

 // Test deserialization
 Integer deserialized = objectMapper.readValue(json, Integer.class);
 assertEquals(negativeNumber, deserialized);
 }

 /**
 * Test for IntegerSerializer with maximum value
 */
 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerMaxValue() throws JsonProcessingException {
 Integer maxValue = Integer.MAX_VALUE;
 String json = "{\"number\":2147483647}"; // Max value for int

 // Test serialization
 JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(maxValue));
 assertEquals(json, actual.toString());

 // Test deserialization
 // This should throw an exception as the maximum value for int cannot be represented as a JsonNumber
 Object deserialized = objectMapper.readValue(json, Integer.class);
 fail("Should have thrown JsonMappingException");
 }

 /**
 * Test for IntegerSerializer with minimum value
 */
 @Test
 public void testIntegerSerializerMinValue() throws JsonProcessingException {
 Integer minValue = Integer.MIN_VALUE;
 String json = "{\"number\":-2147483648}"; // Min value for int

 // Test serialization
 JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(minValue));
 assertEquals(json, actual.toString());

 // Test deserialization
 Integer deserialized = objectMapper.readValue(json, Integer.class);
 assertEquals(minValue, deserialized);
 }

 /**
 * Test for LongSerializer
 */
 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long longNumber = 123L;
 String json = "{\"number\":123}";

 // Test serialization
 JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(longNumber));
 assertEquals(json, actual.toString());

 // Test deserialization
 Long deserialized = objectMapper.readValue(json, Long.class);
 assertEquals(longNumber, deserialized);
 }

 /**
 * Test for LongSerializer with negative number
 */
 @Test
 public void testLongSerializerNegative() throws JsonProcessingException {
 Long negativeLongNumber = -123L;
 String json = "{\"number\":-123}";

 // Test serialization
 JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(negativeLongNumber));
 assertEquals(json, actual.toString());

 // Test deserialization
 Long deserialized = objectMapper.readValue(json, Long.class);
 assertEquals(negativeLongNumber, deserialized);
 }

 /**
 * Test for LongSerializer with maximum value
 */
 @Test(expected = JsonMappingException.class)
 public void testLongSerializerMaxValue() throws JsonProcessingException {
 Long maxValue = Long.MAX_VALUE;
 String json = "{\"number\":9223372036854775807}"; // Max value for long

 // Test serialization
 JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(maxValue));
 assertEquals(json, actual.toString());

 // Test deserialization
 // This should throw an exception as the maximum value for long cannot be represented as a JsonNumber
 Object deserialized = objectMapper.readValue(json, Long.class);
 fail("Should have thrown JsonMappingException");
 }

 /**
 * Test for LongSerializer with minimum value
 */
 @Test
 public void testLongSerializerMinValue() throws JsonProcessingException {
 Long minValue = Long.MIN_VALUE;
 String json = "{\"number\":-9223372036854775808}"; // Min value for long

 // Test serialization
 JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(minValue));
 assertEquals(json, actual.toString());

 // Test deserialization
 Long deserialized = objectMapper.readValue(json, Long.class);
 assertEquals(minValue, deserialized);
 }

 /**
 * Test for FloatSerializer
 */
 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float floatNumber = 123.45F;
 String json = "{\"number\":123.45}";

 // Test serialization
 JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(floatNumber));
 assertEquals(json, actual.toString());

 // Test deserialization
 Float deserialized = objectMapper.readValue(json, Float.class);
 assertEquals(floatNumber, deserialized);
 }

 /**
 * Test for FloatSerializer with negative number
 */
 @Test
 public void testFloatSerializerNegative() throws JsonProcessingException {
 Float negativeFloatNumber = -123.45F;
 String json = "{\"number\":-123.45}";

 // Test serialization
 JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(negativeFloatNumber));
 assertEquals(json, actual.toString());

 // Test deserialization
 Float deserialized = objectMapper.readValue(json, Float.class);
 assertEquals(negativeFloatNumber, deserialized);
 }

 /**
 * Test for FloatSerializer with maximum value
 */
 @Test(expected = JsonMappingException.class)
 public void testFloatSerializerMaxValue() throws JsonProcessingException {
 Float maxValue = Float.MAX_VALUE;
 String json = "{\"number\":3.402823E38}"; // Max value for float

 // Test serialization
 JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(maxValue));
 assertEquals(json, actual.toString());

 // Test deserialization
 // This should throw an exception as the maximum value for float cannot be represented as a JsonNumber
 Object deserialized = objectMapper.readValue(json, Float.class);
 fail("Should have thrown JsonMappingException");
 }

 /**
 * Test for FloatSerializer with minimum value
 */
 @Test
 public void testFloatSerializerMinValue() throws JsonProcessingException {
 Float minValue = Float.MIN_VALUE;
 String json = "{\"number\":-3.402823E38}"; // Min value for float

 // Test serialization
 JsonNode actual = objectMapper.readTree(objectMapper.writeValueAsString(minValue));
 assertEquals(json, actual.toString());

 // Test deserialization
 Float deserialized = objectMapper.readValue(json, Float.class);
 assertEquals(minValue, deserialized);
 }
}