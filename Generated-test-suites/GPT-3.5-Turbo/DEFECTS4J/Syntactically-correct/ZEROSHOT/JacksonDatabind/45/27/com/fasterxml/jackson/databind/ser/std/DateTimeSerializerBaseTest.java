package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

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
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {
    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Integer> serializer = new DateTimeSerializerBase<Integer>(Integer.class, false, null) {
            @Override
            public DateTimeSerializerBase<Integer> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Integer value) {
                return 0;
            }

            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        Assert.assertEquals(true, serializer.isEmpty(null));
        Assert.assertEquals(true, serializer.isEmpty(null, 0));
        Assert.assertEquals(true, serializer.isEmpty(null, 1));
        Assert.assertEquals(false, serializer.isEmpty(null, 2));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Integer> serializer = new DateTimeSerializerBase<Integer>(Integer.class, false, null) {
            @Override
            public DateTimeSerializerBase<Integer> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Integer value) {
                return 0;
            }

            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        JsonNode expected = serializer.createSchemaNode(serializer._asTimestamp(null) ? "number" : "string", true);
        Assert.assertEquals(expected, serializer.getSchema(null, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Integer> serializer = new DateTimeSerializerBase<Integer>(Integer.class, false, null) {
            @Override
            public DateTimeSerializerBase<Integer> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Integer value) {
                return 0;
            }

            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        serializer.acceptJsonFormatVisitor(visitor, null);
        Assert.assertNotNull(visitor);
    }
}