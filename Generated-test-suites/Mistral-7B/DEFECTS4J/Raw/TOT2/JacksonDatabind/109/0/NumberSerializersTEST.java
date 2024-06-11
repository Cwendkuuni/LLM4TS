import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 // Test for addAll method
 @Test
 public void testAddAll() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);

 assertEquals(5, allDeserializers.size());
 }

 // Test for IntegerSerializer
 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer number = 123;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedNumber = mapper.readValue(parser, Object.class);

 assertEquals(number.intValue(), deserializedNumber.intValue());
 }

 // Test for IntegerSerializer with negative number
 @Test
 public void testIntegerSerializerNegativeNumber() throws JsonProcessingException {
 Integer number = -123;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedNumber = mapper.readValue(parser, Object.class);

 assertEquals(number.intValue(), deserializedNumber.intValue());
 }

 // Test for IntegerSerializer with max value
 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerMaxValue() throws JsonProcessingException {
 Integer number = Integer.MAX_VALUE;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 mapper.readValue(parser, Integer.class);
 }

 // Test for IntegerSerializer with min value
 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerMinValue() throws JsonProcessingException {
 Integer number = Integer.MIN_VALUE;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 mapper.readValue(parser, Integer.class);
 }

 // Test for LongSerializer
 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long number = 123L;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedNumber = mapper.readValue(parser, Object.class);

 assertEquals(number.longValue(), deserializedNumber.longValue());
 }

 // Test for LongSerializer with negative number
 @Test
 public void testLongSerializerNegativeNumber() throws JsonProcessingException {
 Long number = -123L;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedNumber = mapper.readValue(parser, Object.class);

 assertEquals(number.longValue(), deserializedNumber.longValue());
 }

 // Test for LongSerializer with max value
 @Test(expected = JsonMappingException.class)
 public void testLongSerializerMaxValue() throws JsonProcessingException {
 Long number = Long.MAX_VALUE;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 mapper.readValue(parser, Long.class);
 }

 // Test for LongSerializer with min value
 @Test(expected = JsonMappingException.class)
 public void testLongSerializerMinValue() throws JsonProcessingException {
 Long number = Long.MIN_VALUE;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 mapper.readValue(parser, Long.class);
 }

 // Test for ShortSerializer
 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short number = (short) 123;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedNumber = mapper.readValue(parser, Object.class);

 assertEquals(number.shortValue(), deserializedNumber.shortValue());
 }

 // Test for ShortSerializer with negative number
 @Test
 public void testShortSerializerNegativeNumber() throws JsonProcessingException {
 Short number = (short) -123;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedNumber = mapper.readValue(parser, Object.class);

 assertEquals(number.shortValue(), deserializedNumber.shortValue());
 }

 // Test for ShortSerializer with max value
 @Test(expected = JsonMappingException.class)
 public void testShortSerializerMaxValue() throws JsonProcessingException {
 Short number = Short.MAX_VALUE;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 mapper.readValue(parser, Short.class);
 }

 // Test for ShortSerializer with min value
 @Test(expected = JsonMappingException.class)
 public void testShortSerializerMinValue() throws JsonProcessingException {
 Short number = Short.MIN_VALUE;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 mapper.readValue(parser, Short.class);
 }

 // Test for FloatSerializer
 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float number = 123.45f;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedNumber = mapper.readValue(parser, Object.class);

 assertEquals(number.floatValue(), deserializedNumber.floatValue(), 0.001f);
 }

 // Test for FloatSerializer with negative number
 @Test
 public void testFloatSerializerNegativeNumber() throws JsonProcessingException {
 Float number = -123.45f;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedNumber = mapper.readValue(parser, Object.class);

 assertEquals(number.floatValue(), deserializedNumber.floatValue(), 0.001f);
 }

 // Test for FloatSerializer with max value (Float.POSITIVE_INFINITY)
 @Test
 public void testFloatSerializerMaxValue() throws JsonProcessingException {
 Float number = Float.POSITIVE_INFINITY;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedNumber = mapper.readValue(parser, Object.class);

 assertEquals(number.floatValue(), deserializedNumber.floatValue(), 0.001f);
 }

 // Test for FloatSerializer with min value (Float.NEGATIVE_INFINITY)
 @Test
 public void testFloatSerializerMinValue() throws JsonProcessingException {
 Float number = Float.NEGATIVE_INFINITY;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedNumber = mapper.readValue(parser, Object.class);

 assertEquals(number.floatValue(), deserializedNumber.floatValue(), 0.001f);
 }

 // Test for DoubleSerializer
 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double number = 123.45;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedNumber = mapper.readValue(parser, Object.class);

 assertEquals(number.doubleValue(), deserializedNumber.doubleValue(), 0.001);
 }

 // Test for DoubleSerializer with negative number
 @Test
 public void testDoubleSerializerNegativeNumber() throws JsonProcessingException {
 Double number = -123.45;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedNumber = mapper.readValue(parser, Object.class);

 assertEquals(number.doubleValue(), deserializedNumber.doubleValue(), 0.001);
 }

 // Test for DoubleSerializer with max value (Double.POSITIVE_INFINITY)
 @Test
 public void testDoubleSerializerMaxValue() throws JsonProcessingException {
 Double number = Double.POSITIVE_INFINITY;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedNumber = mapper.readValue(parser, Object.class);

 assertEquals(number.doubleValue(), deserializedNumber.doubleValue(), 0.001);
 }

 // Test for DoubleSerializer with min value (Double.NEGATIVE_INFINITY)
 @Test
 public void testDoubleSerializerMinValue() throws JsonProcessingException {
 Double number = Double.NEGATIVE_INFINITY;
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(number.getClass());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);

 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedNumber = mapper.readValue(parser, Object.class);

 assertEquals(number.doubleValue(), deserializedNumber.doubleValue(), 0.001);
 }
}