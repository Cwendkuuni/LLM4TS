package com.yourpackage.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer.ValueHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonWriter;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testSerializeNumber() throws Exception {
 Number testNumber = 123;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory()
 .createGenerator(JsonWriter.StandardEncodings.UTF8, null);
 ValueHandler<Number> valueHandler = new ValueHandler<Number>() {
 @Override
 public void writeValue(JsonGenerator jgen, Number value) throws IOException {
 jgen.writeNumber(value);
 }
 };
 numberSerializer.serialize(testNumber, jsonGenerator, null);
 assertEquals("123", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeBigDecimal() throws Exception {
 BigDecimal testBigDecimal = new BigDecimal("123.45");
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory()
 .createGenerator(JsonWriter.StandardEncodings.UTF8, null);
 numberSerializer.serialize(testBigDecimal, jsonGenerator, null);
 assertEquals("123.45", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeBigInteger() throws Exception {
 BigInteger testBigInteger = BigInteger.valueOf(123456789);
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory()
 .createGenerator(JsonWriter.StandardEncodings.UTF8, null);
 numberSerializer.serialize(testBigInteger, jsonGenerator, null);
 assertEquals("123456789", jsonGenerator.getOutputBuffer().toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testSerializeForbiddenTypes() throws Exception {
 Number testLongValue = 123L;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory()
 .createGenerator(JsonWriter.StandardEncodings.UTF8, null);
 numberSerializer.serialize(testLongValue, jsonGenerator, null);
 // Since this method should not be called for Long values, an exception should be thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNullNumber() throws Exception {
 Number testNullNumber = null;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory()
 .createGenerator(JsonWriter.StandardEncodings.UTF8, null);
 numberSerializer.serialize(testNullNumber, jsonGenerator, null);
 // NullNumber should not be accepted as a parameter
 }
}