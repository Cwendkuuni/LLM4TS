package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<LocalDateTime> serializer;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 serializer = new BaseDateTimeSerializerBase<LocalDateTime>() {
 @Override
 protected long _timestamp(LocalDateTime value) {
 return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
 }
 };
 mapper = new ObjectMapper();
 mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
 }

 @Test
 public void testEmpty() throws JsonProcessingException {
 assertFalse(serializer.isEmpty(mapper.getSerializerProvider(), null));
 }

 @Test
 public void testSerializationWithDefaultFormat() throws JsonProcessingException {
 LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 34, 56);
 String json = mapper.writeValueAsString(dateTime);
 assertTrue(json.startsWith("\"2022-01-01T12:34:56\""));
 }

 @Test
 public void testSerializationWithNoCustomFormat() throws JsonProcessingException {
 SerializerProvider provider = mapper.getSerializerProvider();
 DateTimeSerializerBase<Date> serializerDate = new BaseDateTimeSerializerBase<Date>() {
 @Override
 protected long _timestamp(Date value) {
 return value.getTime();
 }
 };
 serializerDate = serializerDate.withFormat(Boolean.FALSE, null);

 Date date = new Date(1633059200000L); // 1 Jan 2022, midnight
 String jsonDate = mapper.writerWithView(serializerDate.getClass()).writeValueAsString(date);
 assertEquals("\"1633059200000\"", jsonDate);
 }

 @Test
 public void testSerializationWithCustomFormat() throws JsonProcessingException {
 SerializerProvider provider = mapper.getSerializerProvider();
 DateTimeSerializerBase<Date> serializerDate = new BaseDateTimeSerializerBase<Date>() {
 @Override
 protected long _timestamp(Date value) {
 return value.getTime();
 }
 };
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
 serializerDate = serializerDate.withFormat(Boolean.FALSE, customFormat);

 Date date = new Date(1633059200000L); // 1 Jan 2022, midnight
 String jsonDate = mapper.writerWithView(serializerDate.getClass()).writeValueAsString(date);
 assertEquals("\"2022-01-01T00:00:00\"", jsonDate);
 }

 @Test
 public void testSerializationWithLocale() throws JsonProcessingException {
 SerializerProvider provider = mapper.getSerializerProvider();
 DateTimeSerializerBase<Date> serializerDate = new BaseDateTimeSerializerBase<Date>() {
 @Override
 protected long _timestamp(Date value) {
 return value.getTime();
 }
 };
 DateFormat customFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
 customFormat.setLocale(Locale.GERMAN);
 serializerDate = serializerDate.withFormat(Boolean.FALSE, customFormat);

 Date date = new Date(1633059200000L); // 1 Jan 2022, midnight
 String jsonDate = mapper.writerWithView(serializerDate.getClass()).writeValueAsString(date);
 assertEquals("\"01.01.2022 00:00:00\"", jsonDate);
 }

 @Test
 public void testSerializationWithTimeZone() throws JsonProcessingException {
 SerializerProvider provider = mapper.getSerializerProvider();
 DateTimeSerializerBase<Date> serializerDate = new BaseDateTimeSerializerBase<Date>() {
 @Override
 protected long _timestamp(Date value) {
 return value.getTime();
 }
 };
 DateFormat customFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
 customFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
 serializerDate = serializerDate.withFormat(Boolean.FALSE, customFormat);

 Date date = new Date(1633059200000L); // 1 Jan 2022, midnight
 String jsonDate = mapper.writerWithView(serializerDate.getClass()).writeValueAsString(date);
 assertEquals("\"01.01.2022 03:00:00\"", jsonDate); // Moscow time is GMT+3
 }

 @Test
 public void testDeserializationWithDefaultFormat() throws JsonProcessingException, ParseException {
 String json = "\"2022-01-01T12:34:56\"";
 LocalDateTime dateTime = mapper.readValue(json, LocalDateTime.class);
 assertEquals(LocalDateTime.of(2022, 1, 1, 12, 34, 56), dateTime);
 }

 @Test
 public void testDeserializationWithNoCustomFormat() throws JsonProcessingException, ParseException {
 String json = "\"1633059200000\"";
 LocalDateTime dateTime = mapper.readValue(json, LocalDateTime.class);
 Instant instant = Instant.ofEpochMilli(1633059200000L);
 assertEquals(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()), dateTime);
 }

 @Test
 public void testDeserializationWithCustomFormat() throws JsonProcessingException, ParseException {
 String json = "\"2022-01-01T00:00:00\"";
 DateTimeSerializerBase<Date> deserializerDate = new BaseDateTimeSerializerBase<Date>() {
 @Override
 protected Date _parseDate(String text, Locale locale, DateFormat format) throws ParseException {
 return format.parse(text);
 }
 };
 deserializerDate = deserializerDate.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
 LocalDateTime dateTime = mapper.readerWithView(deserializerDate.getClass()).readValue(json);
 assertEquals(LocalDateTime.of(2022, 1, 1, 0, 0, 0), dateTime);
 }

 @Test
 public void testDeserializationWithLocale() throws JsonProcessingException, ParseException {
 String json = "\"01.01.2022 00:00:00\"";
 DateTimeSerializerBase<Date> deserializerDate = new BaseDateTimeSerializerBase<Date>() {
 @Override
 protected Date _parseDate(String text, Locale locale, DateFormat format) throws ParseException {
 return format.parse(text);
 }
 };
 AnnotatedClass ac = mapper.getTypeFactory().constructType(DateTime.class);
 AnnotatedField af = ac.findAnnotation(JsonFormat.class).get(0);
 LocalDateTime dateTime = mapper.readerWithView(deserializerDate.getClass())
 .withResolvedAnnotations(af)
 .readValue(json);
 assertEquals(LocalDateTime.of(2022, 1, 1, 0, 0, 0), dateTime);
 }

 @Test
 public void testDeserializationWithTimeZone() throws JsonProcessingException, ParseException {
 String json = "\"01.01.2022 03:00:00\"";
 DateTimeSerializerBase<Date> deserializerDate = new BaseDateTimeSerializerBase<Date>() {
 @Override
 protected Date _parseDate(String text, Locale locale, DateFormat format) throws ParseException {
 return format.parse(text);
 }
 };
 AnnotatedClass ac = mapper.getTypeFactory().constructType(DateTime.class);
 AnnotatedField af = ac.findAnnotation(JsonFormat.class).get(0);
 DateFormat customFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
 customFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
 deserializerDate = deserializerDate.withFormat(Boolean.FALSE, customFormat);
 LocalDateTime dateTime = mapper.readerWithView(deserializerDate.getClass())
 .withResolvedAnnotations(af)
 .readValue(json);
 assertEquals(LocalDateTime.of(2022, 1, 1, 3, 0, 0), dateTime); // Moscow time is GMT+3
 }
}

class BaseDateTimeSerializerBase<T> extends DateTimeSerializerBase<T> {
 // Empty constructor
}