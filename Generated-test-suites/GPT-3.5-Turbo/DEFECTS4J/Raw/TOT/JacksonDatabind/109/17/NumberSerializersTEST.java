package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() throws Exception {
        ShortSerializer serializer = new ShortSerializer();
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        
        // Typical use case
        serializer.serialize((short) 10, gen, provider);
        
        // Edge case
        serializer.serialize(Short.MAX_VALUE, gen, provider);
        
        // Error scenario
        try {
            serializer.serialize("10", gen, provider);
            fail("Expected IOException to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testIntegerSerializer() throws Exception {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        
        // Typical use case
        serializer.serialize(10, gen, provider);
        
        // Edge case
        serializer.serialize(Integer.MAX_VALUE, gen, provider);
        
        // Error scenario
        try {
            serializer.serialize("10", gen, provider);
            fail("Expected IOException to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testIntLikeSerializer() throws Exception {
        IntLikeSerializer serializer = new IntLikeSerializer();
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        
        // Typical use case
        serializer.serialize(10, gen, provider);
        
        // Edge case
        serializer.serialize(Integer.MAX_VALUE, gen, provider);
        
        // Error scenario
        try {
            serializer.serialize("10", gen, provider);
            fail("Expected IOException to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testLongSerializer() throws Exception {
        LongSerializer serializer = new LongSerializer(Long.class);
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        
        // Typical use case
        serializer.serialize(100000000000L, gen, provider);
        
        // Edge case
        serializer.serialize(Long.MAX_VALUE, gen, provider);
        
        // Error scenario
        try {
            serializer.serialize("100000000000", gen, provider);
            fail("Expected IOException to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testFloatSerializer() throws Exception {
        FloatSerializer serializer = new FloatSerializer();
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        
        // Typical use case
        serializer.serialize(10.5f, gen, provider);
        
        // Edge case
        serializer.serialize(Float.MAX_VALUE, gen, provider);
        
        // Error scenario
        try {
            serializer.serialize("10.5", gen, provider);
            fail("Expected IOException to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testDoubleSerializer() throws Exception {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        
        // Typical use case
        serializer.serialize(10.5, gen, provider);
        
        // Edge case
        serializer.serialize(Double.MAX_VALUE, gen, provider);
        
        // Error scenario
        try {
            serializer.serialize("10.5", gen, provider);
            fail("Expected IOException to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

}