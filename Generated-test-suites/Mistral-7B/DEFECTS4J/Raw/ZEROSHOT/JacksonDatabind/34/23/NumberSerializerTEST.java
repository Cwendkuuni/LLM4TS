package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class NumberSerializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testSerializeInteger() throws JsonGenerationException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = MAPPER.getFactory().createGenerator(System.out);
 SerializerProvider provider = MAPper.standardSerializerProvider();

 Integer integer = 123;
 serializer.serialize(integer, generator, provider);
 generator.close();

 Assertions.assertEquals("123", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeLong() throws JsonGenerationException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = MAPPER.getFactory().createGenerator(System.out);
 SerializerProvider provider = MAPper.standardSerializerProvider();

 Long longNumber = 456L;
 serializer.serialize(longNumber, generator, provider);
 generator.close();

 Assertions.assertEquals("456", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeFloat() throws JsonGenerationException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = MAPPER.getFactory().createGenerator(System.out);
 SerializerProvider provider = MAPper.standardSerializerProvider();

 Float floatNumber = 789F;
 serializer.serialize(floatNumber, generator, provider);
 generator.close();

 Assertions.assertEquals("789.0", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeDouble() throws JsonGenerationException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = MAPPER.getFactory().createGenerator(System.out);
 SerializerProvider provider = MAPper.standardSerializerProvider();

 Double doubleNumber = 10111213.1415;
 serializer.serialize(doubleNumber, generator, provider);
 generator.close();

 Assertions.assertEquals("10111213.1415", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = MAPPER.getFactory().createGenerator(System.out);
 SerializerProvider provider = MAPper.standardSerializerProvider();

 BigDecimal bigDecimal = new BigDecimal("123.456");
 serializer.serialize(bigDecimal, generator, provider);
 generator.close();

 Assertions.assertEquals("123.456", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException {
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = MAPPER.getFactory().createGenerator(System.out);
 SerializerProvider provider = MAPper.standardSerializerProvider();

 BigInteger bigInteger = BigInteger.valueOf(123456789012345678L);
 serializer.serialize(bigInteger, generator, provider);
 generator.close();

 Assertions.assertEquals("123456789012345678", generator.getOutputBuffer().toString());
 }

 @Test
 public void testDeserializeInteger() throws IOException {
 JsonParser parser = MAPPER.getFactory().createParser("123");
 Number deserialized = MAPPER.readValue(parser, Number.class);
 parser.close();

 Assertions.assertEquals(123, deserialized.intValue());
 }

 @Test
 public void testDeserializeLong() throws IOException {
 JsonParser parser = MAPPER.getFactory().createParser("456");
 Number deserialized = MAPPER.readValue(parser, Number.class);
 parser.close();

 Assertions.assertEquals(456, deserialized.longValue());
 }

 @Test
 public void testDeserializeFloat() throws IOException {
 JsonParser parser = MAPPER.getFactory().createParser("789.0");
 Number deserialized = MAPPER.readValue(parser, Number.class);
 parser.close();

 Assertions.assertEquals(789f, deserialized, 0.001f); // Allow some error due to float precision
 }

 @Test
 public void testDeserializeDouble() throws IOException {
 JsonParser parser = MAPPER.getFactory().createParser("10111213.1415");
 Number deserialized = MAPPER.readValue(parser, Number.class);
 parser.close();

 Assertions.assertEquals(10111213.1415, deserialized, 0.00001);
 }

 @Test
 public void testDeserializeBigDecimal() throws IOException {
 JsonParser parser = MAPPER.getFactory().createParser("123.456");
 Number deserialized = MAPPER.readValue(parser, Number.class);
 parser.close();

 Assertions.assertEquals(new BigDecimal("123.456"), deserialized);
 }

 @Test
 public void testDeserializeBigInteger() throws IOException {
 JsonParser parser = MAPPER.getFactory().createParser("123456789012345678");
 Number deserialized = MAPPER.readValue(parser, Number.class);
 parser.close();

 Assertions.assertEquals(new BigInteger("123456789012345678"), deserialized);
 }
}