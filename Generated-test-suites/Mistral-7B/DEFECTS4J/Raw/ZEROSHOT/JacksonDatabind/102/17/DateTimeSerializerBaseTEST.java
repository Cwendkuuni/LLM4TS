package com.example.jackson.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider.Feature;
import com.fasterxml.jackson.databind.json.JsonMapperBuilder;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> serializer;
 private SerializerProvider provider;
 private ObjectMapper mapper;

 @Before
 public void setup() throws JsonMappingException {
 mapper = new JsonMapperBuilder()
 .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
 .build();
 provider = mapper.getSerializerProvider();
 serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 }

 @Test
 public void testDefaultBehavior() throws JsonGenerationException, JsonMappingException, IOException {
 Date date = new Date();
 String json = mapper.writeValueAsString(date);
 assertEquals("\"2023-02-24T15:36:25.213Z\"", json);
 }

 @Test
 public void testCustomFormat() throws JsonGenerationException, JsonMappingException, IOException {
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
 customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 DateTimeSerializerBase<Date> customSerializer = serializer.withFormat(false, customFormat);
 String json = mapper.writeValueAsString(date, customSerializer);
 assertEquals("\"2023-02-24 15:36:25 UTC\"", json);
 }

 @Test
 public void testTimestampBehavior() throws JsonGenerationException, JsonMappingException, IOException {
 DateTimeSerializerBase<Date> timestampSerializer = serializer.withFormat(true, null);
 String json = mapper.writeValueAsString(date, timestampSerializer);
 assertEquals("2023-02-24T15:36:25.213Z", json);
 }

 @Test
 public void testCustomFormatReuse() throws JsonGenerationException, JsonMappingException, IOException {
 DateTimeSerializerBase<Date> customSerializer = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US));
 String json1 = mapper.writeValueAsString(date, customSerializer);
 String json2 = mapper.writeValueAsString(date, customSerializer);
 assertEquals("\"2023-02-24 15:36:25 UTC\"", json1);
 assertEquals("\"2023-02-24 15:36:25 UTC\"", json2);
 }

 @Test
 public void testNullSerializer() throws JsonMappingException, IOException {
 DateTimeSerializerBase<Date> nullSerializer = null;
 assertNull("Serializer should be null", nullSerializer.createContextual(provider, null));
 }

 @Test(expected = JsonMappingException.class)
 public void testNullProperty() throws JsonMappingException, IOException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 serializer.createContextual(provider, null);
 }
}