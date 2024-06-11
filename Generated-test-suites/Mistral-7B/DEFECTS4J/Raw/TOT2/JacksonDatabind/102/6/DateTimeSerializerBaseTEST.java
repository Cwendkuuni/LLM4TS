package com.fasterxml.jackson.databind.ser.std;

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
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> serializer;
 private ObjectMapper mapper;

 @Before
 public void setUp() {
 serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, null);
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 }

 @After
 public void tearDown() {
 mapper = null;
 serializer = null;
 }

 @Test
 public void testSerializeDate() throws JsonGenerationException, IOException {
 Date date = new Date();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(date, gen, mapper.getSerializerProvider());
 assertEquals("Expected serialized date to match expected format",
 gen.getOutputContent().toString().trim(),
 "(date): \"2022-12-19T14:30:45.123Z\"");
 }

 @Test
 public void testSerializeDateWithCustomFormat() throws JsonGenerationException, IOException {
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
 DateTimeSerializerBase<Long> serializerWithFormat = serializer.withFormat(Boolean.FALSE, customFormat);
 Date date = new Date();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializerWithFormat.serialize(date, gen, mapper.getSerializerProvider());
 assertEquals("Expected serialized date with custom format to match expected format",
 gen.getOutputContent().toString().trim(),
 "(date): \"2022-12-19T14:30:45.123+00:00\"");
 }

 @Test
 public void testSerializeDateWithUseTimestamp() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<Long> serializerWithTimestamp = serializer.withFormat(Boolean.TRUE, null);
 Date date = new Date();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializerWithTimestamp.serialize(date, gen, mapper.getSerializerProvider());
 assertEquals("Expected serialized date as timestamp to match expected format",
 gen.getOutputContent().toString().trim(),
 "(date): 1671265205000");
 }

 @Test
 public void testSerializeNullDate() throws JsonGenerationException, IOException {
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(null, gen, mapper.getSerializerProvider());
 assertEquals("Expected serialized null date to be empty string",
 gen.getOutputContent().toString().trim(), "");
 }

 @Test
 public void testDeserializeDate() throws JsonGenerationException, IOException, ParseException {
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
 String jsonString = "\"2022-12-19T14:30:45.123+00:00\"";
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 Object readValue = mapper.readValue(parser, Long.class);
 Date deserializedDate = mapper.readValue(parser, Date.class);
 assertEquals("Expected deserialized date to match expected format",
 deserializedDate, new Date(1671265205000L));
 assertEquals("Expected deserialized value to be a Long representing the timestamp",
 readValue.longValue(), 1671265205000L);
 }

 @Test
 public void testDeserializeDateWithCustomFormat() throws JsonGenerationException, IOException, ParseException {
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
 String jsonString = "\"2022-12-19T14:30:45.123+00:00\"";
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 Object readValue = mapper.readTree(parser).get(0);
 assertEquals("Expected deserialized value to be a Long representing the timestamp",
 readValue.getClass(), Long.class);
 assertEquals("Expected deserialized value to be equal to the original timestamp",
 readValue.longValue(), 1671265205000L);
 }

 @Test
 public void testDeserializeDateWithUseTimestamp() throws JsonGenerationException, IOException, ParseException {
 String jsonString = "1671265205000";
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 Object readValue = mapper.readValue(parser, Long.class);
 assertEquals("Expected deserialized value to be a Long representing the timestamp",
 readValue.getClass(), Long.class);
 assertEquals("Expected deserialized value to be equal to the original timestamp",
 readValue.longValue(), 1671265205000L);
 }

 @Test
 public void testDeserializeNullDate() throws IOException {
 Object readValue = mapper.readTree(mapper.getFactory().createParser("null"));
 assertEquals("Expected deserialized value to be null",
 readValue, null);
 }
}