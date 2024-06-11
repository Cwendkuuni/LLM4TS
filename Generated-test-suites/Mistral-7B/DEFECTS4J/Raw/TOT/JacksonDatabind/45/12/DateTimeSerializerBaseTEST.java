package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 }

 @Test
 public void testDateTimeSerializerBaseSerializedStringFormat() throws JsonProcessingException {
 DateTimeSerializerBase<String> serializer = new StdDateSerializer();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, null);
 String testDate = "2022-01-01T12:00:00Z";
 serializer.serialize(testDate, gen, null);
 gen.flush(); // verify it's written as a string
 assertArrayEquals(testDate.getBytes(), gen.getOutputBuffer().getBuffer());
 }

 @Test
 public void testDateTimeSerializerBaseSerializedTimestampFormat() throws JsonProcessingException {
 DateTimeSerializerBase<Long> serializer = new StdSerializableDateSerializer();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, null);
 Long testDate = 1641344000000L; // 2022-01-01T12:00:00Z
 serializer.serialize(testDate, gen, null);
 gen.flush(); // verify it's written as a number
 assertEquals(testDate.toString(), gen.getOutputBuffer().readValueAsTree().toString());
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomFormat() throws JsonMappingException, JsonProcessingException {
 DateTimeSerializerBase<String> serializer = new StdDateSerializer();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setTimeZone(TimeZone.getTimeZone("UTC"));
 format.setPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
 serializers.setAnnotationIntrospector(new SimpleBeanPropertyDefinition(String.class)
 .setAnnotationValues(new Annotation[] { format }));
 String testDate = "2022-01-01T12:00:00Z";
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, serializers);
 serializer.serialize(testDate, gen, serializers);
 gen.flush(); // verify it's written with the custom format
 String expected = "2022-01-01T12:00:00Z";
 assertArrayEquals(expected.getBytes(), gen.getOutputBuffer().getBuffer());
 }

 @Test(expected = JsonMappingException.class)
 public void testDateTimeSerializerBaseWithInvalidCustomFormat() throws JsonMappingException, JsonProcessingException {
 DateTimeSerializerBase<String> serializer = new StdDateSerializer();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setPattern("invalidFormat"); // invalid pattern
 serializers.setAnnotationIntrospector(new SimpleBeanPropertyDefinition(String.class)
 .setAnnotationValues(new Annotation[] { format }));
 String testDate = "2022-01-01T12:00:00Z";
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, serializers);
 serializer.serialize(testDate, gen, serializers);
 }
}