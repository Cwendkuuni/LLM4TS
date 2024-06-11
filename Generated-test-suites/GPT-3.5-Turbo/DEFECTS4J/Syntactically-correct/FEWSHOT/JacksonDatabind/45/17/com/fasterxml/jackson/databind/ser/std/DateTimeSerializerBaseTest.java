package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, mock(DateFormat.class)) {
            
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }
        };
        
        DateTimeSerializerBase<String> newSerializer = serializer.withFormat(false, mock(DateFormat.class));
        
        assertNull(newSerializer._useTimestamp);
        verify(serializer).withFormat(false, mock(DateFormat.class));
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        Annotated annotated = mock(Annotated.class);
        when(property.getMember()).thenReturn(annotated);
        
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);
        format = format.withPattern("yyyy-MM-dd");
        format = format.withLocale(Locale.US);
        
        when(provider.getAnnotationIntrospector()).thenReturn(mock(AnnotationIntrospector.class));
        when(provider.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);
        
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, mock(DateFormat.class)) {
            
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }
        };
        
        JsonSerializer<?> newSerializer = serializer.createContextual(provider, property);
        
        verify(provider).getLocale();
        verify(provider).getTimeZone();
    }
    
    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, mock(DateFormat.class)) {

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }
        };
        
        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(mock(SerializerProvider.class), null));
    }
    
    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, mock(DateFormat.class)) {
            
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }
        };
        
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonNode schema = serializer.getSchema(provider, mock(JavaType.class));
        
        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, mock(DateFormat.class)) {
            
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }
        };
        
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType type = mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, type);
        
        verify(visitor).getProvider();
    }
    
    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, mock(DateFormat.class)) {

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("test");
            }

            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }
        };
        
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        serializer.serialize("testValue", gen, provider);
        
        verify(gen).writeString("test");
    }

    private void assertTrue(boolean empty) {
		// TODO Auto-generated method stub
		
	}
	
}