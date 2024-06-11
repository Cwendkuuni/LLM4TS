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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.None;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Object> dateTimeSerializer;

    @Mock
    private SerializerProvider serializers;
    
    @Mock
    private BeanProperty beanProperty;
    
    @Mock
    private JsonFormat.Value format;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        dateTimeSerializer = new DateTimeSerializerBase<Object>(Object.class, true, null) {
            
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // dummy implementation
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, JavaType typeHint) {
                return null;
            }
            
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            
            @Override
            public JsonSerializer<?> createContextual(SerializerProvider serializers,
                    BeanProperty property) throws JsonMappingException {
                return null;
            }
            
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // dummy implementation
            }
        };
    }

    @Test
    public void testCreateContextual_withFormatNumeric() throws JsonMappingException {
        when(serializers.getAnnotationIntrospector()).thenReturn(new AnnotationIntrospector());
        when(property.getMember()).thenReturn(null);
        when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);

        dateTimeSerializer.createContextual(serializers, property);
        
        verify(serializers).getAnnotationIntrospector();
        verify(property).getMember();
    }
    
    @Test
    public void testCreateContextual_withFormatString() throws JsonMappingException {
        when(serializers.getAnnotationIntrospector()).thenReturn(new AnnotationIntrospector());
        when(property.getMember()).thenReturn(null);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(true);
        when(format.getPattern()).thenReturn("yyyy-MM-dd");
        when(format.hasLocale()).thenReturn(true);
        when(format.getLocale()).thenReturn(Locale.US);
        when(serializers.getLocale()).thenReturn(Locale.CANADA);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getDefault());

        dateTimeSerializer.createContextual(serializers, property);
        
        verify(serializers).getAnnotationIntrospector();
        verify(property).getMember();
        verify(property, times(2)).getMember();
        verify(serializers).getLocale();
        verify(serializers).getTimeZone();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextual_withNullProperty() throws JsonMappingException {
        when(serializers.getAnnotationIntrospector()).thenReturn(new AnnotationIntrospector());

        dateTimeSerializer.createContextual(serializers, null);
        
        verify(serializers).getAnnotationIntrospector();
    }

    @Test
    public void testIsEmpty_withUsingTimestamp() {
        assertEquals(false, dateTimeSerializer.isEmpty(serializers, new Object()));
    }

    @Test
    public void testIsEmpty_withoutUsingTimestamp() {
        dateTimeSerializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {
            
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // dummy implementation
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, JavaType typeHint) {
                return null;
            }
            
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            
            @Override
            public JsonSerializer<?> createContextual(SerializerProvider serializers,
                    BeanProperty property) throws JsonMappingException {
                return null;
            }
            
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // dummy implementation
            }
        };
        
        assertEquals(true, dateTimeSerializer.isEmpty(serializers, new Object()));
    }

    @Test
    public void testGetSchema_withTimestamp() {
        JsonNode jsonNode = dateTimeSerializer.getSchema(serializers, null);
        
        assertEquals("number", jsonNode.asText());
    }
    
    @Test
    public void testGetSchema_withNonTimestamp() {
        dateTimeSerializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {
            
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // dummy implementation
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, JavaType typeHint) {
                return null;
            }
            
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            
            @Override
            public JsonSerializer<?> createContextual(SerializerProvider serializers,
                    BeanProperty property) throws JsonMappingException {
                return null;
            }
            
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // dummy implementation
            }
        };
        
        JsonNode jsonNode = dateTimeSerializer.getSchema(serializers, null);
        
        assertEquals("string", jsonNode.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        dateTimeSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).getProvider();
        verify(visitor).getProvider();
    }
    
    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        
        dateTimeSerializer.serialize(new Object(), gen, serializers);
        
        verify(gen).writeObject(any());
    }
    
    private static class AnnotationIntrospector extends com.fasterxml.jackson.databind.AnnotationIntrospector {

		@Override
		public JsonFormat.Value findFormat(Annotated annotated) {
			return format;
		}
    	
    }
}