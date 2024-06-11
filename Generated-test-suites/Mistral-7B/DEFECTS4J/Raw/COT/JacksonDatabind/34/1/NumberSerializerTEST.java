import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

 private final ObjectMapper mapper = new JsonMapper();

 @Test
 public void testSerializeBigDecimal() throws IOException, JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("123.45");
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
 numberSerializer.serialize(bigDecimal, jsonGenerator, new SerializerProvider());
 String expected = "123.45";
 assertEquals(expected, jsonGenerator.getOutputString());
 }

 @Test
 public void testSerializeBigInteger() throws IOException, JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger bigInteger = new BigInteger("12345678901234567890");
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
 numberSerializer.serialize(bigInteger, jsonGenerator, new SerializerProvider());
 String expected = "12345678901234567890";
 assertEquals(expected, jsonGenerator.getOutputString());
 }

 @Test
 public void testSerializeInteger() throws IOException, JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Integer integer = 123;
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
 numberSerializer.serialize(integer, jsonGenerator, new SerializerProvider());
 String expected = "123";
 assertEquals(expected, jsonGenerator.getOutputString());
 }

 @Test
 public void testSerializeLong() throws IOException, JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Long longValue = 123456789012345L;
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
 numberSerializer.serialize(longValue, jsonGenerator, new SerializerProvider());
 String expected = "123456789012345";
 assertEquals(expected, jsonGenerator.getOutputString());
 }

 @Test
 public void testSerializeDouble() throws IOException, JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Double doubleValue = 123.45;
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
 numberSerializer.serialize(doubleValue, jsonGenerator, new SerializerProvider());
 String expected = "123.45";
 assertEquals(expected, jsonGenerator.getOutputString());
 }

 @Test
 public void testSerializeFloat() throws IOException, JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Float floatValue = 123.45f;
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
 numberSerializer.serialize(floatValue, jsonGenerator, new SerializerProvider());
 String expected = "123.45"; // Float is converted to Double during serialization
 assertEquals(expected, jsonGenerator.getOutputString());
 }

 @Test
 public void testSerializeByteOrShort() throws IOException, JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Byte byteValue = 127;
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
 numberSerializer.serialize(byteValue, jsonGenerator, new SerializerProvider());
 String expected = "127";
 assertEquals(expected, jsonGenerator.getOutputString());

 Short shortValue = 32767;
 JsonGenerator jsonGenerator2 = mapper.getFactory().createGenerator();
 numberSerializer.serialize(shortValue, jsonGenerator2, new SerializerProvider());
 String expected2 = "32767";
 assertEquals(expected2, jsonGenerator2.getOutputString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeUnsupportedType() throws IOException, JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String stringValue = "Hello";
 JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
 numberSerializer.serialize(stringValue, jsonGenerator, new SerializerProvider());
 }

}