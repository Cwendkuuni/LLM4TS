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
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class DateTimeSerializerBaseTest {
    @SuppressWarnings("unchecked")
    @Test
    public void testDateTimeSerializerBase() {
        DateTimeSerializerBase<Long> longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        DateTimeSerializerBase<LocalDateTime> localDateTimeSerializer = new DateTimeSerializerBase<LocalDateTime>(LocalDateTime.class, false, null);

        // Test empty value
        assertNull(longSerializer.serialize(null, null, null));
        assertNull(localDateTimeSerializer.serialize(null, null, null));

        // Test serialization as timestamp
        Long value1 = System.currentTimeMillis();
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        longSerializer.serialize(value1, gen, null);
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputBytes()));
        assertEquals(value1.longValue(), parser.getValueAsLong());

        // Test serialization as string
        LocalDateTime value2 = LocalDateTime.of(2023, 1, 1, 12, 30);
        JsonNode schema = longSerializer.getSchema(null, Long.TYPE);
        assertEquals("number", schema.getClass().getSimpleName());

        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        localDateTimeSerializer.serialize(value2, gen, null);
        schema = localDateTimeSerializer.getSchema(null, LocalDateTime.class);
        assertEquals("string", schema.getClass().getSimpleName());
        assertEquals("\"2023-01-01T12:30:00\"", schema.asText());

        assertEquals("2023-01-01T12:30:00", localDateTimeSerializer.serialize(value2, null, null).asText());

        // Test custom format
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<LocalDateTime> customFormatSerializer = longSerializer.withFormat(false, customFormat);
        JsonNode schemaWithCustomFormat = customFormatSerializer.getSchema(null, LocalDateTime.class);
        assertEquals("string", schemaWithCustomFormat.getClass().getSimpleName());
        assertEquals("\"2023-01-01T12:30:00.000Z\"", schemaWithCustomFormat.asText());
        assertEquals("2023-01-01T12:30:00.000Z", customFormatSerializer.serialize(value2, null, null).asText());

        // Test serialization as timestamp with custom format
        DateTimeSerializerBase<LocalDateTime> timestampWithCustomFormatSerializer = longSerializer.withFormat(true, customFormat);
        JsonGenerator genWithCustomFormat = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        timestampWithCustomFormatSerializer.serialize(value2, genWithCustomFormat, null);
        JsonParser parserWithCustomFormat = new JsonFactory().createParser(new ByteArrayInputStream(genWithCustomFormat.getOutputBytes()));
        assertEquals("2023-01-01T12:30:00.000Z", parserWithCustomFormat.getValueAsText());
    }
}