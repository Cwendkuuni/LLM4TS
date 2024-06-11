package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class NumberSerializersTest {

 private ObjectMapper objectMapper = new JsonMapper();

 @Test
 public void testIntegerSerialization() throws JsonProcessingException, IOException {
 Integer input = 123;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.IntegerSerializer serializer = NumberSerializers.IntegerSerializer.instance;
 serializer.serialize(input, jsonGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return serializer;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }

 @Override
 public void serializeValue(Object value, JsonGenerator serializer, SerializerProvider provider) throws IOException {
 if (value instanceof JsonSerializer<?>) {
 serializer.writeStartObject();
 serializer.writeFieldName("class");
 serializer.writeValue(value.getClass().getName());
 serializer.writeEndObject();
 } else {
 serializer.writeNumber(value.intValue());
 }
 }
 });
 jsonGenerator.flush();

 String output = "123";
 JsonGenerator expectedGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.IntegerSerializer.instance.serialize(123, expectedGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return NumberSerializers.IntegerSerializer.instance;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 expectedGenerator.flush();

 String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
 assertEquals(output, actualOutput);
 }

 @Test
 public void testIntegerSerializationWithContextual() throws JsonProcessingException, IOException {
 Integer input = 123;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.IntegerSerializer serializer = NumberSerializers.IntegerSerializer.instance;
 serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(objectMapper.getTypeFactory()) {
 @Override
 public JsonSerializer<?> visitIntSerializer(JsonSerializer<?> serializer) {
 return serializer;
 }

 @Override
 public void visitObjectSerializer(JsonSerializer<?> serializer) {
 fail("Unexpected visitObjectSerializer call");
 }
 }, TypeFactory.defaultInstance.constructType(Integer.class));
 serializer.serialize(input, jsonGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return serializer;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 jsonGenerator.flush();

 String output = "123";
 JsonGenerator expectedGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.IntegerSerializer.instance.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(objectMapper.getTypeFactory()) {
 @Override
 public JsonSerializer<?> visitIntSerializer(JsonSerializer<?> serializer) {
 return serializer;
 }

 @Override
 public void visitObjectSerializer(JsonSerializer<?> serializer) {
 fail("Unexpected visitObjectSerializer call");
 }
 }, TypeFactory.defaultInstance.constructType(Integer.class));
 NumberSerializers.IntegerSerializer.instance.serialize(123, expectedGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return NumberSerializers.IntegerSerializer.instance;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 expectedGenerator.flush();

 String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
 assertEquals(output, actualOutput);
 }

 @Test
 public void testLongSerialization() throws JsonProcessingException, IOException {
 Long input = 123L;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.LongSerializer serializer = NumberSerializers.LongSerializer.instance;
 serializer.serialize(input, jsonGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return serializer;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 jsonGenerator.flush();

 String output = "123";
 JsonGenerator expectedGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.LongSerializer.instance.serialize(123L, expectedGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return NumberSerializers.LongSerializer.instance;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 expectedGenerator.flush();

 String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
 assertEquals(output, actualOutput);
 }

 @Test
 public void testLongSerializationWithContextual() throws JsonProcessingException, IOException {
 Long input = 123L;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.LongSerializer serializer = NumberSerializers.LongSerializer.instance;
 serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(objectMapper.getTypeFactory()) {
 @Override
 public JsonSerializer<?> visitLongSerializer(JsonSerializer<?> serializer) {
 return serializer;
 }

 @Override
 public void visitObjectSerializer(JsonSerializer<?> serializer) {
 fail("Unexpected visitObjectSerializer call");
 }
 }, TypeFactory.defaultInstance.constructType(Long.class));
 serializer.serialize(input, jsonGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return serializer;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 jsonGenerator.flush();

 String output = "123";
 JsonGenerator expectedGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.LongSerializer.instance.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(objectMapper.getTypeFactory()) {
 @Override
 public JsonSerializer<?> visitLongSerializer(JsonSerializer<?> serializer) {
 return serializer;
 }

 @Override
 public void visitObjectSerializer(JsonSerializer<?> serializer) {
 fail("Unexpected visitObjectSerializer call");
 }
 }, TypeFactory.defaultInstance.constructType(Long.class));
 NumberSerializers.LongSerializer.instance.serialize(123L, expectedGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return NumberSerializers.LongSerializer.instance;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 expectedGenerator.flush();

 String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
 assertEquals(output, actualOutput);
 }

 @Test
 public void testFloatSerialization() throws JsonProcessingException, IOException {
 Float input = 123.45f;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
 serializer.serialize(input, jsonGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return serializer;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 jsonGenerator.flush();

 String output = "123.45";
 JsonGenerator expectedGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.FloatSerializer.instance.serialize(123.45f, expectedGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return NumberSerializers.FloatSerializer.instance;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 expectedGenerator.flush();

 String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
 assertEquals(output, actualOutput);
 }

 @Test
 public void testFloatSerializationWithContextual() throws JsonProcessingException, IOException {
 Float input = 123.45f;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
 serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(objectMapper.getTypeFactory()) {
 @Override
 public JsonSerializer<?> visitFloatSerializer(JsonSerializer<?> serializer) {
 return serializer;
 }

 @Override
 public void visitObjectSerializer(JsonSerializer<?> serializer) {
 fail("Unexpected visitObjectSerializer call");
 }
 }, TypeFactory.defaultInstance.constructType(Float.class));
 serializer.serialize(input, jsonGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return serializer;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 jsonGenerator.flush();

 String output = "123.45";
 JsonGenerator expectedGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.FloatSerializer.instance.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(objectMapper.getTypeFactory()) {
 @Override
 public JsonSerializer<?> visitFloatSerializer(JsonSerializer<?> serializer) {
 return serializer;
 }

 @Override
 public void visitObjectSerializer(JsonSerializer<?> serializer) {
 fail("Unexpected visitObjectSerializer call");
 }
 }, TypeFactory.defaultInstance.constructType(Float.class));
 NumberSerializers.FloatSerializer.instance.serialize(123.45f, expectedGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return NumberSerializers.FloatSerializer.instance;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 expectedGenerator.flush();

 String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
 assertEquals(output, actualOutput);
 }

 @Test
 public void testDoubleSerialization() throws JsonProcessingException, IOException {
 Double input = 123.45;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;
 serializer.serialize(input, jsonGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return serializer;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 jsonGenerator.flush();

 String output = "123.45";
 JsonGenerator expectedGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.DoubleSerializer.instance.serialize(123.45, expectedGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return NumberSerializers.DoubleSerializer.instance;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 expectedGenerator.flush();

 String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
 assertEquals(output, actualOutput);
 }

 @Test
 public void testDoubleSerializationWithContextual() throws JsonProcessingException, IOException {
 Double input = 123.45;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;
 serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(objectMapper.getTypeFactory()) {
 @Override
 public JsonSerializer<?> visitDoubleSerializer(JsonSerializer<?> serializer) {
 return serializer;
 }

 @Override
 public void visitObjectSerializer(JsonSerializer<?> serializer) {
 fail("Unexpected visitObjectSerializer call");
 }
 }, TypeFactory.defaultInstance.constructType(Double.class));
 serializer.serialize(input, jsonGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return serializer;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 jsonGenerator.flush();

 String output = "123.45";
 JsonGenerator expectedGenerator = objectMapper.getFactory().createGenerator(System.out);
 NumberSerializers.DoubleSerializer.instance.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(objectMapper.getTypeFactory()) {
 @Override
 public JsonSerializer<?> visitDoubleSerializer(JsonSerializer<?> serializer) {
 return serializer;
 }

 @Override
 public void visitObjectSerializer(JsonSerializer<?> serializer) {
 fail("Unexpected visitObjectSerializer call");
 }
 }, TypeFactory.defaultInstance.constructType(Double.class));
 NumberSerializers.DoubleSerializer.instance.serialize(123.45, expectedGenerator, new SerializerProvider() {
 @Override
 public JsonSerializer<?> findValueSerializer(Class<?> type) {
 return NumberSerializers.DoubleSerializer.instance;
 }

 @Override
 public JsonSerializer<?> findContentSerializer(Class<?> type, Type typeHint) {
 return null;
 }
 });
 expectedGenerator.flush();

 String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
 assertEquals(output, actualOutput);
 }
}