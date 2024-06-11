package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ConstructorConstructorTest {
    private ConstructorConstructor constructorConstructor;
    private TypeToken<String> stringToken;
    private TypeToken<Integer> integerToken;
    private TypeToken<ArrayList<String>> arrayListToken;
    private TypeToken<LinkedHashMap<Integer, String>> linkedHashMapToken;

    @Before
    public void setUp() {
        constructorConstructor = new ConstructorConstructor(null);
        stringToken = new TypeToken<String>() {};
        integerToken = new TypeToken<Integer>() {};
        arrayListToken = new TypeToken<ArrayList<String>>() {};
        linkedHashMapToken = new TypeToken<LinkedHashMap<Integer, String>>() {};
    }

    @Test
    public void testGet_StringToken_ReturnsDefaultConstructor() {
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(stringToken);
        assertNotNull(objectConstructor);
        assertEquals("", objectConstructor.construct());
    }

    @Test
    public void testGet_IntegerToken_ReturnsDefaultConstructor() {
        ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(integerToken);
        assertNotNull(objectConstructor);
        assertEquals(0, objectConstructor.construct().intValue());
    }

    @Test
    public void testGet_ArrayListToken_ReturnsDefaultImplementationConstructor() {
        ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.get(arrayListToken);
        assertNotNull(objectConstructor);
        assertEquals(ArrayList.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testGet_LinkedHashMapToken_ReturnsDefaultImplementationConstructor() {
        ObjectConstructor<LinkedHashMap<Integer, String>> objectConstructor = constructorConstructor.get(linkedHashMapToken);
        assertNotNull(objectConstructor);
        assertEquals(LinkedHashMap.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testGet_SetToken_ReturnsDefaultImplementationConstructor() {
        TypeToken<Set<String>> setToken = new TypeToken<Set<String>>() {};
        ObjectConstructor<Set<String>> objectConstructor = constructorConstructor.get(setToken);
        assertNotNull(objectConstructor);
        assertEquals(LinkedHashSet.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testGet_SortedSetToken_ReturnsDefaultImplementationConstructor() {
        TypeToken<SortedSet<String>> sortedSetToken = new TypeToken<SortedSet<String>>() {};
        ObjectConstructor<SortedSet<String>> objectConstructor = constructorConstructor.get(sortedSetToken);
        assertNotNull(objectConstructor);
        assertEquals(TreeSet.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testGet_EnumSetToken_ReturnsDefaultImplementationConstructor() {
        TypeToken<EnumSet<TestEnum>> enumSetToken = new TypeToken<EnumSet<TestEnum>>() {};
        ObjectConstructor<EnumSet<TestEnum>> objectConstructor = constructorConstructor.get(enumSetToken);
        assertNotNull(objectConstructor);
        assertEquals(EnumSet.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testGet_QueueToken_ReturnsDefaultImplementationConstructor() {
        TypeToken<Queue<String>> queueToken = new TypeToken<Queue<String>>() {};
        ObjectConstructor<Queue<String>> objectConstructor = constructorConstructor.get(queueToken);
        assertNotNull(objectConstructor);
        assertEquals(LinkedList.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testGet_SortedMapToken_ReturnsDefaultImplementationConstructor() {
        TypeToken<SortedMap<Integer, String>> sortedMapToken = new TypeToken<SortedMap<Integer, String>>() {};
        ObjectConstructor<SortedMap<Integer, String>> objectConstructor = constructorConstructor.get(sortedMapToken);
        assertNotNull(objectConstructor);
        assertEquals(TreeMap.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testGet_MapToken_ReturnsDefaultImplementationConstructor() {
        TypeToken<Map<Integer, String>> mapToken = new TypeToken<Map<Integer, String>>() {};
        ObjectConstructor<Map<Integer, String>> objectConstructor = constructorConstructor.get(mapToken);
        assertNotNull(objectConstructor);
        assertEquals(LinkedHashMap.class, objectConstructor.construct().getClass());
    }

    @Test
    public void testGet_DefaultConstructorNotFound_ReturnsNull() {
        TypeToken<TestClass> testClassToken = new TypeToken<TestClass>() {};
        ObjectConstructor<TestClass> objectConstructor = constructorConstructor.get(testClassToken);
        assertNull(objectConstructor);
    }

    private static class TestEnum { }

    private static class TestClass { }
}