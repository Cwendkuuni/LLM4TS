package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> dateTimeSerializer;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 dateTimeSerializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
 objectMapper = new JsonMapper();
 }

 @Test
 public void testSerializeDateAsTimestamp() throws IOException {
 Long date = 1451606400000L; // 2015-11-16 00:00:00
 Calendar calendar = new GregorianCalendar(2015, 10, 16);
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 String expectedJson = dateFormat.format(calendar.getTime()) + "000";

 SerializerProvider serializers = new SerializerProvider();
 serializers.setSerializerInstance(DateTimeSerializerBase.class, dateTimeSerializer);
 JsonGenerator gen = serializers.getJsonFactory().createGenerator(new java.io.FileOutputStream("output.json"));
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 dateTimeSerializer = dateTimeSerializer.withFormat(true, customFormat);
 dateTimeSerializer.serialize(date, gen, serializers);
 gen.close();

 ObjectMapper mapper = new ObjectMapper(objectMapper);
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
 String actualJson = mapper.writeValueAsString(date);

 assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeDateAsString() throws IOException {
 Long date = 1451606400000L; // 2015-11-16 00:00:00
 Calendar calendar = new GregorianCalendar(2015, 10, 16);
 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 String expectedJson = dateFormat.format(calendar.getTime());

 SerializerProvider serializers = new SerializerProvider();
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 dateTimeSerializer = dateTimeSerializer.withFormat(false, customFormat);
 json.Serializer serializer = new JsonSerializer();
 jsonSerializer.setSerializerInstance(DateTimeSerializerBase.class, dateTimeSerializer);
 JsonGenerator gen = serializers.getJsonFactory().createGenerator(new java.io.FileOutputStream("output.json"));
 dateTimeSerializer.serialize(date, gen, serializers);
 gen.close();

 ObjectMapper mapper = new ObjectMapper(objectMapper);
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 String actualJson = mapper.writeValueAsString(date);

 assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeDateWithCustomFormat() throws IOException {
 Long date = 1451606400000L; // 2015-11-16 00:00:00
 Calendar calendar = new GregorianCalendar(2015, 10, 16);
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
 String expectedJson = dateFormat.format(calendar.getTime());

 SerializerProvider serializers = new SerializerProvider();
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
 dateTimeSerializer = dateTimeSerializer.withFormat(false, customFormat);
 json.Serializer serializer = new JsonSerializer();
 jsonSerializer.setSerializerInstance(DateTimeSerializerBase.class, dateTimeSerializer);
 JsonGenerator gen = serializers.getJsonFactory().createGenerator(new java.io.FileOutputStream("output.json"));
 dateTimeSerializer.serialize(date, gen, serializers);
 gen.close();

 ObjectMapper mapper = new ObjectMapper(objectMapper);
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 mapper.setTimeZone(TimeZone.getTimeZone("UTC"));
 String actualJson = mapper.writeValueAsString(date);

 assertEquals(expectedJson, actualJson);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSerializerProvider() {
 dateTimeSerializer.createContextual(null, null);
 }

}