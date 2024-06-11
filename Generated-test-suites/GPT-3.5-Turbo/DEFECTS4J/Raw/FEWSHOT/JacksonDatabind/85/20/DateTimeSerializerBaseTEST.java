
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider mockSerializerProvider;
    @Mock
    private JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper;
    @Mock
    private JavaType mockTypeHint;
    @Mock
    private SerializerProvider mockProvider;

    private DateTimeSerializerBase<Object> dateTimeSerializerBase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")) {
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
                return;
            }
        };
    }

    @Test
    public void testWithFormat() {
        assertNull(dateTimeSerializerBase.withFormat(true, null));
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        assertNull(dateTimeSerializerBase.createContextual(mockSerializerProvider, null));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(dateTimeSerializerBase.isEmpty((Object) null));
        assertTrue(dateTimeSerializerBase.isEmpty(mockSerializerProvider, null));
    }

    @Test
    public void testGetSchema() {
        assertNotNull(dateTimeSerializerBase.getSchema(mockSerializerProvider, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        dateTimeSerializerBase.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, null);
        verify(mockJsonFormatVisitorWrapper, times(1)).setProvider(mockProvider);
    }

    @Test
    public void testSerialize() throws IOException {
        dateTimeSerializerBase.serialize(null, null, null);
    }
}
