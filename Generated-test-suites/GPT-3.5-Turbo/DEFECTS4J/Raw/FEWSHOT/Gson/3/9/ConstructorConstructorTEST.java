package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;

public class ConstructorConstructorTest {
    private ConstructorConstructor constructor;

    @Before
    public void setUp() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        constructor = new ConstructorConstructor(instanceCreators);
    }

    @Test
    public void testGet_InstanceCreator() {
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        ObjectConstructor<Object> objectConstructor = constructor.get(typeToken);

        assertNull(objectConstructor);
    }

    @Test
    public void testGet_RawTypeCreator() {
        TypeToken<ArrayList<Object>> typeToken = new TypeToken<ArrayList<Object>>() {};
        ObjectConstructor<ArrayList<Object>> objectConstructor = constructor.get(typeToken);

        assertNull(objectConstructor);
    }

    @Test
    public void testGet_DefaultConstructor() {
        TypeToken<String> typeToken = TypeToken.get(String.class);
        ObjectConstructor<String> objectConstructor = constructor.get(typeToken);

        assertNotNull(objectConstructor);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Collection() {
        TypeToken<Collection<Object>> typeToken = new TypeToken<Collection<Object>>() {};
        ObjectConstructor<Collection<Object>> objectConstructor = constructor.get(typeToken);

        assertNotNull(objectConstructor);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_SortedSet() {
        TypeToken<SortedSet<Object>> typeToken = new TypeToken<SortedSet<Object>>() {};
        ObjectConstructor<SortedSet<Object>> objectConstructor = constructor.get(typeToken);

        assertNotNull(objectConstructor);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_EnumSet() {
        TypeToken<EnumSet<Object>> typeToken = new TypeToken<EnumSet<Object>>() {};
        ObjectConstructor<EnumSet<Object>> objectConstructor = constructor.get(typeToken);

        assertNotNull(objectConstructor);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Set() {
        TypeToken<Set<Object>> typeToken = new TypeToken<Set<Object>>() {};
        ObjectConstructor<Set<Object>> objectConstructor = constructor.get(typeToken);

        assertNotNull(objectConstructor);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Queue() {
        TypeToken<Queue<Object>> typeToken = new TypeToken<Queue<Object>>() {};
        ObjectConstructor<Queue<Object>> objectConstructor = constructor.get(typeToken);

        assertNotNull(objectConstructor);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Map() {
        TypeToken<Map<Object, Object>> typeToken = new TypeToken<Map<Object, Object>>() {};
        ObjectConstructor<Map<Object, Object>> objectConstructor = constructor.get(typeToken);

        assertNotNull(objectConstructor);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_SortedMap() {
        TypeToken<SortedMap<Object, Object>> typeToken = new TypeToken<SortedMap<Object, Object>>() {};
        ObjectConstructor<SortedMap<Object, Object>> objectConstructor = constructor.get(typeToken);

        assertNotNull(objectConstructor);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_LinkedHashMap() {
        TypeToken<Map<String, Object>> typeToken = new TypeToken<Map<String, Object>>() {};
        ObjectConstructor<Map<String, Object>> objectConstructor = constructor.get(typeToken);

        assertNotNull(objectConstructor);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_LinkedTreeMap() {
        TypeToken<Map<Object, Object>> typeToken = new TypeToken<Map<Object, Object>>() {};
        ObjectConstructor<Map<Object, Object>> objectConstructor = constructor.get(typeToken);

        assertNotNull(objectConstructor);
    }

    @Test
    public void testGet_UnsafeAllocator() {
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        ObjectConstructor<Object> objectConstructor = constructor.get(typeToken);

        assertNotNull(objectConstructor);
    }
}