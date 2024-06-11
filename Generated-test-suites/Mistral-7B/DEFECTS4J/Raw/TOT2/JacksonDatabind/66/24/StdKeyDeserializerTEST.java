import com.fasterxml.jackson.core.base.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class StdKeyDeserializerTest {

 private KeyDeserializer _keyDeserializer;
 private DeserializationContext _context;

 @Before
 public void setUp() throws Exception {
 _keyDeserializer = StdKeyDeserializer.forType(String.class);
 _context = new TestDeserializationContext();
 }

 @Test
 public void testStringKeyDeserialization() throws IOException {
 String key = "testStringKey";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 assertEquals("testStringKey", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidKeyDeserialization() throws IOException {
 String key = "invalidKey";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 fail("Expected JsonMappingException but got: " + result.getClass());
 }

 @Test
 public void testBooleanKeyDeserialization() throws IOException {
 String keyTrue = "true";
 Object resultTrue = _keyDeserializer.deserializeKey(keyTrue, _context);
 assertTrue((Boolean) resultTrue);

 String keyFalse = "false";
 Object resultFalse = _keyDeserializer.deserializeKey(keyFalse, _context);
 assertFalse((Boolean) resultFalse);
 }

 @Test
 public void testByteKeyDeserialization() throws IOException {
 String key = "127";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 assertEquals((Byte) (byte) 127, result);
 }

 @Test(expected = IOException.class)
 public void testByteKeyDeserializationOverflow() throws IOException {
 String key = "256";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 fail("Expected IOException but got: " + result.getClass());
 }

 @Test
 public void testShortKeyDeserialization() throws IOException {
 String key = "32767";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 assertEquals((Short) (short) 32767, result);
 }

 @Test(expected = IOException.class)
 public void testShortKeyDeserializationOverflow() throws IOException {
 String key = "32768";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 fail("Expected IOException but got: " + result.getClass());
 }

 @Test
 public void testCharKeyDeserialization() throws IOException {
 String key = "a";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 assertEquals((Character) 'a', result);
 }

 @Test(expected = IOException.class)
 public void testCharKeyDeserializationInvalidLength() throws IOException {
 String key = "ab";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 fail("Expected IOException but got: " + result.getClass());
 }

 @Test
 public void testIntKeyDeserialization() throws IOException {
 String key = "12345";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 assertEquals(12345, result);
 }

 @Test
 public void testLongKeyDeserialization() throws IOException {
 String key = "12345678901234567890";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 assertEquals(12345678901234567890L, result);
 }

 @Test(expected = IOException.class)
 public void testLongKeyDeserializationOverflow() throws IOException {
 String key = "9223372036854775808"; // Long.MAX_VALUE + 1
 Object result = _keyDeserializer.deserializeKey(key, _context);
 fail("Expected IOException but got: " + result.getClass());
 }

 @Test
 public void testFloatKeyDeserialization() throws IOException {
 String key = "1.2345";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 assertEquals(1.2345f, result);
 }

 @Test(expected = IOException.class)
 public void testFloatKeyDeserializationInvalidFormat() throws IOException {
 String key = "abcd.efg";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 fail("Expected IOException but got: " + result.getClass());
 }

 @Test
 public void testDoubleKeyDeserialization() throws IOException {
 String key = "1.23456789";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 assertEquals(1.23456789, result, 0.000001);
 }

 @Test(expected = IOException.class)
 public void testDoubleKeyDeserializationInvalidFormat() throws IOException {
 String key = "abcd.efg";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 fail("Expected IOException but got: " + result.getClass());
 }

 @Test
 public void testLocaleKeyDeserialization() throws IOException {
 String key = "en_US";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 assertEquals(Locale.US, result);
 }

 @Test(expected = IOException.class)
 public void testLocaleKeyDeserializationInvalidFormat() throws IOException {
 String key = "en_US_Invalid";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 fail("Expected IOException but got: " + result.getClass());
 }

 @Test
 public void testDateKeyDeserialization() throws IOException {
 String key = "2022-12-31";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 assertEquals(new Date(2022, 11, 31), result);
 }

 @Test(expected = IOException.class)
 public void testDateKeyDeserializationInvalidFormat() throws IOException {
 String key = "2022-12-31-Invalid";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 fail("Expected IOException but got: " + result.getClass());
 }

 @Test
 public void testCalendarKeyDeserialization() throws IOException {
 String key = "2022-12-31";
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date(2022, 11, 31));
 Object result = _keyDeserializer.deserializeKey(key, _context);
 assertEquals(calendar, result);
 }

 @Test(expected = IOException.class)
 public void testCalendarKeyDeserializationInvalidFormat() throws IOException {
 String key = "2022-12-31-Invalid";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 fail("Expected IOException but got: " + result.getClass());
 }

 @Test
 public void testURIKeyDeserialization() throws IOException, MalformedURLException {
 String key = "https://www.example.com";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 assertEquals(new URI(key), result);
 }

 @Test(expected = IOException.class)
 public void testURIKeyDeserializationInvalidFormat() throws IOException {
 String key = "https://www.example.com-Invalid";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 fail("Expected IOException but got: " + result.getClass());
 }

 @Test
 public void testURLKeyDeserialization() throws IOException, MalformedURLException {
 String key = "https://www.example.com";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 assertEquals(new URL(key), result);
 }

 @Test(expected = IOException.class)
 public void testURLKeyDeserializationInvalidFormat() throws IOException {
 String key = "https://www.example.com-Invalid";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 fail("Expected IOException but got: " + result.getClass());
 }

 @Test
 public void testClassKeyDeserialization() throws IOException, ClassNotFoundException {
 String key = "java.lang.String";
 Class<?> result = (Class<?>) _keyDeserializer.deserializeKey(key, _context);
 assertEquals(String.class, result);
 }

 @Test(expected = IOException.class)
 public void testClassKeyDeserializationInvalidFormat() throws IOException {
 String key = "java.lang.StringInvalid";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 fail("Expected IOException but got: " + result.getClass());
 }

 @Test
 public void testEnumKeyDeserialization() throws IOException {
 AnnotatedClass ac = _context.findAnnotationIntrospector().findAnnotation(Enum.class, EnumSet.of(AnnotatedMember.CLASS));
 Enum<?>[] enumConstants = ac.getMemberValues();
 String key = enumConstants[0].getSimpleName().toUpperCase();
 Object result = _keyDeseralizer.deserializeKey(key, _context);
 assertEquals(enumConstants[0], result);
 }

 @Test(expected = JsonMappingException.class)
 public void testEnumKeyDeserializationUnknownValue() throws IOException {
 String key = "XyzEnumValue";
 Object result = _keyDeserializer.deserializeKey(key, _context);
 fail("Expected JsonMappingException but got: " + result.getClass());
 }

 @Test
 public void testEnumKeyDeserializationUsingToStringMethod() throws IOException {
 AnnotatedClass ac = _context.findAnnotationIntrospector().findAnnotation(Enum.class, EnumSet.of(AnnotatedMember.CLASS));
 String enumClassName = ac.getRawMember().getName();
 Class<?> enumClass = Class.forName(enumClassName);
 String key = enumClass.getSimpleName();
 Object result = _keyDeserializer.deserializeKey(key, _context);
 assertEquals(enumClass, result);
 }
}