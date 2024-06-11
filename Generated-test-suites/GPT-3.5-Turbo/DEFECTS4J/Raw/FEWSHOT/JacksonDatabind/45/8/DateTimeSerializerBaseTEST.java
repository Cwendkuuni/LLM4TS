package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {
    private DateTimeSerializerBase<Long> serializer;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBase<Long>(Long.class, true, null) {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }

            @Override
            protected long _timestamp(Long value) {
                return 0;
            }
        };
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Long> newSerializer = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd"));
        assertFalse(newSerializer._asTimestamp(null));
        assertEquals(new SimpleDateFormat("yyyy-MM-dd"), newSerializer._customFormat);
    }

    @Test
    public void testCreateContextual_numericShape() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        Annotated annotated = mock(Annotated.class);
        when(property.getMember()).thenReturn(annotated);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        when(serializers.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);

        JsonSerializer<?> newSerializer = serializer.createContextual(serializers, property);

        assertTrue(newSerializer instanceof DateTimeSerializerBase);
        assertTrue(((DateTimeSerializerBase<?>) newSerializer)._asTimestamp(null));
    }

    @Test
    public void testCreateContextual_stringShape_withoutPattern() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        Annotated annotated = mock(Annotated.class);
        when(property.getMember()).thenReturn(annotated);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        when(serializers.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getTimeZone("America/Los_Angeles"));
        when(serializers.getLocale()).thenReturn(Locale.US);

        JsonSerializer<?> newSerializer = serializer.createContextual(serializers, property);

        assertTrue(newSerializer instanceof DateTimeSerializerBase);
        assertFalse(((DateTimeSerializerBase<?>) newSerializer)._asTimestamp(null));

        DateTimeSerializerBase<?> dateTimeSerializer = (DateTimeSerializerBase<?>) newSerializer;
        SimpleDateFormat expectedDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        expectedDateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        assertEquals(expectedDateFormat, dateTimeSerializer._customFormat);
    }

    @Test
    public void testCreateContextual_stringShape_withPattern() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        Annotated annotated = mock(Annotated.class);
        when(property.getMember()).thenReturn(annotated);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy/MM/dd");
        when(serializers.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getTimeZone("America/Los_Angeles"));
        when(serializers.getLocale()).thenReturn(Locale.US);

        JsonSerializer<?> newSerializer = serializer.createContextual(serializers, property);

        assertTrue(newSerializer instanceof DateTimeSerializerBase);
        assertFalse(((DateTimeSerializerBase<?>) newSerializer)._asTimestamp(null));

        DateTimeSerializerBase<?> dateTimeSerializer = (DateTimeSerializerBase<?>) newSerializer;
        SimpleDateFormat expectedDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        expectedDateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        assertEquals(expectedDateFormat, dateTimeSerializer._customFormat);
    }

    @Test
    public void testIsEmpty_nullValue() {
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmpty_zeroTimestamp() {
        assertTrue(serializer.isEmpty(0L));
    }

    @Test
    public void testIsEmpty_nonZeroTimestamp() {
        assertFalse(serializer.isEmpty(123456789L));
    }

    @Test
    public void testGetSchema_asNumber() {
        SerializerProvider serializers = mock(SerializerProvider.class);

        ObjectNode expectedSchema = JsonNodeFactory.instance.objectNode();
        expectedSchema.put("type", "number");
        expectedSchema.put("format", "utc-millisec");

        assertEquals(expectedSchema, serializer.getSchema(serializers, null));
    }

    @Test
    public void testGetSchema_asString() {
        SerializerProvider serializers = mock(SerializerProvider.class);

        DateTimeSerializerBase<Long> newSerializer = new DateTimeSerializerBase<Long>(Long.class, false, new SimpleDateFormat("yyyy-MM-dd")) {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }

            @Override
            protected long _timestamp(Long value) {
                return 0;
            }
        };

        ObjectNode expectedSchema = JsonNodeFactory.instance.objectNode();
        expectedSchema.put("type", "string");
        expectedSchema.put("format", "date-time");

        assertEquals(expectedSchema, newSerializer.getSchema(serializers, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor_asNumber() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        ArgumentCaptor<JsonValueFormat> formatCaptor = ArgumentCaptor.forClass(JsonValueFormat.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectNumberFormat(typeHint, formatCaptor.capture());
        assertEquals(JsonValueFormat.UTC_MILLISEC, formatCaptor.getValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor_asString() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        ArgumentCaptor<JsonValueFormat> formatCaptor = ArgumentCaptor.forClass(JsonValueFormat.class);

        DateTimeSerializerBase<Long> newSerializer = new DateTimeSerializerBase<Long>(Long.class, false, null) {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }

            @Override
            protected long _timestamp(Long value) {
                return 0;
            }
        };

        newSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectStringFormat(typeHint, formatCaptor.capture());
        assertEquals(JsonValueFormat.DATE_TIME, formatCaptor.getValue());
    }
}