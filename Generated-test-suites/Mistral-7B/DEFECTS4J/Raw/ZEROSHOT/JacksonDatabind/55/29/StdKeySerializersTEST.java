package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

 @Test
 public void testGetStdKeySerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();

 // Test null input
 SerializationConfig config = mapper.getSerializationConfig();
 assertNull(StdKeySerializers.getStdKeySerializer(config, null, false));

 // Test String input
 Class<String> stringClass = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, stringClass, false);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.StringKeySerializer.class, serializer);

 // Test Date input
 Class<Date> dateClass = Date.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, dateClass, false);
 assertNotNull(serializer);
 assertInstanceOf(Default.class, serializer);

 // Test Calendar input
 Class<Calendar> calendarClass = Calendar.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, calendarClass, false);
 assertNotNull(serializer);
 assertInstanceOf(Default.class, serializer);

 // Test Number input
 Class<Double> numberClass = Double.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, numberClass, false);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer);

 // Test Class input
 Class<Class<?>> classClass = Class.class;
 serializer = StdKeySerializers.getStdKeySerializer(config, classClass, false);
 assertNotNull(serializer);
 assertInstanceOf(Default.class, serializer);
 }

 @Test
 public void testGetFallbackKeySerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();

 // Test null input
 SerializationConfig config = mapper.getSerializationConfig();
 assertNull(StdKeySerializers.getFallbackKeySerializer(config, null));

 // Test Enum input
 Class<Enum> enumClass = Enum.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, enumClass);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 @Test
 public void testDefaultKeySerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();

 // Test null input
 SerializationConfig config = mapper.getSerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer);

 // Test Date input
 Date date = new Date();
 SerializerProvider provider = mapper.getDefaultSerializerProvider();
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 StdKeySerializers.DEFAULT_KEY_SERIALIZER.serialize(date, generator, provider);
 generator.close();

 // Test Calendar input
 Calendar calendar = Calendar.getInstance();
 provider = mapper.getDefaultSerializerProvider();
 generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 StdKeySerializers.DEFAULT_KEY_SERIALIZER.serialize(calendar.getTimeInMillis(), generator, provider);
 generator.close();
 }

 @Test
 public void testDynamicKeySerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 PropertySerializerMap serializers = mapper.getDefaultSerializerProvider().getSerializerProvider().getSerializers();

 // Test null input
 JsonSerializer<Object> serializer = StdKeySerializers.Dynamic.class.newInstance();
 assertNull(serializer.getValueSerializer());

 // Test String input
 String string = "Test";
 serializer = (JsonSerializer<Object>) StdKeySerializers.Dynamic.class.newInstance();
 serializer.setValueSerializer(mapper.findValueSerializer(String.class, null));
 assertEquals(string, serializer.getValueSerializer().getValueAsString());
 StdKeySerializers.Dynamic dynamic = (StdKeySerializers.Dynamic) serializer;
 assertEquals(String.class, dynamic.getValueSerializer().getType());
 StdKeySerializers.Dynamic.serialize(string, new JsonGenerator(mapper.getFactory()), mapper.getDefaultSerializerProvider());

 // Test Integer input
 Integer integer = 1;
 serializer = (JsonSerializer<Object>) StdKeySerializers.Dynamic.class.newInstance();
 serializer.setValueSerializer(mapper.findValueSerializer(Integer.class, null));
 assertEquals(integer, serializer.getValueSerializer().getValueAsInt());
 StdKeySerializers.Dynamic dynamicInteger = (StdKeySerializers.Dynamic) serializer;
 assertEquals(Integer.class, dynamicInteger.getValueSerializer().getType());
 StdKeySerializers.Dynamic.serialize(integer, new JsonGenerator(mapper.getFactory()), mapper.getDefaultSerializerProvider());
}
}