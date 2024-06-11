package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConstructorConstructorTest {

    private ConstructorConstructor constructorConstructor;

    @Before
    public void setup() {
        constructorConstructor = new ConstructorConstructor(new HashMap<>());
    }

    @Test
    public void testGet_WithInstanceCreator_ShouldReturnInstanceCreatorConstructor() {
        InstanceCreator<Object> instanceCreator = type -> "Object";
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(Object.class, instanceCreator);
        ConstructorConstructor cc = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<Object> result = cc.get(TypeToken.get(Object.class));
        assertEquals("Object", result.construct());
    }

    @Test
    public void testGet_WithRawTypeInstanceCreator_ShouldReturnInstanceCreatorConstructor() {
        InstanceCreator<List<Object>> instanceCreator = type -> new ArrayList<>();
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(List.class, instanceCreator);
        ConstructorConstructor cc = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<List<Object>> result = cc.get(TypeToken.get(List.class));
        assertEquals(ArrayList.class, result.construct().getClass());
    }

    @Test
    public void testGet_WithDefaultConstructor_ShouldReturnDefaultConstructor() {
        ObjectConstructor<String> result = constructorConstructor.get(TypeToken.get(String.class));
        assertEquals(null, result.construct());
    }

    @Test
    public void testGet_WithInvalidEnumSetType_ShouldThrowJsonIOException() {
        TypeToken<?> typeToken = TypeToken.getParameterized(EnumSet.class, typeToken(Object.class));
        try {
            constructorConstructor.get(typeToken);
            fail("Expected JsonIOException to be thrown");
        } catch (JsonIOException e) {
            assertEquals("Invalid EnumSet type: java.lang.Object", e.getMessage());
        }
    }

    @Test
    public void testGet_WithSetType_ShouldReturnLinkedHashSetConstructor() {
        TypeToken<?> typeToken = TypeToken.getParameterized(Set.class, typeToken(Object.class));
        ObjectConstructor<Set<Object>> result = constructorConstructor.get(typeToken);
        assertEquals(LinkedHashSet.class, result.construct().getClass());
    }

    @Test
    public void testGet_WithQueueType_ShouldReturnLinkedListConstructor() {
        TypeToken<?> typeToken = TypeToken.getParameterized(Queue.class, typeToken(Object.class));
        ObjectConstructor<Queue<Object>> result = constructorConstructor.get(typeToken);
        assertEquals(LinkedList.class, result.construct().getClass());
    }

    @Test
    public void testGet_WithDefaultImplementationInterface_ShouldReturnArrayConstructor() {
        TypeToken<?> typeToken = TypeToken.getParameterized(Collection.class, typeToken(Object.class));
        ObjectConstructor<Collection<Object>> result = constructorConstructor.get(typeToken);
        assertEquals(ArrayList.class, result.construct().getClass());
    }

    @Test
    public void testGet_WithArrayConstructor_ShouldReturnArrayConstructor() {
        TypeToken<?> typeToken = TypeToken.getParameterized(List.class, typeToken(Object.class));
        ObjectConstructor<List<Object>> result = constructorConstructor.get(typeToken);
        assertEquals(ArrayList.class, result.construct().getClass());
    }

    @Test
    public void testGet_WithSortedMapType_ShouldReturnTreeMapConstructor() {
        TypeToken<?> typeToken = TypeToken.getParameterized(SortedMap.class, typeToken(Object.class), typeToken(Object.class));
        ObjectConstructor<SortedMap<Object, Object>> result = constructorConstructor.get(typeToken);
        assertEquals(TreeMap.class, result.construct().getClass());
    }

    @Test
    public void testGet_WithParameterizedMapType_ShouldReturnLinkedHashMapConstructor() {
        TypeToken<?> typeToken = TypeToken.getParameterized(Map.class, typeToken(Object.class), typeToken(Object.class));
        ObjectConstructor<Map<Object, Object>> result = constructorConstructor.get(typeToken);
        assertEquals(LinkedHashMap.class, result.construct().getClass());
    }

    @Test
    public void testToString_WithInstanceCreators_ShouldReturnStringRepresentationOfInstanceCreators() {
        InstanceCreator<Object> instanceCreator = type -> "Object";
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(Object.class, instanceCreator);
        ConstructorConstructor cc = new ConstructorConstructor(instanceCreators);
        String result = cc.toString();
        assertEquals("{class java.lang.Object=InstanceCreator<Object>}", result);
    }

    private static <T> TypeToken<T> typeToken(Class<T> type) {
        return TypeToken.get(type);
    }
}