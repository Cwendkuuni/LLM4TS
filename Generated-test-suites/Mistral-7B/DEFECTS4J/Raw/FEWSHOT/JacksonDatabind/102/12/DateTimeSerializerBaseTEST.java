package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.DateFormat;
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

import org.junit.Test;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() {
        // Test default constructor
        DateTimeSerializerBase<?> defaultSerializer = new DateTimeSerializerBase<Object>(
                Object.class, null, null);
        assertNull(defaultSerializer._useTimestamp);
        assertNull(defaultSerializer._customFormat);
        assertNull(defaultSerializer._reusedCustomFormat);

        // Test constructor with _useTimestamp and null _customFormat
        DateTimeSerializerBase<Long> timestampSerializer = new DateTimeSerializerBase<Long>(
                Long.class, Boolean.TRUE, null);
        assertTrue(timestampSerializer._useTimestamp.booleanValue());
        assertNull(timestampSerializer._customFormat);
        assertNull(timestampSerializer._reusedCustomFormat);

        // Test constructor with null _useTimestamp and _customFormat
        DateTimeSerializerBase<String> customFormatSerializer = new DateTimeSerializerBase<String>(
                String.class, null, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertNull(customFormatSerializer._useTimestamp);
        assertNotEquals(null, customFormatSerializer._customFormat);
        assertNull(customFormatSerializer._reusedCustomFormat);

        // Test constructor with _useTimestamp and _customFormat
        DateTimeSerializerBase<Date> timestampWithCustomFormatSerializer = new DateTimeSerializerBase<Date>(
                Date.class, Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertTrue(timestampWithCustomFormatSerializer._useTimestamp.booleanValue());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), timestampWithCustomFormatSerializer._customFormat);
        assertNull(timestampWithCustomFormatSerializer._reusedCustomFormat);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(
                Date.class, Boolean.FALSE, null);

        // Test withBoolean and null DateFormat
        DateTimeSerializerBase<Date> timestampSerializer = dateSerializer.withFormat(
                Boolean.TRUE, null);
        assertTrue(timestampSerializer._useTimestamp.booleanValue());
        assertNull(timestampSerializer._customFormat);
        assertNull(timestampSerializer._reusedCustomFormat);

        // Test withBoolean and SimpleDateFormat
        DateTimeSerializerBase<Date> customFormatSerializer = dateSerializer.withFormat(
                Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertFalse(customFormatSerializer._useTimestamp.booleanValue());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), customFormatSerializer._customFormat);
        assertNull(customFormatSerializer._reusedCustomFormat);

        // Test withBoolean and AtomicReference
        DateTimeSerializerBase<Date> reusedCustomFormatSerializer = dateSerializer.withFormat(
                Boolean.FALSE, new AtomicReference<DateFormat>(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        assertFalse(reusedCustomFormatSerializer._useTimestamp.booleanValue());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), reusedCustomFormatSerializer._customFormat);
        assertNotNull(reusedCustomFormatSerializer._reusedCustomFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializers = new StandardSerializerFinder().findSerializer(
                Date.class, null);
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(
                Date.class, Boolean.FALSE, null);

        // Test null property
        JsonSerializer<?> serializer1 = dateSerializer.createContextual(serializers, null);
        assertEquals(dateSerializer, serializer1);

        // Test non-null property with no overrides
        JsonFormat.Value format1 = new JsonFormat.Value();
        JsonSerializer<?> serializer2 = dateSerializer.createContextual(serializers, new BeanPropertyModel(""));
        assertEquals(dateSerializer, serializer2);

        // Test non-null property with numeric format override
        JsonFormat.Value format2 = new JsonFormat.Value().shape(JsonFormat.Shape.NUMBER);
        JsonSerializer<?> serializer3 = dateSerializer.createContextual(serializers, new BeanPropertyModel(""));
        assertTrue(serializer3 instanceof DateTimeSerializerBase<?>);
        DateTimeSerializerBase<Date> numericSerializer = (DateTimeSerializerBase<Date>) serializer3;
        assertTrue(numericSerializer._useTimestamp.booleanValue());
        assertNull(numericSerializer._customFormat);
        assertNull(numericSerializer._reusedCustomFormat);

        // Test non-null property with string format override
        JsonFormat.Value format3 = new JsonFormat.Value().shape(JsonFormat.Shape.STRING).pattern("yyyy-MM-dd HH:mm:ss");
        JsonSerializer<?> serializer4 = dateSerializer.createContextual(serializers, new BeanPropertyModel(""));
        assertTrue(serializer4 instanceof DateTimeSerializerBase<?>);
        DateTimeSerializerBase<Date> stringSerializer = (DateTimeSerializerBase<Date>) serializer4;
        assertFalse(stringSerializer._useTimestamp.booleanValue());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), stringSerializer._customFormat);
        assertNotNull(stringSerializer._reusedCustomFormat);
    }

    // Add more test cases for other methods in DateTimeSerializerBase class

    private boolean assertNotEquals(Object expected, Object actual) {
        if (expected == actual) {
            throw new AssertionError("Expected: " + expected + " but was: " + actual);
        }
        return false;
    }
}