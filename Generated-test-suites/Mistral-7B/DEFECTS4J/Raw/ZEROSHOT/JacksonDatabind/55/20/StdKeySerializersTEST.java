package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializersTest {

 private ObjectMapper mapper;

 @BeforeClass
 public static void setupClass() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testGetStdKeySerializer() throws Exception {
 // Test with null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), null, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Dynamic.class, serializer);

 // Test with String rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.StringKeySerializer.class, serializer);

 // Test with int rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), int.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.DefaultSerializer.class, serializer);

 // Test with Class rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Class.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Date rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Calendar rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with UUID rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), UUID.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 @Test
 public void testGetFallbackKeySerializer() throws Exception {
 // Test with null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), null);
 assertNull(serializer);

 // Test with Enum rawKeyType
 Class<Enum> enumClass = Enum.class;
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), enumClass);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Dynamic.class, serializer);

 // Test with int rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), int.class);
 assertInstanceOf(StdKeySerializers.DefaultSerializer.class, serializer);
 }

 @Test
 public void testDefaultSerializer() throws Exception {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 assertInstanceOf(StdKeySerializers.DefaultSerializer.class, serializer);
 }

 @Test
 public void testDefaultSerializerWithEnum() throws Exception {
 Enum testEnum = Enum.valueOf(MyEnum.class, "TEST");
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 ObjectNode result = mapper.writeValueAsTree(new Object[] { testEnum }, serializer);
 JsonNode node = result.get(0);
 String name = node.get("name").asText();
 assertEquals("TEST", name);
 }

 @Test
 public void testDynamicSerializer() throws Exception {
 ObjectMapper mapperWithDynamic = new ObjectMapper();
 mapperWithDynamic.setSerializationConfig(mapper.getSerializationConfig());
 mapperWithDynamic.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 mapperWithDynamic.addSerializer(Date.class, new StaticDateSerializer());

 // Test with Date rawKeyType
 Date date = new Date();
 ObjectNode result = mapperWithDynamic.writeValueAsTree(new Object[] { date }, new StdKeySerializers.Dynamic());
 JsonNode node = result.get(0);
 String dateString = node.get("@class").asText();
 assertEquals("java.util.Date", dateString);

 // Test with Enum rawKeyType
 MyEnum enumValue = MyEnum.VALUE1;
 ObjectNode result2 = mapperWithDynamic.writeValueAsTree(new Object[] { enumValue }, new StdKeySerializers.Dynamic());
 JsonNode node2 = result2.get(0);
 String enumName = node2.get("name").asText();
 assertEquals("VALUE1", enumName);
 }

 @Test
 public void testDynamicSerializerWithCustomSerializer() throws Exception {
 ObjectMapper mapperWithCustomSerializer = new ObjectMapper();
 mapperWithCustomSerializer.setSerializationConfig(mapper.getSerializationConfig());
 mapperWithCustomSerializer.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 mapperWithCustomSerializer.addSerializer(MyEnum.class, new MyEnumSerializer());

 // Test with Enum rawKeyType
 MyEnum enumValue = MyEnum.VALUE1;
 ObjectNode result = mapperWithCustomSerializer.writeValueAsTree(new Object[] { enumValue }, new StdKeySerializers.Dynamic());
 JsonNode node = result.get(0);
 String enumName = node.get("name").asText();
 assertEquals("VALUE1", enumName);
 }

 @Test
 public void testStringKeySerializer() throws Exception {
 String key = "testKey";
 JsonSerializer<Object> serializer = StdKeySerializers.getStringKeySerializer();
 ObjectNode result = mapper.writeValueAsTree(new Object[] { key }, serializer);
 JsonNode node = result.get(0);
 String keyString = node.get("testKey").asText();
 assertEquals("testKey", keyString);
 }
}

 enum MyEnum {
 VALUE1,
 VALUE2,
 @JsonValue("TEST")
 TEST;
}

 class StaticDateSerializer extends JsonSerializer<Date> {
 @Override
 public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeFieldName(date.toString());
 }
}

 class MyEnumSerializer extends JsonSerializer<MyEnum> {
 @Override
 public void serialize(MyEnum enumValue, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeFieldName(enumValue.name());
 }
}