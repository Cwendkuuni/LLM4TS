package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

 private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC();

 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
 static class TestDate {
 }

 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
 static class TestDateWithTimeZone {
 }

 @JsonFormat(shape = JsonFormat.Shape.NUMBER)
 static class TestDateWithoutPattern {
 }

 @JsonFormat(shape = JsonFormat.Shape.NUMBER, useNanos = true)
 static class TestDateWithNanos {
 }

 @JsonFormat(shape = JsonFormat.Shape.STRING, localDateFormat = "dd-MM-yyyy", timeZone = "UTC")
 static class TestLocalDate {
 }

 @JsonFormat(shape = JsonFormat.Shape.STRING, localDateFormat = "dd-MM-yyyy", timeZone = "UTC", useTimezone = true)
 static class TestLocalDateWithTimeZone {
 }

 @JsonFormat(shape = JsonFormat.Shape.STRING, localDateTimeFormat = "dd-MM-yyyy HH:mm:ss.SSSSSS", useNanos = true, timezone = "UTC")
 static class TestLocalDateTime {
 }

 @JsonFormat(shape = JsonFormat.Shape.STRING, localDateTimeFormat = "dd-MM-yyyy HH:mm:ss.SSSSSS", useNanos = true, timezone = "UTC", localizedBy = JsonFormat.Localized.ALL)
 static class TestLocalDateTimeAllLocalized {
 }

 @JsonFormat(shape = JsonFormat.Shape.STRING, localDateTimeFormat = "dd-MM-yyyy HH:mm:ss.SSSSSS", useNanos = true, timezone = "UTC")
 static class TestLocalDateTimeWithTimeZone {
 }

 @JsonFormat(shape = JsonFormat.Shape.STRING, localDateTimeFormat = "dd-MM-yyyy HH:mm:ss.SSSSSS", useNanos = true, timezone = "UTC", serializeAs = JsonFormat.SerializedAs.STRING)
 static class TestLocalDateTimeStringSerialized {
 }

 @JsonFormat(shape = JsonFormat.Shape.STRING, localDateTimeFormat = "dd-MM-yyyy HH:mm:ss.SSSSSS", useNanos = true, timezone = "UTC", serializeAs = JsonFormat.SerializedAs.NUMBER)
 static class TestLocalDateTimeNumberSerialized {
 }

 @JsonFormat(shape = JsonFormat.Shape.STRING, localDateTimeFormat = "dd-MM-yyyy HH:mm:ss.SSSSSS", useNanos = true, timezone = "UTC", serializeAs = JsonFormat.SerializedAs.BOTH)
 static class TestLocalDateTimeBothSerialized {
 }

 public void testDateTimeSerializerBase() throws Exception {
 SerializerProvider serializers = new StandardSerializerProvider();
 ObjectMapper mapper = new ObjectMapper(serializers);

 // Test serialization with default settings
 DateTimeSerializerBase<TestDate> serializerDate = new DateTimeSerializerBase<TestDate>(TestDate.class, false, null);
 JsonNode nodeDate = mapper.writeValueAsTree(new TestDate(), serializerDate);
 assertEquals("\"2023-03-14 00:00:00.000000Z\"", nodeDate.toString());

 DateTimeSerializerBase<TestDateWithTimeZone> serializerDateWithTimeZone = new DateTimeSerializerBase<TestDateWithTimeZone>(TestDateWithTimeZone.class, false, null);
 JsonNode nodeDateWithTimeZone = mapper.writeValueAsTree(new TestDateWithTimeZone(), serializerDateWithTimeZone);
 assertEquals("\"2023-03-14 00:00:00.000Z\"", nodeDateWithTimeZone.toString());

 DateTimeSerializerBase<TestDateWithoutPattern> serializerDateWithoutPattern = new DateTimeSerializerBase<TestDateWithoutPattern>(TestDateWithoutPattern.class, false, null);
 JsonNode nodeDateWithoutPattern = mapper.writeValueAsTree(new TestDateWithoutPattern(), serializerDateWithoutPattern);
 assertEquals("0", nodeDateWithoutPattern.toString());

 DateTimeSerializerBase<TestDateWithNanos> serializerDateWithNanos = new DateTimeSerializerBase<TestDateWithNanos>(TestDateWithNanos.class, false, null);
 JsonNode nodeDateWithNanos = mapper.writeValueAsTree(new TestDateWithNanos(), serializerDateWithNanos);
 assertEquals("0", nodeDateWithNanos.toString());

 // Test serialization with custom format
 DateTimeSerializerBase<TestDate> serializerDateCustomFormat = new DateTimeSerializerBase<TestDate>(TestDate.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
 JsonNode nodeDateCustomFormat = mapper.writeValueAsTree(new TestDate(), serializerDateCustomFormat);
 assertEquals("\"14-03-2023 00:00:00\"", nodeDateCustomFormat.toString());

 DateTimeSerializerBase<TestDateWithTimeZone> serializerDateWithTimeZoneCustomFormat = new DateTimeSerializerBase<TestDateWithTimeZone>(TestDateWithTimeZone.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
 JsonNode nodeDateWithTimeZoneCustomFormat = mapper.writeValueAsTree(new TestDateWithTimeZone(), serializerDateWithTimeZoneCustomFormat);
 assertEquals("\"14-03-2023 00:00:00\"", nodeDateWithTimeZoneCustomFormat.toString());

 // Test serialization with local date format
 DateTimeSerializerBase<TestLocalDate> serializerLocalDate = new DateTimeSerializerBase<TestLocalDate>(TestLocalDate.class, false, null);
 JsonNode nodeLocalDate = mapper.writeValueAsTree(new TestLocalDate(), serializerLocalDate);
 assertEquals("\"2023-03-14\"", nodeLocalDate.toString());

 DateTimeSerializerBase<TestLocalDateWithTimeZone> serializerLocalDateWithTimeZone = new DateTimeSerializerBase<TestLocalDateWithTimeZone>(TestLocalDateWithTimeZone.class, false, null);
 JsonNode nodeLocalDateWithTimeZone = mapper.writeValueAsTree(new TestLocalDateWithTimeZone(), serializerLocalDateWithTimeZone);
 assertEquals("\"2023-03-14T00:00:00Z\"", nodeLocalDateWithTimeZone.toString());

 // Test serialization with local datetime format
 DateTimeSerializerBase<TestLocalDateTime> serializerLocalDateTime = new DateTimeSerializerBase<TestLocalDateTime>(TestLocalDateTime.class, false, null);
 JsonNode nodeLocalDateTime = mapper.writeValueAsTree(new TestLocalDateTime(), serializerLocalDateTime);
 assertEquals("\"2023-03-14 00:00:00.000000Z\"", nodeLocalDateTime.toString());

 DateTimeSerializerBase<TestLocalDateTimeAllLocalized> serializerLocalDateTimeAllLocalized = new DateTimeSerializerBase<TestLocalDateTimeAllLocalized>(TestLocalDateTimeAllLocalized.class, false, null);
 JsonNode nodeLocalDateTimeAllLocalized = mapper.writeValueAsTree(new TestLocalDateTimeAllLocalized(), serializerLocalDateTimeAllLocalized);
 assertEquals("\"14-Mar-2023 00:00:00.000000000Z\"", nodeLocalDateTimeAllLocalized.toString());

 DateTimeSerializerBase<TestLocalDateTimeWithTimeZone> serializerLocalDateTimeWithTimeZone = new DateTimeSerializerBase<TestLocalDateTimeWithTimeZone>(TestLocalDateTimeWithTimeZone.class, false, null);
 JsonNode nodeLocalDateTimeWithTimeZone = mapper.writeValueAsTree(new TestLocalDateTimeWithTimeZone(), serializerLocalDateTimeWithTimeZone);
 assertEquals("\"2023-03-14T00:00:00.000Z\"", nodeLocalDateTimeWithTimeZone.toString());

 DateTimeSerializerBase<TestLocalDateTimeStringSerialized> serializerLocalDateTimeStringSerialized = new DateTimeSerializerBase<TestLocalDateTimeStringSerialized>(TestLocalDateTimeStringSerialized.class, false, null);
 JsonNode nodeLocalDateTimeStringSerialized = mapper.writeValueAsTree(new TestLocalDateTimeStringSerialized(), serializerLocalDateTimeStringSerialized);
 assertEquals("\"2023-03-14T00:00:00.000Z\"", nodeLocalDateTimeStringSerialized.toString());

 DateTimeSerializerBase<TestLocalDateTimeNumberSerialized> serializerLocalDateTimeNumberSerialized = new DateTimeSerializerBase<TestLocalDateTimeNumberSerialized>(TestLocalDateTimeNumberSerialized.class, false, null);
 JsonNode nodeLocalDateTimeNumberSerialized = mapper.writeValueAsTree(new TestLocalDateTimeNumberSerialized(), serializerLocalDateTimeNumberSerialized);
 assertEquals("0", nodeLocalDateTimeNumberSerialized.toString());

 DateTimeSerializerBase<TestLocalDateTimeBothSerialized> serializerLocalDateTimeBothSerialized = new DateTimeSerializerBase<TestLocalDateTimeBothSerialized>(TestLocalDateTimeBothSerialized.class, false, null);
 JsonNode nodeLocalDateTimeBothSerialized = mapper.writeValueAsTree(new TestLocalDateTimeBothSerialized(), serializerLocalDateTimeBothSerialized);
 assertEquals("\"2023-03-14T00:00:00.000Z\"", nodeLocalDateTimeBothSerialized.toString());

 // Test deserialization with default settings
 JsonParser parser = mapper.getFactory().createParser("\"2023-03-14 00:00:00.000000Z\"");
 TestDate deserializedDate = mapper.readValue(parser, TestDate.class);
 assertEquals(new DateTime(), deserializedDate.getClass().getDeclaredField("value").get(deserializedDate));

 TestDateWithTimeZone deserializedDateWithTimeZone = mapper.readValue(parser, TestDateWithTimeZone.class);
 assertEquals(new DateTime(), deserializedDateWithTimeZone.getClass().getDeclaredField("value").get(deserializedDateWithTimeZone));

 TestDateWithoutPattern deserializedDateWithoutPattern = mapper.readValue(parser, TestDateWithoutPattern.class);
 assertEquals(0, deserializedDateWithoutPattern.getClass().getDeclaredField("value").get(deserializedDateWithoutPattern));

 TestDateWithNanos deserializedDateWithNanos = mapper.readValue(parser, TestDateWithNanos.class);
 assertEquals(0, deserializedDateWithNanos.getClass().getDeclaredField("value").get(deserializedDateWithNanos));

 // Test deserialization with custom format
 JsonParser parserCustomFormat = mapper.getFactory().createParser("\"14-03-2023 00:00:00\"");
 TestDate deserializedDateCustomFormat = mapper.readValue(parserCustomFormat, TestDate.class);
 assertEquals(new DateTime(), deserializedDateCustomFormat.getClass().getDeclaredField("value").get(deserializedDateCustomFormat));

 // Test deserialization with local date format
 JsonParser parserLocalDate = mapper.getFactory().createParser("\"2023-03-14\"");
 TestLocalDate deserializedLocalDate = mapper.readValue(parserLocalDate, TestLocalDate.class);
 assertEquals(new DateTime().withDate(new org.joda.time.LocalDate(2023, 3, 14)), deserializedLocalDate.getClass().getDeclaredField("value").get(deserializedLocalDate));

 // Test deserialization with local datetime format
 JsonParser parserLocalDateTime = mapper.getFactory().createParser("\"2023-03-14 00:00:00.000000Z\"");
 TestLocalDateTime deserializedLocalDateTime = mapper.readValue(parserLocalDateTime, TestLocalDateTime.class);
 assertEquals(new DateTime(2023, 3, 14, 0, 0), deserializedLocalDateTime.getClass().getDeclaredField("value").get(deserializedLocalDateTime));

 // Test deserialization with local datetime format and timezone
 JsonParser parserLocalDateTimeWithTimeZone = mapper.getFactory().createParser("\"2023-03-14T00:00:00.000Z\"");
 TestLocalDateTimeWithTimeZone deserializedLocalDateTimeWithTimeZone = mapper.readValue(parserLocalDateTimeWithTimeZone, TestLocalDateTimeWithTimeZone.class);
 assertEquals(new DateTime(2023, 3, 14, 0, 0).toInstant().toEpochMilli(), deserializedLocalDateTimeWithTimeZone.getClass().getDeclaredField("value").get(deserializedLocalDateTimeWithTimeZone).longValue());
 }

}