package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.test.BaseTest;

public class DateTimeSerializerBaseTest extends BaseTest {

 private DateTimeSerializerBase<Long> dateTimeSerializer;
 private SerializerProvider serializers;
 private JsonGenerator gen;
 private JsonParser parser;

 @Before
 public void setup() {
 serializers = new StandardSerializerFinder().findValueSerializer(Long.class, null);
 dateTimeSerializer = new StdDateSerializer(Long.class, null, null);
 gen = mock(JsonGenerator.class);
 parser = mock(JsonParser.class);
 }

 @Test
 public void testCreateContextual_DefaultFormat() throws IOException {
 ObjectCodec codec = new ObjectMapper().getSerializerProvider().getDefaultInstance();
 BeanProperty property = mock(BeanProperty.class);
 JsonFormat.Value format = mock(JsonFormat.Value.class);
 when(format.getShape()).thenReturn(JsonFormat.Shape.DEFAULT);
 when(format.getPattern()).thenReturn(null);
 when(format.getTimeZone()).thenReturn(null);
 when(format.getLocale()).thenReturn(Locale.US);
 when(serializers.getAnnotationIntrospector().findFormat((Annotated) property)).thenReturn(format);

 JsonSerializer<?> serializer = dateTimeSerializer.createContextual(serializers, property);
 assertSame(dateTimeSerializer, serializer);
 }

 @Test
 public void testCreateContextual_NumericFormat() throws IOException {
 ObjectCodec codec = new ObjectMapper().getSerializerProvider().getDefaultInstance();
 BeanProperty property = mock(BeanProperty.class);
 JsonFormat.Value format = mock(JsonFormat.Value.class);
 when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);
 when(format.getPattern()).thenReturn(null);
 when(format.getTimeZone()).thenReturn(null);
 when(format.getLocale()).thenReturn(Locale.US);
 when(serializers.getAnnotationIntrospector().findFormat((Annotated) property)).thenReturn(format);

 JsonSerializer<?> serializer = dateTimeSerializer.createContextual(serializers, property);
 assertTrue(serializer instanceof StdDateSerializer.NumberSerializer);
 }

 @Test
 public void testCreateContextual_StringFormat() throws IOException {
 ObjectCodec codec = new ObjectMapper().getSerializerProvider().getDefaultInstance();
 BeanProperty property = mock(BeanProperty.class);
 JsonFormat.Value format = mock(JsonFormat.Value.class);
 when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
 TimeZone tz = mock(TimeZone.class);
 final String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
 Locale loc = Locale.US;
 SimpleDateFormat df = new SimpleDateFormat(pattern, loc);
 df.setTimeZone(tz);
 when(format.getTimeZone()).thenReturn(tz);
 when(format.getPattern()).thenReturn(pattern);
 when(format.getLocale()).thenReturn(loc);
 when(serializers.getAnnotationIntrospector().findFormat((Annotated) property)).thenReturn(format);

 JsonSerializer<?> serializer = dateTimeSerializer.createContextual(serializers, property);
 assertEquals(dateTimeSerializer.class, serializer.getClass());
 DateTimeSerializerBase<?> serializerWithFormat = (DateTimeSerializerBase<?>) serializer;
 assertEquals(pattern, serializerWithFormat._customFormat.toPattern());
 assertEquals(tz, serializerWithFormat._customFormat.getTimeZone());
 }

 @Test
 public void testCreateContextual_NullFormat() throws IOException {
 ObjectCodec codec = new ObjectMapper().getSerializerProvider().getDefaultInstance();
 BeanProperty property = mock(BeanProperty.class);
 JsonFormat.Value format = mock(JsonFormat.Value.class);
 when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
 when(format.getPattern()).thenReturn(null);
 when(format.getTimeZone()).thenReturn(null);
 when(format.getLocale()).thenReturn(Locale.US);
 when(serializers.getAnnotationIntrospector().findFormat((Annotated) property)).thenReturn(format);

 JsonSerializer<?> serializer = dateTimeSerializer.createContextual(serializers, property);
 assertEquals(dateTimeSerializer.class, serializer.getClass());
 }

 @Test
 public void testSerialize_EmptyDate() throws IOException {
 Long value = null;
 when(parser.getValueAsLong()).thenReturn(0L);
 dateTimeSerializer.serialize(value, gen, serializers);
 assertEquals("0", gen.getOutputValueAsString());
 }

 @Test
 public void testSerialize_CustomFormat() throws IOException {
 Long value = 123L;
 when(parser.getValueAsLong()).thenReturn(value.longValue());
 long timestamp = value.longValue();
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
 DateTimeSerializerBase<Long> serializerWithFormat = dateTimeSerializer.withFormat(Boolean.FALSE, customFormat);
 when(gen.getSerializerProvider().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
 when(gen.getOutputValueAsString()).thenReturn("test");

 serializerWithFormat.serialize(value, gen, serializers);
 assertEquals("\"2023-03-23T12:34:56Z\"", gen.getOutputValueAsString());
 }

 @Test
 public void testSerialize_UseTimestamp() throws IOException {
 Long value = 123L;
 when(parser.getValueAsLong()).thenReturn(value.longValue());
 long timestamp = value.longValue();
 DateTimeSerializerBase<Long> serializerWithFormat = dateTimeSerializer.withFormat(Boolean.TRUE, null);
 when(gen.getSerializerProvider().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);
 when(gen.getOutputValueAsString()).thenReturn("test");

 serializerWithFormat.serialize(value, gen, serializers);
 assertEquals("1679459230000", gen.getOutputValueAsString());
 }

 @Test
 public void testSerialize_EmptyArray() throws IOException {
 Long[] values = new Long[0];
 when(parser.getValueAsLong()).thenReturn(0L);
 dateTimeSerializer.serialize(values, gen, serializers);
 assertEquals("[]", gen.getOutputValueAsString());
 }

 @Test
 public void testSerialize_Array() throws IOException {
 Long[] values = {1L, 2L, 3L};
 when(parser.getValueAsLong()).thenReturn(values[0].longValue());
 long timestamp = values[0].longValue();
 DateTimeSerializerBase<Long> serializerWithFormat = dateTimeSerializer.withFormat(Boolean.FALSE, null);
 when(gen.getSerializerProvider().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
 when(gen.getOutputString().length()).thenReturn(10);

 serializerWithFormat.serialize(values, gen, serializers);
 String expectedOutput = "[\"2023-03-23T12:34:56Z\",\"2023-03-23T13:34:56Z\",\"2023-03-23T14:34:56Z\"]";
 assertEquals(expectedOutput, gen.getOutputValueAsString());
 }
}