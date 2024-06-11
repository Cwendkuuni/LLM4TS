package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    DateTimeSerializerBase<DateTime> serializer;
    SerializerProvider serializerProvider;

    @Before
    public void setup() {
        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, true, null) {

            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {

            }

        };

        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(this.serializer.findFormatOverrides(serializers, property, DateTime.class)).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(false);
        assertNull(serializer.createContextual(serializers, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        serializer._acceptJsonFormatVisitor(visitor, typeHint, true);

        verify(visitor, times(1)).visitIntFormat(eq(typeHint), eq(JsonParser.NumberType.LONG),
                eq(JsonValueFormat.UTC_MILLISEC));
    }

    @Test
    public void testAsTimestampWithUseTimestampNotNull() {
        assertEquals(true, serializer._asTimestamp(serializerProvider));
    }

    @Test
    public void testGetSchema() {
        JsonNode node = serializer.getSchema(serializerProvider, null);

        assertEquals("number", node.get("type").asText());
        assertEquals(true, node.get("required").asBoolean());
    }

    @Test
    public void testIsEmpty() {
        DateTime value = new DateTime();

        assertEquals(true, serializer.isEmpty(value));
        assertEquals(true, serializer.isEmpty(serializerProvider, value));
    }

    @Test
    public void testSerialize() throws IOException {
        DateTime value = new DateTime();
        JsonGenerator gen = mock(JsonGenerator.class);
        serializer.serialize(value, gen, serializerProvider);

        verify(gen, Mockito.never()).writeNumber(anyLong());
    }
}