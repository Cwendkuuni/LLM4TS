package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {
    @Mock
    private JavaType bt;
    
    @Mock
    private TypeIdResolver idRes;
    
    @Mock
    private BeanProperty prop;
    
    @Mock
    private JsonParser jp;
    
    @Mock
    private DeserializationContext ctxt;
    
    @Mock
    private JsonDeserializer<Object> deser;
    
    @Mock
    private TokenBuffer tb;
    
    @InjectMocks
    private AsWrapperTypeDeserializer deserializer;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        when(jp.canReadTypeId()).thenReturn(true);
        when(jp.getTypeId()).thenReturn("typeId");
        when(deserializer._deserializeWithNativeTypeId(jp, ctxt, "typeId")).thenReturn("result");
        
        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
        
        assertEquals("result", result);
    }
    
    @Test
    public void testDeserializeTypedFromObject_NoTypeId() throws IOException {
        when(jp.canReadTypeId()).thenReturn(true);
        when(jp.getTypeId()).thenReturn(null);
        
        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
        
        verifyZeroInteractions(deserializer);
        assertEquals(null, result);
    }
    
    @Test
    public void testDeserializeTypedFromObject_WrongToken() throws IOException {
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        
        try {
            deserializer.deserializeTypedFromObject(jp, ctxt);
        } catch (IOException e) {
            assertEquals("Expected JSON Object token but found: START_ARRAY", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);
        
        assertEquals(result, null); // Change the expected null to actual Null
    }
    
    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);
        
        assertEquals(result, null); // Change the expected null to actual Null
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);
        
        assertEquals(result, null); // Change the expected null to actual Null
    }
    
    @Test
    public void test_forProperty() {
        TypeDeserializer result = deserializer.forProperty(prop);
        
        assertEquals(deserializer, result);
    }
    
    @Test
    public void testGetTypeInclusion() {
        As result = deserializer.getTypeInclusion();
        
        assertEquals(As.WRAPPER_OBJECT, result);
    }
    
    @Test
    public void test_deserialize() throws IOException {
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME, JsonToken.VALUE_STRING, JsonToken.END_OBJECT);
        when(jp.getText()).thenReturn("typeId");
        when(tb.asParser(jp)).thenReturn(jp);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_OBJECT);
        when(deser.deserialize(jp, ctxt)).thenReturn("result");
        
        Object result = deserializer._deserialize(jp, ctxt);
        
        assertEquals("result", result);
    }
    
    @Test
    public void test_deserialize_NoTypeId() throws IOException {
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME, JsonToken.VALUE_STRING, JsonToken.END_OBJECT);
        when(jp.getText()).thenReturn("typeId");
        when(tb.asParser(jp)).thenReturn(jp);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_OBJECT);
        when(deser.deserialize(jp, ctxt)).thenReturn("result");
        
        Object result = deserializer._deserialize(jp, ctxt);
        
        assertEquals("result", result);
    }
    
    @Test
    public void test_deserialize_TypeIdVisible() throws IOException {
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(tb.writeStartObject()).thenReturn(tb);
        when(tb.writeFieldName("typePropertyName")).thenReturn(tb);
        when(tb.writeString("typeId")).thenReturn(tb);
        when(tb.asParser(jp)).thenReturn(jp);
        when(jp.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(deser.deserialize(jp, ctxt)).thenReturn("result");
        when(jp.nextToken()).thenReturn(JsonToken.END_OBJECT);
        
        Object result = deserializer._deserialize(jp, ctxt);
        
        assertEquals("result", result);
    }
    
    @Test
    public void test_deserialize_WrongToken() throws IOException {
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        
        try {
            deserializer._deserialize(jp, ctxt);
        } catch (IOException e) {
            assertEquals("Expected JSON Object token but found: START_ARRAY", e.getMessage());
        }
    }
    
    @Test
    public void test_baseTypeName() {
        assertEquals("javaType", deserializer.baseTypeName());
    }
}