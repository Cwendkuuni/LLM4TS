import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 // Test for StdKeyDeserializer constructor with no arguments
 @Test
 public void testStdKeyDeserializerInstance() {
 StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(-1, String.class);
 assertNotNull(stdKeyDeserializer);
 }

 // Test for StdKeyDeserializer constructor with FromStringDeserializer argument
 @Test
 public void testStdKeyDeserializerWithFromStringDeserializer() {
 StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(1, String.class, null);
 assertNotNull(stdKeyDeserializer);
 }

 // Test for StdKeyDeserializer forType method
 @Test
 public void testStdKeyDeserializerForType() throws Exception {
 Class<?> raw = Integer.class;
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
 assertEquals(Integer.class, keyDeserializer.getKeyClass());
 }

 // Test for deserializeKey method with valid keys
 @Test
 public void testDeserializeKeyWithValidKeys() throws IOException, JsonProcessingException {
 DeserializationContext context = new MockDeserializationContext();
 StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, String.class);

 Object result = keyDeserializer.deserializeKey("true", context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 result = keyDeserializer.deserializeKey("123", context);
 assertEquals(Integer.TYPE, result.getClass());
 assertEquals(123, result);

 result = keyDeserializer.deserializeKey("false", context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 result = keyDeserializer.deserializeKey("3.14", context);
 assertEquals(Double.TYPE, result.getClass());
 assertEquals(3.14, result, 0.001);

 result = keyDeserializer.deserializeKey("Locale[US]", context);
 assertEquals(Locale.class, result.getClass());
 assertEquals("US", result.toString());

 result = keyDeserializer.deserializeKey("java.util.Date", context);
 assertEquals(Date.class, result.getClass());

 result = keyDeserializer.deserializeKey("java.util.Calendar", context);
 assertEquals(Calendar.class, result.getClass());

 result = keyDeserializer.deserializeKey("java.util.UUID", context);
 assertEquals(UUID.class, result.getClass());

 result = keyDeserializer.deserializeKey("java.net.URI", context);
 assertEquals(URI.class, result.getClass());

 result = keyDeserializer.deserializeKey("java.net.URL", context);
 assertEquals(URL.class, result.getClass());

 result = keyDeserializer.deserializeKey("java.lang.Class", context);
 assertEquals(Class.class, result.getClass());
 }

 // Test for deserializeKey method with invalid keys
 @Test
 public void testDeserializeKeyWithInvalidKeys() throws IOException, JsonProcessingException {
 DeserializationContext context = new MockDeserializationContext();
 StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, String.class);

 String key = null;
 Object result = keyDeserializer.deserializeKey(key, context);
 assertNull(result);

 key = "";
 result = keyDeserializer.deserializeKey(key, context);
 assertNull(result);

 key = "invalidKey";
 result = keyDeserializer.deserializeKey(key, context);
 assertNull(result);
 }

 // Test for TYPE_BOOLEAN _parse method
 @Test
 public void testParseBoolean() throws Exception {
 StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
 String key = "true";
 Object result = stdKeyDeserializer._parse(key, new MockDeserializationContext());
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 key = "false";
 result = stdKeyDeserializer._parse(key, new MockDeserializationContext());
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 // Test for TYPE_BYTE _parse method
 @Test
 public void testParseByte() throws Exception {
 StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
 String key = "127";
 Object result = stdKeyDeserializer._parse(key, new MockDeserializationContext());
 assertTrue(result instanceof Byte);
 assertEquals((byte) 127, result);

 String key1 = "128";
 String key2 = "256";
 try {
 stdKeyDeserializer._parse(key1, new MockDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 try {
 stdKeyDeserializer._parse(key2, new MockDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 // Test for TYPE_SHORT _parse method
 @Test
 public void testParseShort() throws Exception {
 StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
 String key = "32767";
 Object result = stdKeyDeserializer._parse(key, new MockDeserializationContext());
 assertTrue(result instanceof Short);
 assertEquals((short) 32767, result);

 String key1 = "32768";
 String key2 = "65536";
 try {
 stdKeyDeserializer._parse(key1, new MockDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 try {
 stdKeyDeserializer._parse(key2, new MockDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 // Test for TYPE_CHAR _parse method
 @Test
 public void testParseChar() throws Exception {
 StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
 String key = "\u0041";
 Object result = stdKeyDeserializer._parse(key, new MockDeserializationContext());
 assertTrue(result instanceof Character);
 assertEquals((char) 'A', result);
 }

 // Test for TYPE_INT _parse method
 @Test
 public void testParseInt() throws Exception {
 StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
 String key = "2147483647";
 Object result = stdKeyDeserializer._parse(key, new MockDeserializationContext());
 assertTrue(result instanceof Integer);
 assertEquals(Integer.MAX_VALUE, result);

 String key1 = "2147483648";
 String key2 = "4294967297";
 try {
 stdKeyDeserializer._parse(key1, new MockDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 try {
 stdKeyDeserializer._parse(key2, new MockDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 // Test for TYPE_LONG _parse method
 @Test
 public void testParseLong() throws Exception {
 StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
 String key = "9223372036854775807";
 Object result = stdKeyDeserializer._parse(key, new MockDeserializationContext());
 assertTrue(result instanceof Long);
 assertEquals(Long.MAX_VALUE, result);

 String key1 = "9223372036854775808";
 String key2 = "18446744073709551616";
 try {
 stdKeyDeserializer._parse(key1, new MockDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 try {
 stdKeyDeserializer._parse(key2, new MockDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 // Test for TYPE_FLOAT _parse method
 @Test
 public void testParseFloat() throws Exception {
 StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
 String key = "3.4E38F";
 Object result = stdKeyDeserializer._parse(key, new MockDeserializationContext());
 assertTrue(result instanceof Float);
 assertEquals(Float.POSITIVE_INFINITY, result, 0.0);

 String key1 = "NaN";
 String key2 = "inf";
 try {
 stdKeyDeserializer._parse(key1, new MockDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 try {
 stdKeyDeserializer._parse(key2, new MockDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 // Test for TYPE_DOUBLE _parse method
 @Test
 public void testParseDouble() throws Exception {
 StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
 String key = "1.79769313486231E308";
 Object result = stdKeyDeserializer._parse(key, new MockDeserializationContext());
 assertTrue(result instanceof Double);
 assertEquals(Double.MAX_VALUE, result);

 String key1 = "NaN";
 String key2 = "inf";
 try {
 stdKeyDeserializer._parse(key1, new MockDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 try {
 stdKeyDeserializer._parse(key2, new MockDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 // Test for TYPE_URI _parse method
 @Test
 public void testParseURI() throws Exception {
 StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
 String key = "http://example.com";
 Object result = stdKeyDeserializer._parse(key, new MockDeserializationContext());
 assertTrue(result instanceof URI);
 assertEquals("http://example.com", result.toString());
 }

 // Test for TYPE_URL _parse method
 @Test
 public void testParseURL() throws Exception {
 StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
 String key = "http://example.com";
 Object result = stdKeyDeserializer._parse(key, new MockDeserializationContext());
 assertTrue(result instanceof URL);
 assertEquals("http://example.com", result.toString());
 }

 // Test for deserializeKey method with invalid enum keys
 @Test
 public void testDeserializeKeyWithInvalidEnumKeys() throws Exception {
 DeserializationContext context = new MockDeserializationContext();
 StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, Enum.class);

 String key = "InvalidEnumValue";
 Object result = keyDeserializer.deserializeKey(key, context);
 assertNull(result);
 }

 // Test for deserializeKey method with valid enum keys
 @Test
 public void testDeserializeKeyWithValidEnumKeys() throws Exception {
 DeserializationContext context = new MockDeserializationContext();
 StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, Enum.class);
 EnumResolver enumResolver = context.getAnnotationIntrospector().findValue("EnumResolver", null);
 EnumResolver enumResolverByName = new EnumResolver(enumResolver.getEnumClass(), null);
 AnnotatedMethod enumFactoryMethod = context.getAnnotations().findMethodAnnotations(enumResolverByName.getFactoryMethod());
 String key = "VALID_ENUM_VALUE";
 Object result = keyDeserializer.deserializeKey(key, context);
 assertEquals(enumResolverByName.getEnumClass().getEnumConstants()[0], result);
 }

 // Test for deserializeKey method with null key
 @Test
 public void testDeserializeKeyWithNullKey() throws Exception {
 DeserializationContext context = new MockDeserializationContext();
 StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, String.class);
 Object result = keyDeserializer.deserializeKey(null, context);
 assertNull(result);
 }

 // Test for deserializeKey method with empty key
 @Test
 public void testDeserializeKeyWithEmptyKey() throws Exception {
 DeserializationContext context = new MockDeserializationContext();
 StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, String.class);
 Object result = keyDeserializer.deserializeKey("", context);
 assertNull(result);
 }
}