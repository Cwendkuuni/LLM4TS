package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperBuilder;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class NumberSerializerTest {

 private static ObjectMapper objectMapper;

 static {
 objectMapper = new JsonMapperBuilder().build();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("123.456");
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 serializer.serialize(bigDecimal, jsonGenerator, new SerializerProvider());
 assertEquals("123.456", jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 BigInteger bigInteger = new BigInteger("1234567890123456789");
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 serializer.serialize(bigInteger, jsonGenerator, new SerializerProvider());
 assertEquals("1234567890123456789", jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testSerializeInteger() throws JsonGenerationException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 int integer = 123;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 serializer.serialize(integer, jsonGenerator, new SerializerProvider());
 assertEquals("123", jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testSerializeLong() throws JsonGenerationException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 long longValue = 1234567890123456789L;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 serializer.serialize(longValue, jsonGenerator, new SerializerProvider());
 assertEquals("1234567890123456789", jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testSerializeDouble() throws JsonGenerationException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 double doubleValue = 123.456;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 serializer.serialize(doubleValue, jsonGenerator, new SerializerProvider());
 assertEquals("123.456", jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testSerializeFloat() throws JsonGenerationException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 float floatValue = 123.456f;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 serializer.serialize(floatValue, jsonGenerator, new SerializerProvider());
 assertEquals("123.456", jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testSerializeByte() throws JsonGenerationException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 byte byteValue = 127;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 serializer.serialize(byteValue, jsonGenerator, new SerializerProvider());
 assertEquals("127", jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testSerializeShort() throws JsonGenerationException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 short shortValue = 127;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 serializer.serialize(shortValue, jsonGenerator, new SerializerProvider());
 assertEquals("127", jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testSerializeOtherNumber() throws JsonGenerationException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 Object otherNumber = new Long(123); {
 // We'll have to use fallback "untyped" number write method
 String result = objectMapper.writeValueAsString(otherNumber);
 assertEquals("123", result);
 }
 }

 @Test
 public void testGetSchemaInteger() throws JsonMappingException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonNode schema = serializer.getSchema(new SerializerProvider(), TypeFactory.defaultInstance.constructType(Integer.class));
 assertEquals("integer", schema.asText());
 }

 @Test
 public void testGetSchemaBigDecimal() throws JsonMappingException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonNode schema = serializer.getSchema(new SerializerProvider(), TypeFactory.defaultInstance.constructType(BigDecimal.class));
 assertEquals("number", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitorIntFormat() throws JsonMappingException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonNode result = serializer.acceptJsonFormatVisitor(null, TypeFactory.defaultInstance.constructType(Integer.class.getName()));
 assertEquals("integer", result.asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testAcceptJsonFormatVisitorUnsupportedType() throws JsonMappingException {
 NumberSerializer serializer = NumberSerializer.instance;
 serializer.acceptJsonFormatVisitor(null, TypeFactory.defaultInstance.constructType("UnsupportedType.class"));
 }
}