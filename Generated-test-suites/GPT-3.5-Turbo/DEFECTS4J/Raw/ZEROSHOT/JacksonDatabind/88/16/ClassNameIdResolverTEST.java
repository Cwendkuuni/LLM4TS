package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        JsonTypeInfo.Id mechanism = resolver.getMechanism();
        assertEquals(JsonTypeInfo.Id.CLASS, mechanism);
    }

    @Test
    public void testRegisterSubtype() {
        // not used with class name - based resolvers
    }

    @Test
    public void testIdFromValue() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        String id = resolver.idFromValue("test");
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testIdFromValueAndType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        String id = resolver.idFromValueAndType("test", String.class);
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        DatabindContext context = new DeserializationContext(null, null);
        JavaType type = resolver.typeFromId(context, "java.lang.String");
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void test_typeFromId_withInvalidId_shouldThrowException() throws IOException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        DatabindContext context = new DeserializationContext(null, null);
        try {
            JavaType type = resolver.typeFromId(context, "invalid.id");
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Invalid type id 'invalid.id' (for id type 'Id.class'): null", e.getMessage());
        }
    }

    @Test
    public void test_typeFromId_withUnknownClass_shouldReturnNull() throws IOException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        DatabindContext context = new DeserializationContext(null, null);
        JavaType type = resolver.typeFromId(context, "com.example.UnknownClass");
        assertNull(type);
    }

    @Test
    public void test_typeFromId_withUnknownClass_shouldHandleUnknownTypeId() throws IOException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        DatabindContext context = new DeserializationContext(null, null);
        try {
            JavaType type = resolver.typeFromId(context, "com.example.UnknownClass");
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("no such class found", e.getMessage());
        }
    }

    @Test
    public void test_idFromValue_withEnumValue_shouldReturnEnumClassName() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        String id = resolver.idFromValue(MyEnum.ONE);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$MyEnum", id);
    }

    @Test
    public void test_idFromValue_withSubClassOfEnumValue_shouldReturnEnumClassName() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        String id = resolver.idFromValue(SubClassOfEnum.ONE);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$MyEnum", id);
    }

    @Test
    public void test_idFromValueAndType_withEnumValue_shouldReturnEnumClassName() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        String id = resolver.idFromValueAndType(MyEnum.TWO, MyEnum.class);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$MyEnum", id);
    }

    @Test
    public void test__typeFromId_withEnumClassName_shouldReturnEnumType() throws IOException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        DatabindContext context = new DeserializationContext(null, null);
        JavaType type = resolver.typeFromId(context, "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$MyEnum");
        assertEquals(MyEnum.class, type.getRawClass());
    }

    @Test
    public void test_idFromValue_withEnumSet_shouldReturnEnumSetClassName() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        String id = resolver.idFromValue(EnumSet.allOf(MyEnum.class));
        assertEquals("java.util.EnumSet<com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$MyEnum>", id);
    }

    @Test
    public void test_idFromValue_withEnumMap_shouldReturnEnumMapClassName() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        String id = resolver.idFromValue(new EnumMap<>(MyEnum.class));
        assertEquals("java.util.EnumMap<com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$MyEnum, java.lang.Object>", id);
    }

    @Test
    public void test__idFrom_withUtilArrayList_shouldReturnJavaUtilArrayListClassName() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
        String id = resolver._idFrom(null, java.util.Collections.emptyList().getClass(), TypeFactory.defaultInstance());
        assertEquals("java.util.ArrayList", id);
    }

    public enum MyEnum {
        ONE, TWO, THREE
    }

    public static class SubClassOfEnum extends MyEnum {
        public static final SubClassOfEnum ONE = new SubClassOfEnum();

        private SubClassOfEnum() {
            super();
        }
    }
}