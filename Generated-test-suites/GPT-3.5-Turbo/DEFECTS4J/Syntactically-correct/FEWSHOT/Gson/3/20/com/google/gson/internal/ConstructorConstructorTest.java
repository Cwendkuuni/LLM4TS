package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
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
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

public class ConstructorConstructorTest {
    @Test
    public void testGet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<String> stringTypeToken = new TypeToken<String>() {};
        ObjectConstructor<String> result = constructorConstructor.get(stringTypeToken);
        assertNotNull(result);
    }

    @Test
    public void testNewDefaultConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        ObjectConstructor<String> result = constructorConstructor.newDefaultConstructor(String.class);
        assertNull(result);
    }

    @Test
    public void testNewDefaultImplementationConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<LinkedHashMap<String, Object>> linkedHashMapTypeToken = new TypeToken<LinkedHashMap<String, Object>>() {};
        ObjectConstructor<LinkedHashMap<String, Object>> result = constructorConstructor.newDefaultImplementationConstructor(linkedHashMapTypeToken.getType(), LinkedHashMap.class);
        assertNotNull(result);
    }

    @Test
    public void testNewUnsafeAllocator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<String> stringTypeToken = new TypeToken<String>() {};
        ObjectConstructor<String> result = constructorConstructor.newUnsafeAllocator(stringTypeToken.getType(), String.class);
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        String result = constructorConstructor.toString();
        assertNotNull(result);
    }
}