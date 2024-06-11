package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
 private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDateTimeSerializerBase() throws JsonProcessingException, IOException {
 DateTimeSerializerBase<DateTime> datetimeSerializer = new DateTimeSerializerBase<DateTime>(DateTime.class, false, null);

 // Test serialization with default settings
 DateTime testDate = new DateTime();
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 datetimeSerializer.serialize(testDate, gen, new SerializerProvider());
 gen.flush();
 assertEquals("\"null\"", gen.getOutputBuffer().toString());

 // Test serialization as timestamp
 DateTimeSerializerBase<DateTime> timestampSerializer = datetimeSerializer.withFormat(true, null);
 testDate = new DateTime(System.currentTimeMillis());
 gen = new JsonFactory().createGenerator(System.out);
 timestampSerializer.serialize(testDate, gen, new SerializerProvider());
 gen.writeNumber(testDate.getMillis());
 gen.flush();
 assertEquals(testDate.getMillis(), gen.getOutputBuffer().toString().longValue());

 // Test serialization with custom format as string
 DateTimeFormatter customFormat = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
 DateTimeSerializerBase<DateTime> stringSerializer = datetimeSerializer.withFormat(false, customFormat.getFormat());
 testDate = new DateTime(System.currentTimeMillis());
 gen = new JsonFactory().createGenerator(System.out);
 stringSerializer.serialize(testDate, gen, new SerializerProvider());
 gen.writeString(customFormat.print(testDate));
 gen.flush();
 assertEquals(customFormat.print(testDate), gen.getOutputBuffer().toString());

 // Test serialization with custom format and locale
 DateTimeSerializerBase<DateTime> localizedSerializer = datetimeSerializer.withFormat(false, customFormat.withLocale(Locale.FRENCH));
 testDate = new DateTime(System.currentTimeMillis());
 gen = new JsonFactory().createGenerator(System.out);
 localizedSerializer.serialize(testDate, gen, new SerializerProvider());
 gen.writeString(customFormat.withLocale(Locale.FRENCH).print(testDate));
 gen.flush();
 assertEquals(customFormat.withLocale(Locale.FRENCH).print(testDate), gen.getOutputBuffer().toString());

 // Test serialization with custom format, locale and timezone
 DateTimeFormatter customFormatWithTimeZone = customFormat.withZone(DateTimeZone.forID("Europe/Paris"));
DateTimeSerializerBase<DateTime> timezoneSerializer = datetimeSerializer.withFormat(false, customFormatWithTimeZone.getFormat());
 testDate = new DateTime(System.currentTimeMillis());
 gen = new JsonFactory().createGenerator(System.out);
 timezoneSerializer.serialize(testDate, gen, new SerializerProvider());
 gen.writeString(customFormatWithTimeZone.print(testDate));
 gen.flush();
 assertEquals(customFormatWithTimeZone.print(testDate), gen.getOutputBuffer().toString());

 // Test deserialization
 JsonParser parser = new JsonFactory().createParser(new StringReader("\"2022-01-01 12:34:56\""));
 JsonDeserializer<DateTime> jsonDeserializer = new JsonDeserializer<DateTime>(DateTime.class) {
 @Override
 public DateTime deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return new DateTime(p.getValueAsString());
 }
 };
 DateTime deserializedDate = jsonDeserializer.deserialize(parser, new SerializerProvider());
 assertEquals(new DateTime("2022-01-01T12:34:56Z"), deserializedDate);

 // Test deserialization with custom format as string
 parser = new JsonFactory().createParser(new StringReader("\"dd-MM-yyyy HH:mm:ss\" \"01-01-2022 12:34:56\""));
 JsonDeserializer<DateTime> customDeserializer = new JsonDeserializer<DateTime>(DateTime.class) {
 @Override
 public DateTime deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return new DateTime(p.getText().substring(12));
 }
 };
 deserializedDate = customDeserializer.deserialize(parser, new SerializerProvider());
 assertEquals(new DateTime("2022-01-01T12:34:56Z"), deserializedDate);

 // Test deserialization with custom format, locale and timezone
 DateTimeFormatter customFormatWithTimeZoneDeserializer = customFormat.withZone(DateTimeZone.forID("Europe/Paris"));
 JsonDeserializer<DateTime> customTimezoneDeserializer = new JsonDeserializer<DateTime>(DateTime.class) {
 @Override
 public DateTime deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return new DateTime(customFormatWithTimeZoneDeserializer.parseLocalDate("01-01-2022").toDateTimeAtStartOfDay().toDate());
 }
 };
 deserializedDate = customTimezoneDeserializer.deserialize(parser, new SerializerProvider());
 assertEquals(new DateTime("2022-01-01T01:00:00.000+01:00"), deserializedDate);
 }
}