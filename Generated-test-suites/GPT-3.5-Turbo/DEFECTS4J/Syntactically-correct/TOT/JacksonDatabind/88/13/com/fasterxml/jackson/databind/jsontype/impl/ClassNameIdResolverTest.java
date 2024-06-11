package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        resolver.registerSubtype(ClassA.class, "A");
        resolver.registerSubtype(ClassB.class, "B");
        // Verify the registration is successful
        assertNotNull(resolver.getTypeIdForClass(ClassA.class));
        assertNotNull(resolver.getTypeIdForClass(ClassB.class));
    }
    
    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        ClassA classA = new ClassA();
        String id = resolver.idFromValue(classA);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassA", id);
    }
    
    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        ClassA classA = new ClassA();
        String id = resolver.idFromValueAndType(classA, ClassA.class);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassA", id);
    }
    
    @Test
    public void testTypeFromId_success() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        DatabindContext context = new DeserializationContext();
        Class<?> expectedClass = ClassA.class;
        String id = "com.fasterxml.jackson.databind.jsontype.impl.ClassA";
        
        JavaType result = resolver.typeFromId(context, id);
        
        assertTrue(result instanceof JavaType);
        assertEquals(expectedClass, result.getRawClass());
    }
    
    @Test(expected = IOException.class)
    public void testTypeFromId_failure() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        DatabindContext context = new DeserializationContext();
        String id = "com.fasterxml.jackson.databind.jsontype.impl.UnknownClass";
        
        resolver.typeFromId(context, id);
    }
    
    @Test
    public void test_typeFromId_withGenerics() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        DatabindContext context = new DeserializationContext();
        Class<?> expectedClass = SubClassA.class;
        String id = "com.fasterxml.jackson.databind.jsontype.impl.SubClassA<java.lang.String>";
        
        JavaType result = resolver._typeFromId(id, context);
        
        assertTrue(result instanceof JavaType);
        assertEquals(expectedClass, result.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test_typeFromId_invalidId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        DatabindContext context = new DeserializationContext();
        String id = "com.fasterxml.jackson.databind.jsontype.impl.InvalidClass<>";
        
        resolver._typeFromId(id, context);
    }
    
    @Test
    public void test_idFrom_success() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        ClassA classA = new ClassA();
        String expectedId = "com.fasterxml.jackson.databind.jsontype.impl.ClassA";
        
        String result = resolver._idFrom(classA, classA.getClass(), resolver.getTypeFactory());
        
        assertEquals(expectedId, result);
    }
    
    @Test
    public void test_idFrom_enumSet() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        EnumSet<EnumA> enumSet = EnumSet.of(EnumA.VALUE1, EnumA.VALUE2);
        String expectedId = "java.util.EnumSet<com.fasterxml.jackson.databind.jsontype.impl.EnumA>";
        
        String result = resolver._idFrom(enumSet, enumSet.getClass(), resolver.getTypeFactory());
        
        assertEquals(expectedId, result);
    }
    
    @Test
    public void test_idFrom_enumMap() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        EnumMap<EnumA, String> enumMap = new EnumMap<>(EnumA.class);
        enumMap.put(EnumA.VALUE1, "Value 1");
        enumMap.put(EnumA.VALUE2, "Value 2");
        String expectedId = "java.util.EnumMap<com.fasterxml.jackson.databind.jsontype.impl.EnumA, java.lang.Object>";
        
        String result = resolver._idFrom(enumMap, enumMap.getClass(), resolver.getTypeFactory());
        
        assertEquals(expectedId, result);
    }
    
    @Test
    public void test_idFrom_innerClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        InnerClass innerClass = new InnerClass();
        String expectedId = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest.InnerClass";
        
        String result = resolver._idFrom(innerClass, innerClass.getClass(), resolver.getTypeFactory());
        
        assertEquals(expectedId, result);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }

    private static class ClassA {}

    private static class ClassB {}

    private enum EnumA {
        VALUE1, VALUE2
    }

    private static class SubClassA<T> extends ClassA {}

    private static class InnerClass {}
}