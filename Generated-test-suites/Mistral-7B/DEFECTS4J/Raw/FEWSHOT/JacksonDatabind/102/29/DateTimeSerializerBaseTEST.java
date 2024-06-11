package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    private static class TestSerializer extends DateTimeSerializerBase<Long> {
        TestSerializer() {
            this(null, null, null);
        }

        TestSerializer(Boolean useTimestamp, DateFormat customFormat) {
            super(Long.TYPE, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
            if (timestamp != null) {
                _useTimestamp = timestamp;
            }
            if (customFormat != null) {
                _customFormat = customFormat;
                _reusedCustomFormat = new AtomicReference<DateFormat>(customFormat);
            }
            return this;
        }

        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            super.serialize(value, gen, serializers);
        }
    }

    @Test
    public void testDefaultSerializer() throws Exception {
        TestSerializer serializer = new TestSerializer();

        JsonParser parser = new ObjectMapper().readValue(new String("[]"), new TypeReference<Object[]>() {});
        assertEquals(0, parser.size());

        JsonGenerator generator = new ObjectMapper().createGenerator(new StringWriter());
        serializer.serialize(1234567890123L, generator, new SerializerProvider());
        assertEquals("1234567890123", generator.getOutputContentAsString().trim());

        generator = new ObjectMapper().createGenerator(new StringWriter());
        serializer.serialize(Instant.now().getEpochSecond(), generator, new SerializerProvider());
        assertEquals("\"1630112363000\"", generator.getOutputContentAsString().trim());
    }

    @Test
    public void testUseTimestampSerializer() throws Exception {
        TestSerializer serializer = new TestSerializer(true, null);

        JsonParser parser = new ObjectMapper().readValue(new String("[]"), new TypeReference<Object[]>() {});
        assertEquals(0, parser.size());

        JsonGenerator generator = new ObjectMapper().createGenerator(new StringWriter());
        serializer.serialize(1234567890123L, generator, new SerializerProvider());
        assertEquals("1234567890123", generator.getOutputContentAsString().trim());

        generator = new ObjectMapper().createGenerator(new StringWriter());
        serializer.serialize(Instant.now().getEpochSecond(), generator, new SerializerProvider());
        assertEquals("1630112363000", generator.getOutputContentAsString().trim());
    }

    @Test
    public void testCustomFormatSerializer() throws Exception {
        TestSerializer serializer = new TestSerializer(false, new SimpleDateFormat("dd-MMM-yyyy hh:mm a z", Locale.ENGLISH));

        JsonParser parser = new ObjectMapper().readValue(new String("[]"), new TypeReference<Object[]>() {});
        assertEquals(0, parser.size());

        JsonGenerator generator = new ObjectMapper().createGenerator(new StringWriter());
        serializer.serialize(Date.from(Instant.now()), generator, new SerializerProvider());
        assertEquals("25-JUN-2023 11:30 AM CET", generator.getOutputContentAsString().trim());

        generator = new ObjectMapper().createGenerator(new StringWriter());
        serializer.serialize(Instant.now().getEpochSecond(), generator, new SerializerProvider());
        assertEquals("\"25-JUN-2023 11:30:56 AM CET\"", generator.getOutputContentAsString().trim());
    }

    @Test
    public void testReusedCustomFormatSerializer() throws Exception {
        TestSerializer serializer = new TestSerializer(false, new SimpleDateFormat("dd-MMM-yyyy hh:mm a z", Locale.ENGLISH));

        DateFormat customFormat = serializer._customFormat;
        assertNotNull(customFormat);

        JsonParser parser = new ObjectMapper().readValue(new String("[]"), new TypeReference<Object[]>() {});
        assertEquals(0, parser.size());

        JsonGenerator generator = new ObjectMapper().createGenerator(new StringWriter());
        serializer.serialize(Date.from(Instant.now()), generator, new SerializerProvider());
        assertEquals("25-JUN-2023 11:30 AM CET", generator.getOutputContentAsString().trim());

        JsonParser parser2 = new ObjectMapper().readValue(new String("[]"), new TypeReference<Object[]>() {});
        assertEquals(0, parser2.size());

        JsonGenerator generator2 = new ObjectMapper().createGenerator(new StringWriter());
        serializer.serialize(Date.from(Instant.now()), generator2, new SerializerProvider());
        assertEquals("25-JUN-2023 11:30 AM CET", generator2.getOutputContentAsString().trim());

        assertNotNull(serializer._reusedCustomFormat.get());
        assertEquals(customFormat, serializer._reusedCustomFormat.get());
    }

    @Test
    public void testJsonFormatVisitor() throws Exception {
        TestSerializer serializer = new TestSerializer(false, null);

        JsonFormatVisitorWrapper visitor = new JsonSerializer.StdSerializer.BaseJsonFormatVisitor(new SerializerProvider(), null, null);
        serializer._acceptJsonFormatVisitor(visitor, Long.TYPE, false);
        assertEquals("DATE_TIME", visitor.getFormatName());
    }

    @Test(expected = IllegalStateException.class)
    public void testMalformedCustomFormatSerializer() throws Exception {
        TestSerializer serializer = new TestSerializer(false, new SimpleDateFormat("dd-MMM-yyyy hh:mm a zzz", Locale.ENGLISH));

        JsonParser parser = new ObjectMapper().readValue(new String("[]"), new TypeReference<Object[]>() {});
        assertEquals(0, parser.size());

        JsonGenerator generator = new ObjectMapper().createGenerator(new StringWriter());
        serializer.serialize(Date.from(Instant.now()), generator, new SerializerProvider());
        // The test should fail here with an IOException or an IllegalStateException
    }
}