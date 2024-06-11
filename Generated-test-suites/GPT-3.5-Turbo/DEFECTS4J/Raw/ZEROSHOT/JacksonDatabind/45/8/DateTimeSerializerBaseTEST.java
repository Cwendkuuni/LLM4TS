package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {
    private DateTimeSerializerBase<TestObject> serializer;

    @Before
    public void setup() {
        serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, false, null) {
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
                // do nothing
            }
        };
    }

    @Test
    public void testCreateContextual_WithShapeNumeric_ReturnsWithFormatTrue() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;

        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        assertTrue(result instanceof DateTimeSerializerBase);
        assertTrue(((DateTimeSerializerBase) result)._useTimestamp);
    }

    @Test
    public void testCreateContextual_WithShapeString_ReturnsWithFormatFalseAndCustomDateFormat() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonFormat.Value format = new JsonFormat.Value();

        format = format.withShape(JsonFormat.Shape.STRING);

        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        assertTrue(result instanceof DateTimeSerializerBase);
        assertFalse(((DateTimeSerializerBase) result)._useTimestamp);
        assertNotNull(((DateTimeSerializerBase) result)._customFormat);
    }

    @Test
    public void testIsEmpty_WithValueIsNull_ReturnsTrue() {
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmpty_WithTimestampZero_ReturnsTrue() {
        TestObject value = new TestObject();
        value.setTimestamp(0L);

        assertTrue(serializer.isEmpty(value));
    }

    @Test
    public void testIsEmpty_WithValueIsNotNullAndTimestampIsNotZero_ReturnsFalse() {
        TestObject value = new TestObject();
        value.setTimestamp(1L);

        assertFalse(serializer.isEmpty(value));
    }

    @Test
    public void testGetSchema_WithTimestamp_ReturnsNumberSchemaNode() {
        SerializerProvider serializers = null;
        Type typeHint = null;

        JsonNode result = serializer.getSchema(serializers, typeHint);

        assertTrue(result.isNumber());
    }

    @Test
    public void testGetSchema_WithString_ReturnsStringSchemaNode() {
        SerializerProvider serializers = null;
        Type typeHint = null;
        JsonSerializer<String> stringSerializer = new JsonSerializer<String>() {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // do nothing
            }

            @Override
            public boolean isEmpty(SerializerProvider provider, String value) {
                return false;
            }
        };

        JsonNode result = stringSerializer.getSchema(serializers, typeHint);

        assertTrue(result.isString());
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithNumericTimestamp_VisitsIntFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        boolean asNumber = true;

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        assertEquals(JsonParser.NumberType.LONG, visitor.getAttribute(JsonFormatVisitable.VISIT_INTEGER_FORMAT));
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getAttribute(JsonFormatValueFormatVisitorBase.TYPE_FORMAT));
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithStringTimestamp_VisitsStringFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        boolean asNumber = false;

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        assertEquals(JsonValueFormat.DATE_TIME, visitor.getAttribute(JsonFormatValueFormatVisitorBase.TYPE_FORMAT));
    }

    class TestObject {
        private long timestamp;

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}