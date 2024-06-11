package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {

 private StdKeyDeserializer<String> stringKD;
 private StdKeyDeserializer<Boolean> booleanKD;
 private StdKeyDeserializer<Byte> byteKD;
 private StdKeyDeserializer<Short> shortKD;
 private StdKeyDeserializer<Character> charKD;
 private StdKeyDeserializer<Integer> intKD;
 private StdKeyDeserializer<Long> longKD;
 private StdKeyDeserializer<Float> floatKD;
 private StdKeyDeserializer<Double> doubleKD;
 private StdKeyDeserializer<Locale> localeKD;
 private StdKeyDeserializer<Date> dateKD;
 private StdKeyDeserializer<Calendar> calendarKD;
 private StdKeyDeserializer<UUID> uuidKD;
 private StdKeyDeserializer<URI> uriKD;
 private StdKeyDeserializer<URL> urlKD;
 private StdKeyDeserializer<Class<?>> classKD;
 private StdKeyDeserializer<Enum> enumKD;

 @Before
 public void setup() throws IOException, JsonProcessingException {
 stringKD = StdKeyDeserializer.forType(String.class);
 booleanKD = StdKeyDeserializer.forType(Boolean.class);
 byteKD = StdKeyDeserializer.forType(Byte.class);
 shortKD = StdKeyDeserializer.forType(Short.class);
 charKD = StdKeyDeserializer.forType(Character.class);
 intKD = StdKeyDeserializer.forType(Integer.class);
 longKD = StdKeyDeserializer.forType(Long.class);
 floatKD = StdKeyDeserializer.forType(Float.class);
 doubleKD = StdKeyDeserializer.forType(Double.class);
 localeKD = StdKeyDeserializer.forType(Locale.class);
 dateKD = StdKeyDeserializer.forType(Date.class);
 calendarKD = StdKeyDeserializer.forType(Calendar.class);
 uuidKD = StdKeyDeserializer.forType(UUID.class);
 uriKD = StdKeyDeserializer.forType(URI.class);
 urlKD = StdKeyDeserializer.forType(URL.class);
 classKD = StdKeyDeserializer.forType(Class.class);
 enumKD = new StdKeyDeserializer(EnumResolver.constructFromAnnotation(Locale.class), null);
 }

 @Test
 public void testBoolean() throws JsonProcessingException {
 assertEquals(Boolean.TRUE, stringKD.deserializeKey("true", null));
 assertEquals(Boolean.FALSE, stringKD.deserializeKey("false", null));
 assertNull(stringKD.deserializeKey("invalid", null));
 }

 @Test
 public void testByte() throws JsonProcessingException {
 assertEquals((byte) 1, intKD.deserializeKey("1", null));
 assertEquals((byte) 127, intKD.deserializeKey("127", null));
 assertNull(intKD.deserializeKey("-1", null));
 assertNull(intKD.deserializeKey("256", null));
 }

 @Test
 public void testShort() throws JsonProcessingException {
 assertEquals((short) 1, intKD.deserializeKey("1", null));
 assertEquals((short) 32767, intKD.deserializeKey("32767", null));
 assertNull(intKD.deserializeKey("-1", null));
 assertNull(intKD.deserializeKey("32768", null));
 }

 @Test
 public void testChar() throws JsonProcessingException {
 assertEquals('a', charKD.deserializeKey("a", null));
 assertNull(charKD.deserializeKey("1", null));
 }

 @Test
 public void testInteger() throws JsonProcessingException {
 assertEquals(1, intKD.deserializeKey("1", null));
 assertEquals(-1, intKD.deserializeKey("-1", null));
 assertEquals(Integer.MAX_VALUE, intKD.deserializeKey("2147483647", null));
 assertEquals(Integer.MIN_VALUE, intKD.deserializeKey("-2147483648", null));
 }

 @Test
 public void testLong() throws JsonProcessingException {
 assertEquals(1L, longKD.deserializeKey("1", null));
 assertEquals(-1L, longKD.deserializeKey("-1", null));
 assertEquals(Long.MAX_VALUE, longKD.deserializeKey("9223372036854775807", null));
 assertEquals(Long.MIN_VALUE, longKD.deserializeKey("-9223372036854775808", null));
 }

 @Test
 public void testFloat() throws JsonProcessingException {
 assertEquals(1.0f, floatKD.deserializeKey("1.0", null), 0.0f);
 assertEquals(-1.0f, floatKD.deserializeKey("-1.0", null), 0.0f);
 }

 @Test
 public void testDouble() throws JsonProcessingException {
 assertEquals(1.0, doubleKD.deserializeKey("1.0", null), 0.0);
 assertEquals(-1.0, doubleKD.deserializeKey("-1.0", null), 0.0);
 }

 @Test
 public void testLocale() throws JsonProcessingException {
 assertEquals(Locale.US, localeKD.deserializeKey("en_US", null));
 assertEquals(Locale.US, localeKD.deserializeKey("US", null));
 assertNull(localeKD.deserializeKey("invalid", null));
 }

 @Test
 public void testDate() throws JsonProcessingException, IOException {
 assertEquals(new Date(0), dateKD.deserializeKey("1970-01-01T00:00:00Z", null));
 assertEquals(new Date(System.currentTimeMillis()), dateKD.deserializeKey(null, null));
 assertNull(dateKD.deserializeKey("invalid", null));
 }

 @Test
 public void testCalendar() throws JsonProcessingException, IOException {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date(0));
 assertEquals(calendar, calendarKD.deserializeKey("1970-01-01", null));
 assertEquals(calendar, calendarKD.deserializeKey("1970-01-01T00:00:00", null));
 assertEquals(calendar, calendarKD.deserializeKey("1970-01-01Z", null));
 assertNull(calendarKD.deserializeKey("invalid", null));
 }

 @Test
 public void testUUID() throws JsonProcessingException {
 assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), uuidKD.deserializeKey("123e4567-e89b-12d3-a456-426614174000", null));
 assertNull(uuidKD.deserializeKey("invalid", null));
 }

 @Test
 public void testURI() throws JsonProcessingException {
 assertEquals(new URI("http://www.example.com"), uriKD.deserializeKey("http://www.example.com", null));
 assertNull(uriKD.deserializeKey("invalid", null));
 }

 @Test
 public void testURL() throws JsonProcessingException {
 assertEquals(new URL("http://www.example.com"), urlKD.deserializeKey("http://www.example.com", null));
 assertNull(urlKD.deserializeKey("invalid", null));
 }

 @Test
 public void testEnum() throws JsonProcessingException, IOException {
 AnnotatedClass ac = new AnnotatedClass(Locale.class, null);
 AnnotatedMethod am = ac.findMethod("getValue", new Class[0]);
 Object[] args = new Object[0];
 StdKeyDeserializer delegate = enumKD;
 StdKeyDeserializer eKD = new StdKeyDeserializer(EnumResolver.constructFromAnnotation(Locale.class), am, delegate);
 assertEquals(Locale.US, eKD.deserializeKey("ENGLISH", null));
 assertNull(eKD.deserializeKey("invalid", null));
 }

 @Test
 public void testClass() throws JsonProcessingException, IOException, ClassNotFoundException {
 assertEquals(String.class, classKD.deserializeKey("java.lang.String", null));
 assertNull(classKD.deserializeKey("invalid", null));
 }
}