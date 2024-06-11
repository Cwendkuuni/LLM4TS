package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.*;

public class StdKeySerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDefaultKeySerializer() throws IOException {
 Object defaultSerializer = StdKeySerializers.getDefault();
 assertNotNull(defaultSerializer);
 assertInstanceOf(StdKeySerializer.class, defaultSerializer);
 }

 @Test
 public void testGetStringSerializer() throws IOException {
 Object stringSerializer = StdKeySerializers.getDefaultStringSerializer();
 assertNotNull(stringSerializer);
 assertInstanceOf(StringKeySerializer.class, stringSerializer);
 }

 @Test
 public void testDynamicKeySerializer() throws IOException {
 Object dynamicSerializer = StdKeySerializers.getDefault();
 assertNotNull(dynamicSerializer);
 assertInstanceOf(StdKeySerializers.Dynamic.class, dynamicSerializer);

 Object testObj = new Object() {};
 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 StdKeySerializers.Dynamic dynamic = (StdKeySerializers.Dynamic) dynamicSerializer;
 dynamic.serialize(testObj, gen, mapper.getSerializerProvider());
 String json = gen.getOutputContent();
 assertTrue(json.contains("\"[Ljava.lang.Object;\"")); // default serialization for unknown class

 PropertySerializerMap dynamicSerializers = dynamic._dynamicSerializers;
 assertNotNull(dynamicSerializers);
 JsonSerializer<Object> serializer = dynamicSerializers.findValueSerializer(testObj.getClass());
 assertNotNull(serializer);
 dynamic.serialize(testObj, gen, mapper.getSerializerProvider());
 json = gen.getOutputContent();
 assertFalse(json.contains("\"[Ljava.lang.Object;\"")); // should use the found serializer
 }

 @Test
 public void testDateKeySerializer() throws IOException {
 Date date = new Date();
 JsonSerializer<Object> dateSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(),
 Date.class, true);
 assertNotNull(dateSerializer);
 assertInstanceOf(Default.class, dateSerializer);
 assertSame(Default.TYPE_DATE, ((Default) dateSerializer)._typeId);

 ObjectWriter ow = mapper.writerWithRootValueSerializer(dateSerializer);
 String json = ow.writeValueAsString(date);
 assertEquals("\"2023-03-01T00:00:00.000Z\"", json);
 }

 @Test
 public void testCalendarKeySerializer() throws IOException {
 Calendar calendar = Calendar.getInstance();
 JsonSerializer<Object> calendarSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(),
 Calendar.class, true);
 assertNotNull(calendarSerializer);
 assertInstanceOf(Default.class, calendarSerializer);
 assertSame(Default.TYPE_CALENDAR, ((Default) calendarSerializer)._typeId);

 ObjectWriter ow = mapper.writerWithRootValueSerializer(calendarSerializer);
 String json = ow.writeValueAsString(calendar);
 assertEquals("\"Thu Mar 02 00:00:00 CET 2023\"", json);
 }

 @Test
 public void testClassKeySerializer() throws IOException {
 Class<?> clazz = String.class;
 JsonSerializer<Object> classSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(),
 clazz, true);
 assertNotNull(classSerializer);
 assertInstanceOf(Default.class, classSerializer);
 assertSame(Default.TYPE_CLASS, ((Default) classSerializer)._typeId);

 ObjectWriter ow = mapper.writerWithRootValueSerializer(classSerializer);
 String json = ow.writeValueAsString(clazz);
 assertEquals("\"java.lang.String\"", json);
 }

 @Test
 public void testEnumKeySerializer() throws IOException {
 Enum enumValue = Enum.valueOf(MyEnum.class, "VALUE_1");
 JsonSerializer<Object> enumSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(),
 enumValue.getClass(), true);
 assertNotNull(enumSerializer);
 assertInstanceOf(Default.class, enumSerializer);
 assertSame(Default.TYPE_ENUM, ((Default) enumSerializer)._typeId);

 ObjectWriter ow = mapper.writerWithRootValueSerializer(enumSerializer);
 String json = ow.writeValueAsString(enumValue);
 assertEquals("\"VALUE_1\"", json);
 }

 @Test
 public void testGetFallbackKeySerializer() throws IOException {
 Object fallbackSerializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(),
 String.class);
 assertNotNull(fallbackSerializer);
 assertInstanceOf(StringKeySerializer.class, fallbackSerializer);
 }

 @Test
 public void testGetFallbackKeySerializerForUnknownClass() throws IOException {
 Object fallbackSerializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(),
 UnknownClass.class);
 assertNull(fallbackSerializer);
 }

 @Test
 public void testGetFallbackKeySerializerForEnum() throws IOException {
 Object fallbackSerializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(),
 MyEnum.class);
 assertInstanceOf(Default.class, fallbackSerializer);
 assertSame(Default.TYPE_ENUM, ((Default) fallbackSerializer)._typeId);
 }
}

enum MyEnum {
 VALUE_1,
 VALUE_2
}

class UnknownClass {
}