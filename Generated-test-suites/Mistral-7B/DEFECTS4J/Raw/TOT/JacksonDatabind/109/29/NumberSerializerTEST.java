package com.example.junit4.test.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.JsonSchemaGenerator;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

 private final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("123456.7890123");

 JsonNode expectedSchema = JsonSchemaGenerator.construct(objectMapper.getClass(), NumberSerializer.class);
 JsonNode expectedJson = objectMapper.readTree("{\"number\": \"123456.7890123\"}");

 // Test serialization
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(bigDecimal, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();

 // Test schema
 JsonNode actualSchema = numberSerializer.getSchema(objectMapper.getSerializerProvider(), Number.class.getType());
 JsonNode actualJson = objectMapper.valueToTree(bigDecimal);

 assertEquals(expectedSchema.toString(), actualSchema.toString());
 assertEquals(expectedJson.toString(), actualJson.toString());
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger bigInteger = new BigInteger("12345678901234567890");

 JsonNode expectedSchema = JsonSchemaGenerator.construct(objectMapper.getClass(), NumberSerializer.class);
 JsonNode expectedJson = objectMapper.readTree("{\"number\": \"12345678901234567890\"}");

 // Test serialization
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(bigInteger, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();

 // Test schema
 JsonNode actualSchema = numberSerializer.getSchema(objectMapper.getSerializerProvider(), Number.class.getType());
 JsonNode actualJson = objectMapper.valueToTree(bigInteger);

 assertEquals(expectedSchema.toString(), actualSchema.toString());
 assertEquals(expectedJson.toString(), actualJson.toString());
 }

 @Test
 public void testSerializeLong() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Long longValue = 123456;

 JsonNode expectedSchema = JsonSchemaGenerator.construct(objectMapper.getClass(), NumberSerializer.class);
 JsonNode expectedJson = objectMapper.readTree("{\"number\": 123456}");

 // Test serialization
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 numberSerializer.serialize(longValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.flush();

 // Test schema
 JsonNode actualSchema = numberSerializer.getSchema(objectMapper.getSerializerProvider(), Number.class.getType());
 JsonNode actualJson = objectMapper.valueToTree(longValue);

 assertEquals(expectedSchema.toString(), actualSchema.toString());
 assertEquals(expectedJson.toString(), actualJson.toString());
 }

 @Test
 public void testSerializeUnknownNumberType() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Object unknownNumber = new Object();

 // Test serialization
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 try {
 numberSerializer.serialize(unknownNumber, jsonGenerator, objectMapper.getSerializerProvider());
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 jsonGenerator.flush();
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 JsonFormatVisitorWrapper jsonFormatVisitor = objectMapper.getPolymorphicSerializerProvider().findSerializerValue(ObjectNode.class, null);

 // Test serialization
 JsonNode expectedSchema = JsonSchemaGenerator.construct(objectMapper.getClass(), NumberSerializer.class);

 numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitor, Number.class.getType());
 JsonNode actualSchema = jsonFormatVisitor.getSchema();

 assertEquals(expectedSchema.toString(), actualSchema.toString());
 }
}