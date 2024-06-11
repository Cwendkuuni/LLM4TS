package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.BeanPropertyIterator;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> dateTimeSerializer;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, dateFormat);
 }

 @Test
 public void testCreateContextual() throws JsonMappingException, JsonProcessingException {
 JsonFormat jsonFormat = new JsonFormat.Builder()
 		.shape(JsonFormat.Shape.STRING)
 		.timeZone(TimeZone.getTimeZone("UTC"))
 		.build();
 BeanProperty beanProperty = new BeanProperty("testDate", Date.class, null, null, null, null, null);
 ObjectMapper contextualSerializerObjectMapper = new ObjectMapper();
 contextualSerializerObjectMapper.setAnnotationIntrospector(new StandardAnnotationIntrospector());
 JsonSerializer<Date> contextualSerializer = dateTimeSerializer.createContextual(contextualSerializerObjectMapper.getSerializerProvider(), beanProperty);
 assertEquals(DateTimeSerializerBase.class, contextualSerializer.getClass());
 JsonFormat jsonFormatContextual = contextualSerializerObjectMapper.getAnnotationIntrospector().findFormat((Annotated)beanProperty.getMember());
 assertEquals(jsonFormat, jsonFormatContextual);
 }

 @Test
 public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
 Date date = new Date();
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 dateTimeSerializer.serialize(date, jsonGenerator, objectMapper.getSerializerProvider());
 JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(jsonGenerator.getOutputBuffer().toString().getBytes()));
 JsonNode jsonNode = jsonParser.readValueAsTree();
 assertEquals("\"2023-03-31 10:45:24\"", jsonNode.toString());
 }

 @Test
 public void testSerializeTimestamp() throws JsonGenerationException, JsonMappingException, IOException {
 ObjectMapper objectMapperTimestamp = new ObjectMapper();
 objectMapperTimestamp.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
 Date date = new Date();
 JsonGenerator jsonGenerator = objectMapperTimestamp.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 DateTimeSerializerBase<Date> dateTimeSerializerTimestamp = new DateTimeSerializerBase<Date>(Date.class, null, null);
 dateTimeSerializerTimestamp.serialize(date, jsonGenerator, objectMapperTimestamp.getSerializerProvider());
 JsonParser jsonParser = objectMapperTimestamp.getFactory().createParser(new ByteArrayInputStream(jsonGenerator.getOutputBuffer().toString().getBytes()));
 JsonNode jsonNode = jsonParser.readValueAsTree();
 assertEquals("1679756824000", jsonNode.toString().trim());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.getSerializerProvider().getJsonFactory()
 		.constructJsonFormatVisitor(new ClassType(JsonNode.class), null);
 dateTimeSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
 assertEquals("DATE_TIME", jsonFormatVisitorWrapper.getFormatName());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(dateTimeSerializer.isEmpty(null));
 assertTrue(dateTimeSerializer.isEmpty(null, objectMapper.getSerializerProvider()));
 assertTrue(dateTimeSerializer._timestamp(null) == 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsEmptyWithInvalidType() {
 DateTimeSerializerBase<String> stringSerializer = new DateTimeSerializerBase<String>(String.class, null, null);
 stringSerializer.isEmpty(new Object());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsEmptyWithInvalidProvider() {
 DateTimeSerializerBase<Date> dateTimeSerializerInvalidProvider = new DateTimeSerializerBase<Date>(Date.class, null, null);
 dateTimeSerializerInvalidProvider.isEmpty(new Object(), null);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 JsonNode jsonNode = dateTimeSerializer.getSchema(objectMapper.getSerializerProvider(), Date.class);
 assertEquals("string", jsonNode.get("type").asText());
 }
}