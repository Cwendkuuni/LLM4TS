package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> serializer;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 serializer = new StdDateSerializer();
 mapper = new JsonMapper();
 }

 @Test
 public void testSerializer_useTimestamp_true() throws JsonProcessingException, IOException {
 // Test serializing with _useTimestamp = true
 SerializerProvider serializers = mapper.getSerializerProvider();
 Date date = new Date();
 JsonGenerator gen = serializers.getJsonFactory().createGenerator(System.out);
 serializer.serialize(date, gen, serializers);
 gen.flush();
 // Expected output: "1970-01-01T00:00:00.000Z" (Unix timestamp in ISO format)
 }

 @Test
 public void testSerializer_useTimestamp_false() throws JsonProcessingException, IOException {
 // Test serializing with _useTimestamp = false and default format
 SerializerProvider serializers = mapper.getSerializerProvider();
 Date date = new Date();
 serializer = serializer.withFormat(Boolean.FALSE, null);
 JsonGenerator gen = serializers.getJsonFactory().createGenerator(System.out);
 serializer.serialize(date, gen, serializers);
 gen.flush();
 // Expected output: "2023-03-22 15:25:56.789+02:00" (Default DateFormat)
 }

 @Test
 public void testSerializer_useCustomFormat() throws JsonProcessingException, IOException {
 // Test serializing with _customFormat set
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
 SerializerProvider serializers = mapper.getSerializerProvider();
 Date date = new Date();
 serializer = serializer.withFormat(Boolean.FALSE, customFormat);
 JsonGenerator gen = serializers.getJsonFactory().createGenerator(System.out);
 serializer.serialize(date, gen, serializers);
 gen.flush();
 // Expected output: "2023-03-22 15:25:56.789+02:00" (Custom DateFormat)
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializer_invalidCustomFormat() throws JsonProcessingException, IOException {
 // Test serializing with invalid custom format
 SerializerProvider serializers = mapper.getSerializerProvider();
 Date date = new Date();
 serializer = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z"));
 JsonGenerator gen = serializers.getJsonFactory().createGenerator(System.out);
 try {
 serializer.serialize(date, gen, serializers);
 gen.flush();
 } catch (JsonMappingException e) {
 // Expected exception
 }
 }

 @Test
 public void testDeserializer_useTimestamp_true() throws JsonProcessingException, IOException {
 // Test deserializing with _useTimestamp = true
 ObjectMapper mapper = new JsonMapper();
 String json = "1970-01-01T00:00:00.000Z";
 Date date = mapper.readValue(json, Date.class);
 assertEquals(new Date(1970, 0, 1, 0, 0, 0, 0L), date);
 }

 @Test
 public void testDeserializer_useTimestamp_false() throws JsonProcessingException, IOException {
 // Test deserializing with _useTimestamp = false and default format
 ObjectMapper mapper = new JsonMapper();
 String json = "2023-03-22 15:25:56.789+02:00";
 Date date = mapper.readValue(json, Date.class);
 assertEquals(new Date(1679340556789L), date);
 }

 @Test
 public void testDeserializer_useCustomFormat() throws JsonProcessingException, IOException {
 // Test deserializing with _customFormat set
 ObjectMapper mapper = new JsonMapper();
 String json = "2023-03-22 15:25:56.789+02:00";
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
 JsonDeserializer<Date> deserializer = mapper.findValueDeserializer(Date.class, customFormat);
 Date date = mapper.readValue(json, Date.class, deserializer);
 assertEquals(new Date(1679340556789L), date);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializer_invalidCustomFormat() throws JsonProcessingException, IOException {
 // Test deserializing with invalid custom format
 ObjectMapper mapper = new JsonMapper();
 String json = "dd-MM-yyyy HH:mm:ss z";
 JsonDeserializer<Date> deserializer = mapper.findValueDeserializer(Date.class, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z"));
 try {
 mapper.readValue(json, Date.class, deserializer);
 } catch (JsonMappingException e) {
 // Expected exception
 }
 }
}