package com.example.junit4;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> dateSerializer;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 objectMapper = new JsonMapper();
 }

 @After
 public void teardown() {
 dateSerializer = null;
 objectMapper = null;
 }

 @Test
 public void testDefaultSerialization() throws JsonGenerationException, IOException {
 Date date = new Date();
 String json = dateSerializer.serialize(date, null, null);
 assertNotNull(json);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 Date parsedDate = objectMapper.readValue(parser, Date.class);
 assertEquals(date, parsedDate);
 }

 @Test
 public void testSerializationWithCustomFormat() throws JsonGenerationException, IOException, ParseException {
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 Date date = new Date();
 String json = customSerializer.serialize(date, null, null);
 assertEquals("25-12-2021 10:20:30", json);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 Date parsedDate = objectMapper.readValue(parser, Date.class);
 assertEquals(date, parsedDate);
 }

 @Test
 public void testSerializationAsTimestamp() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<Date> timestampSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 Date date = new Date();
 String json = timestampSerializer.serialize(date, null, null);
 assertNotNull(json);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 long timestamp = parser.getNumberValue();
 assertEquals(date.getTime(), timestamp);
 }

 @Test
 public void testSerializationWithLocale() throws JsonGenerationException, IOException, ParseException {
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRENCH);
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 Date date = new Date();
 String json = customSerializer.serialize(date, null, null);
 assertEquals("25-12-2021 10:20:30", json);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 Date parsedDate = objectMapper.readValue(parser, Date.class);
 assertEquals(date, parsedDate);
 }

 @Test
 public void testSerializationWithTimeZone() throws JsonGenerationException, IOException, ParseException {
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss Z", Locale.US);
 customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 Date date = new Date();
 String json = customSerializer.serialize(date, null, null);
 assertEquals("25-12-2021 10:20:30Z", json);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 Date parsedDate = objectMapper.readValue(parser, Date.class);
 assertEquals(date, parsedDate);
 }

 @Test
 public void testSerializationWithNullValue() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<Date> nullSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 String json = nullSerializer.serialize(null, null, null);
 assertNull(json);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializationWithInvalidConfig() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<Date> invalidSerializer = new DateTimeSerializerBase<Date>(null, false, null);
 // This will fail since the SerializerProvider is null
 assertFalse(invalidSerializer.isValid());
 }

 @Test
 public void testSerializationWithJsonFormat() throws IOException {
 JsonFormat<Date> jsonFormat = new JsonFormat.Value();
 jsonFormat.setShape(JsonFormat.Shape.STRING);
 jsonFormat.setLocale(Locale.FRENCH);
 jsonFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 jsonFormat.setPattern("dd-MM-yyyy HH:mm:ss");
 DateTimeSerializerBase<Date> jsonSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null).withFormat(false, jsonFormat);
 Date date = new Date();
 String json = jsonSerializer.serialize(date, null, null);
 assertEquals("25-12-2021 10:20:30", json);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 Date parsedDate = objectMapper.readValue(parser, Date.class);
 assertEquals(date, parsedDate);
 }

 @Test
 public void testDeserialization() throws IOException, JsonProcessingException {
 String json = "\"25-12-2021 10:20:30\"";
 Date date = dateSerializer.deserialize(json, null, null);
 assertNotNull(date);
 assertEquals("Sat Dec 25 10:20:30 GMT 2021", date.toString());
 }

 @Test
 public void testDeserializationWithCustomFormat() throws IOException, JsonProcessingException, ParseException {
 String json = "\"25-12-2021 10:20:30\"";
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 Date date = customSerializer.deserialize(json, null, null);
 assertEquals(new Date(), date);
 }

 @Test
 public void testDeserializationAsTimestamp() throws IOException, JsonProcessingException {
 String json = "1647488830000";
 DateTimeSerializerBase<Date> timestampSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 Date date = timestampSerializer.deserialize(json, null, null);
 assertNotNull(date);
 assertEquals(new Date(1647488830000L), date);
 }

 @Test(expected = IOException.class)
 public void testDeserializationWithInvalidJson() throws IOException, JsonProcessingException {
 String json = "{\"invalid\":\"json\"}";
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 Date date = serializer.deserialize(json, null, null);
 fail("Expected an IOException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidFormat() throws IOException, JsonProcessingException {
 String json = "2021-12-25T10:20:30Z";
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 serializer.setFailureHandler(new FailureHandler() {
 @Override
 public void handleFailure(JsonParser p, JsonMappingException e) {
 fail("Expected a JsonMappingException");
 }
 });
 Date date = serializer.deserialize(json, null, null);
 fail("Expected a JsonMappingException");
 }
}