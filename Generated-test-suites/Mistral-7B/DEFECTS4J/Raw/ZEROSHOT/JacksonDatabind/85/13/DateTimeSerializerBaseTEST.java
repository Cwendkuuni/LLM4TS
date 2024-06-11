package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("unchecked")
public class DateTimeSerializerBaseTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDateTimeSerializerBase() throws JsonProcessingException, JsonMappingException, IOException {
 DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, false, null);

 // Test serialization with default settings
 ObjectWriter writer = MAPPER.writerWithDefaultPrettyPrinter();
 DateTime dateTime = new DateTime();
 String json = writer.writeValueAsString(dateTime);
 assertEquals("\"2023-03-15T00:00:00.000Z\"", json);

 // Test serialization with custom format
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
 DateTimeSerializerBase<DateTime> serializerWithFormat = serializer.withFormat(false, customFormat);
 writer = MAPPER.writerWithDefaultPrettyPrinter();
 json = writer.writeValueAsString(dateTime, serializerWithFormat);
 assertEquals("\"15-03-2023 12:00:00 PDT\"", json);

 // Test serialization as timestamp
 DateTimeSerializerBase<DateTime> serializerWithTimestamp = serializer.withFormat(true, null);
 writer = MAPPER.writerWithDefaultPrettyPrinter();
 json = writer.writeValueAsString(dateTime, serializerWithTimestamp);
 assertEquals("2023-03-15T00:00:00.000Z", json);

 // Test deserialization
 ObjectReader reader = MAPPER.readerFor(DateTime.class);
 JsonParser parser = MAPPER.parserForStandardConfiguration();
 parser.setValueFactory(new SimpleTypeValueFactory(DateTime.class));
 DateTime deserializedDateTime = null;

 deserializedDateTime = reader.readValue(parser, DateTimeSerializerBase.class, "\"2023-03-15T00:00:00.000Z\"");
 assertEquals(dateTime, deserializedDateTime);

 deserializedDateTime = reader.readValue(parser, DateTimeSerializerBase.class, "\"15-03-2023 12:00:00 PDT\"");
 assertEquals(dateTime, deserializedDateTime);

 deserializedDateTime = reader.readValue(parser, DateTimeSerializerBase.class, "2023-03-15T00:00:00.000Z");
 assertEquals(dateTime, deserializedDateTime);
 }

 @Test
 public void testDateTimeSerializerBaseEmpty() throws JsonProcessingException, JsonMappingException, IOException {
 ObjectWriter writer = MAPPER.writerWithDefaultPrettyPrinter();
 DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, false, null);
 assertTrue(writer.writeValueAsString(null, serializer).isEmpty());
 assertTrue(writer.writeValueAsString(new DateTime(), serializer).isEmpty());
 }

 @Test
 public void testDateTimeSerializerBaseGetSchema() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 Schema schema = mapper.readerFor(DateTimeSerializerBase.class).readValue(MAPPER.readerFor(DateTimeSerializerBase.class).readValue(MAPPER.writerFor(DateTimeSerializerBase.class).writeValueAsString(new DateTimeFormatter())));
 assertEquals("number", schema.getDefaultValueType().getSimpleName());
 }
}