package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
 private static final Locale LOCALE_UTC = new Locale("en_US", "UTC");

 @Test
 public void testDateTimeSerializerBaseInstance() {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 assertNotNull(serializer);
 }

 @Test
 public void testDateTimeSerializerBaseWithFormat() {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 DateTimeSerializerBase<Date> serialized = serializer.withFormat(Boolean.TRUE, null);
 assertNotSame(serializer, serialized);
 }

 @Test
 public void testDateTimeSerializerBaseSerializationAsTimestamp() throws Exception {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 ObjectMapper mapper = new ObjectMapper();
 Date date = new Date();
 JsonGenerator gen = mapper.getPolymorphicSerializer(serializer).getSerializerProvider().getJsonGenerator();
 mapper.writeValue(gen, date);
 JsonParser parser = mapper.getFactory().getParser();
 long value = parser.getValueAsLong();
 assertEquals(date.getTime(), value);
 }

 @Test
 public void testDateTimeSerializerBaseSerializationAsString() throws Exception {
 DateTimeSerializerBase<ZonedDateTime> serializer = new DateTimeSerializerBase<ZonedDateTime>(ZonedDateTime.class, false,
 new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", LOCALE_UTC));
 ObjectMapper mapper = new ObjectMapper();
 ZonedDateTime date = ZonedDateTime.now();
 JsonGenerator gen = mapper.getPolymorphicSerializer(serializer).getSerializerProvider().getJsonGenerator();
 mapper.writeValue(gen, date);
 JsonParser parser = mapper.getFactory().getParser();
 String value = parser.getValueAsString();
 assertEquals(date.format(StdDateFormat.DEFAULT_DATE_FORMAT), value);
 }

 @Test
 public void testDateTimeSerializerBaseSerializationWithCustomFormat() throws Exception {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false,
 new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz", LOCALE_UTC));
 ObjectMapper mapper = new ObjectMapper();
 Date date = new Date();
 JsonGenerator gen = mapper.getPolymorphicSerializer(serializer).getSerializerProvider().getJsonGenerator();
 gen.setTimeZone(UTC);
 mapper.writeValue(gen, date);
 JsonParser parser = mapper.getFactory().getParser();
 Date parsedDate = mapper.readValue(parser, Date.class);
 assertEquals(date, parsedDate);
 }

 @Test
 public void testDateTimeSerializerBaseSerializationEmptyValue() throws Exception {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator gen = mapper.getPolymorphicSerializer(serializer).getSerializerProvider().getJsonGenerator();
 mapper.writeValue(gen, null);
 JsonParser parser = mapper.getFactory().getParser();
 long value = parser.getValueAsLong();
 assertEquals(0, value);
 }

 @Test
 public void testDateTimeSerializerBaseSerializationEmptyValueWithFormat() throws Exception {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 DateTimeSerializerBase<Date> serialized = serializer.withFormat(Boolean.FALSE,
 new StdDateFormat(StdDateFormat.FULL_DATE_FORMAT, LOCALE_UTC, UTC, null));
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator gen = mapper.getPolymorphicSerializer(serialized).getSerializerProvider().getJsonGenerator();
 mapper.writeValue(gen, null);
 JsonParser parser = mapper.getFactory().getParser();
 String value = parser.getValueAsString();
 assertNull(value);
 }

 @Test
 public void testDateTimeSerializerBaseDeserializationAsTimestamp() throws Exception {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().getParser();
 parser.setCodec(mapper.getSerializationConfig().getEncoder(Date.class));
 parser.nextToken();
 long value = parser.getValueAsLong();
 Date deserialized = mapper.readValue(parser, Date.class);
 assertEquals(deserialized, new Date(value));
 }

 @Test
 public void testDateTimeSerializerBaseDeserializationAsString() throws Exception {
 DateTimeSerializerBase<ZonedDateTime> serializer = new DateTimeSerializerBase<ZonedDateTime>(ZonedDateTime.class, false,
 new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", LOCALE_UTC));
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().getParser();
 parser.setCodec(mapper.getSerializationConfig().getEncoder(ZonedDateTime.class));
 parser.nextValue();
 String value = parser.getValueAsString();
 ZonedDateTime deserialized = mapper.readValue(parser, ZonedDateTime.class);
 assertEquals(deserialized, ZonedDateTime.parse(value, StdDateFormat.DEFAULT_DATE_FORMAT));
 }

 @Test(expected = JsonMappingException.class)
 public void testDateTimeSerializerBaseDeserializationInvalidFormat() throws Exception {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("XXX", LOCALE_UTC));
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().getParser();
 parser.setCodec(mapper.getSerializationConfig().getEncoder(Date.class));
 parser.nextValue();
 }

 @Test(expected = JsonMappingException.class)
 public void testDateTimeSerializerBaseDeserializationNullValue() throws Exception {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().getParser();
 parser.nextValue();
 parser.nextNull();
 }

 @Test(expected = JsonMappingException.class)
 public void testDateTimeSerializerBaseDeserializationEmptyString() throws Exception {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().getParser();
 parser.nextValue();
 parser.nextText("");
 }
}