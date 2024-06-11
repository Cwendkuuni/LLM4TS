package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer.ValueAsFormat;
import com.fasterxml.jackson.databind.JsonValueFormat;
import com.fasterxml.jackson.databind.SerializerProvider.Feature;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import com.example.DateTimeSerializerBase.DateTimeSerializerBase;
import com.example.DateTimeSerializerBase.DateTimeSerializerBaseTest;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> serializer;
 private SerializerProvider serializers;
 private JsonParser parser;
 private JsonGenerator generator;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 serializers = mock(SerializerProvider.class);
 parser = mock(JsonParser.class);
 generator = mock(JsonGenerator.class);
 objectMapper = new ObjectMapper();

 when(serializers.getDefaultDateFormat()).thenReturn(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
 when(serializers.getConfig()).thenReturn(objectMapper.getConfig());
 when(serializers.isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
 }

 @Test
 public void testConstructor() {
 Class<Date> type = new TypeToken<Date>() {}.getType();
 Boolean useTimestamp = false;
 DateFormat customFormat = null;
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(type, useTimestamp, customFormat);
 assertEquals(Date.class, dateTimeSerializerBase.handledType());
 assertEquals(false, dateTimeSerializerBase._useTimestamp.booleanValue());
 assertNull(dateTimeSerializerBase._customFormat);
 }

 @Test
 public void testWithFormatNumericTimestamp() {
 Class<Date> type = new TypeToken<Date>() {}.getType();
 Boolean useTimestamp = true;
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(type, useTimestamp, null);
 DateTimeSerializerBase<Date> withFormat = dateTimeSerializerBase.withFormat(useTimestamp, null);
 assertEquals(dateTimeSerializerBase.handledType(), withFormat.handledType());
 assertEquals(true, withFormat._useTimestamp.booleanValue());
 assertNull(withFormat._customFormat);
 }

 @Test
 public void testWithFormatCustomFormat() throws IOException {
 Class<Date> type = new TypeToken<Date>() {}.getType();
 Boolean useTimestamp = false;
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(type, useTimestamp, customFormat);
 DateTimeSerializerBase<Date> withFormat = dateTimeSerializerBase.withFormat(useTimestamp, customFormat);
 assertEquals(dateTimeSerializerBase.handledType(), withFormat.handledType());
 assertEquals(false, withFormat._useTimestamp.booleanValue());
 assertEquals(customFormat, withFormat._customFormat);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 when(visitor.getProvider()).thenReturn(serializers);
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(new TypeToken<Date>() {}.getType(), false, null);
 dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, null);
 verify(visitor).visitIntFormat(any(JavaType.class), eq(JsonParser.NumberType.LONG), eq(JsonValueFormat.UTC_MILLISEC));
 }

 @Test
 public void testSerialize() throws IOException, ParseException {
 Date date = new Date();
 when(parser.getValueAsNumber()).thenReturn(0L);
 when(parser.getValueAsString()).thenReturn("0");
 when(parser.getText()).thenReturn("\"0\"");
 when(parser.isExpectedStartArrayValue()).thenReturn(false);
 when(parser.isExpectedStartObjectValue()).thenReturn(false);

 serializer = new DateTimeSerializerBase<Date>(new TypeToken<Date>() {}.getType(), false, null);

 byte[] buffer = new byte[1024];
 JsonNode result = null;
 try (JsonGenerator jsonGenerator = generator.createGenerator(buffer)) {
 result = serializer.serialize(date, jsonGenerator, serializers);
 }
 assertEquals("\"2023-03-23T00:00:00.000Z\"", result.asText());

 when(parser.getValueAsNumber()).thenReturn(1516131200000L);
 result = serializer.serialize(date, parser, serializers);
 assertEquals(1516131200000L, result.longValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeNullSerializerProvider() {
 serializer = new DateTimeSerializerBase<Date>(new TypeToken<Date>() {}.getType(), false, null);
 serializer.serialize(null, null, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testIsNullValue() throws JsonMappingException {
 serializer = new DateTimeSerializerBase<Date>(new TypeToken<Date>() {}.getType(), false, null);
 try {
 serializer.serialize(null, generator, serializers);
 } catch (IOException e) {
 throw new JsonMappingException(e.getMessage());
 }
 }

 @Test
 public void testSerializeWithCustomFormat() throws IOException, ParseException {
 DateFormat customFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(new TypeToken<Date>() {}.getType(), false, customFormat);
 byte[] buffer = new byte[1024];
 JsonNode result = null;
 try (JsonGenerator jsonGenerator = generator.createGenerator(buffer)) {
 result = dateTimeSerializerBase.serialize(new Date(), jsonGenerator, serializers);
 }
 assertEquals("\"23/03/2023 12:00 PM\"", result.asText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeWithNullCustomFormat() {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(new TypeToken<Date>() {}.getType(), false, null);
 try {
 dateTimeSerializerBase.serialize(new Date(), generator, serializers);
 } catch (IOException e) {
 throw new IllegalArgumentException(e.getMessage());
 }
 }
}