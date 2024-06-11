package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {
    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();
        Short value = 123;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        Integer value = 456;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new IntLikeSerializer();
        Number value = 789;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer(Long.class);
        Long value = 123456L;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new FloatSerializer();
        Float value = 3.14f;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        Double value = 1.618;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
}