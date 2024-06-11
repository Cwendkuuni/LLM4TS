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
        // No action needed for class name based resolvers, so nothing to test
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValue("test");
        assertEquals("java.lang.String", id);
    }
    
    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValueAndType("test", String.class);
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        JavaType type = resolver.typeFromId(null, "java.lang.String");
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }
    
    @Test(expected = ClassNotFoundException.class)
    public void testTypeFromIdWithUnknownClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        resolver.typeFromId(null, "com.example.UnknownClass");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidTypeId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        resolver.typeFromId(null, "invalid");
    }
    
    @Test
    public void test_idFrom() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom("test", String.class, null);
        assertEquals("java.lang.String", id);
    }
    
    @Test
    public void test_idFromWithEnumClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(TestEnum.ONE, TestEnum.class, null);
        assertEquals("com.example.TestEnum", id);
    }
    
    @Test
    public void test_idFromWithSubclassOfEnum() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(TestSubclass.ONE, TestSubclass.class, null);
        assertEquals("com.example.TestEnum", id);
    }
    
    @Test
    public void test_idFromWithEnumSet() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(EnumSet.of(TestEnum.ONE), null, null);
        assertEquals("java.util.EnumSet<com.example.TestEnum>", id);
    }
    
    @Test
    public void test_idFromWithEnumMap() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom(new EnumMap<TestEnum, String>(TestEnum.class), null, null);
        assertEquals("java.util.EnumMap<com.example.TestEnum, java.lang.String>", id);
    }
    
    @Test
    public void test_idFromWithUtilClassPrefix() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom("java.util.ArrayList", null, null);
        assertEquals("java.util.ArrayList", id);
    }
    
    @Test
    public void test_idFromWithInnerClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver._idFrom("com.example.Outer$Inner", null, null);
        assertEquals("com.example.Outer", id);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
}

enum TestEnum {
    ONE, TWO, THREE
}

class TestSubclass extends TestEnum {
    // subclass of TestEnum
}