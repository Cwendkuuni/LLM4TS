package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DateTimeSerializerBaseTest {
    private DateTimeSerializerBase<DateTime> serializer;
    private SerializerProvider providers;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        serializer = Mockito.mock(DateTimeSerializerBase.class, Mockito.CALLS_REAL_METHODS);
        providers = Mockito.mock(SerializerProvider.class);
        Mockito.when(serializer.handledType()).thenReturn(DateTime.class);
        Mockito.when(providers.getConfig()).thenReturn(mock(SerializationConfig.class));
    }

    @Test
    public void testWithFormat() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateTimeSerializerBase<DateTime> result = serializer.withFormat(Boolean.TRUE, format);
        assertEquals(Boolean.TRUE, result._useTimestamp);
        assertEquals(format, result._customFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.withPattern("yyyy-MM-dd");
        Mockito.when(serializer.findFormatOverrides(any(SerializerProvider.class), any(BeanProperty.class), any(Class.class)))
                .thenReturn(format);

        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonSerializer<?> result = serializer.createContextual(serializers, mock(BeanProperty.class));

        assertEquals(serializer, result);
        Mockito.verify(serializer).withFormat(Boolean.FALSE, Mockito.any(SimpleDateFormat.class));
    }
}