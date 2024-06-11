package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.TestBase;

public class DateTimeSerializerBaseTest extends TestBase {

    @SuppressWarnings("unchecked")
    private static DateTimeSerializerBase<Long> createLongSerializer(boolean useTimestamp, DateFormat customFormat) {
        return new DateTimeSerializerBase<Long>(Long.TYPE, useTimestamp, customFormat) {
            @Override
            protected long _timestamp(Long value) {
                return value;
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static DateTimeSerializerBase<String> createStringSerializer(boolean useTimestamp, DateFormat customFormat) {
        return new DateTimeSerializerBase<String>(String.class, useTimestamp, customFormat) {
            @Override
            protected long _timestamp(String value) {
                return 0L; // assuming empty string is considered empty
            }
        };
    }

    @Test
    public void testDateTimeSerializerBase() {
        // Test default constructor
        DateTimeSerializerBase<Long> defaultSerializer = createLongSerializer(null, null);
        assertEquals(Long.TYPE, defaultSerializer.handledType());
        assertNull(defaultSerializer._useTimestamp);
        assertNull(defaultSerializer._customFormat);

        DateTimeSerializerBase<String> defaultSerializerForString = createStringSerializer(null, null);
        assertEquals(String.class, defaultSerializerForString.handledType());
        assertNull(defaultSerializerForString._useTimestamp);
        assertNull(defaultSerializerForString._customFormat);

        // Test constructor with useTimestamp and null customFormat
        DateTimeSerializerBase<Long> useTimestampSerializer = createLongSerializer(true, null);
        assertEquals(Long.TYPE, useTimestampSerializer.handledType());
        assertTrue(useTimestampSerializer._useTimestamp.booleanValue());
        assertNull(useTimestampSerializer._customFormat);

        DateTimeSerializerBase<String> useTimestampSerializerForString = createStringSerializer(true, null);
        assertEquals(String.class, useTimestampSerializerForString.handledType());
        assertTrue(useTimestampSerializerForString._useTimestamp.booleanValue());
        assertNull(useTimestampSerializerForString._customFormat);

        // Test constructor with null useTimestamp and customFormat
        DateTimeSerializerBase<Long> customFormatSerializer = createLongSerializer(null, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertEquals(Long.TYPE, customFormatSerializer.handledType());
        assertNull(customFormatSerializer._useTimestamp);
        assertEquals(customFormatSerializer._customFormat, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        DateTimeSerializerBase<String> customFormatSerializerForString = createStringSerializer(null, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertEquals(String.class, customFormatSerializerForString.handledType());
        assertNull(customFormatSerializerForString._useTimestamp);
        assertEquals(customFormatSerializerForString._customFormat, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // Test constructor with useTimestamp = false and customFormat
        DateTimeSerializerBase<Long> useTimestampFalseSerializer = createLongSerializer(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertEquals(Long.TYPE, useTimestampFalseSerializer.handledType());
        assertFalse(useTimestampFalseSerializer._useTimestamp.booleanValue());
        assertEquals(useTimestampFalseSerializer._customFormat, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        DateTimeSerializerBase<String> useTimestampFalseSerializerForString = createStringSerializer(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertEquals(String.class, useTimestampFalseSerializerForString.handledType());
        assertFalse(useTimestampFalseSerializerForString._useTimestamp.booleanValue());
        assertEquals(useTimestampFalseSerializerForString._customFormat, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        final ObjectMapper mapper = new ObjectMapper();
        JsonFormat<Object> jsonFormat = new JsonFormat.Builder<Object>()
                .withShape(JsonFormat.Shape.STRING)
                .withLocale(Locale.US)
                .withTimeZone(TimeZone.getTimeZone("UTC"))
                .withPattern("yyyy-MM-dd HH:mm:ss")
                .build();

        Annotated member = mapper.findPropertyMetadata("testDate", String.class);
        JsonFormat format = JsonFormat.Value.getInstance(JsonFormat.Std.DATE_TIME, mapper.getAnnotationIntrospector()
                .findAnnotation(member, JsonFormat.class));
        assertEquals(format.getShape().isNumeric(), createStringSerializer(null, null).createContextual(mapper.getSerializerProvider(), null) instanceof DateTimeSerializerBase<T>);
        assertEquals(format.getShape() == JsonFormat.Shape.STRING, createStringSerializer(null, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).createContextual(mapper.getSerializerProvider(), null) instanceof DateTimeSerializerBase<T>);
    }

    @Test
    public void testSerialize() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ObjectMapper mapper = new ObjectMapper();

        DateTimeSerializerBase<Long> useTimestampSerializer = createLongSerializer(true, null);
        Long testValue = 1598950400000L; // 2020-09-15 12:00:00 UTC

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        useTimestampSerializer.serialize(testValue, gen, mapper.getSerializerProvider());
        assertEquals("1598950400000", gen.getOutputBuffer().toString());

        DateTimeSerializerBase<String> customFormatSerializer = createStringSerializer(null, dateFormat);
        String testStringValue = "2020-09-15 12:00:00";

        gen.reset();
        customFormatSerializer.serialize(testStringValue, gen, mapper.getSerializerProvider());
        assertEquals("\"2020-09-15 12:00:00\"", gen.getOutputBuffer().toString());

        gen.reset();
        customFormatSerializer = createStringSerializer(false, dateFormat);
        customFormatSerializer.serialize(testStringValue, gen, mapper.getSerializerProvider());
        assertEquals("\"2020-09-15 12:00:00\"", gen.getOutputBuffer().toString());

        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(gen.getOutputBuffer().toString().getBytes()));
        assertEquals(testValue, mapper.readValue(parser, Long.class));
        assertEquals(testStringValue, mapper.readValue(parser, String.class));
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Long> serializer = createLongSerializer(false, null);
        assertFalse(serializer.isEmpty(null));
        assertFalse(serializer.isEmpty(1234567890123L));
        assertFalse(serializer.isEmpty(new Date(0)));
        assertTrue(serializer.isEmpty(null, mapper.getSerializerProvider()));
        assertTrue(serializer.isEmpty(null, mapper.getSerializerProvider()));
        assertTrue(serializer.isEmpty(new Date(0), mapper.getSerializerProvider()));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Long> useTimestampSerializer = createLongSerializer(true, null);
        assertEquals("number", useTimestampSerializer.getSchema(mapper.getSerializerProvider(), Long.TYPE));

        DateTimeSerializerBase<String> customFormatSerializer = createStringSerializer(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertEquals("string", customFormatSerializer.getSchema(mapper.getSerializerProvider(), String.class));
    }
}