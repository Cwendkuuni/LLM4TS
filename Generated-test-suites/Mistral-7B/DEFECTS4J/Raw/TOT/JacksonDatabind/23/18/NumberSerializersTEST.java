import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Integer input = 123;
 String json = mapper.writerWithRootValue(NumberSerializers.IntegerSerializer.instance)
 .writeValueAsString(input);

 assertEquals("123", json);

 Object readValue = mapper.readerForType(Integer.class)
 .readValue(mapper.createParser(NumberSerializers.IntegerSerializer.instance.getClass(), new StringReader(json)));

 assertEquals(input.intValue(), ((Integer) readValue).intValue());
 }

 @Test
 public void testIntegerSerializerWithNegativeNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Integer input = -123;
 String json = mapper.writerWithRootValue(NumberSerializers.IntegerSerializer.instance)
 .writeValueAsString(input);

 assertEquals("-123", json);

 Object readValue = mapper.readerForType(Integer.class)
 .readValue(mapper.createParser(NumberSerializers.IntegerSerializer.instance.getClass(), new StringReader(json)));

 assertEquals(input.intValue(), ((Integer) readValue).intValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerWithFloatNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Float input = 123.5f;
 String json = mapper.writerWithRootValue(NumberSerializers.IntegerSerializer.instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerWithString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String input = "abc";
 String json = mapper.writerWithRootValue(NumberSerializers.IntegerSerializer.instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }

 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Long input = 123L;
 String json = mapper.writerWithRootValue(NumberSerializers.IntLikeSerializer.instance)
 .writeValueAsString(input);

 assertEquals("123", json);

 Object readValue = mapper.readerForType(Number.class)
 .readValue(mapper.createParser(NumberSerializers.IntLikeSerializer.instance.getClass(), new StringReader(json)));

 assertEquals(input.intValue(), ((Number) readValue).intValue());
 }

 @Test
 public void testIntLikeSerializerWithNegativeNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Long input = -123L;
 String json = mapper.writerWithRootValue(NumberSerializers.IntLikeSerializer.instance)
 .writeValueAsString(input);

 assertEquals("-123", json);

 Object readValue = mapper.readerForType(Number.class)
 .readValue(mapper.createParser(NumberSerializers.IntLikeSerializer.instance.getClass(), new StringReader(json)));

 assertEquals(input.intValue(), ((Number) readValue).intValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testIntLikeSerializerWithFloatNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Float input = 123.5f;
 String json = mapper.writerWithRootValue(NumberSerializers.IntLikeSerializer.instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }

 @Test(expected = JsonMappingException.class)
 public void testIntLikeSerializerWithString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String input = "abc";
 String json = mapper.writerWithRootValue(NumberSerializers.IntLikeSerializer.instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Short input = Short.MIN_VALUE;
 String json = mapper.writerWithRootValue(NumberSerializers.ShortSerializer.instance)
 .writeValueAsString(input);

 assertEquals("-32768", json);

 Object readValue = mapper.readerForType(Short.class)
 .readValue(mapper.createParser(NumberSerializers.ShortSerializer.instance.getClass(), new StringReader(json)));

 assertEquals(input.shortValue(), ((Short) readValue).shortValue());
 }

 @Test
 public void testShortSerializerWithPositiveNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Short input = 123;
 String json = mapper.writerWithRootValue(NumberSerializers.ShortSerializer.instance)
 .writeValueAsString(input);

 assertEquals("123", json);

 Object readValue = mapper.readerForType(Short.class)
 .readValue(mapper.createParser(NumberSerializers.ShortSerializer.instance.getClass(), new StringReader(json)));

 assertEquals(input.shortValue(), ((Short) readValue).shortValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testShortSerializerWithLongNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Long input = 123L;
 String json = mapper.writerWithRootValue(NumberSerializers.ShortSerializer.instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }

 @Test(expected = JsonMappingException.class)
 public void testShortSerializerWithFloatNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Float input = 123.5f;
 String json = mapper.writerWithRootValue(NumberSerializers.ShortSerializer.instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }

 @Test(expected = JsonMappingException.class)
 public void testShortSerializerWithString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String input = "abc";
 String json = mapper.writerWithRootValue(NumberSerializers.ShortSerializer
 .instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Long input = 1234567890123L;
 String json = mapper.writerWithRootValue(NumberSerializers.LongSerializer.instance)
 .writeValueAsString(input);

 assertEquals("1234567890123", json);

 Object readValue = mapper.readerForType(Long.class)
 .readValue(mapper.createParser(NumberSerializers.LongSerializer.instance.getClass(), new StringReader(json)));

 assertEquals(input.longValue(), ((Long) readValue).longValue());
 }

 @Test
 public void testLongSerializerWithNegativeNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Long input = -1234567890123L;
 String json = mapper.writerWithRootValue(NumberSerializers.LongSerializer.instance)
 .writeValueAsString(input);

 assertEquals("-1234567890123", json);

 Object readValue = mapper.readerForType(Long.class)
 .readValue(mapper.createParser(NumberSerializers.LongSerializer.instance.getClass(), new StringReader(json)));

 assertEquals(input.longValue(), ((Long) readValue).longValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testLongSerializerWithFloatNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Float input = 123.5f;
 String json = mapper.writerWithRootValue(NumberSerializers.LongSerializer.instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }

 @Test(expected = JsonMappingException.class)
 public void testLongSerializerWithString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String input = "abc";
 String json = mapper.writerWithRootValue(NumberSerializers.LongSerializer.instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Float input = 123.5f;
 String json = mapper.writerWithRootValue(NumberSerializers.FloatSerializer.instance)
 .writeValueAsString(input);

 assertEquals("123.5", json);

 Object readValue = mapper.readerForType(Float.class)
 .readValue(mapper.createParser(NumberSerializers.FloatSerializer.instance.getClass(), new StringReader(json)));

 assertEquals(input.floatValue(), ((Float) readValue).floatValue(), 0.001f);
 }

 @Test
 public void testFloatSerializerWithNegativeNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Float input = -123.5f;
 String json = mapper.writerWithRootValue(NumberSerializers.FloatSerializer.instance)
 .writeValueAsString(input);

 assertEquals("-123.5", json);

 Object readValue = mapper.readerForType(Float.class)
 .readValue(mapper.createParser(NumberSerializers.FloatSerializer.instance.getClass(), new StringReader(json)));

 assertEquals(input.floatValue(), ((Float) readValue).floatValue(), 0.001f);
 }

 @Test
 public void testFloatSerializerWithLongNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Long input = 1234567890123L;
 String json = mapper.writerWithRootValue(NumberSerializers.FloatSerializer.instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }

 @Test
 public void testFloatSerializerWithIntNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Integer input = 123;
 String json = mapper.writerWithRootValue(NumberSerializers.FloatSerializer.instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }

 @Test(expected = JsonMappingException.class)
 public void testFloatSerializerWithString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String input = "abc";
 String json = mapper.writerWithRootValue(NumberSerializers.FloatSerializer.instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Double input = 123.12345;
 String json = mapper.writerWithRootValue(NumberSerializers.DoubleSerializer.instance)
 .writeValueAsString(input);

 assertEquals("123.12345", json);

 Object readValue = mapper.readerForType(Double.class)
 .readValue(mapper.createParser(NumberSerializers.DoubleSerializer.instance.getClass(), new StringReader(json)));

 assertEquals(input.doubleValue(), ((Double) readValue).doubleValue(), 0.0001);
 }

 @Test
 public void testDoubleSerializerWithNegativeNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Double input = -123.12345;
 String json = mapper.writerWithRootValue(NumberSerializers.DoubleSerializer.instance)
 .writeValueAsString(input);

 assertEquals("-123.12345", json);

 Object readValue = mapper.readerForType(Double.class)
 .readValue(mapper.createParser(NumberSerializers.DoubleSerializer.instance.getClass(), new StringReader(json)));

 assertEquals(input.doubleValue(), ((Double) readValue).doubleValue(), 0.0001);
 }

 @Test
 public void testDoubleSerializerWithLongNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Long input = 1234567890123L;
 String json = mapper.writerWithRootValue(NumberSerializers.DoubleSerializer.instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }

 @Test
 public void testDoubleSerializerWithIntNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Integer input = 123;
 String json = mapper.writerWithRootValue(NumberSerializers.DoubleSerializer.instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }

 @Test(expected = JsonMappingException.class)
 public void testDoubleSerializerWithString() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String input = "abc";
 String json = mapper.writerWithRootValue(NumberSerializers.DoubleSerializer.instance)
 .writeValueAsString(input);

 fail("Expected JsonMappingException but got: " + json);
 }
}