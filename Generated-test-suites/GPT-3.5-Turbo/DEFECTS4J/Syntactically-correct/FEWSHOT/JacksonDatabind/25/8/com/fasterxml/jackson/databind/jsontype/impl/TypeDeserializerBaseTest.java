package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {
    private final TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
    private final JavaType baseType = Mockito.mock(JavaType.class);
    private final BeanProperty property = Mockito.mock(BeanProperty.class);
    private final Class<?> defaultImpl = Mockito.mock(Class.class);
    
    private final String typePropertyName = "TestType";
    private final boolean typeIdVisible = true;
    
    private final TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }
    };
    
    @Test
    public void testGetPropertyName() {
        assertEquals("TestType", typeDeserializerBase.getPropertyName());
    }
    
    @Test
    public void testGetTypeIdResolver() {
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
    }
    
    @Test
    public void testGetDefaultImpl() {
        assertEquals(defaultImpl, typeDeserializerBase.getDefaultImpl());
    }
    
    @Test
    public void testToString() {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: " + idResolver + "]";
        assertEquals(expected, typeDeserializerBase.toString());
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object typeId = null;
        
        Mockito.when(baseType.getRawClass()).thenReturn(Object.class);
        JsonDeserializer<Object> expected = NullifyingDeserializer.instance;
        
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        
        assertEquals(expected, result);
        Mockito.verify(baseType).getRawClass();
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeId_UnknownTypeId() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object typeId = null;
        
        Mockito.when(baseType.getRawClass()).thenReturn(Object.class);
        
        typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId_NullValue() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object typeId = null;
        
        Mockito.when(baseType.getRawClass()).thenReturn(Object.class);
        JsonDeserializer<Object> expected = NullifyingDeserializer.instance;
        
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        
        assertEquals(expected, result);
        Mockito.verify(baseType).getRawClass();
    }
    
    @Test
    public void testDeserializeWithNativeTypeId_StringTypeId() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object typeId = "test";
        
        Mockito.when(baseType.getRawClass()).thenReturn(Object.class);
        JsonDeserializer<Object> expected = NullifyingDeserializer.instance;
        
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        
        assertEquals(expected, result);
        Mockito.verify(baseType).getRawClass();
    }
    
    @Test
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        String typeId = "test";
        String extraDesc = "known type ids = test";
        JsonMappingException expectedException = new JsonMappingException(_baseType, typeId, extraDesc);
        
        JsonMappingException result = null;
        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        } catch(JsonMappingException ex) {
            result = ex;
        }
        
        assertEquals(expectedException.getMessage(), result.getMessage());
    }
}