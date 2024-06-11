package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private DateTimeSerializerBase<T> createDateTimeSerializerBase(Class<T> type, Boolean useTimestamp, DateFormat customFormat) {
        return new DateTimeSerializerBase<T>() {
            @Override
            public DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }

            @Override
            protected long _timestamp(T value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            protected boolean _asTimestamp(SerializerProvider serializers) {
                return useTimestamp;
            }

            @Override
            protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber) throws JsonMappingException {
                // Empty method body
            }
        }.withType(type).withFormat(useTimestamp, customFormat);
    }

    @Test
    public void testCreateContextualWithNullSerializerProvider() {
        DateTimeSerializerBase<Long> serializer = createDateTimeSerializerBase(Long.TYPE, Boolean.FALSE, null);
        try {
            serializer.createContextual(null, null);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            assertEquals("Null SerializerProvider passed for Long", e.getMessage());
        }
    }

    @Test
    public void testCreateContextualWithFormat() {
        DateTimeSerializerBase<Long> serializer = createDateTimeSerializerBase(Long.TYPE, Boolean.FALSE, null);

        JsonParser parser = createMockJsonParser();
        SerializerProvider serializers = createMockSerializerProvider();
        ObjectCodec codec = createMockObjectCodec();
        Annotated member = createMockAnnotated();

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        member.setValue(format);

        JsonFormatVisitorWrapper visitor = createMockJsonFormatVisitorWrapper();
        JavaType typeHint = createMockJavaType();

        DateTimeSerializerBase<Long> serializedSerializer = serializer.createContextual(serializers, member);

        assertEquals(serializer.getClass(), serializedSerializer.getClass());
        assertEquals(Boolean.FALSE, serializedSerializer._useTimestamp);
        assertEquals(format, serializedSerializer._customFormat);
    }

    @Test
    public void testCreateContextualWithNumericFormat() {
        DateTimeSerializerBase<Long> serializer = createDateTimeSerializerBase(Long.TYPE, Boolean.TRUE, null);

        JsonParser parser = createMockJsonParser();
        SerializerProvider serializers = createMockSerializerProvider();
        ObjectCodec codec = createMockObjectCodec();
        Annotated member = createMockAnnotated();

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        member.setValue(format);

        JsonFormatVisitorWrapper visitor = createMockJsonFormatVisitorWrapper();
        JavaType typeHint = createMockJavaType();

        DateTimeSerializerBase<Long> serializedSerializer = serializer.createContextual(serializers, member);

        assertEquals(serializer.getClass(), serializedSerializer.getClass());
        assertEquals(Boolean.TRUE, serializedSerializer._useTimestamp);
        assertNull(serializedSerializer._customFormat);
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<Long> serializer = createDateTimeSerializerBase(Long.TYPE, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        SerializerProvider serializers = createMockSerializerProvider();
        ObjectCodec codec = createMockObjectCodec();
        JsonGenerator gen = createMockJsonGenerator();
        Long value = 1633059823000L; // Thu, 04 Mar 2021 08:03:03 GMT

        serializer.serialize(value, gen, serializers);

        assertEquals("\"2021-03-04T08:03:03.000Z\"", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeWithTimestamp() throws IOException {
        DateTimeSerializerBase<Long> serializer = createDateTimeSerializerBase(Long.TYPE, Boolean.TRUE, null);
        SerializerProvider serializers = createMockSerializerProvider();
        ObjectCodec codec = createMockObjectCodec();
        JsonGenerator gen = createMockJsonGenerator();
        Long value = 1633059823000L; // Thu, 04 Mar 2021 08:03:03 GMT

        serializer.serialize(value, gen, serializers);

        assertEquals("1633059823000", gen.getOutputBuffer().toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Long> serializer = createDateTimeSerializerBase(Long.TYPE, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

        JsonFormatVisitorWrapper visitor = createMockJsonFormatVisitorWrapper();
        JavaType typeHint = createMockJavaType();

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        assertEquals(JsonFormatVisitorWrapper.Kind.DATE_TIME, visitor.getKind());
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", visitor.getFormat().getPattern());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getFormat().getValueFormat());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithNumericFormat() throws JsonMappingException {
        DateTimeSerializerBase<Long> serializer = createDateTimeSerializerBase(Long.TYPE, Boolean.TRUE, null);

        JsonFormatVisitorWrapper visitor = createMockJsonFormatVisitorWrapper();
        JavaType typeHint = createMockJavaType();

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        assertEquals(JsonFormatVisitorWrapper.Kind.NUMBER, visitor.getKind());
        assertEquals(JsonParser.NumberType.LONG, visitor.getNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getValueFormat());
    }

    private JsonParser createMockJsonParser() {
        return null; // Mock implementation not needed for these tests
    }

    private SerializerProvider createMockSerializerProvider() {
        return null; // Mock implementation not needed for these tests
    }

    private ObjectCodec createMockObjectCodec() {
        return null; // Mock implementation not needed for these tests
    }

    private JsonFormatVisitorWrapper createMockJsonFormatVisitorWrapper() {
        return null; // Mock implementation not needed for these tests
    }

    private JavaType createMockJavaType() {
        return null; // Mock implementation not needed for these tests
    }

    private Annotated createMockAnnotated() {
        return null; // Mock implementation not needed for these tests
    }
}