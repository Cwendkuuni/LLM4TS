package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Object> serializer;

    @Before
    public void setUp() throws Exception {
        serializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
                //Do nothing
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                //Do nothing
            }
        };
    }

    @Test
    public void testIsEmpty_nullValue_returnsTrue() {
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmpty_emptyValue_returnsTrue() {
        assertTrue(serializer.isEmpty(new Object()));
    }

    @Test
    public void testIsEmpty_nonEmptyValue_returnsFalse() {
        assertFalse(serializer.isEmpty(new Object() {
            @Override
            public String toString() {
                return "non-empty";
            }
        }));
    }

    @Test
    public void testGetSchema_returnsNull() {
        assertNull(serializer.getSchema(null, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor_noProvider_setsNumberFormat() throws JsonMappingException {
        final JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        final JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertEquals("number", visitor.getProvider().getTypeId());
    }
}