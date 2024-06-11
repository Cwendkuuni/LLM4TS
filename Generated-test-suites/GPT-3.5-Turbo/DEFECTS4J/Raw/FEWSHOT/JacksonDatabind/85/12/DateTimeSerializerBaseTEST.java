package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest extends StdScalarSerializerTest {

    private DateTimeSerializerBase<String> serializer;

    @Mock
    private SerializerProvider serializers;

    @Mock
    private BeanProperty property;

    @Mock
    private JsonFormat.Value format;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        serializer = new DateTimeSerializerBase<String>(String.class, true, null) {

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // do nothing
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
        };
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        when(property.getMember()).thenReturn(Object.class);
        when(serializers.findFormatOverrides(property, handledType())).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(true);
        when(format.hasLocale()).thenReturn(true);
        when(format.hasTimeZone()).thenReturn(true);
        when(format.getPattern()).thenReturn("yyyy-MM-dd");
        when(format.getLocale()).thenReturn(Locale.US);
        when(format.getTimeZone()).thenReturn(TimeZone.getTimeZone("GMT"));
        DateTimeSerializerBase<String> result = serializer.createContextual(serializers, property);
        assertNotNull(result);
        assertFalse(result._asTimestamp(serializers));
        assertTrue(result._customFormat instanceof SimpleDateFormat);
        assertEquals("yyyy-MM-dd", ((SimpleDateFormat) result._customFormat).toPattern());
        assertEquals(Locale.US, ((SimpleDateFormat) result._customFormat).getDateFormatSymbols().getLocale());
        assertEquals(TimeZone.getTimeZone("GMT"), ((SimpleDateFormat) result._customFormat).getTimeZone());
    }

    @Test
    public void testCreateContextualWithoutPattern() throws JsonMappingException {
        when(property.getMember()).thenReturn(Object.class);
        when(serializers.findFormatOverrides(property, handledType())).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(false);
        when(format.hasLocale()).thenReturn(true);
        when(format.hasTimeZone()).thenReturn(true);
        when(format.getPattern()).thenReturn(null);
        when(format.getLocale()).thenReturn(Locale.US);
        when(format.getTimeZone()).thenReturn(TimeZone.getTimeZone("GMT"));
        DateTimeSerializerBase<String> result = serializer.createContextual(serializers, property);
        assertNotNull(result);
        assertFalse(result._asTimestamp(serializers));
        assertTrue(result._customFormat instanceof SimpleDateFormat);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_ISO8601, ((SimpleDateFormat) result._customFormat).toPattern());
        assertEquals(Locale.US, ((SimpleDateFormat) result._customFormat).getDateFormatSymbols().getLocale());
        assertEquals(TimeZone.getTimeZone("GMT"), ((SimpleDateFormat) result._customFormat).getTimeZone());
    }

    @Test
    public void testCreateContextualWithNumericShape() throws JsonMappingException {
        when(property.getMember()).thenReturn(Object.class);
        when(serializers.findFormatOverrides(property, handledType())).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);
        DateTimeSerializerBase<String> result = serializer.createContextual(serializers, property);
        assertNotNull(result);
        assertTrue(result._asTimestamp(serializers));
        assertNull(result._customFormat);
    }

    @Test
    public void testCreateContextualNoProperty() throws JsonMappingException {
        DateTimeSerializerBase<String> result = serializer.createContextual(serializers, null);
        assertNotNull(result);
        assertTrue(result instanceof DateTimeSerializerBase);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualNullSerializerProvider() throws JsonMappingException {
        serializer.createContextual(null, property);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualHandledTypeNullSerializerProvider() throws JsonMappingException {
        serializer.createContextual(null, null);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(serializer.isEmpty(null, null));
        assertFalse(serializer.isEmpty(null, ""));
    }

    @Test
    public void testGetSchema() {
        assertNull(serializer.getSchema(null, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor, times(1)).expectStringFormat(eq(typeHint));
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        serializer.serialize(null, gen, null);
        verify(gen, times(0)).writeString(anyString());
    }

}