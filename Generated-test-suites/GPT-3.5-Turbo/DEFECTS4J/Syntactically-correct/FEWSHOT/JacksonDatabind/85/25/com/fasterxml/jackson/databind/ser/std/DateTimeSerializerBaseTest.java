
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Test;

public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {

            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };

        assertNull(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {

            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };

        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        assertEquals(serializer, serializer.createContextual(serializers, property));
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {

            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };

        Object value = new Object();
        SerializerProvider serializers = mock(SerializerProvider.class);
        assertEquals(false, serializer.isEmpty(value));
        assertEquals(false, serializer.isEmpty(serializers, value));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {

            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };

        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        assertNull(serializer.getSchema(serializers, typeHint));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {

            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}
