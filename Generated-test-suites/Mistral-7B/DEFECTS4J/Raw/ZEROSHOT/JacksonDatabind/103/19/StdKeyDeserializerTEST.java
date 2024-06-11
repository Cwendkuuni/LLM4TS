package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.XmlParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 }

 @Test
 public void testBooleanKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
 Object result = deserializer.deserializeKey("true", mapper.getDeserializationContext());
 assertEquals(Boolean.TRUE, result);

 result = deserializer.deserializeKey("false", mapper.getDeserializationContext());
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testByteKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
 Object result = deserializer.deserializeKey("127", mapper.getDeserializationContext());
 assertEquals((byte) 127, result);

 result = deserializer.deserializeKey("-128", mapper.getDeserializationContext());
 assertEquals((byte) -128, result);
 }

 @Test
 public void testShortKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
 Object result = deserializer.deserializeKey("32767", mapper.getDeserializationContext());
 assertEquals((short) 32767, result);

 result = deserializer.deserializeKey("-32768", mapper.getDeserializationContext());
 assertEquals((short) -32768, result);
 }

 @Test
 public void testCharKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
 Object result = deserializer.deserializeKey("A", mapper.getDeserializationContext());
 assertEquals('A', result);
 }

 @Test
 public void testIntKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
 Object result = deserializer.deserializeKey("123456", mapper.getDeserializationContext());
 assertEquals(123456, result);
 }

 @Test
 public void testLongKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
 Object result = deserializer.deserializeKey("12345678901234567890", mapper.getDeserializationContext());
 assertEquals(12345678901234567890L, result);
 }

 @Test
 public void testFloatKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
 Object result = deserializer.deserializeKey("123.456", mapper.getDeserializationContext());
 assertEquals(123.456f, result);
 }

 @Test
 public void testDoubleKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
 Object result = deserializer.deserializeKey("123.456.789", mapper.getDeserializationContext());
 assertEquals(123.456789, result);
 }

 @Test
 public void testLocaleKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
 Object result = deserializer.deserializeKey("en_US", mapper.getDeserializationContext());
 assertEquals(Locale.US, result);
 }

 @Test
 public void testDateKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
 Object result = deserializer.deserializeKey("2022-03-16", mapper.getDeserializationContext());
 assertNotNull(result);
 }

 @Test
 public void testCalendarKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
 Object result = deserializer.deserializeKey("2022-03-16", mapper.getDeserializationContext());
 assertNotNull(result);
 }

 @Test
 public void testUriKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
 Object result = deserializer.deserializeKey("http://example.com/", mapper.getDeserializationContext());
 assertEquals(new URI("http://example.com/"), result);
 }

 @Test
 public void testUrlKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
 Object result = deserializer.deserializeKey("http://example.com/", mapper.getDeserializationContext());
 assertEquals(new URL("http://example.com/"), result);
 }

 @Test
 public void testClassKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
 Object result = deserializer.deserializeKey("[Ljava.lang.String;", mapper.getDeserializationContext());
 assertNotNull(result);
 }

 @Test
 public void testEnumKeyDeserializer() throws IOException {
 @JsonDeserialize(using = StdKeyDeserializer.EnumKD.class)
 enum TestEnum {
 A, B, C
 }
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);
 Object result = deserializer.deserializeKey("C", mapper.getDeserializationContext());
 assertEquals(TestEnum.C, result);
 }

 @Test
 public void testCustomKeyDeserializer() throws IOException {
 class CustomKey {
 private final String name;

 CustomKey(String name) {
 this.name = name;
 }

 public String getName() {
 return name;
 }
 }

 StdKeyDeserializer deserializer = new StdKeyDeserializer(3, CustomKey.class) {
 @Override
 public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
 Object result;
 try {
 result = mapper.constructValue(CustomKey.class, new Object[] { key });
 } catch (IllegalArgumentException e) {
 result = ctxt.handleWeirdKey(CustomKey.class, key, "Invalid key: %s", e.getMessage());
 }
 return result;
 }
 };
 Object result = deserializer.deserializeKey("test", mapper.getDeserializationContext());
 assertEquals("test", result.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
 deserializer.deserializeKey("InvalidKey", mapper.getDeserializationContext());
 }

 @Test
 public void testWeirdKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
 Object result = deserializer.deserializeKey("InvalidKey: WeirdValue", mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testStringKeyDeserializer() throws IOException {
 StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
 Object result = deserializer.deserializeKey("test", mapper.getDeserializationContext());
 assertEquals("test", result);
 }
}