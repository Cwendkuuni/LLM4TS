package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private BeanProperty beanProperty;

    @Mock
    private JsonFormat.Value format;

    private DateTimeSerializerBase serializer;
    private SimpleDateFormat simpleDateFormat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        serializer = new DateTimeSerializerBaseImpl();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    }

    @Test
    public void testCreateSchemaNode_returnsSchemaNodeAsNumber() {
        JsonNode actualNode = serializer.getSchema(serializerProvider, null);
        assertEquals("number", actualNode.get("type").asText());
    }

    @Test
    public void testCreateSchemaNode_returnsSchemaNodeAsString() {
        serializer = new DateTimeSerializerBaseImpl("yyyy-MM-dd'T'HH:mm:ssZ", Boolean.FALSE);
        JsonNode actualNode = serializer.getSchema(serializerProvider, null);
        assertEquals("string", actualNode.get("type").asText());
    }

    @Test
    public void testContextualSerializer_returnsThisWhenPropertyIsNull() throws JsonMappingException {
        assertEquals(serializer, serializer.createContextual(serializerProvider, null));
    }

    @Test
    public void testContextualSerializer_returnsThisWhenFormatIsNull() throws JsonMappingException {
        when(beanProperty.findFormatOverrides(any(SerializationConfig.class), any(BeanProperty.class), any(Class.class)))
            .thenReturn(null);
        assertEquals(serializer, serializer.createContextual(serializerProvider, beanProperty));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContextualSerializer_throwsIllegalArgumentExceptionWhenSerializerProviderIsNull()
            throws JsonMappingException {
        serializer.createContextual(null, beanProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContextualSerializer_throwsIllegalArgumentExceptionWhenHandledTypeIsNull()
            throws JsonMappingException {
        serializer.createContextual(serializerProvider, beanProperty);
    }

    @Test
    public void testContextualSerializer_serializesAsNumericTimestamp() throws JsonMappingException {
        when(beanProperty.findFormatOverrides(any(SerializationConfig.class), any(BeanProperty.class), any(Class.class)))
            .thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);

        serializer.createContextual(serializerProvider, beanProperty);

        assertTrue(serializer._asTimestamp(serializerProvider));
    }

    @Test
    public void testContextualSerializer_serializesAsStringWithCustomFormat()
            throws JsonMappingException {
        when(beanProperty.findFormatOverrides(any(SerializationConfig.class), any(BeanProperty.class), any(Class.class)))
            .thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(true);
        when(format.hasLocale()).thenReturn(true);
        when(format.getPattern()).thenReturn("yyyy-MM-dd'T'HH:mm:ssZ");
        when(format.getLocale()).thenReturn(Locale.US);
        when(format.hasTimeZone()).thenReturn(true);
        when(format.getTimeZone()).thenReturn(TimeZone.getTimeZone("GMT"));

        serializer.createContextual(serializerProvider, beanProperty);

        assertFalse(serializer._asTimestamp(serializerProvider));
        assertEquals(simpleDateFormat.toPattern(), ((SimpleDateFormat) serializer._customFormat).toPattern());
        assertEquals(simpleDateFormat.getTimeZone(), ((SimpleDateFormat) serializer._customFormat).getTimeZone());
    }

    @Test
    public void testContextualSerializer_serializesAsStringWithoutCustomFormat()
            throws JsonMappingException {
        when(beanProperty.findFormatOverrides(any(SerializationConfig.class), any(BeanProperty.class), any(Class.class)))
            .thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(false);
        when(format.hasLocale()).thenReturn(false);
        when(format.hasTimeZone()).thenReturn(false);

        serializer.createContextual(serializerProvider, beanProperty);

        assertFalse(serializer._asTimestamp(serializerProvider));
        assertNull(serializer._customFormat);
    }

    private class DateTimeSerializerBaseImpl extends DateTimeSerializerBase<Date> {

        public DateTimeSerializerBaseImpl() {
            super(Date.class, Boolean.FALSE, null);
        }

        public DateTimeSerializerBaseImpl(String pattern, Boolean useTimestamp) {
            super(Date.class, useTimestamp, new SimpleDateFormat(pattern));
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Date value) {
            return 0;
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        }

        @Override
        protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider)
                throws IOException {
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            _acceptJsonFormatVisitor(visitor, typeHint, _asTimestamp(visitor.getProvider()));
        }

    }

}