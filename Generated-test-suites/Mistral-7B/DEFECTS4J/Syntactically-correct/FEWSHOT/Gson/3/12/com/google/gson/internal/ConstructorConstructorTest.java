package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.LinkedTreeMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ConstructorConstructorTest {

    @Test
    public void testGetInstanceWithNullType() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertNull(constructorConstructor.get(null));
    }

    @Test
    public void testGetInstanceWithEmptyType() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertNull(constructorConstructor.get(TypeToken.get(void.class)));
    }

    @Test
    public void testGetInstanceWithInstanceCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        InstanceCreator<String> stringCreator = new InstanceCreator<String>() {
            @Override
            public String createInstance(Type type) {
                return "default";
            }
        };
        instanceCreators.put(String.class, stringCreator);

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<String> result = constructorConstructor.get(TypeToken.get(String.class));
        assertEquals("default", result.construct());
    }

    @Test
    public void testGetInstanceWithRawTypeInstanceCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        InstanceCreator<Object> objectCreator = new InstanceCreator<Object>() {
            @Override
            public Object createInstance(Type type) {
                return "default";
            }
        };
        instanceCreators.put(Object.class, objectCreator);

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<String> result = constructorConstructor.get(TypeToken.get(String.class));
        assertEquals("default", result.construct());
    }

    @Test
    public void testGetInstanceWithDefaultConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<String> result = constructorConstructor.get(TypeToken.get(String.class));
        assertInstanceOf(String.class, result.construct());
    }

    @Test
    public void testGetInstanceWithDefaultImplementationConstructorForMap() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<Map<String, Object>> result = constructorConstructor.get(TypeToken.get(Map.class));
        assertInstanceOf(LinkedTreeMap.class, result.construct());
    }

    @Test
    public void testGetInstanceWithDefaultImplementationConstructorForCollection() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<Collection<String>> result = constructorConstructor.get(TypeToken.get(Collection.class));
        assertInstanceOf(LinkedHashSet.class, result.construct());
    }

    @Test
    public void testGetInstanceWithDefaultImplementationConstructorForEnumSet() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<EnumSet<MyEnum>> result = constructorConstructor.get(TypeToken.get(EnumSet.class));
        assertInstanceOf(EnumSet.class, result.construct());
    }

    @Test(expected = JsonIOException.class)
    public void testGetInstanceWithInvalidEnumSetType() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<EnumSet<String>> result = constructorConstructor.get(TypeToken.get(EnumSet.class));
        result.construct();
    }

    @Test
    public void testGetInstanceWithUnsafeAllocator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<String> result = constructorConstructor.get(TypeToken.get(String.class));
        assertInstanceOf(String.class, result.construct());
    }
}