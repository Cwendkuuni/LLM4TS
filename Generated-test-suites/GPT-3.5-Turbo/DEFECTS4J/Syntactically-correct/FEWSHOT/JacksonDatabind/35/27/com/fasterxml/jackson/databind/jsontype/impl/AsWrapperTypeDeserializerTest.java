
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.TokenBuffer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializer;

public class AsWrapperTypeDeserializerTest {
    @Test
    public void testAsWrapperTypeDeserializer() {
        JavaType javaType = mock(JavaType.class);
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, typeIdResolver,
                typePropertyName, typeIdVisible, defaultImpl);
        
        // assertions
        assertEquals(As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
    }
    
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        AsWrapperTypeDeserializer deserializer = mock(AsWrapperTypeDeserializer.class, Mockito.CALLS_REAL_METHODS);
        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        
        // assertions
        assertNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        AsWrapperTypeDeserializer deserializer = mock(AsWrapperTypeDeserializer.class, Mockito.CALLS_REAL_METHODS);
        Object result = deserializer.deserializeTypedFromArray(jsonParser, deserializationContext);
        
        // assertions
        assertNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        AsWrapperTypeDeserializer deserializer = mock(AsWrapperTypeDeserializer.class, Mockito.CALLS_REAL_METHODS);
        Object result = deserializer.deserializeTypedFromScalar(jsonParser, deserializationContext);
        
        // assertions
        assertNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        AsWrapperTypeDeserializer deserializer = mock(AsWrapperTypeDeserializer.class, Mockito.CALLS_REAL_METHODS);
        Object result = deserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        
        // assertions
        assertNull(result);
    }
    
    @Test
    public void test_deserialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.createParser("{\"type\":\"test\",\"name\":\"John\"}");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();

        AsWrapperTypeDeserializer deserializer = mock(AsWrapperTypeDeserializer.class, Mockito.CALLS_REAL_METHODS);
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);
        Mockito.when(deserializer._findDeserializer(deserializationContext, "test")).thenReturn(jsonDeserializer);
        Mockito.when(jsonDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn("John");
        
        Object result = deserializer._deserialize(jsonParser, deserializationContext);
        
        // assertions
        assertEquals("John", result);
    }
}

