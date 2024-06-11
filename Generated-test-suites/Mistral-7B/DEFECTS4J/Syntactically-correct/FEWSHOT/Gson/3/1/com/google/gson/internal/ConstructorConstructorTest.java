package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

    @Test
    public void testGetInstanceCreator_Collection_ArrayList() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(TypeToken.get(ArrayList.class));
        assertNotNull(constructor);
        assertEquals(ArrayList.class, constructor.getType());
        assertNotNull(constructor.construct());
    }

    @Test
    public void testGetInstanceCreator_Collection_LinkedHashSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        ObjectConstructor<LinkedHashSet<String>> constructor = constructorConstructor.get(TypeToken.get(LinkedHashSet.class));
        assertNotNull(constructor);
        assertEquals(LinkedHashSet.class, constructor.getType());
        assertNotNull(constructor.construct());
    }

    @Test
    public void testGetInstanceCreator_Collection_TreeSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        ObjectConstructor<TreeSet<String>> constructor = constructorConstructor.get(TypeToken.get(TreeSet.class));
        assertNotNull(constructor);
        assertEquals(TreeSet.class, constructor.getType());
        assertNotNull(constructor.construct());
    }

    @Test
    public void testGetInstanceCreator_Collection_EnumSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        ObjectConstructor<EnumSet<String>> constructor = constructorConstructor.get(TypeToken.get(EnumSet.class));
        assertNotNull(constructor);
        assertEquals(EnumSet.class, constructor.getType());
        assertNotNull(constructor.construct());
    }

    @Test
    public void testGetInstanceCreator_Collection_LinkedList() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        ObjectConstructor<LinkedList<String>> constructor = constructorConstructor.get(TypeToken.get(LinkedList.class));
        assertNotNull(constructor);
        assertEquals(LinkedList.class, constructor.getType());
        assertNotNull(constructor.construct());
    }

    @Test
    public void testGetInstanceCreator_Collection_ArrayList_Of_Class() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(TypeToken.get(ArrayList.class, String.class));
        assertNotNull(constructor);
        assertEquals(ArrayList.class, constructor.getType());
        assertNotNull(constructor.construct());
    }

    @Test
    public void testGetInstanceCreator_Map_LinkedHashMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        ObjectConstructor<LinkedHashMap<String, String>> constructor = constructorConstructor.get(TypeToken.get(LinkedHashMap.class));
        assertNotNull(constructor);
        assertEquals(LinkedHashMap.class, constructor.getType());
        assertNotNull(constructor.construct());
    }

    @Test
    public void testGetInstanceCreator_Map_TreeMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        ObjectConstructor<TreeMap<String, String>> constructor = constructorConstructor.get(TypeToken.get(TreeMap.class));
        assertNotNull(constructor);
        assertEquals(TreeMap.class, constructor.getType());
        assertNotNull(constructor.construct());
    }

    @Test
    public void testGetInstanceCreator_Map_SortedMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        ObjectConstructor<SortedMap<String, String>> constructor = constructorConstructor.get(TypeToken.get(SortedMap.class));
        assertNotNull(constructor);
        assertEquals(SortedMap.class, constructor.getType());
        assertNotNull(constructor.construct());
    }

    @Test
    public void testGetInstanceCreator_Map_LinkedTreeMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        ObjectConstructor<LinkedTreeMap<String, Object>> constructor = constructorConstructor.get(TypeToken.get(LinkedTreeMap.class));
        assertNotNull(constructor);
        assertEquals(LinkedTreeMap.class, constructor.getType());
        assertNotNull(constructor.construct());
    }

    @Test
    public void testGetInstanceCreator_UnknownType() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        ObjectConstructor<String> constructor = constructorConstructor.get(TypeToken.get(String.class));
        assertNull(constructor);
    }

    @Test(expected = RuntimeException.class)
    public void testGetInstanceCreator_NoDefaultConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        ObjectConstructor<Object> constructor = constructorConstructor.get(TypeToken.get(Object.class));
        constructor.construct();
    }
}