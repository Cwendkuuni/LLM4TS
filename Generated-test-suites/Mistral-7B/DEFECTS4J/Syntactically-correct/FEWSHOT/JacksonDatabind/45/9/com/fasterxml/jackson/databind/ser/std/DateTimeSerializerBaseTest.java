package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDateTimeSerializerBase() {
        DateTimeSerializerBase<Long> dateTimeSerializerBaseLong = new DateTimeSerializerBase<Long>(Long.class, true, null);
        DateTimeSerializerBase<String> dateTimeSerializerBaseString = new DateTimeSerializerBase<String>(String.class, false, null);

        // Test isEmpty method
        assertNotNull(dateTimeSerializerBaseLong);
        assertNotNull(dateTimeSerializerBaseString);
        assertEquals(false, dateTimeSerializerBaseLong.isEmpty(null));
        assertEquals(false, dateTimeSerializerBaseLong.isEmpty(new Long(0L)));
        assertEquals(true, dateTimeSerializerBaseLong.isEmpty(new Long(1L)));
        assertEquals(false, dateTimeSerializerBaseString.isEmpty(null));
        assertEquals(true, dateTimeSerializerBaseString.isEmpty(""));

        // Test withFormat method
        DateTimeSerializerBase<Long> timestampSerializer = dateTimeSerializerBaseLong.withFormat(true, null);
        DateTimeSerializerBase<String> customFormatSerializer = dateTimeSerializerBaseString.withFormat(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));

        // Test createContextual method
        JsonSerializer<Long> serializerLong = timestampSerializer.createContextual(null, null);
        JsonSerializer<String> serializerString = customFormatSerializer.createContextual(null, null);

        // Test serialize method with null value
        JsonGenerator gen = null;
        try {
            serializerLong.serialize(null, gen, null);
            fail("Expected IllegalStateException for null JsonGenerator");
        } catch (IllegalStateException e) {
            // Expected
        }

        try {
            serializerString.serialize(null, gen, null);
            fail("Expected IllegalStateException for null JsonGenerator");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test serialize method with Long value
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializerLong.serialize(new Long(1516041600000L), gen, null); // Timestamp for 2017-12-31 12:00:00 UTC
        assertEquals("1516041600000", gen.getOutputBuffer().toString().trim());

        // Test serialize method with String value using custom format
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializerString.serialize("2017-12-31 12:00:00 CET", gen, null);
        assertEquals("\"2017-12-31 12:00:00 CET\"", gen.getOutputBuffer().toString().trim());

        // Test acceptJsonFormatVisitor method
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StdDateFormat(), new ObjectMapper().getSerializerProvider());
        DateTimeSerializerBase<?> serializer = dateTimeSerializerBaseString;
        serializer._acceptJsonFormatVisitor(visitor, null, true);
        assertNotNull(visitor.getFormatShape());
        assertEquals(JsonFormat.Shape.NUMBER, visitor.getFormatShape().getShape());

        serializer = dateTimeSerializerBaseString;
        serializer._acceptJsonFormatVisitor(visitor, null, false);
        assertNotNull(visitor.getFormatShape());
        assertEquals(JsonFormat.Shape.STRING, visitor.getFormatShape().getShape());
    }
}