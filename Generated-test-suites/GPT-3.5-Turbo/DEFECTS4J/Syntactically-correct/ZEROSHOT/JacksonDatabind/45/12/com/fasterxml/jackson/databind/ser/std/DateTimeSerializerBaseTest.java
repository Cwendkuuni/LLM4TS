package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.fasterxml.jackson.databind.*;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase serializer;

    @Test
    public void testWithFormat() {
        serializer = new DateTimeSerializerBaseTestImpl(Boolean.TRUE, DateFormat.getInstance()) {
            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };
        DateTimeSerializerBase result = serializer.withFormat(Boolean.FALSE, DateFormat.getInstance());
        assertNotNull(result);
    }

    @Test
    public void testCreateContextual() {
        serializer = new DateTimeSerializerBaseTestImpl(Boolean.TRUE, DateFormat.getInstance()) {
            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };
        JsonSerializer<?> result = serializer.createContextual(null, null);
        assertNotNull(result);
    }

    @Test
    public void testIsEmpty() {
        serializer = new DateTimeSerializerBaseTestImpl(Boolean.TRUE, DateFormat.getInstance()) {
            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };
        boolean empty = serializer.isEmpty(null, null);
        assertTrue(empty);
    }

    @Test
    public void testGetSchema() {
        serializer = new DateTimeSerializerBaseTestImpl(Boolean.TRUE, DateFormat.getInstance()) {
            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };
        JsonNode node = serializer.getSchema(null, null);
        assertNotNull(node);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        serializer = new DateTimeSerializerBaseTestImpl(Boolean.TRUE, DateFormat.getInstance()) {
            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };
        SimpleVisitorWrapper visitor = new SimpleVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertTrue(visitor.isVisited());
    }

    private static class DateTimeSerializerBaseTestImpl extends DateTimeSerializerBase {

        public DateTimeSerializerBaseTestImpl(Boolean useTimestamp, DateFormat customFormat) {
            super(Object.class, useTimestamp, customFormat);
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
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
        }
    }

    private static class SimpleVisitorWrapper implements JsonFormatVisitorWrapper {

        private boolean visited = false;

        @Override
        public SerializerProvider getProvider() {
            return null;
        }

        @Override
        public JsonObjectFormatVisitor expectObjectFormat(JavaType type) {
            return null;
        }

        @Override
        public JsonArrayFormatVisitor expectArrayFormat(JavaType type) {
            return null;
        }

        @Override
        public JsonStringFormatVisitor expectStringFormat(JavaType type) {
            return null;
        }

        @Override
        public JsonNumberFormatVisitor expectNumberFormat(JavaType type) {
            return null;
        }

        @Override
        public JsonIntegerFormatVisitor expectIntegerFormat(JavaType type) {
            return null;
        }

        @Override
        public JsonBooleanFormatVisitor expectBooleanFormat(JavaType type) {
            return null;
        }

        @Override
        public JsonNullFormatVisitor expectNullFormat(JavaType type) {
            return null;
        }

        @Override
        public JsonAnyFormatVisitor expectAnyFormat(JavaType type) {
            return null;
        }

        @Override
        public void setProvider(SerializerProvider provider) {
        }
        
        public boolean isVisited() {
            return visited;
        }
    }
}