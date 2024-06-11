package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumberSerializersTest {
    
    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        
        NumberSerializers.addAll(allDeserializers);
        
        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.class.getName()).getClass());
        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.TYPE.getName()).getClass());
        assertEquals(LongSerializer.class, allDeserializers.get(Long.class.getName()).getClass());
        assertEquals(LongSerializer.class, allDeserializers.get(Long.TYPE.getName()).getClass());
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.class.getName()).getClass());
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.TYPE.getName()).getClass());
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.class.getName()).getClass());
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.TYPE.getName()).getClass());
        assertEquals(FloatSerializer.class, allDeserializers.get(Float.class.getName()).getClass());
        assertEquals(FloatSerializer.class, allDeserializers.get(Float.TYPE.getName()).getClass());
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.class.getName()).getClass());
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.TYPE.getName()).getClass());
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonGenerator gen = new JsonGenerator(null);
        SerializerProvider provider = new SerializerProvider();
        
        shortSerializer.serialize(Short.valueOf((short) 10), gen, provider);
        
        assertEquals("10", gen.toString());
        
    }
    
    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        JsonGenerator gen = new JsonGenerator(null);
        SerializerProvider provider = new SerializerProvider();
        
        integerSerializer.serialize(Integer.valueOf(100), gen, provider);
        
        assertEquals("100", gen.toString());
        
    }
    
    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        JsonGenerator gen = new JsonGenerator(null);
        SerializerProvider provider = new SerializerProvider();
        
        intLikeSerializer.serialize(Byte.valueOf((byte) 5), gen, provider);
        
        assertEquals("5", gen.toString());
        
    }
    
    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer();
        JsonGenerator gen = new JsonGenerator(null);
        SerializerProvider provider = new SerializerProvider();
        
        longSerializer.serialize(Long.valueOf(1000), gen, provider);
        
        assertEquals("1000", gen.toString());
        
    }
    
    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        JsonGenerator gen = new JsonGenerator(null);
        SerializerProvider provider = new SerializerProvider();
        
        floatSerializer.serialize(Float.valueOf("3.14"), gen, provider);
        
        assertEquals("3.14", gen.toString());
        
    }
    
    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        JsonGenerator gen = new JsonGenerator(null);
        SerializerProvider provider = new SerializerProvider();
        
        doubleSerializer.serialize(Double.valueOf("2.718"), gen, provider);
        
        assertEquals("2.718", gen.toString());
        
    }
}