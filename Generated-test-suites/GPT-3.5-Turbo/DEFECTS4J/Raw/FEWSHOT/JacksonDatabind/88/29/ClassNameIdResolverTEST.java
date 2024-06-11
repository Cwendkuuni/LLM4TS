package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {
    
    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }
    
    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        resolver.registerSubtype(String.class, "String");
        // Assert that nothing happens since registerSubtype is not used with class name-based resolvers
    }
    
    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertNotNull(id);
    }
    
    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        assertNotNull(id);
    }
    
    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = "java.lang.String";
        DatabindContext context = null;
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
    }
    
    @Test
    public void testTypeFromId_unknownId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = "UnknownClass";
        DatabindContext context = null;
        JavaType type = resolver.typeFromId(context, id);
        assertNull(type);
    }
    
    @Test
    public void testTypeFromId_nullContext() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = "java.lang.String";
        String unknownId = "UnknownId";
        JavaType type = resolver.typeFromId(null, id);
        assertNotNull(type);
        
        type = resolver.typeFromId(null, unknownId);
        assertNull(type);
    }
    
    @Test
    public void testTypeFromId_ExceptionThrown() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = "java.lang.String";
        DatabindContext context = new DeserializationContext(null, null);
        ExceptionThrown expectedException = null;
        
        try {
            resolver.typeFromId(context, id);
        } catch (ExceptionThrown e) {
            expectedException = e;
        }
        
        assertNotNull(expectedException);
    }

    @Test
    public void testTypeFromId_genericId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = "com.fasterxml.jackson.databind.type.TypeFactory<java.lang.String>";
        DatabindContext context = null;
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
    }
    
    @Test
    public void test_typeFromId_innerClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = "com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase$TypeIdResolverBaseBuilder";
        DatabindContext context = null;
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
    }
    
    @Test
    public void test_typeFromId_innerClass_nullOuterClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = "com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase$1";
        DatabindContext context = null;
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
    }
    
    @Test
    public void test_typeFromId_innerClass_nonStaticOuterClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = "com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase$TypeIdResolverBaseBuilder$1";
        DatabindContext context = null;
        JavaType type = resolver.typeFromId(context, id);
        assertNotNull(type);
    }
    
    @Test
    public void test_idFrom() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = null;
        String id = resolver._idFrom(value, cls, typeFactory);
        assertNotNull(id);
    }
    
    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
}