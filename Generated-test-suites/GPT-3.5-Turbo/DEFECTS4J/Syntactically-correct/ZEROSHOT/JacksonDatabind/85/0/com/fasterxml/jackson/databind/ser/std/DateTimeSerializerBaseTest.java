package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<Object>(Object.class, timestamp, customFormat) {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

                    }

                    @Override
                    protected long _timestamp(Object value) {
                        return 0;
                    }
                };
            }

            @Override
            public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };

        DateTimeSerializerBase<Object> newSerializer = serializer.withFormat(true, null);
        Assert.assertNotNull(newSerializer);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<Object>(Object.class, timestamp, customFormat) {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

                    }

                    @Override
                    protected long _timestamp(Object value) {
                        return 0;
                    }
                };
            }

            @Override
            public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };

        Assert.assertTrue(serializer.isEmpty(null, null));
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<Object>(Object.class, timestamp, customFormat) {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

                    }

                    @Override
                    protected long _timestamp(Object value) {
                        return 0;
                    }
                };
            }

            @Override
            public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };

        JsonGenerator generator = null;
        SerializerProvider provider = null;
        serializer.serialize(null, generator, provider);
    }

    @Test
    public void testFormatSchema() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<Object>(Object.class, timestamp, customFormat) {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

                    }

                    @Override
                    protected long _timestamp(Object value) {
                        return 0;
                    }
                };
            }

            @Override
            public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };

        SerializerProvider provider = null;
        Type typeHint = null;
        JsonNode schema = serializer.getSchema(provider, typeHint);
        Assert.assertNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<Object>(Object.class, timestamp, customFormat) {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

                    }

                    @Override
                    protected long _timestamp(Object value) {
                        return 0;
                    }
                };
            }

            @Override
            public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                visitor.expectNumberFormat(typeHint);
            }
        };

        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}