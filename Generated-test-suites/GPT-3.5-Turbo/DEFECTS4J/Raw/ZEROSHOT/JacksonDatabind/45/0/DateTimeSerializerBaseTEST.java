package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> serializer;
    private Class<Date> type;
    private Boolean useTimestamp;
    private DateFormat customFormat;

    @Before
    public void setup() {
        type = Date.class;
        useTimestamp = false;
        customFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        serializer = new DateTimeSerializerBase<TestBean>(type, useTimestamp, customFormat) {

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // Do Nothing
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // Do Nothing
            }

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            	return null;
            }
        };
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Date> result = serializer.withFormat(useTimestamp, customFormat);
        assertEquals(null, result);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(property.getMember()).thenReturn(null);
        
        DateTimeSerializerBase<Date> result = (DateTimeSerializerBase<Date>) serializer.createContextual(serializers, property);
        assertEquals(null, result);
        
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        
        when(serializers.getAnnotationIntrospector().findFormat(any(Annotated.class))).thenReturn(format);
        when(serializers.getLocale()).thenReturn(Locale.getDefault());
        when(serializers.getTimeZone()).thenReturn(TimeZone.getDefault());
        
        result = (DateTimeSerializerBase<Date>) serializer.createContextual(serializers, property);
        assertEquals(false, result._asTimestamp(serializers));

        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        result = (DateTimeSerializerBase<Date>) serializer.createContextual(serializers, property);
        assertEquals(true, result._asTimestamp(serializers));

        format.setShape(JsonFormat.Shape.NUMBER);
        result = (DateTimeSerializerBase<Date>) serializer.createContextual(serializers, property);
        assertEquals(true, result._asTimestamp(serializers));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullSerializerProvider() throws JsonMappingException {
        serializer.createContextual(null, null);
    }

    @Test
    public void testIsEmptyWithNullValue() {
        boolean result = serializer.isEmpty(null);
        assertEquals(true, result);
    }

    @Test
    public void testIsEmptyWithValue() {
        boolean result = serializer.isEmpty(mock(SerializerProvider.class), new Date());
        assertEquals(false, result);
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = Date.class;
        
        JsonNode result = serializer.getSchema(serializers, typeHint);
        assertEquals(null, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).visitStringFormat(eq(typeHint), eq(JsonValueFormat.DATE_TIME));
    }
}