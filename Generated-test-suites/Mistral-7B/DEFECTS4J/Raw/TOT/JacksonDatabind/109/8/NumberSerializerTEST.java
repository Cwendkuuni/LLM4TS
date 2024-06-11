package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NumberSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);
 BigDecimal number = new BigDecimal("1234567890.1234567890");
 serializer.serialize(number, generator, mapper.getSerializerProvider());
 generator.flush();
 String actual = generator.getOutputBuffer().toString();
 String expected = "1234567890.1234567890";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);
 BigInteger number = new BigInteger("123456789012345678901234567890");
 serializer.serialize(number, generator, mapper.getSerializerProvider());
 generator.flush();
 String actual = generator.getOutputBuffer().toString();
 String expected = "123456789012345678901234567890";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeInt() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);
 int number = 123;
 serializer.serialize(number, generator, mapper.getSerializerProvider());
 generator.flush();
 String actual = generator.getOutputBuffer().toString();
 String expected = "123";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);
 float number = 123.45f;
 serializer.serialize(number, generator, mapper.getSerializerProvider());
 generator.flush();
 String actual = generator.getOutputBuffer().toString();
 String expected = "123.45";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);
 double number = 123.45;
 serializer.serialize(number, generator, mapper.getSerializerProvider());
 generator.flush();
 String actual = generator.getOutputBuffer().toString();
 String expected = "123.45";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);
 long number = 123L;
 serializer.serialize(number, generator, mapper.getSerializerProvider());
 generator.flush();
 String actual = generator.getOutputBuffer().toString();
 String expected = "123";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeByte() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);
 byte number = 127;
 serializer.serialize(number, generator, mapper.getSerializerProvider());
 generator.flush();
 String actual = generator.getOutputBuffer().toString();
 String expected = "127";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeShort() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);
 short number = 123;
 serializer.serialize(number, generator, mapper.getSerializerProvider());
 generator.flush();
 String actual = generator.getOutputBuffer().toString();
 String expected = "123";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeUnknownNumberType() throws JsonMappingException, JsonProcessingException {
 Object obj = new Random().nextObject(); // generate random object of unknown type
 JsonParser parser = mapper.getFactory().createParser(Object.class.getName());
 parser.nextValue();
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(null);
 serializer.serialize(obj, generator, mapper.getSerializerProvider()); // this should throw an exception
 generator.flush();
 String actual = generator.getOutputBuffer().toString();
 assertTrue(actual.equals("") || actual.contains("Cannot serialize value of type"));
 }
}