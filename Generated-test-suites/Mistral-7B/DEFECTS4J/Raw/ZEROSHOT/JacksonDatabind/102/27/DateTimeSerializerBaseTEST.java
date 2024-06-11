package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProviders;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private SerializerProvider serializerProvider;
    private JsonFormat<Object> jsonFormat;

    @Before
    public void setUp() {
        serializerProvider = new SerializerProviders.BaseSerializerProvider();
        jsonFormat = mock(JsonFormat.class);
        when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(jsonFormat.getPattern()).thenReturn("yyyy-MM-dd HH:mm:ss z");
        when(jsonFormat.getLocale()).thenReturn(Locale.US);
        when(jsonFormat.getTimeZone()).thenReturn(TimeZone.getDefault());
    }

    @After
    public void tearDown() {
        serializerProvider = null;
        jsonFormat = null;
    }

    @Test
    public void testCreateContextualWithTimestamp() throws JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        assertEquals(dateTimeSerializer, dateTimeSerializer.createContextual(serializerProvider, null));
    }

    @Test
    public void testCreateContextualWithCustomFormat() throws JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        dateTimeSerializer.acceptJsonFormatVisitor(visitor, null.getType());
        verify(visitor).visitStringFormat(eq(Date.class), any(JsonValueFormat.class));
    }

    @Test
    public void testSerializeAsString() throws JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Date date = new Date();
        dateTimeSerializer.serialize(date, jsonGenerator, serializerProvider);
        assertEquals("2023-03-01 12:00:00 CDT", jsonGenerator.getOutputBuffer().getText().trim());
    }

    @Test
    public void testSerializeAsNumber() throws JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Date date = new Date();
        dateTimeSerializer.serialize(date, jsonGenerator, serializerProvider);
        assertEquals("1678846400000", jsonGenerator.getOutputBuffer().getText().trim());
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        assertEquals(false, dateTimeSerializer.isEmpty(serializerProvider, null));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        assertEquals("string", dateTimeSerializer.getSchema(serializerProvider, null.getType()).toString().trim());
    }
}