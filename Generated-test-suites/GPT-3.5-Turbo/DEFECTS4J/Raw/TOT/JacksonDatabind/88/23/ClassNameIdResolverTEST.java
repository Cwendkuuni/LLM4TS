package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Test;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver idResolver;
    private DatabindContext context;
    private JavaType baseType;
    
    @Test
    public void testClassNameIdResolver() {
        baseType = TypeFactory.defaultInstance().uncheckedSimpleType(TypeFactory.rawClass(String.class));
        idResolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        context = new DeserializationContext(null, null);
        
        testGetMechanism();
        testRegisterSubtype();
        testIdFromValue();
        testIdFromValueAndType();
        testTypeFromId();
    }
    
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, idResolver.getMechanism());
    }
    
    public void testRegisterSubtype() {
        // Since registerSubtype is not used in this class, there are no test cases for it
    }
    
    public void testIdFromValue() {
        Object value = "Test";
        String expected = "java.lang.String";
        assertEquals(expected, idResolver.idFromValue(value));
        
        value = 123;
        expected = "java.lang.Integer";
        assertEquals(expected, idResolver.idFromValue(value));
        
        value = true;
        expected = "java.lang.Boolean";
        assertEquals(expected, idResolver.idFromValue(value));
    }
    
    public void testIdFromValueAndType() {
        Object value = "Test";
        Class<?> type = String.class;
        String expected = "java.lang.String";
        assertEquals(expected, idResolver.idFromValueAndType(value, type));
        
        value = 123;
        type = Integer.class;
        expected = "java.lang.Integer";
        assertEquals(expected, idResolver.idFromValueAndType(value, type));
        
        value = true;
        type = Boolean.class;
        expected = "java.lang.Boolean";
        assertEquals(expected, idResolver.idFromValueAndType(value, type));
    }
    
    public void testTypeFromId() throws IOException {
        String id = "java.lang.String";
        baseType = TypeFactory.defaultInstance().uncheckedSimpleType(TypeFactory.rawClass(String.class));
        idResolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        context = new DeserializationContext(null, null);
        
        JavaType expected = TypeFactory.defaultInstance().uncheckedSimpleType(TypeFactory.rawClass(String.class));
        assertEquals(expected, idResolver.typeFromId(context, id));
        
        id = "java.util.EnumSet";
        baseType = TypeFactory.defaultInstance().uncheckedSimpleType(TypeFactory.rawClass(EnumSet.class));
        idResolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        context = new DeserializationContext(null, null);
        
        expected = TypeFactory.defaultInstance().uncheckedSimpleType(TypeFactory.rawClass(EnumSet.class));
        assertEquals(expected, idResolver.typeFromId(context, id));
        
        id = "java.util.EnumMap";
        baseType = TypeFactory.defaultInstance().uncheckedSimpleType(TypeFactory.rawClass(EnumMap.class));
        idResolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        context = new DeserializationContext(null, null);
        
        expected = TypeFactory.defaultInstance().uncheckedSimpleType(TypeFactory.rawClass(EnumMap.class));
        assertEquals(expected, idResolver.typeFromId(context, id));
        
        id = "invalid.class.Name";
        assertNull(idResolver.typeFromId(context, id));
    }
    
    public void test_idFrom() {
        // This is a protected method and current implementation does not require extra test cases for it
    }
    
    public void testGetDescForKnownTypeIds() {
        String expected = "class name used as type id";
        assertEquals(expected, idResolver.getDescForKnownTypeIds());
    }
    
}