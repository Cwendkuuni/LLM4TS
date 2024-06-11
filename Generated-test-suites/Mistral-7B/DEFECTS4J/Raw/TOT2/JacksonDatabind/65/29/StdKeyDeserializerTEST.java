import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testBooleanType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = keyDeserializer.deserializeKey("true", mapper.getDeserializationContext());
 assertEquals(Boolean.TRUE, key);
 keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 key = keyDeserializer.deserializeKey("false", mapper.getDeserializationContext());
 assertEquals(Boolean.FALSE, key);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidBooleanType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 keyDeserializer.deserializeKey("invalid", mapper.getDeserializationContext());
 }

 @Test
 public void testByteType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object key = keyDeserializer.deserializeKey("127", mapper.getDeserializationContext());
 assertEquals((byte) 127, key);
 keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 key = keyDeserializer.deserializeKey("-128", mapper.getDeserializationContext());
 assertEquals((byte) -128, key);
 keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 key = keyDeserializer.deserializeKey("256", mapper.getDeserializationContext());
 assertNotNull(key); // should throw an exception but JUnit does not support testing exceptions directly
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidByteType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 keyDeserializer.deserializeKey("-257", mapper.getDeserializationContext());
 }

 @Test
 public void testShortType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 Object key = keyDeserializer.deserializeKey("32767", mapper.getDeserializationContext());
 assertEquals((short) 32767, key);
 keyDeserializer = StdKeyDeserializer.forType(Short.class);
 key = keyDeserializer.deserializeKey("-32768", mapper.getDeserializationContext());
 assertEquals((short) -32768, key);
 keyDeserializer = StdKeyDeserializer.forType(Short.class);
 key = keyDeserializer.deserializeKey("65536", mapper.getDeserializationContext());
 assertNotNull(key); // should throw an exception but JUnit does not support testing exceptions directly
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidShortType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 keyDeserializer.deserializeKey("32768", mapper.getDeserializationContext());
 }

 @Test
 public void testCharType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
 Object key = keyDeserializer.deserializeKey("A", mapper.getDeserializationContext());
 assertEquals('A', key);
 keyDeserializer = StdKeyDeserializer.forType(Character.class);
 key = keyDeserializer.deserializeKey("\\u00E9", mapper.getDeserializationContext());
 assertEquals('\u00E9', key);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidCharType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
 keyDeserializer.deserializeKey("\u00E8", mapper.getDeserializationContext());
 }

 @Test
 public void testIntType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object key = keyDeserializer.deserializeKey("2147483647", mapper.getDeserializationContext());
 assertEquals(2147483647, key);
 keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 key = keyDeserializer.deserializeKey("-2147483648", mapper.getDeserializationContext());
 assertEquals(-2147483648, key);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidIntType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 keyDeserializer.deserializeKey("2147483648", mapper.getDeserializationContext());
 }

 @Test
 public void testLongType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 Object key = keyDeserializer.deserializeKey("9223372036854775807", mapper.getDeserializationContext());
 assertEquals(9223372036854775807L, key);
 keyDeserializer = StdKeyDeserializer.forType(Long.class);
 key = keyDeserializer.deserializeKey("-9223372036854775808", mapper.getDeserializationContext());
 assertEquals(-9223372036854775808L, key);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidLongType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 keyDeserializer.deserializeKey("9223372036854775809", mapper.getDeserializationContext());
 }

 @Test
 public void testFloatType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
 Object key = keyDeserializer.deserializeKey("3.14159265358979323846", mapper.getDeserializationContext());
 assertEquals(3.14159265358979323846f, key, 0.00001f);
 }

 @Test
 public void testDoubleType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
 Object key = keyDeserializer.deserializeKey("6.28318530718", mapper.getDeserializationContext());
 assertEquals(6.28318530718, key, 0.000001);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidFloatType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
 keyDeserializer.deserializeKey("6.28318530719", mapper.getDeserializationContext());
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidDoubleType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
 keyDeserializer.deserializeKey("6.28318530718E10", mapper.getDeserializationContext());
 }

 @Test
 public void testLocaleType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object key = keyDeserializer.deserializeKey("en_US", mapper.getDeserializationContext());
 assertEquals("en_US", key.toString());
 keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 key = keyDeserializer.deserializeKey("es_ES", mapper.getDeserializationContext());
 assertEquals("es_ES", key.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidLocaleType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 keyDeserializer.deserializeKey("es_ES_ES", mapper.getDeserializationContext());
 }

 @Test
 public void testDateType() throws JsonProcessingException, ParseException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
 Object key = keyDeserializer.deserializeKey("2022-10-01", mapper.getDeserializationContext());
 assertEquals("Sat Oct 01 00:00:00 CEST 2022", key.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidDateType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
 keyDeserializer.deserializeKey("2022-10-33", mapper.getDeserializationContext());
 }

 @Test
 public void testCalendarType() throws JsonProcessingException, ParseException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 Object key = keyDeserializer.deserializeKey("2022-10-01", mapper.getDeserializationContext());
 assertEquals(Calendar.getInstance().getTime(), key.getTime());
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidCalendarType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 keyDeserializer.deserializeKey("2022-10-33", mapper.getDeserializationContext());
 }

 @Test
 public void testURIType() throws JsonProcessingException, URISyntaxException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 Object key = keyDeserializer.deserializeKey("http://www.example.com", mapper.getDeserializationContext());
 assertEquals("http://www.example.com", key.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidURIType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 keyDeserializer.deserializeKey("http://www.example.com/", mapper.getDeserializationContext());
 }

 @Test
 public void testURLType() throws JsonProcessingException, MalformedURLException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 Object key = keyDeserializer.deserializeKey("http://www.example.com", mapper.getDeserializationContext());
 assertEquals("http://www.example.com", key.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidURLType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 keyDeserializer.deserializeKey("http://www.example.com/", mapper.getDeserializationContext());
 }

 @Test
 public void testClassType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
 Object key = keyDeserializer.deserializeKey("java.lang.String", mapper.getDeserializationContext());
 assertEquals("java.lang.String", key.getClass().getName());
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidClassType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
 keyDeserializer.deserializeKey("java.lang.Integer", mapper.getDeserializationContext());
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidKeyType() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
 keyDeserializer.deserializeKey("abcd1234", mapper.getDeserializationContext());
 }
}