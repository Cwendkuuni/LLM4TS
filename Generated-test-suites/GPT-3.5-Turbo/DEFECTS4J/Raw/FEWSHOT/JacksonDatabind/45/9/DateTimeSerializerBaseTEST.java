package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Test;

public class DateTimeSerializerBaseTest {
    @Test
    public void testDateTimeSerializerBase() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestClass(String.class, true, null) {
            @Override
            public DateTimeSerializerBaseTestClass withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }

            @Override
            protected JsonNode createSchemaNode(String type, boolean isNumber) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                // Do nothing
            }
        };

        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestClass(String.class, true, null) {
            @Override
            public DateTimeSerializerBaseTestClass withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }

            @Override
            protected JsonNode createSchemaNode(String type, boolean isNumber) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                // Do nothing
            }
        };

        String value = "test";

        assertTrue(serializer.isEmpty(value));
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestClass(String.class, true, null) {
            @Override
            public DateTimeSerializerBaseTestClass withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toString());
            }

            @Override
            protected JsonNode createSchemaNode(String type, boolean isNumber) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                // Do nothing
            }
        };

        String value = "test";
        serializer.serialize(value, generator, provider);

        verify(generator, times(1)).writeString(value);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonFormat.Value value = mock(JsonFormat.Value.class);
        Annotated annotated = mock(Annotated.class);
        when(value.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(value.getPattern()).thenReturn("yyyy-MM-dd");
        when(value.hasLocale()).thenReturn(true);
        when(value.getLocale()).thenReturn(Locale.US);

        when(serializers.getAnnotationIntrospector().findFormat(annotated)).thenReturn(value);
        when(property.getMember()).thenReturn(annotated);

        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestClass(String.class, true, null) {
            @Override
            public DateTimeSerializerBaseTestClass withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }

            @Override
            protected JsonNode createSchemaNode(String type, boolean isNumber) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                // Do nothing
            }
        };

        DateTimeSerializerBase result = (DateTimeSerializerBase) serializer.createContextual(serializers, property);

        assertEquals(value, result._customFormat);

        verify(serializers, times(1)).getLocale();
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestClass(String.class, true, null) {
            @Override
            public DateTimeSerializerBaseTestClass withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }

            @Override
            protected JsonNode createSchemaNode(String type, boolean isNumber) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                // Do nothing
            }
        };

        String type = "string";
        boolean isNumber = true;

        JsonNode result = serializer.getSchema(serializers, String.class);

        assertEquals(type, result.get("type").asText());
        assertEquals(isNumber, result.get("isNumber").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestClass(String.class, true, null) {
            @Override
            public DateTimeSerializerBaseTestClass withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }

            @Override
            protected JsonNode createSchemaNode(String type, boolean isNumber) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                visitor.expectStringFormat(typeHint, JsonValueFormat.DATE_TIME);
            }
        };

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).expectStringFormat(typeHint, JsonValueFormat.DATE_TIME);
    }

    private abstract class DateTimeSerializerBaseTestClass<T> extends DateTimeSerializerBase<T> {
        protected DateTimeSerializerBaseTestClass(Class<T> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBaseTestClass<T> withFormat(Boolean timestamp, DateFormat customFormat) {
            // Do nothing
            return null;
        }
    }
}