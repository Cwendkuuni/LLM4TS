package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase serializer;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBaseStub();
    }

    @Test
    public void testWithFormat() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeSerializerBase<DateTimeSerializerBaseStub> result = serializer.withFormat(true, customFormat);

        assertEquals(true, result._useTimestamp);
        assertEquals(customFormat, result._customFormat);
    }

    @Test
    public void testCreateContextualWithPropertyNull() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = null;
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextualWithFormatNull() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(property.getMember()).thenReturn(null);

        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextualWithShapeNumeric() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(property.getMember()).thenReturn(null);
        JsonFormat.Value format = new JsonFormat.Value().withShape(JsonFormat.Shape.NUMBER);
        when(serializers.getAnnotationIntrospector().findFormat((Annotated) property.getMember())).thenReturn(format);

        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        assertEquals(true, ((DateTimeSerializerBaseStub) result)._useTimestamp);
        assertEquals(null, ((DateTimeSerializerBaseStub) result)._customFormat);
    }

    @Test
    public void testCreateContextualWithShapeString() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(property.getMember()).thenReturn(null);
        JsonFormat.Value format = new JsonFormat.Value().withShape(JsonFormat.Shape.STRING);
        when(serializers.getAnnotationIntrospector().findFormat((Annotated) property.getMember())).thenReturn(format);

        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        assertEquals(false, ((DateTimeSerializerBaseStub) result)._useTimestamp);
        assertEquals(((DateTimeSerializerBaseStub) result)._customFormat.getTimeZone(), serializers.getTimeZone());
    }

    @Test
    public void testCreateContextualWithPatternAndLocale() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(property.getMember()).thenReturn(null);
        JsonFormat.Value format = new JsonFormat.Value()
                .withShape(JsonFormat.Shape.STRING)
                .withPattern("yyyy-MM-dd")
                .withLocale(Locale.US);
        when(serializers.getAnnotationIntrospector().findFormat((Annotated) property.getMember())).thenReturn(format);

        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        assertEquals(false, ((DateTimeSerializerBaseStub) result)._useTimestamp);
        assertEquals(
                ((DateTimeSerializerBaseStub) result)._customFormat.getTimeZone(),
                serializers.getTimeZone()
        );
        assertEquals("yyyy-MM-dd", ((DateTimeSerializerBaseStub) result)._customFormat.toPattern());
        assertEquals(Locale.US, ((DateTimeSerializerBaseStub) result)._customFormat.getLocale());
    }

    @Test
    public void testIsEmptyWithValueNull() {
        DateTimeSerializerBaseStub mockedSerializer = mock(DateTimeSerializerBaseStub.class);
        when(mockedSerializer._timestamp(any())).thenReturn(0L);

        boolean result = mockedSerializer.isEmpty(null);

        assertEquals(true, result);
    }

    @Test
    public void testIsEmptyWithValueNotNull() {
        DateTimeSerializerBaseStub mockedSerializer = mock(DateTimeSerializerBaseStub.class);
        when(mockedSerializer._timestamp(any())).thenReturn(1L);

        boolean result = mockedSerializer.isEmpty(new DateTimeSerializerBaseStub());

        assertEquals(false, result);
    }

    @Test
    public void testIsEmptyWithProviderNull() {
        DateTimeSerializerBaseStub mockedSerializer = mock(DateTimeSerializerBaseStub.class);
        mockedSerializer._customFormat = null;
        mockedSerializer._useTimestamp = null;

        boolean result = mockedSerializer.isEmpty(null, new DateTimeSerializerBaseStub());

        assertEquals(false, result);
    }

    @Test
    public void testIsEmptyWithCustomFormatNull() {
        DateTimeSerializerBaseStub mockedSerializer = mock(DateTimeSerializerBaseStub.class);
        mockedSerializer._customFormat = null;
        when(mockedSerializer._asTimestamp(null)).thenReturn(true);

        boolean result = mockedSerializer.isEmpty(null, new DateTimeSerializerBaseStub());

        assertEquals(false, result);
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        DateTimeSerializerBaseStub mockedSerializer = mock(DateTimeSerializerBaseStub.class);
        JsonNode expectedResult = mock(JsonNode.class);
        when(mockedSerializer._asTimestamp(serializers)).thenReturn(true);
        when(mockedSerializer.createSchemaNode("number", true)).thenReturn(expectedResult);

        JsonNode result = mockedSerializer.getSchema(serializers, null);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithAsNumberTrue() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        DateTimeSerializerBaseStub mockedSerializer = mock(DateTimeSerializerBaseStub.class);
        when(mockedSerializer._asTimestamp(visitor.getProvider())).thenReturn(true);

        mockedSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(mockedSerializer).visitIntFormat(
                same(visitor),
                same(typeHint),
                same(JsonParser.NumberType.LONG),
                same(JsonValueFormat.UTC_MILLISEC)
        );
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithAsNumberFalse() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        DateTimeSerializerBaseStub mockedSerializer = mock(DateTimeSerializerBaseStub.class);
        when(mockedSerializer._asTimestamp(visitor.getProvider())).thenReturn(false);

        mockedSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(mockedSerializer).visitStringFormat(
                same(visitor),
                same(typeHint),
                same(JsonValueFormat.DATE_TIME)
        );
        verifyNoMoreInteractions(visitor);
    }

    @Test(expected = IOException.class)
    public void testSerialize() throws IOException {
        DateTimeSerializerBaseStub mockedSerializer = mock(DateTimeSerializerBaseStub.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        mockedSerializer.serialize(new DateTimeSerializerBaseStub(), gen, serializers);

        verify(mockedSerializer).serialize(
                new DateTimeSerializerBaseStub(),
                same(gen),
                same(serializers)
        );
        verifyNoMoreInteractions(gen, serializers);
    }

    private static class DateTimeSerializerBaseStub extends DateTimeSerializerBase<DateTimeSerializerBaseStub> {

        public DateTimeSerializerBaseStub() {
            super(DateTimeSerializerBaseStub.class, true, null);
        }

        protected DateTimeSerializerBaseStub(Class<DateTimeSerializerBaseStub> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<DateTimeSerializerBaseStub> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new DateTimeSerializerBaseStub(this.getClass(), timestamp, customFormat);
        }

        @Override
        protected long _timestamp(DateTimeSerializerBaseStub value) {
            return 0;
        }

        @Override
        public void serialize(DateTimeSerializerBaseStub value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // no implementation needed
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            // no implementation needed
        }
    }
}