package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Test;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseEmpty() {
        DateTimeSerializerBase<?> serializer = new StdDateSerializer(java.util.Date.class);
        assertNull(serializer.serialize(null, new JsonGenerator(new StringWriter()), new SerializerProvider()));
        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(new SerializerProvider(), null));
    }

    @Test
    public void testDateTimeSerializerBaseWithFormat() {
        DateTimeSerializerBase<java.util.Date> serializer = new StdDateSerializer(java.util.Date.class, false, null);
        DateTimeSerializerBase<java.util.Date> serializerTimestamp = serializer.withFormat(true, null);
        DateTimeSerializerBase<java.util.Date> serializerCustomFormat = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));

        // Test with empty object
        assertNull(serializer.serialize(null, new JsonGenerator(new StringWriter()), new SerializerProvider()));
        assertFalse(serializer.isEmpty(new SerializerProvider(), null));

        // Test with serializer with timestamp format
        java.util.Date date = new java.util.Date();
        String expected = Long.toString(date.getTime());
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        serializerTimestamp.serialize(date, gen, new SerializerProvider());
        assertEquals(expected, gen.getOutputContent().toString());

        // Test with serializer with custom format
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        gen = new JsonFactory().createGenerator(new StringWriter());
        serializerCustomFormat.serialize(date, gen, new SerializerProvider());
        assertEquals(format.format(date), gen.getOutputContent().toString());
    }

    @Test
    public void testDateTimeSerializerBaseCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<java.util.Date> serializer = new StdDateSerializer(java.util.Date.class, false, null);

        // Test serializer with JSON format and custom pattern
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        ObjectCodec codec = new ObjectMapper().finderForSerializationInfo(null);
        SerializerProvider serializers = new SerializerProvider();
        SerializationConfig config = new SerializationConfig();
        config.setSerializerProvider(serializers);
        config.setCodec(codec);
        config.setSerializableFormat(format);
        JsonSerializer<java.util.Date> serializedSerializer = serializer.createContextual(serializers, null);

        java.util.Date date = new java.util.Date();
        String expected = format.getPattern();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        serializedSerializer.serialize(date, gen, new SerializerProvider());
        assertEquals(expected, gen.getOutputContent().toString());

        // Test serializer with default format
        serializedSerializer = serializer.createContextual(serializers, null);
        expected = Long.toString(date.getTime());
        gen = new JsonFactory().createGenerator(new StringWriter());
        serializedSerializer.serialize(date, gen, new SerializerProvider());
        assertEquals(expected, gen.getOutputContent().toString());

        // Test serializer with null format
        serializer = new StdDateSerializer(java.util.Date.class, false, null);
        serializedSerializer = serializer.createContextual(serializers, null);
        assertNull(serializedSerializer.serialize(date, gen, new SerializerProvider()));
    }
}