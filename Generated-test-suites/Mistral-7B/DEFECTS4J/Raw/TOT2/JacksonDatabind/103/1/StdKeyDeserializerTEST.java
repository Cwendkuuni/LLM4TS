import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Introspect;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 ctxt = new TestDeserializationContext(new AnnotationIntrospector());
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(DeserializationFeature.FAIL_ON_INVALID_REFS, false);
 mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_FOR_VALUE_TYPE, false);
 mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
 mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 /*
 Test for default constructor
 */
 @Test
 public void testDefaultConstructor() throws Exception {
 StdKeyDeserializer deserializer = new StdKeyDeserializer(-1, Object.class);
 assertEquals(Object.class, deserializer.getKeyClass());
 }

 /*
 Test for constructor with class and int kind
 */
 @Test
 public void testConstructorWithClassAndIntKind() throws Exception {
 Class<?> intClass = Integer.class;
 StdKeyDeserializer deserializer = new StdKeyDeserializer(5, intClass);
 assertEquals(intClass, deserializer.getKeyClass());
 }

 /*
 Test for constructor with class, int kind, and FromStringDeserializer
 */
 @Test
 public void testConstructorWithClassIntKindAndDeserializer() throws Exception {
 Class<?> booleanClass = Boolean.class;
 FromStringDeserializer booleanDeserializer = new FromStringDeserializer(Boolean.class);
 StdKeyDeserializer deserializer = new StdKeyDeserializer(1, booleanClass, booleanDeserializer);
 assertEquals(booleanClass, deserializer.getKeyClass());
 }

 /*
 Test for deserializeKey method with boolean type
 */
 @Test
 public void testDeserializeKeyBoolean() throws Exception {
 String key = "true";
 Object result = mapper.deserializeValue(key, StdKeyDeserializer.class);
 assertTrue((Boolean) result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 key = "false";
 result = mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt);
 assertTrue((Boolean) result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 /*
 Test for deserializeKey method with byte type
 */
 @Test
 public void testDeserializeKeyByte() throws Exception {
 String key = "127";
 int value = ((Byte) mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt)).byteValue();
 assertEquals(127, value);
 }

 /*
 Test for deserializeKey method with short type
 */
 @Test
 public void testDeserializeKeyShort() throws Exception {
 String key = "32767";
 short value = ((Short) mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt)).shortValue();
 assertEquals(32767, value);
 }

 /*
 Test for deserializeKey method with char type
 */
 @Test
 public void testDeserializeKeyChar() throws Exception {
 String key = "A";
 char value = ((Character) mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt)).charValue();
 assertEquals('A', value);
 }

 /*
 Test for deserializeKey method with int type
 */
 @Test
 public void testDeserializeKeyInt() throws Exception {
 String key = "12345";
 int value = ((Integer) mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt)).intValue();
 assertEquals(12345, value);
 }

 /*
 Test for deserializeKey method with long type
 */
 @Test
 public void testDeserializeKeyLong() throws Exception {
 String key = "123456789012345";
 long value = ((Long) mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt)).longValue();
 assertEquals(123456789012345L, value);
 }

 /*
 Test for deserializeKey method with float type
 */
 @Test
 public void testDeserializeKeyFloat() throws Exception {
 String key = "123.45";
 float value = ((Float) mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt)).floatValue();
 assertEquals(123.45f, value, 0.01f);
 }

 /*
 Test for deserializeKey method with double type
 */
 @Test
 public void testDeserializeKeyDouble() throws Exception {
 String key = "123.456";
 double value = ((Double) mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt)).doubleValue();
 assertEquals(123.456, value, 0.001);
 }

 /*
 Test for deserializeKey method with URI type
 */
 @Test
 public void testDeserializeKeyURI() throws Exception {
 String key = "http://localhost:8080/test";
 URI value = (URI) mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt);
 assertEquals("http://localhost:8080/test", value.toString());
 }

 /*
 Test for deserializeKey method with URL type
 */
 @Test
 public void testDeserializeKeyURL() throws Exception {
 String key = "http://localhost:8080/test";
 URL value = (URL) mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt);
 assertEquals("http://localhost:8080/test", value.toString());
 }

 /*
 Test for deserializeKey method with Class type
 */
 @Test
 public void testDeserializeKeyClass() throws Exception {
 String key = "java.lang.String";
 Class<?> value = (Class<?>) mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt);
 assertEquals(String.class, value);
 }

 /*
 Test for deserializeKey method with Locale type
 */
 @Test
 public void testDeserializeKeyLocale() throws Exception {
 String key = "en_US";
 Locale value = (Locale) mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt);
 assertEquals(Locale.US, value);
 }

 /*
 Test for deserializeKey method with Currency type
 */
 @Test
 public void testDeserializeKeyCurrency() throws Exception {
 String key = "USD";
 Currency value = (Currency) mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt);
 assertEquals(Currency.getInstance("USD"), value);
 }

 /*
 Test for deserializeKey method with Enum type
 */
 @Test
 public void testDeserializeKeyEnum() throws Exception {
 Class<Season> enumClass = Season.class;
 Introspect introspect = mapper.getAnnotationIntrospector();
 AnnotatedMethod factoryMethod = introspect.findMethodAnnotations(enumClass, "valueOf");
 StdKeyDeserializer deserializer = new StdKeyDeserializer(
 mapper.getSerializationConfig().getDefaultInstanceProvider().findValueDeserializer(enumClass),
 factoryMethod);
 String key = "SPRING";
 Season value = (Season) mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt, deserializer);
 assertEquals(Season.SPRING, value);
 }

 /*
 Test for deserializeKey method with byte[] type (Base64)
 */
 @Test
 public void testDeserializeKeyByteArrayBase64() throws Exception {
 String key = "RnJvbSBzcGVuYWwgc29tZSBjb250aW51Lg==";
 byte[] value = mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt);
 assertEquals(Base64Variants.STANDARD.decode(key), value);
 }

 /*
 Test for _parse method with boolean type
 */
 @Test
 public void testParseBoolean() throws Exception {
 String key = "true";
 Object result = new StdKeyDeserializer(1, Boolean.class)._parse(key, ctxt);
 assertTrue((Boolean) result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 /*
 Test for _parse method with byte type
 */
 @Test
 public void testParseByte() throws Exception {
 String key = "127";
 Object result = new StdKeyDeserializer(2, Byte.class)._parse(key, ctxt);
 assertTrue((Byte) result instanceof Byte);
 assertEquals((Byte) 127, result);
 }

 /*
 Test for _parse method with short type
 */
 @Test
 public void testParseShort() throws Exception {
 String key = "32767";
 Object result = new StdKeyDeserializer(3, Short.class)._parse(key, ctxt);
 assertTrue((Short) result instanceof Short);
 assertEquals((Short) 32767, result);
 }

 /*
 Test for _parse method with char type
 */
 @Test
 public void testParseChar() throws Exception {
 String key = "A";
 Object result = new StdKeyDeserializer(4, Character.class)._parse(key, ctxt);
 assertTrue((Character) result instanceof Character);
 assertEquals((Character) 'A', result);
 }

 /*
 Test for _parse method with int type
 */
 @Test
 public void testParseInt() throws Exception {
 String key = "12345";
 Object result = new StdKeyDeserializer(5, Integer.class)._parse(key, ctxt);
 assertTrue(result instanceof Integer);
 assertEquals(12345, result);
 }

 /*
 Test for _parse method with long type
 */
 @Test
 public void testParseLong() throws Exception {
 String key = "123456789012345";
 Object result = new StdKeyDeserializer(6, Long.class)._parse(key, ctxt);
 assertTrue(result instanceof Long);
 assertEquals(123456789012345L, result);
 }

 /*
 Test for _parse method with float type
 */
 @Test
 public void testParseFloat() throws Exception {
 String key = "123.45";
 Object result = new StdKeyDeserializer(7, Float.class)._parse(key, ctxt);
 assertTrue(result instanceof Float);
 assertEquals(Float.parseFloat(key), result);
 }

 /*
 Test for _parse method with double type
 */
 @Test
 public void testParseDouble() throws Exception {
 String key = "123.456";
 Object result = new StdKeyDeserializer(8, Double.class)._parse(key, ctxt);
 assertTrue(result instanceof Double);
 assertEquals(Double.parseDouble(key), result);
 }

 /*
 Test for _parse method with Locale type
 */
 @Test
 public void testParseLocale() throws Exception {
 String key = "en_US";
 Object result = new StdKeyDeserializer(9, Locale.class)._parse(key, ctxt);
 assertTrue(result instanceof Locale);
 assertEquals(Locale.US, result);
 }

 /*
 Test for _parse method with Currency type
 */
 @Test
 public void testParseCurrency() throws Exception {
 String key = "USD";
 Object result = new StdKeyDeserializer(16, Currency.class)._parse(key, ctxt);
 assertTrue(result instanceof Currency);
 assertEquals(Currency.getInstance("USD"), result);
 }

 /*
 Test for _parse method with Enum type
 */
 @Test
 public void testParseEnum() throws Exception {
 Class<Season> enumClass = Season.class;
 Object result = new StdKeyDeserializer(enumClass.getDeclaredConstructor().getDeclaringClass(), enumClass)
 ._parse("SPRING", ctxt);
 assertEquals(Season.SPRING, result);
 }

 /*
 Test for _parse method with byte[] type
 */
 @Test
 public void testParseByteArray() throws Exception {
 String key = "RnJvbSBzcGVuYWwgc29tZSBjb250aW51Lg==";
 Object result = new StdKeyDeserializer(17, byte[].class)._parse(key, ctxt);
 assertEquals(Base64Variants.STANDARD.decode(key), result);
 }

 /*
 Test for invalid type
 */
 @Test(expected = IllegalStateException.class)
 public void testInvalidType() throws Exception {
 new StdKeyDeserializer(1, new Object(){})._parse("test", ctxt);
 }

 /*
 Test for null key
 */
 @Test
 public void testNullKey() throws Exception {
 assertNull(mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(null, ctxt));
 }

 /*
 Test for empty key
 */
 @Test
 public void testEmptyKey() throws Exception {
 assertNull(mapper.deserializationContextFromInput(ctxt)
 .deserializeKey("", ctxt));
 }

 /*
 Test for key with illegal character
 */
 @Test(expected = JsonProcessingException.class)
 public void testIllegalCharacter() throws Exception {
 String key = "test#";
 mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt);
 }

 /*
 Test for key with overflow value for byte type
 */
 @Test(expected = JsonProcessingException.class)
 public void testByteOverflow() throws Exception {
 String key = "256";
 mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt);
 }

 /*
 Test for key with overflow value for short type
 */
 @Test(expected = JsonProcessingException.class)
 public void testShortOverflow() throws Exception {
 String key = "32768";
 mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt);
 }

 /*
 Test for key with overflow value for int type
 */
 @Test(expected = JsonProcessingException.class)
 public void testIntOverflow() throws Exception {
 String key = "2147483649";
 mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt);
 }

 /*
 Test for key with overflow value for long type
 */
 @Test(expected = JsonProcessingException.class)
 public void testLongOverflow() throws Exception {
 String key = "9223372036854775808";
 mapper.deserializationContextFromInput(ctxt)
 .deserializeKey(key, ctxt);
 }
}