package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {
    
    private AsWrapperTypeDeserializer deserializer;
    private JsonParser jp;
    private DeserializationContext ctxt;
    
    @Before
    public void setup() {
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        BeanProperty property = mock(BeanProperty.class);
        
        deserializer = new AsWrapperTypeDeserializer(bt, idRes, "typePropertyName", true, Object.class);
        deserializer = new AsWrapperTypeDeserializer(deserializer, property);
        
        jp = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
    }
    
    @Test
    public void testForProperty() {
        BeanProperty prop = mock(BeanProperty.class);
        TypeDeserializer newDeserializer = deserializer.forProperty(prop);
        assertEquals(prop, newDeserializer.getProperty());
    }
    
    @Test
    public void testGetTypeInclusion() {
        assertEquals(As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
    }
    
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        Object result = new Object();
        when(deserializer._deserialize(jp, ctxt)).thenReturn(result);
        
        assertEquals(result, deserializer.deserializeTypedFromObject(jp, ctxt));
    }
    
    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        Object result = new Object();
        when(deserializer._deserialize(jp, ctxt)).thenReturn(result);
        
        assertEquals(result, deserializer.deserializeTypedFromArray(jp, ctxt));
    }
    
    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        Object result = new Object();
        when(deserializer._deserialize(jp, ctxt)).thenReturn(result);
        
        assertEquals(result, deserializer.deserializeTypedFromScalar(jp, ctxt));
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        Object result = new Object();
        when(deserializer._deserialize(jp, ctxt)).thenReturn(result);
        
        assertEquals(result, deserializer.deserializeTypedFromAny(jp, ctxt));
    }
    
    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object result = new Object();
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        
        when(p.canReadTypeId()).thenReturn(false);
        when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(p.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(p.getText()).thenReturn("typeId");
        when(deserializer._findDeserializer(ctxt, "typeId")).thenReturn(deser);
        when(deser.deserialize(p, ctxt)).thenReturn(result);
        when(p.nextToken()).thenReturn(JsonToken.END_OBJECT);
        
        assertEquals(result, deserializer._deserialize(p, ctxt));
    }
}