package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    @Test
    public void testDateTimeSerializerBase() {
        DateTimeSerializerBase<Instant> serializer = new DateTimeSerializerBase<Instant>(
                Instant.class,
                Boolean.FALSE,
                null);
        Instant value = Instant.now();

        // Test default serialization
        String output;
        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
            serializer.serialize(value, gen, null);
            gen.flush();
            output = gen.getOutput().toString();
        } catch (IOException e) {
            fail("Failed to serialize: " + e.getMessage());
        }
        assertEquals("\"2023-03-11T12:34:56.789Z\"", output);

        // Test serialization with custom format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS").withLocale(Locale.US);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.US);
        format.setPattern(formatter.format(value));
        serializer = serializer.withFormat(Boolean.FALSE, new StdDateFormat(formatter.format(value), Locale.US, null));
        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
            serializer.serialize(value, gen, null);
            gen.flush();
            output = gen.getOutput().toString();
        } catch (IOException e) {
            fail("Failed to serialize: " + e.getMessage());
        }
        assertEquals("\"dd-MM-yyyy HH:mm:ss.SSS\"", output);
        assertEquals("\"dd-MM-yyyy HH:mm:ss.SSS\"", serializer.getSchema(null, Instant.class).toString());

        // Test serialization as timestamp
        serializer = new DateTimeSerializerBase<Instant>(
                Instant.class,
                Boolean.TRUE,
                null);
        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
            serializer.serialize(value, gen, null);
            gen.flush();
            output = gen.getOutput().toString();
        } catch (IOException e) {
            fail("Failed to serialize: " + e.getMessage());
        }
        assertEquals("2023-03-11T12:34:56.789Z", output);
        assertEquals("number", serializer.getSchema(null, Instant.class).toString());

        // Test serialization with timezone
        serializer = new DateTimeSerializerBase<Instant>(
                Instant.class,
                Boolean.FALSE,
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        TimeZone timeZone = TimeZone.getTimeZone("UTC+2");
        serializer.customFormat.setTimeZone(timeZone);
        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
            serializer.serialize(value, gen, null);
            gen.flush();
            output = gen.getOutput().toString();
        } catch (IOException e) {
            fail("Failed to serialize: " + e.getMessage());
        }
        assertEquals("\"2023-03-11T14:34:56.789Z\"", output);

        // Test serialization with locale
        serializer = new DateTimeSerializerBase<Instant>(
                Instant.class,
                Boolean.FALSE,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS", Locale.SIMPLIFIED_CHINESE));
        try {
            JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
            serializer.serialize(value, gen, null);
            gen.flush();
            output = gen.getOutput().toString();
        } catch (IOException e) {
            fail("Failed to serialize: " + e.getMessage());
        }
        assertEquals("\"11-03-2023 12:34:56.789\"", output);
    }

    @Test
    public void testNullValue() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(
                String.class,
                Boolean.FALSE,
                null);

        // Test serialization of null value
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);
        try {
            serializer.serialize(null, jsonGenerator, null);
            jsonGenerator.flush();
            String output = stringWriter.toString();
            assertEquals("null", output);
        } catch (IOException e) {
            fail("Failed to serialize null value: " + e.getMessage());
        }
    }
}