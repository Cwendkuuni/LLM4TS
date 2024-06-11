package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.StdSerializer;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseWithDefaults() {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        assertEquals(Long.TYPE, serializer.handledType());
        assertFalse(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testDateTimeSerializerBaseWithUseTimestamp() {
        DateTimeSerializerBase<Instant> serializer = new DateTimeSerializerBase<Instant>(Instant.class, true, null);
        assertEquals(Instant.class, serializer.handledType());
        assertTrue(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormat() {
        String dateFormatPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");

        DateFormat customFormat = new SimpleDateFormat(dateFormatPattern, locale);
        customFormat.setTimeZone(timeZone);

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, customFormat);
        assertEquals(String.class, serializer.handledType());
        assertFalse(serializer._useTimestamp);
        assertSame(customFormat, serializer._customFormat);
    }

    @Test
    public void testDateTimeSerializerBaseCreateContextualSerializerWithFormat() throws JsonMappingException {
        String dateFormatPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");

        DateFormat customFormat = new SimpleDateFormat(dateFormatPattern, locale);
        customFormat.setTimeZone(timeZone);

        SerializerProvider serializers = new StandardSerializer(String.class, null);
        BeanProperty property = null;

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);

        JsonFormat.Value format = new JsonFormat.Value();
        JsonFormat.Shape shape = JsonFormat.Shape.STRING;
        format.setShape(shape);
        format.setPattern(dateFormatPattern);
        format.setLocale(locale);
        format.setTimeZone(timeZone);

        JsonSerializer<String> jsonSerializer = serializer.createContextual(serializers, property);

        JsonFormat jsonFormat = jsonSerializer.getFormat();

        assertEquals(String.class, jsonSerializer.getClass());
        assertEquals(String.class, jsonFormat.getType());
        assertEquals(JsonNodeType.STRING, jsonFormat.getValueType());
        assertEquals(shape, jsonFormat.getShape());
        assertEquals(dateFormatPattern, jsonFormat.getPattern());
        assertEquals(locale, jsonFormat.getLocale());
        assertEquals(timeZone, jsonFormat.getTimeZone());
    }

    @Test
    public void testDateTimeSerializerBaseCreateContextualSerializerWithDefaultFormat() throws JsonMappingException {
        SerializerProvider serializers = new StandardSerializer(String.class, null);
        BeanProperty property = null;

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);

        JsonFormat.Value format = serializer.createContextual(serializers, property).getFormat();

        assertEquals(String.class, serializer.getClass());
        assertEquals(String.class, format.getType());
        assertEquals(JsonNodeType.STRING, format.getValueType());
        assertEquals(JsonFormat.Shape.STRING, format.getShape());
        assertNull(format.getPattern());
        assertNull(format.getLocale());
        assertNull(format.getTimeZone());
    }

    @Test
    public void testDateTimeSerializerBaseSerializeWithDefaults() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);

        Long value = Instant.now().getEpochMilli();

        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(value, gen, new DefaultSerializerProvider());
        assertEquals("\"0\"", gen.getOutputBuffer().asString());

        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serializeNull(gen, new DefaultSerializerProvider());
        assertEquals("null", gen.getOutputBuffer().asString());
    }

    @Test
    public void testDateTimeSerializerBaseSerializeWithUseTimestamp() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);

        Long value = Instant.now().getEpochMilli();

        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(value, gen, new DefaultSerializerProvider());
        assertEquals("0", gen.getOutputBuffer().asString());
    }

    @Test
    public void testDateTimeSerializerBaseSerializeWithCustomFormat() throws IOException {
        String dateFormatPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");

        DateFormat customFormat = new SimpleDateFormat(dateFormatPattern, locale);
        customFormat.setTimeZone(timeZone);

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, customFormat);

        Instant value = Instant.now();

        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(value.toString(), gen, new DefaultSerializerProvider());
        assertEquals(customFormat.format(value.toDate()), gen.getOutputBuffer().asString());
    }
}