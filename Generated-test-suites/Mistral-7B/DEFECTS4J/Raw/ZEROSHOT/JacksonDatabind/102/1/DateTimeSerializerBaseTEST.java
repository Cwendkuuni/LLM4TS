package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFormat.Shape;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase.AtomicDateFormat;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> longSerializer;
 private DateTimeSerializerBase<Instant> instantSerializer;

 @Before
 public void setup() {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, null);
 instantSerializer = new DateTimeSerializerBase<Instant>(Instant.class, Boolean.FALSE, null);
 }

 @Test
 public void testSerializationWithDefaultFormat() throws Exception {
 Long value = Instant.now().getEpochMilli();
 JsonGenerator gen = new JsonGenerator(new StringWriter());
 SerializerProvider serializers = new StandardSerializerProvider();
 longSerializer.serialize(value, gen, serializers);
 gen.flush();
 String json = gen.getOutputContent();
 assertEquals("\"637256903000\"", json);
 }

 @Test
 public void testSerializationWithCustomFormat() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 mapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
 DateTimeSerializerBase<Long> customSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));
 String json = mapper.writeValueAsString(1L);
 assertEquals("\"01-01-2022\"", json);
 }

 @Test
 public void testSerializationWithCustomFormatReuse() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 DateTimeSerializerBase<Long> customSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));
 AtomicDateFormat atomicDateFormat = new AtomicDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
 customSerializer._reusedCustomFormat.set(atomicDateFormat);
 String json = mapper.writeValueAsString(1L);
 assertEquals("\"01-01-2022\"", json);
 String json2 = mapper.writeValueAsString(1L);
 assertEquals("\"01-01-2022\"", json2);
 }

 @Test
 public void testSerializationWithCustomFormatAndShapeString() throws Exception {
 JsonFormat<Instant> format = new JsonFormat.Value()
 .withShape(JsonFormat.Shape.STRING)
 .withLocale(Locale.US)
 .withPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 DateTimeSerializerBase<Instant> customSerializer = new DateTimeSerializerBase<Instant>(Instant.class, Boolean.FALSE, format.getFormat());
 String json = mapper.writeValueAsString(Instant.now());
 assertEquals("\"2022-01-01T00:00:00.000Z\"", json);
 }

 @Test
 public void testDeserializationWithDefaultFormat() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 Long value = mapper.readValue("\"637256903000\"", Long.class);
 assertEquals(Instant.now().getEpochMilli(), value.longValue());
 }

 @Test
 public void testDeserializationWithCustomFormat() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 mapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
 Long value = mapper.readValue("\"01-01-2022\"", Long.class);
 assertEquals(1L, value.longValue());
 }

 @Test
 public void testDeserializationWithCustomFormatAndShapeString() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 mapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
 JsonFormat<Instant> format = new JsonFormat.Value()
 .withShape(JsonFormat.Shape.STRING)
 .withLocale(Locale.US)
 .withPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 Instant value = mapper.readValue("\"2022-01-01T00:00:00.000Z\"", Instant.class);
 assertEquals(Instant.ofEpochMilli(0), value);
 }

 @Test
 public void testDeserializationWithNullCustomFormat() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
DateTimeSerializerBase<Instant> deserializer = new DateTimeSerializerBase<Instant>(Instant.class, Boolean.FALSE, null);
 assertNull(deserializer.createContextual(new StandardSerializerProvider(), null));
 }

 @Test
 public void testSerializationWithCustomFormatWithLocale() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 mapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH));
 DateTimeSerializerBase<Long> customSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));
 String json = mapper.writeValueAsString(1L);
 assertEquals("\"01-01-2022\"", json);
 }

 @Test
 public void testSerializationWithCustomFormatWithTimeZone() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 mapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy", Locale.US));
 mapper.setTimeZone(TimeZone.getTimeZone("UTC"));
 DateTimeSerializerBase<Long> customSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));
 String json = mapper.writeValueAsString(1L);
 assertEquals("\"01-01-2022Z\"", json);
 }

 @Test
 public void testSerializationWithCustomFormatWithLocaleAndTimeZone() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 mapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH));
 mapper.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
 DateTimeSerializerBase<Long> customSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));
 String json = mapper.writeValueAsString(1L);
 assertEquals("\"01-01-2022+01:00\"", json);
 }

}

class AtomicDateFormat extends DateFormat {
 private final DateFormat delegate;

 public AtomicDateFormat(DateFormat dateFormat) {
 this.delegate = dateFormat;
 }

 @Override
 public String format(Date date) {
 return delegate.format(date);
 }

 @Override
 public Date parse(String text) throws ParseException {
 return delegate.parse(text);
 }
}