package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private static class TestDateTimeSerializer<T> extends DateTimeSerializerBase<T> {
  public TestDateTimeSerializer(Class<T> type, Boolean useTimestamp, DateFormat customFormat) {
   super(type, useTimestamp, customFormat);
  }

  @Override
  public DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
   return new TestDateTimeSerializer<>(getType(), timestamp, customFormat);
  }
 }

 @Test
 public void testDateTimeSerializerBaseConstructor() {
  DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  DateTimeSerializerBase<String> serializer = new TestDateTimeSerializer<>(String.class, Boolean.FALSE, customFormat);
  assertNotNull(serializer);
  assertEquals(String.class, serializer.getType());
  assertFalse(serializer.isUseTimestamp());
  assertEquals(customFormat, serializer.getCustomFormat());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDateTimeSerializerBaseNullProvider() {
  DateTimeSerializerBase<String> serializer = new TestDateTimeSerializer<>(String.class, Boolean.FALSE, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDateTimeSerializerBaseNullType() {
  DateTimeSerializerBase<String> serializer = new TestDateTimeSerializer<>(null, Boolean.FALSE, null);
 }

 @Test
 public void testIsEmpty() {
  DateTimeSerializerBase<String> serializer = new TestDateTimeSerializer<>(String.class, Boolean.FALSE, null);
  assertTrue(serializer.isEmpty(null));
  assertTrue(serializer.isEmpty(serializer.getValueInstance()));
  assertFalse(serializer.isEmpty(new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01")));
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
  DateTimeSerializerBase<String> serializer = new TestDateTimeSerializer<>(String.class, Boolean.FALSE, null);
  JsonNode schema = serializer.getSchema(null, ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class));
  assertEquals("string", schema.getClass().getSimpleName());
  assertTrue(schema.isTextual());
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
  ObjectMapper mapper = ObjectMapper.getDefaultInstance();
  DateTimeSerializerBase<String> serializer = new TestDateTimeSerializer<>(String.class, Boolean.FALSE, null);
  JsonFormat.Value format = new JsonFormat.Value();
  format.setShape(JsonFormat.Shape.STRING);
  format.setLocale(Locale.US);
  JsonFormat.Value format2 = new JsonFormat.Value();
  format2.setShape(JsonFormat.Shape.STRING);
  format2.setPattern("yyyy-MM-dd HH:mm:ss");
  JsonFormat.Value format3 = new JsonFormat.Value();
  JsonFormat.Shape shape = JsonFormat.Shape.DATE;
  format3.setShape(shape);
  format3.setTimeZone(TimeZone.getDefault());

  JsonSerializer<String> serialized = serializer.createContextual(mapper.getSerializerProvider(), null);
  assertEquals(serializer.getClass(), serialized.getClass());

  JsonSerializer<String> serializedFormat = serializer.createContextual(mapper.getSerializerProvider(), new JsonProperty(""));
  assertEquals(serializer.getClass(), serializedFormat.getClass());

  JsonSerializer<String> serializedFormatUs = serializer.createContextual(mapper.getSerializerProvider(), new JsonProperty(""));
  serializedFormatUs.acceptJsonFormatVisitor(mapper.getSerializerProvider().findContextualValueSerializer(JsonType. constructType(String.class), new StandardSerializerFactory(mapper, null, StdDateFormat.class)));
  assertEquals(shape, serializedFormatUs.getShape());
  assertTrue(serializedFormatUs.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));

  JsonSerializer<String> serializedFormatCust = serializer.createContextual(mapper.getSerializerProvider(), new JsonProperty(""));
  serializedFormatCust.acceptJsonFormatVisitor(mapper.getSerializerProvider().findContextualValueSerializer(JsonType.constructType(String.class), new StandardSerializerFactory(mapper, null, StdDateFormat.class)));
  assertEquals(format2.getPattern(), serializedFormatCust.getSerializationConfig().getFormatValues().get(0).getPattern());
  assertEquals(format2.getLocale(), serializedFormatCust.getSerializationConfig().getFormatValues().get(0).getLocale());
  assertEquals(format2.getTimeZone(), serializedFormatCust.getSerializationConfig().getFormatValues().get(0).getTimeZone());

  JsonSerializer<String> serializedFormatCustomUTC = serializer.createContextual(mapper.getSerializerProvider(), new JsonProperty(""));
  serializedFormatCustomUTC.acceptJsonFormatVisitor(mapper.getSerializerProvider().findContextualValueSerializer(JsonType.constructType(String.class), new StandardSerializerFactory(mapper, null, StdDateFormat.class)));
  assertEquals(format3.getShape(), serializedFormatCustomUTC.getShape());
  assertEquals(format3.getTimeZone(), serializedFormatCustomUTC.getSerializationConfig().getFormatValues().get(0).getTimeZone());
 }

 @Test
 public void testSerialize() throws IOException {
  ObjectMapper mapper = ObjectMapper.getDefaultInstance();
  DateTimeSerializerBase<String> serializer = new TestDateTimeSerializer<>(String.class, Boolean.FALSE, null);
  JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  String dateString = "2022-01-01 12:00:00";
  Object value = format.parse(dateString);
  serializer.serialize(value, gen, mapper.getSerializerProvider());
  assertEquals("\"2022-01-01 12:00:00\"", gen.getOutputSize());
  gen.close();

  String empty = null;
  serializer.serialize(empty, gen, mapper.getSerializerProvider());
  assertEquals("null", gen.getOutputSize());
  gen.close();
 }

 @Test(expected = IOException.class)
 public void testSerializeWithNullFormat() throws IOException {
  ObjectMapper mapper = ObjectMapper.getDefaultInstance();
  DateTimeSerializerBase<String> serializer = new TestDateTimeSerializer<>(String.class, Boolean.FALSE, null);
  JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
  serializer.serialize(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-01-01 12:00:00"), gen, mapper.getSerializerProvider());
  serializer.serialize(null, gen, mapper.getSerializerProvider());
  gen.flush();
  gen.close();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeWithNullValue() throws IOException {
  ObjectMapper mapper = ObjectMapper.getDefaultInstance();
  DateTimeSerializerBase<String> serializer = new TestDateTimeSerializer<>(String.class, Boolean.FALSE, null);
  JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
  serializer.serialize(null, gen, mapper.getSerializerProvider());
  gen.close();
 }

 @Test
 public void testWithFormat() {
  DateTimeSerializerBase<Date> serializer = new TestDateTimeSerializer<>(Date.class, Boolean.FALSE, null);
  DateTimeSerializerBase<Date> serializerTimestamp = serializer.withFormat(Boolean.TRUE, null);
  DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  DateTimeSerializerBase<Date> serializerCustom = serializer.withFormat(Boolean.FALSE, customFormat);

  assertEquals(serializer.getClass(), serializer.getClass());
  assertNotEquals(serializer.getClass(), serializerTimestamp.getClass());
  assertNotEquals(serializer.getClass(), serializerCustom.getClass());

  assertEquals(null, serializerTimestamp.getCustomFormat());
  assertNull(serializerCustom.getCustomFormat());
  assertEquals(customFormat, serializerCustom.getCustomFormat());
 }
}