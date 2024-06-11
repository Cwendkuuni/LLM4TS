import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberSerializersTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer value = 123;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(123, jsonNode.asInt());
 }

 @Test
 public void testIntegerSerializerWithNegativeNumber() throws JsonProcessingException {
 Integer value = -123;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(-123, jsonNode.asInt());
 }

 @Test
 public void testIntegerSerializerWithZero() throws JsonProcessingException {
 Integer value = 0;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(0, jsonNode.asInt());
 }

 @Test
 public void testIntegerSerializerWithMaxValue() throws JsonProcessingException {
 Integer value = Integer.MAX_VALUE;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(Integer.MAX_VALUE, jsonNode.asInt());
 }

 @Test
 public void testIntegerSerializerWithMinValue() throws JsonProcessingException {
 Integer value = Integer.MIN_VALUE;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(Integer.MIN_VALUE, jsonNode.asInt());
 }

 @Test
 public void testIntegerSerializerWithNaN() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> new IntegerSerializer().serialize(Float.NaN, null, null));
 }

 @Test
 public void testIntegerSerializerWithInfiniteNumber() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> new IntegerSerializer().serialize(Double.POSITIVE_INFINITY, null, null));
 Assertions.assertThrows(IllegalArgumentException.class, () -> new IntegerSerializer().serialize(Double.NEGATIVE_INFINITY, null, null));
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long value = 123L;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(123, jsonNode.asLong());
 }

 @Test
 public void testLongSerializerWithNegativeNumber() throws JsonProcessingException {
 Long value = -123L;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(-123, jsonNode.asLong());
 }

 @Test
 public void testLongSerializerWithZero() throws JsonProcessingException {
 Long value = 0L;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(0, jsonNode.asLong());
 }

 @Test
 public void testLongSerializerWithMaxValue() throws JsonProcessingException {
 Long value = Long.MAX_VALUE;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(Long.MAX_VALUE, jsonNode.asLong());
 }

 @Test
 public void testLongSerializerWithMinValue() throws JsonProcessingException {
 Long value = Long.MIN_VALUE;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(Long.MIN_VALUE, jsonNode.asLong());
 }

 @Test
 public void testLongSerializerWithNaN() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> new LongSerializer().serialize(Float.NaN, null, null));
 }

 @Test
 public void testLongSerializerWithInfiniteNumber() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> new LongSerializer().serialize(Double.POSITIVE_INFINITY, null, null));
 Assertions.assertThrows(IllegalArgumentException.class, () -> new LongSerializer().serialize(Double.NEGATIVE_INFINITY, null, null));
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short value = (short) 123;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(123, jsonNode.asInt());
 }

 @Test
 public void testShortSerializerWithNegativeNumber() throws JsonProcessingException {
 Short value = (short) -123;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(-123, jsonNode.asInt());
 }

 @Test
 public void testShortSerializerWithZero() throws JsonProcessingException {
 Short value = (short) 0;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(0, jsonNode.asInt());
 }

 @Test
 public void testShortSerializerWithMaxValue() throws JsonProcessingException {
 Short value = Short.MAX_VALUE;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(Short.MAX_VALUE, jsonNode.asInt());
 }

 @Test
 public void testShortSerializerWithMinValue() throws JsonProcessingException {
 Short value = Short.MIN_VALUE;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(Short.MIN_VALUE, jsonNode.asInt());
 }

 @Test
 public void testShortSerializerWithNaN() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> new ShortSerializer().serialize((short) Float.NaN, null, null));
 }

 @Test
 public void testShortSerializerWithInfiniteNumber() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> new ShortSerializer().serialize((short) Double.POSITIVE_INFINITY, null, null));
 Assertions.assertThrows(IllegalArgumentException.class, () -> new ShortSerializer().serialize((short) Double.NEGATIVE_INFINITY, null, null));
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float value = 123.45F;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(123.45, jsonNode.asFloat(), 0.001);
 }

 @Test
 public void testFloatSerializerWithNaN() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> new FloatSerializer().serialize(Float.NaN, null, null));
 }

 @Test
 public void testFloatSerializerWithInfiniteNumber() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> new FloatSerializer().serialize(Float.POSITIVE_INFINITY, null, null));
 Assertions.assertThrows(IllegalArgumentException.class, () -> new FloatSerializer().serialize(Float.NEGATIVE_INFINITY, null, null));
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double value = 123.45;
 String json = objectMapper.writeValueAsString(value);
 JsonNode jsonNode = objectMapper.readTree(json);
 Assertions.assertEquals(123.45, jsonNode.asDouble(), 0.001);
 }

 @Test
 public void testDoubleSerializerWithNaN() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> new DoubleSerializer().serialize(Double.NaN, null, null));
 }

 @Test
 public void testDoubleSerializerWithInfiniteNumber() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> new DoubleSerializer().serialize(Double.POSITIVE_INFINITY, null, null));
 Assertions.assertThrows(IllegalArgumentException.class, () -> new DoubleSerializer().serialize(Double.NEGATIVE_INFINITY, null, null));
 }

 @Test
 public void testAddAllMethod() throws JsonProcessingException {
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);
 String json = "{\"numbers\": [1, 2, 3, 1.5, -3.5, 123, -123, 12345678901234567890, 1.234567890123456789E10, 1.234567890123456789E-308]}";
 ObjectMapper objectMapper = new ObjectMapper();
 Number[] numbers = objectMapper.readValue(json, Number[].class);
 for (Number number : numbers) {
 JsonSerializer<?> serializer = allDeserializers.get(number.getClass().getName());
 String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
 JsonNode jsonNode = objectMapper.readTree(jsonString);
 JsonSerializer<?> deserializedNumber = objectMapper.findValueToSerialize(jsonNode, Number.class, null);
 Assertions.assertEquals(number, deserializedNumber);
 }
 }
}