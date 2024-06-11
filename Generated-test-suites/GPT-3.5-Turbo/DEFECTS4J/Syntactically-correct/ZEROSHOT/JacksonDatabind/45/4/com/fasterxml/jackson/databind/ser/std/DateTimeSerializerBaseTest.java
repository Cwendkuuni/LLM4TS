package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase serializer;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public boolean isEmpty(Object value) {
                return false;
            }

            @Override
            public boolean isEmpty(SerializerProvider serializers, Object value) {
                return false;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };
    }

    @Test
    public void testCreateContextual() {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        try {
            serializer.createContextual(serializers, property);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = null;
        Type typeHint = null;
        serializer.getSchema(serializers, typeHint);
    }

    @Test
    public void testSerialize() {
        Object value = null;
        JsonGenerator gen = null;
        SerializerProvider serializers = null;
        try {
            serializer.serialize(value, gen, serializers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }
}