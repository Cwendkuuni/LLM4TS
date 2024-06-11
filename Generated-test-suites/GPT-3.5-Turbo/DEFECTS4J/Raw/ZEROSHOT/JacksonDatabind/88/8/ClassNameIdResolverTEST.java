package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {
    
    private ClassNameIdResolver classNameIdResolver;
    
    @Before
    public void setUp() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);
    }
    
    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }
    
    @Test
    public void testRegisterSubtype() {
        // Since the method implementation is empty, this test can't be performed.
    }
    
    @Test
    public void testIdFromValue() {
        Object value = new Object();
        String expected = "java.lang.Object";
        String result = classNameIdResolver.idFromValue(value);
        assertEquals(expected, result);
    }
    
    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = value.getClass();
        String expected = "java.lang.Object";
        String result = classNameIdResolver.idFromValueAndType(value, type);
        assertEquals(expected, result);
    }
    
    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = new DeserializationContext(null, null);
        String id = "java.lang.Object";
        JavaType expected = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType result = classNameIdResolver.typeFromId(context, id);
        assertEquals(expected, result);
    }
    
    @Test
    public void testTypeFromIdNotFound() throws IOException {
        DatabindContext context = new DeserializationContext(null, null);
        String id = "non.existent.Class";
        JavaType result = classNameIdResolver.typeFromId(context, id);
        assertNull(result);
    }
    
    @Test
    public void testTypeFromIdInvalid() throws IOException {
        DatabindContext context = new DeserializationContext(null, null);
        String id = "java.util.nonexistent";
        try {
            classNameIdResolver.typeFromId(context, id);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid type id 'java.util.nonexistent' (for id type 'Id.class'): no such class found", e.getMessage());
        }
    }
    
    @Test
    public void testTypeFromIdWithGenerics() throws IOException {
        DatabindContext context = new DeserializationContext(null, null);
        String id = "java.util.HashMap<java.lang.String,java.lang.Integer>";
        JavaType expected = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Integer.class);
        JavaType result = classNameIdResolver.typeFromId(context, id);
        assertEquals(expected, result);
    }
    
    @Test
    public void testTypeFromIdWithInnerClass() throws IOException {
        DatabindContext context = new DeserializationContext(null, null);
        String id = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$InnerClass";
        JavaType expected = TypeFactory.defaultInstance().constructType(InnerClass.class);
        JavaType result = classNameIdResolver.typeFromId(context, id);
        assertEquals(expected, result);
    }
    
    @Test
    public void testTypeFromIdInvalidInnerClass() throws IOException {
        DatabindContext context = new DeserializationContext(null, null);
        String id = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$InvalidInnerClass";
        JavaType result = classNameIdResolver.typeFromId(context, id);
        assertNull(result);
    }
    
    @Test
    public void testTypeFromIdWithArrays() throws IOException {
        DatabindContext context = new DeserializationContext(null, null);
        String id = "java.util.ArrayList";
        JavaType expected = TypeFactory.defaultInstance().constructRawCollectionType(ArrayList.class);
        JavaType result = classNameIdResolver.typeFromId(context, id);
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetDescForKnownTypeIds() {
        String expected = "class name used as type id";
        String result = classNameIdResolver.getDescForKnownTypeIds();
        assertEquals(expected, result);
    }
    
    private enum InnerClass {
        INNER;
    }
}