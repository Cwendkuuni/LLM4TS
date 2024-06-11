package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
    private DateTimeSerializerBase<Object> serializer;

    @Before
    public void setup() {
        serializer = mock(DateTimeSerializerBase.class);
    }

    @Test
    public void testWithFormat() {
        Boolean useTimestamp = true;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeSerializerBase result = serializer.withFormat(useTimestamp, customFormat);

        assertEquals(useTimestamp, result._useTimestamp);
        assertEquals(customFormat, result._customFormat);
    }

    @Test
    public void testCreateContextualWithNull() {
        BeanProperty property = null;
        SerializerProvider serializers = mock(SerializerProvider.class);

        DateTimeSerializerBase result = serializer.createContextual(serializers, property);

        assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextualWithFormatNull() {
        BeanProperty property = mock(BeanProperty.class);
        Annotated annotated = mock(Annotated.class);
        when(property.getMember()).thenReturn(annotated);
        when(annotated.getAccessor()).thenReturn(null);
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(serializers.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.ANY);

        DateTimeSerializerBase result = serializer.createContextual(serializers, property);

        assertEquals(Boolean.TRUE, result._useTimestamp);
        assertNull(result._customFormat);
    }

    @Test
    public void testCreateContextualWithFormatNumeric() {
        BeanProperty property = mock(BeanProperty.class);
        Annotated annotated = mock(Annotated.class);
        when(property.getMember()).thenReturn(annotated);
        when(annotated.getAccessor()).thenReturn(null);
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(serializers.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);

        DateTimeSerializerBase result = serializer.createContextual(serializers, property);

        assertEquals(Boolean.TRUE, result._useTimestamp);
        assertNull(result._customFormat);
    }

    @Test
    public void testCreateContextualWithFormatString() {
        BeanProperty property = mock(BeanProperty.class);
        Annotated annotated = mock(Annotated.class);
        when(property.getMember()).thenReturn(annotated);
        when(annotated.getAccessor()).thenReturn(null);
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(serializers.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);

        DateTimeSerializerBase result = serializer.createContextual(serializers, property);

        assertEquals(Boolean.FALSE, result._useTimestamp);
        SimpleDateFormat expectedDateFormat = new SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601,
                serializers.getLocale());
        assertEquals(expectedDateFormat.getTimeZone(), result._customFormat.getTimeZone());
    }

    @Test
    public void testIsEmpty() {
        Object value = new Object();
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);

        boolean result = serializer.isEmpty(serializers, value);

        assertEquals(false, result);
    }

    @Test
    public void testIsEmptyWithNull() {
        Object value = null;
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);

        boolean result = serializer.isEmpty(serializers, value);

        assertEquals(true, result);
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        JsonNode result = serializer.getSchema(serializers, typeHint);

        assertEquals(JsonNodeType.STRING, result.getNodeType());
    }
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(visitor.getProvider()).thenReturn(mock(SerializerProvider.class));

        serializer._acceptJsonFormatVisitor(visitor, typeHint, true);

        verify(visitor).expectIntFormat(typeHint, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
    }

    @Test
    public void testSerialize() throws IOException {
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        serializer.serialize(value, gen, serializers);

        verify(serializer).serialize(value, gen, serializers);
    }
}