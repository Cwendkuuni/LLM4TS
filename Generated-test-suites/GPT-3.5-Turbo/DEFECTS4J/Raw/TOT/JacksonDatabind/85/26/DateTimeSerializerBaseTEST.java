
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private CustomSerializer serializer;

    @Before
    public void setUp() {
        serializer = new CustomSerializer();
    }

    @After
    public void tearDown() {
        serializer = null;
    }

    @Test
    public void testSerializeWithCustomFormat() throws IOException {
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        Date currentDate = new Date();

        serializer.withFormat(false, new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss")).serialize(currentDate, jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerializeWithTimestamp() throws IOException {
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        Date currentDate = new Date();

        serializer.withFormat(true, null).serialize(currentDate, jsonGenerator, serializerProvider);
    }

    @Test
    public void testCreateContextualWithNumericShape() throws IOException {
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        BeanProperty property = null;

        DateTimeSerializerBase<?> base = serializer.createContextual(serializerProvider, property);
        assertTrue(base.withFormat(true, null)._useTimestamp);
    }

    @Test
    public void testCreateContextualWithStringShape() throws IOException {
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        BeanProperty property = null;

        DateTimeSerializerBase<?> base = serializer.createContextual(serializerProvider, property);
        assertFalse(base.withFormat(true, null)._useTimestamp);
    }

    @Test
    public void testCreateContextualWithCustomPattern() throws IOException {
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        BeanProperty property = null;
        JsonFormat.Value format = new JsonFormat.Value();
        format.setPattern("yyyy-MM-dd HH:mm:ss");
        format.setShape(JsonFormat.Shape.STRING);

        DateTimeSerializerBase<?> base = serializer.createContextual(serializerProvider, property);
        assertFalse(base.withFormat(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))._useTimestamp);
    }

    private class CustomSerializer extends DateTimeSerializerBase<Date> {

        private CustomSerializer() {
            super(Date.class, true, new SimpleDateFormat("yyyy-MM-dd"));
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new CustomSerializer();
        }

        @Override
        protected long _timestamp(Date value) {
            return value.getTime();
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(_customFormat.format(value));
        }

        @Override
        public JsonNode getSchema(SerializerProvider serializers, java.lang.reflect.Type typeHint) {
            return null;
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, com.fasterxml.jackson.databind.JavaType typeHint)
                throws com.fasterxml.jackson.databind.JsonMappingException {

        }
    }

}

