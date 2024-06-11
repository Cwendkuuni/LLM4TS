package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.test.BaseTest;

public class DateTimeSerializerBaseTest extends BaseTest {

 private static class TestDateTimeSerializerBase<T> extends DateTimeSerializerBase<T> {
 @Override
 public long _timestamp(T value) {
 throw new UnsupportedOperationException();
 }
 }

 @SuppressWarnings("unchecked")
 private TestDateTimeSerializerBase<Long> longSerializer = new TestDateTimeSerializerBase<Long>() {
 @Override
 protected long _timestamp(Long value) {
 return value.longValue();
 }
 };

 @SuppressWarnings("unchecked")
 private TestDateTimeSerializerBase<String> stringSerializer = new TestDateTimeSerializerBase<String>() {
 @Override
 protected long _timestamp(String value) {
 return Long.parseLong(value);
 }
 };

 @Test
 public void testConstructor() {
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, null);
 assertEquals(Long.TYPE, serializer.handledType());
 assertFalse(serializer._useTimestamp.booleanValue());
 assertNull(serializer._customFormat);
 }

 @Test
 public void testWithFormat() {
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, null);
 DateTimeSerializerBase<Long> serializedAsTimestamp = serializer.withFormat(Boolean.TRUE, null);
 assertTrue(serializedAsTimestamp._useTimestamp.booleanValue());
 assertNull(serializedAsTimestamp._customFormat);

 DateTimeSerializerBase<Long> serializedAsString = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));
 assertFalse(serializedAsString._useTimestamp.booleanValue());
 assertNotNull(serializedAsString._customFormat);
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 JsonSerializerProvider serializers = mapper.getSerializerProvider();

 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setPattern("dd-MM-yyyy");
 format.setLocale(Locale.US);
 format.setTimeZone(TimeZone.getTimeZone("UTC"));

 DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
 JsonSerializer<String> contextualSerializer = serializer.createContextual(serializers, null);
 JsonSerializer<String> expectedSerializer = stringSerializer;

 assertEquals(expectedSerializer.getClass(), contextualSerializer.getClass());
 assertSame(expectedSerializer, contextualSerializer);

 expectedSerializer = stringSerializer.withFormat(Boolean.FALSE, format.getFormat());
 contextualSerializer = serializer.createContextual(serializers, null);
 assertEquals(expectedSerializer.getClass(), contextualSerializer.getClass());
 assertSame(expectedSerializer, contextualSerializer);
}

 @Test(expected = IllegalArgumentException.class)
 public void testCreateContextualNullProvider() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 JsonSerializerProvider serializers = null;
 DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
 serializer.createContextual(serializers, null);
}

 @Test(expected = JsonMappingException.class)
 public void testSerializeNull() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 JsonSerializerProvider serializers = mapper.getSerializerProvider();
 JsonGenerator generator = writer.getJsonGenerator();
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, null);
 serializer.serialize(null, generator, serializers);
}

 @Test
 public void testSerializeInteger() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 JsonSerializerProvider serializers = mapper.getSerializerProvider();
 JsonGenerator generator = writer.getJsonGenerator();
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, null);
 long value = 1234567890L;
 serializer.serialize(value, generator, serializers);
 assertEquals("1234567890", generator.getOutputBank().toString());
}

 @Test
 public void testSerializeString() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 JsonSerializerProvider serializers = mapper.getSerializerProvider();
 JsonGenerator generator = writer.getJsonGenerator();
 DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
 String value = "12-Jun-2017";
 serializer.serialize(value, generator, serializers);
 assertEquals("\"12-Jun-2017\"", generator.getOutputBank().toString());
}

 @Test
 public void testSerializeCustomFormat() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 JsonSerializerProvider serializers = mapper.getSerializerProvider();
 JsonGenerator generator = writer.getJsonGenerator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", locale);
 dateFormat.setTimeZone(timeZone);
 DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, dateFormat);
 String value = "12-Jun-2017";
 serializer.serialize(value, generator, serializers);
 assertEquals("\"12-Jun-2017\"", generator.getOutputBank().toString());
}

 @Test
 public void testSerializeTimestamp() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 JsonSerializerProvider serializers = mapper.getSerializerProvider();
 JsonGenerator generator = writer.getJsonGenerator();
 SerializationConfig config = mapper.getSerializationConfig();
 config.setTimeZone("UTC");
 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.TRUE, null);
 long value = 1234567890123L;
 serializer.serialize(value, generator, serializers);
 assertEquals("1234567890123", generator.getOutputBank().toString());
}

 @Test
 public void testGetSchema() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectWriter writer = mapper.writer();
 JsonSerializerProvider serializers = mapper.getSerializerProvider();
 JsonNode schema = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, null).getSchema(serializers, Long.TYPE);
 assertEquals("number", schema.getClass().getSimpleName());
 assertTrue(schema.isNumberType());
}

}