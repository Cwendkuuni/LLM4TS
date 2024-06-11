package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> serializer;
 private JsonFactory jsonFactory;

 @Before
 public void setup() {
 serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
 jsonFactory = new JsonFactory();
 }

 @Test
 public void testConstructor() {
 DateTimeSerializerBase<Long> ser = new DateTimeSerializerBase<Long>(Long.class, false, null);
 assertNotNull(ser);
 }

 @Test
 public void testWithFormat() {
 DateTimeSerializerBase<Long> ser1 = serializer.withFormat(true, null);
 DateTimeSerializerBase<Long> ser2 = serializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy"));
 assertNotSame(serializer, ser1);
 assertNotSame(serializer, ser2);
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper(jsonFactory);
 JsonFormat.Value format = new JsonFormat.Value();
 JsonFormat jsonFormat = new JsonFormat();
 jsonFormat.setShape(JsonFormat.Shape.STRING);
 jsonFormat.setPattern("dd-MM-yyyy");
 jsonFormat.setLocale(Locale.US);
 jsonFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 format.setFormat(jsonFormat);
 Annotated annotated = mapper.introspect().findProperty("testDate");
 SerializerProvider serializers = mapper.getSerializerProvider();
 DateTimeSerializerBase<Long> expectedSerializer = serializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy"));
 assertEquals(expectedSerializer, serializers.getSerializerProvider().findValueSerializer(Long.class, serializers, annotated));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(serializer.isEmpty(null));
 assertTrue(serializer.isEmpty(new Long(0)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsEmptyWithNullSerializerProvider() {
 serializer.isEmpty(null, null);
 }

 @Test
 public void testGetSchema() {
 JsonNode schema = serializer.getSchema(jsonFactory.getSerializerProvider(), Long.class);
 assertEquals("string", schema.getClass().getSimpleName());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = new MapperTestContextualSerializerProvider.StdDateSerializers.StdSerializers.DateSerializerProvider()._visitor;
 serializer.acceptJsonFormatVisitor(visitor, Long.class);
 assertTrue(visitor.getFormat().isDateFormat());
 }

 @Test
 public void testSerialize() throws IOException {
 Long date = 1633059200000L; // 1st January 2022
 JsonGenerator gen = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(date, gen, jsonFactory.getSerializerProvider());
 String json = gen.getOutputContentAsString();
 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
 Date parsedDate = formatter.parse(json);
 assertEquals(date.longValue(), parsedDate.getTime());
 }

 @Test
 public void testSerializeWithCustomFormat() throws IOException, ParseException {
 Long date = 1633059200000L; // 1st January 2022
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 serializer = new DateTimeSerializerBase<Long>(Long.class, false, customFormat);
 JsonGenerator gen = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(date, gen, jsonFactory.getSerializerProvider());
 String json = gen.getOutputContentAsString();
 Date parsedDate = customFormat.parse(json);
 assertEquals(date.longValue(), parsedDate.getTime());
 }

 @Test(expected = IOException.class)
 public void testSerializeWithNullGenerator() {
 serializer.serialize(new Long(0), null, jsonFactory.getSerializerProvider());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeWithNullValue() {
 serializer.serialize(null, jsonFactory.getGenerator(), jsonFactory.getSerializerProvider());
 }
}