package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;

public class DateTimeSerializerBaseTest {

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider serializers;

    @Mock
    private BeanProperty property;

    @Mock
    private Annotated annotated;

    @Mock
    private JsonFormatVisitorWrapper visitor;

    @Mock
    private JavaType typeHint;

    private DateTimeSerializerBase<Object> serializer;

    private DateFormat dateFormat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Object> newSerializer = serializer.withFormat(true, null);
        assertTrue(newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat);

        newSerializer = serializer.withFormat(false, dateFormat);
        assertFalse(newSerializer._useTimestamp);
        assertSame(dateFormat, newSerializer._customFormat);
    }

    @Test
    public void testCreateContextualWithNullProperty() throws JsonMappingException {
        serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {

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
        
        assertNull(serializer.createContextual(serializers, null));
    }

    @Test
    public void testCreateContextualWithFormat() throws JsonMappingException {
        when(serializers.getAnnotationIntrospector()).thenReturn(null);

        serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {

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
        
        assertNull(serializer.createContextual(serializers, property));
    }
    
    @Test
    public void testCreateContextualWithShapeIsNumeric() throws JsonMappingException {
        when(property.getMember()).thenReturn(annotated);
        when(serializers.getAnnotationIntrospector()).thenReturn(null);
        when(visitor.getProvider()).thenReturn(serializers);
        when(visitor.getProvider().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);
        
        serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {

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
        
        DateTimeSerializerBase<Object> newSerializer = (DateTimeSerializerBase<Object>) serializer.createContextual(serializers, property);
        assertTrue(newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat);
    }

    @Test
    public void testCreateContextualWithStringFormat() throws JsonMappingException {
        when(property.getMember()).thenReturn(annotated);
        when(serializers.getAnnotationIntrospector()).thenReturn(null);
        when(visitor.getProvider()).thenReturn(serializers);
        when(visitor.getProvider().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
        when(format.getTimeZone()).thenReturn(null);
        when(format.hasPattern()).thenReturn(true);
        when(format.getPattern()).thenReturn("yyyy-MM-dd'T'HH:mm:ss");
        when(format.hasLocale()).thenReturn(true);
        when(format.getLocale()).thenReturn(Locale.US);
        when(format.getTimeZone()).thenReturn(null);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getTimeZone("GMT+00:00"));
        
        serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {

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
        
        DateTimeSerializerBase<Object> newSerializer = (DateTimeSerializerBase<Object>) serializer.createContextual(serializers, property);
        assertFalse(newSerializer._useTimestamp);
        assertNotNull(newSerializer._customFormat);
        assertTrue(newSerializer._customFormat instanceof SimpleDateFormat);
        SimpleDateFormat sdf = (SimpleDateFormat) newSerializer._customFormat;
        assertEquals("yyyy-MM-dd'T'HH:mm:ss", sdf.toPattern());
        assertEquals(Locale.US, sdf.getLocale());
        assertEquals(TimeZone.getTimeZone("GMT+00:00"), sdf.getTimeZone());
    }

    @Test
    public void testIsEmptyWithValueNull() {
        assertTrue(serializer.isEmpty(serializers, null));
    }

    @Test
    public void testIsEmptyWithTimestamp0() {
        Object value = new Object() {

            @Override
            public String toString() {
                return "timestamp";
            }

        };

        when(serializer._timestamp(value)).thenReturn(0L);
        
        assertTrue(serializer.isEmpty(serializers, value));
    }

    @Test
    public void testIsEmptyWithValueNotNullAndTimestampNot0() {
        Object value = new Object() {

            @Override
            public String toString() {
                return "timestamp";
            }

        };

        when(serializer._timestamp(value)).thenReturn(1000L);
        
        assertFalse(serializer.isEmpty(serializers, value));
    }

    @Test
    public void testGetSchemaWithAsTimestamp() {
        when(serializer._asTimestamp(serializers)).thenReturn(true);
        
        JsonNode node = serializer.getSchema(serializers, Object.class);
        
        assertEquals("number", node.get("type").textValue());
        assertTrue(node.get("required").booleanValue());
    }

    @Test
    public void testGetSchemaWithoutAsTimestamp() {
        when(serializer._asTimestamp(serializers)).thenReturn(false);
        
        JsonNode node = serializer.getSchema(serializers, Object.class);
        
        assertEquals("string", node.get("type").textValue());
        assertTrue(node.get("required").booleanValue());
        assertEquals(JsonValueFormat.DATE_TIME.getValue(), node.get("format").textValue());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithAsTimestamp() throws JsonMappingException {
        when(visitor.getProvider()).thenReturn(serializers);
        when(visitor.getProvider().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);
        
        serializer._acceptJsonFormatVisitor(visitor, typeHint, true);
        
        verify(visitor).expectInteger(typeHint, JsonParser.NumberType.LONG);
        verify(visitor).getProvider();
    }

    @Test
    public void testAcceptJsonFormatVisitorWithoutAsTimestamp() throws JsonMappingException {
        when(visitor.getProvider()).thenReturn(serializers);
        when(visitor.getProvider().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
        
        serializer._acceptJsonFormatVisitor(visitor, typeHint, false);
        
        verify(visitor).expectString(typeHint);
        verify(visitor).setFormat(JsonValueFormat.DATE_TIME);
    }
    
    @Test
    public void testSerialize() throws IOException {
        Object value = new Object() {

            @Override
            public String toString() {
                return "timestamp";
            }

        };
        
        serializer.serialize(value, gen, serializers);
        
        verify(gen).writeStartObject();
        verify(gen).writeEndObject();
    }

}