package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal number = new BigDecimal("123456789.123456789");
 JsonNode expectedJson = JsonNodeFactory.instance.numberNode("123456789.123456789");

 JsonNode actualJson = mapper.writeValueAsTree(number, numberSerializer);
 assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger number = new BigInteger("98765432109876543210");
 JsonNode expectedJson = JsonNodeFactory.instance.numberNode("98765432109876543210");

 JsonNode actualJson = mapper.writeValueAsTree(number, numberSerializer);
 assertEquals(expectedJson, actualJson);
 }

 @Test (expected = JsonGenerationException.class)
 public void testSerializeInvalidNumber() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Object invalidNumber = new Object();
 JsonNode actualJson = mapper.writeValueAsTree(invalidNumber, numberSerializer);
 fail("Expected JsonGenerationException but got: " + actualJson.toString());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getStdSerializerProvider().findValueSerializer(JsonNode.class);
 JsonNode schema = numberSerializer.acceptJsonFormatVisitor(visitor, JsonType.NUMBER);
 assertEquals("number", schema.asText());
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonNode schema = numberSerializer.getSchema(mapper.getSerializerProvider(), Number.class);
 assertEquals("number", schema.asText());
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BeanProperty property = new BeanProperty("testProperty", JsonType.NUMBER, null);
 JsonSerializer<?> serializer = numberSerializer.createContextual(mapper.getSerializerProvider(), property);
 assertEquals(toStringSerializer(Number.class), serializer);
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Long number = 1234567890L;
 JsonNode expectedJson = JsonNodeFactory.instance.numberNode("1234567890");

 JsonNode actualJson = mapper.writeValueAsTree(number, numberSerializer);
 assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Double number = 1234567890.123456789;
 JsonNode expectedJson = JsonNodeFactory.instance.numberNode("1234567890.123456789");

 JsonNode actualJson = mapper.writeValueAsTree(number, numberSerializer);
 assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Float number = 1234567890.123f;
 JsonNode expectedJson = JsonNodeFactory.instance.numberNode("1234567890.123"); // float is rounded

 JsonNode actualJson = mapper.writeValueAsTree(number, numberSerializer);
 assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeByte() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Byte number = (byte) 127;
 JsonNode expectedJson = JsonNodeFactory.instance.numberNode("127");

 JsonNode actualJson = mapper.writeValueAsTree(number, numberSerializer);
 assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeShort() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Short number = (short) 1234;
 JsonNode expectedJson = JsonNodeFactory.instance.numberNode("1234");

 JsonNode actualJson = mapper.writeValueAsTree(number, numberSerializer);
 assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeInteger() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Integer number = 1234567890;
 JsonNode expectedJson = JsonNodeFactory.instance.numberNode("1234567890");

 JsonNode actualJson = mapper.writeValueAsTree(number, numberSerializer);
 assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeIntArray() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Object intArray = new int[] { 1, 2, 3, 4, 5 };
 JsonNode expectedJson = JsonNodeFactory.instance.arrayNode(JsonNodeFactory.instance.numberNode("1"),
 JsonNodeFactory.instance.numberNode("2"), JsonNodeFactory.instance.numberNode("3"),
 JsonNodeFactory.instance.numberNode("4"), JsonNodeFactory.instance.numberNode("5") );

 JsonNode actualJson = mapper.writeValueAsTree(intArray, numberSerializer);
 assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeNumberArray() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 Object numberArray = new Number[] { new BigDecimal("123.45"), new BigInteger("321"), new Double(1.23) };
 JsonNode expectedJson = JsonNodeFactory.instance.arrayNode(JsonNodeFactory.instance.numberNode("123.45"),
 JsonNodeFactory.instance.numberNode("321"), JsonNodeFactory.instance.numberNode("1.23") );

 JsonNode actualJson = mapper.writeValueAsTree(numberArray, numberSerializer);
 assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testToStringSerializerForNumber() throws JsonProcessingException {
 JsonSerializer<Number> toStringSerializer = ToStringSerializer.instance;
 JsonNode expectedJson = JsonNodeFactory.instance.valueNode("1234567890");

 JsonNode actualJson = mapper.writeValueAsTree(1234567890, toStringSerializer);
 assertEquals(expectedJson, actualJson);
 }

 private JsonSerializer<Number> toStringSerializer(Class<Number> clazz) {
 return mapper.findValueSerializer(clazz);
 }

}