package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase.TimestampType;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase_withDefaultSettings() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(DateTimeSerializerBase.class);
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);
        assertNotNull(serializer);
        assertEquals(TimestampType.DATE_TIME, serializer.getSerializeAs(), TimestampType.DATE_TIME);
        assertNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
        assertFalse(serializer._useTimestamp.booleanValue());
    }

    @Test
    public void testDateTimeSerializerBase_withCustomFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss zzz");
        JavaType javaType = mapper.getTypeFactory().constructType(DateTimeSerializerBase.class);
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);
        serializer = serializer.withFormat(Boolean.FALSE, customFormat);
        assertEquals(TimestampType.STRING, serializer.getSerializeAs(), TimestampType.STRING);
        assertEquals(customFormat, serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
        assertFalse(serializer._useTimestamp.booleanValue());
    }

    @Test
    public void testDateTimeSerializerBase_withCustomFormatAndThreadSafeReuse() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss zzz");
        JavaType javaType = mapper.getTypeFactory().constructType(DateTimeSerializerBase.class);
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);
        serializer = serializer.withFormat(Boolean.FALSE, customFormat);
        assertEquals(TimestampType.STRING, serializer.getSerializeAs(), TimestampType.STRING);
        assertEquals(customFormat, serializer._customFormat);
        assertNotNull(serializer._reusedCustomFormat);
        DateFormat formatter = serializer._reusedCustomFormat.getAndSet(null);
        assertNotNull(formatter);
        assertEquals(customFormat, formatter);
        assertFalse(serializer._useTimestamp.booleanValue());
    }

    @Test
    public void testDateTimeSerializerBase_withUseTimestamp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(DateTimeSerializerBase.class);
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);
        serializer = serializer.withFormat(Boolean.TRUE, null);
        assertEquals(TimestampType.NUMBER, serializer.getSerializeAs(), TimestampType.NUMBER);
        assertNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
        assertTrue(serializer._useTimestamp.booleanValue());
    }

    @Test
    public void testSerialize_withDefaultSettings() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);
        Date date = new Date();
        serializer.serialize(date, gen, null);
        String output = gen.getOutputStr();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        Date parsedDate = dateFormat.parse(output);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testSerialize_withCustomFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss zzz");
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);
        serializer = serializer.withFormat(Boolean.FALSE, customFormat);
        JsonGenerator gen = mapper.getFactory().createGenerator();
        Date date = new Date();
        serializer.serialize(date, gen, null);
        String output = gen.getOutputStr();
        CustomDateFormatParser parser = new CustomDateFormatParser(customFormat);
        Date parsedDate = parser.parse(output);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testSerialize_withUseTimestamp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);
        serializer = serializer.withFormat(Boolean.TRUE, null);
        JsonGenerator gen = mapper.getFactory().createGenerator();
        Date date = new Date();
        serializer.serialize(date, gen, null);
        long timestamp = gen.getOutputStream().readLong();
        assertEquals(date.getTime(), timestamp);
    }

    @Test
    public void testDeserialize_withDefaultSettings() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(mapper.writeValueAsString(new Date()));
        DateTimeSerializerBase<Date> serializer = mapper.findValueDeserializer(Date.class, null);
        Date date = serializer.deserialize(parser, null);
        assertEquals(parser.getValueAsString(), mapper.writeValueAsString(date));
    }

    @Test
    public void testDeserialize_withCustomFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss zzz");
        DateTimeSerializerBase<Date> serializer = mapper.findValueDeserializer(Date.class, null);
        serializer = (DateTimeSerializerBase<Date>) serializer.withContextualHandler(new CustomDateFormatContextualDeserializer(customFormat));
        JsonParser parser = mapper.getFactory().createParser(mapper.writeValueAsString("01-01-2023 12:00:00 EST"));
        Date date = serializer.deserialize(parser, null);
        assertEquals(parser.getValueAsString(), "01-01-2023 12:00:00 EST");
        assertEquals(date, new Date(parser.getValueAsLong()));
    }

    @Test
    public void testDeserialize_withUseTimestamp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = mapper.findValueDeserializer(Date.class, null);
        serializer = (DateTimeSerializerBase<Date>) serializer.withContextualHandler(new TimestampDeserializer());
        JsonParser parser = mapper.getFactory().createParser("1672492545000");
        Date date = serializer.deserialize(parser, null);
        assertEquals(parser.getValueAsString(), "1672492545000");
        assertEquals(date, new Date(parser.getValueAsLong()));
    }

    private static class CustomDateFormatParser extends StdDateParser {

        private final SimpleDateFormat _dateFormat;

        public CustomDateFormatParser(SimpleDateFormat dateFormat) {
            _dateFormat = dateFormat;
        }

        @Override
        protected Date parseparsedDate(String text, ParsePosition pp) throws ParseException {
            return _dateFormat.parse(text);
        }
    }

    private static class CustomDateFormatContextualDeserializer extends BaseDateDeserializer {

        private final SimpleDateFormat _customFormat;

        public CustomDateFormatContextualDeserializer(SimpleDateFormat customFormat) {
            _customFormat = customFormat;
        }

        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return _customFormat.parse(p.getValueAsString());
        }
    }

    private static class TimestampDeserializer extends BaseDateDeserializer {

        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Date(p.getValueAsLong());
        }
    }
}