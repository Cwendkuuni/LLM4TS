package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver resolver;

    @Before
    public void setUp() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        resolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        JsonTypeInfo.Id mechanism = resolver.getMechanism();
        assertEquals(JsonTypeInfo.Id.CLASS, mechanism);
    }

    @Test
    public void testRegisterSubtype() {
        // not used with class name - based resolvers
    }

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertEquals(value.getClass().getName(), id);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        assertEquals(type.getName(), id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = new MockDatabindContext();
        String id = "java.lang.String";
        JavaType expectedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType actualType = resolver.typeFromId(context, id);
        assertEquals(expectedType, actualType);
    }
    
    @Test
    public void testTypeFromId_UnknownType() throws IOException {
        DatabindContext context = new MockDatabindContext();
        String id = "unknown.type";
        JavaType actualType = resolver.typeFromId(context, id);
        assertNull(actualType);
    }

    @Test
    public void testIdFromValue_Enum() {
        Object value = MockEnum.FIRST;
        String id = resolver.idFromValue(value);
        assertEquals(MockEnum.FIRST.getClass().getSuperclass().getName(), id);
    }

    @Test
    public void testIdFromValueAndType_Enum() {
        Object value = MockEnum.SECOND;
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        assertEquals(type.getSuperclass().getName(), id);
    }

    @Test
    public void testIdFromValue_EnumSet() {
        Object value = EnumSet.allOf(MockEnum.class);
        String id = resolver.idFromValue(value);
        String expectedId = TypeFactory.defaultInstance()
                            .constructCollectionType(EnumSet.class, MockEnum.class)
                            .toCanonical();
        assertEquals(expectedId, id);
    }

    @Test
    public void testIdFromValue_EnumMap() {
        Object value = new EnumMap<>(MockEnum.class);
        String id = resolver.idFromValue(value);
        String expectedId = TypeFactory.defaultInstance()
                            .constructMapType(EnumMap.class, MockEnum.class, Object.class)
                            .toCanonical();
        assertEquals(expectedId, id);
    }

    @Test
    public void testIdFromValue_JavaUtilArraysList() {
        Object value = Arrays.asList("value1", "value2");
        String id = resolver.idFromValue(value);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void test_idFrom_WithInnerClass() {
        Object value = new ClassNameIdResolverTest.InnerClass();
        Class<?> cls = value.getClass();
        JavaType baseType = TypeFactory.defaultInstance().constructType(cls);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        ClassNameIdResolver innerResolver = new ClassNameIdResolver(baseType, typeFactory);
        String id = innerResolver._idFrom(value, cls, typeFactory);
        assertEquals(cls.getName(), id);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        String desc = resolver.getDescForKnownTypeIds();
        assertEquals("class name used as type id", desc);
    }

    private enum MockEnum {
        FIRST,
        SECOND
    }

    private class MockDatabindContext extends DatabindContext {
        // Mock implementation for testing
    }
    
    private class InnerClass {
        // Inner class for testing
    }
}