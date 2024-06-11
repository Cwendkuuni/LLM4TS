package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 // Test case for constructor with useTimestamp = true and no customFormat
 @Test
 public void testDTSerializerBaseUseTimestampTrue() throws JsonProcessingException, IOException {
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.class, true, null);
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
 Long date = Instant.now().toEpochMilli();
 TextNode result = mapper.writeValueAsTree(date, serializer);
 assertEquals("1633932458000", result.asText());
 }

 // Test case for constructor with useTimestamp = false and customFormat
 @Test
 public void testDTSerializerBaseUseTimestampFalseCustomFormat() throws JsonProcessingException, IOException {
 DateTimeFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
 DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, dateFormat);
 ObjectMapper mapper = new ObjectMapper();
 Date date = new Date();
 mapper.setDateFormat(dateFormat);
 String input = "15-03-2022 16:30:00 PST";
 Date parsedDate = mapper.readValue(input, Date.class);
 TextNode result = mapper.writeValueAsTree(parsedDate, serializer);
 assertEquals("\"15-Mar-2022 16:30:00 PST\"", result.asText());
 }

 // Test case for isEmpty method
 @Test
 public void testIsEmpty() throws JsonProcessingException, IOException {
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.class, false, null);
 ObjectMapper mapper = new ObjectMapper();
 Long date = 0L;
 assertFalse(serializer.isEmpty(mapper.getSerializationConfig(), date));
 assertFalse(serializer.isEmpty(mapper, date));
 }

 // Test case for getSchema method
 @Test
 public void testGetSchema() throws JsonProcessingException {
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.class, false, null);
 JsonNode schema = serializer.getSchema(new SerializerProvider(), null);
 assertEquals("number", schema.getClass().getSimpleName());
 }

 // Test case for createContextual method with null property
 @Test
 public void testCreateContextualNullProperty() throws JsonMappingException {
 DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
 JsonFormat.Value format = null;
 JsonSerializer<?> result = serializer.createContextual(new SerializerProvider(), null);
 assertEquals(serializer, result);
 }

 // Test case for createContextual method with format overrides
 @Test
 public void testCreateContextualFormatOverrides() throws JsonMappingException {
 DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setPattern("dd-MM-yyyy HH:mm:ss z");
 format.setLocale(Locale.ENGLISH);
 format.setTimeZone(TimeZone.getTimeZone("UTC"));
 JsonSerializer<?> result = serializer.createContextual(new SerializerProvider(), null);
 assertFalse(result.equals(serializer));
 }

 // Test case for serialize method
 @Test
 public void testSerialize() throws JsonProcessingException, IOException {
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.class, false, null);
 ObjectMapper mapper = new ObjectMapper();
 Long date = Instant.now().toEpochMilli();
 TextNode result = mapper.writeValueAsTree(date, serializer);
 assertEquals("1633932458000", result.asText());
 }

 // Test case for _asTimestamp method
 @Test
 public void testAsTimestamp() throws JsonProcessingException, IOException {
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.class, false, null);
 SerializerProvider provider = new SerializerProvider();
 assertFalse(serializer._asTimestamp(provider));
 }

 // Test case for _acceptJsonFormatVisitor method with asNumber = true
 @Test
 public void testAcceptJsonFormatVisitorAsNumber() throws JsonProcessingException, IOException {
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.class, false, null);
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonFormatVisitorWrapper visitor = mapper.schemaProvider().findSchemaVisitor(JsonFormatVisitor.Std.class);
 serializer._acceptJsonFormatVisitor(visitor, null, true);
 assertEquals("number", visitor.getCurrentValue().getSchema().getSchemaName());
 }

 // Test case for _acceptJsonFormatVisitor method with asString = false
 @Test
 public void testAcceptJsonFormatVisitorAsString() throws JsonProcessingException, IOException {
 DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
 ObjectMapper mapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = mapper.schemaProvider().findSchemaVisitor(JsonFormatVisitor.Std.class);
 serializer._acceptJsonFormatVisitor(visitor, null, false);
 assertEquals("string", visitor.getCurrentValue().getSchema().getSchemaName());
 }

 // Test case for _serializeAsString method
 @Test
 public void testSerializeAsString() throws JsonProcessingException, IOException, ParseException {
 DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
 ObjectMapper mapper = new ObjectMapper();
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
 mapper.setDateFormat(dateFormat);
 Date date = new Date();
 TextNode result = mapper.writeValueAsTree(serializer._serializeAsString(date, mapper.getJsonFactory(), mapper));
 assertEquals("\"31-Dec-1969 01:14:06 GMT\"", result.asText());
 }
}