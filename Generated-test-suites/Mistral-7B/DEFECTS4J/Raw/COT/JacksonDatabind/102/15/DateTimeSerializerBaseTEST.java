package com.example.test; // replace with your test package name

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.std.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private SerializerProvider serializers;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        serializers = new SerializerProvider();
        objectMapper = new ObjectMapper(serializers);
        serializers.setDefaultPropertyInclusion(JsonProperty.Visibility.ANY);
        serializers.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // Test method for the constructor
    @Test
    public void testDateTimeSerializerBaseConstructor() {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        assertNotNull(dateSerializer);
    }

    // Test method for the abstract method 'withFormat'
    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethodWithFormat() {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        dateSerializer.withFormat(Boolean.TRUE, null); // Should throw UnsupportedOperationException
    }

    // Test method for the 'createContextual' method
    @Test
    public void testCreateContextualMethod() throws JsonMappingException, JsonProcessingException, IOException {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, dateFormat);

        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        jsonFormat.setLocale(Locale.ENGLISH);
        jsonFormat.setTimeZone(TimeZone.getDefault());
        jsonFormat.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        JsonFormat<Date> serializerFormat = dateSerializer.createContextual(serializers, null);
        assertEquals(Date.class, serializerFormat.getType());
        assertEquals(JsonFormat.Shape.STRING, serializerFormat.getShape());
        assertEquals(Locale.ENGLISH, serializerFormat.getLocale());
        assertEquals(TimeZone.getDefault(), serializerFormat.getTimeZone());
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", serializerFormat.getPattern());
    }

    // Test method for the 'serialize' method
    @Test
    public void testSerializeMethod() throws JsonGenerationException, JsonMappingException, IOException {
        Date date = new Date();
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        String output;
        JsonGenerator gen = serializers.getFactory().createGenerator(System.out, null);

        dateSerializer.serialize(date, gen, serializers);
        gen.flush();
        output = gen.getOutputString();

        assertEquals("2023-03-27T14:34:56.028Z", output);

        // Test for serialization as timestamp
        SerializerProvider serializersWithTimestamp = new SerializerProvider();
        serializersWithTimestamp.setDefaultPropertyInclusion(JsonProperty.Visibility.ANY);
        serializersWithTimestamp.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        DateTimeSerializerBase<Long> longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        long value = date.getTime();

        output = longSerializer.serialize(value, serializersWithTimestamp.getFactory().createGenerator(System.out, null), serializersWithTimestamp);
        assertEquals("1679802696000", output);
    }

    // Test method for the 'isEmpty' method
    @Test
    public void testIsEmptyMethod() {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        Date date = new Date();

        assertFalse(dateSerializer.isEmpty(serializers, date));

        // Test for empty Date
       Date emptyDate = new Date(0);

        assertTrue(dateSerializer.isEmpty(serializers, emptyDate));
    }

    // Test method for the 'getSchema' method
    @Test
    public void testGetSchemaMethod() {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        JsonNode schema = dateSerializer.getSchema(serializers, Date.class);
        assertEquals("number", schema.getType());

        dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        schema = dateSerializer.getSchema(serializers, Date.class);
        assertEquals("string", schema.getType());
    }

    // Test method for the 'acceptJsonFormatVisitor' method
    @Test
    public void testAcceptJsonFormatVisitorMethod() throws JsonMappingException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        JsonFormatVisitorWrapper visitor = serializers.getFactory().getJsonFormatVisitor(serializers, Date.class);
        dateSerializer.acceptJsonFormatVisitor(visitor, Date.class);

        JsonFormat<Date> jsonFormat = visitor.getFormat();
        assertEquals(JsonFormat.Shape.STRING, jsonFormat.getShape());
        assertEquals(JsonValueFormat.DATE_TIME, jsonFormat.getValueFormat());
    }

    // Test edge cases and exception handling

    // Test custom format with null and non-default locale
    @Test(expected = IllegalArgumentException.class)
    public void testCustomFormatNull() {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        dateSerializer.createContextual(serializers, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomFormatLocaleNull() {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        jsonFormat.setLocale(null);
        dateSerializer.createContextual(serializers, null, jsonFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomFormatLocaleEmpty() {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        jsonFormat.setLocale(new Locale(""));
        dateSerializer.createContextual(serializers, null, jsonFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomFormatTimeZoneNull() {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        jsonFormat.setTimeZone(null);
        dateSerializer.createContextual(serializers, null, jsonFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomFormatTimeZoneEmpty() {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        jsonFormat.setTimeZone(new TimeZone(""));
        dateSerializer.createContextual(serializers, null, jsonFormat);
    }

    @Test
    public void testCustomFormatReusedInstance() throws JsonMappingException, JsonProcessingException, IOException {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");

        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, formatter);
        JsonFormat<Date> jsonFormat = dateSerializer.createContextual(serializers, null);

        assertEquals(formatter.getClass(), jsonFormat.getValueSerializer().getFormat().getClass());

        Date date1 = new Date();
        JsonParser parser = serializers.getFactory().createParser(new ByteArrayInputStream(jsonFormat.serializeValue(date1).getBytes()), 0, jsonFormat.serializeValue(date1).length());
        Date date2 = dateSerializer.deserialize(parser, Date.class);

        assertEquals(date1, date2);

        // Test reuse of formatter instance
        Date date3 = new Date();
        JsonFormat<Date> jsonFormat2 = dateSerializer.createContextual(serializers, null);
        JsonParser parser2 = serializers.getFactory().createParser(new ByteArrayInputStream(jsonFormat2.serializeValue(date3).getBytes()), 0, jsonFormat2.serializeValue(date3).length());
        Date date4 = dateSerializer.deserialize(parser2, Date.class);

        assertEquals(date1, date4); // Reused formatter instance should return the same serialized value
    }
}