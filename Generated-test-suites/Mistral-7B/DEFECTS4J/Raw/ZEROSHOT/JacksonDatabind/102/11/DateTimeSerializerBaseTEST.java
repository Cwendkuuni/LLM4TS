package com.example.jackson.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 private ObjectMapper objectMapper;
 private DateTimeSerializerBase<Date> dateTimeSerializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 }

 @Test
 public void testDateTimeSerializerBaseWithDefaultSettings() throws JsonGenerationException, IOException, JsonMappingException {
 // Test serialization
 Date date = new Date();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 dateTimeSerializer.serialize(date, generator, objectMapper.getSerializerProvider());
 String serializedDate = generator.getOutputBuffer().toString();
 assertEquals("Thu Jan 01 00:00:00 GMT", serializedDate);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(new ByteArrayInputStream(serializedDate.getBytes()));
 Date deserializedDate = dateTimeSerializer.deserialize(parser, objectMapper.getSerializerProvider().getRootValueInstance(Date.class));
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomFormat() throws JsonGenerationException, IOException, JsonMappingException, ParseException {
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
 customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);

 // Test serialization
 Date customDate = new Date();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 dateTimeSerializer.serialize(customDate, generator, objectMapper.getSerializerProvider());
 String serializedDateWithCustomFormat = generator.getOutputBuffer().toString();
 assertEquals("01-01-2023 12:00:00 UTC", serializedDateWithCustomFormat);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(new ByteArrayInputStream(serializedDateWithCustomFormat.getBytes()));
 JsonStringFormatVisitor visitor = objectMapper.getFactory().getSerializerProvider().findValueSerializer(JsonValueType.STRING);
 Date deserializedDateWithCustomFormat = visitor.acceptStringFormatVisitor(dateTimeSerializer, null, null, parser, null, null);
 assertEquals(customDate, deserializedDateWithCustomFormat);
 }

 @Test
 public void testDateTimeSerializerBaseWithTimestampSetting() throws JsonGenerationException, IOException, JsonMappingException {
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);

 // Test serialization
 Date date = new Date();
 JsonGenerator generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 dateTimeSerializer.serialize(date, generator, objectMapper.getSerializerProvider());
 long serializedDateAsLong = generator.getOutputBuffer().readLong();
 assertEquals(date.getTime(), serializedDateAsLong);

 // Test deserialization
 JsonParser parser = objectMapper.getFactory().createParser(new ByteArrayInputStream(Long.toString(date.getTime()).getBytes()));
 Date deserializedDate = dateTimeSerializer.deserialize(parser, objectMapper.getSerializerProvider().getRootValueInstance(Date.class));
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testDateTimeSerializerBaseWithNullCustomFormat() {
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 assertNull(dateTimeSerializer._customFormat);
 }

 @Test
 public void testDateTimeSerializerBaseWithNullUseTimestamp() {
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, null, null);
 assertNull(dateTimeSerializer._useTimestamp);
 }
}