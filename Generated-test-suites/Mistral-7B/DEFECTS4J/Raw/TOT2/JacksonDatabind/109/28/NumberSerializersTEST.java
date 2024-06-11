import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

 // Test the addAll method
 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);
 assertEquals(6, allDeserializers.size());
 }

 // Test IntegerSerializer
 @Test
 public void testIntegerSerializer() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonParser jsonParser = objectMapper.getFactory().createParser("123");
 JsonSerializer<Integer> integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
 Integer deserialized = jsonParser.readValueAs(Integer.class, integerSerializer);
 assertEquals(123, deserialized);
 }

 // Test IntegerSerializer with negative number
 @Test
 public void testIntegerSerializerNegativeNumber() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonParser jsonParser = objectMapper.getFactory().createParser("-123");
 JsonSerializer<Integer> integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
 Integer deserialized = jsonParser.readValueAs(Integer.class, integerSerializer);
 assertEquals(-123, deserialized);
 }

 // Test IntegerSerializer with max value
 @Test
 public void testIntegerSerializerMaxValue() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonParser jsonParser = objectMapper.getFactory().createParser(Integer.MAX_VALUE + "");
 JsonSerializer<Integer> integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
 Integer deserialized = jsonParser.readValueAs(Integer.class, integerSerializer);
 assertEquals(Integer.MAX_VALUE, deserialized);
 }

 // Test IntegerSerializer with min value
 @Test
 public void testIntegerSerializerMinValue() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonParser jsonParser = objectMapper.getFactory().createParser(Integer.MIN_VALUE + "");
 JsonSerializer<Integer> integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
 Integer deserialized = jsonParser.readValueAs(Integer.class, integerSerializer);
 assertEquals(Integer.MIN_VALUE, deserialized);
 }

 // Test IntLikeSerializer
 @Test
 public void testIntLikeSerializer() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_On_EMPTY_BEANS, false);
 JsonParser jsonParser = objectMapper.getFactory().createParser("123");
 JsonSerializer<Object> intLikeSerializer = new NumberSerializers.IntLikeSerializer();
 Integer deserialized = jsonParser.readValueAs(Integer.class, intLikeSerializer);
 assertEquals(123, deserialized);
 }

 // Test LongSerializer
 @Test
 public void testLongSerializer() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonParser jsonParser = objectMapper.getFactory().createParser("456789123456789");
 JsonSerializer<Long> longSerializer = new NumberSerializers.LongSerializer(Long.class);
 Long deserialized = jsonParser.readValueAs(Long.class, longSerializer);
 assertEquals(456789123456789L, deserialized);
 }

 // Test LongSerializer with negative number
 @Test
 public void testLongSerializerNegativeNumber() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonParser jsonParser = objectMapper.getFactory().createParser("-456789123456789");
 JsonSerializer<Long> longSerializer = new NumberSerializers.LongSerializer(Long.class);
 Long deserialized = jsonParser.readValueAs(Long.class, longSerializer);
 assertEquals(-456789123456789L, deserialized);
 }

 // Test LongSerializer with max value
 @Test
 public void testLongSerializerMaxValue() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonParser jsonParser = objectMapper.getFactory().createParser(Long.MAX_VALUE + "");
 JsonSerializer<Long> longSerializer = new NumberSerializers.LongSerializer(Long.class);
 Long deserialized = jsonParser.readValueAs(Long.class, longSerializer);
 assertEquals(Long.MAX_VALUE, deserialized);
 }

 // Test LongSerializer with min value
 @Test
 public void testLongSerializerMinValue() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonParser jsonParser = objectMapper.getFactory().createParser(Long.MIN_VALUE + "");
 JsonSerializer<Long> longSerializer = new NumberSerializers.LongSerializer(Long.class);
 Long deserialized = jsonParser.readValueAs(Long.class, longSerializer);
 assertEquals(Long.MIN_VALUE, deserialized);
 }

 // Test FloatSerializer
 @Test
 public void testFloatSerializer() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonParser jsonParser = objectMapper.getFactory().createParser("3.14");
 JsonSerializer<Float> floatSerializer = new NumberSerializers.FloatSerializer();
 Float deserialized = jsonParser.readValueAs(Float.class, floatSerializer);
 assertEquals(3.14f, deserialized, 0.001f);
 }

 // Test FloatSerializer with negative number
 @Test
 public void testFloatSerializerNegativeNumber() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonParser jsonParser = objectMapper.getFactory().createParser("-3.14");
 JsonSerializer<Float> floatSerializer = new NumberSerializers.FloatSerializer();
 Float deserialized = jsonParser.readValueAs(Float.class, floatSerializer);
 assertEquals(-3.14f, deserialized, 0.001f);
 }

 // Test FloatSerializer with max value
 @Test
 public void testFloatSerializerMaxValue() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonParser jsonParser = objectMapper.getFactory().createParser(Float.MAX_VALUE + "");
 JsonSerializer<Float> floatSerializer = new NumberSerializers.FloatSerializer();
 Float deserialized = jsonParser.readValueAs(Float.class, floatSerializer);
 assertEquals(Float.MAX_VALUE, deserialized, 0.001f);
 }

 // Test FloatSerializer with min value
 @Test
 public void testFloatSerializerMinValue() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonParser jsonParser = objectMapper.getFactory().createParser(Float.MIN_VALUE + "");
 JsonSerializer<Float> floatSerializer = new NumberSerializers.FloatSerializer();
 Float deserialized = jsonParser.readValueAs(Float.class, floatSerializer);
 assertEquals(Float.MIN_VALUE, deserialized, 0.001f);
 }

}