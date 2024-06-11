package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

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
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase_EmptyConstructor() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, null, null);
        assertNull(serializer._useTimestamp);
        assertNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    @Test
    public void testDateTimeSerializerBase_WithUseTimestampTrue() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, Boolean.TRUE, null);
        assertEquals(true, serializer._useTimestamp.booleanValue());
        assertNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    @Test
    public void testDateTimeSerializerBase_WithUseTimestampFalseAndCustomFormatNull() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, Boolean.FALSE, null);
        assertEquals(false, serializer._useTimestamp.booleanValue());
        assertNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    @Test
    public void testDateTimeSerializerBase_WithUseTimestampFalseAndCustomFormat() {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, Boolean.FALSE, customFormat);
        assertEquals(false, serializer._useTimestamp.booleanValue());
        assertNotNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    @Test
    public void testDateTimeSerializerBase_WithFormat() {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, Boolean.FALSE, customFormat);
        DateTimeSerializerBase<Object> withFormat = serializer.withFormat(Boolean.FALSE, null);
        assertEquals(false, serializer._useTimestamp.booleanValue());
        assertEquals(false, withFormat._useTimestamp.booleanValue());
        assertNotNull(serializer._customFormat);
        assertNull(withFormat._customFormat);
        assertNull(serializer._reusedCustomFormat);
        assertNull(withFormat._reusedCustomFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, Boolean.FALSE, customFormat);

        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> createdSerializer = mapper.serializers().findValueSerializer(Object.class);
        assertEquals(serializer, createdSerializer);

        JsonFormat.Shape shape = JsonFormat.Value.shapeForSerializer(createdSerializer);
        assertEquals(JsonFormat.Shape.STRING, shape);
    }

    @Test
    public void testSerialize() throws IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, Boolean.FALSE, customFormat);

        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        String result = mapper.writer().writeValueAsString(new Date());
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(date, gen, mapper.getSerializerProvider());
        assertEquals(result, gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerialize_WithUseTimestampTrue() throws IOException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, Boolean.TRUE, null);

        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        String result = mapper.writer().writeValueAsString(date.getTime());
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(date, gen, mapper.getSerializerProvider());
        assertEquals(result, gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerialize_WithUseTimestampFalseAndCustomFormat() throws IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, Boolean.FALSE, customFormat);

        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        String result = mapper.writer().withDateFormat(customFormat).writeValueAsString(date);
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(date, gen, mapper.getSerializerProvider());
        assertEquals(result, gen.getOutputBuffer().toString());
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, null, null);
        assertFalse(serializer.isEmpty(null, null));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, null, null);
        JsonNode schema = serializer.getSchema(null, null);
        assertEquals("string", schema.getClass().getSimpleName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, null, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper().getSerializerProvider(), null, true);
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertNotNull(visitor.getFormatVisitor());
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithUseTimestampTrue() throws JsonMappingException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, Boolean.TRUE, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper().getSerializerProvider(), null, true);
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertNotNull(visitor.getNumberFormatVisitor());
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithUseTimestampFalseAndCustomFormat() throws JsonMappingException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, Boolean.FALSE, customFormat);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper().getSerializerProvider(), null, true);
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertNotNull(visitor.getStringFormatVisitor());
    }

    @Test
    public void testSerialize_WithReusedCustomFormat() throws IOException, ParseException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, Boolean.FALSE, customFormat);

        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"2022-01-01T01:01:01.123Z\",\"2022-01-01T01:01:01.123Z\"]";
        ObjectReader reader = mapper.readerForValueType(Object[].class);
        Object[] objects = reader.readValue(json);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        for (Object obj : objects) {
            serializer.serialize((Date) obj, gen, mapper.getSerializerProvider());
        }
        String result = gen.getOutputBuffer().toString();
        assertEquals("[\"2022-01-01T01:01:01.123Z\",\"2022-01-01T01:01:01.123Z\"]", result);

        // Reused custom format instance
        DateFormat formatter = _reusedCustomFormat.getAndSet(null);
        assertEquals("2022-01-01T01:01:01.123Z", formatter.format(new Date()));
        assertEquals("2022-01-01T01:01:01.123Z", formatter.format(new Date()));
        _reusedCustomFormat.compareAndSet(null, formatter);
    }
}