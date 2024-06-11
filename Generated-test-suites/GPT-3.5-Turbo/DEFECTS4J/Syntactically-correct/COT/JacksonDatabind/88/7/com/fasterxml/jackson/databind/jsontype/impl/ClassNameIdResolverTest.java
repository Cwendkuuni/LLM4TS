
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver idResolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, idResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // Method not used with class name - based resolvers, no need to test
    }
    
    @Test
    public void testIdFromValue() {
        Object value = new Object();
        ClassNameIdResolver idResolver = new ClassNameIdResolver(null, null);
        String id = idResolver.idFromValue(value);
        assertEquals(value.getClass().getName(), id);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = value.getClass();
        ClassNameIdResolver idResolver = new ClassNameIdResolver(null, null);
        String id = idResolver.idFromValueAndType(value, type);
        assertEquals(type.getName(), id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = new DeserializationContext(null, null);
        String id = Object.class.getName();
        ClassNameIdResolver idResolver = new ClassNameIdResolver(null, null);
        JavaType javaType = idResolver.typeFromId(context, id);
        assertEquals(TypeFactory.defaultInstance().constructType(Object.class), javaType);
    }

    @Test
    public void testTypeFromId_UnknownId() throws IOException {
        DatabindContext context = new DeserializationContext(null, null);
        String id = "com.example.UnknownClass";
        ClassNameIdResolver idResolver = new ClassNameIdResolver(null, null);
        JavaType javaType = idResolver.typeFromId(context, id);
        assertNull(javaType);
    }

    @Test
    public void testTypeFromId_InvalidId() throws IOException {
        DatabindContext context = new DeserializationContext(null, null);
        String id = "invalid-id";
        ClassNameIdResolver idResolver = new ClassNameIdResolver(null, null);
        try {
            idResolver.typeFromId(context, id);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Invalid type id 'invalid-id' (for id type 'Id.class'): null", e.getMessage());
        }
    }

    @Test
    public void test_typeFromId_genericsInfoAdded() throws IOException {
        DatabindContext context = new DeserializationContext(null, null);
        String id = "java.util.List<java.lang.String>";
        ClassNameIdResolver idResolver = new ClassNameIdResolver(null, null);
        JavaType javaType = idResolver.typeFromId(context, id);
        assertEquals(TypeFactory.defaultInstance().constructCollectionType(List.class, String.class), javaType);
    }

    @Test
    public void test_idFrom_enumClass() {
        Class<?> cls = TestEnum.class;
        ClassNameIdResolver idResolver = new ClassNameIdResolver(null, null);
        Object value = TestEnum.ONE;
        String id = idResolver._idFrom(value, cls, null);
        assertEquals(value.getClass().getName(), id);
    }

    @Test
    public void test_idFrom_enumSubclass() {
        Class<?> cls = TestEnumChild.class;
        ClassNameIdResolver idResolver = new ClassNameIdResolver(null, null);
        Object value = TestEnumChild.ONE;
        String id = idResolver._idFrom(value, cls, null);
        assertEquals(cls.getSuperclass().getName(), id);
    }

    @Test
    public void test_idFrom_javaUtilEnumSet() {
        Class<?> cls = EnumSet.class;
        ClassNameIdResolver idResolver = new ClassNameIdResolver(null, null);
        Object value = EnumSet.of(TestEnum.ONE);
        String id = idResolver._idFrom(value, cls, null);
        assertEquals(EnumSet.class.getName(), id);
    }

    @Test
    public void test_idFrom_javaUtilEnumMap() {
        Class<?> cls = EnumMap.class;
        ClassNameIdResolver idResolver = new ClassNameIdResolver(null, null);
        Object value = new EnumMap<>(TestEnum.class);
        String id = idResolver._idFrom(value, cls, null);
        assertEquals(EnumMap.class.getName(), id);
    }

    @Test
    public void test_idFrom_javaUtilArrayList() {
        Class<?> cls = ArrayList.class;
        ClassNameIdResolver idResolver = new ClassNameIdResolver(null, null);
        Object value = new ArrayList<>();
        String id = idResolver._idFrom(value, cls, null);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void test_idFrom_innerClass() {
        Class<?> cls = Outer.Inner.class;
        ClassNameIdResolver idResolver = new ClassNameIdResolver(null, null);
        Object value = new Outer.Inner();
        String id = idResolver._idFrom(value, cls, null);
        assertEquals(cls.getName(), id);
    }

    @Test
    public void test_getDescForKnownTypeIds() {
        ClassNameIdResolver idResolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", idResolver.getDescForKnownTypeIds());
    }

    private static enum TestEnum {
        ONE, TWO, THREE
    }

    private static class TestEnumChild extends TestEnum {
        
    }
}
