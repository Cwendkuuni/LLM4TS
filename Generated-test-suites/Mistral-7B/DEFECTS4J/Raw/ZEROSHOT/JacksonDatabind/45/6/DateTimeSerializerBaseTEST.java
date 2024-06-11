package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonFormat.Shape;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.StdDateFormat;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.BeanPropertyIntrospector;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.type.JavaTypeFactory;

public class DateTimeSerializerBaseTest {

 private DateTimeFormatter formatter;
 private JsonParser parser;
 private JsonGenerator generator;
 private SerializerProvider serializers;
 private BeanPropertyIntrospector introspector;
 private JavaTypeFactory typeFactory;

 @Before
 public void setup() {
 formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
 parser = mock(JsonParser.class);
 generator = mock(JsonGenerator.class);
 serializers = mock(SerializerProvider.class);
 introspector = new BeanPropertyIntrospector();
 typeFactory = introspector.getJavaTypeInstance();
 }

 @Test
 public void testDefaultSerialization() throws JsonMappingException, JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = new BaseDateSerializer(Date.class, false, null);
 BeanProperty property = createProperty("testDate", Date.class);
 dateSerializer.serialize(new Date(), generator, serializers);
 assertEquals("\"2022-01-01 00:00:00 +0000\"", generator.getOutputValueAsString());
 }

 @Test
 public void testSerializationWithCustomFormat() throws JsonMappingException, JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = new BaseDateSerializer(Date.class, false, formatter);
 BeanProperty property = createProperty("testDate", Date.class);
 dateSerializer.serialize(new Date(), generator, serializers);
 assertEquals("\"2022-01-01 00:00:00 +0000\"", generator.getOutputValueAsString());
 }

 @Test
 public void testSerializationAsTimestamp() throws JsonMappingException, JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = new BaseDateSerializer(Date.class, true, null);
 BeanProperty property = createProperty("testDate", Date.class);
 dateSerializer.serialize(new Date(), generator, serializers);
 assertEquals("2022-01-01T00:00:00.000Z", generator.getOutputValueAsString());
 }

 @Test
 public void testSerializationAsStringWithCustomFormat() throws JsonMappingException, JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = new BaseDateSerializer(Date.class, false, formatter);
 BeanProperty property = createProperty("testDate", Date.class);
 dateSerializer.serialize(new Date(), generator, serializers);
 assertEquals("\"2022-01-01 00:00:00 +0000\"", generator.getOutputValueAsString());
 }

 @Test
 public void testSerializationAsStringWithCustomFormatAndTimeZone() throws JsonMappingException, JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = new BaseDateSerializer(Date.class, false, formatter);
 BeanProperty property = createProperty("testDate", Date.class);
 when(serializers.getTimeZone()).thenReturn(ZoneId.of("UTC+1"));
 dateSerializer.serialize(new Date(), generator, serializers);
 assertEquals("\"2022-01-01 01:00:00 +0100\"", generator.getOutputValueAsString());
 }

 @Test
 public void testSerializationWithJsonFormatAnnotation() throws JsonMappingException, JsonProcessingException {
 MyDate myDate = new MyDate();
 DateTimeSerializerBase<MyDate> dateSerializer = new BaseDateSerializer(MyDate.class, false, null);
 BeanProperty property = createProperty("testDate", MyDate.class);
 when(serializers.findAnnotationIntrospector()).thenReturn(introspector);
 when(introspector.findFormat((Annotated)property.getMember())).thenReturn(new JsonFormat(Shape.STRING, null, null, null, null, null));
 dateSerializer.serialize(myDate, generator, serializers);
 assertEquals("\"2022-01-01T00:00:00.000+00:00\"", generator.getOutputValueAsString());
 }

 @Test
 public void testDeserialization() throws JsonMappingException, JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = new BaseDateSerializer(Date.class, false, null);
 when(parser.getValueAsString()).thenReturn("\"2022-01-01 00:00:00 +0000\"");
 JsonNode node = dateSerializer.deserialize(parser, serializers, null);
 assertEquals(new Date(), node.asInstancia(Date.class));
 }

 @Test
 public void testDeserializationWithCustomFormat() throws JsonMappingException, JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = new BaseDateSerializer(Date.class, false, formatter.Formatter());
 when(parser.getValueAsString()).thenReturn("\"2022-01-01 00:00:00 +0000\"");
 JsonNode node = dateSerializer.deserialize(parser, serializers, null);
 assertEquals(new Date(), node.asInstancia(Date.class));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidFormat() throws JsonMappingException, JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = new BaseDateSerializer(Date.class, false, null);
 when(parser.getValueAsString()).thenReturn("\"invalid format\"");
 JsonNode node = dateSerializer.deserialize(parser, serializers, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidFormatAndCustomFormat() throws JsonMappingException, JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = new BaseDateSerializer(Date.class, false, formatter);
 when(parser.getValueAsString()).thenReturn("\"invalid format\"");
 JsonNode node = dateSerializer.deserialize(parser, serializers, null);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 DateTimeSerializerBase<Date> dateSerializer = new BaseDateSerializer(Date.class, false, null);
 JsonFormatVisitorWrapper visitor = createVisitor();
 dateSerializer.acceptJsonFormatVisitor(visitor, typeFactory.construct(Date.class));
 assertEquals(JsonFormatTypes.DATE_TIME, visitor.getFormatType());
 }

 @Test
 public void testAcceptJsonFormatVisitorAsNumber() throws JsonMappingException {
 DateTimeSerializerBase<Date> dateSerializer = new BaseDateSerializer(Date.class, true, null);
 JsonFormatVisitorWrapper visitor = createVisitor();
 dateSerializer.acceptJsonFormatVisitor(visitor, typeFactory.construct(Date.class));
 assertEquals(JsonFormatTypes.DATE_TIME, visitor.getFormatType());
 assertEquals(JsonParser.NumberType.LONG, visitor.getNumberType());
 }

 private BeanProperty createProperty(String name, Class<?> type) {
 BeanProperty property = mock(BeanProperty.class);
 when(property.getName()).thenReturn(name);
 when(property.getType()).thenReturn(type);
 return property;
 }

 private JsonFormatVisitorWrapper createVisitor() {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 return visitor;
 }

 private static class MyDate {
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
 private Date testDate;
 }

}