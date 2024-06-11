package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.DefaultSerializers;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.JavaType;

@SuppressWarnings("unchecked")
public class NumberSerializerTest {

 private NumberSerializer numberSerializer;
 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockSerializerProvider;
 private ObjectMapper mockObjectMapper;

 @Before
 public void setUp() {
 numberSerializer = new NumberSerializer(Number.class);
 mockJsonGenerator = mock(JsonGenerator.class);
 mockSerializerProvider = mock(SerializerProvider.class);
 mockObjectMapper = new ObjectMapper();
 DefaultSerializers serializers = mockObjectMapper.getDefaultSerializers();
 when(mockSerializerProvider.findValueSerializer(Number.class)).thenReturn(numberSerializer);
 when(mockSerializerProvider.getSerializers()).thenReturn(serializers);
 }

 @Test
 public void testSerializeBigDecimal() throws IOException {
 BigDecimal number = new BigDecimal("123.45");
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123.45", mockJsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeBigInteger() throws IOException {
 BigInteger number = new BigInteger("123");
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123", mockJsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeInteger() throws IOException {
 Integer number = 123;
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123", mockJsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeLong() throws IOException {
 Long number = 123L;
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123", mockJsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeDouble() throws IOException {
 Double number = 123.45;
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123.45", mockJsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeFloat() throws IOException {
 Float number = 123.45F;
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123.45", mockJsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeByte() throws IOException {
 Byte number = (byte) 123;
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123", mockJsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeShort() throws IOException {
 Short number = (short) 123;
 numberSerializer.serialize(number, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123", mockJsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeUnsupportedNumberType() throws IOException {
 Object unsupportedNumber = new Object();
 numberSerializer.serialize(unsupportedNumber, mockJsonGenerator, mockSerializerProvider);
 assertEquals("123", mockJsonGenerator.getOutputBuffer().toString()); // This should be the default number value: "123"
 }

 @Test
 public void testGetSchema() throws Exception {
 JsonNode schema = numberSerializer.getSchema(mockSerializerProvider, Number.class);
 assertEquals("number", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws Exception {
 JsonFormatVisitorWrapper mockJsonFormatVisitor = mock(JsonFormatVisitorWrapper.class);
 numberSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitor, Number.class);
 // Verification code for acceptJsonFormatVisitor method goes here
 }
}