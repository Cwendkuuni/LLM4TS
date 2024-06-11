package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

 private static ObjectMapper objectMapper;

 @BeforeClass
 public static void setup() {
 objectMapper = JsonMapperFactory.getMapper();
 }

 // Test getStdKeySerializer method
 @Test
 public void testGetStdKeySerializer() {
 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), rawKeyType, false);
 assertNotNull(serializer);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

 rawKeyType = Date.class;
 serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), rawKeyType, false);
 assertNotNull(serializer);

 rawKeyType = Calendar.class;
 serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), rawKeyType, false);
 assertNotNull(serializer);
 }

 // Test getFallbackKeySerializer method
 @Test
 public void testGetFallbackKeySerializer() {
 Class<?> rawKeyType = String.class;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), rawKeyType);
 assertNotNull(serializer);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

 rawKeyType = Date.class;
 serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), rawKeyType);
 assertNotNull(serializer);

 rawKeyType = Class.class;
 serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), rawKeyType);
 assertNull(serializer);
 }

 // Test getDefault method
 @Test
 @Deprecated
 public void testGetDefault() {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 assertNotNull(serializer);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
 }

 // Test Default class
 @Test
 public void testDefault() throws Exception {
 Date date = new Date();
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(date);
 String key = "test";

 // Test serialization with known types
 JsonSerializer<Object> dateSerializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), Date.class, false);
 JsonSerializer<Object> calendarSerializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), Calendar.class, false);

 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 generator.writeStartArray();
 dateSerializer.serialize(date, generator, objectMapper.getSerializationConfig().getSerializerProvider());
 generator.writeObject(calendar);
 generator.writeEnd();
 String result = generator.getOutputString();
 String expected = "[\"Date\",\"Calendar\"]";
 assertEquals(expected, result);

 // Test serialization with String key
 String testEnum = EnumSet.allOf(Enum.class).iterator().next().name();
 serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), Enum.class);
 generator = objectMapper.getFactory().createGenerator();
 generator.writeStartObject();
 generator.writeFieldName(testEnum);
 generator.writeValue("testValue");
 generator.writeEnd();
 String result2 = generator.getOutputString();
 String expected2 = "{\"testEnum\":\"testValue\"}";
 assertEquals(expected2, result2);

 // Test serialization with unknown type
 Object unknownType = new Object() {
 @JsonValue
 public String toString() {
 return "testUnknownType";
 }
 };
 serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), unknownType.getClass());
 generator = objectMapper.getFactory().createGenerator();
 generator.writeStartObject();
 generator.writeFieldName("testUnknownType");
 generator.writeValue("testValue");
 generator.writeEnd();
 String result3 = generator.getOutputString();
 String expected3 = "{\"testUnknownType\":\"testValue\"}";
 assertEquals(expected3, result3);

 // Test deserialization with known types
 ObjectMapper mapper = objectMapper.copy();
 mapper.setSerializationInclusion(Include.NON_NULL);
 mapper.setSerializationInclusion(Include.NON_DEFAULT);
 ObjectMapper deserializer = objectMapper.copy();
 deserializer.setAnnotationIntrospector(new StandardAnnotationIntrospector());
 ObjectReader reader = mapper.getFactory().createObjectReader();
 JsonParser parser = mapper.getFactory().createParser("[\"Date\",\"Calendar\"]");
 JsonToken currentToken = parser.nextToken();
 assertNotNull(currentToken);
 assertNull(parser.getCurrentName());

 Object readValue = reader.readValueAs(Map.class);
 assertTrue(readValue instanceof HashMap);
 assertEquals(2, ((HashMap) readValue).size());
 assertEquals(Date.class, ((HashMap) readValue).get(0).getClass());
 assertEquals(Calendar.class, ((HashMap) readValue).get(1).getClass());

 // Test deserialization with String key
 ObjectMapper mapper2 = objectMapper.copy();
 mapper2.setSerializationInclusion(Include.NON_NULL);
 mapper2.setSerializationInclusion(Include.NON_DEFAULT);
 ObjectMapper deserializer2 = objectMapper.copy();
 deserializer2.setAnnotationIntrospector(new StandardAnnotationIntrospector());
 ObjectReader reader2 = mapper2.getFactory().createObjectReader();
 JsonParser parser2 = mapper2.getFactory().createParser(expected2);
 JsonToken currentToken2 = parser2.nextToken();
 assertNotNull(currentToken2);
 assertNull(parser2.getCurrentName());

 Object readValue2 = reader2.readValueAs(Map.class);
 assertTrue(readValue2 instanceof HashMap);
 assertEquals(1, ((HashMap) readValue2).size());
 assertEquals(Enum.class, ((HashMap) readValue2).get("testEnum").getClass());

 // Test deserialization with unknown type
 ObjectMapper mapper3 = objectMapper.copy();
 mapper3.setSerializationInclusion(Include.NON_NULL);
 mapper3.setSerializationInclusion(Include.NON_DEFAULT);
 ObjectMapper deserializer3 = objectMapper.copy();
 deserializer3.setAnnotationIntrospector(new StandardAnnotationIntrospector());
 ObjectReader reader3 = mapper3.getFactory().createObjectReader();
 JsonParser parser3 = mapper3.getFactory().createParser(expected3);
 JsonToken currentToken3 = parser3.nextToken();
 assertNotNull(currentToken3);
 assertNull(parser3.getCurrentName());

 Object readValue3 = reader3.readValueAs(Map.class);
 assertTrue(readValue3 instanceof HashMap);
 assertEquals(1, ((HashMap) readValue3).size());
 assertEquals(Class.forName("test.Test$UnknownType"), ((HashMap) readValue3).get("testUnknownType").getClass());
 }

 // Test Dynamic class
 @Test
 public void testDynamic() throws Exception {
 ObjectMapper mapper = objectMapper.copy();
 mapper.setSerializationInclusion(Include.NON_NULL);
 mapper.setSerializationInclusion(Include.NON_DEFAULT);
 ObjectMapper deserializer = objectMapper.copy();
 deserializer.setAnnotationIntrospector(new StandardAnnotationIntrospector());
 ObjectReader reader = mapper.getFactory().createObjectReader();
 JsonParser parser = mapper.getFactory().createParser("[\"Test$DynamicKeySerializer\",\"Test$DynamicValueSerializer\"]");
 JsonToken currentToken = parser.nextToken();
 assertNotNull(currentToken);
 assertNull(parser.getCurrentName());

 Object readValue = reader.readValueAs(Map.class);
 assertTrue(readValue instanceof HashMap);
 assertEquals(2, ((HashMap) readValue).size());
 assertEquals(DynamicKeySerializer.class, ((HashMap) readValue).get(0).getClass());
 assertEquals(DynamicValueSerializer.class, ((HashMap) readValue).get(1).getClass());
 }

}

public class DynamicKeySerializer extends Enum {
 public static final DynamicKeySerializer VALUE_1 = new DynamicKeySerializer("VALUE_1");
 public static final DynamicKeySerializer VALUE_2 = new DynamicKeySerializer("VALUE_2");

 private DynamicKeySerializer(String name) {
 super(name);
 }
}

public class DynamicValueSerializer {
 @JsonValue
 public String toString() {
 return "testValue";
 }
}