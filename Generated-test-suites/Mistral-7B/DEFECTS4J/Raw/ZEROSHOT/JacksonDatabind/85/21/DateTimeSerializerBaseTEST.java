package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 private ObjectMapper objectMapper;
 private SerializerProvider serializers;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 serializers = objectMapper.getSerializationConfig().getDefaultSerializerProvider();
 }

 @Test
 public void testDateTimeSerializerBaseWithDefaultSettings() throws JsonProcessingException, IOException {
 DateTimeSerializerBase<Long> serializer = new StdDateSerializer();
 Long date = System.currentTimeMillis();
 String json = objectMapper.writeValueAsString(date);
 Long deserializedDate = objectMapper.readValue(json, Long.class);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomFormat() throws JsonProcessingException, IOException, ParseException {
 DateTimeSerializerBase<String> serializer = new StdDateSerializer() {
 @Override
 public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
 return new StdDateSerializer<String>() {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
 throws IOException {
 try {
 gen.writeString(dateFormat.format(new Date(Long.parseLong(value))));
 } catch (NumberFormatException e) {
 throw new IOException("Failed to parse long value: " + value);
 }
 }
 @Override
 protected long _timestamp(String value) {
 return Long.parseLong(value);
 }
 };
 }
 };
 String dateString = "25-12-2022";
 Long date = 167155200000L; // corresponding UNIX timestamp for "25-12-2022"
 String json = objectMapper.writeValueAsString(dateString);
 Long deserializedDate = objectMapper.readValue(json, Long.class);
 assertEquals(date, deserializedDate);

 String jsonWithCustomFormat = objectMapper.writeValueAsString(dateString);
 String deserializedDateWithCustomFormat = objectMapper.readValue(jsonWithCustomFormat, String.class);
 assertEquals(dateString, deserializedDateWithCustomFormat);
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomFormatAndLocale() throws JsonProcessingException, IOException, ParseException {
 DateTimeSerializerBase<String> serializer = new StdDateSerializer() {
 @Override
 public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH);
 return new StdDateSerializer<String>() {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
 throws IOException {
 try {
 gen.writeString(dateFormat.format(new Date(Long.parseLong(value))));
 } catch (NumberFormatException e) {
 throw new IOException("Failed to parse long value: " + value);
 }
 }
 @Override
 protected long _timestamp(String value) {
 return Long.parseLong(value);
 }
 };
 }
 };
 String dateString = "25-12-2022";
 Long date = 167155200000L; // corresponding UNIX timestamp for "25-12-2022"
 String json = objectMapper.writeValueAsString(dateString);
 String deserializedDate = objectMapper.readValue(json, String.class);
 assertEquals(dateString, deserializedDate);

 String jsonWithCustomFormat = objectMapper.writeValueAsString(dateString);
 String deserializedDateWithCustomFormat = objectMapper.readValue(jsonWithCustomFormat, String.class);
 assertEquals(dateString, deserializedDateWithCustomFormat);
}

 @Test
 public void testDateTimeSerializerBaseWithCustomFormatAndTimeZone() throws JsonProcessingException, IOException, ParseException {
 DateTimeSerializerBase<String> serializer = new StdDateSerializer() {
 @Override
 public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss z", Locale.ENGLISH);
 dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 return new StdDateSerializer<String>() {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
 throws IOException {
 try {
 Date date = dateFormat.parse(value);
 gen.writeString(dateFormat.format(date));
 } catch (ParseException e) {
 throw new IOException("Failed to parse string value: " + value);
 }
 }
 @Override
 protected long _timestamp(String value) {
 try {
 return dateFormat.parse(value).getTime();
 } catch (ParseException e) {
 throw new RuntimeException("Failed to parse date from string: " + value);
 }
 }
 };
 }
 };
 String dateString = "25-12-2022 10:30:00 UTC";
 Long date = 167155200000L; // corresponding UNIX timestamp for "25-12-2022 10:30:00 UTC"
 String json = objectMapper.writeValueAsString(dateString);
 String deserializedDate = objectMapper.readValue(json, String.class);
 assertEquals(dateString, deserializedDate);

 String jsonWithCustomFormat = objectMapper.writeValueAsString(dateString);
 String deserializedDateWithCustomFormat = objectMapper.readValue(jsonWithCustomFormat, String.class);
 assertEquals(dateString, deserializedDateWithCustomFormat);
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomFormatAndCustomSerialization() throws JsonProcessingException, IOException {
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z")
 static class TestDate {
 }
 DateTimeSerializerBase<TestDate> serializer = new StdDateSerializer<TestDate>() {
 @Override
 public DateTimeSerializerBase<TestDate> withFormat(Boolean timestamp, DateFormat customFormat) {
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
 return new StdDateSerializer<TestDate>() {
 @Override
 public void serialize(TestDate value, JsonGenerator gen, SerializerProvider serializers)
 throws IOException {
 try {
 Date date = dateFormat.parse(value.toString());
 gen.writeString(dateFormat.format(date));
 } catch (ParseException e) {
 throw new IOException("Failed to parse TestDate: " + value);
 }
 }
 @Override
 protected long _timestamp(TestDate value) {
 return dateFormat.parse(value.toString()).getTime();
 }
 };
 }
 };
 TestDate date = new TestDate();
 date.setTime(System.currentTimeMillis());
 String json = objectMapper.writeValueAsString(date);
 TestDate deserializedDate = objectMapper.readValue(json, TestDate.class);
 assertEquals(date.toString(), deserializedDate.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testDateTimeSerializerBaseWithNullProvider() throws JsonProcessingException, IOException {
 DateTimeSerializerBase serializer = new StdDateSerializer();
 serializer.createContextual(null, null);
 }
}