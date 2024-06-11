package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase.JsonFormatShape;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

 @SuppressWarnings("unchecked")
 @Test
 public void testDateTimeSerializerBase() throws Exception {
 DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 ObjectMapper mapper = new ObjectMapper();

 // Test serialization with default settings
 Date date = new Date();
 JsonNode result = mapper.writeValueAsTree(date, new TypeReference<JsonNode>() {});
 assertEquals(DATE_FORMAT.format(date), result.textValue());

 // Test serialization as numeric timestamp
 DateTimeSerializerBase<Date> dateTimeSerializerAsTimestamp = dateTimeSerializer.withFormat(Boolean.TRUE, null);
 result = mapper.writeValueAsTree(date, new TypeReference<JsonNode>() {});
 assertTrue(result.isNumber());
 assertEquals(date.getTime(), result.numberValue().longValue());

 // Test serialization as String with custom format
 DateTimeSerializerBase<Date> dateTimeSerializerWithCustomFormat = dateTimeSerializer.withFormat(Boolean.FALSE, DATE_FORMAT);
 result = mapper.writeValueAsTree(date, new TypeReference<JsonNode>() {});
 assertEquals(DATE_FORMAT.format(date), result.textValue());

 // Test serialization as String with custom format and TimeZone
 DateTimeSerializerBase<Date> dateTimeSerializerWithCustomFormatAndTimeZone = dateTimeSerializer.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US, TimeZone.getTimeZone("UTC")));
 result = mapper.writeValueAsTree(date, new TypeReference<JsonNode>() {});
 assertEquals(DATE_FORMAT.format(date), result.textValue());

 // Test serialization as String with custom format and Locale
 DateTimeSerializerBase<Date> dateTimeSerializerWithCustomFormatAndLocale = dateTimeSerializer.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US));
 result = mapper.writeValueAsTree(date, new TypeReference<JsonNode>() {});
 assertEquals(DATE_FORMAT.format(date), result.textValue());

 // Test serialization as String with custom format, Locale and TimeZone
 DateTimeSerializerBase<Date> dateTimeSerializerWithCustomFormatLocaleAndTimeZone = dateTimeSerializer.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US, TimeZone.getTimeZone("UTC")));
 result = mapper.writeValueAsTree(date, new TypeReference<JsonNode>() {});
 assertEquals(DATE_FORMAT.format(date), result.textValue());

 // Test serialization as String with custom format, Locale, TimeZone and custom serializer
 ObjectMapper mapperWithCustomSerializer = new ObjectMapper();
 mapperWithCustomSerializer.setSerializerProvider(mapperWithCustomSerializer.getSerializerProvider().withCurrentSerializerProviderChain(new DateTimeSerializerBase<Date>(Date.class, false, null)));
 DateTimeSerializerBase<Date> dateTimeSerializerWithCustomFormatLocaleTimeZoneAndCustomSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null).withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US, TimeZone.getTimeZone("UTC")));
 mapperWithCustomSerializer.writeValueAsString(date);
 result = mapper.readTree(mapperWithCustomSerializer.writeValueAsString(date));
 assertEquals(DATE_FORMAT.format(date), result.textValue());

 // Test deserialization with default settings
 JsonParser parser = mapper.getFactory().createParser(DATE_FORMAT.format(date));
 Date deserializedDate = mapper.readValue(parser, Date.class);
 assertEquals(date, deserializedDate);

 // Test deserialization as numeric timestamp
 parser = mapper.getFactory().createParser(Long.toString(date.getTime()));
 deserializedDate = mapper.readValue(parser, Date.class);
 assertEquals(date, deserializedDate);

 // Test deserialization as String with custom format
 parser = mapper.getFactory().createParser(DATE_FORMAT.format(date));
 deserializedDate = mapper.readValue(parser, Date.class);
 assertEquals(date, deserializedDate);

 // Test deserialization as String with custom format and TimeZone
 parser = mapper.getFactory().createParser("2023-01-01 12:00:00");
 AnnotatedMember member = new AnnotatedMember("test", Date.class, new Annotated());
 JsonFormat format = new JsonFormat();
 format.setShape(JsonFormat.Shape.STRING);
 format.setTimeZone(TimeZone.getTimeZone("UTC"));
 format.setLocale(Locale.US);
 format.setPattern("yyyy-MM-dd HH:mm:ss");
 JsonParser.NumberType numberType = null;
 JsonValueFormat jsonValueFormat = JsonValueFormat.DATE_TIME;
 mapper.getFactory().findFormatProvider().setProvider(new JsonFormatProvider() {
 @Override
 public JsonFormat<Object> findFormat(Class<?> type, JsonFormat.Value value) {
 if (type.isAssignableFrom(Date.class)) {
 return format;
 }
 return null;
 }
 });
 deserializedDate = mapper.readValue(parser, Date.class);
 assertEquals(date, deserializedDate);

 // Test deserialization as String with custom format and Locale
 parser = mapper.getFactory().createParser("2023-01-01 12:00:00");
 mapper.getFactory().findFormatProvider().setProvider(new JsonFormatProvider() {
 @Override
 public JsonFormat<Object> findFormat(Class<?> type, JsonFormat.Value value) {
 if (type.isAssignableFrom(Date.class)) {
 JsonFormat.Shape shape = value.getShape();
 if (shape.isString()) {
 JsonFormat format = new JsonFormat();
 format.setShape(shape);
 format.setLocale(Locale.US);
 return format;
 }
 }
 return null;
 }
 });
 deserializedDate = mapper.readValue(parser, Date.class);
 assertEquals(date, deserializedDate);

 // Test deserialization as String with custom format, Locale, TimeZone and custom serializer
 mapperWithCustomSerializer.getFactory().findFormatProvider().setProvider(new JsonFormatProvider() {
 @Override
 public JsonFormat<Object> findFormat(Class<?> type, JsonFormat.Value value) {
 if (type.isAssignableFrom(Date.class)) {
 JsonFormat format = new JsonFormat();
 format.setShape(JsonFormat.Shape.STRING);
 format.setLocale(Locale.US);
 format.setTimeZone(TimeZone.getTimeZone("UTC"));
 format.setPattern("yyyy-MM-dd HH:mm:ss");
 return format;
 }
 return null;
 }
 });
 mapperWithCustomSerializer.readValue(mapperWithCustomSerializer.writerWithRootValue("2023-01-01 12:00:00").writeValueAsString(""));
 deserializedDate = mapper.readValue(mapperWithCustomSerializer.writerWithRootValue("2023-01-01 12:00:00").writeValueAsString(""), Date.class);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testGetEmpty() {
 DateTimeSerializerBase<DateTime> datetimeSerializerBase = new DateTimeSerializerBase<DateTime>(DateTime.class, false, null);
 ObjectMapper mapper = new ObjectMapper();
 DateTime dateTime = null;
 assertTrue(datetimeSerializerBase.isEmpty(dateTime));
 assertTrue(datetimeSerializerBase.isEmpty(mapper, dateTime));
 }

 @Test
 public void testGetTimestamp() {
 DateTimeSerializerBase<DateTime> datetimeSerializerBase = new DateTimeSerializerBase<DateTime>(DateTime.class, false, null);
 ObjectMapper mapper = new ObjectMapper();
 DateTime dateTime = new DateTime();
 long timestamp = datetimeSerializerBase._timestamp(dateTime);
 assertEquals(dateTime.getMillis(), timestamp);
 assertEquals(dateTime.getMillis(), datetimeSerializerBase.serialize(dateTime, null, null).getSerializableValue().longValue());
 }

 @Test
 public void testGetSchema() throws JsonMappingException, JsonProcessingException {
 DateTimeSerializerBase<DateTime> datetimeSerializerBase = new DateTimeSerializerBase<DateTime>(DateTime.class, false, null);
 ObjectMapper mapper = new ObjectMapper();
 JsonNode schema = datetimeSerializerBase.getSchema(mapper.getSerializerProvider(), DateTime.class);
 assertEquals("number", schema.typeName());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 DateTimeSerializerBase<DateTime> datetimeSerializerBase = new DateTimeSerializerBase<DateTime>(DateTime.class, false, null);
 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getSerializerProvider().getRootValueSerializerProvider().findSerializerProviderForType(DateTime.class).acceptJsonFormatVisitor(new TypeReference<JsonFormatVisitorWrapper>() {});
 datetimeSerializerBase._acceptJsonFormatVisitor(visitor, DateTime.class.getType(), true);
 assertEquals(JsonFormatVisitorWrapper.VISITED_INT_FORMAT, visitor.getVisited());

 datetimeSerializerBase._acceptJsonFormatVisitor(visitor, DateTime.class.getType(), false);
 assertEquals(JsonFormatVisitorWrapper.VISITED_STRING_FORMAT, visitor.getVisited());
 }
}