package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private static final long TEST_TIMESTAMP = 1234567890123L;
 private static final Date TEST_DATE = new Date(TEST_TIMESTAMP);
 private static final SimpleDateFormat TEST_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
 private static final TimeZone TEST_TIME_ZONE = TimeZone.getTimeZone("UTC");

 @SuppressWarnings("unchecked")
 @org.junit.Before
 public void setup() {
 SerializerProvider serializers = mock(SerializerProvider.class);
 ObjectCodec objectMapper = mock(ObjectCodec.class);
 when(serializers.getDefaultSerializerProvider()).thenReturn(objectMapper);
 when(serializers.findValueSerializer(JsonTypeFactory.booleanType())).thenReturn(new StdScalarSerializer(Boolean.TYPE));
 when(serializers.findValueSerializer(Date.class)).thenReturn(new DateTimeSerializerBase<Date>(Date.class, false, null));
 }

 @org.junit.Test
 public void testCreateContextualWithNoFormat() throws JsonMappingException, JsonProcessingException, IOException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 assertEquals(serializer, serializer.createContextual(mock(SerializerProvider.class), null));
 }

 @org.junit.Test
 public void testCreateContextualWithJsonFormat() throws JsonMappingException, JsonProcessingException, IOException {
 JsonFormat<Date> jsonFormat = new JsonFormat.Builder<Date>()
         .shape(JsonFormat.Shape.STRING)
         .timeZone(TEST_TIME_ZONE)
         .format("yyyy-MM-dd HH:mm:ss.SSS z")
         .build();
 Annotated annotated = mock(Annotated.class);
 when(annotated.getType()).thenReturn(Date.class);
 when(annotated.getValue()).thenReturn(TEST_DATE);
 when(serializers.getAnnotationIntrospector().findFormat((Annotated)annotated)).thenReturn(jsonFormat);

 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 assertEquals(new DateTimeSerializerBase<Date>(Date.class, false, TEST_FORMAT), serializer.createContextual(serializers, null));
 }

 @org.junit.Test
 public void testSerializeAsTimestamp() throws JsonMappingException, JsonProcessingException, IOException {
 SerializerProvider serializers = mock(SerializerProvider.class);
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.writeNumber(TEST_TIMESTAMP)).thenReturn(true);

 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 serializer.serialize(TEST_DATE, gen, serializers);
 verify(gen).writeNumber(TEST_TIMESTAMP);
 }

 @org.junit.Test
 public void testSerializeAsString() throws JsonMappingException, JsonProcessingException, IOException {
 SerializerProvider serializers = mock(SerializerProvider.class);
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.writeString(TEST_FORMAT.format(TEST_DATE))).thenReturn(true);

 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, TEST_FORMAT);
 serializer.serialize(TEST_DATE, gen, serializers);
 verify(gen).writeString(TEST_FORMAT.format(TEST_DATE));
 }

 @org.junit.Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 when(visitor.getProvider()).thenReturn(mock(SerializerProvider.class));
 when(visitor.getType().getRawClass()).thenReturn(Date.class);
 when(visitor.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);

 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 serializer._acceptJsonFormatVisitor(visitor, null, true);
 verify(visitor).visitIntFormat(any(JsonFormatVisitorWrapper.class), eq(Date.class), eq(JsonParser.NumberType.LONG), eq(JsonValueFormat.UTC_MILLISEC));

 serializer = new DateTimeSerializerBase<Date>(Date.class, false, TEST_FORMAT);
 serializer._acceptJsonFormatVisitor(visitor, null, false);
 verify(visitor).visitStringFormat(any(JsonFormatVisitorWrapper.class), eq(Date.class), eq(JsonValueFormat.DATE_TIME));
 }
}