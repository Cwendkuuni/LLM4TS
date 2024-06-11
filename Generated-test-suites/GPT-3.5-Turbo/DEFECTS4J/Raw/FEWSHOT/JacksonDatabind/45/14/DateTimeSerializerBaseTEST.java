
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {
    
    private static class DummyDateTimeSerializerBase extends DateTimeSerializerBase<String> {
        
        public DummyDateTimeSerializerBase(Class<String> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new DummyDateTimeSerializerBase(String.class, timestamp, customFormat);
        }

        @Override
        protected long _timestamp(String value) {
            // TODO Auto-generated method stub
            return 0;
        }
        
        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // TODO Auto-generated method stub
            
        }

    }
    
    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<String> serializer = new DummyDateTimeSerializerBase(String.class, true, null);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        
        serializer.serialize("value", gen, serializers);
        Mockito.verify(gen, Mockito.times(1)).writeString("value");
        
        serializer = new DummyDateTimeSerializerBase(String.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        gen = Mockito.mock(JsonGenerator.class);
        serializers = Mockito.mock(SerializerProvider.class);
        
        serializer.serialize("value", gen, serializers);
        Mockito.verify(gen, Mockito.times(1)).writeString("value");
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DummyDateTimeSerializerBase(String.class, true, null);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        Mockito.when(property.getMember()).thenReturn(DummyAnnotated.class);
        Mockito.when(serializers.getAnnotationIntrospector().findFormat((Annotated)property.getMember())).thenReturn(new JsonFormat.Value());
        
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        
        assertEquals(Boolean.TRUE, ((DummyDateTimeSerializerBase)result).get_useTimestamp());
        assertNull(((DummyDateTimeSerializerBase)result).get_customFormat());
        
        serializers = Mockito.mock(SerializerProvider.class);
        property = Mockito.mock(BeanProperty.class);
        Mockito.when(property.getMember()).thenReturn(DummyAnnotated.class);
        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat = jsonFormat.withShape(JsonFormat.Shape.STRING);
        jsonFormat = jsonFormat.withPattern("yyyy-MM-dd");
        jsonFormat = jsonFormat.withLocale(Locale.US);
        jsonFormat = jsonFormat.withTimeZone(TimeZone.getTimeZone("GMT"));
        Mockito.when(serializers.getAnnotationIntrospector().findFormat((Annotated)property.getMember())).thenReturn(jsonFormat);
        
        result = serializer.createContextual(serializers, property);
        
        assertEquals(Boolean.FALSE, ((DummyDateTimeSerializerBase)result).get_useTimestamp());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd", Locale.US).getTimeZone(), ((DummyDateTimeSerializerBase)result).get_customFormat().getTimeZone());
    }
    
    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<String> serializer = new DummyDateTimeSerializerBase(String.class, true, null);
        assertNull(serializer.isEmpty("value"));
        
        serializer = new DummyDateTimeSerializerBase(String.class, false, null);
        assertNull(serializer.isEmpty("value"));
        
        serializer = new DummyDateTimeSerializerBase(String.class, null, null);
        assertNull(serializer.isEmpty("value"));
    }
    
    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<String> serializer = new DummyDateTimeSerializerBase(String.class, true, null);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);
        
        JsonNode result = serializer.getSchema(serializers, typeHint);
        
        assertEquals("number", result.get("type").textValue());
        
        serializer = new DummyDateTimeSerializerBase(String.class, false, null);
        serializers = Mockito.mock(SerializerProvider.class);
        typeHint = Mockito.mock(Type.class);
        
        result = serializer.getSchema(serializers, typeHint);
        
        assertEquals("string", result.get("type").textValue());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DummyDateTimeSerializerBase(String.class, true, null);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        Mockito.verify(visitor, Mockito.times(1)).expectInteger();
        
        serializer = new DummyDateTimeSerializerBase(String.class, false, null);
        visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        typeHint = Mockito.mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        Mockito.verify(visitor, Mockito.times(1)).expectString();
    }
        
    private static class DummyAnnotated {}
}

