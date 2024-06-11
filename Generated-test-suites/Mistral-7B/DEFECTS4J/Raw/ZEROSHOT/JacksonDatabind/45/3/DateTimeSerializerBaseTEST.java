package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private static final long TIMESTAMP_VALUE = 1516041234000L; // Mon Dec 11 07:37:14 UTC 2017

 @SuppressWarnings("unchecked")
 @org.junit.Before
 public void setup() {
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
 dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 DateFormat formattedDateFormat = mock(DateFormat.class);
 when(formattedDateFormat.format(dateFormat)).thenReturn("2017-12-11T07:37:14Z");

 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = mapper.findValueSerializer(DateTime.class);
 DateTimeSerializerBase<DateTime> dateTimeSerializer = (DateTimeSerializerBase<DateTime>) serializer;
 when(formattedDateFormat.parse(dateFormat.format(new Date(TIMESTAMP_VALUE)))).thenReturn(new Date(TIMESTAMP_VALUE));
 DateFormat dateFormatMock = mock(DateFormat.class);
 when(dateFormatMock.parse(org.junit.Assert.equals("2017-12-11T07:37:14Z", String.class))).thenReturn(new Date(TIMESTAMP_VALUE));

 ObjectMapper testMapper = new ObjectMapper();
 testMapper.setDateFormat(dateFormatMock);
 }

 @org.junit.Test
 public void testCreateContextual() throws IOException {
 JsonFormat<DateTime> jsonFormat = new JsonFormat.Builder<DateTime>()
         .shape(JsonFormat.Shape.STRING) // Date format shape: String
         .timeZone(TimeZone.getTimeZone("UTC"))
         .build();

 Annotated property = mock(Annotated.class);
 when(property.getMember()).thenReturn(null);
 when(property.getAnnotation(JsonFormat.class)).thenReturn(jsonFormat);

 SerializerProvider serializers = mock(SerializerProvider.class);
 ContextualSerializer contextualSerializer = dateTimeSerializer.createContextual(serializers, property);

 assertEquals(dateTimeSerializer, contextualSerializer);
 }

 @org.junit.Test
 public void testSerializeWithFormatString() throws IOException {
 DateTime value = new DateTime(TIMESTAMP_VALUE);
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider serializers = mock(SerializerProvider.class);
 when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);

 DateTimeSerializerBase<DateTime> dateTimeSerializer = (DateTimeSerializerBase<DateTime>) dateTimeSerializer;
 dateTimeSerializer.serialize(value, gen, serializers);

 String expectedJson = "\"2017-12-11T07:37:14Z\"";
 String actualJson = gen.getOutputContent().getString();
 assertEquals(expectedJson, actualJson);
 }

 @org.junit.Test
 public void testSerializeWithFormatCustom() throws IOException {
 DateTime value = new DateTime(TIMESTAMP_VALUE);
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider serializers = mock(SerializerProvider.class);

 DateTimeSerializerBase<DateTime> dateTimeSerializer = (DateTimeSerializerBase<DateTime>) dateTimeSerializer;
 dateTimeSerializer.serialize(value, gen, serializers);

 DateFormat customFormat = formattedDateFormat;
 JsonFormat<DateTime> jsonFormat = new JsonFormat.Builder<DateTime>()
         .format(customFormat)
         .build();

 ContextualSerializer contextualSerializer = dateTimeSerializer.createContextual(serializers, null);
 contextualSerializer.serialize(value, gen, serializers);

 String expectedJson = "\"2017-12-11T07:37:14Z\"";
 String actualJson = gen.getOutputContent().getString();
 assertEquals(expectedJson, actualJson);
 }

 @org.junit.Test
 public void testSerializeWithFormatCustomAndTimestamp() throws IOException {
 DateTime value = new DateTime(TIMESTAMP_VALUE);
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider serializers = mock(SerializerProvider.class);
 when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);

 DateTimeSerializerBase<DateTime> dateTimeSerializer = (DateTimeSerializerBase<DateTime>) dateTimeSerializer;
 dateTimeSerializer.serialize(value, gen, serializers);

 long expectedJson = TIMESTAMP_VALUE;
 long actualJson = gen.getOutputLong();
 assertEquals(expectedJson, actualJson);
 }

 @org.junit.Test
 public void testAcceptJsonFormatVisitor() throws IOException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 when(visitor.getProvider()).thenReturn(testMapper.getSerializerProvider());

 DateTimeSerializerBase<DateTime> dateTimeSerializer = (DateTimeSerializerBase<DateTime>) dateTimeSerializer;
 dateTimeSerializer.acceptJsonFormatVisitor(visitor, Object.class.getType());

 JsonSerializer<DateTime> expectedSerializer = (JsonSerializer<DateTime>) mapper.findValueSerializer(DateTime.class);
 JsonSerializer<? extends Object> actualSerializer = visitor.getSerializerProvider().getSerializerProvider().findValueSerializer(DateTime.class);
 assertEquals(expectedSerializer, actualSerializer);
 }
}