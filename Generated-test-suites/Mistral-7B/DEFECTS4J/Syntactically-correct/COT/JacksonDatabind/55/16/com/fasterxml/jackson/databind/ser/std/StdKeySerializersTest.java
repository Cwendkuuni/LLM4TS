package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;

import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
public class StdKeySerializersTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testGetStdKeySerializer() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> expected = new StringKeySerializer();
 JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertEquals(expected, result);
 }

 @Test
 public void testGetStdKeySerializerWithDefault() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> expected = StdKeySerializers.DEFAULT_STRING_SERIALIZER;
 JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
 assertEquals(expected, result);
 }

 @Test
 public void testGetStdKeySerializerWithNullType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, null, false);
 assertNull(result);
 }

 @Test
 public void testGetStdKeySerializerWithUnknownType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = new Object() {}.getClass();
 JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
 assertNull(result);
 }

 @Test
 public void testGetFallbackKeySerializer() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> expected = new StringKeySerializer();
 JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertEquals(expected, result);
 }

 @Test
 public void testGetFallbackKeySerializerWithEnumType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Enum.class;
 JsonSerializer<Object> expected = StdKeySerializers.getDefault();
 JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertEquals(expected, result);
 }

 @Test
 public void testGetFallbackKeySerializerWithNullType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, null);
 assertNull(result);
 }

 @Test
 public void testGetDefault() throws Exception {
 JsonSerializer<Object> result = StdKeySerializers.getDefault();
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
 }

 @Test
 public void testDynamicSerializer() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 PropertySerializerMap propertySerializerMap = Mockito.mock(PropertySerializerMap.class);
 Dynamic dynamicSerializer = new Dynamic();
 JsonSerializer<Object> result = dynamicSerializer.serialize(new UUID(1, 2), mapper.getSerializerProvider());
 assertEquals(String.class, result.getClass());
 }

 @Test
 public void testGetFallbackKeySerializerWithClassType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Class.class;
 JsonSerializer<Object> expected = new Default(Default.TYPE_CLASS, rawKeyType);
 JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertEquals(expected, result);
 }

 @Test
 public void testGetFallbackKeySerializerWithDateType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Date.class;
 JsonSerializer<Object> expected = new Default(Default.TYPE_DATE, rawKeyType);
 JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertEquals(expected, result);
 }

 @Test
 public void testGetFallbackKeySerializerWithCalendarType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = Calendar.class;
 JsonSerializer<Object> expected = new Default(Default.TYPE_CALENDAR, rawKeyType);
 JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertEquals(expected, result);
 }

 @Test
 public void testGetFallbackKeySerializerWithUUIDType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Class<?> rawKeyType = UUID.class;
 JsonSerializer<Object> expected = new Default(Default.TYPE_TO_STRING, rawKeyType);
 JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
 assertEquals(expected, result);
 }

 @Test
 public void testDefaultSerializerWithDateType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 Date date = new Date();
 ObjectNode resultNode = mapper.writeValueAsTree(date, new Default(Default.TYPE_DATE, Date.class));
 assertEquals("\"2022-12-12T12:12:12.123Z\"", resultNode.get("key").asText());
 }

 @Test
 public void testDefaultSerializerWithCalendarType() throws Exception {
 Calendar calendar = Calendar.getInstance();
 ObjectNode resultNode = mapper.writeValueAsTree(calendar, new Default(Default.TYPE_CALENDAR, Calendar.class));
 assertEquals("\"2022-12-12T12:12:12.123Z\"", resultNode.get("key").asText());
 }

 @Test
 public void testDefaultSerializerWithStringType() throws Exception {
 String key = "test";
 ObjectNode resultNode = mapper.writeValueAsTree(key, new Default(Default.TYPE_TO_STRING, String.class));
 assertEquals("\"test\"", resultNode.get("key").asText());
 }

 @Test
 public void testDefaultSerializerWithEnumType() throws Exception {
 EnumSet<TestEnum> enumSet = EnumSet.allOf(TestEnum.class);
 ObjectNode resultNode = mapper.writeValueAsTree(enumSet, new Default(Default.TYPE_ENUM, TestEnum.class));
 assertEquals("[\"ELEMENT_1\", \"ELEMENT_2\", \"ELEMENT_3\"]", resultNode.get("key").asText());
 }

 @Test
 public void testDefaultSerializerWithClassType() throws Exception {
 Class<?> clazz = String[].class;
 ObjectNode resultNode = mapper.writeValueAsTree(clazz, new Default(Default.TYPE_CLASS, clazz));
 assertEquals("\"[Ljava.lang.String;\"", resultNode.get("key").asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testDynamicSerializerWithInvalidType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 ObjectNode resultNode = mapper.writeValueAsTree(new UUID(1, 2), mapper.getSerializerProvider());
 fail("Dynamic serializer should throw JsonMappingException for invalid type.");
 }

 @Test(expected = JsonMappingException.class)
 public void testDynamicSerializerWithNullType() throws Exception {
 SerializationConfig config = Mockito.mock(SerializationConfig.class);
 ObjectNode resultNode = mapper.writeValueAsTree(null, mapper.getSerializerProvider());
 fail("Dynamic serializer should throw JsonMappingException for null type.");
 }

 @Test(expected = JsonMappingException.class)
 public void testDynamicSerializerWithCircularReference() throws Exception {
 Object obj = new Object() {
 private static final long serialVersionUID = 1L;
 String field;
 };
 ObjectNode originalNode = mapper.writeValueAsTree(obj, mapper.getSerializerProvider());
 ObjectNode resultNode = mapper.writeValueAsTree(originalNode, mapper.getSerializerProvider());
 fail("Dynamic serializer should throw JsonMappingException for circular reference.");
 }

 @Test
 public void testToStringSerializerWithCustomToStringMethod() throws Exception {
 class TestObjectWithCustomToString {
 private static final long serialVersionUID = 1L;
 private String customToStringValue;
 @Override
 public String toString() {
 return customToStringValue;
 }
 }
 TestObjectWithCustomToString testObjectWithCustomToString = new TestObjectWithCustomToString();
 testObjectWithCustomToString.customToStringValue = "customToStringValue";
 ObjectNode resultNode = mapper.writeValueAsTree(testObjectWithCustomToString, new Default(Default.TYPE_TO_STRING, TestObjectWithCustomToString.class));
 assertEquals("\"customToStringValue\"", resultNode.get("key").asText());
 }

 @Test
 public void testToStringSerializerWithDefaultToStringMethod() throws Exception {
 class TestObjectWithDefaultToString {
 private static final long serialVersionUID = 1L;
 private String value;
 }
 TestObjectWithDefaultToString testObjectWithDefaultToString = new TestObjectWithDefaultToString();
 testObjectWithDefaultToString.value = "defaultToStringValue";
 ObjectNode resultNode = mapper.writeValueAsTree(testObjectWithDefaultToString, new Default(Default.TYPE_TO_STRING, TestObjectWithDefaultToString.class));
 assertEquals("\"TestObjectWithDefaultToString@15db9742\"", resultNode.get("key").asText());
 }

 @Test
 public void testToStringSerializerWithEmptyClass() throws Exception {
 ObjectNode resultNode = mapper.writeValueAsTree(new Object(), new Default(Default.TYPE_TO_STRING, Object.class));
 assertEquals("\"[B@7f37c4c8\"", resultNode.get("key").asText());
 }

 @Test
 public void testToStringSerializerWithSerializableClass() throws Exception {
 class TestObject implements Serializable {
 private static final long serialVersionUID = 1L;
 private String value;
 }
 TestObject testObject = new TestObject();
 testObject.value = "value";
 ObjectNode resultNode = mapper.writeValueAsTree(testObject, new Default(Default.TYPE_TO_STRING, TestObject.class));
 assertEquals("\"TestObject(value=value)\"", resultNode.get("key").asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testToStringSerializerWithNonSerializableClass() throws Exception {
 class TestObject {
 private static final long serialVersionUID = 1L;
 private String value;
 }
 ObjectNode resultNode = mapper.writeValueAsTree(new TestObject(), new Default(Default.TYPE_TO_STRING, TestObject.class));
 fail("ToString serializer should throw JsonMappingException for non-serializable class.");
 }
}