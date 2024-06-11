package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> dateSerializer;
 private DateTimeSerializerBase<ZonedDateTime> zonedDateTimeSerializer;

 @Before
 public void setup() {
 dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 zonedDateTimeSerializer = new DateTimeSerializerBase<ZonedDateTime>(ZonedDateTime.class, false, null);
 }

 @Test
 public void testSerialization_Date() throws IOException, JsonProcessingException {
 Date date = new Date(1571131822000L); // Mon Sep 23 14:53:42 EDT 2019
 JsonSerializer<Date> serializer = dateSerializer;

 // Test serialization with default settings
 SerializerProvider serializers = new SerializerProvider();
 JsonGenerator gen = serializers.getFactory().createGenerator(new StringBuilder(), null);
 serializer.serialize(date, gen, serializers);
 gen.flush();
 assertEquals("1571131822000", gen.getOutputBuffer().toString());

 // Test serialization with custom format
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
 serializer = dateSerializer.withFormat(false, customFormat);
 gen.setDateFormat(customFormat);
 serializer.serialize(date, gen, serializers);
 gen.flush();
 assertEquals("1571131822000", gen.getOutputBuffer().toString());

 // Test serialization as timestamp
 serializer = dateSerializer.withFormat(true, null);
 serializer.serialize(date, gen, serializers);
 gen.flush();
 assertEquals("1571131822000", gen.getOutputBuffer().toString());
 }

 @Test
 public void testSerialization_ZonedDateTime() throws IOException, JsonProcessingException {
 ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(1571131822000L), ZoneId.of("America/New_York"));
 JsonSerializer<ZonedDateTime> serializer = zonedDateTimeSerializer;

 // Test serialization with default settings
 SerializerProvider serializers = new SerializerProvider();
 JsonGenerator gen = serializers.getFactory().createGenerator(new StringBuilder(), null);
 serializer.serialize(zonedDateTime, gen, serializers);
 gen.flush();
 assertEquals("2019-09-23T14:53:42.000-04:00", gen.getOutputBuffer().toString());

 // Test serialization with custom format
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 serializer = zonedDateTimeSerializer.withFormat(false, customFormat);
 gen.setDateFormat(customFormat);
 serializer.serialize(zonedDateTime, gen, serializers);
 gen.flush();
 assertEquals("2019-09-23T14:53:42.000-04:00", gen.getOutputBuffer().toString());

 // Test serialization as timestamp
 serializer = zonedDateTimeSerializer.withFormat(true, null);
 serializer.serialize(zonedDateTime, gen, serializers);
 gen.flush();
 assertEquals("1571131822000", gen.getOutputBuffer().toString());
 }

 @Test
 public void testDeserialization_Date() throws IOException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String jsonDate = "1571131822000";
 Date date = mapper.readValue(jsonDate, Date.class);
 assertNotNull(date);
 assertEquals(1571131822000L, date.getTime());

 jsonDate = "\"1571131822000\"";
 date = mapper.readValue(jsonDate, Date.class);
 assertNotNull(date);
 assertEquals(1571131822000L, date.getTime());

 // Test deserialization with custom format
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
 mapper.setDateFormat(customFormat);
 date = mapper.readValue(jsonDate, Date.class);
 assertNotNull(date);
 assertEquals(1571131822000L, date.getTime());
 }

 @Test
 public void testDeserialization_ZonedDateTime() throws IOException, JsonProcessingException, ParseException {
 ObjectMapper mapper = new ObjectMapper();
 String jsonZonedDateTime = "2019-09-23T14:53:42.000-04:00";
 ZonedDateTime zonedDateTime = mapper.readValue(jsonZonedDateTime, ZonedDateTime.class);
 assertNotNull(zonedDateTime);
 assertEquals(Instant.ofEpochMilli(1571131822000L), zonedDateTime.toInstant());
 assertEquals("America/New_York", zonedDateTime.getZone().getId());

 jsonZonedDateTime = "\"2019-09-23T14:53:42.000-04:00\"";
 zonedDateTime = mapper.readValue(jsonZonedDateTime, ZonedDateTime.class);
 assertNotNull(zonedDateTime);
 assertEquals(Instant.ofEpochMilli(1571131822000L), zonedDateTime.toInstant());
 assertEquals("America/New_York", zonedDateTime.getZone().getId());

 // Test deserialization with custom format
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 mapper.setDateFormat(customFormat);
 zonedDateTime = mapper.readValue(jsonZonedDateTime, ZonedDateTime.class);
 assertNotNull(zonedDateTime);
 assertEquals(Instant.ofEpochMilli(1571131822000L), zonedDateTime.toInstant());
 assertEquals("America/New_York", zonedDateTime.getZone().getId());
 }

 @Test
 public void testSerializationWithJsonFormatAnnotation() throws IOException, JsonProcessingException {
 @JsonSerialize(using = DateTimeSerializerBase.class)
 class TestDate {
 private Date date;

 public Date getDate() {
 return date;
 }

 public void setDate(Date date) {
 this.date = date;
 }
 }

 TestDate testDate = new TestDate();
 testDate.setDate(new Date(1571131822000L));

 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<TestDate> serializer = mapper.findValueSerializer(TestDate.class, Date.class);
 SerializerProvider serializers = new SerializerProvider();
 JsonGenerator gen = serializers.getFactory().createGenerator(new StringBuilder(), null);
 serializer.serialize(testDate, gen, serializers);
 gen.flush();
 assertEquals("1571131822000", gen.getOutputBuffer().toString());
 }

 @Test
 public void testDeserializationWithJsonFormatAnnotation() throws IOException, JsonProcessingException {
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmssSSS")
 class TestDate {
 @JsonSerialize
 private Date date;

 public Date getDate() {
 return date;
 }

 public void setDate(Date date) {
 this.date = date;
 }
 }

 ObjectMapper mapper = new ObjectMapper();
 String jsonDate = "1571131822000";
 TestDate testDate = mapper.readValue(jsonDate, TestDate.class);
 assertNotNull(testDate);
 assertEquals(1571131822000L, testDate.getDate().getTime());
 }
}