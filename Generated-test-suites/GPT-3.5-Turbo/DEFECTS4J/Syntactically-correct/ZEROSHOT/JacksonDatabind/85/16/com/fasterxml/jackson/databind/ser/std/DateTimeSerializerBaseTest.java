
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private Class<Object> type;
    private Boolean useTimestamp;
    private DateFormat customFormat;
    private DateTimeSerializerBase<Object> dateTimeSerializerBase;

    @SuppressWarnings("unchecked")
    @Before
    public void init() {
        type = mock(Class.class);
        useTimestamp = false;
        customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateTimeSerializerBase = new DateTimeSerializerBase<Object>(type, useTimestamp, customFormat) {

            private static final long serialVersionUID = 1L;

            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        assertNull(dateTimeSerializerBase.createContextual(serializers, null));
        assertEquals(dateTimeSerializerBase, dateTimeSerializerBase.createContextual(null, property));
    }

    @Test
    public void testIsEmptyObject() {
        Object value = null;
        assertEquals(true, dateTimeSerializerBase.isEmpty(value));
        assertEquals(true, dateTimeSerializerBase.isEmpty(null, value));
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        assertEquals("{\"type\":\"number\",\"format\":\"UTC_MILLISEC\"}", dateTimeSerializerBase.getSchema(serializers, null).toString());
        customFormat = null;
        assertEquals("{\"type\":\"string\",\"format\":\"date-time\"}", dateTimeSerializerBase.getSchema(serializers, null).toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);
    }
}
