package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
    
    private DateTimeSerializerBase<String> serializer;

    @Before
    public void setUp() {
        serializer = createDateTimeSerializerBase();
    }
    
    private DateTimeSerializerBase<String> createDateTimeSerializerBase() {
        return new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null) {

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };
    }

    @Test
    public void testCreateContextual_NullProperty_ReturnsSameSerializer() throws JsonMappingException {
        BeanProperty property = null;
        ContextualSerializer actual = serializer.createContextual(new SerializerProvider(null, null, null, null, null), property);
        assertEquals(serializer, actual);
    }

    @Test
    public void testCreateContextual_NoFormat_ReturnsSameSerializer() throws JsonMappingException {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        ContextualSerializer actual = serializer.createContextual(new SerializerProvider(null, null, null, null, null), property);
        assertEquals(serializer, actual);
    }

    @Test
    public void testCreateContextual_NumericShape_ReturnsNewSerializerWithTimestampFormat() throws JsonMappingException {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.NUMBER);
        Mockito.when(property.findFormatOverrides(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(format);
        ContextualSerializer actual = serializer.createContextual(new SerializerProvider(null, null, null, null, null), property);
        assertTrue(actual instanceof DateTimeSerializerBase);
        assertTrue(((DateTimeSerializerBase<?>) actual)._useTimestamp);
        assertNull(((DateTimeSerializerBase<?>) actual)._customFormat);
    }

    @Test
    public void testCreateContextual_StringShape_ReturnsNewSerializerWithStringFormat() throws JsonMappingException {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);
        Mockito.when(property.findFormatOverrides(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(format);
        ContextualSerializer actual = serializer.createContextual(new SerializerProvider(null, null, null, null, null), property);
        assertTrue(actual instanceof DateTimeSerializerBase);
        assertFalse(((DateTimeSerializerBase<?>) actual)._useTimestamp);
        assertNotNull(((DateTimeSerializerBase<?>) actual)._customFormat);
    }

    @Test
    public void testCreateContextual_CustomShape_ReturnsSameSerializer() throws JsonMappingException {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonFormat.Value format = new JsonFormat.Value();
        Mockito.when(property.findFormatOverrides(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(format);
        ContextualSerializer actual = serializer.createContextual(new SerializerProvider(null, null, null, null, null), property);
        assertEquals(serializer, actual);
    }

    @Test
    public void testIsEmpty_ValueIsNull_ReturnsTrue() {
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmpty_ValueIsNotNull_ReturnsFalse() {
        assertFalse(serializer.isEmpty(""));
    }

    @Test
    public void testGetSchema_AsTimestamp_ReturnsNumberSchema() {
        SerializerProvider serializers = new SerializerProvider(null, null, null, null, null);
        assertFalse(serializer._asTimestamp(serializers));
        JsonNode schema = serializer.getSchema(serializers, null);
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testGetSchema_AsString_ReturnsStringSchema() {
        SerializerProvider serializers = new SerializerProvider(null, null, null, null, null);
        serializer = createDateTimeSerializerBaseWithCustomFormat();
        assertTrue(serializer._asTimestamp(serializers));
        JsonNode schema = serializer.getSchema(serializers, null);
        assertEquals("string", schema.get("type").asText());
    }

    private DateTimeSerializerBase<String> createDateTimeSerializerBaseWithCustomFormat() {
        return new DateTimeSerializerBase<String>(String.class, null, new SimpleDateFormat()) {

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsNumber_CallsVisitIntFormat() throws JsonMappingException {
        JavaType typeHint = Mockito.mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(visitor).visitIntFormat(Mockito.eq(typeHint), Mockito.eq(JsonParser.NumberType.LONG), Mockito.eq(JsonValueFormat.UTC_MILLISEC));
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsString_CallsVisitStringFormat() throws JsonMappingException {
        JavaType typeHint = Mockito.mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        serializer = createDateTimeSerializerBaseWithCustomFormat();
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(visitor).visitStringFormat(Mockito.eq(typeHint), Mockito.eq(JsonValueFormat.DATE_TIME));
    }
    
    @Test
    public void testSerialize_DoesNotThrowException() throws IOException {
        serializer.serialize("", null, null);
    }
}