package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.*;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Class<?> type = Object.class;
        String name = "MyObject";
        resolver.registerSubtype(type, name);
        // Check that nothing happens since this method is not used
        // with class name - based resolvers
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertEquals(value.getClass().getName(), id);
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        assertEquals(type.getName(), id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "com.example.myapp.MyClass";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(Class.forName(id), javaType.getRawClass());
    }

    @Test
    public void test_typeFromId_withUnknownType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "com.example.myapp.UnknownClass";
        JavaType javaType = resolver.typeFromId(context, id);
        assertNull(javaType);
    }

    @Test
    public void test_typeFromId_withException() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "com.example.myapp.InvalidClass";
        try {
            resolver.typeFromId(context, id);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Invalid type id 'com.example.myapp.InvalidClass' (for id type 'Id.class'): null", e.getMessage());
        }
    }

    @Test
    public void test_typeFromId_withEnumSet() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "java.util.EnumSet";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(EnumSet.class, javaType.getRawClass());
    }

    @Test
    public void test_typeFromId_withEnumMap() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "java.util.EnumMap";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(EnumMap.class, javaType.getRawClass());
    }

    @Test
    public void test_idFrom_withEnum() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Enum<MockEnum> value = MockEnum.VALUE1;
        String id = resolver._idFrom(value, value.getClass(), null);
        assertEquals(value.getDeclaringClass().getName(), id);
    }

    @Test
    public void test_idFrom_withSubEnum() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Enum<MockSubEnum> value = MockSubEnum.VALUE1;
        String id = resolver._idFrom(value, value.getClass(), null);
        assertEquals(MockEnum.class.getName(), id);
    }

    @Test
    public void test_getDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }

    private enum MockEnum {
        VALUE1, VALUE2
    }

    private enum MockSubEnum {
        VALUE1, VALUE2
    }
}