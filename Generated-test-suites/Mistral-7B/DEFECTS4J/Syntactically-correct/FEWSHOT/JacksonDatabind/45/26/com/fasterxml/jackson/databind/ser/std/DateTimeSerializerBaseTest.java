package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDateTimeSerializerBase() throws IOException {
        DateTimeSerializerBase<Long> longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        DateTimeSerializerBase<Long> timestampSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);

        JsonSerializer<?> longSerializerContextual = longSerializer.createContextual(null, null);
        JsonSerializer<?> timestampSerializerContextual = timestampSerializer.createContextual(null, null);

        // Test with no format annotation
        Object value = 1633059200000L; // Timestamp in seconds since epoch
        JsonNode result = null;

        // Test serialization as numeric timestamp
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        longSerializerContextual.serialize(value, gen, new DefaultSerializerProvider());
        result = gen.getOutputStream().readAllBytes();
        gen.close();
        assertEquals("1633059200000", new String(result));

        // Test serialization as string
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        timestampSerializerContextual.serialize(value, gen, new DefaultSerializerProvider());
        result = gen.getOutputStream().readAllBytes();
        gen.close();
        assertEquals("\"1979-12-28T03:10:00.000Z\"", new String(result));

        // Test with format annotation
        JsonFormat formatAnnotation = new JsonFormat();
        formatAnnotation.shape(JsonFormat.Shape.STRING);
        formatAnnotation.timeZone(TimeZone.getTimeZone("UTC"));
        formatAnnotation.pattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Object annotatedValue = new Object() {
            @JsonFormat(format = formatAnnotation)
            long value = 1633059200000L;
        };
        JsonNode annotatedResult = null;

        // Test serialization as string with format annotation
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        JsonSerializer<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null).withContextualSerialization(Object.class);
        serializer = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat(formatAnnotation.getPattern(), formatAnnotation.getLocale()));
        serializer.serialize(annotatedValue, gen, new DefaultSerializerProvider());
        result = gen.getOutputStream().readAllBytes();
        gen.close();
        assertEquals("\"1979-12-28T03:10:00.000Z\"", new String(result));

        // Test serialization as numeric timestamp with format annotation
        serializer = new DateTimeSerializerBase<Object>(Object.class, true, null).withContextualSerialization(Object.class);
        serializer = serializer.withFormat(Boolean.TRUE, null);
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(annotatedValue, gen, new DefaultSerializerProvider());
        result = gen.getOutputStream().readAllBytes();
        gen.close();
        assertEquals("1633059200000", new String(result));

        // Test deserialization as numeric timestamp
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(result));
        long deserializedValue = 0;
        deserializedValue = longSerializerContextual.deserialize(parser, Long.class, null);
        assertEquals(value, deserializedValue);

        // Test deserialization as string
        parser = new JsonFactory().createParser(new ByteArrayInputStream(result.elements().get(1).asText().getBytes()));
        deserializedValue = timestampSerializerContextual.deserialize(parser, Long.class, null);
        assertEquals(value, deserializedValue);

        // Test deserialization with format annotation
        parser = new JsonFactory().createParser(new ByteArrayInputStream(result.elements().get(0).asText().getBytes()));
        deserializedValue = serializer.deserialize(parser, Object.class, null);
        assertEquals(annotatedValue, deserializedValue);

        parser = new JsonFactory().createParser(new ByteArrayInputStream(result.elements().get(1).asText().getBytes()));
        deserializedValue = serializer.deserialize(parser, Long.class, null);
        assertEquals(value, deserializedValue);
    }
}