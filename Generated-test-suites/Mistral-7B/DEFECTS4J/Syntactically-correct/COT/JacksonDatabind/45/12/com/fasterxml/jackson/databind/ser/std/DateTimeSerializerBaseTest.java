package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 private static final Class<Date> DATE_CLASS = Date.class;

 @SuppressWarnings("unchecked")
 private static <T> DateTimeSerializerBase<T> createSerializer(Boolean useTimestamp, DateFormat customFormat) {
 return (DateTimeSerializerBase<T>) new DateTimeSerializerBase<T>(DATE_CLASS, useTimestamp, customFormat) {
 // Do not instantiate _timestamp method as it's abstract
 };
 }

 @Test
 public void testCreateContextual_nullProperty() {
 JsonFormat<Object> jsonFormat = null;
 ObjectMapper objectMapper = new ObjectMapper();
 JsonSerializer<Date> serializer = createSerializer(true, null).createContextual(objectMapper.getSerializerProvider(), null);
 assertNull(serializer);
 }

 @Test
 public void testCreateContextual_noFormatAnnotation() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 Date date = new Date();
 JsonSerializer<Date> serializer = createSerializer(false, null).createContextual(objectMapper.getSerializerProvider(), null);
 assertEquals(date.toString(), serializer.serialize(date, new JsonGenerator(new StringWriter()), objectMapper.getSerializerProvider()));
 }

 @Test
 public void testCreateContextual_numericFormat() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
 JsonSerializer<Date> serializer = createSerializer(false, dateFormat).createContextual(objectMapper.getSerializerProvider(), null);
 assertEquals("20230101", serializer.serialize(new Date(0L), new JsonGenerator(new StringWriter()), objectMapper.getSerializerProvider()).getValueAsString());
 }

 @Test
 public void testCreateContextual_stringFormat() throws JsonProcessingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormat<Date> jsonFormat = new JsonFormat.Value();
 jsonFormat.setShape(JsonFormat.Shape.STRING);
 jsonFormat.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 jsonFormat.setLocale(Locale.US);
 jsonFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 JsonSerializer<Date> serializer = createSerializer(false, null).createContextual(objectMapper.getSerializerProvider(), jsonFormat);
 Date date = new Date(System.currentTimeMillis());
 String expected = "2023-03-23T12:34:56.789Z";
 assertEquals(expected, serializer.serialize(date, new JsonGenerator(new StringWriter()), objectMapper.getSerializerProvider()).getValueAsString());
 }

 @Test
 public void testCreateContextual_stringFormatWithCustomTimeZone() throws JsonProcessingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormat<Date> jsonFormat = new JsonFormat.Value();
 jsonFormat.setShape(JsonFormat.Shape.STRING);
 jsonFormat.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 jsonFormat.setLocale(Locale.US);
 jsonFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
 JsonSerializer<Date> serializer = createSerializer(false, null).createContextual(objectMapper.getSerializerProvider(), jsonFormat);
 Date date = new Date(System.currentTimeMillis());
 String expected = "2023-03-23T16:34:56.789-04:00";
 assertEquals(expected, serializer.serialize(date, new JsonGenerator(new StringWriter()), objectMapper.getSerializerProvider()).getValueAsString());
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextual_invalidFormat() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormat<Date> jsonFormat = new JsonFormat.Value();
 jsonFormat.setShape(JsonFormat.Shape.STRING);
 jsonFormat.setPattern("xxxx-MM-dd'T'HH:mm:ss.SSS'Z'");
 JsonSerializer<Date> serializer = createSerializer(false, null).createContextual(objectMapper.getSerializerProvider(), jsonFormat);
 }

 @Test
 public void testEmpty() {
 assertFalse(createSerializer(false, null).isEmpty(null));
 assertFalse(createSerializer(false, null).isEmpty(new Date()));
 assertTrue(createSerializer(false, null).isEmpty(new Date(0L)));
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonNode schema = createSerializer(true, null).getSchema(objectMapper.getSerializerProvider(), DATE_CLASS);
 assertEquals("number", schema.getType().getClass().getSimpleName());
 schema = createSerializer(false, null).getSchema(objectMapper.getSerializerProvider(), DATE_CLASS);
 assertEquals("string", schema.getType().getClass().getSimpleName());
 }

}