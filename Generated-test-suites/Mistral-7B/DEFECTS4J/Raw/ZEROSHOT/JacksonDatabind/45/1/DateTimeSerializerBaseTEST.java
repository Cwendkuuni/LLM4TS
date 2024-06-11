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
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonFormat.Shape;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.Typing;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> longSerializer;
 private DateTimeSerializerBase<String> stringSerializer;

 @Before
 public void setup() {
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
 longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null) {
 @Override
 protected long _timestamp(Long value) {
 return value;
 }
 };
 stringSerializer = new DateTimeSerializerBase<String>(String.class, false, customFormat) {
 @Override
 protected long _timestamp(String value) {
 try {
 return customFormat.parse(value).getTime();
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }
 };
 }

 @Test
 public void testDefaultSerialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 long dateValue = 1639146223000L; // Mon Mar 13 15:27:03 UTC 2023

 JsonSerializer<Long> serializer = mapper.findValueSerializer(Long.class);
 assertEquals(mapper.writeValueAsString(dateValue),
 serializer.serialize(dateValue, new JsonGenerator(mapper.getOutputFactory()), mapper.getSerializerProvider()));

 assertEquals(mapper.writeValueAsString(dateValue),
 longSerializer.serialize(dateValue, new JsonGenerator(mapper.getOutputFactory()), mapper.getSerializerProvider()));
 }

 @Test
 public void testSerializationWithFormat() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 SerializerProvider serializers = mapper.getDefaultSerializerProvider();
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setPattern("yyyy-MM-dd HH:mm:ss z");

 BeanProperty property = new BeanProperty("testProperty", Long.class);
 Annotated annotated = new Annotated(property);
 annotated.setValue(format);

 JsonSerializer<Long> serializer = longSerializer.withFormat(Boolean.FALSE, format);
 JsonNode expectedNode = Json.valueNode("2023-03-13 15:27:03 +0000");

 JsonParser jsonParser = mapper.getFactory().createParser(expectedNode.toString());
 Long parsedValue = mapper.readValue(jsonParser, Long.class);

 assertEquals(mapper.writeValueAsString(parsedValue),
 serializer.serialize(parsedValue, new JsonGenerator(mapper.getOutputFactory()), serializers));
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidFormat() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 SerializerProvider serializers = mapper.getDefaultSerializerProvider();
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setPattern("InvalidPattern");

 BeanProperty property = new BeanProperty("testProperty", Long.class);
 Annotated annotated = new Annotated(property);
 annotated.setValue(format);

 longSerializer.withFormat(Boolean.FALSE, format).serialize(null, new JsonGenerator(mapper.getOutputFactory()), serializers);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithNullFormat() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 SerializerProvider serializers = mapper.getDefaultSerializerProvider();
 JsonFormat.Value format = null;

 BeanProperty property = new BeanProperty("testProperty", Long.class);
 Annotated annotated = new Annotated(property);
 annotated.setValue(format);

 longSerializer.withFormat(Boolean.FALSE, format).serialize(null, new JsonGenerator(mapper.getOutputFactory()), serializers);
 }

 @Test
 public void testDeserialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String jsonString = "\"2023-03-13 15:27:03 +0000\"";
 Long deserializedValue = mapper.readValue(jsonString, Long.class);

 assertEquals(deserializedValue.longValue(),
 longSerializer.deserialize(jsonString, new JsonParser(mapper.getFactory()), mapper.getSerializerProvider()).longValue());
 }

 @Test
 public void testDeserializationWithJsonFormatAnnotation() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setPattern("yyyy-MM-dd HH:mm:ss z");
 JsonFormat jsonFormat = new JsonFormat(format);
 String jsonString = "\"2023-03-13 15:27:03 +0000\"";

 BeanProperty property = new BeanProperty("testProperty", Long.class);
 property.setAnnotation(new JsonFormat(format));
 Annotated annotated = new Annotated(property);
 annotated.setValue(format);

 JsonSerializer<Long> serializer = longSerializer.withFormat(Boolean.FALSE, format);
 Long deserializedValue = mapper.readValue(jsonString, Long.class);

 BeanProperty deserializedProperty = mapper.getTypeFactory().constructValueType(Long.class, "java.lang.Object", new Class[] { JsonFormat.class });
 Object deserializedObject = mapper.readValue(jsonString, deserializedProperty);

 JsonFormat actualFormat = AnnotationIntrospector.findFormat((Annotated)deserializedProperty.getMember());
 assertEquals(format, actualFormat);
 assertEquals(deserializedValue.longValue(),
 deserializedObject.getClass().getMethod("value").invoke(deserializedObject).longValue());
 assertEquals(deserializedValue,
 serializer.deserialize(jsonString, new JsonParser(mapper.getFactory()), mapper.getSerializerProvider()));
 }

 @Test
 public void testDeserializationWithCustomDateFormat() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
 String jsonString = "\"2023-03-13 15:27:03 +0000\"";

 JsonSerializer<String> serializer = stringSerializer;
 String deserializedValue = mapper.readValue(jsonString, String.class);

 assertEquals(deserializedValue, serializer.deserialize(jsonString, new JsonParser(mapper.getFactory()), mapper.getSerializerProvider()));
 }

 @Test
 public void testSerializationWithTimestamp() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

 JsonSerializer<Long> serializer = longSerializer.withFormat(Boolean.TRUE, null);
 assertEquals("1639146223000", mapper.writeValueAsString(1639146223000L));
 assertEquals("1639146223000", serializer.serialize(1639146223000L, new JsonGenerator(mapper.getOutputFactory()), mapper.getSerializerProvider()).toString());
 }
}