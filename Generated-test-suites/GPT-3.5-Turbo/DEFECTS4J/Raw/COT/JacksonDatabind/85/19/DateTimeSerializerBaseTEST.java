
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase dt = new DateTimeSerializerBase(Object.class, Boolean.TRUE, DateFormat.getDateInstance()) {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, java.lang.reflect.Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper visitor, com.fasterxml.jackson.databind.JavaType typeHint) throws com.fasterxml.jackson.databind.JsonMappingException {

            }
        };

        DateTimeSerializerBase result = dt.withFormat(Boolean.TRUE, DateFormat.getDateInstance());
        assertNotNull(result);
    }

    @Test
    public void testCreateContextual() throws Exception {
        DateTimeSerializerBase dt = new DateTimeSerializerBase(Object.class, Boolean.TRUE, DateFormat.getDateInstance()) {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, java.lang.reflect.Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper visitor, com.fasterxml.jackson.databind.JavaType typeHint) throws com.fasterxml.jackson.databind.JsonMappingException {

            }
        };

        SerializerProvider sp = new SerializerProvider();
        dt.createContextual(sp, null);
        assertNotNull(dt);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase dt = new DateTimeSerializerBase(Object.class, Boolean.TRUE, DateFormat.getDateInstance()) {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, java.lang.reflect.Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper visitor, com.fasterxml.jackson.databind.JavaType typeHint) throws com.fasterxml.jackson.databind.JsonMappingException {

            }
        };

        boolean result = dt.isEmpty(new SerializerProvider(), new Object());
        assertTrue(result);
    }

    @Test
    public void testSerialize() {
        DateTimeSerializerBase dt = new DateTimeSerializerBase(Object.class, Boolean.TRUE, DateFormat.getDateInstance()) {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("Test");
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, java.lang.reflect.Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper visitor, com.fasterxml.jackson.databind.JavaType typeHint) throws com.fasterxml.jackson.databind.JsonMappingException {

            }
        };

        SerializerProvider sp = new SerializerProvider();
        try {
            dt.serialize(new Object(), new JsonGenerator(), sp);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase dt = new DateTimeSerializerBase(Object.class, Boolean.TRUE, DateFormat.getDateInstance()) {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, java.lang.reflect.Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper visitor, com.fasterxml.jackson.databind.JavaType typeHint) throws com.fasterxml.jackson.databind.JsonMappingException {

            }
        };

        SerializerProvider sp = new SerializerProvider();
        String result = dt.getSchema(sp, null).toString();
        assertEquals("CompatibleSchema", result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase dt = new DateTimeSerializerBase(Object.class, Boolean.TRUE, DateFormat.getDateInstance()) {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, java.lang.reflect.Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper visitor, com.fasterxml.jackson.databind.JavaType typeHint) throws com.fasterxml.jackson.databind.JsonMappingException {

            }
        };

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();

        try {
            dt.acceptJsonFormatVisitor(visitor, null);
        } catch (JsonMappingException e) {
            fail("Exception thrown");
        }
    }
}
