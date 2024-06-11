package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<DateTime> dateTimeSerializerBase;

    @Before
    public void setUp() {
        dateTimeSerializerBase = new DateTimeSerializerBaseTestImplementation(
                DateTime.class, true, mock(DateFormat.class)) {
            @Override
            public void serialize(DateTime value, JsonGenerator gen,
                    SerializerProvider serializers) throws IOException {
            }

            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }
        };
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<DateTime> result = dateTimeSerializerBase
                .withFormat(true, mock(DateFormat.class));
        assertEquals(true, result._useTimestamp);
        assertNull(result._customFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        DateTimeSerializerBase<DateTime> result = (DateTimeSerializerBase<DateTime>) dateTimeSerializerBase
                .createContextual(serializers, property);
        assertEquals(dateTimeSerializerBase, result);
    }

    @Test
    public void testIsEmpty() {
        DateTime value = new DateTime();
        assertEquals(false, dateTimeSerializerBase.isEmpty(value));
        assertEquals(false,
                dateTimeSerializerBase.isEmpty(mock(SerializerProvider.class),
                        value));
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        JsonNode result = dateTimeSerializerBase.getSchema(serializers,
                typeHint);
        assertEquals("string", result.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);
    }
}

class DateTimeSerializerBaseTestImplementation extends
        DateTimeSerializerBase<DateTime> {

    protected DateTimeSerializerBaseTestImplementation(Class<DateTime> type,
            Boolean useTimestamp, DateFormat customFormat) {
        super(type, useTimestamp, customFormat);
    }

    @Override
    public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp,
            DateFormat customFormat) {
        return null;
    }

    @Override
    public void serialize(DateTime value, JsonGenerator gen,
            SerializerProvider serializers) throws IOException {
    }

    @Override
    protected long _timestamp(DateTime value) {
        return 0;
    }
}