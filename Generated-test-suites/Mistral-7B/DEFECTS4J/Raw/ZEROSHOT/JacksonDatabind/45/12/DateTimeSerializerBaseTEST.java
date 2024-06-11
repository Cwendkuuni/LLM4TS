package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObjectTestCase;
import org.jmock.lib.legacy.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest extends MockObjectTestCase {

 private static final Long TEST_LONG_VALUE = 1234567890L;
 private static final Date TEST_DATE_VALUE = new Date(TEST_LONG_VALUE);

 @SuppressWarnings("unchecked")
 private DateFormat createMockDateFormat() {
 return (DateFormat) ClassImposteriser.createMock(DateFormat.class);
 }

 @Before
 public void setUp() {
 setSystemProperty("jackson.serialization.WRITE_DATES_AS_TIMESTAMPS", "false");
 }

 @Test
 public void testSerializationWithDefaultSettings() throws JsonProcessingException, IOException {
 SerializerProvider serializers = new SerializerProvider();
 ObjectMapper mapper = new ObjectMapper(serializers);
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new StdDateSerializer(Date.class);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);

 Date testDate = TEST_DATE_VALUE;
 DateTimeSerializerBase<Date> serializer = dateTimeSerializerBase;
 serializer.serialize(testDate, gen, serializers);
 gen.flush();

 String actualOutput = gen.getOutputContentAsString();
 String expectedOutput = "\"2009-12-25T12:10:45.123Z\""; // Example output from StdDateSerializer
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializationWithTimestampSetting() throws JsonProcessingException, IOException {
 SerializerProvider serializers = new SerializerProvider();
 ObjectMapper mapper = new ObjectMapper(serializers);
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new StdDateSerializer(Date.class);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);

 Date testDate = TEST_DATE_VALUE;
 DateTimeSerializerBase<Date> serializer = dateTimeSerializerBase.withFormat(Boolean.TRUE, null);
 serializer.serialize(testDate, gen, serializers);
 gen.flush();

 String actualOutput = gen.getOutputContentAsString();
 String expectedOutput = TEST_LONG_VALUE.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializationWithCustomFormat() throws JsonProcessingException, IOException {
 SerializerProvider serializers = new SerializerProvider();
 ObjectMapper mapper = new ObjectMapper(serializers);
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new StdDateSerializer(Date.class);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);

 Date testDate = TEST_DATE_VALUE;
 DateFormat customFormat = createMockDateFormat();
 DateTimeSerializerBase<Date> serializer = dateTimeSerializerBase.withFormat(Boolean.FALSE, customFormat);
 serializer.serialize(testDate, gen, serializers);
 gen.flush();

 verify(customFormat).format(any(Date.class));
 }

 @Test
 public void testSerializationWithJsonFormatAnnotation() throws JsonProcessingException, IOException {
 SerializerProvider serializers = new SerializerProvider();
 ObjectMapper mapper = new ObjectMapper(serializers);
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new StdDateSerializer(Date.class);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);

 Date testDate = TEST_DATE_VALUE;
 JsonFormat jsonFormatAnnotation = new JsonFormat();
 jsonFormatAnnotation.setShape(JsonFormat.Shape.STRING);
 jsonFormatAnnotation.setTimeZone(TimeZone.getTimeZone("UTC"));
 jsonFormatAnnotation.setPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
 Annotated annotatedTestDate = mock(Annotated.class);
 when(annotatedTestDate.getMember()).thenReturn(null);
 when(annotatedTestDate.getAnnotation(JsonFormat.class)).thenReturn(jsonFormatAnnotation);

 DateTimeSerializerBase<Date> serializer = dateTimeSerializerBase.createContextual(serializers, (BeanProperty) annotatedTestDate);
 serializer.serialize(testDate, gen, serializers);
 gen.flush();

 String actualOutput = gen.getOutputContentAsString();
 String expectedOutput = "\"2009-12-25T12:10:45Z\"";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializationWithNullValue() throws JsonProcessingException, IOException {
 SerializerProvider serializers = new SerializerProvider();
 ObjectMapper mapper = new ObjectMapper(serializers);
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new StdDateSerializer(Date.class);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);

 DateTimeSerializerBase<Date> serializer = dateTimeSerializerBase;
 serializer.serialize(null, gen, serializers);
 gen.flush();

 String actualOutput = gen.getOutputContentAsString();
 String expectedOutput = "null";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDeserializationWithDefaultSettings() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("\"2009-12-25T12:10:45.123Z\"");

 Date testDate = mapper.readValue(parser, Date.class);
 assertEquals(TEST_DATE_VALUE, testDate);
 }

 @Test
 public void testDeserializationWithTimestampSetting() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("1234567890");

 Date testDate = mapper.readValue(parser, Date.class);
 assertEquals(TEST_DATE_VALUE, testDate);
 }

 @Test
 public void testDeserializationWithCustomFormat() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("\"2009-12-25 12:10:45\"");
 DateFormat customFormat = createMockDateFormat();
 when(customFormat.parse("2009-12-25 12:10:45")).thenReturn(TEST_DATE_VALUE);

 DateTimeSerializerBase<Date> serializer = new StdDateSerializer(Date.class).withFormat(Boolean.FALSE, customFormat);
 ObjectMapper mapperWithCustomSerializer = new ObjectMapper();
 mapperWithCustomSerializer.setSerializerInstance(DateTimeSerializerBase.class, serializer);

 Date testDate = mapperWithCustomSerializer.readValue(parser, Date.class);
 assertEquals(TEST_DATE_VALUE, testDate);
 }

 @Test
 public void testDeserializationWithJsonFormatAnnotation() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("\"2009-12-25T12:10:45Z\"");
 JsonFormat jsonFormatAnnotation = new JsonFormat();
 jsonFormatAnnotation.setShape(JsonFormat.Shape.STRING);
 jsonFormatAnnotation.setTimeZone(TimeZone.getTimeZone("UTC"));
 jsonFormatAnnotation.setPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
 Annotated annotatedTestDate = mock(Annotated.class);
 when(annotatedTestDate.getMember()).thenReturn(null);
 when(annotatedTestDate.getAnnotation(JsonFormat.class)).thenReturn(jsonFormatAnnotation);

 DateTimeSerializerBase<Date> serializer = new StdDateSerializer(Date.class).createContextual(null, (BeanProperty) annotatedTestDate);
 ObjectMapper mapperWithCustomSerializer = new ObjectMapper();
 mapperWithCustomSerializer.setSerializerInstance(DateTimeSerializerBase.class, serializer);

 Date testDate = mapperWithCustomSerializer.readValue(parser, Date.class);
 assertEquals(TEST_DATE_VALUE, testDate);
 }
}