package com.google.gson.internal;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

    @Test
    public void testGetInstanceWithInstanceCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        ObjectConstructor<String> stringConstructor = constructorConstructor.get(TypeToken.of(String.class));
        assertEquals("", stringConstructor.construct());

        instanceCreators.put(TypeToken.of(String.class), new StringInstanceCreator());
        stringConstructor = constructorConstructor.get(TypeToken.of(String.class));
        assertEquals("Test", stringConstructor.construct());

        instanceCreators.remove(TypeToken.of(String.class));
    }

    @Test
    public void testGetInstanceWithRawTypeInstanceCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        ObjectConstructor<ArrayList<Integer>> arrayListConstructor = constructorConstructor.get(TypeToken.of(ArrayList.class));
        assertNotNull(arrayListConstructor);
        assertTrue(arrayListConstructor instanceof ObjectConstructor);
        assertEquals(ArrayList.class, arrayListConstructor.construct().getClass());

        instanceCreators.put(Integer.class, new IntegerInstanceCreator());
        ObjectConstructor<ArrayList<Integer>> newArrayListConstructor = constructorConstructor.get(TypeToken.of(ArrayList.class));
        ArrayList<Integer> list = newArrayListConstructor.construct();
        assertEquals(0, list.size());
        list.add(1);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).intValue());

        instanceCreators.remove(Integer.class);
    }

    @Test
    public void testGetInstanceWithDefaultConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        ObjectConstructor<LinkedHashMap<String, Object>> linkedHashMapConstructor = constructorConstructor.get(
            TypeToken.getParameterized(LinkedHashMap.class, String.class, Object.class));
        assertNotNull(linkedHashMapConstructor);
        assertTrue(linkedHashMapConstructor instanceof ObjectConstructor);
        assertEquals(LinkedHashMap.class, linkedHashMapConstructor.construct().getClass());

        instanceCreators.clear();
    }

    @Test
    public void testGetInstanceWithDefaultImplementationConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        ObjectConstructor<SortedSet<String>> sortedSetConstructor = constructorConstructor.get(
            TypeToken.getParameterized(SortedSet.class, String.class));
        assertNotNull(sortedSetConstructor);
        assertTrue(sortedSetConstructor instanceof ObjectConstructor);
        assertEquals(TreeSet.class, sortedSetConstructor.construct().getClass());

        instanceCreators.put(String.class, new StringInstanceCreator());
        ObjectConstructor<SortedSet<String>> treeSetConstructor = constructorConstructor.get(
            TypeToken.getParameterized(SortedSet.class, String.class));
        SortedSet<String> set = treeSetConstructor.construct();
        assertNotNull(set);
        assertTrue(set instanceof TreeSet);
        assertTrue(set.isEmpty());

        instanceCreators.remove(String.class);
    }

    @Test(expected = RuntimeException.class)
    public void testGetInstanceWithUnsupportedType() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(TypeToken.of(Object.class));
        assertTrue(objectConstructor == null);
    }

    @Test(expected = RuntimeException.class)
    public void testGetInstanceWithUnsafeAllocator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        ObjectConstructor<String> stringConstructor = constructorConstructor.get(TypeToken.of(String.class));
        assertNull(stringConstructor);

        instanceCreators.put(String.class, new StringInstanceCreator());
        stringConstructor = constructorConstructor.get(TypeToken.of(String.class));
        assertNotNull(stringConstructor);

        instanceCreators.clear();

        String string = constructorConstructor.get(TypeToken.of(String.class)).construct();
        assertNull(string);
    }

    private static class StringInstanceCreator implements InstanceCreator<String> {
        @Override public String createInstance(Type type) {
            return "";
        }
    }

    private static class IntegerInstanceCreator implements InstanceCreator<Integer> {
        @Override public Integer createInstance(Type type) {
            return 0;
        }
    }

}