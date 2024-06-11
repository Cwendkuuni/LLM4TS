package com.yourpackage.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

 private static ObjectMapper mapper = JsonMapperFactory.getMapper();

 @Test
 public void testSerializeNumber() throws JsonMappingException, JsonGenerationException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);

 // Test serialization of various Number types
 Number testNumbers[] = {new Integer(1), new Long(1), new Float(1.0f), new Double(1.0), new BigDecimal("1.0"), new BigInteger("1"), new BigDecimal("1.234"), new BigInteger("1234567890123456789012345678901234567890")};
 for (Number number : testNumbers) {
 serializeAndCompare(serializer, number, generator);
 }
 }

 @Test
 public void testSerializeBigDecimal() throws JsonMappingException, JsonGenerationException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);

 BigDecimal bd = new BigDecimal("123.456");
 serializeAndCompare(serializer, bd, generator);
 }

 @Test
 public void testSerializeBigInteger() throws JsonMappingException, JsonGenerationException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);

 BigInteger bi = new BigInteger("1234567890123456789012345678901234567890");
 serializeAndCompare(serializer, bi, generator);
 }

 @Test
 public void testSerializeInvalidNumber() throws JsonMappingException, JsonGenerationException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);

 // Test serialization of null and invalid Number types
 serializeAndCompare(serializer, null, generator);
 serializeAndCompare(serializer, new String("abc"), generator);
 }

 @Test
 public void testDeserializeNumber() throws JsonMappingException, IOException {
 ObjectMapper mapper = JsonMapperFactory.getMapper();
 JsonParser parser = mapper.getFactory().createParser("1");

 // Test deserialization of various Number types
 Number deserializedNumbers[] = new Number[8];
 for (int i = 0; i < 8; i++) {
 deserializedNumbers[i] = mapper.readValue(parser, Number.class);
 }

 assertEquals(1, deserializedNumbers[0].intValue());
 assertEquals(1, deserializedNumbers[1].longValue());
 assertEquals(1.0f, deserializedNumbers[2].floatValue(), 0.0f);
 assertEquals(1.0, deserializedNumbers[3].doubleValue(), 0.0);
 assertEquals(new BigDecimal("1.0"), deserializedNumbers[4]);
 assertEquals(new BigInteger("1"), deserializedNumbers[5]);
 assertEquals(new BigDecimal("1.234"), deserializedNumbers[6]);
 assertEquals(new BigInteger("1234567890123456789012345678901234567890"), deserializedNumbers[7]);
 }

 @Test
 public void testDeserializeBigDecimal() throws JsonMappingException, IOException {
 ObjectMapper mapper = JsonMapperFactory.getMapper();
 JsonParser parser = mapper.getFactory().createParser("1.234");

 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(new BigDecimal("1.234"), deserializedNumber);
 }

 @Test
 public void testDeserializeBigInteger() throws JsonMappingException, IOException {
 ObjectMapper mapper = JsonMapperFactory.getMapper();
 JsonParser parser = mapper.getFactory().createParser("1234567890123456789012345678901234567890");

 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(new BigInteger("1234567890123456789012345678901234567890"), deserializedNumber);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidNumber() throws JsonMappingException, IOException {
 ObjectMapper mapper = JsonMapperFactory.getMapper();
 JsonParser parser = mapper.getFactory().createParser("invalid");

 mapper.readValue(parser, Number.class);
 }

 private void serializeAndCompare(NumberSerializer serializer, Number number, JsonGenerator generator) throws JsonMappingException, JsonGenerationException {
 serializer.serialize(number, generator, new SerializerProvider());
 generator.flush();
 String serialized = generator.getOutputFactory().getValueAsString();
 String expected = number.toString();
 assertEquals(expected, serialized);
 }
}