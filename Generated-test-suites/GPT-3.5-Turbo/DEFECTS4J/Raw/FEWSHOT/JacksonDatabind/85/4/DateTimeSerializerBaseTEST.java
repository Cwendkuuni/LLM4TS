package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
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

import org.junit.Test;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseConstructor() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // implementation not needed for the test
            }

            @Override
            protected long _timestamp(String value) {
                // implementation not needed for the test
                return 0;
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                // implementation not needed for the test
                return null;
            }
        };

        assertTrue(serializer._useTimestamp);
        assertNotNull(serializer._customFormat);
    }

    @Test
    public void testCreateContextualWithNullProperty() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // implementation not needed for the test
            }

            @Override
            protected long _timestamp(String value) {
                // implementation not needed for the test
                return 0;
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                // implementation not needed for the test
                return null;
            }
        };

        assertNull(serializer.createContextual(null, null));
    }

    @Test
    public void testCreateContextualWithNumericShape() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // implementation not needed for the test
            }

            @Override
            protected long _timestamp(String value) {
                // implementation not needed for the test
                return 0;
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                // implementation not needed for the test
                return null;
            }
        };

        assertTrue(serializer.createContextual(null, null)._useTimestamp);
    }

    @Test
    public void testCreateContextualWithStringShape() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // implementation not needed for the test
            }

            @Override
            protected long _timestamp(String value) {
                // implementation not needed for the test
                return 0;
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                // implementation not needed for the test
                return null;
            }
        };

        assertFalse(serializer.createContextual(null, null)._useTimestamp);
    }

    @Test
    public void testAcceptJsonFormatVisitorAsNumber() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // implementation not needed for the test
            }

            @Override
            protected long _timestamp(String value) {
                // implementation not needed for the test
                return 0;
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                // implementation not needed for the test
                return null;
            }
        };

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertEquals(JsonParser.NumberType.LONG, visitor
                .getProvider()
                .expectIntegerFormat(typeHint)
                .getNumberType());
    }

    @Test
    public void testAcceptJsonFormatVisitorAsString() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // implementation not needed for the test
            }

            @Override
            protected long _timestamp(String value) {
                // implementation not needed for the test
                return 0;
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                // implementation not needed for the test
                return null;
            }
        };

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertEquals(JsonValueFormat.DATE_TIME, visitor
                .getProvider()
                .expectStringFormat(typeHint)
                .getFormat());
    }
}