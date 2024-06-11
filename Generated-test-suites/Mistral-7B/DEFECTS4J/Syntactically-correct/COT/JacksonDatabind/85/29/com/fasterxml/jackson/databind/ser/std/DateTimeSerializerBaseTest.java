package com.yourpackagename.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> dateTimeSerializer;
    private SerializerProvider serializers;
    private JsonGenerator jsonGenerator;
    private JsonParser jsonParser;

    @Before
    public void setUp() {
        serializers = mock(SerializerProvider.class);
        jsonGenerator = mock(JsonGenerator.class);
        jsonParser = mock(JsonParser.class);
        dateTimeSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, null) {
            @Override
            protected long _timestamp(Long value) {
                return value;
            }
        };
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(Long.TYPE, dateTimeSerializer.handledType());
        assertNull(_useTimestamp);
        assertNull(_customFormat);
    }

    @Test
    public void testWithFormat() {
        Boolean useTimestamp = Boolean.TRUE;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        DateTimeSerializerBase<Long> serializerWithFormat = dateTimeSerializer.withFormat(useTimestamp, customFormat);

        assertEquals(Long.TYPE, serializerWithFormat.handledType());
        assertEquals(useTimestamp, serializerWithFormat._useTimestamp);
        assertEquals(customFormat, serializerWithFormat._customFormat);
    }

    @Test
    public void testCreateContextualWithDefaults() throws IOException {
        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);

        DateTimeSerializerBase<Long> serializer = (DateTimeSerializerBase<Long>) dateTimeSerializer.createContextual(serializers, null);

        assertFalse(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testCreateContextualWithCustomFormat() throws IOException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
        when(serializers.getLocale()).thenReturn(Locale.US);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getDefault());

        DateTimeSerializerBase<Long> serializer = (DateTimeSerializerBase<Long>) dateTimeSerializer.createContextual(serializers, null);

        assertTrue(serializer._useTimestamp);
        assertEquals(customFormat, serializer._customFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullSerializerProvider() throws IOException {
        dateTimeSerializer.createContextual(null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorWithNullTypeHint() throws IOException {
        dateTimeSerializer.acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), null);
    }

    @Test
    public void testSerializeWithDefaults() throws IOException {
        when(jsonGenerator.writeNumber(Long.MIN_VALUE)).thenAnswer(invocation -> {
            JsonGenerator gen = invocation.getArgument(0);
            gen.writeStartNumber();
            gen.writeNumber(Long.MIN_VALUE);
            gen.writeEndNumber();
        });

        dateTimeSerializer.serialize(Long.MIN_VALUE, jsonGenerator, serializers);

        verify(jsonGenerator).writeStartNumber();
        verify(jsonGenerator).writeNumber(Long.MIN_VALUE);
        verify(jsonGenerator).writeEndNumber();
    }

    @Test
    public void testSerializeWithCustomFormat() throws IOException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        long testValue = System.currentTimeMillis();

        when(jsonGenerator.writeString(customFormat.format(new Date(testValue)))).thenAnswer(invocation -> {
            JsonGenerator gen = invocation.getArgument(0);
            gen.writeString(customFormat.format(new Date(testValue)));
        });

        dateTimeSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, customFormat) {
            @Override
            protected long _timestamp(Long value) {
                return value;
            }
        };

        dateTimeSerializer.serialize(testValue, jsonGenerator, serializers);

        verify(jsonGenerator).writeString(customFormat.format(new Date(testValue)));
    }
}