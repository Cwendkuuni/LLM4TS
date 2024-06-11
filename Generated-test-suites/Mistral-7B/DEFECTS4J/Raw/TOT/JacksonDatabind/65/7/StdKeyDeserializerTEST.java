package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testBooleanKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer booleanKD = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "true";
 Object result = booleanKD.deserializeKey(key, context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 key = "false";
 result = booleanKD.deserializeKey(key, context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 key = "invalid";
 result = booleanKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testByteKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer byteKD = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "127";
 Object result = byteKD.deserializeKey(key, context);
 assertTrue(result instanceof Byte);
 assertEquals((Byte) 127, result);
 key = "-128";
 result = byteKD.deserializeKey(key, context);
 assertTrue(result instanceof Byte);
 assertEquals((Byte) -128, result);
 key = "256";
 result = byteKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testShortKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer shortKD = StdKeyDeserializer.forType(Short.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "32767";
 Object result = shortKD.deserializeKey(key, context);
 assertTrue(result instanceof Short);
 assertEquals((Short) 32767, result);
 key = "-32768";
 result = shortKD.deserializeKey(key, context);
 assertTrue(result instanceof Short);
 assertEquals((Short) -32768, result);
 key = "32768";
 result = shortKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testCharKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer charKD = StdKeyDeserializer.forType(Character.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "A";
 Object result = charKD.deserializeKey(key, context);
 assertTrue(result instanceof Character);
 assertEquals((Character) 'A', result);
 key = "invalid";
 result = charKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testIntKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "42";
 Object result = intKD.deserializeKey(key, context);
 assertTrue(result instanceof Integer);
 assertEquals((Integer) 42, result);
 key = "Integer.MIN_VALUE";
 result = intKD.deserializeKey(key, context);
 assertTrue(result instanceof Integer);
 assertEquals(Integer.MIN_VALUE, result);
 key = "Integer.MAX_VALUE";
 result = intKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testLongKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer longKD = StdKeyDeserializer.forType(Long.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "9223372036854775807";
 Object result = longKD.deserializeKey(key, context);
 assertTrue(result instanceof Long);
 assertEquals((Long) 9223372036854775807L, result);
 key = "-9223372036854775808";
 result = longKD.deserializeKey(key, context);
 assertTrue(result instanceof Long);
 assertEquals((Long) -9223372036854775808L, result);
 key = "9223372036854775808";
 result = longKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testFloatKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer floatKD = StdKeyDeserializer.forType(Float.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "3.14159265";
 Object result = floatKD.deserializeKey(key, context);
 assertTrue(result instanceof Float);
 assertEquals((Float) 3.14159265F, result);
 key = "Float.NaN";
 result = floatKD.deserializeKey(key, context);
 assertTrue(result instanceof Float);
 assertEquals(Float.NaN, result);
 key = "Float.POSITIVE_INFINITY";
 result = floatKD.deserializeKey(key, context);
 assertTrue(result instanceof Float);
 assertEquals(Float.POSITIVE_INFINITY, result);
 key = "Float.NEGATIVE_INFINITY";
 result = floatKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testDoubleKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "6.283185307179586";
 Object result = doubleKD.deserializeKey(key, context);
 assertTrue(result instanceof Double);
 assertEquals((Double) 6.283185307179586, result);
 key = "Double.NaN";
 result = doubleKD.deserializeKey(key, context);
 assertTrue(result instanceof Double);
 assertEquals(Double.NaN, result);
 key = "Double.POSITIVE_INFINITY";
 result = doubleKD.deserializeKey(key, context);
 assertTrue(result instanceof Double);
 assertEquals(Double.POSITIVE_INFINITY, result);
 key = "Double.NEGATIVE_INFINITY";
 result = doubleKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testLocaleKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "en_US";
 Object result = localeKD.deserializeKey(key, context);
 assertTrue(result instanceof Locale);
 assertEquals(Locale.US, result);
 key = "invalid_locale";
 result = localeKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testDateKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer dateKD = StdKeyDeserializer.forType(Date.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "2022-12-31T23:59:59.999Z";
 Object result = dateKD.deserializeKey(key, context);
 assertTrue(result instanceof Date);
 assertEquals(new Date(1671542399999L), result);
 key = "invalid_date";
 result = dateKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testCalendarKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer calendarKD = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "2022-12-31";
 Object result = calendarKD.deserializeKey(key, context);
 assertTrue(result instanceof Calendar);
 assertEquals(Calendar.getInstance().getTime(), result.getTime());
 key = "invalid_date";
 result = calendarKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testUUIDKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer uuidKD = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "00000000-0000-0000-0000-000000000001";
 Object result = uuidKD.deserializeKey(key, context);
 assertTrue(result instanceof UUID);
 assertEquals(UUID.fromString(key), result);
 key = "invalid_uuid";
 result = uuidKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testURIKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "http://example.com/path?query=string";
 Object result = uriKD.deserializeKey(key, context);
 assertTrue(result instanceof URI);
 assertEquals(new URI(key), result);
 key = "invalid_uri";
 result = uriKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testURLKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer urlKD = StdKeyDeserializer.forType(URL.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "http://example.com/path?query=string";
 Object result = urlKD.deserializeKey(key, context);
 assertTrue(result instanceof URL);
 assertEquals(new URL(key), result);
 key = "invalid_url";
 result = urlKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testClassKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer classKD = StdKeyDeserializer.forType(String.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "java.lang.String";
 Object result = classKD.deserializeKey(key, context);
 assertTrue(result instanceof Class);
 assertEquals(String.class, result);
 key = "invalid_class";
 result = classKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testEnumKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer enumKD = StdKeyDeserializer.forType(MyEnum.class);
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "MY_ENUM_VALUE";
 Object result = enumKD.deserializeKey(key, context);
 assertTrue(result instanceof MyEnum);
 assertEquals(MyEnum.MY_ENUM_VALUE, result);
 key = "invalid_enum_value";
 result = enumKD.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testDelegatingKeyDeserialization() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer delegatingKD = new DelegatingKD(String.class, mapper.findValueDeserializer(String.class));
 DeserializationContext context = mapper.getDeserializationContext();
 String key = "value";
 Object result = delegatingKD.deserializeKey(key, context);
 assertTrue(result instanceof String);
 assertEquals("value", result);
 key = "invalid_value";
 result = delegatingKD.deserializeKey(key, context);
 assertNull(result);
 }
}