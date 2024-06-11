package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
    @Mock
    private SerializerProvider serializers;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testConstructor() {
        Boolean useTimestamp = Boolean.TRUE;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, useTimestamp, customFormat) {

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

        };

        assertEquals(String.class, serializer.handledType());
        assertEquals(useTimestamp, serializer._useTimestamp);
        assertEquals(customFormat, serializer._customFormat);
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.OBJECT);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("yyyy-MM-dd");
        format.setLocale(Locale.ENGLISH);
        
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null) {

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

        };
        
        ContextualSerializer contextualSerializer = serializer.createContextual(serializers, null);
        assertNotNull(contextualSerializer);
        assertEquals(serializer, contextualSerializer);
        
        BeanProperty property = Mockito.mock(BeanProperty.class);
        Mockito.when(property.getMember()).thenReturn(null);
        contextualSerializer = serializer.createContextual(serializers, property);
        assertNotNull(contextualSerializer);
        assertEquals(serializer, contextualSerializer);
        
        property = Mockito.mock(BeanProperty.class);
        Annotated annotated = Mockito.mock(Annotated.class);
        Mockito.when(property.getMember()).thenReturn(annotated);
        Mockito.when(serializers.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);
        
        SimpleDateFormat expectedDateFormat = new SimpleDateFormat(format.getPattern(), format.getLocale());
        expectedDateFormat.setTimeZone(format.getTimeZone());
        
        DateFormat resultDateFormat = ((DateTimeSerializerBase<?>) serializer).createContextual(serializers, property)._customFormat;
        
        assertEquals(expectedDateFormat, resultDateFormat);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullProperty() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null) {

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

        };

        serializer.createContextual(serializers, null);
    }
    
    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null) {

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

        };

        assertTrue(serializer.isEmpty(null));
        assertFalse(serializer.isEmpty(serializers, "2020-01-01"));
    }
    
    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.TRUE, null) {

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

        };

        assertEquals("number", serializer.getSchema(serializers, null).get("type").asText());
        
        serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null) {

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

        };

        assertEquals("string", serializer.getSchema(serializers, null).get("type").asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.TRUE, null) {

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

        };

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(serializer, Mockito.times(1))._acceptJsonFormatVisitor(visitor, typeHint, true);

        serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null) {

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

        };

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(serializer, Mockito.times(1))._acceptJsonFormatVisitor(visitor, typeHint, false);
    }
    
    @Test
    public void testHelperMethods() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

        };
        
        assertNull(serializer._customFormat);
        
        assertNotNull(serializer._acceptJsonFormatVisitor(null, null, true));
        assertNotNull(serializer._acceptJsonFormatVisitor(null, null, false));
    }
}