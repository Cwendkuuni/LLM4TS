package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private static final Class<Date> DATE_CLASS = Date.class;

 @Test
 public void testDateTimeSerializerBaseConstruction() {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, null, null);
 assertNotNull(serializer);
 }

 @Test
 public void testDateTimeSerializerBaseWithFormat() {
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, null, customFormat);
 assertEquals(customFormat, serializer.getFormat());
 }

 @Test
 public void testDateTimeSerializerBaseWithTimestamp() {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, true, null);
 assertTrue(serializer.useTimestamp());
 }

 @Test
 public void testDateTimeSerializerBaseWithDefaultFormat() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(DATE_CLASS);
 assertNull(serializer.getFormat());
 }

 @Test
 public void testDateTimeSerializerBaseAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getFormatVisitor(Date.class);
 DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(DATE_CLASS);
 serializer.acceptJsonFormatVisitor(visitor, DATE_CLASS);
 assertNotNull(visitor.getFormat());
 }

 @Test
 public void testDateTimeSerializerBaseWithTimestampSerialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.enableDefaultTyping(false);
 ObjectWriter writer = mapper.writerWithRootValueSerializer(new DateTimeSerializerBase<Date>(DATE_CLASS, true, null));
 JsonNode node = writer.writeValueAsTree(new Date());
 assertEquals("1632450865000", node.getTextValue());
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomFormatSerialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writerWithRootValueSerializer(new DateTimeSerializerBase<Date>(DATE_CLASS, null, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
 JsonNode node = writer.writeValueAsTree(new Date());
 assertEquals("2023-03-25 14:33:14", node.getTextValue());
 }

 @Test
 public void testDateTimeSerializerBaseDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("2023-03-25 14:33:14");
 ObjectReader reader = mapper.readerForType(Date.class);
 Date date = reader.readValue(parser);
 assertEquals(new Date(), date);
 }
}