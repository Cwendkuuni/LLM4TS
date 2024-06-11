package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.test.BaseTest;

public class DateTimeSerializerBaseTest extends BaseTest {

 private static class TestDateTimeSerializer extends DateTimeSerializerBase<Date> {
  TestDateTimeSerializer() {
   super(Date.class, false, null);
  }

  @Override
  public JsonSerializer<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
   return new TestDateTimeSerializerWithFormat(this, timestamp, customFormat);
  }
 }

 private static class TestDateTimeSerializerWithFormat extends DateTimeSerializerBase<Date> {
  private final DateTimeSerializerBase<Date> _base;
  private final Boolean _useTimestamp;
  private final DateFormat _customFormat;

  TestDateTimeSerializerWithFormat(DateTimeSerializerBase<Date> base, Boolean useTimestamp, DateFormat customFormat) {
   super(Date.class, useTimestamp, customFormat);
   _base = base;
   _useTimestamp = useTimestamp;
   _customFormat = customFormat;
  }
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testCreateContextual() throws Exception {
  ObjectMapper mapper = new ObjectMapper();

  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
  JsonFormat jsonFormat = new JsonFormat.Builder()
      .shape(JsonFormat.Shape.STRING)
      .timeZone(TimeZone.getTimeZone("UTC"))
      .locale(Locale.US)
      .pattern("yyyy-MM-dd HH:mm:ss.SSS z")
      .build();

  BeanProperty property = mock(BeanProperty.class);
  when(property.getMember()).thenReturn(mock(Field.class));

  JsonSerializer<Date> serializer = _base.createContextual(mapper.getSerializerProvider(), property);

  assertTrue(serializer instanceof TestDateTimeSerializerWithFormat);
  TestDateTimeSerializerWithFormat testSerializer = (TestDateTimeSerializerWithFormat) serializer;
  assertEquals(JsonFormat.Shape.STRING, testSerializer._useTimestamp);
  assertEquals(dateFormat, testSerializer._customFormat);

  JsonFormat format = mapper.getAnnotationIntrospector().findFormat((Annotated)property.getMember());
  assertSame(jsonFormat, format);
 }

 @org.junit.Test
 public void testSerializeNull() throws Exception {
  ObjectMapper mapper = new ObjectMapper();
  TestDateTimeSerializer serializer = new TestDateTimeSerializer();

  JsonGenerator gen = mock(JsonGenerator.class);
  SerializerProvider serializers = mock(SerializerProvider.class);

  serializer.serialize(null, gen, serializers);
  verify(gen).writeNull();
 }

 @org.junit.Test
 public void testSerializeEmpty() throws Exception {
  ObjectMapper mapper = new ObjectMapper();
  TestDateTimeSerializer serializer = new TestDateTimeSerializer();

  Date date = Calendar.getInstance().getTime();
  Calendar calendar = Calendar.getInstance();
  calendar.setTime(date);
  calendar.add(Calendar.MILLISECOND, -1);
  Date emptyDate = calendar.getTime();

  JsonGenerator gen = mock(JsonGenerator.class);
  SerializerProvider serializers = mock(SerializerProvider.class);

  serializer.serialize(emptyDate, gen, serializers);
  verify(gen).writeNumber(eq(0L));
 }

 @org.junit.Test
 public void testSerializeWithUseTimestamp() throws Exception {
  ObjectMapper mapper = new ObjectMapper();
  TestDateTimeSerializer serializer = new TestDateTimeSerializer();

  Date date = Calendar.getInstance().getTime();

  JsonGenerator gen = mock(JsonGenerator.class);
  SerializerProvider serializers = mock(SerializerProvider.class);

  serializer.serialize(date, gen, serializers);
  verify(gen).writeNumber(date.getTime());
 }

 @org.junit.Test
 public void testSerializeWithCustomFormat() throws Exception {
  ObjectMapper mapper = new ObjectMapper();
  TestDateTimeSerializer serializer = new TestDateTimeSerializer();

  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  TestDateTimeSerializerWithFormat serializerWithFormat = (TestDateTimeSerializerWithFormat) serializer.withFormat(false, dateFormat);

  Date date = Calendar.getInstance().getTime();

  JsonGenerator gen = mock(JsonGenerator.class);
  SerializerProvider serializers = mock(SerializerProvider.class);

  serializerWithFormat.serialize(date, gen, serializers);
  verify(gen).writeString(dateFormat.format(date));
 }

 @org.junit.Test(expected = JsonMappingException.class)
 public void testSerializeWithInvalidFormat() throws Exception {
  ObjectMapper mapper = new ObjectMapper();
  TestDateTimeSerializer serializer = new TestDateTimeSerializer();

  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS zzz");
  TestDateTimeSerializerWithFormat serializerWithFormat = (TestDateTimeSerializerWithFormat) serializer.withFormat(false, dateFormat);

  Date date = Calendar.getInstance().getTime();

  JsonGenerator gen = mock(JsonGenerator.class);
  SerializerProvider serializers = mock(SerializerProvider.class);

  serializerWithFormat.serialize(date, gen, serializers);
 }

 @org.junit.Test
 public void testGetSchema() throws Exception {
  ObjectMapper mapper = new ObjectMapper();
  TestDateTimeSerializer serializer = new TestDateTimeSerializer();

  JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Date.class);
  assertEquals("string", schema.asText());
 }
}