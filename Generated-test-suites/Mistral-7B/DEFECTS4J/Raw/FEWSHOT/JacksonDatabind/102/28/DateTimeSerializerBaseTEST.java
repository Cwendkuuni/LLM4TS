package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    private static final SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);

    @SuppressWarnings("unchecked")
    private static final JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);

    @Test
    public void testCreateContextualSerializer_noFormatOverride() throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        DateFormat expectedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);

        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<>(Date.class, false, null);

        JsonSerializer<?> actualSerializer = dateSerializer.createContextual(mockSerializerProvider, null);

        assertEquals(dateSerializer.getClass(), actualSerializer.getClass());
        assertEquals(expectedDateFormat, actualSerializer._customFormat);
    }

    @Test
    public void testCreateContextualSerializer_numericTimestampFormat() throws IOException {
        DateTimeSerializerBase<Long> longSerializer = new DateTimeSerializerBase<>(Long.class, true, null);

        JsonSerializer<?> actualSerializer = longSerializer.createContextual(mockSerializerProvider, null);

        assertTrue(actualSerializer instanceof DateTimeSerializerBase);
        assertFalse(actualSerializer._customFormat != null);
    }

    @Test
    public void testCreateContextualSerializer_customFormatOverride() throws IOException {
        JsonFormat customFormat = new JsonFormat.Value()
                .shape(JsonFormat.Shape.STRING)
                .pattern("yyyy-MM-dd HH:mm:ss.SSS")
                .locale(Locale.ENGLISH);

        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<>(Date.class, false, null);

        JsonSerializer<?> actualSerializer = dateSerializer.createContextual(mockSerializerProvider, null)
                .withFormat(Boolean.FALSE, customFormat);

        assertEquals(dateSerializer.getClass(), actualSerializer.getClass());
        assertEquals(customFormat, actualSerializer._customFormat);
    }

    @Test
    public void testCreateContextualSerializer_customFormatOverride_reusedInstance() throws IOException {
        JsonFormat customFormat = new JsonFormat.Value()
                .shape(JsonFormat.Shape.STRING)
                .pattern("yyyy-MM-dd HH:mm:ss.SSS")
                .locale(Locale.ENGLISH);

        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<>(Date.class, false, null);

        JsonSerializer<?> actualSerializer = dateSerializer.createContextual(mockSerializerProvider, null)
                .withFormat(Boolean.FALSE, customFormat);

        JsonSerializer<?> serializer1 = actualSerializer;
        JsonSerializer<?> serializer2 = actualSerializer.createContextual(mockSerializerProvider, null)
                .withFormat(Boolean.FALSE, customFormat);

        assertEquals(serializer1, serializer2);
    }

    @Test
    public void testCreateContextualSerializer_customFormatOverride_newInstance() throws IOException {
        JsonFormat customFormat = new JsonFormat.Value()
                .shape(JsonFormat.Shape.STRING)
                .pattern("yyyy-MM-dd HH:mm:ss.SSS")
                .locale(Locale.ENGLISH);

        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<>(Date.class, false, null);

        JsonSerializer<?> actualSerializer = dateSerializer.createContextual(mockSerializerProvider, null)
                .withFormat(Boolean.FALSE, customFormat);

        JsonSerializer<?> serializer1 = actualSerializer;
        JsonSerializer<?> serializer2 = dateSerializer.createContextual(mockSerializerProvider, null)
                .withFormat(Boolean.FALSE, customFormat);

        assertNotEquals(serializer1, serializer2);
    }

    @Test
    public void testSerialize_numericTimestampFormat() throws IOException {
        DateTimeSerializerBase<Long> longSerializer = new DateTimeSerializerBase<>(Long.class, true, null);
        Long timestamp = 1622352430000L;

        when(mockJsonGenerator.writeNumber(anyLong()))
                .thenAnswer(invocation -> invocation.getArgument(0).toString());

        longSerializer.serialize(timestamp, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeNumber(timestamp);
    }

    @Test
    public void testSerialize_customFormat() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<>(Date.class, false, null);
        Date date = new Date();

        JsonFormat customFormat = new JsonFormat.Value()
                .shape(JsonFormat.Shape.STRING)
                .pattern("yyyy-MM-dd HH:mm:ss.SSS")
                .locale(Locale.ENGLISH);

        when(mockJsonGenerator.writeString(anyString()))
                .thenAnswer(invocation -> "");

        dateSerializer.serialize(date, mockJsonGenerator, mockSerializerProvider);

        DateTimeSerializerBase<Date> customFormatterSerializer = new DateTimeSerializerBase<>(Date.class, false, null)
                .withFormat(Boolean.FALSE, customFormat);

        when(mockJsonGenerator.writeString(anyString()))
                .thenReturn("");

        customFormatterSerializer.serialize(date, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator, times(2)).writeString(dateFormat.format(date));
    }

    @Test
    public void testSerialize_customFormat_reusedInstance() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<>(Date.class, false, null);
        Date date = new Date();

        JsonFormat customFormat = new JsonFormat.Value()
                .shape(JsonFormat.Shape.STRING)
                .pattern("yyyy-MM-dd HH:mm:ss.SSS")
                .locale(Locale.ENGLISH);

        when(mockJsonGenerator.writeString(anyString()))
                .thenAnswer(invocation -> "");

        dateSerializer.serialize(date, mockJsonGenerator, mockSerializerProvider);

        JsonSerializer<?> serializer1 = dateSerializer.createContextual(mockSerializerProvider, null)
                .withFormat(Boolean.FALSE, customFormat);

        when(mockJsonGenerator.writeString(anyString()))
                .thenReturn("");

        serializer1.serialize(date, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator, times(3)).writeString(dateFormat.format(date));
    }

    @Test
    public void testSerialize_customFormat_newInstance() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<>(Date.class, false, null);
        Date date = new Date();

        JsonFormat customFormat = new JsonFormat.Value()
                .shape(JsonFormat.Shape.STRING)
                .pattern("yyyy-MM-dd HH:mm:ss.SSS")
                .locale(Locale.ENGLISH);

        when(mockJsonGenerator.writeString(anyString()))
                .thenAnswer(invocation -> "");

        dateSerializer.serialize(date, mockJsonGenerator, mockSerializerProvider);

        JsonSerializer<?> serializer1 = dateSerializer.createContextual(mockSerializerProvider, null)
                .withFormat(Boolean.FALSE, customFormat);

        when(mockJsonGenerator.writeString(anyString()))
                .thenReturn("");

        serializer1.serialize(date, mockJsonGenerator, mockSerializerProvider);

        JsonSerializer<?> serializer2 = dateSerializer.createContextual(mockSerializerProvider, null)
                .withFormat(Boolean.FALSE, customFormat);

        when(mockJsonGenerator.writeString(anyString()))
                .thenReturn("");

        serializer2.serialize(date, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator, times(6)).writeString(dateFormat.format(date));
    }
}