
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testSerialize_TimestampFormat() throws IOException {
        // Arrange
        Class<?> type = Date.class;
        Boolean useTimestamp = true;
        DateFormat customFormat = null;    
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImplementation(type, useTimestamp, customFormat);
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        Date value = new Date(1626900588000L); // July 22, 2021, 10:09:48 AM UTC

        // Act
        serializer.serialize(value, gen, serializers);

        // Assert
        verify(gen).writeNumber(1626900588000L);
    }

    @Test
    public void testSerialize_StringFormat() throws IOException {
        // Arrange
        Class<?> type = Date.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = mock(DateFormat.class);
        when(customFormat.format(any())).thenReturn("2021-07-22T12:09:48Z");
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImplementation(type, useTimestamp, customFormat);
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        Date value = new Date(1626900588000L); // July 22, 2021, 10:09:48 AM UTC

        // Act
        serializer.serialize(value, gen, serializers);

        // Assert
        verify(gen).writeString("2021-07-22T12:09:48Z");
    }

    @Test
    public void testSerialize_NullValue() throws IOException {
        // Arrange
        Class<?> type = Date.class;
        Boolean useTimestamp = true;
        DateFormat customFormat = null;
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImplementation(type, useTimestamp, customFormat);
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        Date value = null;

        // Act
        serializer.serialize(value, gen, serializers);

        // Assert
        verify(gen).writeNull();
    }

    @Test
    public void testIsEmpty_NullValue() {
        // Arrange
        Class<?> type = Date.class;
        Boolean useTimestamp = true;
        DateFormat customFormat = null;
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImplementation(type, useTimestamp, customFormat);
        SerializerProvider serializers = mock(SerializerProvider.class);
        Date value = null;

        // Act
        boolean result = serializer.isEmpty(serializers, value);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_ValueWithTimestampZero() {
        // Arrange
        Class<?> type = Date.class;
        Boolean useTimestamp = true;
        DateFormat customFormat = null;
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImplementation(type, useTimestamp, customFormat);
        SerializerProvider serializers = mock(SerializerProvider.class);
        Date value = new Date(0L);

        // Act
        boolean result = serializer.isEmpty(serializers, value);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_ValueWithNonZeroTimestamp() {
        // Arrange
        Class<?> type = Date.class;
        Boolean useTimestamp = true;
        DateFormat customFormat = null;
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImplementation(type, useTimestamp, customFormat);
        SerializerProvider serializers = mock(SerializerProvider.class);
        Date value = new Date(1626900588000L); // July 22, 2021, 10:09:48 AM UTC

        // Act
        boolean result = serializer.isEmpty(serializers, value);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetSchema_TimestampFormat() {
        // Arrange
        Class<?> type = Date.class;
        Boolean useTimestamp = true;
        DateFormat customFormat = null;
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImplementation(type, useTimestamp, customFormat);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(any())).thenReturn(true);
        Type typeHint = null;

        // Act
        JsonNode schema = serializer.getSchema(serializers, typeHint);

        // Assert
        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.get("format").isNull());
    }

    @Test
    public void testGetSchema_StringFormat() {
        // Arrange
        Class<?> type = Date.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = null;
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImplementation(type, useTimestamp, customFormat);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(any())).thenReturn(true);
        Type typeHint = null;

        // Act
        JsonNode schema = serializer.getSchema(serializers, typeHint);

        // Assert
        assertEquals("string", schema.get("type").asText());
        assertEquals("date-time", schema.get("format").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_TimestampFormat() throws JsonMappingException {
        // Arrange
        Class<?> type = Date.class;
        Boolean useTimestamp = true;
        DateFormat customFormat = null;
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImplementation(type, useTimestamp, customFormat);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(visitor.getProvider()).thenReturn(mock(SerializerProvider.class));

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor).expectIntegerFormat(typeHint, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
    }

    @Test
    public void testAcceptJsonFormatVisitor_StringFormat() throws JsonMappingException {
        // Arrange
        Class<?> type = Date.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = null;
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImplementation(type, useTimestamp, customFormat);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(visitor.getProvider()).thenReturn(mock(SerializerProvider.class));

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor).expectStringFormat(typeHint, JsonValueFormat.DATE_TIME);
    }

    private static class DateTimeSerializerBaseImplementation extends DateTimeSerializerBase<Date> {
        protected DateTimeSerializerBaseImplementation(Class<Date> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null; // Not needed for the test
        }

        @Override
        protected long _timestamp(Date value) {
            return 0; // Not needed for the test
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // Not needed for the test
        }
    }

}

