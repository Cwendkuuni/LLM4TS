package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
    
    private DateTimeSerializerBase<String> serializer;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBase<String>(String.class, true, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // custom serialization method
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }
        };
    }

    @Test
    public void testCreateContextualWithNullProperty() throws JsonMappingException {
        JsonSerializer<?> result = serializer.createContextual(null, null);
        assertSame(serializer, result);
    }

    @Test
    public void testCreateContextualWithNumericShape() throws JsonMappingException {
        JsonSerializer<?> result = serializer.createContextual(null, new BeanProperty() {
            @Override
            public PropertyName getSimpleName() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public Class<?>[] getViews() {
                return new Class[0];
            }

            @Override
            public JsonFormat.Value findFormatOverrides(SerializerProvider serializers, 
                BeanProperty property, Class<?> type) {
                return new JsonFormat.Value();
            }
        });
        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializer = (DateTimeSerializerBase<?>) result;
        assertEquals(Boolean.TRUE, dateTimeSerializer._useTimestamp);
        assertNull(dateTimeSerializer._customFormat);
    }

    @Test
    public void testCreateContextualWithStringShape() throws JsonMappingException {
        JsonSerializer<?> result = serializer.createContextual(null, new BeanProperty() {
            @Override
            public PropertyName getSimpleName() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public Class<?>[] getViews() {
                return new Class[0];
            }

            @Override
            public JsonFormat.Value findFormatOverrides(SerializerProvider serializers, 
                BeanProperty property, Class<?> type) {
                JsonFormat.Value value = new JsonFormat.Value();
                value.setShape(JsonFormat.Shape.STRING);
                return value;
            }
        });
        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializer = (DateTimeSerializerBase<?>) result;
        assertEquals(Boolean.FALSE, dateTimeSerializer._useTimestamp);
        assertNotNull(dateTimeSerializer._customFormat);
    }

    @Test
    public void testCreateContextualWithPattern() throws JsonMappingException {
        JsonSerializer<?> result = serializer.createContextual(null, new BeanProperty() {
            @Override
            public PropertyName getSimpleName() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public Class<?>[] getViews() {
                return new Class[0];
            }

            @Override
            public JsonFormat.Value findFormatOverrides(SerializerProvider serializers, 
                BeanProperty property, Class<?> type) {
                JsonFormat.Value value = new JsonFormat.Value();
                value.setPattern("yyyy-MM-dd");
                return value;
            }
        });
        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializer = (DateTimeSerializerBase<?>) result;
        assertEquals(Boolean.FALSE, dateTimeSerializer._useTimestamp);
        assertNotNull(dateTimeSerializer._customFormat);
        assertEquals("yyyy-MM-dd", ((DateFormat) dateTimeSerializer._customFormat).toPattern());
    }

    @Test
    public void testCreateContextualWithLocale() throws JsonMappingException {
        JsonSerializer<?> result = serializer.createContextual(null, new BeanProperty() {
            @Override
            public PropertyName getSimpleName() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public Class<?>[] getViews() {
                return new Class[0];
            }

            @Override
            public JsonFormat.Value findFormatOverrides(SerializerProvider serializers, 
                BeanProperty property, Class<?> type) {
                JsonFormat.Value value = new JsonFormat.Value();
                value.setLocale(Locale.US);
                return value;
            }
        });
        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializer = (DateTimeSerializerBase<?>) result;
        assertEquals(Boolean.FALSE, dateTimeSerializer._useTimestamp);
        assertNotNull(dateTimeSerializer._customFormat);
        assertEquals(Locale.US, ((DateFormat) dateTimeSerializer._customFormat).getLocale());
    }

    @Test
    public void testCreateContextualWithTimeZone() throws JsonMappingException {
        JsonSerializer<?> result = serializer.createContextual(null, new BeanProperty() {
            @Override
            public PropertyName getSimpleName() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public Class<?>[] getViews() {
                return new Class[0];
            }

            @Override
            public JsonFormat.Value findFormatOverrides(SerializerProvider serializers, 
                BeanProperty property, Class<?> type) {
                JsonFormat.Value value = new JsonFormat.Value();
                value.setTimeZone(TimeZone.getTimeZone("GMT+2"));
                return value;
            }
        });
        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializer = (DateTimeSerializerBase<?>) result;
        assertEquals(Boolean.FALSE, dateTimeSerializer._useTimestamp);
        assertNotNull(dateTimeSerializer._customFormat);
        assertEquals("GMT+02:00", ((DateFormat) dateTimeSerializer._customFormat).getTimeZone().getID());
    }

    @Test
    public void testIsEmptyWithValueIsNull() throws IOException {
        boolean result = serializer.isEmpty(null);
        assertTrue(result);
    }

    @Test
    public void testIsEmptyWithValueIsNullUsingProvider() throws IOException {
        boolean result = serializer.isEmpty(null, null);
        assertTrue(result);
    }

    @Test
    public void testIsEmptyWithValueIsNotNull() throws IOException {
        boolean result = serializer.isEmpty("value");
        assertFalse(result);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithAsNumber() throws JsonMappingException {
        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper.Base() {
            @Override
            public void expectNumberFormat(JavaType convertedType) throws JsonMappingException {
                // custom expectNumberFormat method
            }
        }, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithAsString() throws JsonMappingException {
        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper.Base() {
            @Override
            public void expectStringFormat(JavaType convertedType) throws JsonMappingException {
                // custom expectStringFormat method
            }
        }, null);
    }
}