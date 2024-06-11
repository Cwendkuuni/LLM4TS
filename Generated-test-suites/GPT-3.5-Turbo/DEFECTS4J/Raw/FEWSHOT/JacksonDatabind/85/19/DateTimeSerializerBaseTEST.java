package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider mockProvider;

    @Mock
    private BeanProperty mockProperty;

    private DateTimeSerializerBase<Integer> serializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        serializer = new DateTimeSerializerBase<Integer>(Integer.class, true, null) {
            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }

            @Override
            protected long _timestamp(Integer value) {
                return 0;
            }
        };
        
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Integer> newSerializer = serializer.withFormat(true, null);
        assertTrue(newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        Mockito.when(mockProvider.getLocale()).thenReturn(null);
        Mockito.when(mockProvider.getTimeZone()).thenReturn(null);
        Mockito.when(mockProperty.findFormatOverrides(Mockito.any())).thenReturn(null);

        serializer.createContextual(mockProvider, null);
        assertTrue(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testCreateContextualWithShapeString() throws JsonMappingException {
        JsonFormat.Value mockFormat = Mockito.mock(JsonFormat.Value.class);
        Mockito.when(mockFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);
        Mockito.when(mockFormat.hasPattern()).thenReturn(false);
        Mockito.when(mockFormat.hasLocale()).thenReturn(false);
        Mockito.when(mockFormat.hasTimeZone()).thenReturn(false);
        Mockito.when(mockProperty.findFormatOverrides(Mockito.any())).thenReturn(mockFormat);
        Mockito.when(mockProvider.getLocale()).thenReturn(null);
        Mockito.when(mockProvider.getTimeZone()).thenReturn(null);

        serializer.createContextual(mockProvider, mockProperty);
        assertFalse(serializer._useTimestamp);
        assertTrue(serializer._customFormat instanceof DateFormat);
    }

    @Test
    public void testCreateContextualWithShapeNumeric() throws JsonMappingException {
        JsonFormat.Value mockFormat = Mockito.mock(JsonFormat.Value.class);
        Mockito.when(mockFormat.getShape()).thenReturn(JsonFormat.Shape.NUMBER);
        Mockito.when(mockProperty.findFormatOverrides(Mockito.any())).thenReturn(mockFormat);
        Mockito.when(mockProvider.getLocale()).thenReturn(null);
        Mockito.when(mockProvider.getTimeZone()).thenReturn(null);

        serializer.createContextual(mockProvider, mockProperty);
        assertTrue(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testIsEmptyWithValue() {
        assertFalse(serializer.isEmpty(mockProvider, 1));
    }

    @Test
    public void testIsEmptyWithNull() {
        assertTrue(serializer.isEmpty(mockProvider, null));
    }

    @Test
    public void testIsEmptyWithZeroTimestamp() {
        DateTimeSerializerBase<Long> serializerWithNullCheck = new DateTimeSerializerBase<Long>(Long.class, null, null) {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }

            @Override
            protected long _timestamp(Long value) {
                return 0;
            }
        };
        
        assertTrue(serializerWithNullCheck.isEmpty(mockProvider, 0L));
    }

    @Test
    public void testGetSchemaAsTimestamp() {
        JsonNode schema = serializer.getSchema(mockProvider, null);
        assertTrue(schema.get("type").asText().equals("number"));
    }

    @Test
    public void testGetSchemaAsString() {
        DateTimeSerializerBase<Long> serializerWithNullCheck = new DateTimeSerializerBase<Long>(Long.class, null, null) {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }

            @Override
            protected long _timestamp(Long value) {
                return 0;
            }
        };

        JsonNode schema = serializerWithNullCheck.getSchema(mockProvider, null);
        assertTrue(schema.get("type").asText().equals("string"));
    }

    @Test
    public void testAcceptJsonFormatVisitorWithTimestamp() throws JsonMappingException {
        JsonFormatVisitorWrapper.MockBase mockVisitor = new JsonFormatVisitorWrapper.MockBase();
        serializer.acceptJsonFormatVisitor(mockVisitor, null);
        assertEquals(JsonParser.NumberType.LONG, mockVisitor.mockNumberType);
        assertEquals(JsonValueFormat.UTC_MILLISEC, mockVisitor.mockValueFormat);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithString() throws JsonMappingException {
        DateTimeSerializerBase<Long> serializerWithNullCheck = new DateTimeSerializerBase<Long>(Long.class, null, null) {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }

            @Override
            protected long _timestamp(Long value) {
                return 0;
            }
        };

        JsonFormatVisitorWrapper.MockBase mockVisitor = new JsonFormatVisitorWrapper.MockBase();
        serializerWithNullCheck.acceptJsonFormatVisitor(mockVisitor, null);
        assertEquals(JsonValueFormat.DATE_TIME, mockVisitor.mockValueFormat);
    }
}