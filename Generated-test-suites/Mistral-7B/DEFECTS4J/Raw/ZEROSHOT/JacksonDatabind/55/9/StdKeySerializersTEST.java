package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class StdKeySerializersTest {

 private SerializationConfig config;
 private ObjectMapper mapper;

 @Before
 public void setUp() {
 config = new SerializationConfig();
 mapper = new ObjectMapper(config);
 }

 @Test
 public void testGetStdKeySerializer() throws JsonMappingException {
 // Test with null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
 assertNotNull(serializer);

 // Test with String rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
 assertInstanceOf(StdKeySerializers.StringKeySerializer.class, serializer);

 // Test with Date rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Calendar rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Class rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Enum rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(config, Enum.class, false);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Number rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(config, Number.class, false);
 assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer);
 }

 @Test
 public void testGetFallbackKeySerializer() throws JsonMappingException {
 // Test with null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
 assertNotNull(serializer);

 // Test with String rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
 assertInstanceOf(StdKeySerializers.StringKeySerializer.class, serializer);

 // Test with Date rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(config, Date.class);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Calendar rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(config, Calendar.class);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Class rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(config, Class.class);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Enum rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);
 assertInstanceOf(StdKeySerializers.Dynamic.class, serializer);
 }

 @Test
 public void testSerializeWithDefaultSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 Object testObject = new TestObject("testKey", 123);
 String json = mapper.writeValueAsString(new Object[] { testObject, testObject });

 JsonParser parser = mapper.getFactory().createParser(json);
 Object readValue = parser.readValueAs(Object[].class);

 assertNotNull(readValue);
 assertNotNull(readValue[0]);
 assertInstanceOf(TestObject.class, readValue[0]);
 assertInstanceOf(TestObject.class, readValue[1]);
 }

 @Test
 public void testSerializeWithCustomSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.setKeySerializer(StdKeySerializers.StringKeySerializer.class);

 TestObject testObject = new TestObject("testKey", 123);
 String json = mapper.writeValueAsString(new Object[] { testObject, testObject });

 JsonParser parser = mapper.getFactory().createParser(json);
 Object readValue = parser.readValueAs(Object[].class);

 assertNotNull(readValue);
 assertNotNull(readValue[0]);
 assertInstanceOf(TestObject.class, readValue[0]);
 assertNotNull(readValue[1]);
 assertInstanceOf(TestObject.class, readValue[1]);
 assertNotSame(readValue[0], readValue[1]);
 }

 private static class TestObject {
 private String key;
 private int value;

 public TestObject(String key, int value) {
 this.key = key;
 this.value = value;
 }

 @Override
 public String toString() {
 return key;
 }
 }
}