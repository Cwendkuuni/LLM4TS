package com.fasterxml.jackson.databind.ser.std.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider.Feature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateSerializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest extends BaseMapTest {

    @Test
    public void testDefaultConstructor() {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, null, null);
        assertNull(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testWithFormatTimestampTrue() {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.TRUE, null);
        assertTrue(serializer._useTimestamp.booleanValue());
        assertNull(serializer._customFormat);
    }

    @Test
    public void testWithFormatCustomFormat() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, dateFormat);
        assertFalse(serializer._useTimestamp.booleanValue());
        assertEquals(dateFormat, serializer._customFormat);
    }

    @Test
    public void testCreateContextualSerializerNullProperty() throws JsonMappingException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, null);
        assertEquals(serializer, serializer.createContextual(null, null));
    }

    @Test
    public void testCreateContextualSerializerEmptyProperty() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
        assertEquals(serializer, serializer.createContextual(null, new BeanProperty(new AnnotatedClass(String.class), new AnnotatedMember())));
    }

    @Test
    public void testCreateContextualSerializerFormat() throws JsonMappingException {
        DateTimeSerializerBase<java.util.Date> serializer = new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, Boolean.FALSE, null);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        assertEquals(new StdDateSerializer(java.util.Date.class, null, format), serializer.createContextual(null, new BeanProperty(new AnnotatedClass(java.util.Date.class), new AnnotatedMember())));
    }

    @Test
    public void testSerializeNullValue() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
        assertEquals("null", serializer.serialize(null, objectMapper.getFactory().createGenerator(new StringWriter()), objectMapper.getSerializerProvider()).toString());
    }

    @Test
    public void testSerializeTimestampValue() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.TRUE, null);
        Long timestamp = 1633053200000L;
        assertEquals(Long.toString(timestamp), serializer.serialize(timestamp, objectMapper.getFactory().createGenerator(new StringWriter()), objectMapper.getSerializerProvider()).toString());
    }

    @Test
    public void testSerializeStringValue() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
        String dateString = "2023-03-23T14:15:23.456Z";
        assertEquals(dateString, serializer.serialize(dateString, objectMapper.getFactory().createGenerator(new StringWriter()), objectMapper.getSerializerProvider()).toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeInvalidFormatValue() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String dateString = "2023-03-23T14:15:23.456";
        serializer.serialize(dateString, objectMapper.getFactory().createGenerator(new StringWriter()), objectMapper.getSerializerProvider());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.TRUE, null);
        JsonNode schema = serializer.getSchema(objectMapper.getSerializerProvider(), Long.TYPE);
        assertEquals("number", schema.getClass().getSimpleName());
    }

    @Test
    public void testGetSchemaString() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
        JsonNode schema = serializer.getSchema(objectMapper.getSerializerProvider(), String.class);
        assertEquals("string", schema.getClass().getSimpleName());
    }
}