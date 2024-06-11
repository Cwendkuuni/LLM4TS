package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

public class StdKeySerializersTest {

 private ObjectMapper mapper;

 @BeforeClass
 public static void setupClass() {
 ObjectMapper objectMapper = new ObjectMapper();
 StdKeySerializers.mapper = objectMapper;
 }

 @Before
 public void setup() {
 mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 mapper.findAndRegisterModules();
 }

 @Test
 public void testGetStdKeySerializer() {
 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNotNull(serializer);
 assertSame(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

 rawKeyType = java.util.Date.class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNotNull(serializer);

 rawKeyType = Calendar.class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNotNull(serializer);

 rawKeyType = java.util.UUID.class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNotNull(serializer);

 rawKeyType = EnumSet.class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNotNull(serializer);

 rawKeyType = Number.class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = boolean.class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = int.class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = long.class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = float.class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = double.class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = char.class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = byte.class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = short.class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = boolean[].class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = int[].class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = long[].class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = float[].class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = double[].class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = char[].class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = byte[].class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);

 rawKeyType = short[].class;
 serializer = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);
 assertNull(serializer);
 }

 @Test
 public void testGetFallbackKeySerializer() {
 Class<?> rawKeyType = Enum.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, rawKeyType);
 assertNotNull(serializer);
 assertSame(StdKeySerializers.getDefault(), serializer);

 rawKeyType = null;
 serializer = StdKeySerializers.getFallbackKeySerializer(null, rawKeyType);
 assertNull(serializer);
 }

 @Test
 public void testGetDefault() {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 assertNotNull(serializer);
 assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 @Test
 public void testDefaultSerializeDateKey() throws JsonProcessingException {
 Date date = new Date();
 JsonGenerator gen = mapper.getFactory().createGenerator();
 StdKeySerializers.Default serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
 serializer.serialize(date, gen, mapper.getSerializerProvider());
 assertEquals("\"2023-03-22T15:14:53.831Z\"", gen.getOutputBuffer().toString());
 }

 @Test
 public void testDefaultSerializeCalendarKey() throws JsonProcessingException {
 Calendar calendar = Calendar.getInstance();
 JsonGenerator gen = mapper.getFactory().createGenerator();
 StdKeySerializers.Default serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
 serializer.serialize(calendar, gen, mapper.getSerializerProvider());
 assertEquals("\"2023-03-22T15:14:53.831Z\"", gen.getOutputBuffer().toString());
 }

 @Test
 public void testDynamicSerializeNullValue() throws JsonProcessingException {
 Object value = null;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
 dynamicSerializer.serialize(value, gen, mapper.getSerializerProvider());
 assertEquals("null", gen.getOutputBuffer().toString());
 }

 @Test
 public void testDynamicSerializeStringValue() throws JsonProcessingException {
 String value = "test";
 JsonGenerator gen = mapper.getFactory().createGenerator();
 StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
 dynamicSerializer.serialize(value, gen, mapper.getSerializerProvider());
 assertEquals("\"test\"", gen.getOutputBuffer().toString());
 }

 @Test
 public void testDynamicSerializeEnumValue() throws JsonProcessingException {
 Enum value = Enum. values()[0];
 JsonGenerator gen = mapper.getFactory().createGenerator();
 StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
 dynamicSerializer.serialize(value, gen, mapper.getSerializerProvider());
 assertEquals("\"EAGER\"", gen.getOutputBuffer().toString());
 }

 @Test
 public void testDynamicSerializeClassValue() throws JsonProcessingException {
 Class<?> value = String.class;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
 dynamicSerializer.serialize(value, gen, mapper.getSerializerProvider());
 assertEquals("\"java.lang.String\"", gen.getOutputBuffer().toString());
 }

 @Test
 public void testDefaultSerializeClassKey() throws JsonProcessingException {
 Class<?> value = String.class;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 StdKeySerializers.Default serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, String.class);
 serializer.serialize(value, gen, mapper.getSerializerProvider());
 assertEquals("\"java.lang.String\"", gen.getOutputBuffer().toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testDefaultSerializeIncompatibleClassKey() throws JsonProcessingException {
 Class<?> value = int.class;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 StdKeySerializers.Default serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, String.class);
 serializer.serialize(value, gen, mapper.getSerializerProvider());
 fail("Expected JsonMappingException");
 }
}