package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private static final Class<?> TYPE = DateTimeSerializerBase.class;
    private DateTimeSerializerBase<Date> serializer;

    @Before
    public void setup() {
        serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // implementation
            }

            @Override
            protected long _timestamp(Date value) {
                // implementation
            }
        };
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Date> result = serializer.withFormat(Boolean.FALSE, null);
        assertNotNull(result);
        assertSame(serializer, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithFormatNullCustomFormat() {
        serializer.withFormat(Boolean.FALSE, null);
    }

    @Test
    public void testWithFormatNonNullCustomFormat() {
        SimpleDateFormat format = new SimpleDateFormat();
        DateTimeSerializerBase<Date> result = serializer.withFormat(Boolean.FALSE, format);
        assertNotNull(result);
        assertSame(serializer, result);
    }

    @Test
    public void testCreateContextual() {
        SerializerProvider provider = null;
        BeanProperty property = null;
        ContextualSerializer result = serializer.createContextual(provider, property);
        assertNotNull(result);
        assertSame(serializer, result);
    }

    @Test
    public void testIsEmpty() {
        SerializerProvider provider = null;
        Date value = new Date();
        assertFalse(serializer.isEmpty(provider, value));
    }

    @Test
    public void testIsEmptyWithValueZeroTimestamp() {
        SerializerProvider provider = null;
        Date value = new Date(0L);
        assertFalse(serializer.isEmpty(provider, value));
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = null;
        String type = serializer._asTimestamp(provider) ? "number" : "string";
        JsonNode result = serializer.getSchema(provider, null);
        assertNotNull(result);
        assertEquals(type, result.asText());
    }

    @Test
    public void testGetSchemaAsString() {
        SerializerProvider provider = null;
        serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            // implementation
        };

        JsonNode result = serializer.getSchema(provider, null);
        assertNotNull(result);
        assertEquals("number", result.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        boolean asNumber = serializer._asTimestamp(visitor.getProvider());
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // verify visitor methods were called appropriately
    }

    @Test
    public void testSerializeAsStringWithDefaultFormat() {
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        Date value = new Date();

        serializer.serialize(value, gen, provider);

        // verify implementation details
    }

    @Test
    public void testSerializeAsStringWithCustomFormat() {
        serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat()) {
            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                _serializeAsString(value, gen, serializers);
            }
        };

        JsonGenerator gen = null;
        SerializerProvider provider = null;
        Date value = new Date();

        serializer.serialize(value, gen, provider);

        // verify implementation details
    }

}