package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
        assertNull(serializer.withFormat(true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
        assertNotNull(serializer.createContextual(null, null));
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
        assertFalse(serializer.isEmpty(null, new TestObject()));
    }


    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
        assertNotNull(serializer.getSchema(null, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
        serializer.acceptJsonFormatVisitor(null, null);
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
        serializer.serialize(new TestObject(), null, null);
    }

}
class TestObject {

}