package com.example.test.jackson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for NumberSerializer.
 */
public class NumberSerializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setup() {
 SimpleModule module = new SimpleModule();
 module.addSerializer(Number.class, NumberSerializer.instance);
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(module);
 NumberSerializerTest.objectMapper = mapper;
 }

 /**
 * Test method for {@link NumberSerializer#NumberSerializer(Class)}.
 */
 @Test
 public void testNumberSerializerConstructor() {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 assertNull(numberSerializer.getClass());
 assertEquals(Number.class, numberSerializer.handledType());
 }

 /**
 * Test method for {@link NumberSerializer#createContextual(SerializerProvider, BeanProperty)}.
 */
 @Test
 public void testCreateContextual() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Number.class);
 BeanProperty property = new BeanProperty("", "numberProperty", Number.class);
 assertEquals(numberSerializer, numberSerializer.createContextual(NumberSerializerTest.objectMapper.getSerializerProvider(), property));
 }

 /**
 * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
 */
 @Test
 public void testSerializeLong() throws IOException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Long.class);
 Long value = 1L;
 String expected = "1";
 JsonGenerator jsonGenerator = NumberSerializerTest.objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(value, jsonGenerator, NumberSerializerTest.objectMapper.getSerializerProvider());
 assertEquals(expected, jsonGenerator.getOutputBuffer().toString());
 }

 /**
 * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
 */
 @Test
 public void testSerializeDouble() throws IOException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Double.class);
 Double value = 1.0;
 String expected = "1.0";
 JsonGenerator jsonGenerator = NumberSerializerTest.objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(value, jsonGenerator, NumberSerializerTest.objectMapper.getSerializerProvider());
 assertEquals(expected, jsonGenerator.getOutputBuffer().toString());
 }

 /**
 * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
 */
 @Test
 public void testSerializeInteger() throws IOException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Integer.class);
 Integer value = 1;
 String expected = "1";
 JsonGenerator jsonGenerator = NumberSerializerTest.objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(value, jsonGenerator, NumberSerializerTest.objectMapper.getSerializerProvider());
 assertEquals(expected, jsonGenerator.getOutputBuffer().toString());
 }

 /**
 * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
 */
 @Test
 public void testSerializeByte() throws IOException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Byte.class);
 Byte value = (byte) 1;
 String expected = "1";
 JsonGenerator jsonGenerator = NumberSerializerTest.objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(value, jsonGenerator, NumberSerializerTest.objectMapper.getSerializerProvider());
 assertEquals(expected, jsonGenerator.getOutputBuffer().toString());
 }

 /**
 * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
 */
 @Test
 public void testSerializeShort() throws IOException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Short.class);
 Short value = (short) 1;
 String expected = "1";
 JsonGenerator jsonGenerator = NumberSerializerTest.objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(value, jsonGenerator, NumberSerializerTest.objectMapper.getSerializerProvider());
 assertEquals(expected, jsonGenerator.getOutputBuffer().toString());
 }

 /**
 * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
 */
 @Test
 public void testSerializeBigInteger() throws IOException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(BigInteger.class);
 BigInteger value = new BigInteger("1");
 String expected = "1";
 JsonGenerator jsonGenerator = NumberSerializerTest.objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(value, jsonGenerator, NumberSerializerTest.objectMapper.getSerializerProvider());
 assertEquals(expected, jsonGenerator.getOutputBuffer().toString());
 }

 /**
 * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
 */
 @Test
 public void testSerializeBigDecimal() throws IOException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(BigDecimal.class);
 BigDecimal value = new BigDecimal("1");
 String expected = "1";
 JsonGenerator jsonGenerator = NumberSerializerTest.objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(value, jsonGenerator, NumberSerializerTest.objectMapper.getSerializerProvider());
 assertEquals(expected, jsonGenerator.getOutputBuffer().toString());
 }

 /**
 * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
 */
 @Test(expected = JsonGenerationException.class)
 public void testSerializeNull() throws IOException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Number.class);
 JsonGenerator jsonGenerator = NumberSerializerTest.objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(null, jsonGenerator, NumberSerializerTest.objectMapper.getSerializerProvider());
 }

 /**
 * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
 */
 @Test(expected = JsonGenerationException.class)
 public void testSerializeBigIntegerNegative() throws IOException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(BigInteger.class);
 BigInteger value = new BigInteger("-1");
 JsonGenerator jsonGenerator = NumberSerializerTest.objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(value, jsonGenerator, NumberSerializerTest.objectMapper.getSerializerProvider());
 }

 /**
 * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
 */
 @Test(expected = JsonGenerationException.class)
 public void testSerializeBigDecimalNegative() throws IOException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(BigDecimal.class);
 BigDecimal value = new BigDecimal("-1");
 JsonGenerator jsonGenerator = NumberSerializerTest.objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(value, jsonGenerator, NumberSerializerTest.objectMapper.getSerializerProvider());
 }

 /**
 * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
 */
 @Test(expected = JsonGenerationException.class)
 public void testSerializeNaN() throws IOException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Double.class);
 Double value = Double.NaN;
 JsonGenerator jsonGenerator = NumberSerializerTest.objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(value, jsonGenerator, NumberSerializerTest.objectMapper.getSerializerProvider());
 }

 /**
 * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
 */
 @Test(expected = JsonGenerationException.class)
 public void testSerializeInfinity() throws IOException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Double.class);
 Double value = Double.POSITIVE_INFINITY;
 JsonGenerator jsonGenerator = NumberSerializerTest.objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(value, jsonGenerator, NumberSerializerTest.objectMapper.getSerializerProvider());
 }

 /**
 * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
 */
 @Test(expected = JsonGenerationException.class)
 public void testSerializeNegativeInfinity() throws IOException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Double.class);
 Double value = Double.NEGATIVE_INFINITY;
 JsonGenerator jsonGenerator = NumberSerializerTest.objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(value, jsonGenerator, NumberSerializerTest.objectMapper.getSerializerProvider());
 }

 /**
 * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}.
 */
 @Test
 public void testSerializeString() throws IOException, JsonGenerationException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(String.class);
 String value = "1";
 String expected = "1";
 JsonGenerator jsonGenerator = NumberSerializerTest.objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(value, jsonGenerator, NumberSerializerTest.objectMapper.getSerializerProvider());
 assertEquals(expected, jsonGenerator.getOutputBuffer().toString());
 }

 /**
 * Test method for {@link NumberSerializer#getSchema(SerializerProvider, Type)}.
 */
 @Test
 public void testGetSchemaLong() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Long.class);
 assertEquals("integer", numberSerializer.getSchema(NumberSerializerTest.objectMapper.getSerializerProvider(), Long.TYPE).getSchemaName());
 }

 /**
 * Test method for {@link NumberSerializer#getSchema(SerializerProvider, Type)}.
 */
 @Test
 public void testGetSchemaDouble() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Double.class);
 assertEquals("number", numberSerializer.getSchema(NumberSerializerTest.objectMapper.getSerializerProvider(), Double.TYPE).getSchemaName());
 }

 /**
 * Test method for {@link NumberSerializer#getSchema(SerializerProvider, Type)}.
 */
 @Test
 public void testGetSchemaInteger() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Integer.class);
 assertEquals("integer", numberSerializer.getSchema(NumberSerializerTest.objectMapper.getSerializerProvider(), Integer.TYPE).getSchemaName());
 }

 /**
 * Test method for {@link NumberSerializer#getSchema(SerializerProvider, Type)}.
 */
 @Test
 public void testGetSchemaByte() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Byte.class);
 assertEquals("integer", numberSerializer.getSchema(NumberSerializerTest.objectMapper.getSerializerProvider(), Byte.TYPE).getSchemaName());
 }

 /**
 * Test method for {@link NumberSerializer#getSchema(SerializerProvider, Type)}.
 */
 @Test
 public void testGetSchemaShort() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Short.class);
 assertEquals("integer", numberSerializer.getSchema(NumberSerializerTest.objectMapper.getSerializerProvider(), Short.TYPE).getSchemaName());
 }

 /**
 * Test method for {@link NumberSerializer#getSchema(SerializerProvider, Type)}.
 */
 @Test
 public void testGetSchemaBigInteger() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(BigInteger.class);
 assertEquals("integer", numberSerializer.getSchema(NumberSerializerTest.objectMapper.getSerializerProvider(), BigInteger.TYPE).getSchemaName());
 }

 /**
 * Test method for {@link NumberSerializer#getSchema(SerializerProvider, Type)}.
 */
 @Test
 public void testGetSchemaBigDecimal() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(BigDecimal.class);
 assertEquals("number", numberSerializer.getSchema(NumberSerializerTest.objectMapper.getSerializerProvider(), BigDecimal.class).getSchemaName());
 }

 /**
 * Test method for {@link NumberSerializer#acceptJsonFormatVisitor(JsonFormatVisitorWrapper, JavaType)}.
 */
 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializerTest.objectMapper.findValueSerializer(Number.class);
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = new JsonFormatVisitorWrapper(NumberSerializerTest.objectMapper.getSerializerProvider().getActiveContext());
 assertNull(numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, Number.class));
 }
}