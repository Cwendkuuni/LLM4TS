package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.node.TextNode;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDefaultConstructor() {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, null, null);
        assertNotNull(serializer);
        assertEquals(Long.TYPE, serializer.handledType());
        assertNull(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testTimestampConstructor() {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        assertNotNull(serializer);
        assertEquals(Long.TYPE, serializer.handledType());
        assertTrue(serializer._useTimestamp.booleanValue());
        assertNull(serializer._customFormat);
    }

    @Test
    public void testCustomFormatConstructor() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, df);
        assertNotNull(serializer);
        assertEquals(String.class, serializer.handledType());
        assertFalse(serializer._useTimestamp.booleanValue());
        assertEquals(df, serializer._customFormat);
    }

    @Test
    public void testTimestampSerialization() throws JsonMappingException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        ObjectMapper mapper = new ObjectMapper();
        Long value = System.currentTimeMillis();

        ObjectNode node = mapper.createObjectNode();
        node.put("value", value);

        JsonNode result = serializer.serialize(value, mapper.getFactory().createGenerator(mapper.getOutputFactory()), mapper);
        assertEquals(TextNode.value("" + value + "000"), result);
    }

    @Test
    public void testCustomFormatSerialization() throws JsonMappingException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, df);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setTimeZone(TimeZone.getTimeZone("UTC"));
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        ObjectNode node = mapper.createObjectNode();
        node.put("value", "2022-01-01 12:00:00 EST");

        JsonNode result = serializer.serialize(node.get("value"), mapper.getFactory().createGenerator(mapper.getOutputFactory()), mapper);
        assertEquals(TextNode.value("2022-01-01T12:00:00-05:00"), result);
    }

    @Test
    public void testContextualSerialization() throws JsonMappingException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, df);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd HH:mm:ss z");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        ObjectNode node = mapper.createObjectNode();
        node.setMixInAnnotation(JsonFormat.class);
        node.set("value", format);
        node.put("value", "2022-01-01 12:00:00 EST");

        JsonNode result = serializer.createContextual(mapper.getSerializerProvider(), null).serialize(node.get("value"), mapper.getFactory().createGenerator(mapper.getOutputFactory()), mapper);
        assertEquals(TextNode.value("2022-01-01T12:00:00-05:00"), result);
    }

}