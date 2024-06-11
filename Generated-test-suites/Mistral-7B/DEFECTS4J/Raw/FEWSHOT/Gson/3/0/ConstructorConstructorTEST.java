package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

public final class ConstructorConstructorTest {

    @Test
    public void testGetInstanceCreatorForArrayList() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        TypeToken<ArrayList<Integer>> typeToken = new TypeToken<ArrayList<Integer>>() {};
        ObjectConstructor<ArrayList<Integer>> objectConstructor = constructorConstructor.get(typeToken);
        ArrayList<Integer> list = objectConstructor.construct();
        assertNotNull(list);
    }

    @Test
    public void testGetInstanceCreatorForArrayListWithGenericType() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.get(new TypeToken<ArrayList<String>>() {}.getType());
        ArrayList<String> list = objectConstructor.construct();
        assertNotNull(list);
    }

    @Test
    public void testGetInstanceCreatorForLinkedHashMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        TypeToken<LinkedHashMap<String, Object>> typeToken = new TypeToken<LinkedHashMap<String, Object>>() {};
        ObjectConstructor<LinkedHashMap<String, Object>> objectConstructor = constructorConstructor.get(typeToken);
        LinkedHashMap<String, Object> map = objectConstructor.construct();
        assertNotNull(map);
    }

    @Test
    public void testGetInstanceCreatorForTreeMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        TypeToken<TreeMap<Integer, Object>> typeToken = new TypeToken<TreeMap<Integer, Object>>() {};
        ObjectConstructor<TreeMap<Integer, Object>> objectConstructor = constructorConstructor.get(typeToken);
        TreeMap<Integer, Object> map = objectConstructor.construct();
        assertNotNull(map);
    }

    @Test
    public void testGetInstanceCreatorForLinkedList() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        TypeToken<LinkedList<Integer>> typeToken = new TypeToken<LinkedList<Integer>>() {};
        ObjectConstructor<LinkedList<Integer>> objectConstructor = constructorConstructor.get(typeToken);
        LinkedList<Integer> list = objectConstructor.construct();
        assertNotNull(list);
    }

    @Test
    public void testGetInstanceCreatorForLinkedHashSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        TypeToken<LinkedHashSet<Integer>> typeToken = new TypeToken<LinkedHashSet<Integer>>() {};
        ObjectConstructor<LinkedHashSet<Integer>> objectConstructor = constructorConstructor.get(typeToken);
        LinkedHashSet<Integer> set = objectConstructor.construct();
        assertNotNull(set);
    }

    @Test
    public void testGetInstanceCreatorForEnumSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        Type type = new TypeToken<EnumSet<MyEnum>>() {}.getType();
        ObjectConstructor<EnumSet<MyEnum>> objectConstructor = constructorConstructor.get(type);
        EnumSet<MyEnum> set = objectConstructor.construct();
        assertNotNull(set);
    }

    @Test
    public void testGetInstanceCreatorForSortedSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        TypeToken<TreeSet<Integer>> typeToken = new TypeToken<TreeSet<Integer>>() {};
        ObjectConstructor<TreeSet<Integer>> objectConstructor = constructorConstructor.get(typeToken);
        TreeSet<Integer> set = objectConstructor.construct();
        assertNotNull(set);
    }

    @Test
    public void testGetInstanceCreatorForSortedMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        TypeToken<TreeMap<Integer, Object>> typeToken = new TypeToken<TreeMap<Integer, Object>>() {};
        ObjectConstructor<TreeMap<Integer, Object>> objectConstructor = constructorConstructor.get(typeToken);
        TreeMap<Integer, Object> map = objectConstructor.construct();
        assertNotNull(map);
    }

    @Test
    public void testGetInstanceCreatorForCollection() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        TypeToken<Collection<Integer>> typeToken = new TypeToken<Collection<Integer>>() {};
        ObjectConstructor<Collection<Integer>> objectConstructor = constructorConstructor.get(typeToken);
        Collection<Integer> collection = objectConstructor.construct();
        assertNotNull(collection);
    }

    @Test
    public void testGetInstanceCreatorForMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        TypeToken<Map<Integer, Object>> typeToken = new TypeToken<Map<Integer, Object>>() {};
        ObjectConstructor<Map<Integer, Object>> objectConstructor = constructorConstructor.get(typeToken);
        Map<Integer, Object> map = objectConstructor.construct();
        assertNotNull(map);
    }

    @Test
    public void testGetInstanceCreatorForSortedMapWithStringKey() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        TypeToken<TreeMap<String, Object>> typeToken = new TypeToken<TreeMap<String, Object>>() {};
        ObjectConstructor<TreeMap<String, Object>> objectConstructor = constructorConstructor.get(typeToken);
        TreeMap<String, Object> map = objectConstructor.construct();
        assertNotNull(map);
    }

    @Test
    public void testGetInstanceCreatorForEnum() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        Type type = new TypeToken<MyEnum>() {}.getType();
        ObjectConstructor<MyEnum> objectConstructor = constructorConstructor.get(type);
        MyEnum enumValue = objectConstructor.construct();
        assertNotNull(enumValue);
    }

    @Test
    public void testGetInstanceCreatorForClassWithDefaultConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        Type type = String.class.getType();
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(new TypeToken<String>() {}.getType());
        String string = objectConstructor.construct();
        assertNotNull(string);
    }

    @Test
    public void testGetInstanceCreatorForClassWithNoDefaultConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        Type type = new TypeToken<MyClassWithNoDefaultConstructor>() {}.getType();
        ObjectConstructor<MyClassWithNoDefaultConstructor> objectConstructor = constructorConstructor.get(type);
        assertNull(objectConstructor);
    }

    @Test
    public void testGetInstanceCreatorForClassWithPrivateConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        Type type = new TypeToken<MyClassWithPrivateConstructor>() {}.getType();
        ObjectConstructor<MyClassWithPrivateConstructor> objectConstructor = constructorConstructor.get(type);
        assertNull(objectConstructor);
    }

    @Test(expected = RuntimeException.class)
    public void testGetInstanceCreatorUnsupportedType() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        Type type = new TypeToken<Object>() {}.getType();
        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(type);
    }

    @Test
    public void testGetInstanceCreatorForNullType() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(null);
        assertNull(objectConstructor);
    }
}