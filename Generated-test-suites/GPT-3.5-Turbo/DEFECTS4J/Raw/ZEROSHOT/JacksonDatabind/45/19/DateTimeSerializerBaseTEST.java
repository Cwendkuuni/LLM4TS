package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase serializer;

    @Before
    public void setUp() {
        serializer = mock(DateTimeSerializerBase.class);
    }

    @Test
    public void testWithFormat() {
        Boolean timestamp = true;
        DateFormat customFormat = new SimpleDateFormat("yyyy/MM/dd");
        when(serializer.withFormat(timestamp, customFormat)).thenReturn(serializer);
        
        DateTimeSerializerBase result = serializer.withFormat(timestamp, customFormat);
        
        assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(property.getMember()).thenReturn(mock(Annotated.class));
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.getTimeZone()).thenReturn(TimeZone.getDefault());
        when(format.getPattern()).thenReturn("yyyy/MM/dd");
        when(format.getLocale()).thenReturn(Locale.getDefault());
        when(serializer.createContextual(serializers, property)).thenReturn(serializer);
        when(serializers.getAnnotationIntrospector().findFormat((Annotated)property.getMember())).thenReturn(format);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getDefault());
        when(serializers.getLocale()).thenReturn(Locale.getDefault());
        
        DateTimeSerializerBase result = (DateTimeSerializerBase)serializer.createContextual(serializers, property);
        
        assertEquals(serializer, result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateContextualNullProperty() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = null;
        
        serializer.createContextual(serializers, property);
    }

    @Test
    public void testIsEmptyWithValueNull() {
        Object value = null;
        when(serializer.isEmpty(value)).thenCallRealMethod();
        
        boolean result = serializer.isEmpty(value);
        
        assertEquals(true, result);
    }

    @Test
    public void testIsEmptyWithValueNotNull() {
        Object value = new Object();
        when(serializer.isEmpty(value)).thenCallRealMethod();
        
        boolean result = serializer.isEmpty(value);
        
        assertEquals(false, result);
    }

    @Test
    public void testIsEmptyWithProviderAndValueNull() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        Object value = null;
        when(serializer.isEmpty(serializers, value)).thenCallRealMethod();
        
        boolean result = serializer.isEmpty(serializers, value);
        
        assertEquals(true, result);
    }

    @Test
    public void testIsEmptyWithProviderAndValueNotNull() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        Object value = new Object();
        when(serializer.isEmpty(serializers, value)).thenCallRealMethod();
        
        boolean result = serializer.isEmpty(serializers, value);
        
        assertEquals(false, result);
    }

    @Test
    public void testGetSchemaWithTimestampShape() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        when(serializer._asTimestamp(serializers)).thenReturn(true);
        JsonNode schemaNode = mock(JsonNode.class);
        when(serializer.createSchemaNode("number", true)).thenReturn(schemaNode);
        when(serializer.getSchema(serializers, typeHint)).thenCallRealMethod();
        
        JsonNode result = serializer.getSchema(serializers, typeHint);
        
        assertEquals(schemaNode, result);
    }

    @Test
    public void testGetSchemaWithStringShape() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        when(serializer._asTimestamp(serializers)).thenReturn(false);
        JsonNode schemaNode = mock(JsonNode.class);
        when(serializer.createSchemaNode("string", true)).thenReturn(schemaNode);
        when(serializer.getSchema(serializers, typeHint)).thenCallRealMethod();
        
        JsonNode result = serializer.getSchema(serializers, typeHint);
        
        assertEquals(schemaNode, result);
    }

    @Test
    public void testAcceptJsonFormatVisitorAsNumber() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(serializer._asTimestamp(visitor.getProvider())).thenReturn(true);
        when(serializer.acceptJsonFormatVisitor(visitor, typeHint)).thenCallRealMethod();
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(serializer, times(1)).visitIntFormat(visitor, typeHint,
                JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
    }

    @Test
    public void testAcceptJsonFormatVisitorAsString() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(serializer._asTimestamp(visitor.getProvider())).thenReturn(false);
        when(serializer.acceptJsonFormatVisitor(visitor, typeHint)).thenCallRealMethod();
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(serializer, times(1)).visitStringFormat(visitor, typeHint, JsonValueFormat.DATE_TIME);
    }

    @Test
    public void testSerialize() throws IOException {
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        doNothing().when(serializer).serialize(value, gen, serializers);
        
        serializer.serialize(value, gen, serializers);
        
        verify(serializer, times(1)).serialize(value, gen, serializers);
    }


}