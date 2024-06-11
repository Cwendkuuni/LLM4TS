package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 }

 @Test
 public void testDateTimeSerializerBase_useTimestamp_true() throws JsonGenerationException, JsonMappingException, IOException {
 Class<Long> longType = Long.TYPE;
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(longType, true, null);
 long timestamp = 1633027200000L; // Thu Jan 01 00:30:00 GMT 2022

 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
 Date date = dateFormat.parse("2022-01-01 00:30:00 UTC");

 Long value = timestamp;
 String json = objectMapper.writeValueAsString(value, null, serializer);
 assertEquals("1633027200000", json);

 Date parsedDate = dateFormat.parse(json);
 Long parsedValue = objectMapper.readValue(JsonFactory.value(json), Long.class);
 assertEquals(timestamp, parsedValue.longValue());
 }

 @Test
 public void testDateTimeSerializerBase_useTimestamp_false_default() throws JsonGenerationException, JsonMappingException, IOException {
 Class<Date> dateType = Date.class;
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(dateType, null, null);
 Date date = new Date();

 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
 String json = objectMapper.writeValueAsString(date, null, serializer);
 assertEquals("Thu Jan 01 00:30:04 GMT+01:00 2022", json);

 Date parsedDate = dateFormat.parse(json);
 Date parsedValue = objectMapper.readValue(JsonFactory.value(json), Date.class);
 assertEquals(date, parsedValue);
 }

 @Test
 public void testDateTimeSerializerBase_useTimestamp_false_custom() throws JsonGenerationException, JsonMappingException, IOException {
 Class<Date> dateType = Date.class;
 JsonFormat jsonFormat = new JsonFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, JsonFormat.Shape.STRING, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(dateType, null, new SimpleDateFormat(jsonFormat.getPattern(), jsonFormat.getLocale()));
 Date date = new Date();

 String json = objectMapper.writeValueAsString(date, null, serializer);
 assertEquals("2022-01-01T00:30:04+01:00", json);

 Date parsedDate = new SimpleDateFormat(jsonFormat.getPattern(), jsonFormat.getLocale()).parse(json);
 Date parsedValue = objectMapper.readValue(JsonFactory.value(json), Date.class);
 assertEquals(date, parsedValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testDateTimeSerializerBase_useTimestamp_false_custom_invalidFormat() throws JsonGenerationException, JsonMappingException, IOException {
 Class<Date> dateType = Date.class;
 JsonFormat jsonFormat = new JsonFormat("invalidFormat", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(dateType, null, new SimpleDateFormat(jsonFormat.getPattern(), jsonFormat.getLocale()));
 Date date = new Date();

 String json = objectMapper.writeValueAsString(date, null, serializer);
 // This test should fail with JsonMappingException
 }
}