package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBaseTestClass serializer;

    @Before
    public void setup() {
        serializer = new DateTimeSerializerBaseTestClass();
    }

    @Test
    public void testCreateContextualWithNullProperty() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);

        JsonSerializer jsonSerializer = serializer.createContextual(serializers, null);

        assertEquals(serializer, jsonSerializer);
    }

    @Test
    public void testCreateContextualWithNumericShapeFormat() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);

        when(property.getType()).thenReturn(mock(Type.class));
        when(serializer.findFormatOverrides(eq(serializers), eq(property), eq(serializer.handledType())))
                .thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);

        DateTimeSerializerBaseTestClass jsonSerializer =
                (DateTimeSerializerBaseTestClass) serializer.createContextual(serializers, property);

        assertEquals(Boolean.TRUE, jsonSerializer._useTimestamp);
        assertEquals(null, jsonSerializer._customFormat);
    }

    @Test
    public void testCreateContextualWithStringShapeFormatAndPattern() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);

        when(property.getType()).thenReturn(mock(Type.class));
        when(serializer.findFormatOverrides(eq(serializers), eq(property), eq(serializer.handledType())))
                .thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(true);
        when(format.getPattern()).thenReturn("yyyy-MM-dd");
        when(format.hasLocale()).thenReturn(false);
        when(format.hasTimeZone()).thenReturn(false);

        DateTimeSerializerBaseTestClass jsonSerializer =
                (DateTimeSerializerBaseTestClass) serializer.createContextual(serializers, property);

        assertEquals(Boolean.FALSE, jsonSerializer._useTimestamp);
        assertEquals(null, jsonSerializer._reusedCustomFormat);
        assertEquals("yyyy-MM-dd", ((SimpleDateFormat)jsonSerializer._customFormat).toPattern());
    }

    @Test
    public void testCreateContextualWithDateFormatInstance() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        DateFormat dateFormat = mock(DateFormat.class);

        when(property.getType()).thenReturn(mock(Type.class));
        when(serializer.findFormatOverrides(eq(serializers), eq(property), eq(serializer.handledType())))
                .thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(false);
        when(serializers.getConfig()).thenReturn(mock(SerializationConfig.class));
        when(serializers.getConfig().getDateFormat()).thenReturn(dateFormat);

        DateTimeSerializerBaseTestClass jsonSerializer =
                (DateTimeSerializerBaseTestClass) serializer.createContextual(serializers, property);

        assertEquals(Boolean.FALSE, jsonSerializer._useTimestamp);
        assertEquals(null, jsonSerializer._customFormat);
    }

    private static class DateTimeSerializerBaseTestClass extends DateTimeSerializerBase<Date> {

        public DateTimeSerializerBaseTestClass() {
            super(Date.class, null, null);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new DateTimeSerializerBaseTestClass();
        }

        @Override
        protected long _timestamp(Date value) {
            return 0;
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        }
    }
}