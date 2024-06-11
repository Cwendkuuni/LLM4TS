import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testBooleanType() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", new TestContext()));
 assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testBooleanTypeInvalidKey() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 deserializer.deserializeKey("invalid_key", new TestContext());
 }

 @Test
 public void testByteType() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 assertEquals((byte) 1, deserializer.deserializeKey("1", new TestContext()));
 assertEquals((byte) 127, deserializer.deserializeKey("127", new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testByteTypeOutOfRange() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 deserializer.deserializeKey("-1", new TestContext());
 deserializer.deserializeKey("128", new TestContext());
 }

 @Test
 public void testShortType() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 assertEquals((short) 1, deserializer.deserializeKey("1", new TestContext()));
 assertEquals((short) Short.MAX_VALUE, deserializer.deserializeKey("32767", new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testShortTypeOutOfRange() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 deserializer.deserializeKey("-1", new TestContext());
 deserializer.deserializeKey("32768", new TestContext());
 }

 @Test
 public void testCharType() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 assertEquals((char) 'A', deserializer.deserializeKey("A", new TestContext()));
 assertEquals((char) 'ö', deserializer.deserializeKey("ö", new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testCharTypeInvalidKey() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 deserializer.deserializeKey("invalid_key", new TestContext());
 }

 @Test
 public void testIntType() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 assertEquals(1, deserializer.deserializeKey("1", new TestContext()));
 assertEquals(Integer.MAX_VALUE, deserializer.deserializeKey("2147483647", new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testIntTypeOutOfRange() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 deserializer.deserializeKey("-1", new TestContext());
 deserializer.deserializeKey("2147483648", new TestContext());
 }

 @Test
 public void testLongType() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 assertEquals(1L, deserializer.deserializeKey("1", new TestContext()));
 // Long.MAX_VALUE is too large
 assertEquals(Long.parseUnsignedLong("4294967295"), deserializer.deserializeKey("4294967295", new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testLongTypeOutOfRange() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 deserializer.deserializeKey("-1", new TestContext());
 deserializer.deserializeKey("4294967296", new TestContext());
 }

 @Test
 public void testFloatType() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 assertEquals(1.0f, deserializer.deserializeKey("1.0", new TestContext()), 0.001f);
 assertEquals(Float.POSITIVE_INFINITY, deserializer.deserializeKey("Infinity", new TestContext()));
 assertEquals(Float.NaN, deserializer.deserializeKey("NaN", new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testFloatTypeInvalidKey() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 deserializer.deserializeKey("invalid_key", new TestContext());
 }

 @Test
 public void testDoubleType() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 assertEquals(1.0, deserializer.deserializeKey("1.0", new TestContext()), 0.001);
 assertEquals(Double.POSITIVE_INFINITY, deserializer.deserializeKey("Infinity", new TestContext()));
 assertEquals(Double.NaN, deserializer.deserializeKey("NaN", new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testDoubleTypeInvalidKey() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 deserializer.deserializeKey("invalid_key", new TestContext());
 }

 @Test
 public void testLocaleType() throws JsonMappingException, UnsupportedEncodingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
 assertEquals(Locale.ENGLISH, deserializer.deserializeKey("en", new TestContext()));
 assertEquals(Locale.GERMAN, deserializer.deserializeKey("de_DE", new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testLocaleTypeInvalidKey() throws JsonMappingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
 deserializer.deserializeKey("invalid_key", new TestContext());
 }

 @Test
 public void testDateType() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 assertEquals(new Date(), deserializer.deserializeKey("", new TestContext()));
 assertEquals(new Date(1623381200000L), deserializer.deserializeKey("2021-10-01T11:10:00Z", new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testDateTypeInvalidFormat() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 deserializer.deserializeKey("invalid_format", new TestContext());
 }

 @Test
 public void testCalendarType() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 assertEquals(Calendar.getInstance(), deserializer.deserializeKey("", new TestContext()));
 assertEquals(Calendar.getInstance().getTime(), deserializer.deserializeKey("2021-10-01", new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testCalendarTypeInvalidFormat() throws JsonProcessingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 deserializer.deserializeKey("invalid_format", new TestContext());
 }

 @Test
 public void testURIType() throws JsonMappingException, MalformedURLException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 assertEquals(new URI(""), deserializer.deserializeKey("", new TestContext()));
 assertEquals(new URI("http://example.com"), deserializer.deserializeKey("http://example.com", new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testURITypeInvalidFormat() throws JsonMappingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 deserializer.deserializeKey("invalid_format", new TestContext());
 }

 @Test
 public void testURLType() throws JsonMappingException, MalformedURLException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 assertEquals(new URL(""), deserializer.deserializeKey("", new TestContext()));
 assertEquals(new URL("http://example.com"), deserializer.deserializeKey("http://example.com", new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testURLTypeMalformed() throws JsonMappingException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 deserializer.deserializeKey("malformed_url", new TestContext());
 }

 @Test
 public void testClassType() throws JsonMappingException, ClassNotFoundException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 assertEquals(String.class, deserializer.deserializeKey("String", new TestContext()));
 assertEquals(Integer.class, deserializer.deserializeKey("java.lang.Integer", new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testClassTypeInvalidFormat() throws JsonMappingException, ClassNotFoundException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 deserializer.deserializeKey("invalid_format", new TestContext());
 }
}