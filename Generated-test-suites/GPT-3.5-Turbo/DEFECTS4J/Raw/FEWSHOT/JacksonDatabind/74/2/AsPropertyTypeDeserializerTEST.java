package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AsPropertyTypeDeserializerTest {
    
    private static final String TYPE_PROPERTY_NAME = "type";

    @Test
    public void testConstructorWithTypeIdVisible() {
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType defaultImpl = mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, TYPE_PROPERTY_NAME, true, defaultImpl);
        
        assertEquals(bt, deserializer.baseType());
        assertEquals(idRes, deserializer._idResolver);
        assertEquals(TYPE_PROPERTY_NAME, deserializer._typePropertyName);
        assertTrue(deserializer._typeIdVisible);
        assertEquals(defaultImpl, deserializer._defaultImpl);
        assertEquals(As.PROPERTY, deserializer._inclusion);
    }
    
    @Test
    public void testConstructorWithoutTypeIdVisible() {
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType defaultImpl = mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, TYPE_PROPERTY_NAME, false, defaultImpl);
        
        assertEquals(bt, deserializer.baseType());
        assertEquals(idRes, deserializer._idResolver);
        assertEquals(TYPE_PROPERTY_NAME, deserializer._typePropertyName);
        assertFalse(deserializer._typeIdVisible);
        assertEquals(defaultImpl, deserializer._defaultImpl);
        assertEquals(As.PROPERTY, deserializer._inclusion);
    }
    
    @Test
    public void testGetTypeInclusion() {
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType defaultImpl = mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, TYPE_PROPERTY_NAME, true, defaultImpl);
        
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }
    
    @Test
    public void testForProperty() {
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType defaultImpl = mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, TYPE_PROPERTY_NAME, true, defaultImpl);
        
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer newDeserializer = deserializer.forProperty(property);
        
        assertTrue(newDeserializer instanceof AsPropertyTypeDeserializer);
        assertNotEquals(deserializer, newDeserializer);
    }
    
    @Test
    public void testDeserializeTypedFromObjectWithNativeTypeId() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType defaultImpl = mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, TYPE_PROPERTY_NAME, true, defaultImpl);
        
        when(p.canReadTypeId()).thenReturn(true);
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);
        
        assertNull(result);
        verifyZeroInteractions(ctxt);
    }
    
    @Test
    public void testDeserializeTypedFromObjectWithStartObjectToken() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType defaultImpl = mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = spy(new AsPropertyTypeDeserializer(bt, idRes, TYPE_PROPERTY_NAME, true, defaultImpl));
        
        when(p.canReadTypeId()).thenReturn(false);
        when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT, JsonToken.FIELD_NAME);
        when(p.nextToken()).thenReturn(JsonToken.FIELD_NAME, JsonToken.END_OBJECT);
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);
        
        assertNull(result);
        verify(ctxt, never()).reportWrongTokenException(p, JsonToken.FIELD_NAME,
                "missing property '"+TYPE_PROPERTY_NAME+"' that is to contain type id  (for class "+deserializer.baseTypeName()+")");
    }
    
    @Test
    public void testDeserializeTypedFromObjectWithNonFieldToken() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType defaultImpl = mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = spy(new AsPropertyTypeDeserializer(bt, idRes, TYPE_PROPERTY_NAME, true, defaultImpl));
        
        when(p.canReadTypeId()).thenReturn(false);
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);
        
        assertNull(result);
        verify(ctxt).reportWrongTokenException(p, JsonToken.FIELD_NAME,
                "missing property '"+TYPE_PROPERTY_NAME+"' that is to contain type id  (for class "+deserializer.baseTypeName()+")");
    }
    
    @Test
    public void testDeserializeTypedFromObjectWithFieldToken() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType defaultImpl = mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = spy(new AsPropertyTypeDeserializer(bt, idRes, TYPE_PROPERTY_NAME, true, defaultImpl));
        
        when(p.canReadTypeId()).thenReturn(false);
        when(p.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        when(p.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);
        
        assertNull(result);
        verify(ctxt, never()).reportWrongTokenException(p, JsonToken.FIELD_NAME,
                "missing property '"+TYPE_PROPERTY_NAME+"' that is to contain type id  (for class "+deserializer.baseTypeName()+")");
    }
    
    @Test
    public void testDeserializeTypedForId() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType defaultImpl = mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = spy(new AsPropertyTypeDeserializer(bt, idRes, TYPE_PROPERTY_NAME, true, defaultImpl));
        
        String typeId = "type";
        
        Object result = deserializer._deserializeTypedForId(p, ctxt, null);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType defaultImpl = mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = spy(new AsPropertyTypeDeserializer(bt, idRes, TYPE_PROPERTY_NAME, true, defaultImpl));
        
        Object result = deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, null);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeTypedFromAnyWithStartArrayToken() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType defaultImpl = mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = spy(new AsPropertyTypeDeserializer(bt, idRes, TYPE_PROPERTY_NAME, true, defaultImpl));
        
        when(p.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        Object result = deserializer.deserializeTypedFromAny(p, ctxt);
        
        assertNull(result);
        verify(deserializer).deserializeTypedFromArray(p, ctxt);
    }
    
    @Test
    public void testDeserializeTypedFromAnyWithNonStartArrayToken() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType defaultImpl = mock(JavaType.class);
        AsPropertyTypeDeserializer deserializer = spy(new AsPropertyTypeDeserializer(bt, idRes, TYPE_PROPERTY_NAME, true, defaultImpl));
        
        when(p.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        Object result = deserializer.deserializeTypedFromAny(p, ctxt);
        
        assertNull(result);
        verify(deserializer).deserializeTypedFromObject(p, ctxt);
        verify(deserializer, never()).deserializeTypedFromArray(p, ctxt);
    }
}