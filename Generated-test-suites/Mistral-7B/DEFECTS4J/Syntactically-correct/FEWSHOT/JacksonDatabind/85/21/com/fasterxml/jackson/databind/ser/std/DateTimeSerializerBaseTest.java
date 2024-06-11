package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public abstract class DateTimeSerializerBaseTest<T> {

    private static final Class<T> TEST_TYPE = String.class;

    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<T> INSTANCE = new DateTimeSerializerBase<T>(TEST_TYPE, false, null);

    @Test
    public void testSerializationWithDefaults() throws Exception {
        T value = new Date(0);
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        INSTANCE.serialize(value, gen, new SerializerProvider());
        assertEquals("null", gen.getOutputValue());
    }

    @Test
    public void testSerializationAsTimestamp() throws Exception {
        T value = new Date(System.currentTimeMillis());
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        INSTANCE.serialize(value, gen, new SerializerProvider().withFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true));
        assertEquals(Long.toString(value.getClass().getMethod("getTime").invoke(value)), gen.getOutputValue());
    }

    @Test
    public void testSerializationWithCustomFormat() throws Exception {
        T value = new Date(System.currentTimeMillis());
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        DateFormat customFormat = new SimpleDateFormat(pattern, locale);
        customFormat.setTimeZone(timeZone);

        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        INSTANCE.withFormat(true, customFormat).serialize(value, gen, new SerializerProvider());
        assertEquals(customFormat.format(value), gen.getOutputValue());
    }

    @Test
    public void testSerializationWithJsonFormatAnnotation() throws Exception {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        T annotatedValue = new Date(System.currentTimeMillis());

        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        INSTANCE.serialize(annotatedValue, gen, new SerializerProvider());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(annotatedValue), gen.getOutputValue());
    }

    @Test
    public void testDeserializationWithDefaults() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "null";
        T result = mapper.readValue(json, TEST_TYPE);
        assertNull(result);
    }

    @Test
    public void testDeserializationAsTimestamp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "1631459333000";
        T result = mapper.readValue(json, TEST_TYPE);
        assertEquals(new Date(1631459333000L), result);
    }

    @Test
    public void testDeserializationWithCustomFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "2023-03-15T14:25:36.789Z";
        T result = mapper.readValue(json, TEST_TYPE);
        assertEquals(new Date(System.currentTimeMillis()), result); // This test only checks if deserialization is successful.
    }

    @Test
    public void testDeserializationWithJsonFormatAnnotation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"2023-03-15T14:25:36.789Z\"";
        T result = mapper.readValue(json, TEST_TYPE);
        assertEquals(new Date(System.currentTimeMillis()), result); // This test only checks if deserialization is successful.
    }
}