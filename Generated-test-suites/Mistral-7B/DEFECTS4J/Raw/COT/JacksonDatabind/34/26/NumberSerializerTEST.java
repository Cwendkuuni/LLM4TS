package com.yourpackage.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeNumber() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;

 testSerialize(serializer, null, "null");
 testSerialize(serializer, new Integer(1), "1");
 testSerialize(serializer, new Long(1), "1");
 testSerialize(serializer, new Float(1.0f), "1.0");
 testSerialize(serializer, new Double(1.0), "1.0");
 testSerialize(serializer, new BigInteger("12345678901234567890"), "12345678901234567890");
 testSerialize(serializer, new BigDecimal("1234567890.1234567890"), "1234567890.1234567890");
 }

 private void testSerialize(NumberSerializer serializer, Number number, String expected) throws JsonGenerationException, JsonMappingException, IOException {
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), new JsonSerializer.DefaultPrettyPrinter());
 serializer.serialize(number, generator, mapper.getSerializerProvider());
 generator.flush();
 String result = new String(generator.getOutputBuffer().toString());
 assertEquals(expected, result);
 }

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;

 BigDecimal bigDecimal = new BigDecimal("1234567890.1234567890");
 testSerialize(serializer, bigDecimal, "1234567890.1234567890");
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;

 BigInteger bigInteger = new BigInteger("12345678901234567890");
 testSerialize(serializer, bigInteger, "12345678901234567890");
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeString() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 String str = "test";
 testSerialize(serializer, str, str);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 NumberSerializer serializer = NumberSerializer.instance;
 assertEquals("number", serializer.getSchema(mapper.getSerializerProvider(), null).toString());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 NumberSerializer serializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 TypeFactory typeFactory = mapper.getTypeFactory();

 // Test for int
 JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getNumberFormatVisitor(typeFactory.constructType(Integer.class), null, null, null, null);
 serializer.acceptJsonFormatVisitor(visitor, typeFactory.constructType(Integer.class));

 // Test for BigDecimal
 visitor = mapper.schemaFactory().getNumberFormatVisitor(typeFactory.constructType(BigDecimal.class), null, null, null, null);
 serializer.acceptJsonFormatVisitor(visitor, typeFactory.constructType(BigDecimal.class));
 }

 // Add edge cases and exception handling tests here
}