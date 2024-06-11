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
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.node.JsonNode;

public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDateTimeSerializerBase() {
        // Test default initialization
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<Object>(Object.class, null, null);
        assertNull(serializer._useTimestamp);
        assertNull(serializer._customFormat);

        // Test initialization with _useTimestamp set
        DateTimeSerializerBase<Long> timestampSerializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
        assertTrue(timestampSerializer._useTimestamp != null && timestampSerializer._useTimestamp.booleanValue());
        assertNull(timestampSerializer._customFormat);

        // Test initialization with _customFormat set
        DateTimeSerializerBase<String> formatSerializer = new DateTimeSerializerBase<String>(String.class, false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        assertNull(formatSerializer._useTimestamp);
        assertNotNull(formatSerializer._customFormat);

        // Test creation with custom format from JsonFormat annotation
        JsonFormat<Object> formatAnnotation = new JsonFormat<>();
        formatAnnotation.shape(JsonFormat.Shape.STRING);
        formatAnnotation.timeZone(TimeZone.getDefault());
        formatAnnotation.pattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase<Object> annotatedSerializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                if (timestamp != null) {
                    return this;
                }
                return new DateTimeSerializerBase<Object>(Object.class, null, customFormat);
            }
        };
        Annotated member = new AnnotatedClass(annotatedSerializer.handledType(), null);
        member.setValue(formatAnnotation);
        DateTimeSerializerBase<Object> annotatedSerializerInstance = annotatedSerializer.createContextual(null, null);
        DateFormat customFormat = annotatedSerializerInstance._customFormat;
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", customFormat.toPattern());
        assertNull(annotatedSerializerInstance._useTimestamp);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null, null);
        serializer._acceptJsonFormatVisitor(visitor, null, false);
        assertEquals("number", visitor.getFormatType());
        assertEquals(JsonParser.NumberType.LONG, visitor.getCurrentType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getCurrentValueFormat());
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        Long value = 1633043200000L; // 2021-10-01 00:00:00 UTC
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(value, gen, new DefaultSerializerProvider());
        JsonNode node = gen.getOutputContent();
        assertEquals(Long.toString(value), node.toString());
    }

    @Test
    public void testSerializeWithCustomFormat() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        String value = "2021-10-01 12:34:56";
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(value, gen, new DefaultSerializerProvider());
        JsonNode node = gen.getOutputContent();
        assertEquals(value, node.toString());
    }
}