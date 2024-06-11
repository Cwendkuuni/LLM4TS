import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

 private final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("123.456");
 String json = objectMapper.writeValueAsString(bigDecimal, numberSerializer);
 assertEquals("123.456", json);
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger bigInteger = new BigInteger("12345678901234567890");
 String json = objectMapper.writeValueAsString(bigInteger, numberSerializer);
 assertEquals("12345678901234567890", json);
 }

 @Test
 public void testSerializeInteger() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Integer integer = 123;
 String json = objectMapper.writeValueAsString(integer, numberSerializer);
 assertEquals("123", json);
 }

 @Test
 public void testSerializeLong() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Long longValue = 1234567890123456789L;
 String json = objectMapper.writeValueAsString(longValue, numberSerializer);
 assertEquals("1234567890123456789", json);
 }

 @Test
 public void testSerializeDouble() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Double doubleValue = 123.456;
 String json = objectMapper.writeValueAsString(doubleValue, numberSerializer);
 assertEquals("123.456", json);
 }

 @Test
 public void testSerializeFloat() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Float floatValue = 123.45f;
 String json = objectMapper.writeValueAsString(floatValue, numberSerializer);
 assertEquals("123.45", json);
 }

 @Test
 public void testSerializeByte() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Byte byteValue = 127;
 String json = objectMapper.writeValueAsString(byteValue, numberSerializer);
 assertEquals("127", json);
 }

 @Test
 public void testSerializeShort() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Short shortValue = 123;
 String json = objectMapper.writeValueAsString(shortValue, numberSerializer);
 assertEquals("123", json);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnsupportedType() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String stringValue = "UnsupportedType";
 String json = objectMapper.writeValueAsString(stringValue, numberSerializer);
 }
}