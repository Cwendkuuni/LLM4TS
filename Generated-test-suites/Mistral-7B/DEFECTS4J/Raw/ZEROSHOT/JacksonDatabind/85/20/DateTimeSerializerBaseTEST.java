package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.*;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> longSerializer;
 private DateTimeSerializerBase<DateTime> dateTimeSerializer;

 @Before
 public void setup() {
 longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
 dateTimeSerializer = new DateTimeSerializerBase<DateTime>(DateTime.class, false, null);
 }

 @Test
 public void testDefaultSerialization() throws Exception {
 // Test long serialization
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
 Long testLong = new Long(1492235200000L); // Fri Jan 20 12:00:00 UTC 2017
 String json = objectWriter.writeValueAsString(testLong);
 assertEquals("1492235200000", json);

 // Test DateTime serialization
 DateTime testDateTime = new DateTime(2017, 1, 20, 12, 0, 0, 0, Locale.US);
 ObjectWriter objectWriterDateTime = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String jsonDateTime = objectWriterDateTime.writeValueAsString(testDateTime);
 assertEquals("2017-01-20T12:00:00.000Z", jsonDateTime);
 }

 @Test
 public void testCustomFormatSerialization() throws Exception {
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
 ObjectWriter objectWriterCustomFormat = new ObjectMapper()
 .writerWithDefaultPrettyPrinter()
 .withSerializers(new SimpleModule().addSerializer(DateTime.class, new DateTimeSerializerBase<DateTime>(DateTime.class, false, new SimpleDateFormat("dd-MM-yy hh:mm a z"))));

 DateTime testDateTime = new DateTime(2017, 1, 20, 12, 0, 0, 0, Locale.US);
 String jsonDateTime = objectWriterCustomFormat.writeValueAsString(testDateTime);
 assertEquals("20-01-17 12:00 PM CST", jsonDateTime);
 }

 @Test
 public void testSerializationWithTimestamp() throws Exception {
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
 ObjectWriter objectWriterWithTimestamp = new ObjectMapper()
 .writerWithDefaultPrettyPrinter()
 .withSerializationFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

 Long testLong = new Long(1492235200000L); // Fri Jan 20 12:00:00 UTC 2017
 String json = objectWriterWithTimestamp.writeValueAsString(testLong);
 assertEquals("1492235200000", json);
 }

 @Test
 public void testSerializationWithCustomFormatAndTimestamp() throws Exception {
 ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
 ObjectWriter objectWriterCustomFormat = new ObjectMapper()
 .writerWithDefaultPrettyPrinter()
 .withSerializationFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
 .withSerializers(new SimpleModule().addSerializer(DateTime.class, new DateTimeSerializerBase<DateTime>(DateTime.class, false, new SimpleDateFormat("dd-MM-yy hh:mm a z"))));

 DateTime testDateTime = new DateTime(2017, 1, 20, 12, 0, 0, 0, Locale.US);
 String jsonDateTime = objectWriterCustomFormat.writeValueAsString(testDateTime);
 assertEquals("20-01-17 12:00 PM CST", jsonDateTime);
 }

 @Test
 public void testDeserializationLong() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("1492235200000");
 Long deserializedLong = objectMapper.readValue(jsonParser, Long.class);
 assertEquals(new Long(1492235200000L), deserializedLong);
 }

 @Test
 public void testDeserializationDateTime() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("2017-01-20T12:00:00.000Z");
 DateTime deserializedDateTime = objectMapper.readValue(jsonParser, DateTime.class);
 DateTime expectedDateTime = new DateTime(2017, 1, 20, 12, 0, 0, 0, Locale.US);
 assertEquals(expectedDateTime, deserializedDateTime);
 }

 @Test
 public void testDeserializationWithCustomFormat() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper objectMapperCustomFormat = new ObjectMapper();
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy hh:mm a z");
 objectMapperCustomFormat.setDateFormatInstance(dateFormat);
 JsonParser jsonParser = objectMapperCustomFormat.getFactory().createParser("20-01-17 12:00 PM CST");
 DateTime deserializedDateTime = objectMapperCustomFormat.readValue(jsonParser, DateTime.class);
 DateTime expectedDateTime = new DateTime(2017, 1, 20, 12, 0, 0, 0, Locale.US);
 assertEquals(expectedDateTime, deserializedDateTime);
 }

 @Test
 public void testDeserializationWithCustomFormatAndTimestamp() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper objectMapperCustomFormat = new ObjectMapper();
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy hh:mm a z");
 objectMapperCustomFormat.setDateFormatInstance(dateFormat);
 objectMapperCustomFormat.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
 JsonParser jsonParser = objectMapperCustomFormat.getFactory().createParser("20-01-17 12:00 PM CST");
 Long deserializedLong = objectMapperCustomFormat.readValue(jsonParser, Long.class);
 assertEquals(new Long(1389260800000L), deserializedLong); // 20-Jan-13 12:00:00 CST
 }

 @Test
 public void testDeserializationNull() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser("null");
 assertNull(objectMapper.readValue(jsonParser, Long.class));
 }
}