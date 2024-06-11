package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProviders;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    private static final JsonFormat<Object> DEFAULT_FORMAT = new JsonFormat<Object>() {{
        setShape(JsonFormat.Shape.NUMBER);
    }};

    @SuppressWarnings("unchecked")
    private static final JsonFormat<Object> STRING_FORMAT = new JsonFormat<Object>() {{
        setShape(JsonFormat.Shape.STRING);
        setPattern("yyyy-MM-dd HH:mm:ss.SSS'Z'");
        setTimeZone(TimeZone.UTC);
    }};

    @Test
    public void testCreateContextualWithNoFormat() throws Exception {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        SerializerProvider serializers = new SerializerProvider();
        BeanProperty property = mock(BeanProperty.class);

        ContextualSerializer contextual = serializer.createContextual(serializers, property);
        assertSame(serializer, contextual);
    }

    @Test
    public void testCreateContextualWithNumericFormat() throws Exception {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        SerializerProvider serializers = new SerializerProvider();
        JsonFormat<Object> format = DEFAULT_FORMAT;

        ContextualSerializer contextual = serializer.createContextual(serializers, null);
        JsonSerializer<Long> serializerWithFormat = serializer.withFormat(Boolean.TRUE, null).withoutResolutionVisibility(null);
        when(serializers.getAnnotationIntrospector().findFormat((Annotated) null)).thenReturn(format);

        assertSame(serializerWithFormat, contextual);
    }

    @Test
    public void testCreateContextualWithStringFormat() throws Exception {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        SerializerProvider serializers = new SerializerProvider();
        JsonFormat<Object> format = STRING_FORMAT;

        ContextualSerializer contextual = serializer.createContextual(serializers, null);
        JsonSerializer<String> serializerWithFormat = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat(format.getPattern(), format.getLocale())).withoutResolutionVisibility(null);
        when(serializers.getAnnotationIntrospector().findFormat((Annotated) null)).thenReturn(format);

        assertSame(serializerWithFormat, contextual);
    }

    @Test
    public void testCreateContextualWithAnnotatedFormat() throws Exception {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        SerializerProvider serializers = new SerializerProvider();
        JsonFormat<Object> format = STRING_FORMAT;
        BeanProperty property = mock(BeanProperty.class);
        when(property.getMember()).thenReturn(new Long(0));
        when(serializers.getAnnotationIntrospector().findFormat((Annotated) property)).thenReturn(format);

        ContextualSerializer contextual = serializer.createContextual(serializers, property);
        JsonSerializer<String> serializerWithFormat = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat(format.getPattern(), format.getLocale())).withoutResolutionVisibility(null);

        assertSame(serializerWithFormat, contextual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullSerializers() throws Exception {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        serializer.createContextual(null, null);
    }

    @Test
    public void testSerializeWithDefaultFormat() throws Exception {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        Long value = 1L;
        SerializerProvider serializers = new SerializerProvider();
        JsonGenerator gen = mock(JsonGenerator.class);

        serializer.serialize(value, gen, serializers);

        // Test that the default format is used when no custom format is provided
        SerializerProvider serializersWithFormat = new SerializerProvider();
        JsonFormat<Object> format = DEFAULT_FORMAT;
        when(serializersWithFormat.getAnnotationIntrospector().findFormat((Annotated) null)).thenReturn(format);
        when(gen.getSerializerProvider()).thenReturn(serializersWithFormat);
        long timestamp = System.currentTimeMillis();
        when(serializersWithFormat.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);

        serializer.serialize(value, gen, serializersWithFormat);

        assertNull(gen.findProperty("timestamp"));
    }

    @Test
    public void testSerializeWithCustomFormat() throws Exception {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        String value = "2023-03-15 12:34:56.789";
        SerializerProvider serializers = new SerializerProvider();
        JsonGenerator gen = mock(JsonGenerator.class);

        serializer.serialize(value, gen, serializers);

        // Test that the custom format is used when provided
        SerializerProvider serializersWithFormat = new SerializerProvider();
        JsonFormat<Object> format = STRING_FORMAT;
        when(serializersWithFormat.getAnnotationIntrospector().findFormat((Annotated) null)).thenReturn(format);
        when(gen.getSerializerProvider()).thenReturn(serializersWithFormat);

        serializer.serialize(value, gen, serializersWithFormat);

        String expectedOutput = "\"2023-03-15T12:34:56.789Z\"";
        assertEquals(expectedOutput, gen.getOutputValueAsString());
    }

    private void assertEquals(String expectedOutput, String actualOutput) throws IOException {
        JsonParser parser = new JsonFactory().createParser(new StringReader(expectedOutput));
        JsonParser parsed = new JsonFactory().createParser(new StringReader(actualOutput));

        assertEquals(parser.getValueAsString(), parsed.getValueAsString());
    }
}