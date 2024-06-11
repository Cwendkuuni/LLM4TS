package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;
 private DeserializationContext context;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 context = objectMapper.getDeserializationContext();
 }

 @Test
 public void testBooleanKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object result = keyDeserializer.deserializeKey("true", context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 result = keyDeserializer.deserializeKey("false", context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 result = keyDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testByteKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object result = keyDeserializer.deserializeKey("1", context);
 assertTrue(result instanceof Byte);
 assertEquals((Byte) Byte.valueOf((byte) 1), result);

 result = keyDeserializer.deserializeKey("-127", context);
 assertTrue(result instanceof Byte);
 assertEquals((Byte) Byte.valueOf((byte) -127), result);

 result = keyDeserializer.deserializeKey("128", context);
 assertNull(result);
 }

 @Test
 public void testShortKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 Object result = keyDeserializer.deserializeKey("1", context);
 assertTrue(result instanceof Short);
 assertEquals((Short) Short.valueOf((short) 1), result);

 result = keyDeserializer.deserializeKey("32767", context);
 assertTrue(result instanceof Short);
 assertEquals((Short) Short.valueOf((short) 32767), result);

 result = keyDeserializer.deserializeKey("32768", context);
 assertNull(result);
 }

 @Test
 public void testCharKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
 Object result = keyDeserializer.deserializeKey("'A'", context);
 assertTrue(result instanceof Character);
 assertEquals('A', result.charValue());

 result = keyDeserializer.deserializeKey("'Z'", context);
 assertTrue(result instanceof Character);
 assertEquals('Z', result.charValue());

 result = keyDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testIntKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object result = keyDeserializer.deserializeKey("1", context);
 assertEquals((Integer) 1, result);

 result = keyDeserializer.deserializeKey("-2147483648", context);
 assertEquals((Integer) -2147483648, result);

 result = keyDeserializer.deserializeKey("2147483647", context);
 assertNull(result);
 }

 @Test
 public void testLongKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 Object result = keyDeserializer.deserializeKey("1", context);
 assertEquals((Long) 1L, result);

 result = keyDeserializer.deserializeKey("-9223372036854775808", context); // Long.MIN_VALUE
 assertEquals((Long) -9223372036854775808L, result);

 result = keyDeserializer.deserializeKey("9223372036854775807", context); // Long.MAX_VALUE
 assertNull(result);
 }

 @Test
 public void testFloatKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
 Object result = keyDeserializer.deserializeKey("1.0", context);
 assertTrue(result instanceof Float);
 assertEquals((Float) Float.valueOf(1.0f), result);

 result = keyDeserializer.deserializeKey("NaN", context);
 assertTrue(result instanceof Float);
 assertEquals((Float) Float.NaN, result);

 result = keyDeserializer.deserializeKey("inf", context);
 assertTrue(result instanceof Float);
 assertEquals((Float) Float.POSITIVE_INFINITY, result);

 result = keyDeserializer.deserializeKey("-inf", context);
 assertTrue(result instanceof Float);
 assertEquals((Float) Float.NEGATIVE_INFINITY, result);

 result = keyDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testDoubleKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
 Object result = keyDeserializer.deserializeKey("1.0", context);
 assertTrue(result instanceof Double);
 assertEquals((Double) Double.valueOf(1.0), result);

 result = keyDeserializer.deserializeKey("NaN", context);
 assertTrue(result instanceof Double);
 assertEquals((Double) Double.NaN, result);

 result = keyDeserializer.deserializeKey("inf", context);
 assertTrue(result instanceof Double);
 assertEquals((Double) Double.POSITIVE_INFINITY, result);

 result = keyDeserializer.deserializeKey("-inf", context);
 assertTrue(result instanceof Double);
 assertEquals((Double) Double.NEGATIVE_INFINITY, result);

 result = keyDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testLocaleKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object result = keyDeserializer.deserializeKey("en_US", context);
 assertTrue(result instanceof Locale);
 assertEquals(Locale.US, result);

 result = keyDeserializer.deserializeKey("fr_FR", context);
 assertTrue(result instanceof Locale);
 assertEquals(Locale.FRANCE, result);

 result = keyDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testDateKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
 Object result = keyDeserializer.deserializeKey("2023-03-20", context);
 assertTrue(result instanceof Date);

 result = keyDeserializer.deserializeKey("2023-03-31", context);
 assertNull(result);
 }

 @Test
 public void testCalendarKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 Object result = keyDeserializer.deserializeKey("2023-03-20", context);
 assertTrue(result instanceof Calendar);

 result = keyDeserializer.deserializeKey("2023-03-31", context);
 assertNull(result);
 }

 @Test
 public void testUUIDKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
 Object result = keyDeserializer.deserializeKey("8b0d6b7f-b0a1-4d2a-9719-1ab345f4d12e", context);
 assertTrue(result instanceof UUID);

 result = keyDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testURIKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 Object result = keyDeserializer.deserializeKey("http://example.com", context);
 assertTrue(result instanceof URI);

 result = keyDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testURLKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 Object result = keyDeserializer.deserializeKey("http://example.com", context);
 assertTrue(result instanceof URL);

 result = keyDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testClassKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
 Object result = keyDeseralizer.deserializeKey("java.lang.String", context);
 assertTrue(result instanceof Class);
 assertEquals(String.class, result);

 result = keyDeserializer.deserializeKey("java.util.Date", context);
 assertTrue(result instanceof Class);
 assertEquals(Date.class, result);

 result = keyDeserializer.deserializeKey("java.util.List", context);
 assertEquals(List.class, result);

 result = keyDeserializer.deserializeKey("java.util.Map", context);
 assertEquals(Map.class, result);

 result = keyDeserializer.deserializeKey("java.lang.Boolean", context);
 assertEquals(Boolean.class, result);

 result = keyDeserializer.deserializeKey("java.lang.Character", context);
 assertEquals(Character.class, result);

 result = keyDeserializer.deserializeKey("java.lang.Short", context);
 assertEquals(Short.class, result);

 result = keyDeserializer.deserializeKey("java.lang.Byte", context);
 assertEquals(Byte.class, result);

 result = keyDeserializer.deserializeKey("java.lang.Integer", context);
 assertEquals(Integer.class, result);

 result = keyDeserializer.deserializeKey("java.lang.Long", context);
 assertEquals(Long.class, result);

 result = keyDeserializer.deserializeKey("java.lang.Float", context);
 assertEquals(Float.class, result);

 result = keyDeserializer.deserializeKey("java.lang.Double", context);
 assertEquals(Double.class, result);

 result = keyDeserializer.deserializeKey("java.util.Locale", context);
 assertEquals(Locale.class, result);

 result = keyDeserializer.deserializeKey("java.util.Date", context);
 assertEquals(Date.class, result);

 result = keyDeserializer.deserializeKey("java.util.Calendar", context);
 assertEquals(Calendar.class, result);

 result = keyDeserializer.deserializeKey("java.util.UUID", context);
 assertEquals(UUID.class, result);

 result = keyDeserializer.deserializeKey("java.net.URI", context);
 assertEquals(URI.class, result);

 result = keyDeserializer.deserializeKey("java.net.URL", context);
 assertEquals(URL.class, result);

 result = keyDeserializer.deserializeKey("java.lang.Enum", context);
 assertEquals(Enum.class, result);

 result = keyDeserializer.deserializeKey("java.util.EnumSet", context);
 assertEquals(EnumSet.class, result);

 result = keyDeserializer.deserializeKey("java.lang.EnumMap", context);
 assertEquals(EnumMap.class, result);

 result = keyDeserializer.deserializeKey("com.example.MyClass", context);
 assertEquals(Class.forName("com.example.MyClass"), result);

 result = keyDeserializer.deserializeKey("java.util.Map<java.lang.String, java.util.List<java.lang.String>>", context);
 assertEquals(Map.class, result);
 }

 @Test
 public void testEnumKeyDeserializer() throws IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyEnum.class);
 Object result = keyDeserializer.deserializeKey("MY_VALUE", context);
 assertTrue(result instanceof MyEnum);
 assertEquals(MyEnum.MY_VALUE, result);

 result = keyDeserializer.deserializeKey("invalid", context);
 assertNull(result);
 }

 @Test
 public void testCustomKeyDeserializer() throws IOException {
 class CustomClass {
 public String key;

 public CustomClass(String key) {
 this.key = key;
 }
 }

 StdKeyDeserializer keyDeserializer = new StringCtorKeyDeserializer(CustomClass.class);
 Object result = keyDeserializer.deserializeKey("test", context);
 assertTrue(result instanceof CustomClass);
 assertEquals("test", ((CustomClass) result).key);
 }
}