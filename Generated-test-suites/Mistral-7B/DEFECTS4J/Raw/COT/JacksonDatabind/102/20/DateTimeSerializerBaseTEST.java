package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseConstructor() {
        Class<Date> dateType = Date.class;
        Boolean useTimestamp = true;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(dateType, useTimestamp, customFormat);

        assertEquals(Date.class, serializer.getType());
        assertEquals(useTimestamp, serializer.isUseTimestamp());
        assertEquals(customFormat, serializer.getCustomFormat());
        assertNull(serializer.getReusedCustomFormat());
    }

    @Test
    public void testDateTimeSerializerBaseWithFormat() {
        Class<Date> dateType = Date.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        DateTimeSerializerBase<Date> baseSerializer = new DateTimeSerializerBase<>(dateType, useTimestamp, null);

        DateTimeSerializerBase<Date> serializedSerializer = baseSerializer.withFormat(useTimestamp, customFormat);
        assertEquals(baseSerializer.getClass(), serializedSerializer.getClass());
        assertEquals(useTimestamp, serializedSerializer.isUseTimestamp());
        assertEquals(customFormat, serializedSerializer.getCustomFormat());
        assertNotNull(serializedSerializer.getReusedCustomFormat());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException, IOException {
        Class<Date> dateType = Date.class;
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("yyyy-MM-dd HH:mm:ss z");

        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> baseSerializer = mock(DateTimeSerializerBase.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(baseSerializer.getType()).thenReturn(dateType);
        when(baseSerializer.isUseTimestamp()).thenReturn(true);
        when(baseSerializer.getCustomFormat()).thenReturn(null);

        when(serializers.getConfig()).thenReturn(mapper.getSerializationConfig());
        when(serializers.getLocale()).thenReturn(Locale.US);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.getProvider()).thenReturn(serializers);

        DateTimeSerializerBase<Date> serializedSerializer = baseSerializer.createContextual(serializers, null);
        assertEquals(baseSerializer.getClass(), serializedSerializer.getClass());
        assertEquals(true, serializedSerializer.isUseTimestamp());
        assertEquals(null, serializedSerializer.getCustomFormat());
        assertEquals(StdDateFormat.class, serializedSerializer.getClass().getSuperclass());
    }

    @Test
    public void testSerializeAsString() throws IOException {
        Class<Date> dateType = Date.class;
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(dateType, false, null) {
            @Override
            protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                super._serializeAsString(value, g, provider);
                g.writeString("Test");
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider serializers = mapper.getSerializerProvider();
        Date date = new Date();

        serializer.serialize(date, gen, serializers);
        assertEquals("Test" + gen.getOutputBuffer().toString(), gen.getOutputBuffer().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSerializerProvider() throws JsonMappingException {
        Class<Date> dateType = Date.class;
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(dateType, false, null);
        serializer.createContextual(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullProperty() throws JsonMappingException {
        Class<Date> dateType = Date.class;
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(dateType, false, null);
        serializer.createContextual(mock(SerializerProvider.class), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidCustomFormatPattern() throws JsonMappingException, IOException {
        Class<Date> dateType = Date.class;
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(dateType, false, null);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("invalidPattern");

        when(serializers.getConfig()).thenReturn(mapper.getSerializationConfig());
        when(serializers.getLocale()).thenReturn(Locale.US);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.getProvider()).thenReturn(serializers);

        serializer.createContextual(serializers, null);
    }

    @Test
    public void testSerialize() throws IOException {
        Class<Date> dateType = Date.class;
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(dateType, false, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider serializers = mapper.getSerializerProvider();
        Date date = new Date();

        serializer.serialize(date, gen, serializers);
        Date deserializedDate = mapper.readValue(gen.getOutputBuffer().toString(), Date.class);
        assertEquals(date.getTime(), deserializedDate.getTime());
    }

    @Test
    public void testSerializeAsTimestamp() throws IOException {
        Class<Date> dateType = Date.class;
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(dateType, true, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider serializers = mapper.getSerializerProvider();
        Date date = new Date();

        serializer.serialize(date, gen, serializers);
        long deserializedLong = mapper.readValue(gen.getOutputBuffer().toString(), Long.TYPE);
        assertEquals(date.getTime(), deserializedLong);
    }

    @Test
    public void testParseExceptionForInvalidFormat() throws IOException, ParseException {
        Class<Date> dateType = Date.class;
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(dateType, false, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("invalidString");
        SerializerProvider serializers = mapper.getSerializerProvider();

        try {
            serializer.deserialize(parser, Date.class, serializers);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testParseExceptionForNullValue() throws IOException {
        Class<Date> dateType = Date.class;
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(dateType, false, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(null);
        SerializerProvider serializers = mapper.getSerializerProvider();

        try {
            serializer.deserialize(parser, Date.class, serializers);
            fail("Expected ParseException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValueForTimestampSerialization() throws IOException {
        Class<Date> dateType = Date.class;
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(dateType, true, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider serializers = mapper.getSerializerProvider();
        serializer.serialize(null, gen, serializers);
    }
}