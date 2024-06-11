package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.test.BaseTest;

public class NumberSerializersTest extends BaseTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerialization() throws JsonProcessingException, IOException {
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // Test Integer serialization
 Integer intValue = 123;
 JsonSerializer<Integer> intSerializer = NumberSerializers.findValueSerializer(Integer.class, mapper);
 String intJson = mapper.writeValueAsString(intValue, intSerializer);
 assertEquals("123", intJson);

 // Test Integer serialization with TypeSerializer
 Type type = mapper.getTypeFactory().constructType(Integer.class);
 JsonSerializer<Integer> intSerializerWithType = NumberSerializers.findValueSerializer(Integer.class, mapper);
 String intJsonWithType = mapper.writeValueAsString(intValue, intSerializerWithType, TypeSerializer.instance);
 assertEquals("123", intJsonWithType);

 // Test Integer serialization with JsonFormat
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Integer formattedInt = 456;
 String formattedIntJson = mapper.writeValueAsString(formattedInt);
 assertEquals("\"456\"", formattedIntJson);

 // Test Long serialization
 Long longValue = 789L;
 JsonSerializer<Long> longSerializer = NumberSerializers.findValueSerializer(Long.class, mapper);
 String longJson = mapper.writeValueAsString(longValue, longSerializer);
 assertEquals("789", longJson);

 // Test Long serialization with TypeSerializer
 Type longType = mapper.getTypeFactory().constructType(Long.class);
 JsonSerializer<Long> longSerializerWithType = NumberSerializers.findValueSerializer(Long.class, mapper);
 String longJsonWithType = mapper.writeValueAsString(longValue, longSerializerWithType, TypeSerializer.instance);
 assertEquals("789", longJsonWithType);

 // Test Long serialization with JsonFormat
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Long formattedLong = 1010L;
 String formattedLongJson = mapper.writeValueAsString(formattedLong);
 assertEquals("\"1010\"", formattedLongJson);

 // Test Float serialization
 Float floatValue = 3.14F;
 JsonSerializer<Float> floatSerializer = NumberSerializers.findValueSerializer(Float.class, mapper);
 String floatJson = mapper.writeValueAsString(floatValue, floatSerializer);
 assertEquals("3.14", floatJson);

 // Test Float serialization with TypeSerializer
 Type floatType = mapper.getTypeFactory().constructType(Float.class);
 JsonSerializer<Float> floatSerializerWithType = NumberSerializers.findValueSerializer(Float.class, mapper);
 String floatJsonWithType = mapper.writeValueAsString(floatValue, floatSerializerWithType, TypeSerializer.instance);
 assertEquals("3.14", floatJsonWithType);

 // Test Double serialization
 Double doubleValue = 1.23;
 JsonSerializer<Double> doubleSerializer = NumberSerializers.findValueSerializer(Double.class, mapper);
 String doubleJson = mapper.writeValueAsString(doubleValue, doubleSerializer);
 assertEquals("1.23", doubleJson);

 // Test Double serialization with TypeSerializer
 Type doubleType = mapper.getTypeFactory().constructType(Double.class);
 JsonSerializer<Double> doubleSerializerWithType = NumberSerializers.findValueSerializer(Double.class, mapper);
 String doubleJsonWithType = mapper.writeValueAsString(doubleValue, doubleSerializerWithType, TypeSerializer.instance);
 assertEquals("1.23", doubleJsonWithType);

 // Test Double serialization with JsonFormat
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Double formattedDouble = 2.718;
 String formattedDoubleJson = mapper.writeValueAsString(formattedDouble);
 assertEquals("\"2.718\"", formattedDoubleJson);
 }

 @Test
 public void testSerializationWithContextualSerializer() throws JsonProcessingException {
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

 Object obj = new Object() {{
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Integer intField = 1;
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Long longField = 2L;
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Float floatField = 3.0f;
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Double doubleField = 4.0;
 }};

 ObjectWriter writer = mapper.writer();
 String json = writer.writeValueAsString(obj);

 assertEquals("{" +
 "\"intField\":\"1\"," +
 "\"longField\":\"2\"," +
 "\"floatField\":\"3.0\"," +
 "\"doubleField\":\"4.0\"" +
 "}", json);
 }

 @Test
 public void testDeserialization() throws JsonProcessingException, IOException {
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // Test Integer deserialization
 String intJson = "123";
 JsonDeserializer<Integer> intDeserializer = NumberSerializers.findDeserializer(Integer.class, mapper);
 Integer intDeserialized = mapper.readValue(intJson, intDeserializer);
 assertEquals(123, intDeserialized.intValue());

 // Test Integer deserialization with TypeDeserializer
 Type intType = mapper.getTypeFactory().constructType(Integer.class);
 JsonDeserializer<Integer> intDeserializerWithType = NumberSerializers.findDeserializer(Integer.class, mapper);
 Integer intDeserializedWithType = mapper.readValue(intJson, intDeserializerWithType);
 assertEquals(123, intDeserializedWithType.intValue());

 // Test Long deserialization
 String longJson = "789";
 JsonDeserializer<Long> longDeserializer = NumberSerializers.findDeserializer(Long.class, mapper);
 Long longDeserialized = mapper.readValue(longJson, longDeserializer);
 assertEquals(789L, longDeserialized);

 // Test Long deserialization with TypeDeserializer
 Type longType = mapper.getTypeFactory().constructType(Long.class);
 JsonDeserializer<Long> longDeserializerWithType = NumberSerializers.findDeserializer(Long.class, mapper);
 Long longDeserializedWithType = mapper.readValue(longJson, longDeserializerWithType);
 assertEquals(789L, longDeserializedWithType);

 // Test Float deserialization
 String floatJson = "3.14";
 JsonDeserializer<Float> floatDeserializer = NumberSerializers.findDeserializer(Float.class, mapper);
 Float floatDeserialized = mapper.readValue(floatJson, floatDeserializer);
 assertEquals(3.14f, floatDeserialized.floatValue(), 0.001f);

 // Test Float deserialization with TypeDeserializer
 Type floatType = mapper.getTypeFactory().constructType(Float.class);
 JsonDeserializer<Float> floatDeserializerWithType = NumberSerializers.findDeserializer(Float.class, mapper);
 Float floatDeserializedWithType = mapper.readValue(floatJson, floatDeserializerWithType);
 assertEquals(3.14f, floatDeserializedWithType.floatValue(), 0.001f);

 // Test Double deserialization
 String doubleJson = "1.23";
 JsonDeserializer<Double> doubleDeserializer = NumberSerializers.findDeserializer(Double.class, mapper);
 Double doubleDeserialized = mapper.readValue(doubleJson, doubleDeserializer);
 assertEquals(1.23, doubleDeserialized.doubleValue(), 0.001);

 // Test Double deserialization with TypeDeserializer
 Type doubleType = mapper.getTypeFactory().constructType(Double.class);
 JsonDeserializer<Double> doubleDeserializerWithType = NumberSerializers.findDeserializer(Double.class, mapper);
 Double doubleDeserializedWithType = mapper.readValue(doubleJson, doubleDeserializerWithType);
 assertEquals(1.23, doubleDeserializedWithType.doubleValue(), 0.001);
 }
}