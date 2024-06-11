package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test file for NumberSerializer class
 */
public class NumberSerializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializeNumber() throws JsonMappingException, JsonGenerationException, IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getPolymorphicSerializer(NumberSerializer.class).getGenerator();

 // Test serialization for Number types
 Number number = 123;
 numberSerializer.serialize(number, generator, mapper.getSerializerProvider());
 assertEquals("123", generator.getOutputBuffer().toString());
 generator.flush();
 generator.close();

 BigDecimal bigDecimal = new BigDecimal("123.456");
 numberSerializer.serialize(bigDecimal, generator, mapper.getSerializerProvider());
 assertEquals("123.456", generator.getOutputBuffer().toString());
 generator.flush();
 generator.close();

 BigInteger bigInteger = BigInteger.valueOf(1234567890L);
 numberSerializer.serialize(bigInteger, generator, mapper.getSerializerProvider());
 assertEquals("1234567890", generator.getOutputBuffer().toString());
 generator.flush();
 generator.close();

 // Test serialization for Integer types
 Integer intNumber = 123;
 numberSerializer.serialize(intNumber, generator, mapper.getSerializerProvider());
 assertEquals("123", generator.getOutputBuffer().toString());
 generator.flush();
 generator.close();

 // Test serialization for Double types
 Double doubleNumber = 123.456;
 numberSerializer.serialize(doubleNumber, generator, mapper.getSerializerProvider());
 assertEquals("123.456", generator.getOutputBuffer().toString());
 generator.flush();
 generator.close();

 // Test serialization for Float types
 Float floatNumber = 123.456f;
 numberSerializer.serialize(floatNumber, generator, mapper.getSerializerProvider());
 assertEquals("123.456", generator.getOutputBuffer().toString());
 generator.flush();
 generator.close();

 // Test serialization for Byte and Short types
 Byte byteNumber = 127;
 numberSerializer.serialize(byteNumber, generator, mapper.getSerializerProvider());
 assertEquals("127", generator.getOutputBuffer().toString());
 generator.flush();
 generator.close();

 Short shortNumber = 123;
 numberSerializer.serialize(shortNumber, generator, mapper.getSerializerProvider());
 assertEquals("123", generator.getOutputBuffer().toString());
 generator.flush();
 generator.close();
 }

 @Test
 public void testSerializeJsonFormat() throws JsonMappingException, JsonGenerationException, IOException {
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 ObjectWriter writer = mapper.writer();
 JsonGenerator generator = writer.getGenerator();

 Number number = 123;
 NumberSerializer numberSerializer = new NumberSerializer(number.getClass());
 writer.writeValueAsString(number);
 assertNull(format);

 number = new BigDecimal("123.456");
 numberSerializer = new NumberSerializer(number.getClass());
 writer.writeValueAsString(number);
 assertEquals("bigdecimal", format.getSerializableContentType());

 number = new BigInteger("1234567890");
 numberSerializer = new NumberSerializer(number.getClass());
 writer.writeValueAsString(number);
 assertEquals("biginteger", format.getSerializableContentType());

 generator.close();
 }

 @Test
 public void testDeserializeNumber() throws JsonMappingException, JsonParseException, IOException {
 JsonParser parser = mapper.getFactory().createParser("123");
 Number number = mapper.readValue(parser, Number.class);
 assertEquals(123, number.intValue());
 parser.close();

 parser = mapper.getFactory().createParser("123.456");
 number = mapper.readValue(parser, Number.class);
 assertEquals(123.456, number.doubleValue(), 0.001);
 parser.close();

 parser = mapper.getFactory().createParser("1234567890");
 number = mapper.readValue(parser, Number.class);
 assertEquals(1234567890L, number.longValue());
 parser.close();

 parser = mapper.getFactory().createParser("1.23E10");
 number = mapper.readValue(parser, Number.class);
 assertEquals(123000000000L, number.longValue());
 parser.close();

 parser = mapper.getFactory().createParser("\"123.456\"");
 number = mapper.readValue(parser, Number.class);
 assertEquals(123.456, number.doubleValue(), 0.001);
 parser.close();

 parser = mapper.getFactory().createParser("\"-123E-10\"");
 number = mapper.readValue(parser, Number.class);
 assertEquals(-123000000000L, number.longValue());
 parser.close();
 }

 @Test
 public void testDeserializeBigDecimalAndBigInteger() throws JsonMappingException, JsonParseException, IOException {
 JsonParser parser = mapper.getFactory().createParser("123.456");
 BigDecimal bigDecimal = mapper.readValue(parser, BigDecimal.class);
 assertEquals("123.456", bigDecimal.toString());
 parser.close();

 parser = mapper.getFactory().createParser("1234567890");
 BigInteger bigInteger = mapper.readValue(parser, BigInteger.class);
 assertEquals("1234567890", bigInteger.toString());
 parser.close();
 }
}