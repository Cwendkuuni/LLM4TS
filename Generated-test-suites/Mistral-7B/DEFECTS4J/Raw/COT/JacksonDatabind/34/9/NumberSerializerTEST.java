package com.example.numberserializer;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.JavaType;

@JacksonStdImpl
public class NumberSerializerTest {

 private NumberSerializer numberSerializer;
 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockSerializerProvider;
 private ObjectMapper mockObjectMapper;

 @Before
 public void setup() {
 numberSerializer = new NumberSerializer(Number.class);
 mockJsonGenerator = mock(JsonGenerator.class);
 mockSerializerProvider = mock(SerializerProvider.class);
 mockObjectMapper = mock(ObjectMapper.class);
 }

 @Test
 public void testSerializeInteger() throws JsonProcessingException, IOException {
 // Given
 int value = 123;

 // When
 numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

 // Then
 assertEquals("123", mockJsonGenerator.getOutputContentAsString());
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException, IOException {
 // Given
 long value = 456;

 // When
 numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

 // Then
 assertEquals("456", mockJsonGenerator.getOutputContentAsString());
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException, IOException {
 // Given
 double value = 789.123;

 // When
 numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

 // Then
 assertEquals("789.123", mockJsonGenerator.getOutputContentAsString());
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException, IOException {
 // Given
 float value = 2.5f;

 // When
 numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

 // Then
 assertEquals("2.5", mockJsonGenerator.getOutputContentAsString());
 }

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException, IOException {
 // Given
 BigDecimal value = new BigDecimal("123.456");

 // When
 numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

 // Then
 assertEquals("123.456", mockJsonGenerator.getOutputContentAsString());
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException, IOException {
 // Given
 BigInteger value = new BigInteger("12345678901234567890");

 // When
 numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

 // Then
 assertEquals("12345678901234567890", mockJsonGenerator.getOutputContentAsString());
 }

 @Test(expected = IOException.class)
 public void testSerializeUnsupportedType() throws JsonProcessingException, IOException {
 // Given
 String value = "UnsupportedType";

 // When & Then
 numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 // Given
 JavaType javaType = mock(JavaType.class);

 // When
 JsonNode schema = numberSerializer.getSchema(mockSerializerProvider, javaType);

 // Then
 assertEquals("number", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitorInteger() throws JsonMappingException {
 // Given
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 JavaType typeHint = mock(JavaType.class);

 when(typeHint.getRawClass()).thenReturn(Integer.class);

 // When
 numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

 // Then
 // Unclear what to verify here as the implementation is missing
 }

 @Test
 public void testAcceptJsonFormatVisitorBigDecimal() throws JsonMappingException {
 // Given
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 JavaType typeHint = mock(JavaType.class);

 when(typeHint.getRawClass()).thenReturn(BigDecimal.class);

 // When
 numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

 // Then
 // Unclear what to verify here as the implementation is missing
 }
}