package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BigDecimalNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.NumberNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class, numberSerializer).getGenerator();
 BigDecimal number = new BigDecimal("123.456");
 numberSerializer.serialize(number, gen, mapper.getSerializerProvider());
 JsonNode result = mapper.readTree(gen.asStream());
 assertEquals("123.456", result.asText());
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class, numberSerializer).getGenerator();
 BigInteger number = BigInteger.valueOf(123456789);
 numberSerializer.serialize(number, gen, mapper.getSerializerProvider());
 JsonNode result = mapper.readTree(gen.asStream());
 assertEquals(123456789, result.asLong());
 }

 @Test
 public void testSerializeInteger() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class, numberSerializer).getGenerator();
 Integer number = 123;
 numberSerializer.serialize(number, gen, mapper.getSerializerProvider());
 JsonNode result = mapper.readTree(gen.asStream());
 assertEquals(123, result.asInt());
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class, numberSerializer).getGenerator();
 Long number = 123456789L;
 numberSerializer.serialize(number, gen, mapper.getSerializerProvider());
 JsonNode result = mapper.readTree(gen.asStream());
 assertEquals(123456789, result.asLong());
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class, numberSerializer).getGenerator();
 Double number = 1.23;
 numberSerializer.serialize(number, gen, mapper.getSerializerProvider());
 JsonNode result = mapper.readTree(gen.asStream());
 assertEquals("1.23", result.asText());
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class, numberSerializer).getGenerator();
 Float number = 1.23f;
 numberSerializer.serialize(number, gen, mapper.getSerializerProvider());
 JsonNode result = mapper.readTree(gen.asStream());
 assertEquals("1.23", result.asText());
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class, numberSerializer).getGenerator();
 String number = "123";
 numberSerializer.serialize(number, gen, mapper.getSerializerProvider());
 JsonNode result = mapper.readTree(gen.asStream());
 assertEquals("123", result.asText());
 }

 @Test(expected = JsonGenerationException.class)
 public void testSerializeInvalidNumberType() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class, numberSerializer).getGenerator();
 // This should not be reached, but just in case
 Object invalidNumber = new Object();
 numberSerializer.serialize(invalidNumber, gen, mapper.getSerializerProvider());
 }

 @Test
 public void testDeserializeBigDecimal() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("123.456");
 JsonDeserializer<Number> deserializer = mapper.getPolymorphicDeserializer(Number.class, NumberSerializer.instance);
 Number deserializedNumber = deserializer.deserialize(parser, null);
 assertEquals(new BigDecimal("123.456"), deserializedNumber);
 }

 @Test
 public void testDeserializeBigInteger() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("123456789");
 JsonDeserializer<Number> deserializer = mapper.getPolymorphicDeserializer(Number.class, NumberSerializer.instance);
 Number deserializedNumber = deserializer.deserialize(parser, null);
 assertEquals(BigInteger.valueOf(123456789), deserializedNumber);
 }

 @Test
 public void testDeserializeInteger() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("123");
 JsonDeserializer<Number> deserializer = mapper.getPolymorphicDeserializer(Number.class, NumberSerializer.instance);
 Number deserializedNumber = deserializer.deserialize(parser, null);
 assertEquals(123, deserializedNumber.intValue());
 }

 @Test
 public void testDeserializeLong() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("123456789");
 JsonDeserializer<Number> deserializer = mapper.getPolymorphicDeserializer(Number.class, NumberSerializer.instance);
 Number deserializedNumber = deserializer.deserialize(parser, null);
 assertEquals(123456789, deserializedNumber.longValue());
 }

 @Test
 public void testDeserializeDouble() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("1.23");
 JsonDeserializer<Number> deserializer = mapper.getPolymorphicDeserializer(Number.class, NumberSerializer.instance);
 Number deserializedNumber = deserializer.deserialize(parser, null);
 assertEquals(1.23, deserializedNumber.doubleValue(), 0.001);
 }

 @Test
 public void testDeserializeFloat() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("1.23");
 JsonDeserializer<Number> deserializer = mapper.getPolymorphicDeserializer(Number.class, NumberSerializer.instance);
 Number deserializedNumber = deserializer.deserialize(parser, null);
 assertEquals(1.23f, deserializedNumber.floatValue(), 0.001f);
 }

 @Test
 public void testDeserializeString() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("\"123\"");
 JsonDeserializer<Number> deserializer = mapper.getPolymorphicDeserializer(Number.class, NumberSerializer.instance);
 Number deserializedNumber = deserializer.deserialize(parser, null);
 assertEquals(123, deserializedNumber.intValue());
 }

 @Test
 public void testDeserializeInvalidNumberType() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("invalidType");
 JsonDeserializer<Number> deserializer = mapper.getPolymorphicDeserializer(Number.class, NumberSerializer.instance);
 try {
 Number deserializedNumber = deserializer.deserialize(parser, null);
 fail("Deserialization should fail");
 } catch (JsonProcessingException e) {
 // Deserialization failed as expected
 }
 }

 @Test
 public void testSerializationWithJsonFormat() throws JsonGenerationException, JsonMappingException {
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.NUMBER);
 format.setNumberFormat(JsonFormat.NumberFormat.INT_VALUE);
 ObjectWriter writer = mapper.writerFor(Number.class)
 .withValueToNumberSerializer(NumberSerializer.instance)
 .withSerializerNullProvider(mapper.getSerializerProvider().getNullValueSerializer())
 .withRootValueSerializer(new DefaultSerializerProvider.SerializerProviderMap().findValueSerializer(JsonNode.class, mapper));
 JsonNode node = writer.writeValueAsTree(new BigInteger("123456789"));
 assertEquals("123456789", node.asText());
 }
}