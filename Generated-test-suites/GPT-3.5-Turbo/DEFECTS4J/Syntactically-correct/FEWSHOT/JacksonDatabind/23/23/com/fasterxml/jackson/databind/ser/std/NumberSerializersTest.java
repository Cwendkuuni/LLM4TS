package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {
    
    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = mock(Map.class);
        
        NumberSerializers.addAll(allDeserializers);
        
        verify(allDeserializers, times(6)).put(anyString(), any(JsonSerializer.class));
    }
    
    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();
        Short value = 10;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize(value, gen, provider);
            verify(gen).writeNumber(value.shortValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer();
        Integer value = 10;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize((Object) value, gen, provider);
            verify(gen).writeNumber(value.intValue());
            
            serializer.serializeWithType((Object) value, gen, provider, null);
            verify(gen, times(2)).writeNumber(value.intValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new IntLikeSerializer();
        Number value = 10;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize(value, gen, provider);
            verify(gen).writeNumber(value.intValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer();
        Long value = 10L;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize((Object) value, gen, provider);
            verify(gen).writeNumber(value.longValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new FloatSerializer();
        Float value = 10.0f;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize((Object) value, gen, provider);
            verify(gen).writeNumber(value.floatValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer();
        Double value = 10.0;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            serializer.serialize((Object) value, gen, provider);
            verify(gen).writeNumber(value.doubleValue());
            
            serializer.serializeWithType((Object) value, gen, provider, null);
            verify(gen, times(2)).writeNumber(value.doubleValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}