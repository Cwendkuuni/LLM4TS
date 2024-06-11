
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Type;
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
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer();
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateTimeSerializerBase<Date> modifiedSerializer = serializer.withFormat(true, customFormat);
        assertEquals(true, modifiedSerializer._useTimestamp);
        assertEquals(customFormat, modifiedSerializer._customFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer();
        SerializerProvider serializers = new DefaultSerializerProvider.Impl();
        BeanProperty property = null;
        DateTimeSerializerBase<Date> modifiedSerializer = serializer.createContextual(serializers, property);
        assertEquals(serializer, modifiedSerializer);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer();
        Date date = null;
        boolean isEmpty = serializer.isEmpty(date);
        assertEquals(true, isEmpty);
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer();
        SerializerProvider serializers = new DefaultSerializerProvider.Impl();
        Type typeHint = Date.class;
        JsonNode schema = serializer.getSchema(serializers, typeHint);
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer();
        JsonGenerator gen = null;
        SerializerProvider serializers = new DefaultSerializerProvider.Impl();
        Date date = new Date();
        serializer.serialize(date, gen, serializers);
        // Serialization cannot be tested without mocking the JsonGenerator
    }

    private static class DateTimeSerializer extends DateTimeSerializerBase<Date> {

        public DateTimeSerializer() {
            super(Date.class, false, null);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new DateTimeSerializer();
        }

        @Override
        protected long _timestamp(Date value) {
            return value.getTime();
        }
    }

}

