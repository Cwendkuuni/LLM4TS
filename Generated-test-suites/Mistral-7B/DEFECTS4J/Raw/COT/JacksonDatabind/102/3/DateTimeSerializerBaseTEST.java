package com.example.junit;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateFormatSerializer;

public class DateTimeSerializerBaseTest {

    private static final Class<Date> DATE_CLASS = Date.class;

    @Test
    public void testDateTimeSerializerBaseConstructor() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(DATE_CLASS, false, null);
        assertNotNull(serializer);
    }

    @Test
    public void testDateTimeSerializerBaseWithFormat() {
        DateTimeSerializerBase<Date> baseSerializer = new DateTimeSerializerBase<>(DATE_CLASS, false, null);
        DateTimeSerializerBase<Date> timestampSerializer = baseSerializer.withFormat(true, null);
        assertNotSame(baseSerializer, timestampSerializer);
    }

    @Test
    public void testCreateContextualWithNullProperty() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(DATE_CLASS, false, null);
        assertEquals(serializer, serializer.createContextual(null, null));
    }

    @Test
    public void testCreateContextualWithFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(DATE_CLASS, false, dateFormat);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("yyyy-MM-dd HH:mm:ss z");
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("format", format);
        SerializerProvider serializers = new StandardSerializerProvider();
        JsonSerializer<ObjectNode> contextualSerializer = serializer.createContextual(serializers, null);
        assertEquals("yyyy-MM-dd HH:mm:ss z", contextualSerializer.getFormat().getPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithInvalidFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(DATE_CLASS, false, dateFormat);
        SerializerProvider serializers = new StandardSerializerProvider();
        JsonSerializer<ObjectNode> contextualSerializer = serializer.createContextual(serializers, null);
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("format", new ObjectNode());
        contextualSerializer.createContextual(serializers, null);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(DATE_CLASS, false, null);
        assertFalse(serializer.isEmpty(null, null));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(DATE_CLASS, false, null);
        JsonNode schema = serializer.getSchema(null, null);
        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(DATE_CLASS, false, null);
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(node, null), null);
        JsonNode format = node.get("format");
        assertEquals("string", format.get("valueClass").asText());
        assertEquals("date-time", format.get("valueFormat").asText());
    }

    @Test
    public void testSerializeAsString() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(DATE_class, false, dateFormat);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        Date date = new Date();
        serializer.serialize(date, gen, null);
        assertEquals("2023-03-29 15:33:12 EDT", gen.getOutputBuffer().toString());
    }

    @Test(expected = IOException.class)
    public void testSerializeWithNullValue() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(DATE_CLASS, false, null);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(null, gen, null);
    }
}