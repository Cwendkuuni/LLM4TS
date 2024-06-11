package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
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
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest<T> {

 private DateTimeSerializerBase<Long> dateTimeSerializerBase;
 private DateTimeSerializerBase<String> stringDateTimeSerializerBase;

 @Before
 public void setup() {
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
 customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 dateTimeSerializerBase = new DateTimeSerializerBase<Long>(Long.TYPE, false, customFormat) {};
 stringDateTimeSerializerBase = new DateTimeSerializerBase<String>(String.class, false, null) {};
 }

 @Test
 public void testCreateContextual() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Long> serializer = mapper.findValueSerializer(JsonSerialization.Value.forClass(Long.class));
 JsonSerializer<String> stringSerializer = mapper.findValueSerializer(JsonSerialization.Value.forClass(String.class));
 BeanProperty property = new BeanProperty(null, "testDate", Long.TYPE);
 Annotated annotated = new AnnotatedClass(property.getMember().getType(), property);
 JsonFormat format = AnnotationIntrospector.getDefault().findFormat(annotated);

 JsonSerializer<Long> resultSerializer = dateTimeSerializerBase.createContextual(mapper.getSerializerProvider(), property);
 JsonSerializer<String> stringResultSerializer = stringDateTimeSerializerBase.createContextual(mapper.getSerializerProvider(), property);

 assertTrue(resultSerializer instanceof DateTimeSerializerBase);
 assertTrue(stringResultSerializer instanceof DateTimeSerializerBase);

 JsonSerializer<Long> expectedSerializer = withFormat(resultSerializer, true, null);
 JsonSerializer<String> expectedStringSerializer = withFormat(stringResultSerializer, false, customFormat);

 assertEquals(expectedSerializer.getClass(), resultSerializer.getClass());
 assertEquals(expectedSerializer, expectedSerializer);
 assertEquals(expectedStringSerializer.getClass(), stringResultSerializer.getClass());
 assertEquals(expectedStringSerializer, expectedStringSerializer);
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mapper.getFactory().createParser("2023-03-29 14:30:00Z");
 JsonGenerator jsonGenerator = mapper.createGenerator(System.out);

 Long value = 1679635200000L; // 2023-03-29 14:30:00 UTC timestamp
 DateTimeSerializerBase<Long> datetimeSerializerBase = new DateTimeSerializerBase<Long>(Long.TYPE, false, null) {};

 datetimeSerializerBase.serialize(value, jsonGenerator, mapper.getSerializerProvider());
 jsonParser.nextValue();
 assertEquals(value, jsonParser.getValueAsLong());
 }

 @Test
 public void testSerializeString() throws JsonProcessingException, IOException, ParseException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mapper.getFactory().createParser("\"2023-03-29 14:30:00Z\"");
 JsonGenerator jsonGenerator = mapper.createGenerator(System.out);

 String value = "2023-03-29 14:30:00Z";
 DateTimeSerializerBase<String> datetimeSerializerBase = new DateTimeSerializerBase<String>(String.class, false, null) {};

 datetimeSerializerBase.serialize(value, jsonGenerator, mapper.getSerializerProvider());
 jsonParser.nextValue();
 assertEquals(value, jsonParser.getValueAsText());
 }

 @Test
 public void testSerializeWithFormat() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mapper.getFactory().createParser("\"2023-03-29 14:30:00Z\"");
 JsonGenerator jsonGenerator = mapper.createGenerator(System.out);

 DateTimeSerializerBase<String> datetimeSerializerBase = new DateTimeSerializerBase<String>(String.class, false, null) {};
 DateTimeSerializerBase<String> formattedSerializer = datetimeSerializerBase.withFormat(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));

 formattedSerializer.serialize("2023-03-29 14:30:00Z", jsonGenerator, mapper.getSerializerProvider());
 jsonParser.nextValue();
 assertEquals("2023-03-29 14:30:00-05:00", jsonParser.getValueAsText());
 }

 @Test
 public void testSerializeWithTimestamp() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mapper.getFactory().createParser("2023-03-29 14:30:00Z");
 JsonGenerator jsonGenerator = mapper.createGenerator(System.out);

 DateTimeSerializerBase<Long> datetimeSerializerBase = new DateTimeSerializerBase<Long>(Long.TYPE, true, null) {};

 datetimeSerializerBase.serialize(1679635200000L, jsonGenerator, mapper.getSerializerProvider());
 jsonParser.nextValue();
 assertEquals(1679635200000L, jsonParser.getValueAsLong());
 }

 private DateTimeSerializerBase<T> withFormat(DateTimeSerializerBase<T> serializerBase, boolean asTimestamp, DateFormat customFormat) {
 return (DateTimeSerializerBase<T>)serializerBase.withFormat(asTimestamp, customFormat);
 }
}