package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testSerialize_TimestampFormat() throws IOException {
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

        assertTrue(serializer._asTimestamp(null));
        assertFalse(serializer._asTimestamp(serializers));
    }

    @Test
    public void testSerialize_StringFormat() throws IOException {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, df) {
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

        assertFalse(serializer._asTimestamp(null));
        assertFalse(serializer._asTimestamp(serializers));
    }

    @Test
    public void testSerialize_SerializeNull() throws IOException {
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

        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(serializers, null));
    }

    @Test
    public void testGetSchema_NumberFormat() throws JsonMappingException {
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

        JsonNode schema = serializer.getSchema(null, null);
        assertNotNull(schema);
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testGetSchema_StringFormat() throws JsonMappingException {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, df) {
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

        JsonNode schema = serializer.getSchema(null, null);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
        assertEquals("date-time", schema.get("format").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsNumber() throws JsonMappingException {
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

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base().expectNumberFormat(JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
        serializer.acceptJsonFormatVisitor(visitor, null);

        // TODO: Assert statements for the expected behavior
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsString() throws JsonMappingException {
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

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base().expectStringFormat(JsonValueFormat.DATE_TIME);
        serializer.acceptJsonFormatVisitor(visitor, null);

        // TODO: Assert statements for the expected behavior
    }
}