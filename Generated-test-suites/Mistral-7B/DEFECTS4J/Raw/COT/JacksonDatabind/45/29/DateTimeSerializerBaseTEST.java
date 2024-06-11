package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.JavaType;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();
 private static final Class<Date> DATE_CLASS = Date.class;

 @Test
 public void testCreateContextual() throws JsonMappingException, IOException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 SerializerProvider serializers = MAPPER.getSerializerProvider();
 JsonFormatVisitorWrapper visitor = null;

 assertEquals(serializer, serializer.createContextual(serializers, null));

 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 format.setLocale(Locale.US);
 format.setTimeZone(TimeZone.getDefault());
 BeanProperty property = null;

 assertEquals(serializer, serializer.createContextual(serializers, property));

 assertEquals(serializer.withFormat(Boolean.FALSE, new SimpleDateFormat(format.getPattern(), format.getLocale())), serializer.createContextual(serializers, property));
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextualWithNullProvider() throws JsonMappingException, IOException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 assertEquals(serializer, serializer.createContextual(null, null));
 }

 @Test
 public void testIsEmpty() throws IOException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 Date value1 = null;
 Date value2 = new Date(0L);

 assertTrue(serializer.isEmpty(value1));
 assertTrue(serializer.isEmpty(MAPPER.getSerializerProvider(), value1));
 assertTrue(serializer.isEmpty(value2));
 assertTrue(serializer.isEmpty(MAPPER.getSerializerProvider(), value2));
 }

 @Test
 public void testGetSchema() throws JsonMappingException, IOException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 JsonNode schema = serializer.getSchema(MAPPER.getSerializerProvider(), DATE_CLASS);
 assertEquals("string", schema.getClass().getSimpleName());
 }

 @Test
 public void testSerialize() throws JsonMappingException, IOException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 Date date = new Date();
 JsonGenerator gen = MAPPER.getFactory().createGenerator();
 SerializerProvider serializers = MAPPER.getSerializerProvider();

 serializer.serialize(date, gen, serializers);
 String json = gen.getOutputFactory().writeValueAsString(gen.getBuffered());
 assertEquals("\"\\/Date(<?)>\"", json);

 gen.reset();
 serializer = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US));
 serializer.serialize(date, gen, serializers);
 json = gen.getOutputFactory().writeValueAsString(gen.getBuffered());
 assertEquals("\"2023-03-29T14:30:15.012Z\"", json);
 }

 @Test(expected = IOException.class)
 public void testSerializeWithNullValue() throws JsonMappingException, IOException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 JsonGenerator gen = MAPPER.getFactory().createGenerator();
 SerializerProvider serializers = MAPPER.getSerializerProvider();

 serializer.serialize(null, gen, serializers);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeWithInvalidFormat() throws JsonMappingException, IOException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 Date date = new Date();
 JsonFormat format = new JsonFormat();
 format.setShape(JsonFormat.Shape.STRING);
 format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'E'"); // invalid character 'E'
 JsonParser parser = MAPPER.getFactory().createParser("test");
 BeanProperty property = new BeanProperty(DATE_CLASS, "testProperty");
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(MAPPER.getSerializerProvider().getConfig(), property);
 serializer.acceptJsonFormatVisitor(visitor, JavaType.construct(JsonNode.class, DATE_CLASS));
 assertEquals(JsonFormat.Shape.STRING, visitor.getFormat().getShape());
 assertEquals("testProperty", visitor.getPropertyName());
 assertNotNull(visitor.getFormat().getPattern());
 assertNotNull(visitor.getFormat().getLocale());
 assertNotNull(visitor.getFormat().getTimeZone());

 serializer.serialize(date, gen, serializers);
 }
}