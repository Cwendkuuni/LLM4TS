package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> longSerializer;
 private DateTimeSerializerBase<String> stringSerializer;

 @Before
 public void setUp() {
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
 longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, dateFormat);
 stringSerializer = new DateTimeSerializerBase<String>(String.class, false, dateFormat);
 }

 @Test
 public void testSerialization() throws IOException {
 Long date = 1563257600000L; // 2019-05-12 12:00:00 UTC
 String expectedJson = "2019-05-12T12:00:00Z";

 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper().getSerializerProvider());
 longSerializer.serialize(date, gen, new ObjectMapper().getSerializerProvider());
 assertEquals(expectedJson, gen.getOutputBuffer().toString());

 gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper().getSerializerProvider());
 stringSerializer.serialize(date, gen, new ObjectMapper().getSerializerProvider());
 assertEquals(expectedJson, gen.getOutputBuffer().toString());
 }

 @Test
 public void testSerializationAsTimestamp() throws IOException {
 Long date = 1563257600000L;

 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper().getSerializerProvider());
 longSerializer.serialize(date, gen, new ObjectMapper().getSerializerProvider(), true);
 assertEquals("1563257600000", gen.getOutputBuffer().toString());

 gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper().getSerializerProvider());
 stringSerializer.serialize(date, gen, new ObjectMapper().getSerializerProvider(), true);
 assertEquals("1563257600000", gen.getOutputBuffer().toString());
 }

 @Test
 public void testContextualSerialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonNode node = mapper.readTree("{\"date\": \"2019-05-12T12:00:00Z\"}");
 Long date = mapper.treeToValue(node.path("date"), Long.class);
 assertEquals(1563257600000L, date.longValue());
 }

 @Test
 public void testCustomFormatSerialization() throws IOException {
 DateTimeSerializerBase<String> customFormatSerializer = new DateTimeSerializerBase<String>(String.class, false, null);
 String customFormatJson = "{\"date\": \"CustomFormat:d\"}";
 JsonParser parser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(customFormatJson.getBytes()));
 String result = customFormatSerializer.deserialize(parser, String.class, new ObjectMapper().getSerializerProvider());
 assertEquals("CustomFormat:d", result);
 }

 @Test
 public void testSerializationWithCustomFormat() throws IOException {
 DateTimeSerializerBase<String> customFormatSerializer = new DateTimeSerializerBase<String>(String.class, false, new SimpleDateFormat("yyyy-MM-dd"));
 String expectedJson = "2019-05-12";

 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper().getSerializerProvider());
 customFormatSerializer.serialize(new Date(1563257600000L), gen, new ObjectMapper().getSerializerProvider());
 assertEquals(expectedJson, gen.getOutputBuffer().toString());
 }

 @Test
 public void testSerializationWithJsonFormat() throws IOException {
 JsonFormat.Value jsonFormat = new JsonFormat.Value();
 jsonFormat.setShape(JsonFormat.Shape.STRING);
 jsonFormat.setPattern("yyyy-MM-dd HH:mm:ss z");
 jsonFormat.setLocale(Locale.US);
 jsonFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 ObjectMapper mapper = new ObjectMapper();
 mapper.setDefaultPropertyInclusion(FindValueAsPropertyName.Std.EXISTS_AND_NON_EMPTY);
 mapper.findAndRegisterColumns(Date.class, null);
 mapper.setSerializerProvider(new ObjectMapper().getSerializerProvider());
 JsonSerializer<Date> serializer = mapper.findValueSerializer(Date.class, null, null);

 Long date = 1563257600000L;
 String expectedJson = "2019-05-12T12:00:00Z";

 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 serializer.serialize(new Date(date), gen, mapper.getSerializerProvider());
 assertEquals(expectedJson, gen.getOutputBuffer().toString());
 }
}