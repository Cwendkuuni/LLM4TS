package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import junit.framework.JUnit4TestAdapter;
import junit.runner.JUnitRunner;

@RunWith(JUnitRunner.class)
public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    private static <T> DateTimeSerializerBase<T> getSerializer(Boolean useTimestamp, DateFormat customFormat) {
        return (DateTimeSerializerBase<T>) new DateTimeSerializerBase<T>(
                Mockito.mock(Class.class), useTimestamp, customFormat) {
            @Override
            protected long _timestamp(T value) {
                // empty implementation for testing
                return 0;
            }
        };
    }

    @Test
    public void testConstructor() {
        DateTimeSerializerBase<?> serializer = getSerializer(true, null);
        assertNotNull(serializer);

        DateTimeSerializerBase<?> serializer2 = getSerializer(false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        assertNotNull(serializer2);
    }

    @Test
    public void testWithFormat_useTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Long> serializer = getSerializer(true, null);
        DateTimeSerializerBase<Long> serialized = serializer.withFormat(true, null);
        assertSame(serializer, serialized);

        JsonParser parser = Mockito.mock(JsonParser.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);

        Object value = 1234567890L;
        serialized.serialize(value, gen, Mockito.mock(SerializerProvider.class));

        assertEquals(value, parser.parseLongValue(gen.getOutputValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithFormat_nullCustomFormat() {
        DateTimeSerializerBase<String> serializer = getSerializer(false, null);
        serializer.withFormat(false, null);
    }

    @Test
    public void testCreateContextual() throws JsonProcessingException {
        DateTimeSerializerBase<String> serializer = getSerializer(false, null);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonFormat format = Mockito.mock(JsonFormat.class);
        Annotated annotated = Mockito.mock(Annotated.class);

        when(property.getMember()).thenReturn(annotated);
        when(annotated.getAnnotation(JsonFormat.class)).thenReturn(format);

        JsonFormat.Shape shape = JsonFormat.Shape.DATE_TIME;
        when(format.getShape()).thenReturn(shape);

        JsonSerializer<?> serialized = serializer.createContextual(serializers, property);
        assertNotNull(serialized);

        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        when(serializers.getAnnotationIntrospector()).thenReturn(Mockito.mock(AnnotationIntrospector.class));

        serialized.acceptJsonFormatVisitor(visitor, Mockito.mock(JavaType.class));

        String expectedPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        assertEquals(expectedPattern, visitor.getFormat().getPattern());
    }

    @Test
    public void testAcceptJsonFormatVisitor_asNumber() throws JsonProcessingException {
        DateTimeSerializerBase<Long> serializer = getSerializer(true, null);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);

        serializer.acceptJsonFormatVisitor(visitor, Mockito.mock(JavaType.class), true);

        String expectedFormatId = "number";
        assertEquals(expectedFormatId, visitor.getFormat().getClass().getSimpleName());

        assertEquals(JsonParser.NumberType.LONG, visitor.getFormat().getNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getFormat().getValueFormat());
    }

    @Test
    public void testAcceptJsonFormatVisitor_asString() throws JsonProcessingException {
        DateTimeSerializerBase<String> serializer = getSerializer(false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);

        serializer.acceptJsonFormatVisitor(visitor, Mockito.mock(JavaType.class), false);

        String expectedFormatId = "string";
        assertEquals(expectedFormatId, visitor.getFormat().getClass().getSimpleName());

        assertEquals(JsonValueFormat.DATE_TIME, visitor.getFormat().getValueFormat());
    }

    @Test
    public void testSerialize_useTimestamp() throws IOException {
        DateTimeSerializerBase<Long> serializer = getSerializer(true, null);
        Long value = 1234567890L;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);

        serializer.serialize(value, gen, serializers);

        String expected = "1234567890000";
        assertEquals(expected, gen.getOutputValueAsString());
    }

    @Test
    public void testSerialize_customFormat() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<String> serializer = getSerializer(false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        String value = "2022-01-01T00:00:00.000+01:00";
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);

        serializer.serialize(value, gen, serializers);

        String expected = "\"2022-01-01T00:00:00.000+01:00\"";
        assertEquals(expected, gen.getOutputValueAsString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_nullValue() throws IOException {
        DateTimeSerializerBase<String> serializer = getSerializer(false, null);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);

        serializer.serialize(null, gen, serializers);
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerialize_invalidCustomFormat() throws IOException {
        DateTimeSerializerBase<String> serializer = getSerializer(false, new SimpleDateFormat("xxxx-MM-dd'T'HH:mm:ss.SSS'Z'"));
        String value = "2022-01-01T00:00:00.000+01:00";
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);

        serializer.serialize(value, gen, serializers);
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        DateTimeSerializerBase<Long> serializer = getSerializer(true, null);
        assertEquals("number", serializer.getSchema(Mockito.mock(SerializerProvider.class), Mockito.mock(Type.class)).toString());
    }

    @Test
    public void testGetSchema_customFormat() throws JsonProcessingException {
        DateTimeSerializerBase<String> serializer = getSerializer(false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        assertEquals("string", serializer.getSchema(Mockito.mock(SerializerProvider.class), Mockito.mock(Type.class)).toString());
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DateTimeSerializerBaseTest.class);
    }
}