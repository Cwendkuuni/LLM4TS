package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

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
    
    private NumberSerializers numberSerializers;
    private Map<String, JsonSerializer<?>> allDeserializers;
    
    @Before
    public void setUp() {
        numberSerializers = new NumberSerializers();
        allDeserializers = new HashMap<>();
    }
    
    @Test
    public void testAddAll() {
        numberSerializers.addAll(allDeserializers);
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }
    
    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        Short value = (short) 1;
        
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        shortSerializer.serialize(value, jsonGenerator, serializerProvider);
    }
    
    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
        Integer value = 1;
        
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        integerSerializer.serialize(value, jsonGenerator, serializerProvider);
    }
    
    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        Number value = 1;
        
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        intLikeSerializer.serialize(value, jsonGenerator, serializerProvider);
    }
    
    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer();
        Long value = (long) 1;
        
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        longSerializer.serialize(value, jsonGenerator, serializerProvider);
    }
    
    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        Float value = (float) 1;
        
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        floatSerializer.serialize(value, jsonGenerator, serializerProvider);
    }
    
    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();
        Double value = (double) 1;
        
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        doubleSerializer.serialize(value, jsonGenerator, serializerProvider);
    }
}