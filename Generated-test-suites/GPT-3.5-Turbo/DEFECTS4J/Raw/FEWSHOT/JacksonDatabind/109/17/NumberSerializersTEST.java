package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

@SuppressWarnings("serial")
public class NumberSerializersTest {
    
    @Test
    public void testSerializeShort() throws IOException {
        ShortSerializer serializer = new ShortSerializer();
        Short value = 42;
        JsonGenerator mockGen = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        serializer.serialize(value, mockGen, mockProvider);
        
        verify(mockGen).writeNumber((short) 42);
    }
    
    @Test
    public void testSerializeInteger() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        Integer value = 42;
        JsonGenerator mockGen = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        serializer.serialize(value, mockGen, mockProvider);
        
        verify(mockGen).writeNumber(42);
    }
    
    @Test
    public void testSerializeIntLike() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        Number value = 42;
        JsonGenerator mockGen = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        serializer.serialize(value, mockGen, mockProvider);
        
        verify(mockGen).writeNumber(42);
    }
    
    @Test
    public void testSerializeLong() throws IOException {
        LongSerializer serializer = new LongSerializer(Long.class);
        Long value = 42L;
        JsonGenerator mockGen = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        serializer.serialize(value, mockGen, mockProvider);
        
        verify(mockGen).writeNumber(42L);
    }
    
    @Test
    public void testSerializeFloat() throws IOException {
        FloatSerializer serializer = new FloatSerializer();
        Float value = 42.0f;
        JsonGenerator mockGen = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        serializer.serialize(value, mockGen, mockProvider);
        
        verify(mockGen).writeNumber(42.0f);
    }
    
    @Test
    public void testSerializeDouble() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        Double value = 42.0;
        JsonGenerator mockGen = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        serializer.serialize(value, mockGen, mockProvider);
        
        verify(mockGen).writeNumber(42.0);
    }
    
    @Test
    public void testSerializeWithType() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        Double value = 42.0;
        JsonGenerator mockGen = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        TypeSerializer mockTypeSer = mock(TypeSerializer.class);
        
        serializer.serializeWithType(value, mockGen, mockProvider, mockTypeSer);
        
        verify(mockGen).writeNumber(42.0);
    }
    
    @Test
    public void testGetSchema() {
        ShortSerializer serializer = new ShortSerializer();
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        JavaType mockType = mock(JavaType.class);
        
        JsonNode result = serializer.getSchema(mockProvider, mockType);
        
        assertNotNull(result);
    }
    
    @Test
    public void testAcceptJsonFormatVisitorForInt() throws JsonMappingException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        JsonFormatVisitorWrapper mockVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType mockType = mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(mockVisitorWrapper, mockType);
        
        verify(mockVisitorWrapper).visitIntFormat(eq(mockType), any(JsonParser.NumberType.class));
    }
    
    @Test
    public void testAcceptJsonFormatVisitorForFloat() throws JsonMappingException {
        FloatSerializer serializer = new FloatSerializer();
        JsonFormatVisitorWrapper mockVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType mockType = mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(mockVisitorWrapper, mockType);
        
        verify(mockVisitorWrapper).visitFloatFormat(eq(mockType), any(JsonParser.NumberType.class));
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        BeanProperty mockProperty = mock(BeanProperty.class);
        
        JsonSerializer<?> result = serializer.createContextual(mockProvider, mockProperty);
        
        assertEquals(serializer, result);
    }
    
    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        
        NumberSerializers.addAll(allDeserializers);
        
        assertEquals(7, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }
}