package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class DateTimeSerializerBaseTest {

 private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDateTimeSerializerBaseWithTimestamp() throws JsonGenerationException, IOException {
 Long timestamp = 1597283200000L; // 2020-10-01 00:00:00.000Z
 ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
 node.put("value", timestamp);

 DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>((Class<Long>) Long.class, true, null);
 JsonGenerator gen = ObjectMapper.getDefaultInstance().getPolymorphicSerializer(serializer.handledType()).getSerializerProvider().getJsonFactory().createGenerator(new StringWriter());
 serializer.serialize(node.get("value"), gen, ObjectMapper.getDefaultInstance().getSerializationConfig());
 assertEquals("1597283200000", gen.toString());

 gen.close();
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDateTimeSerializerBaseWithCustomFormat() throws JsonGenerationException, IOException {
 DateTime dateTime = new DateTime(2020, 10, 1, 12, 30, 45, 567, TimeZone.getDefault());
 AnnotatedClass ac = ObjectMapper.getDefaultInstance().getAnnotationIntrospector().findClassAnnotations(DateTime.class);
 JsonFormat.Value jsonFormatValue = ac.findAnnotation(JsonFormat.class);
 DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<>((Class<DateTime>) DateTime.class, false, jsonFormatValue != null ? new SimpleDateFormat(jsonFormatValue.pattern(), jsonFormatValue.locale()) : null);
 ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
 node.put("value", dateTime);

 JsonGenerator gen = ObjectMapper.getDefaultInstance().getPolymorphicSerializer(serializer.handledType()).getSerializerProvider().getJsonFactory().createGenerator(new StringWriter());
 serializer.serialize(node.get("value"), gen, ObjectMapper.getDefaultInstance().getSerializationConfig());
 String expected = DATE_TIME_FORMATTER.print(dateTime);
 assertEquals(expected, gen.toString());

 gen.close();
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDateTimeSerializerBaseWithEmptyValue() throws JsonGenerationException, IOException {
 ObjectMapper mapper = ObjectMapper.getDefaultInstance();
 ObjectNode node = mapper.createObjectNode();
 node.putNull("value");

 DateTimeSerializerBase<Object> serializer = mapper.findValueSerializer(Object.class);
 JsonGenerator gen = mapper.getPolymorphicSerializer(serializer.handledType()).getSerializerProvider().getJsonFactory().createGenerator(new StringWriter());
 serializer.serialize(node.get("value"), gen, mapper.getSerializationConfig());
 assertTrue(gen.toString().isEmpty());

 gen.close();
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDateTimeSerializerBaseWithNullValue() throws JsonGenerationException, IOException {
 ObjectMapper mapper = ObjectMapper.getDefaultInstance();
 ObjectNode node = mapper.createObjectNode();
 node.putNull("value");

 DateTimeSerializerBase<Object> serializer = mapper.findValueSerializer(Object.class);
 JsonGenerator gen = mapper.getPolymorphicSerializer(serializer.handledType()).getSerializerProvider().getJsonFactory().createGenerator(new StringWriter());
 serializer.serialize(node.get("value"), gen, mapper.getSerializationConfig());
 assertNull(gen.toString());

 gen.close();
 }

 @org.junit.Test
 public void testDateTimeSerializerBaseGetSchema() throws IOException {
 ObjectMapper mapper = ObjectMapper.getDefaultInstance();
 SchemaFactory factory = mapper.getSerializationConfig().getDefaultOutputFactory();
 JsonSchema schema = mapper.getSerializerProvider().getSchemaFactory().getSchema(mapper.getSerializerProvider(), DateTime.class);
 JsonNode expected = factory.createSchemaNode("number");
 assertEquals(expected.toString(), schema.toString());
 }
}