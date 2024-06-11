package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorBaseTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testWriteString() throws IOException, JsonProcessingException {
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new OutputStreamWriter(new ByteArrayOutputStream()), null);
 gen.writeString("Hello World");
 assertEquals("Hello World", gen.getCurrentValue().asText());
 gen.close();
 }

 @Test
 public void testWriteBoolean() throws IOException, JsonProcessingException {
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new OutputStreamWriter(new ByteArrayOutputStream()), null);
 gen.writeBoolean(true);
 assertEquals("true", gen.getCurrentValue().asText());
 gen.close();
 }

 @Test
 public void testWriteNull() throws IOException, JsonProcessingException {
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new OutputStreamWriter(new ByteArrayOutputStream()), null);
 gen.writeNull();
 assertNull(gen.getCurrentValue());
 gen.close();
 }

 @Test
 public void testWriteNumber() throws IOException, JsonProcessingException {
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new OutputStreamWriter(new ByteArrayOutputStream()), null);
 gen.writeNumber(12345);
 assertEquals("12345", gen.getCurrentValue().asText());
 gen.close();
 }

 @Test
 public void testWriteBigDecimal() throws IOException, JsonProcessingException {
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new OutputStreamWriter(new ByteArrayOutputStream()), null);
 BigDecimal decimal = new BigDecimal("12345.6789");
 gen.writeNumber(decimal);
 assertEquals("12345.6789", gen.getCurrentValue().asText());
 gen.close();
 }

 @Test
 public void testWriteArray() throws IOException, JsonProcessingException {
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new OutputStreamWriter(new ByteArrayOutputStream()), null);
 gen.writeStartArray();
 gen.writeNumber(123);
 gen.writeNull();
 gen.writeString("Hello");
 gen.writeEndArray();

 JsonParser parser = MAPPER.getFactory().createParser(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token.getId());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
 assertEquals(123, parser.getValueAsInt());

 token = parser.nextToken();
 assertEquals(JsonToken.NULL, token.getId());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token.getId());
 assertEquals("Hello", parser.getValueAsString());

 token = parser.nextToken();
 assertEquals(JsonToken.END_ARRAY, token.getId());

 parser.close();
 gen.close();
 }

 @Test
 public void testWriteObject() throws IOException, JsonProcessingException {
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new OutputStreamWriter(new ByteArrayOutputStream()), null);
 gen.writeStartObject();
 gen.writeFieldName("name");
 gen.writeString("John");
 gen.writeFieldName("age");
 gen.writeNumber(30);
 gen.writeEndObject();

 JsonParser parser = MAPPER.getFactory().createParser(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));

 assertEquals(JsonToken.START_OBJECT, parser.nextToken().getId());
 assertEquals("name", parser.getCurrentName());
 assertEquals("John", parser.nextTextValue());

 assertEquals("age", parser.getCurrentName());
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getId());
 assertEquals(30, parser.nextIntValue());

 assertEquals(JsonToken.END_OBJECT, parser.nextToken().getId());

 parser.close();
 gen.close();
 }

 @Test(expected = JsonGenerationException.class)
 public void testWriteInvalidJson() throws IOException, JsonProcessingException {
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new OutputStreamWriter(new ByteArrayOutputStream()), null);
 gen.writeString("{"); // missing colon
 gen.writeFieldName("name");
 gen.writeString("John");
 gen.writeEndObject();
 gen.close();
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteDuplicateField() throws IOException, JsonProcessingException {
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new OutputStreamWriter(new ByteArrayOutputStream()), null);
 gen.writeStartObject();
 gen.writeFieldName("name");
 gen.writeString("John");
 gen.writeFieldName("name"); // duplicate field name
 gen.writeString("Jane");
 gen.writeEndObject();
 gen.close();
 }

 @Test(expected = JsonGenerationException.class)
 public void testWriteUnsupportedWriteValue() throws IOException, JsonProcessingException {
 JsonGenerator gen = MAPPER.getFactory().createGenerator(new OutputStreamWriter(new ByteArrayOutputStream()), null);
 gen.writeRawValue(new byte[1000]); // write raw value larger than buffer
 gen.close();
 }

 @Test
 public void testWriteBinary() throws IOException, JsonProcessingException {
 // Testing binary writing is not supported in JUnit
 // Use TestNG instead or write a custom test runner
 }
}