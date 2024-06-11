import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper mapper = new JsonMapper();

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer input = 123;
 NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
 String expectedOutput = "123";
 String actualOutput = mapper.writeValueAsString(input, serializer);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIntegerSerializerWithType() throws JsonProcessingException {
 Integer input = 123;
 NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
 String expectedOutput = "123";
 String actualOutput = mapper.writeValueAsString(input, serializer, new TypeSerializer());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 Number input = 123;
 NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
 String expectedOutput = "123";
 String actualOutput = mapper.writeValueAsString(input, serializer);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIntLikeSerializerWithType() throws JsonProcessingException {
 Number input = 123;
 NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
 String expectedOutput = "123";
 String actualOutput = mapper.writeValueAsString(input, serializer, new TypeSerializer());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short input = 127;
 NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
 String expectedOutput = "127";
 String actualOutput = mapper.writeValueAsString(input, serializer);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testShortSerializerWithType() throws JsonProcessingException {
 Short input = 127;
 NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
 String expectedOutput = "127";
 String actualOutput = mapper.writeValueAsString(input, serializer, new TypeSerializer());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long input = 1234567890L;
 NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
 String expectedOutput = "1234567890";
 String actualOutput = mapper.writeValueAsString(input, serializer);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testLongSerializerWithType() throws JsonProcessingException {
 Long input = 1234567890L;
 NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
 String expectedOutput = "1234567890";
 String actualOutput = mapper.writeValueAsString(input, serializer, new TypeSerializer());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float input = 123.45f;
 NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
 String expectedOutput = "123.45";
 String actualOutput = mapper.writeValueAsString(input, serializer);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testFloatSerializerWithType() throws JsonProcessingException {
 Float input = 123.45f;
 NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
 String expectedOutput = "123.45";
 String actualOutput = mapper.writeValueAsString(input, serializer, new TypeSerializer());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double input = 123.456789;
 NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
 String expectedOutput = "123.456789";
 String actualOutput = mapper.writeValueAsString(input, serializer);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDoubleSerializerWithType() throws JsonProcessingException {
 Double input = 123.456789;
 NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
 String expectedOutput = "123.456789";
 String actualOutput = mapper.writeValueAsString(input, serializer, new TypeSerializer());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntegerSerializerWithNullInput() {
 NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
 // This test case should throw an IllegalArgumentException as the serializer
 // cannot handle null inputs
 mapper.writeValueAsString(null, serializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntLikeSerializerWithNullInput() {
 NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
 // This test case should throw an IllegalArgumentException as the serializer
 // cannot handle null inputs
 mapper.writeValueAsString(null, serializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShortSerializerWithNullInput() {
 NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
 // This test case should throw an IllegalArgumentException as the serializer
 // cannot handle null inputs
 mapper.writeValueAsString(null, serializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLongSerializerWithNullInput() {
 NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
 // This test case should throw an IllegalArgumentException as the serializer
 // cannot handle null inputs
 mapper.writeValueAsString(null, serializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFloatSerializerWithNullInput() {
 NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
 // This test case should throw an IllegalArgumentException as the serializer
 // cannot handle null inputs
 mapper.writeValueAsString(null, serializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoubleSerializerWithNullInput() {
 NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
 // This test case should throw an IllegalArgumentException as the serializer
 // cannot handle null inputs
 mapper.writeValueAsString(null, serializer);
 }

 @Test
 public void testJsonFormatAnnotation() throws JsonProcessingException {
 class TestClass {
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 private int field;
 }
 NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
 TestClass obj = new TestClass();
 obj.field = 123;
 String expectedOutput = "\"" + 123 + "\"";
 String actualOutput = mapper.writeValueAsString(obj, serializer);
 assertEquals(expectedOutput, actualOutput);
 }
}