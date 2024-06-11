package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {
    
    private TypeDeserializerBase typeDeserializerBase;
    
    @Before
    public void setUp() {
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        typeDeserializerBase = new TypeDeserializerBaseMock(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }
    
    @Test
    public void testBaseTypeName() {
        String expected = null;
        String actual = typeDeserializerBase.baseTypeName();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetPropertyName() {
        String expected = "";
        String actual = typeDeserializerBase.getPropertyName();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver expected = null;
        TypeIdResolver actual = typeDeserializerBase.getTypeIdResolver();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDefaultImpl() {
        Class<?> expected = null;
        Class<?> actual = typeDeserializerBase.getDefaultImpl();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseMock; base-type:null; id-resolver: null]";
        String actual = typeDeserializerBase.toString();
        assertEquals(expected, actual);
    }
    
    private class TypeDeserializerBaseMock extends TypeDeserializerBase {
        
        public TypeDeserializerBaseMock(JavaType baseType, TypeIdResolver idRes,
                String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }
        
        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }
        
        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }
}