package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
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
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider mockSerializerProvider;

    @Mock
    private SerializerProvider mockNullSerializerProvider;

    @Mock
    private JsonGenerator mockJsonGenerator;

    @Mock
    private SerializerProvider mockEmptySerializerProvider;

    private DateTimeSerializerBaseTestImplementation dateTimeSerializerBaseTestImplementation;

    private class DateTimeSerializerBaseTestImplementation extends DateTimeSerializerBase<Long> {

        public DateTimeSerializerBaseTestImplementation() {
            super(Long.class, true, null);
        }

        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        }

        @Override
        protected long _timestamp(Long value) {
            return 0;
        }

        @Override
        public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dateTimeSerializerBaseTestImplementation = new DateTimeSerializerBaseTestImplementation();
    }

    @Test
    public void testIsEmptyWithValue() {
        Long value = 123456L;
        boolean result = dateTimeSerializerBaseTestImplementation.isEmpty(mockSerializerProvider, value);
        assertEquals(false, result);
    }

    @Test
    public void testIsEmptyWithNullValue() {
        Long value = null;
        boolean result = dateTimeSerializerBaseTestImplementation.isEmpty(mockSerializerProvider, value);
        assertEquals(true, result);
    }

    @Test
    public void testIsEmptyWithZeroTimestampValue() {
        Long value = 0L;
        boolean result = dateTimeSerializerBaseTestImplementation.isEmpty(mockSerializerProvider, value);
        assertEquals(true, result);
    }

    @Test
    public void testGetSchemaAsTimestamp() {
        when(mockSerializerProvider.isEnabled(Mockito.any(SerializationFeature.class))).thenReturn(true);
        when(mockSerializerProvider.getTimeZone()).thenReturn(TimeZone.getDefault());

        JsonNode result = dateTimeSerializerBaseTestImplementation.getSchema(mockSerializerProvider, null);
        assertEquals("number", result.get("type").asText());
    }

    @Test
    public void testGetSchemaAsString() {
        when(mockSerializerProvider.isEnabled(Mockito.any(SerializationFeature.class))).thenReturn(false);
        when(mockSerializerProvider.getTimeZone()).thenReturn(TimeZone.getDefault());

        JsonNode result = dateTimeSerializerBaseTestImplementation.getSchema(mockSerializerProvider, null);
        assertEquals("string", result.get("type").asText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSchemaWithNullSerializerProvider() {
        dateTimeSerializerBaseTestImplementation.getSchema(null, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorAsTimestamp() throws IOException {
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        JavaType mockTypeHint = mock(JavaType.class);
        when(mockVisitor.getProvider()).thenReturn(mockSerializerProvider);
        when(mockSerializerProvider.isEnabled(Mockito.any(SerializationFeature.class))).thenReturn(true);

        dateTimeSerializerBaseTestImplementation.acceptJsonFormatVisitor(mockVisitor, mockTypeHint);
        verify(mockVisitor).visitIntFormat(Mockito.any(JsonFormatVisitable.class), Mockito.any(JavaType.class),
                Mockito.eq(JsonParser.NumberType.LONG), Mockito.eq(JsonValueFormat.UTC_MILLISEC));
    }

    @Test
    public void testAcceptJsonFormatVisitorAsString() throws IOException {
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        JavaType mockTypeHint = mock(JavaType.class);
        when(mockVisitor.getProvider()).thenReturn(mockSerializerProvider);
        when(mockSerializerProvider.isEnabled(Mockito.any(SerializationFeature.class))).thenReturn(false);

        dateTimeSerializerBaseTestImplementation.acceptJsonFormatVisitor(mockVisitor, mockTypeHint);
        verify(mockVisitor).visitStringFormat(Mockito.any(JsonFormatVisitable.class), Mockito.any(JavaType.class),
                Mockito.eq(JsonValueFormat.DATE_TIME));
    }

}