import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class StdKeyDeserializerTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testBooleanDeserializer() throws IOException {
 String trueKey = "true";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = deserializer.deserializeKey(trueKey, new TestContext());
 assertEquals(Boolean.TRUE, key);

 String falseKey = "false";
 key = deserializer.deserializeKey(falseKey, new TestContext());
 assertEquals(Boolean.FALSE, key);
 }

 @Test
 public void testByteDeserializer() throws IOException {
 String key = "127";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertEquals((Byte) 127, result);

 key = "-128";
 result = deserializer.deserializeKey(key, new TestContext());
 assertEquals((Byte) -128, result);

 key = "256";
 try {
 result = deserializer.deserializeKey(key, new TestContext());
 fail("Expected IOException for byte key > 127");
 } catch (IOException e) {
 // pass
 }
 }

 @Test
 public void testShortDeserializer() throws IOException {
 String key = "32767";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertEquals((Short) 32767, result);

 key = "-32768";
 result = deserializer.deserializeKey(key, new TestContext());
 assertEquals((Short) -32768, result);

 key = "32768";
 try {
 result = deserializer.deserializeKey(key, new TestContext());
 fail("Expected IOException for short key > 32767");
 } catch (IOException e) {
 // pass
 }
 }

 @Test
 public void testCharDeserializer() throws IOException {
 String key = "\u0041";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Character.valueOf('\u0041'), result);

 key = "❄";
 result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Character.valueOf(0x25B2), result);
 }

 @Test
 public void testIntDeserializer() throws IOException {
 String key = "Integer.MAX_VALUE";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Integer.MAX_VALUE, result);

 key = "Integer.MIN_VALUE";
 result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Integer.MIN_VALUE, result);
 }

 @Test
 public void testLongDeserializer() throws IOException {
 String key = "Long.MAX_VALUE";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Long.MAX_VALUE, result);

 String keyLongMinusOne = Long.toString(Long.MAX_VALUE - 1);
 result = deserializer.deserializeKey(keyLongMinusOne, new TestContext());
 assertEquals((Long) Long.MAX_VALUE - 1, result);

 key = "Long.MIN_VALUE";
 result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Long.MIN_VALUE, result);
 }

 @Test
 public void testFloatDeserializer() throws IOException {
 String key = "Float.POSITIVE_INFINITY";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Float.POSITIVE_INFINITY, result);

 key = "Float.NEGATIVE_INFINITY";
 result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Float.NEGATIVE_INFINITY, result);

 key = "NaN";
 result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Float.NaN, result);
 }

 @Test
 public void testDoubleDeserializer() throws IOException {
 String key = "Double.POSITIVE_INFINITY";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Double.POSITIVE_INFINITY, result);

 key = "Double.NEGATIVE_INFINITY";
 result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Double.NEGATIVE_INFINITY, result);

 key = "NaN";
 result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Double.NaN, result);
 }

 @Test
 public void testLocaleDeserializer() throws IOException {
 String key = "en_US";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Locale.US, result);

 key = "es_ES";
 result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Locale.SPAIN, result);
 }

 @Test
 public void testDateDeserializer() throws IOException {
 String key = "2022-12-31";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(new Date(System.currentTimeMillis()), result); // For simplicity, assuming the current date is later than December 31, 2022

 key = "2022-02-29";
 try {
 result = deserializer.deserializeKey(key, new TestContext());
 fail("Expected IOException for non-existent date: Feb 29, 2022");
 } catch (IOException e) {
 // pass
 }
 }

 @Test
 public void testCalendarDeserializer() throws IOException {
 String key = "2022-12-31";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Calendar.getInstance(), result); // For simplicity, assuming the current calendar instance represents the date December 31, 2022

 key = "2022-02-29";
 try {
 result = deserializer.deserializeKey(key, new TestContext());
 fail("Expected IOException for non-existent date: Feb 29, 2022");
 } catch (IOException e) {
 // pass
 }
 }

 @Test
 public void testURIDeserializer() throws IOException {
 String key = "https://example.com";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(new URI("https://example.com"), result);

 key = "http://example.com/%7Euser/file.txt";
 result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(new URI("http://example.com/~user/file.txt"), result);
 }

 @Test
 public void testURLDeserializer() throws IOException {
 String key = "https://example.com";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(new URL("https://example.com"), result);

 key = "http://example.com/%7Euser/file.txt";
 try {
 result = deserializer.deserializeKey(key, new TestContext());
 fail("Expected IOException for non-URL: % in the key");
 } catch (IOException e) {
 // pass
 }
 }

 @Test
 public void testClassDeserializer() throws IOException {
 String key = "java.lang.String";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Class.class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertTrue(result instanceof Class);
 assertEquals(String.class, result);

 key = "java.util.Date";
 result = deserializer.deserializeKey(key, new TestContext());
 assertTrue(result instanceof Class);
 assertEquals(Date.class, result);
 }

 @Test
 public void testEnumDeserializer() throws IOException {
 String key = "com.example.MyEnum.TEST";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(MyEnum.TEST, result);

 String unknownKey = "com.example.MyEnum.UNKNOWN";
 try {
 result = deserializer.deserializeKey(unknownKey, new TestContext());
 fail("Expected IOException for unknown enum constant: UNKNOWN");
 } catch (IOException e) {
 // pass
 }
 }

 @Test
 public void testByteArrayDeserializer() throws IOException {
 String key = "HS256:RnZQXuzIhvjrNWV6bWl6cGFzcw==";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(byte[].class);
 Object result = deserializer.deserializeKey(key, new TestContext());
 assertEquals(Arrays.copyOf(mapper.getFactory().getBase64Variant().decode(key), 256), result);
 }

 @Test
 public void testInvalidKeyDeserializer() throws IOException {
 String key = "invalid_key";
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 try {
 deserializer.deserializeKey(key, new TestContext());
 fail("Expected IOException for invalid key: " + key);
 } catch (IOException e) {
 // pass
 }
 }

 @SuppressWarnings("unchecked")
 private static class TestContext extends DeserializationContext {
 private final ObjectMapper _mapper = mapper;

 @Override
 public ObjectMapper getObjectMapper() {
 return _mapper;
 }

 @Override
 public AnnotationIntrospector getAnnotationIntrospector() {
 return mapper.getAnnotationIntrospector();
 }

 @Override
 public AnnotatedClass getClassInfo(Class<?> cls) {
 return mapper.getClassInfo(cls);
 }

 @Override
 public AnnotatedMethod getMethodInfo(Method m) {
 return mapper.getMethodInfo(m);
 }

 @Override
 public Converter<String, Object> findValueConverter(DeserializationConfig config, JavaType type, TypeDeserializer valueDeserializer) {
 return mapper.getFactory().createValueSerializer(type).getDeserializer();
 }

 @Override
 public Set<DeserializationFeature> getEnabledFeatures() {
 return EnumSet.allOf(DeserializationFeature.class);
 }
 }
}