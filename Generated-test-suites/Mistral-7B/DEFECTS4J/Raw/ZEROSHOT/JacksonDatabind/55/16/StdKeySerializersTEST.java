package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

public class StdKeySerializersTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws IOException {
 mapper = new ObjectMapper();
 }

 @Test
 public void testGetStdKeySerializer() throws IOException {
 // Test with null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(),
 null, true);
 assertNotNull(serializer);
 // Test with String rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, true);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
 // Test with int rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), int.class, true);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 // Test with Class rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Class.class, true);
 assertEquals(StdKeySerializers.DEFAULT, serializer);
 // Test with Date rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, true);
 assertEquals(StdKeySerializers.Default.TYPE_DATE, ((StdKeySerializers.Default) serializer)._typeId);
 // Test with Calendar rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, true);
 assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, ((StdKeySerializers.Default) serializer)._typeId);
 // Test with UUID rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), java.util.UUID.class, true);
 assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) serializer)._typeId);
 // Test with custom class rawKeyType
 MyCustomClass customClass = new MyCustomClass();
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), customClass.getClass(), true);
 assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer() throws IOException {
 // Test with null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), null);
 assertNull(serializer);
 // Test with String rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), String.class);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
 // Test with int rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), int.class);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 // Test with Class rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Class.class);
 assertEquals(StdKeySerializers.DEFAULT, serializer);
 // Test with Date rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Date.class);
 assertEquals(StdKeySerializers.Default.TYPE_DATE, ((StdKeySerializers.Default) serializer)._typeId);
 // Test with Calendar rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Calendar.class);
 assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, ((StdKeySerializers.Default) serializer)._typeId);
 // Test with UUID rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), java.util.UUID.class);
 assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) serializer)._typeId);
 // Test with Enum rawKeyType
 Class<MyCustomEnum> enumClass = MyCustomEnum.class;
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), enumClass);
 assertEquals(StdKeySerializers.Default.TYPE_ENUM, ((StdKeySerializers.Default) serializer)._typeId);
 }

 @Test
 public void testDefaultKeySerializer() {
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER,
 StdKeySerializers.getDefault());
 }

 @Test
 public void testSerializeWithDefaultKeySerializer() throws IOException {
 ObjectWriter writer = mapper.writer();
 String input = "{\"key\":1}";
 ObjectNode node = mapper.readValue(input, ObjectNode.class);

 node.put("key", new Date());
 String output = writer.writeValueAsString(node);
 assertTrue(output.contains("\"key\":\"" + new Date().toISO8601String() + "\""));
 }

 @Test
 public void testSerializeWithStringKeySerializer() throws IOException {
 ObjectWriter writer = mapper.writer();
 String input = "{\"key\":\"stringKey\"}";
 ObjectNode node = mapper.readValue(input, ObjectNode.class);

 node.put("key", "newStringKey");
 String output = writer.writeValueAsString(node);
 assertTrue(output.contains("\"key\":\"newStringKey\""));
 }

 @Test
 public void testSerializeWithDynamicKeySerializer() throws IOException {
 ObjectWriter writer = mapper.writer();
 ObjectNode node = mapper.createObjectNode();
 node.put("key", new MyCustomClass());
 String output = writer.writeValueAsString(node);
 assertEquals("{\"key\":\"com.mypackage.MyCustomClass@1b7d3542\"}", output);
 }

 @Test
 public void testSerializeWithDateKeySerializer() throws IOException {
 ObjectWriter writer = mapper.writer();
 ObjectNode node = mapper.createObjectNode();
 node.put("key", new Date());
 String output = writer.writeValueAsString(node);
 assertEquals("{\"key\":\"2022-12-21T14:15:16.789Z\"}", output);
 }

 @Test
 public void testSerializeWithCalendarKeySerializer() throws IOException {
 ObjectWriter writer = mapper.writer();
 ObjectNode node = mapper.createObjectNode();
 Calendar calendar = Calendar.getInstance();
 node.put("key", calendar);
 String output = writer.writeValueAsString(node);
 assertEquals("{\"key\":1672867200000}", output); // 1672867200000 is Unix Timestamp for the current date
 }

 @Test
 public void testSerializeWithEnumKeySerializer() throws IOException {
 ObjectWriter writer = mapper.writer();
 ObjectNode node = mapper.createObjectNode();
 MyCustomEnum enumValue = MyCustomEnum.VALUE_1;
 node.put("key", enumValue);
 String output = writer.writeValueAsString(node);
 assertEquals("{\"key\":\"VALUE_1\"}", output);
 }

}

 class MyCustomClass {
 }

 enum MyCustomEnum {
 VALUE_1,
 VALUE_2
}