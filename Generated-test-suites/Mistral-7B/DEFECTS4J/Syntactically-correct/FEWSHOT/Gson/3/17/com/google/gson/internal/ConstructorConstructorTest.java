package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

import org.junit.Test;

public class ConstructorConstructorTest {

    @Test
    public void testGet_Collection_ArrayList() {
        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<ArrayList<Object>> objectConstructor = constructor.get(new TypeToken<ArrayList<Object>>() {}.getType());
        assertNotNull(objectConstructor);
        assertInstanceOf(ArrayList.class, objectConstructor.construct());
    }

    @Test
    public void testGet_Collection_LinkedHashSet() {
        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<LinkedHashSet<Object>> objectConstructor = constructor.get(new TypeToken<LinkedHashSet<Object>>() {}.getType());
        assertNotNull(objectConstructor);
        assertInstanceOf(LinkedHashSet.class, objectConstructor.construct());
    }

    @Test
    public void testGet_Collection_TreeSet() {
        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<TreeSet<Object>> objectConstructor = constructor.get(new TypeToken<TreeSet<Object>>() {}.getType());
        assertNotNull(objectConstructor);
        assertInstanceOf(TreeSet.class, objectConstructor.construct());
    }

    @Test
    public void testGet_Collection_EnumSet() {
        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<EnumSet<Object>> objectConstructor = constructor.get(new TypeToken<EnumSet<Object>>() {}.getType());
        assertNotNull(objectConstructor);
        assertInstanceOf(EnumSet.class, objectConstructor.construct());
    }

    @Test
    public void testGet_Collection_LinkedList() {
        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<LinkedList<Object>> objectConstructor = constructor.get(new TypeToken<LinkedList<Object>>() {}.getType());
        assertNotNull(objectConstructor);
        assertInstanceOf(LinkedList.class, objectConstructor.construct());
    }

    @Test
    public void testGet_Collection_ArrayListWithGenericType() {
        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<ArrayList<String>> objectConstructor = constructor.get(new TypeToken<ArrayList<String>>() {}.getType());
        assertNotNull(objectConstructor);
        assertInstanceOf(ArrayList.class, objectConstructor.construct());
        assertInstanceOf(ArrayList.class, ((ArrayList<?>) objectConstructor.construct()).getClass());
        assertInstanceOf(String.class, ((ArrayList<?>) objectConstructor.construct()).get(0).getClass());
    }

    @Test
    public void testGet_Collection_LinkedHashSetWithGenericType() {
        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<LinkedHashSet<String>> objectConstructor = constructor.get(new TypeToken<LinkedHashSet<String>>() {}.getType());
        assertNotNull(objectConstructor);
        assertInstanceOf(LinkedHashSet.class, objectConstructor.construct());
        assertInstanceOf(LinkedHashSet.class, ((LinkedHashSet<?>) objectConstructor.construct()).getClass());
        assertInstanceOf(String.class, ((LinkedHashSet<?>) objectConstructor.construct()).iterator().next().getClass());
    }

    @Test
    public void testGet_Map_LinkedHashMap() {
        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<LinkedHashMap<String, Object>> objectConstructor = constructor.get(new TypeToken<LinkedHashMap<String, Object>>() {}.getType());
        assertNotNull(objectConstructor);
        assertInstanceOf(LinkedHashMap.class, objectConstructor.construct());
    }

    @Test
    public void testGet_Map_TreeMap() {
        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<TreeMap<String, Object>> objectConstructor = constructor.get(new TypeToken<TreeMap<String, Object>>() {}.getType());
        assertNotNull(objectConstructor);
        assertInstanceOf(TreeMap.class, objectConstructor.construct());
    }

    @Test
    public void testGet_Map_HashMap() {
        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<HashMap<String, Object>> objectConstructor = constructor.get(new TypeToken<HashMap<String, Object>>() {}.getType());
        assertNotNull(objectConstructor);
        assertInstanceOf(HashMap.class, objectConstructor.construct());
    }

    @Test
    public void testGet_Map_HashMapWithGenericType() {
        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<HashMap<String, String>> objectConstructor = constructor.get(new TypeToken<HashMap<String, String>>() {}.getType());
        assertNotNull(objectConstructor);
        assertInstanceOf(HashMap.class, objectConstructor.construct());
        assertInstanceOf(HashMap.class, ((HashMap<?, ?>) objectConstructor.construct()).getClass());
        assertInstanceOf(String.class, ((HashMap<?, ?>) objectConstructor.construct()).keySet().getClass());
        assertInstanceOf(String.class, ((HashMap<?, ?>) objectConstructor.construct()).values().getClass());
        assertInstanceOf(String.class, ((HashMap<?, ?>) objectConstructor.construct()).get("key").getClass());
    }

    @Test
    public void testGet_Null() {
        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<Object> objectConstructor = constructor.get(null);
        assertNull(objectConstructor);
    }

    @Test(expected = RuntimeException.class)
    public void testGet_UnsupportedType() {
        ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<Integer> objectConstructor = constructor.get(new TypeToken<Integer>() {}.getType());
        assertNotNull(objectConstructor);
        objectConstructor.construct();
    }
}