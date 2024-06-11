package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

@SuppressWarnings("unchecked")
public class DateTimeSerializerBaseTest {

 private static final Object MILLIS_ZERO = 0L;
 private static final Object NULL_DATE = null;

 @Test
 public void testDateTimeSerializerBaseConstructor() {
 DateTimeSerializerBase<Long> dateTimeSerializerBaseLong = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
 DateTimeSerializerBase<String> dateTimeSerializerBaseString = new DateTimeSerializerBase<String>(String.class, false, null);
 assertEquals(Long.TYPE, dateTimeSerializerBaseLong.handledType());
 assertEquals(String.class, dateTimeSerializerBaseString.handledType());
 }

 @Test
 public void testDateTimeSerializerBaseWithFormat_Boolean_DateFormat() {
 DateTimeSerializerBase<Long> dateTimeSerializerBaseLong = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
 DateTimeSerializerBase<Long> dateTimeSerializerBaseWithFormatLong = dateTimeSerializerBaseLong.withFormat(Boolean.TRUE, null);
 DateTimeSerializerBase<Long> dateTimeSerializerBaseWithFormatLongCustomFormat = dateTimeSerializerBaseLong.withFormat(Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
 assertEquals(DateTimeSerializerBase.class, dateTimeSerializerBaseWithFormatLong.getClass());
 assertEquals(DateTimeSerializerBase.class, dateTimeSerializerBaseWithFormatLongCustomFormat.getClass());
 assertEquals(Boolean.TRUE, dateTimeSerializerBaseWithFormatLong._useTimestamp);
 assertEquals(Boolean.TRUE, dateTimeSerializerBaseWithFormatLongCustomFormat._useTimestamp);
 assertEquals(null, dateTimeSerializerBaseWithFormatLong._customFormat);
 assertEquals(null, dateTimeSerializerBaseWithFormatLongCustomFormat._customFormat);

 DateTimeSerializerBase<String> dateTimeSerializerBaseString = new DateTimeSerializerBase<String>(String.class, false, null);
 DateTimeSerializerBase<String> dateTimeSerializerBaseWithFormatString = dateTimeSerializerBaseString.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
 assertEquals(DateTimeSerializerBase.class, dateTimeSerializerBaseWithFormatString.getClass());
 assertEquals(Boolean.FALSE, dateTimeSerializerBaseWithFormatString._useTimestamp);
 assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"), dateTimeSerializerBaseWithFormatString._customFormat);
 }

 @Test
 public void testCreateContextualSerializer() throws JsonMappingException {
 SerializerProvider serializers = new SerializerProvider();
 ObjectMapper mapper = new ObjectMapper(serializers);
 ObjectWriter writer = mapper.writer();

 DateTimeSerializerBase<Long> dateTimeSerializerBaseLong = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
 JsonSerializer<Long> jsonSerializer = dateTimeSerializerBaseLong.createContextual(serializers, null);
 assertEquals(DateTimeSerializerBase.class, jsonSerializer.getClass());
 assertEquals(Long.TYPE, jsonSerializer.handleType());

 JsonSerializer<String> jsonSerializerString = dateTimeSerializerBaseLong.createContextual(serializers, null);
 assertNotNull(jsonSerializerString);
 assertNotSame(jsonSerializer, jsonSerializerString);
 assertEquals(String.class, jsonSerializerString.handleType());

 DateTimeSerializerBase<Long> dateTimeSerializerBaseWithFormatLong = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
 JsonSerializer<Long> jsonSerializerWithFormatLong = dateTimeSerializerBaseWithFormatLong.createContextual(serializers, null);
 assertEquals(DateTimeSerializerBase.WithTimestampSerializer.class, jsonSerializerWithFormatLong.getClass());
 assertEquals(Long.TYPE, jsonSerializerWithFormatLong.handleType());

 DateTimeSerializerBase<String> dateTimeSerializerBaseWithFormatString = new DateTimeSerializerBase<String>(String.class, false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
 JsonSerializer<String> jsonSerializerWithFormatString = dateTimeSerializerBaseWithFormatString.createContextual(serializers, null);
 assertEquals(DateTimeSerializerBase.StringSerializer.class, jsonSerializerWithFormatString.getClass());
 assertEquals(String.class, jsonSerializerWithFormatString.handleType());
 }

 @Test
 public void testIsEmpty() {
 DateTimeSerializerBase<Long> dateTimeSerializerBaseLong = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
 assertTrue(dateTimeSerializerBaseLong.isEmpty(MILLIS_ZERO));
 assertTrue(dateTimeSerializerBaseLong.isEmpty(NULL_DATE));

 DateTimeSerializerBase<Long> dateTimeSerializerBaseWithFormatLong = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
 assertTrue(dateTimeSerializerBaseWithFormatLong.isEmpty(MILLIS_ZERO));
 assertTrue(dateTimeSerializerBaseWithFormatLong.isEmpty(NULL_DATE));
 }

 @Test
 public void testGetSchema() {
 SerializerProvider serializers = new SerializerProvider();
 ObjectMapper mapper = new ObjectMapper(serializers);
 ObjectWriter writer = mapper.writer();

 DateTimeSerializerBase<Long> dateTimeSerializerBaseLong = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
 JsonNode schema = dateTimeSerializerBaseLong.getSchema(serializers, Long.TYPE);
 assertEquals("number", schema.asText());

 DateTimeSerializerBase<Long> dateTimeSerializerBaseWithFormatLong = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
 schema = dateTimeSerializerBaseWithFormatLong.getSchema(serializers, Long.TYPE);
 assertEquals("number", schema.asText());

 DateTimeSerializerBase<String> dateTimeSerializerBaseString = new DateTimeSerializerBase<String>(String.class, false, null);
 schema = dateTimeSerializerBaseString.getSchema(serializers, String.class);
 assertEquals("string", schema.asText());

 DateTimeSerializerBase<String> dateTimeSerializerBaseWithFormatString = new DateTimeSerializerBase<String>(String.class, false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
 schema = dateTimeSerializerBaseWithFormatString.getSchema(serializers, String.class);
 assertEquals("string", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StandardSerializer<>(Integer.class), null, null);
 DateTimeSerializerBase<Long> dateTimeSerializerBaseLong = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
 dateTimeSerializerBaseLong._acceptJsonFormatVisitor(visitor, Long.TYPE, false);

 JsonFormatVisitorWrapper visitorWithFormat = new JsonFormatVisitorWrapper(new StandardSerializer<>(Integer.class), null, null);
 DateTimeSerializerBase<Long> dateTimeSerializerBaseWithFormatLong = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
 dateTimeSerializerBaseWithFormatLong._acceptJsonFormatVisitor(visitorWithFormat, Long.TYPE, true);

 JsonFormatVisitorWrapper visitorWithFormatCustom = new JsonFormatVisitorWrapper(new StandardSerializer<>(Integer.class), null, null);
 DateTimeSerializerBase<Long> dateTimeSerializerBaseWithFormatLongCustomFormat = new DateTimeSerializerBase<Long>(Long.TYPE, true, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
 dateTimeSerializerBaseWithFormatLongCustomFormat._acceptJsonFormatVisitor(visitorWithFormatCustom, Long.TYPE, true);

 JsonSerializer<Long> jsonSerializer = new StandardSerializer<>(Long.class);
 JsonFormatVisitorWrapper visitorWithoutFormat = new JsonFormatVisitorWrapper(jsonSerializer, null, null);
 DateTimeSerializerBase<Long> dateTimeSerializerBaseWithoutFormat = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
 dateTimeSerializerBaseWithoutFormat._acceptJsonFormatVisitor(visitorWithoutFormat, Long.TYPE, false);

 JsonFormat.Shape shapeWithoutFormat = JsonFormat.Shape.ARRAY;
 JsonFormatVisitorWrapper visitorWithShapeWithoutFormat = new JsonFormatVisitorWrapper(new StandardSerializer<>(Long[].class), null, null);
 DateTimeSerializerBase<Long> dateTimeSerializerBaseWithShapeWithoutFormat = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
 dateTimeSerializerBaseWithShapeWithoutFormat._acceptJsonFormatVisitor(visitorWithShapeWithoutFormat, Long[].class, shapeWithoutFormat);

 JsonFormatVisitorWrapper visitorWithShapeString = new JsonFormatVisitorWrapper(new StandardSerializer<>(String[].class), null, null);
 DateTimeSerializerBase<Long> dateTimeSerializerBaseWithShapeString = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
 dateTimeSerializerBaseWithShapeString._acceptJsonFormatVisitor(visitorWithShapeString, String[].class, JsonFormat.Shape.STRING);
 }

 @Test
 public void testSerialize() throws IOException {
 SerializerProvider serializers = new SerializerProvider();
 ObjectMapper mapper = new ObjectMapper(serializers);
 ObjectWriter writer = mapper.writer();
 Long value = 1618728147410L;
 JsonGenerator jsonGenerator = writer.getGenerator();
 DateTimeSerializerBase<Long> dateTimeSerializerBaseLong = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);

 jsonGenerator.writeStartArray();
 dateTimeSerializerBaseLong.serialize(MILLIS_ZERO, jsonGenerator, serializers);
 jsonGenerator.writeNumber(0);
 jsonGenerator.writeEndArray();
 jsonGenerator.flush();
 String jsonExpected = "[0]";
 assertEquals(jsonExpected, writer.writeValueAsString(writer.readValue(jsonGenerator.getOutputStreamAsString())));

 jsonGenerator.writeStartArray();
 dateTimeSerializerBaseLong.serialize(value, jsonGenerator, serializers);
 jsonGenerator.writeNumber(value.longValue());
 jsonGenerator.writeEndArray();
 jsonGenerator.flush();
 jsonExpected = "[1618728147410]";
 assertEquals(jsonExpected, writer.writeValueAsString(writer.readValue(jsonGenerator.getOutputStreamAsString())));

 jsonGenerator.writeStartArray();
 DateTimeSerializerBase<Long> dateTimeSerializerBaseWithFormatLong = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
 dateTimeSerializerBaseWithFormatLong.serialize(value, jsonGenerator, serializers);
 jsonGenerator.writeEndArray();
 jsonGenerator.flush();
 jsonExpected = "[1618728147410]";
 assertEquals(jsonExpected, writer.writeValueAsString(writer.readValue(jsonGenerator.getOutputStreamAsString())));

 jsonGenerator.writeStartArray();
 DateTimeSerializerBase<Long> dateTimeSerializerBaseWithFormatLongCustomFormat = new DateTimeSerializerBase<Long>(Long.TYPE, true, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
 dateTimeSerializerBaseWithFormatLongCustomFormat.serialize(value, jsonGenerator, serializers);
 jsonGenerator.writeEndArray();
 jsonGenerator.flush();
 jsonExpected = "[\"2021-01-01T00:02:03.045Z\"]";
 assertEquals(jsonExpected, writer.writeValueAsString(writer.readValue(jsonGenerator.getOutputStreamAsString())));

 jsonGenerator.writeStartObject();
 jsonGenerator.writeFieldName("foo");
 DateTimeSerializerBase<Long> dateTimeSerializerBaseWithFormatLongCustomFormat2 = new DateTimeSerializerBase<Long>(Long.TYPE, true, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
 dateTimeSerializerBaseWithFormatLongCustomFormat2.serialize(value, jsonGenerator, serializers);
 jsonGenerator.writeEndObject();
 jsonGenerator.flush();
 jsonExpected = "{\"foo\":\"2021-01-01T00:02:03.045Z\"}";
 assertEquals(jsonExpected, writer.writeValueAsString(writer.readValue(jsonGenerator.getOutputStreamAsString())));
 }

 @Test
 public void testDeserialize() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mapper.parser();
 String jsonString = "[1618728147410]";
 Long value = mapper.readerForType(Long.class).readValue(jsonParser.readValue(jsonString));
 assertEquals(1618728147410L, value.longValue());

 jsonString = "[\"2021-01-01T00:02:03.045Z\"]";
 value = mapper.readerForValue(JsonType.DATE_TIME).readValue(jsonParser.readValue(jsonString));
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(value.getValue());
 assertEquals(2021, calendar.get(Calendar.YEAR));
 assertEquals(1, calendar.get(Calendar.MONTH) + 1); // Month is 0-indexed in Calendar but 1-indexed in JsonFormat
 assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
 assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
 assertEquals(2, calendar.get(Calendar.MINUTE));
 assertEquals(3, calendar.get(Calendar.SECOND));
 assertEquals(4, calendar.get(Calendar.MILLISECOND));
 assertEquals(TimeZone.getDefault(), calendar.getTimeZone());
 assertEquals(Locale.getDefault(), calendar.getLocale());
 }
}