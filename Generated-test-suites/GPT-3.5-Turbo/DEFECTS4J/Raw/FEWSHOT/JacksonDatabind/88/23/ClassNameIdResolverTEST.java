package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ClassNameIdResolverTest {
    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // not used with class name - based resolvers, so no assert needed
        resolver.registerSubtype(ClassNameIdResolver.class, "");
    }
    
    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValue(new ClassNameIdResolver(null, null));
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver", id);
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValueAndType(new ClassNameIdResolver(null, null), ClassNameIdResolver.class);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver", id);
    }

    @Test
    public void testTypeFromId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertNull(resolver.typeFromId(null, ""));
    }

    @Test
    public void test_typeFromId_with_generics() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertNull(resolver.typeFromId(null, "java.util.List<java.lang.String>"));
    }

    @Test
    public void test_typeFromId_class_not_found() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertNull(resolver.typeFromId(new DeserializationContext(), "com.example.FooBar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_typeFromId_invalid_id() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        resolver.typeFromId(null, "some invalid id");
    }
    
    @Test
    public void test_idFromEnumSet() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(EnumSet.noneOf(Enum.class), EnumSet.class, null);
        assertEquals("java.util.EnumSet<java.lang.Enum>", id);
    }

    @Test
    public void test_idFromEnumMap() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(new EnumMap<>(Enum.class), EnumMap.class, null);
        assertEquals("java.util.EnumMap<java.lang.Enum, java.lang.Object>", id);
    }

    @Test
    public void test_idFromValue_enum() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(Enum.FOO, Enum.class, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.Enum", id);
    }

    @Test
    public void test_idFromValue_enum_subclass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(new Enum<Foo>() {}, Enum.class, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.Enum", id);
    }

    @Test
    public void test_idFromValue_regular_class() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(new ClassNameIdResolver(null, null), ClassNameIdResolver.class, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver", id);
    }

    @Test
    public void test_idFromValue_regular_class_inner_class() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(new InnerClass(), InnerClass.class, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver.InnerClass", id);
    }

    @Test
    public void test_getDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }

    // Additional test methods

    @Test
    public void testIdFromValue_null_value() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValue(null);
        assertNull(id);
    }

    @Test
    public void testIdFromValueAndType_null_value() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValueAndType(null, null);
        assertNull(id);
    }

    @Test
    public void testIdFromValueAndType_null_type() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValueAndType(new ClassNameIdResolver(null, null), null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver", id);
    }

    @Test
    public void testIdFromValueAndType_null_value_and_type() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValueAndType(null, null);
        assertNull(id);
    }

    @Test
    public void testIdFromEnumSet_null_typeFactory() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(EnumSet.noneOf(Enum.class), EnumSet.class, null);
        assertEquals("java.util.EnumSet<java.lang.Enum>", id);
    }

    @Test
    public void testIdFromEnumMap_null_typeFactory() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(new EnumMap<>(Enum.class), EnumMap.class, null);
        assertEquals("java.util.EnumMap<java.lang.Enum, java.lang.Object>", id);
    }

    @Test
    public void testIdFromValue_enum_null_typeFactory() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(Enum.FOO, Enum.class, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.Enum", id);
    }

    @Test
    public void testIdFromValue_enum_subclass_null_typeFactory() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(new Enum<Foo>() {}, Enum.class, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.Enum", id);
    }

    @Test
    public void testIdFromValue_regular_class_null_typeFactory() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(new ClassNameIdResolver(null, null), ClassNameIdResolver.class, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver", id);
    }

    @Test
    public void testIdFromValue_regular_class_inner_class_null_typeFactory() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(new InnerClass(), InnerClass.class, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver.InnerClass", id);
    }

    // Inner class for testing purposes
    private static class InnerClass {}
}