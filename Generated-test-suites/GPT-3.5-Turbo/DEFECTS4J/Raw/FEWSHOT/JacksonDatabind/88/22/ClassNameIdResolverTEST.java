package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
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
        // As the method is not used, there are no test cases to write.
        // The method can simply be left untested.
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // Create test objects for different classes and validate the generated ID
        assertEquals("java.util.ArrayList", resolver.idFromValue(new java.util.ArrayList<>()));
        assertEquals("java.util.HashMap", resolver.idFromValue(new java.util.HashMap<>()));
        assertEquals("java.util.HashSet", resolver.idFromValue(new java.util.HashSet<>()));
        assertEquals("java.util.LinkedList", resolver.idFromValue(new java.util.LinkedList<>()));
        assertEquals("java.util.PriorityQueue", resolver.idFromValue(new java.util.PriorityQueue<>()));
        assertEquals("java.util.Stack", resolver.idFromValue(new java.util.Stack<>()));
        assertEquals("java.util.TreeMap", resolver.idFromValue(new java.util.TreeMap<>()));
        assertEquals("java.util.TreeSet", resolver.idFromValue(new java.util.TreeSet<>()));
        assertEquals("java.util.Vector", resolver.idFromValue(new java.util.Vector<>()));
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // Create test objects for different classes and validate the generated ID
        assertEquals("java.util.ArrayList", resolver.idFromValueAndType(new java.util.ArrayList<>(), java.util.ArrayList.class));
        assertEquals("java.util.HashMap", resolver.idFromValueAndType(new java.util.HashMap<>(), java.util.HashMap.class));
        assertEquals("java.util.HashSet", resolver.idFromValueAndType(new java.util.HashSet<>(), java.util.HashSet.class));
        assertEquals("java.util.LinkedList", resolver.idFromValueAndType(new java.util.LinkedList<>(), java.util.LinkedList.class));
        assertEquals("java.util.PriorityQueue", resolver.idFromValueAndType(new java.util.PriorityQueue<>(), java.util.PriorityQueue.class));
        assertEquals("java.util.Stack", resolver.idFromValueAndType(new java.util.Stack<>(), java.util.Stack.class));
        assertEquals("java.util.TreeMap", resolver.idFromValueAndType(new java.util.TreeMap<>(), java.util.TreeMap.class));
        assertEquals("java.util.TreeSet", resolver.idFromValueAndType(new java.util.TreeSet<>(), java.util.TreeSet.class));
        assertEquals("java.util.Vector", resolver.idFromValueAndType(new java.util.Vector<>(), java.util.Vector.class));
    }

    @Test
    public void testTypeFromId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // Create test ID strings for different classes and validate the generated JavaType
        assertEquals(new com.fasterxml.jackson.databind.type.CollectionType(resolver._typeFactory, java.util.List.class, null), resolver.typeFromId(null, "java.util.List"));
        assertEquals(new com.fasterxml.jackson.databind.type.CollectionType(resolver._typeFactory, java.util.Map.class, null), resolver.typeFromId(null, "java.util.Map"));
        assertEquals(new com.fasterxml.jackson.databind.type.CollectionType(resolver._typeFactory, java.util.Set.class, null), resolver.typeFromId(null, "java.util.Set"));
        assertEquals(new com.fasterxml.jackson.databind.type.CollectionType(resolver._typeFactory, java.util.Queue.class, null), resolver.typeFromId(null, "java.util.Queue"));
        assertEquals(new com.fasterxml.jackson.databind.type.CollectionType(resolver._typeFactory, java.util.Deque.class, null), resolver.typeFromId(null, "java.util.Deque"));
        assertEquals(new com.fasterxml.jackson.databind.type.CollectionType(resolver._typeFactory, java.util.SortedSet.class, null), resolver.typeFromId(null, "java.util.SortedSet"));
        assertEquals(new com.fasterxml.jackson.databind.type.CollectionType(resolver._typeFactory, java.util.SortedMap.class, null), resolver.typeFromId(null, "java.util.SortedMap"));
        assertEquals(new com.fasterxml.jackson.databind.type.CollectionType(resolver._typeFactory, java.util.Collection.class, null), resolver.typeFromId(null, "java.util.Collection"));
        assertEquals(new com.fasterxml.jackson.databind.type.ResolvedRecursiveType(resolver._typeFactory, com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver.class), resolver.typeFromId(null, "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver"));
    }

    @Test
    public void test_typeFromId_withDeserializationContext() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // Create test ID strings for different classes and validate the generated JavaType
        try {
            resolver.typeFromId(new com.fasterxml.jackson.databind.deser.DeserializationContext(null, null), "java.util.List");
        } catch (java.io.IOException e) {
            assertEquals("no such class found", e.getMessage());
        }
    }

    @Test
    public void test_typeFromId_withException() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // Create test ID strings for different classes and validate the generated JavaType
        try {
            resolver.typeFromId(new com.fasterxml.jackson.databind.deser.DeserializationContext(null, null), "");
        } catch (java.lang.Exception e) {
            assertEquals("Invalid type id '' (for id type 'Id.class'): null", e.getMessage());
        }
    }

    @Test
    public void test_typeFromId_withClassNotFoundException() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // Create test ID strings for different classes and validate the generated JavaType
        try {
            resolver.typeFromId(null, "com.fasterxml.jackson.databind.jsontype.impl.DoesNotExist");
        } catch (java.lang.IllegalArgumentException e) {
            assertEquals("Invalid type id 'com.fasterxml.jackson.databind.jsontype.impl.DoesNotExist' (for id type 'Id.class'): null", e.getMessage());
        }
    }

    @Test
    public void test_typeFromId_withUnknownTypeId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // Create test ID strings for different classes and validate the generated JavaType
        try {
            resolver.typeFromId(null, "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver");
        } catch (java.lang.IllegalArgumentException e) {
            assertEquals("Invalid type id 'com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver' (for id type 'Id.class'): no such class found", e.getMessage());
        }
    }

    @Test
    public void test_typeFromId_withNestedClass() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // Create test ID strings for different classes and validate the generated JavaType
        assertEquals(new com.fasterxml.jackson.databind.type.ResolvedRecursiveType(resolver._typeFactory, java.util.Collections.Nested.class), resolver.typeFromId(null, "java.util.Collections.Nested"));
    }

    @Test
    public void test_typeFromId_withNestedStaticClass() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // Create test ID strings for different classes and validate the generated JavaType
        ClassNameIdResolverTest.InnerSubClass innerSubClass = new ClassNameIdResolverTest.InnerSubClass();
        assertEquals(new com.fasterxml.jackson.databind.type.CollectionType(resolver._typeFactory, java.util.Collection.class, null), resolver.typeFromId(null, innerSubClass.getClass().getName()));
    }

    @Test
    public void test_idFrom() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // Create test objects for different classes and validate the generated ID
        Object object = null;
        assertEquals("java.util.A $Coll", resolver._idFrom(object, java.util.A.Coll.class, resolver._typeFactory));
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }

    // test inner class
    static class InnerSubClass {
    }

}