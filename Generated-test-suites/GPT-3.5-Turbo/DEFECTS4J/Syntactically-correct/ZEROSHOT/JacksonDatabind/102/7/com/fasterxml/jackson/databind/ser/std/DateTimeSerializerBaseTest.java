package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class DateTimeSerializerBaseTest {

    @Test
    public void testSerialize_NullCustomFormat() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // TODO Auto-generated method stub

            }

        };

        boolean result = serializer.isEmpty(null, new Date());
        assertEquals(false, result);
    }

    @Test
    public void testSerialize_CustomFormat() throws Exception {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat) {

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // TODO Auto-generated method stub

            }

        };

        boolean result = serializer.isEmpty(null, new Date());
        assertEquals(false, result);
    }

    @Test
    public void testGetSchema_Timestamp() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // TODO Auto-generated method stub

            }

        };

        JsonNode result = serializer.getSchema(null, Date.class);
        assertEquals("number", result.get("type").asText());
    }

    @Test
    public void testGetSchema_String() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)) {

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // TODO Auto-generated method stub

            }

        };

        JsonNode result = serializer.getSchema(null, Date.class);
        assertEquals("string", result.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_Number() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // TODO Auto-generated method stub

            }

        };

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base().expectIntegerFormat(JsonParser.NumberType.LONG);
        serializer.acceptJsonFormatVisitor(visitor, Date.class);
    }

    @Test
    public void testAcceptJsonFormatVisitor_String() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)) {

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // TODO Auto-generated method stub

            }

        };

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base().expectStringFormat(JsonValueFormat.DATE_TIME);
        serializer.acceptJsonFormatVisitor(visitor, Date.class);
    }

    @Test
    public void testSerializeAsString_NullCustomFormat() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                _serializeAsString(value, gen, serializers);
            }

        };

        JsonGenerator gen = null;
        SerializerProvider serializers = null;

        Date value = new Date();
        serializer.serialize(value, gen, serializers);
    }

    @Test
    public void testSerializeAsString_CustomFormat() throws Exception {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat) {

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                _serializeAsString(value, gen, serializers);
            }

        };

        JsonGenerator gen = null;
        SerializerProvider serializers = null;

        Date value = new Date();
        serializer.serialize(value, gen, serializers);
    }
}