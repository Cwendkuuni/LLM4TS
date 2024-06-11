package com.example.numberserializer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for NumberSerializer
 *
 */
public class NumberSerializerTest {

 private ObjectMapper objectMapper;
 private JsonNodeFactory jsonNodeFactory;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 jsonNodeFactory = objectMapper.getNodeFactory();
 }

 /**
 * Test for serialize method with BigDecimal input
 */
 @Test
 public void testSerializeBigDecimal() throws Exception {
 BigDecimal bigDecimal = new BigDecimal("123.456");
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(bigDecimal, jsonGenerator, serializerProvider);
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputBuffer());
 TextNode expectedTextNode = jsonNodeFactory.textNode("123.456");
 assertEquals(expectedTextNode, actualJsonNode);
 }

 /**
 * Test for serialize method with BigInteger input
 */
 @Test
 public void testSerializeBigInteger() throws Exception {
 BigInteger bigInteger = new BigInteger("123");
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(bigInteger, jsonGenerator, serializerProvider);
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputBuffer());
 TextNode expectedTextNode = jsonNodeFactory.textNode("123");
 assertEquals(expectedTextNode, actualJsonNode);
 }

 /**
 * Test for serialize method with Integer input
 */
 @Test
 public void testSerializeInteger() throws Exception {
 Integer integer = 123;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(integer, jsonGenerator, serializerProvider);
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputBuffer());
 TextNode expectedTextNode = jsonNodeFactory.textNode("123");
 assertEquals(expectedTextNode, actualJsonNode);
 }

 /**
 * Test for serialize method with Long input
 */
 @Test
 public void testSerializeLong() throws Exception {
 Long longValue = 123L;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(longValue, jsonGenerator, serializerProvider);
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputBuffer());
 TextNode expectedTextNode = jsonNodeFactory.textNode("123");
 assertEquals(expectedTextNode, actualJsonNode);
 }

 /**
 * Test for serialize method with Double input
 */
 @Test
 public void testSerializeDouble() throws Exception {
 Double doubleValue = 123.456;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(doubleValue, jsonGenerator, serializerProvider);
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputBuffer());
 TextNode expectedTextNode = jsonNodeFactory.textNode("123.456");
 assertEquals(expectedTextNode, actualJsonNode);
 }

 /**
 * Test for serialize method with Float input
 */
 @Test
 public void testSerializeFloat() throws Exception {
 Float floatValue = 123.456f;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(floatValue, jsonGenerator, serializerProvider);
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputBuffer());
 TextNode expectedTextNode = jsonNodeFactory.textNode("123.456");
 assertEquals(expectedTextNode, actualJsonNode);
 }

 /**
 * Test for serialize method with Byte input
 */
 @Test
 public void testSerializeByte() throws Exception {
 Byte byteValue = (byte) 123;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(byteValue, jsonGenerator, serializerProvider);
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputBuffer());
 TextNode expectedTextNode = jsonNodeFactory.textNode("123");
 assertEquals(expectedTextNode, actualJsonNode);
 }

 /**
 * Test for serialize method with Short input
 */
 @Test
 public void testSerializeShort() throws Exception {
 Short shortValue = (short) 123;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(shortValue, jsonGenerator, serializerProvider);
 JsonNode actualJsonNode = objectMapper.readTree(jsonGenerator.getOutputBuffer());
 TextNode expectedTextNode = jsonNodeFactory.textNode("123");
 assertEquals(expectedTextNode, actualJsonNode);
 }

 /**
 * Test for serialize method with null input
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNullInput() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 numberSerializer.serialize(null, jsonGenerator, serializerProvider);
 }

 /**
 * Test for getSchema method
 */
 @Test
 public void testGetSchema() throws Exception {
 Type type = TypeFactory.defaultInstance().constructType(Number.class);
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonNode actualJsonNode = numberSerializer.getSchema(objectMapper.getSerializerProvider(), type);
 TextNode expectedTextNode = jsonNodeFactory.textNode("number");
 assertEquals(expectedTextNode, actualJsonNode);
 }

 /**
 * Test for acceptJsonFormatVisitor method with integer format
 */
 @Test
 public void testAcceptJsonFormatVisitorIntegerFormat() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = objectMapper.getPolymorphicSerializerProvider().findValueSerializer(BigInteger.class);
 numberSerializer.acceptJsonFormatVisitor(visitor, TypeFactory.defaultInstance().constructType(Number.class));
 }

 /**
 * Test for acceptJsonFormatVisitor method with float format
 */
 @Test
 public void testAcceptJsonFormatVisitorFloatFormat() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = objectMapper.getPolymorphicSerializerProvider().findValueSerializer(BigDecimal.class);
 numberSerializer.acceptJsonFormatVisitor(visitor, TypeFactory.defaultInstance().constructType(Number.class));
 }

 /**
 * Test for acceptJsonFormatVisitor method with unsupported format
 */
 @Test(expected = JsonMappingException.class)
 public void testAcceptJsonFormatVisitorUnsupportedFormat() throws Exception {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.acceptJsonFormatVisitor(visitor, TypeFactory.defaultInstance().constructType(Number.class));
 }
}