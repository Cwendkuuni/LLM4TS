package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.apache.commons.cli.Options;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Test
    public void testSerialize() throws IOException {
        SerializerProvider serializers = null;
        JsonGenerator gen = null;
        
        // Test case 1
        DateTimeSerializerBase<Object> serializer1 = new DateTimeSerializerBase<Object>(
                Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp,
                    DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen,
                    SerializerProvider serializers) throws IOException {
                assertEquals("", gen.toString());
            }
        };
        serializer1.serialize(null, gen, serializers);
        
        // Test case 2
        DateTimeSerializerBase<Object> serializer2 = new DateTimeSerializerBase<Object>(
                Object.class, false, new StdDateFormat()) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp,
                    DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen,
                    SerializerProvider serializers) throws IOException {
                assertEquals("", gen.toString());
            }
        };
        serializer2.serialize(null, gen, serializers);
        
    }

    @Test
    public void testIsEmpty() {
        // Test case 1
        DateTimeSerializerBase<Object> serializer1 = new DateTimeSerializerBase<Object>(
                Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp,
                    DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };
        assertNull(serializer1.isEmpty(null));
        
        // Test case 2
        DateTimeSerializerBase<Object> serializer2 = new DateTimeSerializerBase<Object>(
                Object.class, false, new StdDateFormat()) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp,
                    DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };
        assertNull(serializer2.isEmpty(null));
    }
    
}