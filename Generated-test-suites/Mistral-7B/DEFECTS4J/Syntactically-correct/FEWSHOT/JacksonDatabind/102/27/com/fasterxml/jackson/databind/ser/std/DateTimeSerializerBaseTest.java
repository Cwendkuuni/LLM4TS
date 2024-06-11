package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Test;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private static final Class<DateTimeSerializerBase<T>> TYPE = (Class<DateTimeSerializerBase<T>>) DateTimeSerializerBase.class;

    @Test
    public void testConstructor() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, customFormat);
        assertEquals(Date.class, serializer.getType());
        assertEquals(false, serializer._useTimestamp.booleanValue());
        assertSame(customFormat, serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    @Test
    public void testWithFormat() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null);
        DateTimeSerializerBase<Date> serializerWithFormat = serializer.withFormat(Boolean.FALSE, customFormat);
        assertEquals(Date.class, serializerWithFormat.getType());
        assertEquals(false, serializerWithFormat._useTimestamp.booleanValue());
        assertSame(customFormat, serializerWithFormat._customFormat);
        assertNull(serializerWithFormat._reusedCustomFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, df);
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = new BeanProperty("test", Date.class, mapper.getTypeFactory().constructType(Map.class), null);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("yyyy-MM-dd HH:mm:ss.SSSZ");
        serializer.config(mapper.getSerializerProvider(), property, format);
        JsonSerializer<?> serializedSerializer = serializer.createContextual(mapper.getSerializerProvider(), property);
        JsonFormat.Shape shape = serializedSerializer.getShape();
        assertEquals(JsonFormat.Shape.STRING, shape);
        assertEquals(Locale.US, serializedSerializer.getFormat().getLocale());
        assertEquals(TimeZone.getTimeZone("UTC"), serializedSerializer.getFormat().getTimeZone());
        assertEquals("yyyy-MM-dd HH:mm:ss.SSSZ", serializedSerializer.getFormat().getPattern());
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null);
        assertFalse(serializer.isEmpty(null, null));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        JsonNode schema = serializer.getSchema(null, Object.class);
        assertEquals("string", schema.getClass().getName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFormatVisitor(JsonFormatVisitor.Std.DATE_TIME);
        serializer.acceptJsonFormatVisitor(visitor, null);
        JsonValueFormat format = visitor.getFormat();
        assertEquals(JsonFormat.Shape.STRING, format.getShape());
        assertNull(format.getLocale());
        assertNull(format.getTimeZone());
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        Date date = new Date();
        serializer.serialize(date, gen, mapper.getSerializerProvider());
        gen.flush();
        String expected = "\"2023-03-31T05:47:56.789Z\"";
        assertEquals(expected, gen.getOutputFile().toString());

        DateTimeSerializerBase<Date> serializerWithFormat = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        gen.reset();
        serializerWithFormat.serialize(date, gen, mapper.getSerializerProvider());
        gen.flush();
        expected = "\"2023-03-31T05:47:56.789+00:00\"";
        assertEquals(expected, gen.getOutputFile().toString());
    }

    @Test
    public void testSerializeAsString() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        Date date = new Date();
        serializer._serializeAsString(date, gen, mapper.getSerializerProvider());
        gen.flush();
        String expected = "\"2023-03-31T05:47:56.789Z\"";
        assertEquals(expected, gen.getOutputFile().toString());
    }

    @Test
    public void testAsTimestamp() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        assertFalse(_asTimestamp(serializer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSerializerProvider() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        serializer.createContextual(null, null);
    }

    private static boolean _asTimestamp(DateTimeSerializerBase<?> serializer) {
        return serializer._asTimestamp(null);
    }
}