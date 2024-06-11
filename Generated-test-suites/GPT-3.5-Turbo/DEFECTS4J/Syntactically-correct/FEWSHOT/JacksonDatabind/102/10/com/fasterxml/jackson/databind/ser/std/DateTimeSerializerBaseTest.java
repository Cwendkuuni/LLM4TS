package com.fasterxml.jackson.databind.ser.std;

import java.util.Date;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class DateTimeSerializerBaseTest {

    @Mock
    private JsonGenerator mockJsonGenerator;

    @Mock
    private SerializerProvider mockSerializerProvider;

    @Mock
    private JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper;

    @Mock
    private JavaType mockJavaType;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl(Date.class, null, null) {
            @Override
            protected long _timestamp(Date value) {
                return 0L;
            }

            @Override
            protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                // do something
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // do something
            }
        };
        Date value = new Date();

        serializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        verify(serializer).serialize(eq(value), eq(mockJsonGenerator), eq(mockSerializerProvider));
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl(Date.class, null, null) {
            @Override
            protected long _timestamp(Date value) {
                return 0L;
            }

            @Override
            public boolean isEmpty(SerializerProvider serializers, Object value) {
                return false;
            }
        };
        Date value = new Date();

        boolean result = serializer.isEmpty(mockSerializerProvider, value);

        assertFalse(result);
        verify(serializer).isEmpty(eq(mockSerializerProvider), eq(value));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl(Date.class, null, null) {
            @Override
            protected long _timestamp(Date value) {
                return 0L;
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }
        };
        Type typeHint = Date.class;

        JsonNode result = serializer.getSchema(mockSerializerProvider, typeHint);

        assertNull(result);
        verify(serializer).getSchema(eq(mockSerializerProvider), eq(typeHint));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl(Date.class, null, null) {
            @Override
            protected long _timestamp(Date value) {
                return 0L;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                // do something
            }
        };

        serializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);

        verify(serializer).acceptJsonFormatVisitor(eq(mockJsonFormatVisitorWrapper), eq(mockJavaType));
    }

    private static class DateTimeSerializerBaseImpl extends DateTimeSerializerBase {

        public DateTimeSerializerBaseImpl(Class type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Object value) {
            return 0L;
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // do something
        }

        @Override
        public void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber) throws JsonMappingException {
            // do something
        }

        @Override
        protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
            // do something
        }
    }
}