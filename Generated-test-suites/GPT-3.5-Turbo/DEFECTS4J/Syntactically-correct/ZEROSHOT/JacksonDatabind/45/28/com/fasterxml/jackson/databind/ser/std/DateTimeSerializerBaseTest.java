package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> dateTimeSerializerBase;

    @Before
    public void setUp() {
        dateTimeSerializerBase = new DateTimeSerializerBase<Date>(
                Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };
    }

    @Test
    public void testCreateContextualWhenPropertyIsNull() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);

        JsonSerializer<?> result = dateTimeSerializerBase.createContextual(serializers, null);

        assertSame(dateTimeSerializerBase, result);
    }

    @Test
    public void testCreateContextualWhenFormatIsNull() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        Annotated annotated = mock(Annotated.class);
        BeanProperty beanProperty = mock(BeanProperty.class);
        when(beanProperty.getMember()).thenReturn(annotated);
        when(serializers.getAnnotationIntrospector())
                .thenReturn(mock(AnnotationIntrospector.class));
        when(serializers.getAnnotationIntrospector()
                .findFormat(annotated))
                .thenReturn(null);

        JsonSerializer<?> result = dateTimeSerializerBase.createContextual(serializers, beanProperty);

        assertSame(dateTimeSerializerBase, result);
    }

    @Test
    public void testCreateContextualWhenShapeIsNumeric() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        Annotated annotated = mock(Annotated.class);
        BeanProperty beanProperty = mock(BeanProperty.class);
        when(beanProperty.getMember()).thenReturn(annotated);
        when(serializers.getAnnotationIntrospector())
                .thenReturn(mock(AnnotationIntrospector.class));
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);
        when(serializers.getAnnotationIntrospector()
                .findFormat(annotated))
                .thenReturn(format);

        JsonSerializer<?> result = dateTimeSerializerBase.createContextual(serializers, beanProperty);

        assertFalse(dateTimeSerializerBase._asTimestamp(serializers));
        assertNull(dateTimeSerializerBase._customFormat);
        assertTrue(result.withFormat(true, null)._useTimestamp);
    }

    @Test
    public void testCreateContextualWhenShapeIsString() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        Annotated annotated = mock(Annotated.class);
        BeanProperty beanProperty = mock(BeanProperty.class);
        when(beanProperty.getMember()).thenReturn(annotated);
        when(serializers.getAnnotationIntrospector())
                .thenReturn(mock(AnnotationIntrospector.class));
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.getTimeZone()).thenReturn(null);
        when(format.hasPattern()).thenReturn(false);
        when(format.hasLocale()).thenReturn(false);
        when(serializers.getAnnotationIntrospector()
                .findFormat(annotated))
                .thenReturn(format);

        JsonSerializer<?> result = dateTimeSerializerBase.createContextual(serializers, beanProperty);
        TimeZone timeZone = TimeZone.getDefault();
        Locale locale = serializers.getLocale();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale);
        df.setTimeZone(timeZone);

        assertFalse(dateTimeSerializerBase._asTimestamp(serializers));
        assertEquals(df, dateTimeSerializerBase._customFormat);
        assertFalse(result.withFormat(false, df)._useTimestamp);
    }

    @Test
    public void testIsEmptyWhenValueIsNullAndTimestampIsZero() {
        assertTrue(dateTimeSerializerBase.isEmpty((Date) null));
    }

    @Test
    public void testIsEmptyWhenValueIsNullAndTimestampIsNotZero() {
        assertFalse(dateTimeSerializerBase.isEmpty(mock(Date.class)));
    }

    @Test
    public void testIsEmptyWhenValueIsNotNullAndTimestampIsZero() {
        assertTrue(dateTimeSerializerBase.isEmpty(mock(Date.class)));
    }

    @Test
    public void testIsEmptyWhenValueIsNotNullAndTimestampIsNotZero() {
        assertFalse(dateTimeSerializerBase.isEmpty(mock(SerializerProvider.class), mock(Date.class)));
    }

    @Test
    public void testGetSchemaWhenAsTimestamp() {
        SerializerProvider serializers = mock(SerializerProvider.class);

        JsonNode result = dateTimeSerializerBase.getSchema(serializers, mock(Type.class));

        assertEquals("number", result.getType().name());
        assertTrue(((ObjectNode) result).get("required").asBoolean());
    }

    @Test
    public void testGetSchemaWhenNotAsTimestamp() {
        SerializerProvider serializers = mock(SerializerProvider.class);

        JsonNode result = dateTimeSerializerBase.getSchema(serializers, mock(Type.class));

        assertEquals("string", result.getType().name());
        assertTrue(((ObjectNode) result).get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitorWhenAsTimestamp() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(visitor.getProvider()).thenReturn(mock(SerializerProvider.class));

        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);

        String formatName = JsonValueFormat.UTC_MILLISEC.getValue();
        verify(visitor).expectNumberFormat(typeHint, JsonParser.NumberType.LONG);
        verify(visitor).expectFormat(typeHint, formatName);
    }

    @Test
    public void testAcceptJsonFormatVisitorWhenNotAsTimestamp() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(visitor.getProvider()).thenReturn(mock(SerializerProvider.class));
        when(visitor.getProvider().getLocale()).thenReturn(null);

        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);

        String formatName = JsonValueFormat.DATE_TIME.getValue();
        verify(visitor).expectStringFormat(typeHint);
        verify(visitor).expectFormat(typeHint, formatName);
    }
}