package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.test.BaseTest;

@SuppressWarnings("unchecked")
public class DateTimeSerializerBaseTest extends BaseTest {

 private static final Class<Date> DATE_CLASS = Date.class;

 // Test double instance of DateTimeSerializerBase
 class TestDateTimeSerializerBase extends DateTimeSerializerBase<Date> {
  protected long _timestamp(Date date) {
   return date.getTime();
  }
 }

 // Test instance of DateTimeSerializerBase with custom format
 class CustomFormatDateTimeSerializerBase extends DateTimeSerializerBase<Date> {
  public CustomFormatDateTimeSerializerBase() {
   super(DATE_CLASS, Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy hh:mm a"));
  }

  @Override
  public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
   return (DateTimeSerializerBase<Date>) this;
  }
 }

 @Before
 public void setUp() throws Exception {
  _objectMapper = new ObjectMapper();
 }

 // Test method: DateTimeSerializerBase(Class<T>, Boolean useTimestamp, DateFormat customFormat)
 @Test
 public void test_DateTimeSerializerBase() {
  TestDateTimeSerializerBase serializer1 = new TestDateTimeSerializerBase();
  TestDateTimeSerializerBase serializer2 = new TestDateTimeSerializerBase();
  assertNotSame(serializer1, serializer2);

  // Test with no arguments
  DateTimeSerializerBase<Date> serializer3 = new DateTimeSerializerBase<Date>();
  assertEquals(Boolean.FALSE, serializer3._useTimestamp);
  assertNull(serializer3._customFormat);
 }

 // Test method: withFormat(Boolean timestamp, DateFormat customFormat)
 @Test
 public void test_withFormat_Boolean_DateFormat() {
  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();

  // Test null timestamp and null customFormat
  DateTimeSerializerBase<Date> serializer1 = serializer.withFormat(null, null);
  assertEquals(serializer, serializer1);

  // Test null timestamp and non-null customFormat
  DateTimeSerializerBase<Date> serializer2 = serializer.withFormat(null, new SimpleDateFormat("dd-MM-yyyy hh:mm a"));
  assertEquals(serializer, serializer2);

  // Test non-null timestamp and null customFormat
  DateTimeSerializerBase<Date> serializer3 = serializer.withFormat(Boolean.TRUE, null);
  assertEquals(Boolean.TRUE, serializer3._useTimestamp);
  assertNull(serializer3._customFormat);

  // Test non-null timestamp and non-null customFormat
  DateTimeSerializerBase<Date> serializer4 = serializer.withFormat(Boolean.TRUE, new SimpleDateFormat("dd-MM-yyyy hh:mm a"));
  assertEquals(Boolean.TRUE, serializer4._useTimestamp);
  assertEquals(serializer4._customFormat, new SimpleDateFormat("dd-MM-yyyy hh:mm a"));
 }

 // Test method: createContextual(SerializerProvider serializers, BeanProperty property)
 @Test
 public void test_createContextual() throws IOException, JsonMappingException {
  JsonFormat.Value format = new JsonFormat.Value();
  format.setShape(JsonFormat.Shape.STRING);
  format.setPattern("dd-MM-yyyy hh:mm a");
  format.setTimeZone(TimeZone.getDefault());
  format.setLocale(Locale.getDefault());
  Annotated mockedProperty = mock(Annotated.class);
  when(mockedProperty.getMember()).thenReturn(null);
  when(mockedProperty.getValue()).thenReturn(null);
  when(mockedProperty.getAnnotation(JsonFormat.class)).thenReturn(format);

  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();
  JsonSerializer<Date> contextualSerializer = serializer.createContextual(_objectMapper.getSerializerProvider(), mockedProperty);
  SimpleDateFormat expectedDF = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
  assertEquals(expectedDF, contextualSerializer.getFormat());
  assertEquals(Boolean.FALSE, contextualSerializer._useTimestamp);
 }

 // Test method: isEmpty(T value)
 @Test
 public void test_isEmpty() {
  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();
  assertTrue(serializer.isEmpty(null));
  assertTrue(serializer.isEmpty(new Date(0)));
  assertFalse(serializer.isEmpty(new Date()));
 }

 // Test method: isEmpty(SerializerProvider serializers, T value)
 @Test
 public void test_isEmpty_SerializerProvider() {
  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();
  assertTrue(serializer.isEmpty(_objectMapper.getSerializerProvider(), null));
  assertTrue(serializer.isEmpty(_objectMapper.getSerializerProvider(), new Date(0)));
  assertFalse(serializer.isEmpty(_objectMapper.getSerializerProvider(), new Date()));
 }

 // Test method: _timestamp(T value)
 @Test
 public void test__timestamp() {
  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();
  long timestamp = 1234567890L;
  Date date = new Date(timestamp);
  assertEquals(timestamp, serializer._timestamp(date));
 }

 // Test method: getSchema(SerializerProvider serializers, Type typeHint)
 @Test
 public void test_getSchema() throws JsonProcessingException {
  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();
  JsonNode schema = serializer.getSchema(_objectMapper.getSerializerProvider(), DATE_CLASS);
  assertEquals("number", schema.getClass().getSimpleName());
 }

 // Test method: acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
 @Test
 public void test_acceptJsonFormatVisitor() throws JsonMappingException {
  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();
  JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
  when(visitor.getProvider()).thenReturn(_objectMapper.getSerializerProvider());
  when(visitor.getTypeHint()).thenReturn(DATE_CLASS);

  serializer._acceptJsonFormatVisitor(visitor, DATE_CLASS, Boolean.TRUE);
  verify(visitor).visitIntFormat(any(JsonFormatVisitor.IntFormatVisitor.class), eq(DATE_CLASS), eq(JsonParser.NumberType.LONG), eq(JsonValueFormat.UTC_MILLISEC));

  serializer._acceptJsonFormatVisitor(visitor, DATE_CLASS, Boolean.FALSE);
  verify(visitor).visitStringFormat(any(JsonFormatVisitor.StringFormatVisitor.class), eq(DATE_CLASS), eq(JsonValueFormat.DATE_TIME));
 }

 // Test method: serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException
 @Test
 public void test_serialize() throws IOException {
  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();
  ByteArrayOutputStream output = new ByteArrayOutputStream();
  JsonGenerator gen = _objectMapper.getFactory().createGenerator(output, JsonEncoding.UTF8);
  Date date = new Date();

  serializer.serialize(date, gen, _objectMapper.getSerializerProvider());
  gen.flush();
  String expectedOutput = "\"2023-03-31T13:24:56.789Z\"";
  assertEquals(expectedOutput, output.toString());
 }

 // Test edge cases and exception handling
 @Test(expected = IllegalArgumentException.class)
 public void test_nullProvider() throws IOException {
  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();
  serializer.createContextual(null, null);
 }

 @Test(expected = JsonMappingException.class)
 public void test_missingFormatAnnotatedProperty() throws IOException, JsonMappingException {
  JsonFormat.Value format = new JsonFormat.Value();
  Annotated mockedProperty = mock(Annotated.class);
  when(mockedProperty.getMember()).thenReturn(null);
  when(mockedProperty.getValue()).thenReturn(null);
  when(mockedProperty.getAnnotation(JsonFormat.class)).thenReturn(format);
  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();
  serializer.createContextual(_objectMapper.getSerializerProvider(), mockedProperty);
 }

 @Test(expected = JsonMappingException.class)
 public void test_invalidFormat() throws IOException, JsonMappingException {
  JsonFormat.Value format = new JsonFormat.Value();
  format.setShape(JsonFormat.Shape.STRING);
  format.setPattern("foo");
  Annotated mockedProperty = mock(Annotated.class);
  when(mockedProperty.getMember()).thenReturn(null);
  when(mockedProperty.getValue()).thenReturn(null);
  when(mockedProperty.getAnnotation(JsonFormat.class)).thenReturn(format);
  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();
  serializer.createContextual(_objectMapper.getSerializerProvider(), mockedProperty);
 }

 @Test(expected = JsonMappingException.class)
 public void test_missingCustomFormat() throws IOException, JsonMappingException {
  JsonFormat.Value format = new JsonFormat.Value();
  format.setShape(JsonFormat.Shape.STRING);
  format.setPattern(null);
  Annotated mockedProperty = mock(Annotated.class);
  when(mockedProperty.getMember()).thenReturn(null);
  when(mockedProperty.getValue()).thenReturn(null);
  when(mockedProperty.getAnnotation(JsonFormat.class)).thenReturn(format);
  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();
  serializer.createContextual(_objectMapper.getSerializerProvider(), mockedProperty);
 }

 @Test(expected = JsonMappingException.class)
 public void test_invalidCustomFormat() throws IOException, JsonMappingException {
  JsonFormat.Value format = new JsonFormat.Value();
  format.setShape(JsonFormat.Shape.STRING);
  format.setPattern("dd-MM-yyyy hh:mm aZ");
  Annotated mockedProperty = mock(Annotated.class);
  when(mockedProperty.getMember()).thenReturn(null);
  when(mockedProperty.getValue()).thenReturn(null);
  when(mockedProperty.getAnnotation(JsonFormat.class)).thenReturn(format);
  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();
  serializer.createContextual(_objectMapper.getSerializerProvider(), mockedProperty);
 }

 @Test(expected = JsonMappingException.class)
 public void test_invalidTimeZone() throws IOException, JsonMappingException {
  JsonFormat.Value format = new JsonFormat.Value();
  format.setShape(JsonFormat.Shape.STRING);
  format.setPattern("dd-MM-yyyy hh:mm a");
  format.setTimeZone(null);
  Annotated mockedProperty = mock(Annotated.class);
  when(mockedProperty.getMember()).thenReturn(null);
  when(mockedProperty.getValue()).thenReturn(null);
  when(mockedProperty.getAnnotation(JsonFormat.class)).thenReturn(format);
  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();
  serializer.createContextual(_objectMapper.getSerializerProvider(), mockedProperty);
 }

 @Test(expected = JsonMappingException.class)
 public void test_invalidLocale() throws IOException, JsonMappingException {
  JsonFormat.Value format = new JsonFormat.Value();
  format.setShape(JsonFormat.Shape.STRING);
  format.setPattern("dd-MM-yyyy hh:mm a");
  format.setLocale(new Locale("XX", "XX"));
  Annotated mockedProperty = mock(Annotated.class);
  when(mockedProperty.getMember()).thenReturn(null);
  when(mockedProperty.getValue()).thenReturn(null);
  when(mockedProperty.getAnnotation(JsonFormat.class)).thenReturn(format);
  TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase();
  serializer.createContextual(_objectMapper.getSerializerProvider(), mockedProperty);
 }

 @Test
 public void test_serializeWithCustomFormat() throws IOException {
  CustomFormatDateTimeSerializerBase serializer = new CustomFormatDateTimeSerializerBase();
  ByteArrayOutputStream output = new ByteArrayOutputStream();
  JsonGenerator gen = _objectMapper.getFactory().createGenerator(output, JsonEncoding.UTF8);
  Date date = new Date();

  SimpleDateFormat expectedDF = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
  expectedDF.setTimeZone(TimeZone.getDefault());
  expectedDF.setLocale(Locale.getDefault());

  serializer.serialize(date, gen, _objectMapper.getSerializerProvider());
  gen.flush();
  String expectedOutput = expectedDF.format(date);
  assertEquals(expectedOutput, output.toString());
 }
}