package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase dateTimeSerializerBase;

    @Before
    public void setUp() {
        dateTimeSerializerBase = new DateTimeSerializerBase(mock(Class.class), true, null) {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0l;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };
    }

    @Test
    public void testWithContextual() throws JsonMappingException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanProperty beanProperty = mock(BeanProperty.class);
        assertEquals(dateTimeSerializerBase, dateTimeSerializerBase.createContextual(serializerProvider, null));
        assertNull(dateTimeSerializerBase.createContextual(serializerProvider, beanProperty));

        JsonFormat.Value format = new JsonFormat.Value(null, null, null, null, null, null, null, null);
        assertEquals(dateTimeSerializerBase, dateTimeSerializerBase.createContextual(serializerProvider, null));
        assertNull(dateTimeSerializerBase.createContextual(serializerProvider, beanProperty));

        format = new JsonFormat.Value(JsonFormat.Shape.NULL, null, null, null, null, null, null, null);
        assertEquals(dateTimeSerializerBase, dateTimeSerializerBase.createContextual(serializerProvider, null));
        assertNull(dateTimeSerializerBase.createContextual(serializerProvider, beanProperty));

        format = new JsonFormat.Value(JsonFormat.Shape.ARRAY, null, null, null, null, null, null, null);
        assertEquals(dateTimeSerializerBase, dateTimeSerializerBase.createContextual(serializerProvider, null));
        assertNull(dateTimeSerializerBase.createContextual(serializerProvider, beanProperty));

        format = new JsonFormat.Value(JsonFormat.Shape.BOOLEAN, null, null, null, null, null, null, null);
        assertEquals(dateTimeSerializerBase, dateTimeSerializerBase.createContextual(serializerProvider, null));
        assertNull(dateTimeSerializerBase.createContextual(serializerProvider, beanProperty));

        format = new JsonFormat.Value(JsonFormat.Shape.EMPTY, null, null, null, null, null, null, null);
        assertEquals(dateTimeSerializerBase, dateTimeSerializerBase.createContextual(serializerProvider, null));
        assertNull(dateTimeSerializerBase.createContextual(serializerProvider, beanProperty));

        format = new JsonFormat.Value(JsonFormat.Shape.NUMBER, null, null, null, null, null, null, null);
        assertEquals(dateTimeSerializerBase, dateTimeSerializerBase.createContextual(serializerProvider, beanProperty));
    }

    @Test
    public void testIsEmpty() {
        assertNull(dateTimeSerializerBase.isEmpty(null));
        assertNull(dateTimeSerializerBase.isEmpty(mock(SerializerProvider.class), null));
    }

    @Test
    public void testGetSchema() {
        assertNull(dateTimeSerializerBase.getSchema(mock(SerializerProvider.class), null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);
    }
}