import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 int value = 42;
 String json = objectMapper.writeValueAsString(value);
 assertEquals("42", json);

 Object deserialized = objectMapper.readValue(json, Integer.class);
 assertEquals(value, deserialized);
 }

 @Test
 public void testIntegerSerializerWithMinValue() throws JsonProcessingException {
 int minValue = Integer.MIN_VALUE;
 String json = objectMapper.writeValueAsString(minValue);
 assertEquals("-2147483648", json);

 Object deserialized = objectMapper.readValue(json, Integer.class);
 assertEquals(minValue, deserialized);
 }

 @Test
 public void testIntegerSerializerWithMaxValue() throws JsonProcessingException {
 int maxValue = Integer.MAX_VALUE;
 String json = objectMapper.writeValueAsString(maxValue);
 assertEquals("2147483647", json);

 Object deserialized = objectMapper.readValue(json, Integer.class);
 assertEquals(maxValue, deserialized);
 }

 @Test
 public void testIntegerSerializerWithNegativeValue() throws JsonProcessingException {
 int negativeValue = -42;
 String json = objectMapper.writeValueAsString(negativeValue);
 assertEquals("-42", json);

 Object deserialized = objectMapper.readValue(json, Integer.class);
 assertEquals(negativeValue, deserialized);
 }

 @Test
 public void testIntegerSerializerWithNull() throws JsonProcessingException {
 int value = null;
 String json = objectMapper.writeValueAsString(value);
 assertEquals("null", json);

 Object deserialized = objectMapper.readValue(json, Integer.class);
 assertEquals(null, deserialized);
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 long value = 42L;
 String json = objectMapper.writeValueAsString(value);
 assertEquals("42", json);

 Object deserialized = objectMapper.readValue(json, Long.class);
 assertEquals(value, deserialized);
 }

 @Test
 public void testLongSerializerWithMinValue() throws JsonProcessingException {
 long minValue = Long.MIN_VALUE;
 String json = objectMapper.writeValueAsString(minValue);
 assertEquals("-9223372036854775808", json);

 Object deserialized = objectMapper.readValue(json, Long.class);
 assertEquals(minValue, deserialized);
 }

 @Test
 public void testLongSerializerWithMaxValue() throws JsonProcessingException {
 long maxValue = Long.MAX_VALUE;
 String json = objectMapper.writeValueAsString(maxValue);
 assertEquals("9223372036854775807", json);

 Object deserialized = objectMapper.readValue(json, Long.class);
 assertEquals(maxValue, deserialized);
 }

 @Test
 public void testLongSerializerWithNegativeValue() throws JsonProcessingException {
 long negativeValue = -42L;
 String json = objectMapper.writeValueAsString(negativeValue);
 assertEquals("-42", json);

 Object deserialized = objectMapper.readValue(json, Long.class);
 assertEquals(negativeValue, deserialized);
 }

 @Test
 public void testLongSerializerWithNull() throws JsonProcessingException {
 long value = null;
 String json = objectMapper.writeValueAsString(value);
 assertEquals("null", json);

 Object deserialized = objectMapper.readValue(json, Long.class);
 assertEquals(null, deserialized);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 float value = 42.5f;
 String json = objectMapper.writeValueAsString(value);
 assertEquals("42.5", json);

 Object deserialized = objectMapper.readValue(json, Float.class);
 assertEquals(value, deserialized);
 }

 @Test
 public void testFloatSerializerWithMinValue() throws JsonProcessingException {
 float minValue = Float.MIN_VALUE;
 String json = objectMapper.writeValueAsString(minValue);
 assertEquals("-3.4028235E38", json);

 Object deserialized = objectMapper.readValue(json, Float.class);
 assertEquals(minValue, deserialized);
 }

 @Test
 public void testFloatSerializerWithMaxValue() throws JsonProcessingException {
 float maxValue = Float.MAX_VALUE;
 String json = objectMapper.writeValueAsString(maxValue);
 assertEquals("3.4028235E38", json);

 Object deserialized = objectMapper.readValue(json, Float.class);
 assertEquals(maxValue, deserialized);
 }

 @Test
 public void testFloatSerializerWithNegativeValue() throws JsonProcessingException {
 float negativeValue = -42.5f;
 String json = objectMapper.writeValueAsString(negativeValue);
 assertEquals("-42.5", json);

 Object deserialized = objectMapper.readValue(json, Float.class);
 assertEquals(negativeValue, deserialized);
 }

 @Test
 public void testFloatSerializerWithNull() throws JsonProcessingException {
 float value = null;
 String json = objectMapper.writeValueAsString(value);
 assertEquals("null", json);

 Object deserialized = objectMapper.readValue(json, Float.class);
 assertEquals(null, deserialized);
 }
}