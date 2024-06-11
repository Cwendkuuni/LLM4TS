import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testBooleanType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "true";
 Object result = deserializer.deserializeKey(key, context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 key = "false";
 result = deserializer.deserializeKey(key, context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testByteType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "127";
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(Byte.valueOf((byte) 127), result);
 key = "-128";
 try {
 result = deserializer.deserializeKey(key, context);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("not a valid representation: value can not be represented as 8-bit value", e.getMessage());
 }
 }

 @Test
 public void testShortType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "32767";
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(Short.valueOf((short) 32767), result);
 key = "-32768";
 try {
 result = deserializer.deserializeKey(key, context);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("overflow, value can not be represented as 16-bit value", e.getMessage());
 }
 }

 @Test
 public void testCharType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "A";
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(Character.valueOf('A'), result);
 key = "\\u0061";
 Object result2 = deserializer.deserializeKey(key, context);
 assertEquals(Character.valueOf('a'), result2);
 }

 @Test
 public void testIntType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "12345";
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(Integer.valueOf(12345), result);
 }

 @Test
 public void testLongType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "9223372036854775807";
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(Long.valueOf(9223372036854775807L), result);
 }

 @Test
 public void testFloatType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "3.14159";
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(Float.valueOf(3.14159f), result);
 }

 @Test
 public void testDoubleType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "6.28318530718";
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(Double.valueOf(6.28318530718), result);
 }

 @Test
 public void testLocaleType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "en_US";
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(Locale.US, result);
 }

 @Test
 public void testDateType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "2023-03-25";
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(new Date(2023, 2, 25), result);
 }

 @Test
 public void testCalendarType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "2023-03-25";
 Object result = deserializer.deserializeKey(key, context);
 Calendar calendar = (Calendar) result;
 assertEquals(2023, calendar.get(Calendar.YEAR));
 assertEquals(3, calendar.get(Calendar.MONTH));
 assertEquals(25, calendar.get(Calendar.DATE));
 }

 @Test
 public void testUUIDType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "8b0d6bcd-b01a-49e8-8fcc-6b7b7e61e69b";
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(UUID.fromString(key), result);
 }

 @Test
 public void testURIType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "https://www.example.com/";
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(new URI(key), result);
 }

 @Test
 public void testURLType() throws JsonMappingException, JsonProcessingException, IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "https://www.example.com/";
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(new URL(key), result);
 }

 @Test
 public void testClassType() throws JsonMappingException, JsonProcessingException, ClassNotFoundException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "java.lang.String";
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(String.class, result.getClass());
 }

 @Test
 public void testEnumType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "VALUE_B";
 Object result = deserializer.deserializeKey(key, context);
 assertEquals(MyEnum.VALUE_B, result);
 }

 @Test
 public void testInvalidKeyType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = null;
 try {
 Object result = deserializer.deserializeKey(key, context);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("not a valid representation", e.getMessage());
 }
 }

 @Test
 public void testInvalidEnumValue() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "INVALID_VALUE";
 try {
 Object result = deserializer.deserializeKey(key, context);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("not one of values excepted for Enum class: [VALUE_A, VALUE_B]", e.getMessage());
 }
 }

 @Test
 public void testExceptionHandling() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "InvalidInput";
 try {
 Object result = deserializer.deserializeKey(key, context);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("not a valid representation", e.getMessage());
 }
 }
}