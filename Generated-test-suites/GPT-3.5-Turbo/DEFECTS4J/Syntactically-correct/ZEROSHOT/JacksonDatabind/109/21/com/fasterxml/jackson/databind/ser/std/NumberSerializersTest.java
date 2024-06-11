package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonSerializer<?> serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        
        gen = new JsonGenerator();
        provider = new SerializerProvider();
        
        serializer.serialize(123, gen, provider);
        
        Assert.assertEquals("123", gen.getResult());
    }
    
    @Test
    public void testShortSerializer() throws IOException {
        JsonSerializer<?> serializer = new NumberSerializers.ShortSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        
        gen = new JsonGenerator();
        provider = new SerializerProvider();
        
        serializer.serialize((short) 456, gen, provider);
        
        Assert.assertEquals("456", gen.getResult());
    }
    
    @Test
    public void testIntLikeSerializer() throws IOException {
        JsonSerializer<?> serializer = new NumberSerializers.IntLikeSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        
        gen = new JsonGenerator();
        provider = new SerializerProvider();
        
        serializer.serialize(789, gen, provider);
        
        Assert.assertEquals("789", gen.getResult());
    }
    
    @Test
    public void testLongSerializer() throws IOException {
        JsonSerializer<?> serializer = new NumberSerializers.LongSerializer(Long.TYPE);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        
        gen = new JsonGenerator();
        provider = new SerializerProvider();
        
        serializer.serialize(10000000000L, gen, provider);
        
        Assert.assertEquals("10000000000", gen.getResult());
    }
    
    @Test
    public void testFloatSerializer() throws IOException {
        JsonSerializer<?> serializer = new NumberSerializers.FloatSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        
        gen = new JsonGenerator();
        provider = new SerializerProvider();
        
        serializer.serialize(1.23f, gen, provider);
        
        Assert.assertEquals("1.23", gen.getResult());
    }
    
    @Test
    public void testDoubleSerializer() throws IOException {
        JsonSerializer<?> serializer = new NumberSerializers.DoubleSerializer(Double.TYPE);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        
        gen = new JsonGenerator();
        provider = new SerializerProvider();
        
        serializer.serialize(3.141592653589793, gen, provider);
        
        Assert.assertEquals("3.141592653589793", gen.getResult());
    }
    
    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        
        Assert.assertEquals(8, allDeserializers.size());
    }
}