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
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ConstructorConstructorTest {

    @Test
    public void testGetCollectionInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<Collection<String>> collectionConstructor = constructorConstructor.get(TypeToken.get(Collection.class, String.class));
        Collection<String> collection = collectionConstructor.construct();

        assertNotNull(collection);
        assertNull(collection.isEmpty());
    }

    @Test
    public void testGetSetInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<Set<String>> setConstructor = constructorConstructor.get(TypeToken.get(Set.class, String.class));
        Set<String> set = setConstructor.construct();

        assertNotNull(set);
        assertNull(set.isEmpty());
    }

    @Test
    public void testGetQueueInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<Queue<String>> queueConstructor = constructorConstructor.get(TypeToken.get(Queue.class, String.class));
        Queue<String> queue = queueConstructor.construct();

        assertNotNull(queue);
        assertNull(queue.isEmpty());
    }

    @Test
    public void testGetMapInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<Map<String, String>> mapConstructor = constructorConstructor.get(TypeToken.get(Map.class, String.class, String.class));
        Map<String, String> map = mapConstructor.construct();

        assertNotNull(map);
        assertNull(map.isEmpty());
    }

    @Test
    public void testGetSortedMapInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<SortedMap<String, String>> sortedMapConstructor = constructorConstructor.get(new TypeToken<SortedMap<String, String>>() {}.getType());
        SortedMap<String, String> sortedMap = sortedMapConstructor.construct();

        assertNotNull(sortedMap);
        assertNull(sortedMap.isEmpty());
    }

    @Test
    public void testGetSortedSetInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<SortedSet<String>> sortedSetConstructor = constructorConstructor.get(TypeToken.get(SortedSet.class, String.class));
        SortedSet<String> sortedSet = sortedSetConstructor.construct();

        assertNotNull(sortedSet);
        assertNull(sortedSet.isEmpty());
    }

    @Test
    public void testGetEnumSetInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<EnumSet<String>> enumSetConstructor = constructorConstructor.get(new TypeToken<EnumSet<String>>() {}.getType());
        EnumSet<String> enumSet = enumSetConstructor.construct();

        assertNotNull(enumSet);
        assertNull(enumSet.isEmpty());
    }

    @Test
    public void testGetDefaultConstructorInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<Integer> intConstructor = new DefaultConstructorConstructor<Integer>(int[].class);
        ObjectConstructor<Integer> defaultConstructor = constructorConstructor.get(intConstructor.getType());

        Integer integer = defaultConstructor.construct();
        assertNotNull(integer);
        assertEquals(0, integer.intValue());
    }

    @Test(expected = RuntimeException.class)
    public void testGetNullInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<String> stringConstructor = new ObjectConstructor<String>() {};
        constructorConstructor.get(stringConstructor.getType());
    }

    @Test(expected = RuntimeException.class)
    public void testGetUnknownTypeInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<Float> floatConstructor = new ObjectConstructor<Float>() {};
        constructorConstructor.get(floatConstructor.getType());
    }

    @Test
    public void testToString() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        assertEquals("{instanceCreators=[null]}", constructorConstructor.toString());
    }
}

class DefaultConstructorConstructor<T> implements ObjectConstructor<T> {
    private final Class<? super T> clazz;

    public DefaultConstructorConstructor(Class<? super T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T construct() {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (T) constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getType() {
        return clazz;
    }
}