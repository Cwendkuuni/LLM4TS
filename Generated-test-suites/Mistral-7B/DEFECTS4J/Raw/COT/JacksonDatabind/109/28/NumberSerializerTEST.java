import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.test.BaseTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest extends BaseTest {

 private static final NumberSerializer numberSerializer = NumberSerializer.instance;

 @Test
 public void testSerializeNumber() throws JsonGenerationException, JsonMappingException {
 Number number = 123;
 String expected = "123";

 String result = mapper.writeValueAsString(numberSerializer, number);
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException {
 BigDecimal bigDecimal = new BigDecimal("123.45");
 String expected = "123.45";

 String result = mapper.writeValueAsString(numberSerializer, bigDecimal);
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException {
 BigInteger bigInteger = new BigInteger("12345678901234567890");
 String expected = "12345678901234567890";

 String result = mapper.writeValueAsString(numberSerializer, bigInteger);
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeLong() throws JsonGenerationException, JsonMappingException {
 Long longValue = 123L;
 String expected = "123";

 String result = mapper.writeValueAsString(numberSerializer, longValue);
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeDouble() throws JsonGenerationException, JsonMappingException {
 Double doubleValue = 123.45;
 String expected = "123.45";

 String result = mapper.writeValueAsString(numberSerializer, doubleValue);
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeFloat() throws JsonGenerationException, JsonMappingException {
 Float floatValue = 123.45f;
 String expected = "123.45";

 String result = mapper.writeValueAsString(numberSerializer, floatValue);
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeInteger() throws JsonGenerationException, JsonMappingException {
 Integer integerValue = 123;
 String expected = "123";

 String result = mapper.writeValueAsString(numberSerializer, integerValue);
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeByte() throws JsonGenerationException, JsonMappingException {
 Byte byteValue = (byte) 123;
 String expected = "123";

 String result = mapper.writeValueAsString(numberSerializer, byteValue);
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeShort() throws JsonGenerationException, JsonMappingException {
 Short shortValue = (short) 123;
 String expected = "123";

 String result = mapper.writeValueAsString(numberSerializer, shortValue);
 assertEquals(expected, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnsupportedType() throws JsonGenerationException, JsonMappingException {
 String unsupportedType = new Object() {};

 String result = mapper.writeValueAsString(numberSerializer, unsupportedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateContextualWithNullProperty() throws JsonMappingException {
 BeanProperty nullProperty = null;
 JsonSerializer<?> result = numberSerializer.createContextual(mapper.getSerializationConfig(), nullProperty);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNullNumber() throws JsonGenerationException, JsonMappingException {
 Number nullNumber = null;
 String result = mapper.writeValueAsString(numberSerializer, nullNumber);
 }
}