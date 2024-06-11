package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ConstructorConstructorTest {

    @SuppressWarnings("unchecked") // types must agree
    private static <T> Constructor<T> getConstructor(final Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to get constructor for " + clazz.getName(), e);
        }
    }

    private static <T> T newInstance(final Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to instantiate " + clazz.getName(), e);
        }
    }

    private static <T> T newInstanceWithArgs(final Class<T> clazz, Object... args) {
        try {
            Constructor<T> constructor = getConstructor(clazz);
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate " + clazz.getName() + " with arguments " + args, e);
        }
    }

    @Test
    public void testGetInstanceCreatorInstance() {
        final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        final Type intType = new TypeToken<Integer>() {}.getType();
        final InstanceCreator<Integer> instanceCreator = i -> i;
        instanceCreators.put(intType, instanceCreator);

        final ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        final ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(new TypeToken<Integer>() {}.getType());
        assertEquals(instanceCreator.createInstance(intType), objectConstructor.construct());
    }

    @Test
    public void testGetInstanceCreatorRawType() {
        final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        final Type intType = new TypeToken<Integer>() {}.getType();
        final InstanceCreator<Integer> instanceCreator = i -> i;
        instanceCreators.put(int.class, instanceCreator);

        final ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        final ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(intType);
        assertEquals(instanceCreator.createInstance(intType), objectConstructor.construct());
    }

    @Test
    public void testGetDefaultConstructor() {
        final Class<String> stringClass = String.class;
        final ObjectConstructor<String> objectConstructor = new ConstructorConstructor(null).get(new TypeToken<String>() {}.getType());
        assertEquals(newInstance(stringClass), objectConstructor.construct());
    }

    @Test
    public void testGetDefaultImplementationConstructorMap() {
        final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        final Class<TreeMap<String, Object>> treeMapClass = TreeMap.class;
        instanceCreators.put(treeMapClass, () -> new TreeMap<String, Object>());

        final ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        final ObjectConstructor<TreeMap<String, Object>> objectConstructor = constructorConstructor.get(new TypeToken<TreeMap<String, Object>>() {}.getType());
        assertEquals(newInstance(treeMapClass), objectConstructor.construct());
    }

    @Test
    public void testGetDefaultImplementationConstructorSet() {
        final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        final Class<LinkedHashSet<Object>> linkedHashSetClass = LinkedHashSet.class;
        instanceCreators.put(Set.class, () -> new LinkedHashSet<Object>());

        final ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        final ObjectConstructor<LinkedHashSet<Object>> objectConstructor = constructorConstructor.get(new TypeToken<LinkedHashSet<Object>>() {}.getType());
        assertEquals(newInstance(linkedHashSetClass), objectConstructor.construct());
    }

    @Test
    public void testGetDefaultImplementationConstructorQueue() {
        final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        final Class<LinkedList<Object>> linkedListClass = LinkedList.class;
        instanceCreators.put(Queue.class, () -> new LinkedList<Object>());

        final ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        final ObjectConstructor<LinkedList<Object>> objectConstructor = constructorConstructor.get(new TypeToken<LinkedList<Object>>() {}.getType());
        assertEquals(newInstance(linkedListClass), objectConstructor.construct());
    }

    @Test
    public void testGetDefaultImplementationConstructorCollection() {
        final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        final Class<ArrayList<Object>> arrayListClass = ArrayList.class;
        instanceCreators.put(Collection.class, () -> new ArrayList<Object>());

        final ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        final ObjectConstructor<ArrayList<Object>> objectConstructor = constructorConstructor.get(new TypeToken<ArrayList<Object>>() {}.getType());
        assertEquals(newInstance(arrayListClass), objectConstructor.construct());
    }

    @Test
    public void testGetDefaultImplementationConstructorEnumSet() {
        final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        final Class<EnumSet<String>> enumSetClass = EnumSet.class;
        instanceCreators.put(Set.class, () -> EnumSet.noneOf(String.class));

        final ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        final ObjectConstructor<EnumSet<String>> objectConstructor = constructorConstructor.get(new TypeToken<EnumSet<String>>() {}.getType());
        assertInstanceOf(EnumSet.class, objectConstructor.construct());
    }

    @Test
    public void testGetDefaultImplementationConstructorUnsupportedType() {
        final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        final Class<Integer> intClass = Integer.class;
        instanceCreators.put(Integer.class, () -> 0);

        final ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        final ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(intClass);
        assertNull(objectConstructor.construct());
    }

    @Test
    public void testGetUnsafeConstructor() {
        final Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        final Class<TreeMap<String, Object>> treeMapClass = TreeMap.class;
        instanceCreators.put(treeMapClass, null);

        final ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        final ObjectConstructor<TreeMap<String, Object>> objectConstructor = constructorConstructor.get(new TypeToken<TreeMap<String, Object>>() {}.getType());
        assertNotNull(objectConstructor);
        assertEquals(newInstance(treeMapClass), objectConstructor.construct());
    }
}