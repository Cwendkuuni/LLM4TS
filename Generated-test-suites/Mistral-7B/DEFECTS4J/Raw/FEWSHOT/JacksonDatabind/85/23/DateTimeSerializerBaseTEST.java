package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private DateTimeSerializerBase<T> getSerializer(Boolean useTimestamp, DateFormat customFormat) {
        return new StdDateSerializerBase<T>() {
            @Override
            protected DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }

            @Override
            protected boolean _useTimestamp() {
                return useTimestamp;
            }

            @Override
            protected DateFormat _customFormat() {
                return customFormat;
            }
        }.withFormat(useTimestamp, customFormat);
    }

    @Test
    public void testCreateContextual() throws Exception {
        JsonParser parser = new ObjectMapper().getFactory().createParser("{}");
        JsonParser.ContextualSerializers cctx = parser.getCodec().contextualSerializerInstance(Date.class);

        // Test null values
        SerializerProvider serializers = new SerializerProvider();
        JsonSerializer<Date> serializer1 = cctx.findValueSerializer(serializers, null);
        assertNull(serializer1);

        // Test default serializer
        JsonSerializer<Date> serializer2 = cctx.findValueSerializer(serializers, new Date());
        assertNotNull(serializer2);

        // Test timestamp serializer
        JsonSerializer<Date> serializer3 = cctx.findValueSerializer(serializers, new Date(), new JsonFormat.Value(new JsonFormat.Shape.NUMBER()));
        assertTrue(serializer3 instanceof StdDateSerializer);

        // Test string serializer with custom format
        JsonSerializer<Date> serializer4 = cctx.findValueSerializer(serializers, new Date(), new JsonFormat.Value(new JsonFormat.Shape.STRING(), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")));
        assertTrue(serializer4 instanceof DateTimeSerializerBase);
    }

    @Test
    public void testSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), com.fasterxml.jackson.databind.JsonMapping.Builder().build());
        SerializerProvider serializers = new SerializerProvider();

        DateTimeSerializerBase<Date> serializer = getSerializer(Boolean.TRUE, null);
        serializer.serialize(new Date(0), gen, serializers);
        assertEquals("0\n", gen.getOutputBuffer().toString());

        serializer = getSerializer(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        serializer.serialize(new Date(0), gen, serializers);
        assertEquals("\"0001-01-01T00:00:00.000Z\"\n", gen.getOutputBuffer().toString());

        serializer = getSerializer(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd"));
        serializer.serialize(new Date(1640995200000L), gen, serializers); // Wed Nov 10 00:00:00 CET 2021
        assertEquals("\"2021-11-10\"\n", gen.getOutputBuffer().toString());

        serializer = getSerializer(Boolean.FALSE, null);
        serializer.serialize(new Date(1640995200000L), gen, serializers);
        throw new AssertionError("Expected JSON string, but got number");
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{}");
        JsonFormatVisitorWrapper visitor = mapper.getFactory().constructSchemaVisitor();

        DateTimeSerializerBase<Date> serializer = getSerializer(Boolean.TRUE, null);
        serializer.acceptJsonFormatVisitor(visitor, JavaType.construct(Date.class, null));
        assertEquals(JsonFormat.Shape.NUMBER, visitor.getCurrentShape());

        serializer = getSerializer(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        serializer.acceptJsonFormatVisitor(visitor, JavaType.construct(Date.class, null));
        assertEquals(JsonFormat.Shape.STRING, visitor.getCurrentShape());
        assertEquals("date", visitor.getCurrentName());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getCurrentValueFormat());
    }
}