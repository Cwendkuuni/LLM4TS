package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializeInt() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializerProvider();
 int value = 123;
 numberSerializer.serialize(value, generator, provider);
 generator.flush();
 String json = generator.getOutputStr();
 assertEquals("123", json);
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializerProvider();
 long value = 123456;
 numberSerializer.serialize(value, generator, provider);
 generator.flush();
 String json = generator.getOutputStr();
 assertEquals("123456", json);
 }

 @Test
 public void testSerializeShort() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializerProvider();
 short value = 12;
 numberSerializer.serialize(value, generator, provider);
 generator.flush();
 String json = generator.getOutputStr();
 assertEquals("12", json);
 }

 @Test
 public void testSerializeByte() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializerProvider();
 byte value = 1;
 numberSerializer.serialize(value, generator, provider);
 generator.flush();
 String json = generator.getOutputStr();
 assertEquals("1", json);
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializerProvider();
 float value = 123.5f;
 numberSerializer.serialize(value, generator, provider);
 generator.flush();
 String json = generator.getOutputStr();
 assertEquals("123.5", json);
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializerProvider();
 double value = 123.5;
 numberSerializer.serialize(value, generator, provider);
 generator.flush();
 String json = generator.getOutputStr();
 assertEquals("123.5", json);
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializerProvider();
 BigInteger value = new BigInteger("123456789012345678901234567890");
 numberSerializer.serialize(value, generator, provider);
 generator.flush();
 String json = generator.getOutputStr();
 assertEquals("123456789012345678901234567890", json);
 }

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializerProvider();
 BigDecimal value = new BigDecimal("123456789012345678901234567890.1234");
 numberSerializer.serialize(value, generator, provider);
 generator.flush();
 String json = generator.getOutputStr();
 assertEquals("123456789012345678901234567890.1234", json);
 }

 @Test
 public void testSerializeUnknownNumberType() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializerProvider();
 Object value = new Object() {
 };
 numberSerializer.serialize(value, generator, provider);
 generator.flush();
 String json = generator.getOutputStr();
 assertEquals("null", json);
 }

 @Test
 public void testDeserializeInt() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("123");
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(123, deserializedNumber.intValue());
 }

 @Test
 public void testDeserializeLong() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("123456");
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(123456, deserializedNumber.longValue());
 }

 @Test
 public void testDeserializeShort() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("12");
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(12, deserializedNumber.shortValue());
 }

 @Test
 public void testDeserializeByte() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("1");
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(1, deserializedNumber.byteValue());
 }

 @Test
 public void testDeserializeFloat() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("123.5");
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(123.5f, deserializedNumber.floatValue(), 0.001f);
 }

 @Test
 public void testDeserializeDouble() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("123.5");
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(123.5, deserializedNumber.doubleValue(), 0.001);
 }

 @Test
 public void testDeserializeBigInteger() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("123456789012345678901234567890");
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 BigInteger bigInteger = (BigInteger) deserializedNumber;
 assertEquals(new BigInteger("123456789012345678901234567890"), bigInteger);
 }

 @Test
 public void testDeserializeBigDecimal() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("123456789012345678901234567890.1234");
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 BigDecimal bigDecimal = (BigDecimal) deserializedNumber;
 assertEquals(new BigDecimal("123456789012345678901234567890.1234"), bigDecimal);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnknownNumberType() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("abcd");
 mapper.readValue(parser, Number.class);
 }
}