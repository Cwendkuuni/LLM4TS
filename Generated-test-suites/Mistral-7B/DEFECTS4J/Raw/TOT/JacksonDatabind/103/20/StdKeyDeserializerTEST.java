package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private static final DeserializationContext context = new TestDeserializationContext();

 @Before
 public void setup() {
 }

 @Test
 public void testBooleanKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = keyDeserializer.deserializeKey("true", context);
 assertTrue(key instanceof Boolean);
 assertEquals(Boolean.TRUE, key);

 key = keyDeserializer.deserializeKey("false", context);
 assertTrue(key instanceof Boolean);
 assertEquals(Boolean.FALSE, key);
 }

 @Test
 public void testByteKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object key = keyDeserializer.deserializeKey("127", context);
 assertTrue(key instanceof Byte);
 assertEquals((Byte) 127, key);

 try {
 Object key2 = keyDeserializer.deserializeKey("-128", context);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testShortKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 Object key = keyDeserializer.deserializeKey("32767", context);
 assertTrue(key instanceof Short);
 assertEquals((Short) 32767, key);

 try {
 Object key2 = keyDeserializer.deserializeKey("-32768", context);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCharKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
 Object key = keyDeserializer.deserializeKey("A", context);
 assertTrue(key instanceof Character);
 assertEquals((Character) 'A', key);
 }

 @Test
 public void testIntKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object key = keyDeserializer.deserializeKey("123456", context);
 assertTrue(key instanceof Integer);
 assertEquals((Integer) 123456, key);
 }

 @Test
 public void testLongKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 Object key = keyDeserializer.deserializeKey("9223372036854775807", context);
 assertTrue(key instanceof Long);
 assertEquals((Long) 9223372036854775807L, key);
 }

 @Test
 public void testFloatKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
 Object key = keyDeserializer.deserializeKey("3.14", context);
 assertTrue(key instanceof Float);
 assertEquals((Float) 3.14f, key);
 }

 @Test
 public void testDoubleKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
 Object key = keyDeserializer.deserializeKey("3.14159265", context);
 assertTrue(key instanceof Double);
 assertEquals((Double) 3.14159265d, key);
 }

 @Test
 public void testLocaleKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object key = keyDeserializer.deserializeKey("en_US", context);
 assertTrue(key instanceof Locale);
 assertEquals(Locale.US, key);
 }

 @Test
 public void testDateKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
 Object key = keyDeserializer.deserializeKey("2021-12-31", context);
 assertTrue(key instanceof Date);
 // check date value
 // ...
 }

 @Test
 public void testCalendarKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 // Calendar cannot be instantiated directly, need to use a subclass
 Object key = keyDeserializer.deserializeKey("2021-12-31", new TestCalendarContext());
 assertTrue(key instanceof Calendar);
 // check calendar value
 // ...
 }

 @Test
 public void testUUIDKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
 Object key = keyDeserializer.deserializeKey("123e4567-789b-123d-a45f-123456789abc", context);
 assertTrue(key instanceof UUID);
 assertEquals(UUID.fromString("123e4567-789b-123d-a45f-123456789abc"), key);
 }

 @Test
 public void testURIKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 Object key = keyDeserializer.deserializeKey("http://www.example.com", context);
 assertTrue(key instanceof URI);
 assertEquals(new URI("http://www.example.com"), key);
 }

 @Test
 public void testURLKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 Object key = keyDeserializer.deserializeKey("http://www.example.com", context);
 assertTrue(key instanceof URL);
 assertEquals(new URL("http://www.example.com"), key);
 }

 @Test
 public void testClassKeyDeserializer() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
 Object key = keyDeserializer.deserializeKey("[Ljava.lang.String;@123456", context);
 assertTrue(key instanceof String[]);
 // check array value
 // ...
 }

 @Test
 public void testEnumKeyDeserializer() throws JsonProcessingException {
 // Define a custom enumeration for testing
 enum CustomEnum {
 FOO, BAR, BAZ
 }

 AnnotationIntrospector introspector = context.getAnnotationIntrospector();
 AnnotatedClass ac = introspector.findClassAnnotations(CustomEnum.class, null);
 EnumResolver enumResolver = introspector.findPropertyValueResolver(null, CustomEnum.class, "name");
 KeyDeserializer keyDeserializer = new EnumKD(enumResolver, ac.getMember("values").getAnnotatedMethod("valueOf", String.class));

 Object keyFoo = keyDeserializer.deserializeKey("FOO", context);
 assertTrue(keyFoo instanceof CustomEnum);
 assertEquals(CustomEnum.FOO, keyFoo);

 Object keyBaz = keyDeserializer.deserializeKey("BAZ", context);
 assertTrue(keyBaz instanceof CustomEnum);
 assertEquals(CustomEnum.BAZ, keyBaz);

 Object keyUnknown = keyDeserializer.deserializeKey("UNKNOWN", context);
 assertNull(keyUnknown);
 }

 @Test
 public void testCustomKeyDeserializer() throws JsonProcessingException {
 // Define a custom key deserializer for testing
 class CustomKeyDeserializer extends StdKeyDeserializer {
 public CustomKeyDeserializer(int kind, Class<?> keyClass) {
 super(kind, keyClass);
 }

 @Override
 public Object _parse(String key, DeserializationContext ctxt) throws Exception {
 // Custom parsing logic
 return Integer.valueOf(Integer.parseInt(key) * 2);
 }
 }

 Object key = new CustomKeyDeserializer(1, Integer.class).deserializeKey("42", context);
 assertTrue(key instanceof Integer);
 assertEquals((Integer) 84, key);
 }
}