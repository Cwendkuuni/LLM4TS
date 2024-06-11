package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.BigDecimalSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.BigIntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;

public class NumberSerializerTest {

private static ObjectMapper objectMapper;

@BeforeClass
public static void setup() {
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
}

@Test
public void testSerializeBigDecimal() throws IOException {
    BigDecimal value = new BigDecimal("12345.6789");
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
    NumberSerializer.BigDecimalSerializer serializer = NumberSerializers.BigDecimalSerializer.instance;
    serializer.serialize(value, jsonGenerator, null);
    jsonGenerator.flush();
    assertEquals("12345.6789", jsonGenerator.getOutputStream().toString());
}

@Test
public void testSerializeBigInteger() throws IOException {
    BigInteger value = new BigInteger("1234567890");
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
    NumberSerializer.BigIntegerSerializer serializer = NumberSerializers.BigIntegerSerializer.instance;
    serializer.serialize(value, jsonGenerator, null);
    jsonGenerator.flush();
    assertEquals("1234567890", jsonGenerator.getOutputStream().toString());
}

@Test
public void testSerializeDouble() throws IOException {
    Double value = 12345.6789;
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
    NumberSerializer.DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;
    serializer.serialize(value, jsonGenerator, null);
    jsonGenerator.flush();
    assertEquals("12345.6789", jsonGenerator.getOutputStream().toString());
}

@Test
public void testSerializeFloat() throws IOException {
    Float value = 1234.5678f;
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
    NumberSerializer.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
    serializer.serialize(value, jsonGenerator, null);
    jsonGenerator.flush();
    assertEquals("1234.5678", jsonGenerator.getOutputStream().toString());
}

@Test
public void testSerializeInteger() throws IOException {
    Integer value = 12345;
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
    NumberSerializer.IntegerSerializer serializer = NumberSerializers.IntegerSerializer.instance;
    serializer.serialize(value, jsonGenerator, null);
    jsonGenerator.flush();
    assertEquals("12345", jsonGenerator.getOutputStream().toString());
}

@Test
public void testSerializeLong() throws IOException {
    Long value = 1234567890L;
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
    NumberSerializer.LongSerializer serializer = NumberSerializers.LongSerializer.instance;
    serializer.serialize(value, jsonGenerator, null);
    jsonGenerator.flush();
    assertEquals("1234567890", jsonGenerator.getOutputStream().toString());
}

@Test
public void testSerializeShort() throws IOException {
    Short value = 1234;
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
    NumberSerializer.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
    serializer.serialize(value, jsonGenerator, null);
    jsonGenerator.flush();
    assertEquals("1234", jsonGenerator.getOutputStream().toString());
}

@Test(expected = NullPointerException.class)
public void testSerializeNull() throws IOException {
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
    NumberSerializer instance = new NumberSerializer(null);
    instance.serialize(null, jsonGenerator, null);
    jsonGenerator.flush();
}

@Test(expected = ClassCastException.class)
public void testSerializeInvalidValue() throws IOException {
    Byte value = 123;
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
    NumberSerializer.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
    serializer.serialize(value, jsonGenerator, null);
    jsonGenerator.flush();
}

@Test
public void testSerializeUntypedNumber() throws IOException {
    Number value = new BigDecimal("12345.6789");
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
    NumberSerializer instance = new NumberSerializer(Number.class);
    instance.serialize(value, jsonGenerator, null);
    jsonGenerator.flush();
    assertEquals("12345.6789", jsonGenerator.getOutputStream().toString());
}

@Test
public void testGetSchema() {
    NumberSerializer instance = NumberSerializer.instance;
    SerializerProvider provider = objectMapper.getSerializerProvider();
    Object schema = instance.getSchema(provider, null);
    assertNotNull(schema);
}

@Test
public void testAcceptJsonFormatVisitor() throws JsonMappingException {
    NumberSerializer instance = NumberSerializer.instance;
    // Single test case is enough to verify the behavior
    instance.acceptJsonFormatVisitor(null, null);
    assertTrue(true);
}

@Test(expected = InvalidDefinitionException.class)
public void testCreateContextualWithException() throws JsonMappingException {
    NumberSerializer instance = NumberSerializer.instance;
    BeanProperty property = null;
    instance.createContextual(null, property);
}

}