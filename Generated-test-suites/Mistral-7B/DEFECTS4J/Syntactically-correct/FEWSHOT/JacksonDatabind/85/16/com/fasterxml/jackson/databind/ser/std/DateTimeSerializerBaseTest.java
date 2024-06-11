package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() {
        // Test constructor
        DateTimeSerializerBase<Long> longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, null);
        DateTimeSerializerBase<java.util.Date> dateSerializer = new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, Boolean.FALSE, null);

        // Test getters
        assertNull(longSerializer._customFormat);
        assertFalse(longSerializer._useTimestamp.booleanValue());
        assertEquals(Long.TYPE, longSerializer.handledType());
        assertNotNull(dateSerializer._customFormat);
        assertFalse(dateSerializer._useTimestamp.booleanValue());
        assertEquals(java.util.Date.class, dateSerializer.handledType());

        // Test withFormat method
        DateTimeSerializerBase<Long> longSerializerWithFormat = longSerializer.withFormat(Boolean.TRUE, null);
        DateTimeSerializerBase<java.util.Date> dateSerializerWithFormat = dateSerializer.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // Test isEmpty method
        assertFalse(longSerializer.isEmpty(null));
        assertFalse(longSerializer.isEmpty(new Long(0L)));
        assertTrue(longSerializer.isEmpty(new Long(1L)));
        assertFalse(dateSerializer.isEmpty(null));
        assertFalse(dateSerializer.isEmpty(new Date(0L)));
        assertTrue(dateSerializer.isEmpty(new Date(1L)));

        // Test getSchema method
        assertNotNull(longSerializer.getSchema(null, Long.TYPE));
        assertNotNull(dateSerializer.getSchema(null, java.util.Date.class));

        // Test createContextual method
        JsonFactory factory = new JsonFactory();
        SerializerProvider serializers = factory.getSerializerProvider();
        ObjectCodec objectCodec = new ObjectMapper().getSerializerProvider().getCodec();
        assertNotNull(longSerializer.createContextual(serializers, null));
        assertNotNull(dateSerializer.createContextual(serializers, null));

        // Test serialize method
        try {
            Long value = 1585651200000L; // 2020-05-15 00:00:00Z
            JsonGenerator gen = factory.createGenerator(new ByteArrayOutputStream());
            longSerializer.serialize(value, gen, serializers);
            assertEquals("1585651200000", gen.getOutputBuffer().toString());
            gen.close();

            Date date = new Date(value);
            gen = factory.createGenerator(new ByteArrayOutputStream());
            dateSerializer.serialize(date, gen, serializers);
            assertEquals("2020-05-15T00:00:00Z", gen.getOutputBuffer().toString());
            gen.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test acceptJsonFormatVisitor method
        JsonSerializer.TypingVisitor visitor = new JsonSerializer.TypingVisitor();
        longSerializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals(JsonType.LONG, visitor.getType());
        visitor = new JsonSerializer.TypingVisitor();
        dateSerializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals(JsonType.VALUE_STRING, visitor.getType());

        // Test _asTimestamp method
        assertFalse(longSerializer._asTimestamp(serializers));
        assertTrue(dateSerializer._asTimestamp(serializers));

        // Test _acceptJsonFormatVisitor method
        JsonFormatVisitorWrapper visitor2 = new JsonFormatVisitorWrapper(new stdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeValueAsString("test");
            }
        });
        longSerializer._acceptJsonFormatVisitor(visitor2, null, true);
        assertEquals(JsonParser.NumberType.LONG, visitor2.getFormat().getNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor2.getFormat().getShape());

        JsonFormat format = new JsonFormat();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd HH:mm:ss");
        visitor2 = new JsonFormatVisitorWrapper(null);
        dateSerializer._acceptJsonFormatVisitor(visitor2, null.getClass(), false);
        assertEquals(format, visitor2.getFormat());
    }
}