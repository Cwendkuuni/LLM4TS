import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer integerValue = 123;
 String json = objectMapper.writeValueAsString(integerValue);
 assertEquals("123", json);
 }

 @Test
 public void testIntegerSerializerWithNegativeValue() throws JsonProcessingException {
 Integer negativeIntegerValue = -123;
 String json = objectMapper.writeValueAsString(negativeIntegerValue);
 assertEquals("-123", json);
 }

 @Test
 public void testIntegerSerializerWithMaxValue() throws JsonProcessingException {
 Integer maxIntegerValue = Integer.MAX_VALUE;
 String json = objectMapper.writeValueAsString(maxIntegerValue);
 assertEquals(Integer.toString(Integer.MAX_VALUE), json);
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerWithStringValue() throws JsonProcessingException {
 Integer integerValue = 123;
 String jsonString = "{\"number\": \"test\"}";
 String result = objectMapper.readValue(jsonString, Integer.class);
 fail("Expected JsonMappingException when trying to deserialize a string to Integer");
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerWithNullValue() throws JsonProcessingException {
 String json = "null";
 Integer result = objectMapper.readValue(json, Integer.class);
 fail("Expected JsonMappingException when trying to deserialize null to Integer");
 }

 @Test
 public void testIntegerSerializerWithJsonFormat() throws JsonProcessingException {
 JsonFormat.Feature features = JsonFormat.Feature.INTEGER_FOR_INTS;
 ObjectWriter writer = objectMapper.writer().withFeatures(features);
 String json = writer.writeValueAsString(123);
 assertEquals("123", json);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float floatValue = 123.45f;
 String json = objectMapper.writeValueAsString(floatValue);
 assertEquals("123.45", json);
 }

 @Test
 public void testFloatSerializerWithNegativeValue() throws JsonProcessingException {
 Float negativeFloatValue = -123.45f;
 String json = objectMapper.writeValueAsString(negativeFloatValue);
 assertEquals("-123.45", json);
 }

 @Test
 public void testFloatSerializerWithMaxValue() throws JsonProcessingException {
 Float maxFloatValue = Float.MAX_VALUE;
 String json = objectMapper.writeValueAsString(maxFloatValue);
 assertEquals(Float.toString(Float.MAX_VALUE), json);
 }

 @Test(expected = JsonMappingException.class)
 public void testFloatSerializerWithStringValue() throws JsonProcessingException {
 Float floatValue = 123.45f;
 String jsonString = "{\"number\": \"test\"}";
 Float result = objectMapper.readValue(jsonString, Float.class);
 fail("Expected JsonMappingException when trying to deserialize a string to Float");
 }

 @Test(expected = JsonMappingException.class)
 public void testFloatSerializerWithNullValue() throws JsonProcessingException {
 String json = "null";
 Float result = objectMapper.readValue(json, Float.class);
 fail("Expected JsonMappingException when trying to deserialize null to Float");
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double doubleValue = 123.45;
 String json = objectMapper.writeValueAsString(doubleValue);
 assertEquals("123.45", json);
 }

 @Test
 public void testDoubleSerializerWithNegativeValue() throws JsonProcessingException {
 Double negativeDoubleValue = -123.45;
 String json = objectMapper.writeValueAsString(negativeDoubleValue);
 assertEquals("-123.45", json);
 }

 @Test
 public void testDoubleSerializerWithMaxValue() throws JsonProcessingException {
 Double maxDoubleValue = Double.MAX_VALUE;
 String json = objectMapper.writeValueAsString(maxDoubleValue);
 assertEquals(Double.toString(Double.MAX_VALUE), json);
 }

 @Test(expected = JsonMappingException.class)
 public void testDoubleSerializerWithStringValue() throws JsonProcessingException {
 Double doubleValue = 123.45;
 String jsonString = "{\"number\": \"test\"}";
 Double result = objectMapper.readValue(jsonString, Double.class);
 fail("Expected JsonMappingException when trying to deserialize a string to Double");
 }

 @Test(expected = JsonMappingException.class)
 public void testDoubleSerializerWithNullValue() throws JsonProcessingException {
 String json = "null";
 Double result = objectMapper.readValue(json, Double.class);
 fail("Expected JsonMappingException when trying to deserialize null to Double");
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short shortValue = (short) 123;
 String json = objectMapper.writeValueAsString(shortValue);
 assertEquals("123", json);
 }

 @Test
 public void testShortSerializerWithNegativeValue() throws JsonProcessingException {
 Short negativeShortValue = (short) -123;
 String json = objectMapper.writeValueAsString(negativeShortValue);
 assertEquals("-123", json);
 }

 @Test
 public void testShortSerializerWithMaxValue() throws JsonProcessingException {
 Short maxShortValue = Short.MAX_VALUE;
 String json = objectMapper.writeValueAsString(maxShortValue);
 assertEquals(Short.toString(Short.MAX_VALUE), json);
 }

 @Test(expected = JsonMappingException.class)
 public void testShortSerializerWithStringValue() throws JsonProcessingException {
 Short shortValue = (short) 123;
 String jsonString = "{\"number\": \"test\"}";
 Short result = objectMapper.readValue(jsonString, Short.class);
 fail("Expected JsonMappingException when trying to deserialize a string to Short");
 }

 @Test(expected = JsonMappingException.class)
 public void testShortSerializerWithNullValue() throws JsonProcessingException {
 String json = "null";
 Short result = objectMapper.readValue(json, Short.class);
 fail("Expected JsonMappingException when trying to deserialize null to Short");
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long longValue = 123L;
 String json = objectMapper.writeValueAsString(longValue);
 assertEquals("123", json);
 }

 @Test
 public void testLongSerializerWithNegativeValue() throws JsonProcessingException {
 Long negativeLongValue = -123L;
 String json = objectMapper.writeValueAsString(negativeLongValue);
 assertEquals("-123", json);
 }

 @Test
 public void testLongSerializerWithMaxValue() throws JsonProcessingException {
 Long maxLongValue = Long.MAX_VALUE;
 String json = objectMapper.writeValueAsString(maxLongValue);
 assertEquals(Long.toString(Long.MAX_VALUE), json);
 }

 @Test(expected = JsonMappingException.class)
 public void testLongSerializerWithStringValue() throws JsonProcessingException {
 Long longValue = 123L;
 String jsonString = "{\"number\": \"test\"}";
 Long result = objectMapper.readValue(jsonString, Long.class);
 fail("Expected JsonMappingException when trying to deserialize a string to Long");
 }

 @Test(expected = JsonMappingException.class)
 public void testLongSerializerWithNullValue() throws JsonProcessingException {
 String json = "null";
 Long result = objectMapper.readValue(json, Long.class);
 fail("Expected JsonMappingException when trying to deserialize null to Long");
 }
}