package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

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

import org.junit.Test;

public class ConstructorConstructorTest {

    @Test
    public void testGetInstanceCreatorForMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        Type typeToken = new TypeToken<TreeMap<String, Object>>(){}.getType();
        ObjectConstructor<TreeMap<String, Object>> instance = constructorConstructor.get(typeToken);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceCreatorForArrayList() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new ArrayList<>());
        Type typeToken = new TypeToken<ArrayList<Object>>(){}.getType();
        ObjectConstructor<ArrayList<Object>> instance = constructorConstructor.get(typeToken);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceCreatorForLinkedHashSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashSet<>());
        Type typeToken = new TypeToken<LinkedHashSet<Object>>(){}.getType();
        ObjectConstructor<LinkedHashSet<Object>> instance = constructorConstructor.get(typeToken);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceCreatorForTreeSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new TreeSet<>());
        Type typeToken = new TypeToken<TreeSet<Object>>(){}.getType();
        ObjectConstructor<TreeSet<Object>> instance = constructorConstructor.get(typeToken);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceCreatorForLinkedList() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedList<>());
        Type typeToken = new TypeToken<LinkedList<Object>>(){}.getType();
        ObjectConstructor<LinkedList<Object>> instance = constructorConstructor.get(typeToken);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceCreatorForTreeMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new TreeMap<>());
        Type typeToken = new TypeToken<TreeMap<Object, Object>>(){}.getType();
        ObjectConstructor<TreeMap<Object, Object>> instance = constructorConstructor.get(typeToken);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceCreatorForEnumSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(EnumSet.noneOf(String.class));
        Type typeToken = new TypeToken<EnumSet<String>>(){}.getType();
        ObjectConstructor<EnumSet<String>> instance = constructorConstructor.get(typeToken);
        assertNotNull(instance);
    }

    @Test
    public void testGetNullInstanceCreatorForNullTypeToken() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<Object> typeToken = null;
        ObjectConstructor<Object> instance = constructorConstructor.get(typeToken);
        assertNull(instance);
    }

    @Test
    public void testGetInstanceCreatorForUnknownType() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        Type typeToken = new TypeToken<UnknownType>(){}.getType();
        ObjectConstructor<UnknownType> instance = constructorConstructor.get(typeToken);
        assertNull(instance);
    }
}