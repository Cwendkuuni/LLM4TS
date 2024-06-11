package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import com.example.NumberSerializers.Base;
import com.example.NumberSerializers.NumberSerializers;
import com.example.NumberSerializers.ShortSerializer;
import com.example.NumberSerializers.IntegerSerializer;
import com.example.NumberSerializers.IntLikeSerializer;
import com.example.NumberSerializers.LongSerializer;
import com.example.NumberSerializers.FloatSerializer;
import com.example.NumberSerializers.DoubleSerializer;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializersTest {

 private ObjectMapper objectMapper;
 @InjectMocks
 private NumberSerializers numberSerializers;

 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = Mockito.mock(Map.class);
 numberSerializers.addAll(allDeserializers);
 assertEquals(6, allDeserializers.size());
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 ShortSerializer shortSerializer = numberSerializers.getShortSerializer();
 JsonSerializer<Short> jsonShortSerializer = Mockito.spy(shortSerializer);
 Short shortValue = Short.parseShort("32767");
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
 TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

 when(objectMapper.getFactory().createGenerator(Mockito.anyWriterTo(JsonGenerator.class), Mockito.eq(null)))
 .thenReturn(jsonGenerator);
 when(objectMapper.getSerializationConfig().findValueSerializer(Short.class))
 .thenReturn(jsonShortSerializer);
 when(jsonShortSerializer.serializeValue(shortValue, jsonGenerator, serializerProvider))
 .thenAnswer(invocation -> jsonGenerator.writeNumber(shortValue.shortValue()));

 JsonNode result = objectMapper.writeValueAsTree(shortValue);
 String jsonString = objectMapper.writerForTree().writeValueAsString(result);
 assertEquals("32767", jsonString);
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 IntegerSerializer integerSerializer = numberSerializers.getIntegerSerializer();
 JsonSerializer<Integer> jsonIntegerSerializer = Mockito.spy(integerSerializer);
 Integer integerValue = 123;
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
 TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

 when(objectMapper.getFactory().createGenerator(Mockito.anyWriterTo(JsonGenerator.class), Mockito.eq(null)))
 .thenReturn(jsonGenerator);
 when(objectMapper.getSerializationConfig().findValueSerializer(Integer.class))
 .thenReturn(jsonIntegerSerializer);
 when(jsonIntegerSerializer.serializeValue(integerValue, jsonGenerator, serializerProvider))
 .thenAnswer(invocation -> jsonGenerator.writeNumber(integerValue.intValue()));

 JsonNode result = objectMapper.writeValueAsTree(integerValue);
 String jsonString = objectMapper.writerForTree().writeValueAsString(result);
 assertEquals("123", jsonString);
 }

 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 IntLikeSerializer intLikeSerializer = numberSerializers.getIntLikeSerializer();
 JsonSerializer<Number> jsonNumberSerializer = Mockito.spy(intLikeSerializer);
 Number numberValue = 123;
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
 TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

 when(objectMapper.getFactory().createGenerator(Mockito.anyWriterTo(JsonGenerator.class), Mockito.eq(null)))
 .thenReturn(jsonGenerator);
 when(objectMapper.getSerializationConfig().findValueSerializer(Number.class))
 .thenReturn(jsonNumberSerializer);
 when(jsonNumberSerializer.serializeValue(numberValue, jsonGenerator, serializerProvider))
 .thenAnswer(invocation -> jsonGenerator.writeNumber(numberValue.intValue()));

 JsonNode result = objectMapper.writeValueAsTree(numberValue);
 String jsonString = objectMapper.writerForTree().writeValueAsString(result);
 assertEquals("123", jsonString);
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 LongSerializer longSerializer = numberSerializers.getLongSerializer();
 JsonSerializer<Long> jsonLongSerializer = Mockito.spy(longSerializer);
 Long longValue = Long.parseLong("9223372036854775807L");
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
 TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

 when(objectMapper.getFactory().createGenerator(Mockito.anyWriterTo(JsonGenerator.class), Mockito.eq(null)))
 .thenReturn(jsonGenerator);
 when(objectMapper.getSerializationConfig().findValueSerializer(Long.class))
 .thenReturn(jsonLongSerializer);
 when(jsonLongSerializer.serializeValue(longValue, jsonGenerator, serializerProvider))
 .thenAnswer(invocation -> jsonGenerator.writeNumber(longValue.longValue()));

 JsonNode result = objectMapper.writeValueAsTree(longValue);
 String jsonString = objectMapper.writerForTree().writeValueAsString(result);
 assertEquals("9223372036854775807", jsonString);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 FloatSerializer floatSerializer = numberSerializers.getFloatSerializer();
 JsonSerializer<Float> jsonFloatSerializer = Mockito.spy(floatSerializer);
 Float floatValue = 123.45f;
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
 TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

 when(objectMapper.getFactory().createGenerator(Mockito.anyWriterTo(JsonGenerator.class), Mockito.eq(null)))
 .thenReturn(jsonGenerator);
 when(objectMapper.getSerializationConfig().findValueSerializer(Float.class))
 .thenReturn(jsonFloatSerializer);
 when(jsonFloatSerializer.serializeValue(floatValue, jsonGenerator, serializerProvider))
 .thenAnswer(invocation -> jsonGenerator.writeNumber(floatValue.floatValue()));

 JsonNode result = objectMapper.writeValueAsTree(floatValue);
 String jsonString = objectMapper.writerForTree().writeValueAsString(result);
 assertEquals("123.45", jsonString);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 DoubleSerializer doubleSerializer = numberSerializers.getDoubleSerializer();
 JsonSerializer<Double> jsonDoubleSerializer = Mockito.spy(doubleSerializer);
 Double doubleValue = 123.45d;
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
 TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

 when(objectMapper.getFactory().createGenerator(Mockito.anyWriterTo(JsonGenerator.class), Mockito.eq(null)))
 .thenReturn(jsonGenerator);
 when(objectMapper.getSerializationConfig().findValueSerializer(Double.class))
 .thenReturn(jsonDoubleSerializer);
 when(jsonDoubleSerializer.serializeValue(doubleValue, jsonGenerator, serializerProvider))
 .thenAnswer(invocation -> jsonGenerator.writeNumber(doubleValue.doubleValue()));

 JsonNode result = objectMapper.writeValueAsTree(doubleValue);
 String jsonString = objectMapper.writerForTree().writeValueAsString(result);
 assertEquals("123.45", jsonString);
 }

 @Test
 public void testNullValue() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
 TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

 when(objectMapper.getFactory().createGenerator(Mockito.anyWriterTo(JsonGenerator.class), Mockito.eq(null)))
 .thenReturn(jsonGenerator);

 ShortSerializer shortSerializer = numberSerializers.getShortSerializer();
 IntegerSerializer integerSerializer = numberSerializers.getIntegerSerializer();
 IntLikeSerializer intLikeSerializer = numberSerializers.getIntLikeSerializer();
 LongSerializer longSerializer = numberSerializers.getLongSerializer();
 FloatSerializer floatSerializer = numberSerializers.getFloatSerializer();
 DoubleSerializer doubleSerializer = numberSerializers.getDoubleSerializer();

 for (JsonSerializer<?> serializer : new JsonSerializer<?>[] {shortSerializer, integerSerializer, intLikeSerializer, longSerializer, floatSerializer, doubleSerializer}) {
 JsonNode result = objectMapper.valueToTree(null, new TypeReference<JsonNode>() {});
 JsonSerializer<?> deserializedValue = objectMapper.readerForTree(JsonNode.class).readValueAs(serializer.handledType());
 assertNull(deserializedValue);
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonFormatAnnotation() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 public static class TestClass {
 private Double doubleValue;
 }
 TestClass testClass = new TestClass();
 testClass.doubleValue = 123.45d;
 JsonNode result = objectMapper.writeValueAsTree(testClass);
 String jsonString = objectMapper.writerForTree().writeValueAsString(result);
 assertEquals("\"123.45\"", jsonString);
 }
}