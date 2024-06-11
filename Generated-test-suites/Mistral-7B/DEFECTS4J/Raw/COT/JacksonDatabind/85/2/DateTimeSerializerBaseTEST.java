package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBaseTester;

public class DateTimeSerializerBaseTest extends DateTimeSerializerBaseTester {

 private DateTimeSerializerBase<Long> serializer;

 @Before
 public void setUp() throws Exception {
 serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
 }

 @Test
 public void testWithFormat() throws JsonProcessingException {
 DateTimeSerializerBase<Long> serializerWithFormat = serializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy"));
 assertNotSame(serializer, serializerWithFormat);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextualNullProperty() throws JsonProcessingException {
 serializer.createContextual(null, null);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testCreateContextualNullSerializerProvider() throws JsonProcessingException {
 JsonParser parser = createContextualParser();
 JsonGenerator generator = createContextualGenerator();
 serializer.createContextual(null, nullProperty(parser, Long.class, "test"));
 assertNull(serializer.createContextual(generator.getSerializerProvider(), nullProperty(generator, Long.class, "test")));
 }

 @Test
 public void testCreateContextualWithFormat() throws JsonProcessingException {
 JsonParser parser = createContextualParser();
 JsonGenerator generator = createContextualGenerator();
 JsonFormat.Value format = createFormat(false, "dd-MM-yyyy");
 ObjectCodec codec = JsonFactory.getInstance().getCodec();
 SerializerProvider serializers = new SerializerProvider();
 serializers.setCodec(codec);
 JsonSerializer<Object> serializerWithFormat = serializer.createContextual(serializers, nullProperty(parser, Object.class, "test"));
 JsonSerializer<Long> serializerWithFormatForType = serializerWithFormat.withValueHandler(LongDeserializer.instance);
 assertNotNull(serializerWithFormatForType);
 assertEquals("dd-MM-yyyy", format.getPattern());
 assertEquals(JsonFormat.Shape.STRING, format.getShape());
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextualInvalidFormat() throws JsonProcessingException {
 JsonParser parser = createContextualParser();
 JsonGenerator generator = createContextualGenerator();
 SerializerProvider serializers = new SerializerProvider();
 JsonFormat.Value format = createFormat(false, "xx-XX-xxxx");
 serializer.createContextual(serializers, nullProperty(parser, Object.class, "test"));
 fail("Expected JsonMappingException");
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 Long value = null;
 JsonGenerator gen = createContextualGenerator();
 serializer.serialize(value, gen, new SerializerProvider());
 assertNull(gen.getCurrentValue());
 }

 @Test
 public void testSerializeZero() throws JsonProcessingException {
 Long value = 0L;
 JsonGenerator gen = createContextualGenerator();
 serializer.serialize(value, gen, new SerializerProvider());
 assertEquals("0", gen.getCurrentValueAsString());
 }

 @Test
 public void testSerializeTimestampTrue() throws JsonProcessingException {
 Long value = 1633054400000L; // Mon, 19 Jul 2021 12:00:00 GMT
 JsonGenerator gen = createContextualGenerator();
 serializer.serialize(value, gen, new SerializerProvider());
 assertEquals("1633054400000", gen.getCurrentValueAsString());
 }

 @Test
 public void testSerializeTimestampFalse() throws JsonProcessingException {
 Long value = 1633054400000L; // Mon, 19 Jul 2021 12:00:00 GMT
 JsonGenerator gen = createContextualGenerator();
 serializer.serialize(value, gen, new SerializerProvider(), new SimpleDateFormat("dd-MM-yyyy"));
 assertEquals("19-07-2021", gen.getCurrentValueAsString());
 }

 @Test
 public void testSerializeWithCustomDateFormat() throws JsonProcessingException {
 Long value = 1633054400000L; // Mon, 19 Jul 2021 12:00:00 GMT
 JsonGenerator gen = createContextualGenerator();
 serializer.serialize(value, gen, new SerializerProvider(), new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z"));
 assertEquals("19-07-2021 12:00:00 GMT", gen.getCurrentValueAsString());
 }

 @Test
 public void testSerializeWithLocale() throws JsonProcessingException {
 Locale locale = Locale.US;
 JsonGenerator gen = createContextualGenerator();
 serializer.serialize(1633054400000L, gen, new SerializerProvider(), new SimpleDateFormat("dd-MM-yyyy"), locale);
 assertEquals("07-19-2021", gen.getCurrentValueAsString());
 }

 @Test
 public void testSerializeWithTimeZone() throws JsonProcessingException {
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 JsonGenerator gen = createContextualGenerator();
 serializer.serialize(1633054400000L, gen, new SerializerProvider(), new SimpleDateFormat("dd-MM-yyyy z"), timeZone);
 assertEquals("07-19-2021 UTC", gen.getCurrentValueAsString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptySerializerProvider() throws JsonProcessingException {
 serializer.serialize(1633054400000L, createContextualGenerator().getSerializerProvider(), new SerializerProvider());
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 assertEquals("string", serializer.getSchema(new SerializerProvider(), Long.TYPE).getClass().getSimpleName());
 }

}