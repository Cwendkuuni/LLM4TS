package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    public class DateTimeSerializerBaseImpl extends DateTimeSerializerBase<String> {
        public DateTimeSerializerBaseImpl() {
            super(String.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        }

        public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
            return null;
        }

        public boolean isEmpty(String value) {
            return false;
        }

        public boolean isEmpty(SerializerProvider serializers, String value) {
            return false;
        }

        protected long _timestamp(String value) {
            return 0;
        }

        public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
            return null;
        }

        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

        }

        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        }

    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBaseImpl();
        String value = null;
        boolean expected = true;
        boolean result = serializer.isEmpty(value);
        assertEquals(expected, result);
    }
}