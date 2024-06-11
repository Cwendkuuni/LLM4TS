package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.StdDateFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDateTimeSerializerBaseSerialization() throws IOException, JsonProcessingException {
 Date date = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30); // 30 days ago
 DateTimeSerializerBase<Date> dateSerializer = mapper.findValueSerializer(Date.class);
 JsonGenerator gen = mapper.getPolymorphicSerializer(Date.class).createContextual(mapper.getSerializerProvider(), null);

 // Test serialization with default settings
 dateSerializer.serialize(date, gen, mapper.getSerializerProvider());
 gen.flush();
 String jsonString = gen.getOutputStream().toString();
 assertEquals("\"2022-11-01T00:00:30.000Z\"", jsonString);

 // Test serialization with custom format
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
 DateTimeSerializerBase<Date> dateSerializerWithFormat = dateSerializer.withFormat(false, customFormat);
 gen = mapper.getPolymorphicSerializer(Date.class).createContextual(mapper.getSerializerProvider(), null);
 dateSerializerWithFormat.serialize(date, gen, mapper.getSerializerProvider());
 gen.flush();
 jsonString = gen.getOutputStream().toString();
 assertEquals("\"2022-11-01T00:00:30.000-05:00\"", jsonString); // Assuming default timezone is EST-5
 }

 @Test
 public void testDateTimeSerializerBaseDeserialization() throws IOException, JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("\"2022-11-01T00:00:30.000Z\"");
 Date date = mapper.readValue(parser, Date.class);
 assertEquals(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30), date);

 JsonParser parserWithCustomFormat = mapper.getFactory().createParser("\"2022-11-01T00:00:30.000-05:00\"");
 DateTimeSerializerBase<Date> dateSerializer = mapper.findValueDeserializer(Date.class, null);
 date = mapper.readValue(parserWithCustomFormat, Date.class);
 assertEquals(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30 - 18000000), date); // EST timezone offset in milliseconds
 }

 @Test
 public void testDateTimeSerializerBaseSerializationWithUseTimestamp() throws IOException, JsonProcessingException {
 Date date = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30); // 30 days ago
 DateTimeSerializerBase<Date> dateSerializer = mapper.findValueSerializer(Date.class);
 JsonGenerator gen = mapper.getPolymorphicSerializer(Date.class).createContextual(mapper.getSerializerProvider(), null);

 // Test serialization with useTimestamp = true
 DateTimeSerializerBase<Date> dateSerializerWithTimestamp = dateSerializer.withFormat(true, null);
 dateSerializerWithTimestamp.serialize(date, gen, mapper.getSerializerProvider());
 gen.flush();
 String jsonString = gen.getOutputStream().toString();
 assertEquals("1667996830000", jsonString);
 }

 @Test(expected = JsonMappingException.class)
 public void testDateTimeSerializerBaseSerializationWithInvalidCustomFormat() throws IOException, JsonProcessingException {
 Date date = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30); // 30 days ago
 DateTimeSerializerBase<Date> dateSerializer = mapper.findValueSerializer(Date.class);
 JsonGenerator gen = mapper.getPolymorphicSerializer(Date.class).createContextual(mapper.getSerializerProvider(), null);

 // Test serialization with invalid custom format
 DateTimeSerializerBase<Date> dateSerializerWithFormat = dateSerializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd")); // Invalid format: missing 'T' and 'Z'
 dateSerializerWithFormat.serialize(date, gen, mapper.getSerializerProvider());
 }

 @Test
 public void testDateTimeSerializerBaseDeserializationWithUseTimestamp() throws IOException, JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("1667996830000");
 Date date = mapper.readValue(parser, Date.class);
 assertEquals(new Date(1667996830000L), date);
 }

 @Test(expected = JsonMappingException.class)
 public void testDateTimeSerializerBaseDeserializationWithInvalidCustomFormat() throws IOException, JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("\"2022-11-01\"");
 DateTimeSerializerBase<Date> dateSerializer = mapper.findValueDeserializer(Date.class, null);
 }

 @Test
 public void testDateTimeSerializerBaseDeserializationWithEmptyValue() throws IOException, JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("null");
 DateTimeSerializerBase<Date> dateSerializer = mapper.findValueDeserializer(Date.class, null);
 assertEquals(null, dateSerializer.deserialize(parser, Date.class, null));
 }
}