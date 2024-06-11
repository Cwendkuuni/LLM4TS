package com.yourpackage.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer.ValueType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper.DefaultTyping;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeNumber() throws JsonMappingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;

 // Test serialization of various Number types
 assertEquals(ValueType.NUMBER, numberSerializer.getSerializationShape(null));

 Number number = 123;
 JsonNode actual = mapper.writeValueAsTree(number, DefaultTyping.instance());
 assertEquals("123", actual.toString());

 number = 123L;
 actual = mapper.writeValueAsTree(number, DefaultTyping.instance());
 assertEquals("123", actual.toString());

 number = 123.0;
 actual = mapper.writeValueAsTree(number, DefaultTyping.instance());
 assertEquals("123.0", actual.toString());

 number = 123.0f;
 actual = mapper.writeValueAsTree(number, DefaultTyping.instance());
 assertEquals("123.0", actual.toString());

 number = 123.0d;
 actual = mapper.writeValueAsTree(number, DefaultTyping.instance());
 assertEquals("123.0", actual.toString());

 number = new BigDecimal("123.45");
 actual = mapper.writeValueAsTree(number, DefaultTyping.instance());
 assertEquals("123.45", actual.toString());

 number = new BigInteger("12345678901234567890");
 ObjectNode expected = JsonNodeFactory.instance.objectNode();
 expected.put("@type", "number");
 expected.put("value", "1.2345678901234567890E14");
 actual = (ObjectNode) mapper.writeValueAsTree(number, DefaultTyping.instance());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testSerializeBigDecimal() throws JsonMappingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;

 BigDecimal bigDecimal = new BigDecimal("123.45678901234567890");
 JsonNode actual = mapper.writeValueAsTree(bigDecimal, DefaultTyping.instance());
 ObjectNode expected = JsonNodeFactory.instance.objectNode();
 expected.put("@type", "number");
 expected.put("value", "123.45678901234567890");
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testSerializeBigInteger() throws JsonMappingException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializer.instance;

 BigInteger bigInteger = new BigInteger("12345678901234567890");
 JsonNode actual = mapper.writeValueAsTree(bigInteger, DefaultTyping.instance());
 ObjectNode expected = JsonNodeFactory.instance.objectNode();
 expected.put("@type", "number");
 expected.put("value", "1.2345678901234567890E14");
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
 SerializerProvider provider = mapper.getSerializerProvider();
 Number number = 123;
 JsonSerializer<Number> serializer = numberSerializer.createContextual(provider, null);
 assertEquals(NumberSerializer.class, serializer.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextualWithJsonFormat() throws JsonMappingException {
 SerializerProvider provider = mapper.getSerializerProvider();
 Number number = 123;
 JsonFormat.Value format = new JsonFormat.Value();
 JsonSerializer<Number> serializer = numberSerializer.createContextual(provider, null);
 JsonSerializer<Number> serializerWithFormat = numberSerializer.createContextual(provider, null, format);
 assertEquals(serializer.getClass(), serializerWithFormat.getClass());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = objectMapper.getSerializerProvider().getDefaultVisibilityChecker().findSerializerProviderDefault(JsonFormatVisitorWrapper.class);
 NumberSerializer numberSerializer = NumberSerializer.instance;
 visitor.expectNumberFormat(null);
 numberSerializer.acceptJsonFormatVisitor(visitor, null);
 // Additional assertions can be added here if needed
 }
}