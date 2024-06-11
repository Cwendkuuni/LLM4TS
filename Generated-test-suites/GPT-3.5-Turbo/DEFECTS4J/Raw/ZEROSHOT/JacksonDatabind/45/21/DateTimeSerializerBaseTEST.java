
package com.fasterxml.jackson.databind.ser.std;

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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> serializer;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // do nothing
            }
        };
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        assertNull(serializer.createContextual(serializers, property));
    }

    @Test
    public void testIsEmpty() {
        Date value = null;
        assertTrue(serializer.isEmpty(value));

        value = new Date(0);
        assertTrue(serializer.isEmpty(value));

        value = new Date();
        assertFalse(serializer.isEmpty(value));
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = null;
        Type typeHint = null;
        JsonNode schema = serializer.getSchema(serializers, typeHint);
        assertNotNull(schema);
        assertTrue(schema.isObject());
        JsonNode formatNode = schema.get("format");
        assertNotNull(formatNode);
        assertEquals(JsonNodeType.STRING, formatNode.getNodeType());
        assertEquals("string", formatNode.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}

