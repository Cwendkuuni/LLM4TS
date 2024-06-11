package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

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
        resolver.registerSubtype(Object.class, "object");
        // Check that the method does nothing
    }
    
    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        assertEquals("java.lang.Object", resolver.idFromValue(value));
    }
    
    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        Class<?> type = Object.class;
        assertEquals("java.lang.Object", resolver.idFromValueAndType(value, type));
    }
    
    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "java.lang.Object";
        JavaType expected = TypeFactory.defaultInstance().constructType(Object.class);
        assertEquals(expected, resolver.typeFromId(context, id));
    }
    
    @Test
    public void testTypeFromId_unknownId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "unknown.Type";
        assertNull(resolver.typeFromId(context, id));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromId_invalidType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "invalid.Type";
        resolver.typeFromId(context, id);
    }
    
    @Test
    public void test_typeFromId_generics() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "java.util.List<java.lang.String>";
        JavaType expected = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        assertEquals(expected, resolver.typeFromId(context, id));
    }
    
    @Test
    public void test_typeFromId_innerClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "com.fasterxml.jackson.databind.util.ClassUtil$TestClass";
        JavaType expected = TypeFactory.defaultInstance().constructType(ClassUtil.TestClass.class);
        assertEquals(expected, resolver.typeFromId(context, id));
    }
    
    @Test
    public void test_typeFromId_enumSet() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "java.util.EnumSet<java.lang.String>";
        JavaType expected = TypeFactory.defaultInstance().constructCollectionType(EnumSet.class, String.class);
        assertEquals(expected, resolver.typeFromId(context, id));
    }
    
    @Test
    public void test_typeFromId_enumMap() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "java.util.EnumMap<java.lang.String,java.lang.Integer>";
        JavaType expected = TypeFactory.defaultInstance().constructMapType(EnumMap.class, String.class, Integer.class);
        assertEquals(expected, resolver.typeFromId(context, id));
    }
    
    @Test
    public void test_typeFromId_collectionFromLists() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "java.util.Arrays$ArrayList";
        JavaType expected = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertEquals(expected, resolver.typeFromId(context, id));
    }
    
    @Test
    public void test_idFrom() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        Class<?> cls = Object.class;
        String expected = "java.lang.Object";
        assertEquals(expected, resolver._idFrom(value, cls, null));
    }
    
    @Test
    public void test_idFrom_enumClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new EnumClass();
        Class<?> cls = EnumClass.class;
        String expected = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$EnumClass";
        assertEquals(expected, resolver._idFrom(value, cls, null));
    }
    
    @Test
    public void test_idFrom_enumSet() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = EnumSet.allOf(EnumClass.class);
        Class<?> cls = EnumSet.class;
        String expected = "java.util.EnumSet<com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$EnumClass>";
        assertEquals(expected, resolver._idFrom(value, cls, null));
    }
    
    @Test
    public void test_idFrom_enumMap() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new EnumMap<EnumClass, String>(EnumClass.class);
        Class<?> cls = EnumMap.class;
        String expected = "java.util.EnumMap<com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$EnumClass,java.lang.String>";
        assertEquals(expected, resolver._idFrom(value, cls, null));
    }
    
    public static class EnumClass {
        public enum EnumType {
            VALUE1, VALUE2
        }
    }
    
    @Test
    public void test_getDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
    
}