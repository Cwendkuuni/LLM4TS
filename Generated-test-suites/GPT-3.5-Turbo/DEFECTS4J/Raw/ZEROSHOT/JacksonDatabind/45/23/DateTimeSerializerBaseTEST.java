package com.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Mock
    private JsonSerializer<?> mockedSerializer;
    @Mock
    private SerializerProvider mockedSerializerProvider;
    @Mock
    private BeanProperty mockedProperty;
    @Mock
    private JsonFormat.Value mockedFormat;
    @Mock
    private Annotated mockedAnnotated;
    @Mock
    private JsonFormatVisitorWrapper mockedVisitor;
    @Mock
    private JavaType mockedType;

    private DateTimeSerializerBase<Object> testedSerializer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateContextual_WithNullProperty_ReturnsThis() throws JsonMappingException {
        testedSerializer = new DateTimeSerializerBase<Object>(Object.class, true, mock(DateFormat.class)) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        ContextualSerializer result = testedSerializer.createContextual(mockedSerializerProvider, null);

        assertEquals(testedSerializer, result);
    }

    @Test
    public void testCreateContextual_WithNonNullIntrospector_ReturnsThis() throws JsonMappingException {
        when(mockedSerializerProvider.getAnnotationIntrospector().findFormat(mockedAnnotated)).thenReturn(mockedFormat);
        when(mockedFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);

        testedSerializer = new DateTimeSerializerBase<Object>(Object.class, true, mock(DateFormat.class)) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        ContextualSerializer result = testedSerializer.createContextual(mockedSerializerProvider, mockedProperty);

        assertEquals(testedSerializer, result);
    }

    @Test
    public void testCreateContextual_WithNonNullIntrospectorHasPattern_ReturnsThis() throws JsonMappingException {
        when(mockedSerializerProvider.getAnnotationIntrospector().findFormat(mockedAnnotated)).thenReturn(mockedFormat);
        when(mockedFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(mockedFormat.hasPattern()).thenReturn(true);
        when(mockedFormat.getPattern()).thenReturn("yyyy-MM-dd");

        testedSerializer = new DateTimeSerializerBase<Object>(Object.class, true, mock(DateFormat.class)) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        ContextualSerializer result = testedSerializer.createContextual(mockedSerializerProvider, mockedProperty);

        assertEquals(testedSerializer, result);
    }

    @Test
    public void testCreateContextual_WithNonNullIntrospectorHasLocale_ReturnsThis() throws JsonMappingException {
        when(mockedSerializerProvider.getAnnotationIntrospector().findFormat(mockedAnnotated)).thenReturn(mockedFormat);
        when(mockedFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(mockedFormat.hasLocale()).thenReturn(true);
        when(mockedFormat.getLocale()).thenReturn(Locale.US);

        testedSerializer = new DateTimeSerializerBase<Object>(Object.class, true, mock(DateFormat.class)) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        ContextualSerializer result = testedSerializer.createContextual(mockedSerializerProvider, mockedProperty);

        assertEquals(testedSerializer, result);
    }

    @Test
    public void testCreateContextual_WithNonNullIntrospectorAndNullTimeZone_ReturnsThis() throws JsonMappingException {
        when(mockedSerializerProvider.getAnnotationIntrospector().findFormat(mockedAnnotated)).thenReturn(mockedFormat);
        when(mockedFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(mockedFormat.getTimeZone()).thenReturn(null);

        testedSerializer = new DateTimeSerializerBase<Object>(Object.class, true, mock(DateFormat.class)) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        ContextualSerializer result = testedSerializer.createContextual(mockedSerializerProvider, mockedProperty);

        assertEquals(testedSerializer, result);
    }

    @Test
    public void testCreateContextual_WithNonNullIntrospectorAndNonNullTimeZone_ReturnsThis() throws JsonMappingException {
        when(mockedSerializerProvider.getAnnotationIntrospector().findFormat(mockedAnnotated)).thenReturn(mockedFormat);
        when(mockedFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(mockedFormat.getTimeZone()).thenReturn(TimeZone.getDefault());

        testedSerializer = new DateTimeSerializerBase<Object>(Object.class, true, mock(DateFormat.class)) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        ContextualSerializer result = testedSerializer.createContextual(mockedSerializerProvider, mockedProperty);

        assertEquals(testedSerializer, result);
    }

    @Test
    public void testIsEmpty_WithNonNullValue_returnsFalse() {
        testedSerializer = new DateTimeSerializerBase<Object>(Object.class, true, mock(DateFormat.class)) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 1L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        boolean result = testedSerializer.isEmpty(mockedSerializerProvider, new Object());

        assertEquals(false, result);
    }

    @Test
    public void testIsEmpty_WithNullValue_returnsTrue() {
        testedSerializer = new DateTimeSerializerBase<Object>(Object.class, true, mock(DateFormat.class)) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        boolean result = testedSerializer.isEmpty(mockedSerializerProvider, null);

        assertEquals(true, result);
    }

    @Test
    public void testGetSchema_WithAsTimestampTrue_returnsJsonNumberNode() {
        testedSerializer = new DateTimeSerializerBase<Object>(Object.class, true, mock(DateFormat.class)) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 1L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        JsonNode result = testedSerializer.getSchema(mockedSerializerProvider, null);

        assertEquals("number", result.asText());
    }

    @Test
    public void testGetSchema_WithAsTimestampFalse_returnsJsonStringNode() {
        testedSerializer = new DateTimeSerializerBase<Object>(Object.class, false, mock(DateFormat.class)) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 1L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        JsonNode result = testedSerializer.getSchema(mockedSerializerProvider, null);

        assertEquals("string", result.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsNumber() throws JsonMappingException {
        testedSerializer = new DateTimeSerializerBase<Object>(Object.class, false, mock(DateFormat.class)) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 1L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        testedSerializer.acceptJsonFormatVisitor(mockedVisitor, mockedType);

        // assert visitIntFormat
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsString() throws JsonMappingException {
        testedSerializer = new DateTimeSerializerBase<Object>(Object.class, true, mock(DateFormat.class)) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 1L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };

        testedSerializer.acceptJsonFormatVisitor(mockedVisitor, mockedType);

        // assert visitStringFormat
    }

}