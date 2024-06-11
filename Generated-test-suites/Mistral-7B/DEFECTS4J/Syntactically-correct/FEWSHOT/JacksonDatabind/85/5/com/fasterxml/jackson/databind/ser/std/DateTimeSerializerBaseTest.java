package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest<T> {

    abstract static class TestDateTimeSerializerBase extends DateTimeSerializerBase<T> {
        TestDateTimeSerializerBase(Class<T> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new TestDateTimeSerializerBase<>(type, timestamp, customFormat);
        }
    }

    @Test
    public void testDateTimeSerializerBaseCreation() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        DateTimeSerializerBase<String> serializer1 = new TestDateTimeSerializerBase<String>(String.class, null, null);
        DateTimeSerializerBase<String> serializer2 = serializer1.withFormat(Boolean.FALSE, customFormat);
        DateTimeSerializerBase<String> serializer3 = serializer1.withFormat(Boolean.TRUE, null);
        assertNotNull(serializer1);
        assertNotNull(serializer2);
        assertNotNull(serializer3);
    }

    @Test
    public void testCreateContextualWithDefaults() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Calendar> serializer = new TestDateTimeSerializerBase<>(Calendar.class, null, null);
        JsonSerializer<Calendar> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        assertInstanceOf(DateTimeSerializerBase.class, contextualSerializer);
    }

    @Test
    public void testCreateContextualWithCustomFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Calendar> serializer = new TestDateTimeSerializerBase<>(Calendar.class, null, new SimpleDateFormat("yyyy-MM-dd"));
        JsonSerializer<Calendar> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        assertInstanceOf(DateTimeSerializerBase.class, contextualSerializer);
        assertNotNull(contextualSerializer.getFormat());
    }

    @Test
    public void testCreateContextualWithCustomFormatAndLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Calendar> serializer = new TestDateTimeSerializerBase<>(Calendar.class, null, new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH));
        JsonSerializer<Calendar> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        assertInstanceOf(DateTimeSerializerBase.class, contextualSerializer);
        assertNotNull(contextualSerializer.getFormat());
        assertEquals(Locale.FRENCH, contextualSerializer.getFormat().getLocale());
    }

    @Test
    public void testCreateContextualWithCustomFormatAndTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Calendar> serializer = new TestDateTimeSerializerBase<>(Calendar.class, null, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        JsonSerializer<Calendar> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        assertInstanceOf(DateTimeSerializerBase.class, contextualSerializer);
        assertNotNull(contextualSerializer.getFormat());
        assertEquals("GMT+01:00", contextualSerializer.getFormat().getTimeZone().getID());
    }

    @Test
    public void testCreateContextualWithCustomFormatAndTimestamp() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Calendar> serializer = new TestDateTimeSerializerBase<>(Calendar.class, Boolean.TRUE, null);
        JsonSerializer<Calendar> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        assertInstanceOf(DateTimeSerializerBase.class, contextualSerializer);
        assertNull(contextualSerializer.getFormat());
    }

    @Test
    public void testCreateContextualWithCustomFormatAndTimestampAndLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Calendar> serializer = new TestDateTimeSerializerBase<>(Calendar.class, Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH));
        JsonSerializer<Calendar> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        assertInstanceOf(DateTimeSerializerBase.class, contextualSerializer);
        assertNull(contextualSerializer.getFormat());
    }

    @Test
    public void testCreateContextualWithCustomFormatAndTimestampAndTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Calendar> serializer = new TestDateTimeSerializerBase<>(Calendar.class, Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        JsonSerializer<Calendar> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        assertInstanceOf(DateTimeSerializerBase.class, contextualSerializer);
        assertNull(contextualSerializer.getFormat());
    }

    @Test
    public void testSerializeWithDefaults() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Calendar> serializer = new TestDateTimeSerializerBase<>(Calendar.class, null, null);
        JsonSerializer<Calendar> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        Calendar calendar = Calendar.getInstance();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        contextualSerializer.serialize(calendar, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
    }

    @Test
    public void testSerializeWithCustomFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Calendar> serializer = new TestDateTimeSerializerBase<>(Calendar.class, null, new SimpleDateFormat("yyyy-MM-dd"));
        JsonSerializer<Calendar> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        Calendar calendar = Calendar.getInstance();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        contextualSerializer.serialize(calendar, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
    }

    @Test
    public void testSerializeWithCustomFormatAndLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Calendar> serializer = new TestDateTimeSerializerBase<>(Calendar.class, null, new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH));
        JsonSerializer<Calendar> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        Calendar calendar = Calendar.getInstance();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        contextualSerializer.serialize(calendar, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
    }

    @Test
    public void testSerializeWithCustomFormatAndTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Calendar> serializer = new TestDateTimeSerializerBase<>(Calendar.class, null, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        JsonSerializer<Calendar> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        Calendar calendar = Calendar.getInstance();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        contextualSerializer.serialize(calendar, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
    }

    @Test
    public void testSerializeWithTimestamp() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Calendar> serializer = new TestDateTimeSerializerBase<>(Calendar.class, Boolean.TRUE, null);
        JsonSerializer<Calendar> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        Calendar calendar = Calendar.getInstance();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        contextualSerializer.serialize(calendar, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
    }

    @Test
    public void testSerializeWithTimestampAndLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Calendar> serializer = new TestDateTimeSerializerBase<>(Calendar.class, Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH));
        JsonSerializer<Calendar> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        Calendar calendar = Calendar.getInstance();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        contextualSerializer.serialize(calendar, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
    }

    @Test
    public void testSerializeWithTimestampAndTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Calendar> serializer = new TestDateTimeSerializerBase<>(Calendar.class, Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        JsonSerializer<Calendar> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        Calendar calendar = Calendar.getInstance();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        contextualSerializer.serialize(calendar, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
    }
}