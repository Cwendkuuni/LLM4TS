package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class DateTimeSerializerBaseTest {

 private ObjectMapper mapper;
 private DateTimeSerializerBase<Date> dateTimeSerializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 dateTimeSerializer = mapper.findValueSerializer(Date.class);
 }

 @Test
 public void testDefaultConstruction() {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, null, null);
 assertEquals(DateTimeSerializerBase.class, serializer.getClass());
 assertNull(serializer._customFormat);
 assertNull(serializer._reusedCustomFormat);
 }

 @Test
 public void testCustomFormatConstruction() {
 DateFormat customFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a zzz", Locale.US);
 TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
 customFormat.setTimeZone(timeZone);
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, null, customFormat);
 assertEquals(DateTimeSerializerBase.class, serializer.getClass());
 assertEquals(customFormat, serializer._customFormat);
 assertNull(serializer._reusedCustomFormat);
 }

 @Test
 public void testTimestampConstruction() {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 assertEquals(DateTimeSerializerBase.class, serializer.getClass());
 assertNull(serializer._customFormat);
 assertNull(serializer._reusedCustomFormat);
 }

 @Test
 public void testSerializationAsDate() throws Exception {
 Date date = new Date();
 JsonGenerator gen = mapper.getPolymorphicSerializer(Date.class).getSerializerProvider().getJsonGenerator();
 dateTimeSerializer.serialize(date, gen, null);
 assertEquals("\"2023-03-29T00:00:00.000Z\"", gen.getOutputBuffer().toString());
 }

 @Test
 public void testSerializationAsString() throws Exception {
 DateFormat customFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a zzz", Locale.US);
 TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
 customFormat.setTimeZone(timeZone);
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 Date date = new Date();
 JsonGenerator gen = mapper.getPolymorphicSerializer(Date.class).getSerializerProvider().getJsonGenerator();
 serializer.serialize(date, gen, null);
 assertEquals("\"25/03/2023 10:30 AM EDT\"", gen.getOutputBuffer().toString());
 }

 @Test
 public void testSerializationWithCustomFormat() throws Exception {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 serializer = serializer.withFormat(false, new SimpleDateFormat("dd/MM/yyyy hh:mm a zzz", Locale.US));
 Date date = new Date();
 JsonGenerator gen = mapper.getPolymorphicSerializer(Date.class).getSerializerProvider().getJsonGenerator();
 serializer.serialize(date, gen, null);
 assertEquals("\"25/03/2023 10:30 AM EDT\"", gen.getOutputBuffer().toString());
 }

 @Test
 public void testSerializationWithTimestamp() throws Exception {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 serializer = serializer.withFormat(true, null);
 Date date = new Date();
 JsonGenerator gen = mapper.getPolymorphicSerializer(Date.class).getSerializerProvider().getJsonGenerator();
 serializer.serialize(date, gen, null);
 assertEquals("2023-03-29T00:00:00.000Z", gen.getOutputBuffer().toString());
 }

 @Test
 public void testSerializationWithCustomFormatAndTimestamp() throws Exception {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 serializer = serializer.withFormat(false, new SimpleDateFormat("dd/MM/yyyy hh:mm a zzz", Locale.US));
 serializer = serializer.withFormat(false, null);
 Date date = new Date();
 JsonGenerator gen = mapper.getPolymorphicSerializer(Date.class).getSerializerProvider().getJsonGenerator();
 serializer.serialize(date, gen, null);
 assertEquals("25/03/2023 10:30 AM EDT", gen.getOutputBuffer().toString());
 }

 @Test
 public void testDeserializationAsDate() throws Exception {
 String dateString = "\"2023-03-29T00:00:00.000Z\"";
 JsonParser parser = mapper.getFactory().createParser(dateString);
 Date deserializedDate = mapper.readValue(parser, Date.class);
 assertEquals(new Date(), deserializedDate);
 }

 @Test
 public void testDeserializationAsString() throws Exception {
 String dateString = "\"25/03/2023 10:30 AM EDT\"";
 JsonParser parser = mapper.getFactory().createParser(dateString);
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 Date deserializedDate = mapper.readValue(parser, Date.class, serializer);
 assertEquals(new Date(), deserializedDate);
 }

 @Test
 public void testDeserializationWithCustomFormat() throws Exception {
 String dateString = "\"25/03/2023 10:30 AM EDT\"";
 JsonParser parser = mapper.getFactory().createParser(dateString);
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd/MM/yyyy hh:mm a zzz", Locale.US));
 Date deserializedDate = mapper.readValue(parser, Date.class, serializer);
 assertEquals(new Date(), deserializedDate);
 }

 @Test
 public void testDeserializationWithTimestamp() throws Exception {
 String dateString = "2023-03-29T00:00:00.000Z";
 JsonParser parser = mapper.getFactory().createParser(dateString);
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 Date deserializedDate = mapper.readValue(parser, Date.class, serializer);
 assertEquals(new Date(), deserializedDate);
 }

 @Test
 public void testDeserializationWithCustomFormatAndTimestamp() throws Exception {
 String dateString = "25/03/2023 10:30 AM EDT";
 JsonParser parser = mapper.getFactory().createParser(dateString);
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd/MM/yyyy hh:mm a zzz", Locale.US));
 serializer = serializer.withFormat(false, null);
 Date deserializedDate = mapper.readValue(parser, Date.class, serializer);
 assertEquals(new Date(), deserializedDate);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws Exception {
 JsonFormatVisitorWrapper visitor = mapper.getTypeFactory().constructValueType(Map.class, JsonValueFormat.class);
 visitor.expectStringFormat(null, JsonValueFormat.DATE_TIME);
 dateTimeSerializer.acceptJsonFormatVisitor(visitor, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAcceptJsonFormatVisitorNullProvider() throws Exception {
 JsonFormatVisitorWrapper visitor = mapper.getTypeFactory().constructValueType(Map.class, JsonValueFormat.class);
 dateTimeSerializer.acceptJsonFormatVisitor(visitor, null);
 }

 @Test
 public void testGetSchema() throws Exception {
 JsonNode schema = dateTimeSerializer.getSchema(mapper.getSerializerProvider(), TypeFactory.defaultInstance().constructType(Date.class));
 assertEquals("string", schema.toString());
 }
}