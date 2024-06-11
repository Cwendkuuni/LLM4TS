package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

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
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Test;

public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDateTimeSerializerBase() {
        // Test construction with default settings
        DateTimeSerializerBase<Date> dateSerializerBase1 = new DateTimeSerializerBase<Date>(Date.class, false, null);
        assertEquals(Date.class, dateSerializerBase1.handledType());
        assertFalse(dateSerializerBase1.useTimestamp());
        assertNull(dateSerializerBase1.customFormat());
        assertNull(dateSerializerBase1.reusedCustomFormat());

        // Test construction with custom settings
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        DateTimeSerializerBase<Date> dateSerializerBase2 = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        assertEquals(Date.class, dateSerializerBase2.handledType());
        assertFalse(dateSerializerBase2.useTimestamp());
        assertEquals(customFormat, dateSerializerBase2.customFormat());
        assertNull(dateSerializerBase2.reusedCustomFormat());

        // Test construction with timestamp setting
        DateTimeSerializerBase<Long> timestampSerializerBase = new DateTimeSerializerBase<Long>(Long.class, true, null);
        assertEquals(Long.class, timestampSerializerBase.handledType());
        assertTrue(timestampSerializerBase.useTimestamp());
        assertNull(timestampSerializerBase.customFormat());
        assertNull(timestampSerializerBase.reusedCustomFormat());
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        // Test serialization as timestamp with default settings
        JsonGenerator gen = createJsonGenerator();
        dateSerializerBase.serialize(new Date(), gen, new SerializerProvider());
        assertEquals(JsonParser.ValueType.NUMBER, gen.getCurrentValueType());
        assertEquals(0, gen.getCurrentValueAsLong());

        // Test serialization as string with custom format
        DateTimeSerializerBase<Date> dateSerializerBaseWithFormat = dateSerializerBase.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        gen = createJsonGenerator();
        dateSerializerBaseWithFormat.serialize(new Date(), gen, new SerializerProvider());
        assertEquals(JsonParser.ValueType.STRING, gen.getCurrentValueType());
        assertEquals("2023-03-23 15:30:45", gen.getCurrentValueAsString());

        // Test serialization as timestamp with custom format
        DateTimeSerializerBase<Date> dateSerializerBaseWithTimestampAndFormat = dateSerializerBase.withFormat(Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        gen = createJsonGenerator();
        dateSerializerBaseWithTimestampAndFormat.serialize(new Date(), gen, new SerializerProvider());
        assertEquals(JsonParser.ValueType.NUMBER, gen.getCurrentValueType());
        assertEquals(1679142645000L, gen.getCurrentValueAsLong()); // Timestamp for "2023-03-23 15:30:45"

        // Test serialization as string with custom format and locale
        DateTimeSerializerBase<Date> dateSerializerBaseWithLocale = dateSerializerBase.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("fr", "FR")));
        gen = createJsonGenerator();
        dateSerializerBaseWithLocale.serialize(new Date(), gen, new SerializerProvider());
        assertEquals(JsonParser.ValueType.STRING, gen.getCurrentValueType());
        assertEquals("23-03-2023 15:30:45", gen.getCurrentValueAsString());

        // Test serialization as string with custom format, locale, and timezone
        DateTimeSerializerBase<Date> dateSerializerBaseWithFormatLocaleAndTimezone = dateSerializerBase.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", new Locale("fr", "FR")));
        gen = createJsonGenerator();
        dateSerializerBaseWithFormatLocaleAndTimezone.serialize(new Date(), gen, new SerializerProvider());
        assertEquals(JsonParser.ValueType.STRING, gen.getCurrentValueType());
        assertEquals("23-03-2023 15:30:45 EDT", gen.getCurrentValueAsString());
    }

    @Test
    public void testCreateContextual() {
        JsonParser parser = createJsonParser("2023-03-23 15:30:45");
        ObjectCodec objectMapper = new ObjectMapper();

        // Test with default settings
        DateTimeSerializerBase<Date> dateSerializerBase1 = new DateTimeSerializerBase<Date>(Date.class, false, null);
        JsonSerializer<Date> serializer1 = dateSerializerBase1.createContextual(objectMapper.getSerializerProvider(), null);
        JsonNode node1 = serializer1.serializeValue(new Date(), objectMapper, null);
        assertEquals(JsonParser.ValueType.STRING, node1.getType());
        assertEquals("2023-03-23 15:30:45", node1.getTextValue());

        // Test with custom format
        DateTimeSerializerBase<Date> dateSerializerBase2 = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JsonSerializer<Date> serializer2 = dateSerializerBase2.createContextual(objectMapper.getSerializerProvider(), null);
        JsonNode node2 = serializer2.serializeValue(new Date(), objectMapper, null);
        assertEquals(JsonParser.ValueType.STRING, node2.getType());
        assertEquals("2023-03-23 15:30:45", node2.getTextValue());

        // Test with custom format, locale, and timezone
        DateTimeSerializerBase<Date> dateSerializerBase3 = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", new Locale("fr", "FR")));
        JsonSerializer<Date> serializer3 = dateSerializerBase3.createContextual(objectMapper.getSerializerProvider(), null);
        JsonNode node3 = serializer3.serializeValue(new Date(), objectMapper, null);
        assertEquals(JsonParser.ValueType.STRING, node3.getType());
        assertEquals("23-03-2023 15:30:45 EDT", node3.getTextValue());

        // Test with custom format and timestamp setting
        DateTimeSerializerBase<Date> dateSerializerBase4 = new DateTimeSerializerBase<Date>(Date.class, true, null);
        JsonSerializer<Date> serializer4 = dateSerializerBase4.createContextual(objectMapper.getSerializerProvider(), null);
        JsonNode node4 = serializer4.serializeValue(new Date(), objectMapper, null);
        assertEquals(JsonParser.ValueType.NUMBER, node4.getType());
        assertEquals(1679142645000L, node4.getLongValue());

        // Test with custom format, timestamp setting, locale, and timezone
        DateTimeSerializerBase<Date> dateSerializerBase5 = new DateTimeSerializerBase<Date>(Date.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", new Locale("fr", "FR")));
        JsonSerializer<Date> serializer5 = dateSerializerBase5.createContextual(objectMapper.getSerializerProvider(), null);
        JsonNode node5 = serializer5.serializeValue(new Date(), objectMapper, null);
        assertEquals(JsonParser.ValueType.NUMBER, node5.getType());
        assertEquals(1679142645000L, node5.getLongValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSerializerProvider() {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        dateSerializerBase.createContextual(null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullHandledType() {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(null, false, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCustomFormat() {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        dateSerializerBase.withFormat(Boolean.FALSE, null);
    }

    private JsonParser createJsonParser(String content) {
        ObjectCodec objectMapper = new ObjectMapper();
        return objectMapper.getFactory().createParser(new ByteArrayInputStream(content.getBytes()));
    }

    private JsonGenerator createJsonGenerator() {
        ObjectCodec objectMapper = new ObjectMapper();
        return objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
    }

}