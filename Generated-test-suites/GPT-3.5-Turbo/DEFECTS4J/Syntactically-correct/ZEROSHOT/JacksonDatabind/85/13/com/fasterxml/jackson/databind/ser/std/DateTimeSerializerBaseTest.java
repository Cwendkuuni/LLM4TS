
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Object> dateTimeSerializerBase;

    @Before
    public void setup() {
        dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, false, null) {

            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializersMock = mock(SerializerProvider.class);
        BeanProperty propertyMock = mock(BeanProperty.class);

        assertNull(dateTimeSerializerBase.createContextual(serializersMock, null));

        Object handledType = new Object();
        JsonFormat.Value formatMock = mock(JsonFormat.Value.class);
        when(propertyMock.getGenericType()).thenReturn(handledType);
        when(formatMock.getShape()).thenReturn(JsonFormat.Shape.ARRAY);
        assertEquals(dateTimeSerializerBase, dateTimeSerializerBase.createContextual(serializersMock, propertyMock));

        when(formatMock.getShape()).thenReturn(JsonFormat.Shape.NUMBER);
        when(formatMock.hasPattern()).thenReturn(true);
        when(formatMock.getPattern()).thenReturn("yyyy-MM-dd");
        when(formatMock.hasLocale()).thenReturn(true);
        when(formatMock.getLocale()).thenReturn(Locale.ENGLISH);
        when(formatMock.hasTimeZone()).thenReturn(true);
        when(formatMock.getTimeZone()).thenReturn(TimeZone.getDefault());
        assertNull(dateTimeSerializerBase.createContextual(serializersMock, propertyMock));

        when(formatMock.getShape()).thenReturn(JsonFormat.Shape.STRING);
        assertNotNull(dateTimeSerializerBase.createContextual(serializersMock, propertyMock));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(dateTimeSerializerBase.isEmpty(null));
        assertFalse(dateTimeSerializerBase.isEmpty(mock(Object.class)));
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider serializersMock = mock(SerializerProvider.class);

        dateTimeSerializerBase.serialize(null, genMock, serializersMock);
        verify(genMock, times(0)).writeObject(any());

        dateTimeSerializerBase.serialize(mock(Object.class), genMock, serializersMock);
        verify(genMock, times(0)).writeObject(any());
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializersMock = mock(SerializerProvider.class);
        Type typeHintMock = mock(Type.class);

        assertNull(dateTimeSerializerBase.getSchema(serializersMock, typeHintMock));
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsNumber() throws JsonMappingException {
        JsonFormatVisitorWrapper visitorMock = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHintMock = mock(JavaType.class);
        when(visitorMock.getProvider()).thenReturn(null);

        dateTimeSerializerBase.acceptJsonFormatVisitor(visitorMock, typeHintMock);
        verify(visitorMock, times(1)).getProvider();
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsString() throws JsonMappingException {
        JsonFormatVisitorWrapper visitorMock = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHintMock = mock(JavaType.class);
        when(visitorMock.getProvider()).thenReturn(mock(SerializerProvider.class));

        dateTimeSerializerBase.acceptJsonFormatVisitor(visitorMock, typeHintMock);
        verify(visitorMock, times(0)).getProvider();
    }
}
