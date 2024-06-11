package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {
    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null){};
        
        String actual = typeDeserializerBase.baseTypeName();
        
        assertTrue(actual.isEmpty());
    }
    
    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "typePropertyName", true, null){};
        
        String actual = typeDeserializerBase.getPropertyName();
        
        assertEquals("typePropertyName", actual);
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null){};
        
        DeserializationContext mockDeserializationContext = new DeserializationContext(null, null);
        TypeIdResolver actual = typeDeserializerBase.getTypeIdResolver();
        
        assertNull(actual);
    }
    
    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null){};
        
        Class<?> actual = typeDeserializerBase.getDefaultImpl();
        
        assertNull(actual);
    }
    
    @Test
    public void testToString() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null){};
        
        String actual = typeDeserializerBase.toString();
        
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", actual);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null){};
        
        DeserializationContext mockDeserializationContext = new DeserializationContext(null, null);
        
        Object actual = typeDeserializerBase._deserializeWithNativeTypeId(null, mockDeserializationContext);
        
        assertNull(actual);
    }
    
    @Test
    public void testHandleUnknownTypeId() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null){};
        
        DeserializationContext mockDeserializationContext = new DeserializationContext(null, null);
        
        JavaType mockBaseType = new JavaType(null, ClassUtil.bootstrapNull, null, null, null);
        
        try {
            typeDeserializerBase._handleUnknownTypeId(mockDeserializationContext, "typeId", null, mockBaseType);
        } catch (Exception e) {
            assertEquals("Cannot construct instance of java.util.Set (although at least one Creator exists): no String-argument constructor/factory method to deserialize from String value ('typeId')\n at [Source: (com.fasterxml.jackson.core.JsonParser); line: 1, column: 1]", e.getMessage());
        }
    }
}