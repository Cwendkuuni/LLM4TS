package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.JsonSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private static class TestDateTimeSerializer<T> extends DateTimeSerializerBase<T> {
 
  public TestDateTimeSerializer(Class<T> type, boolean useTimestamp, DateFormat customFormat) {
    super(type, useTimestamp, customFormat);
  }

  @Override
  protected long _timestamp(T value) {
    throw new UnsupportedOperationException();
  }
 }

 @SuppressWarnings("unchecked")
 private static <T> TestDateTimeSerializer<T> createTestSerializer(Class<T> type, boolean useTimestamp, DateFormat customFormat) {
  return new TestDateTimeSerializer<>(type, useTimestamp, customFormat);
 }

 @org.junit.Test
 public void testCreateContextual() throws IOException, JsonProcessingException {
  // Test serializer creation with no format provided
  JsonSerializerFactory factory = mock(JsonSerializerFactory.class);
  SerializerProvider serializers = mock(SerializerProvider.class);
  BeanProperty property = mock(BeanProperty.class);
  TestDateTimeSerializer<Object> serializer = createTestSerializer(Object.class, false, null);

  when(factory.createContextual(any(SerializerProvider.class), any(BeanProperty.class)))
      .thenReturn(serializer);

  ObjectMapper mapper = new ObjectMapper();
  mapper.setSerializerFactory(factory);
  mapper.writeValueAsString(new Object());

  // Test serializer creation with format provided
  DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  when(factory.createContextual(serializers, property))
      .thenReturn(createTestSerializer(Object.class, false, format));

  JsonNode node = mapper.readTree("{\"format\":{\"shape\":\"STRING\",\"pattern\":\"yyyy-MM-dd HH:mm:ss\",\"timeZone\":null,\"locale\":\"en\"}}");
  BeanPropertyDefinition def = JacksonAnnotationIntrospector.findPropertyDefinition(node, "format");

  JsonSerializer<Object> serializer = mapper.findValueSerializer(Object.class, def);

  assertEquals(format, serializer.getFormat());
 }

 @org.junit.Test
 public void testIsEmpty() throws IOException {
  TestDateTimeSerializer<Object> serializer = createTestSerializer(Object.class, false, null);

  assertNull(serializer.isEmpty(null));
  assertNull(serializer.isEmpty(serializer, null));
  assertNull(serializer.isEmpty(new Object()));
 }

 @org.junit.Test
 public void testGetSchema() throws JsonMappingException {
  TestDateTimeSerializer<Object> serializer = createTestSerializer(Object.class, false, null);

  assertEquals("number", serializer.getSchema(null, Object.class).getType().getClassName());
  assertEquals("string", serializer.getSchema(null, Object.class, true).getType().getClassName());
 }

 @org.junit.Test
 public void testSerialize() throws IOException {
  TestDateTimeSerializer<Long> serializer = createTestSerializer(Long.class, false, null);
  ObjectMapper mapper = new ObjectMapper();
  SerializerProvider serializers = mapper.getSerializerProvider();
  JsonGenerator gen = serializers.getJsonFactory().createGenerator(System.out);
  serializer.serialize(System.currentTimeMillis(), gen, serializers);
  gen.flush();

  TestDateTimeSerializer<String> serializerString = createTestSerializer(String.class, false, null);
  ObjectMapper mapperString = new ObjectMapper();
  SerializerProvider serializersString = mapperString.getSerializerProvider();
  serializerString.serialize("2023-03-05 10:15:20", serializersString.getJsonFactory().createGenerator(System.out), serializersString);
  serializersString.getJsonFactory().createGenerator(System.out).flush();
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void test_timestamp() throws IOException {
  TestDateTimeSerializer<Object> serializer = createTestSerializer(Object.class, false, null);
  serializer._timestamp(null);
 }

 @org.junit.Test
 public void test_asTimestamp() throws IOException {
  TestDateTimeSerializer<Object> serializer = createTestSerializer(Object.class, true, null);
  SerializerProvider serializers = mock(SerializerProvider.class);
  assertTrue(serializer._asTimestamp(serializers));

  TestDateTimeSerializer<Object> serializerString = createTestSerializer(Object.class, false, null);
  assertFalse(serializerString._asTimestamp(serializers));
 }

 @org.junit.Test
 public void test_acceptJsonFormatVisitor() throws JsonMappingException {
  TestDateTimeSerializer<Object> serializer = createTestSerializer(Object.class, false, null);

  JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
  JavaType typeHint = mock(JavaType.class);
  when(visitor.getProvider()).thenReturn(mock(SerializerProvider.class));
  when(visitor.getProvider().isEnabled(any(SerializationFeature.class)))
      .thenReturn(true);

  serializer._acceptJsonFormatVisitor(visitor, typeHint, false);

  assertEquals("string", visitor.getValueFormat().getShape().asString());
  assertEquals("DATE_TIME", visitor.getValueFormat().getFormatValue().getStyle().asString());
 }
}