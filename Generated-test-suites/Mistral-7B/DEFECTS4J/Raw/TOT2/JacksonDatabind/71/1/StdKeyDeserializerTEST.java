import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testBooleanType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(Boolean.TRUE, keyDeserializer.deserializeKey("true", ctxt));
 assertEquals(Boolean.FALSE, keyDeserializer.deserializeKey("false", ctxt));
 }

 @Test
 public void testByteType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals((byte) 1, keyDeserializer.deserializeKey("1", ctxt));
 assertEquals((byte) 127, keyDeserializer.deserializeKey("127", ctxt));
 try {
 keyDeserializer.deserializeKey("256", ctxt);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testShortType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals((short) 1, keyDeserializer.deserializeKey("1", ctxt));
 assertEquals((short) 32767, keyDeserializer.deserializeKey("32767", ctxt));
 try {
 keyDeserializer.deserializeKey("32768", ctxt);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testCharType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals('a', keyDeserializer.deserializeKey("a", ctxt));
 assertEquals('\u00E9', keyDeserializer.deserializeKey("\u00E9", ctxt));
 }

 @Test
 public void testIntType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(1, keyDeserializer.deserializeKey("1", ctxt));
 assertEquals(Integer.MAX_VALUE, keyDeserializer.deserializeKey(Integer.MAX_VALUE + "", ctxt));
 try {
 keyDeserializer.deserializeKey(Integer.MIN_VALUE + "", ctxt);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testLongType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(1L, keyDeserializer.deserializeKey("1", ctxt));
 assertEquals(Long.MAX_VALUE, keyDeserializer.deserializeKey(Long.MAX_VALUE + "", ctxt));
 try {
 keyDeserializer.deserializeKey(Long.MIN_VALUE + "", ctxt);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testFloatType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals((float) 1.0, keyDeserializer.deserializeKey("1.0", ctxt), 0.001f);
 assertEquals((float) 3.1415927f, keyDeserializer.deserializeKey("3.1415927", ctxt), 0.001f);
 }

 @Test
 public void testDoubleType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(1.0, keyDeserializer.deserializeKey("1.0", ctxt), 0.001);
 assertEquals(3.1415927, keyDeserializer.deserializeKey("3.1415927", ctxt), 0.001);
 }

 @Test
 public void testLocaleType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(Locale.ENGLISH, keyDeserializer.deserializeKey("en_US", ctxt));
 assertEquals(Locale.FRENCH, keyDeserializer.deserializeKey("fr_FR", ctxt));
 }

 @Test
 public void testDateType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(new Date(), keyDeserializer.deserializeKey("", ctxt));
 assertEquals(new Date(1234567890000L), keyDeserializer.deserializeKey("1234567890000", ctxt));
 }

 @Test
 public void testCalendarType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Calendar calendar = Calendar.getInstance();
 assertEquals(calendar, keyDeserializer.deserializeKey("", ctxt));
 assertEquals(calendar.getInstance(Locale.FRENCH), keyDeserializer.deserializeKey("fr_FR", ctxt));
 }

 @Test
 public void testUUIDType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(UUID.randomUUID(), keyDeserializer.deserializeKey("", ctxt));
 assertEquals(UUID.fromString("123e4567-789b-12d3-a456-426614174000"), keyDeserializer.deserializeKey("123e4567-789b-12d3-a456-426614174000", ctxt));
 }

 @Test
 public void testURIType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(new URI(""), keyDeserializer.deserializeKey("", ctxt));
 assertEquals(new URI("http://example.com"), keyDeserializer.deserializeKey("http://example.com", ctxt));
 }

 @Test
 public void testURLType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(new URL(""), keyDeserializer.deserializeKey("", ctxt));
 assertEquals(new URL("http://example.com"), keyDeserializer.deserializeKey("http://example.com", ctxt));
 }

 @Test
 public void testClassType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(String.class, keyDeserializer.deserializeKey("java.lang.String", ctxt));
 try {
 keyDeserializer.deserializeKey("com.example.MyClass", ctxt);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testUnknownType() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("java.util.HashMap", ctxt);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidBooleanValue() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("invalid", ctxt);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidByteValue() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("-128", ctxt);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidShortValue() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("-32768", ctxt);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidCharValue() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("\\uFFFF", ctxt);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidIntValue() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("-2147483649", ctxt);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidLongValue() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("-9223372036854775809", ctxt);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidFloatValue() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("NaN", ctxt);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidDoubleValue() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("NaN", ctxt);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidLocaleValue() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("invalid_locale", ctxt);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidDateValue() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("2023-02-30", ctxt);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidCalendarValue() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("2023-02-30", ctxt);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidUUIDValue() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("123e4567-789b-12d3-a456-426614174001", ctxt);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidURIValue() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("invalid_uri", ctxt);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidURLValue() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 keyDeserializer.deserializeKey("invalid_url", ctxt);
 }
}