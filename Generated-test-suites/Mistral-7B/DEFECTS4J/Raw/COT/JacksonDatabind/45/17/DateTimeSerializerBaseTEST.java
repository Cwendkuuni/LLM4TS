package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("unchecked")
public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> longSerializer;
    private DateTimeSerializerBase<Date> dateSerializer;

    @Before
    public void setUp() {
        longSerializer = new StdDateSerializer(Long.TYPE, null, null);
        dateSerializer = new StdDateSerializer(Date.class, null, null);
    }

    @Test
    public void testDefaultConstruction() {
        DateTimeSerializerBase<?> ser = new DateTimeSerializerBase<>(null, null, null);
        assertNull(ser._useTimestamp);
        assertNull(ser._customFormat);
    }

    @Test
    public void testWithFormat_timestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Long> ser = longSerializer.withFormat(Boolean.TRUE, null);
        assertNotNull(ser._useTimestamp);
        assertTrue(ser._useTimestamp.booleanValue());
        assertNull(ser._customFormat);

        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        ser.serialize(null, gen, new DefaultSerializerProvider());
        assertEquals("0", gen.getOutputBuffer().toString());
    }

    @Test
    public void testWithFormat_customFormat() throws JsonProcessingException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Date> ser = dateSerializer.withFormat(Boolean.FALSE, df);

        assertFalse(ser._useTimestamp.booleanValue());
        assertNotNull(ser._customFormat);
        assertEquals(df, ser._customFormat);

        Date date = new Date();
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        ser.serialize(date, gen, new DefaultSerializerProvider());
        String result = gen.getOutputBuffer().toString();
        assertNotNull(result);
        assertEquals("\"2023-03-15T14:30:45.123Z\"", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextual_noProperty() throws JsonMappingException {
        DateTimeSerializerBase<?> ser = dateSerializer;
        ser.createContextual(new DefaultSerializerProvider(), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextual_noFormatAnnotation() throws JsonMappingException {
        Annotated property = Mockito.mock(Annotated.class);
        DateTimeSerializerBase<?> ser = dateSerializer;
        ser.createContextual(new DefaultSerializerProvider(), property);
    }

    @Test
    public void testCreateContextual_numericFormat() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        Annotated property = Mockito.mock(Annotated.class);
        when(property.getMember()).thenReturn(Mockito.mock(Field.class));
        when(property.getAnnotation(JsonFormat.class)).thenReturn(format);

        DateTimeSerializerBase<?> ser = dateSerializer;
        JsonSerializer<?> result = ser.createContextual(new DefaultSerializerProvider(), property);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertTrue(result.getClass() == longSerializer.getClass());
    }

    @Test
    public void testCreateContextual_stringFormat() throws JsonMappingException, ParseException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Annotated property = Mockito.mock(Annotated.class);
        when(property.getMember()).thenReturn(Mockito.mock(Field.class));
        when(property.getAnnotation(JsonFormat.class)).thenReturn(format);

        DateTimeSerializerBase<?> ser = dateSerializer;
        JsonSerializer<?> result = ser.createContextual(new DefaultSerializerProvider(), property);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertTrue(result.getClass() == dateSerializer.getClass());
        SimpleDateFormat dateFormat = (SimpleDateFormat) result._customFormat;
        assertEquals(format.getPattern(), dateFormat.toPattern());
        assertEquals(format.getTimeZone(), dateFormat.getTimeZone());
    }

    @Test
    public void testIsEmpty() throws JsonMappingException {
        assertFalse(longSerializer.isEmpty(null));
        assertFalse(longSerializer.isEmpty(new DefaultSerializerProvider(), null));
        assertFalse(longSerializer.isEmpty(new Long(0L)));
        assertFalse(longSerializer.isEmpty(new DefaultSerializerProvider(), new Long(0L)));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SchemaNode result = longSerializer.getSchema(new DefaultSerializerProvider(), Long.TYPE);
        assertEquals("number", result.getType());
    }
}