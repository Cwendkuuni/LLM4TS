package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

    private ConstructorConstructor constructorConstructor;

    @Mock
    private InstanceCreator<Object> instanceCreatorMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
    }

    @Test
    public void testGet_withInstanceCreator() {
        TypeToken<Object> typeToken = new TypeToken<Object>() {};
        Type type = typeToken.getType();
        Class<Object> rawType = typeToken.getRawType();

        when(instanceCreatorMock.createInstance(type)).thenReturn(new Object());
        constructorConstructor.instanceCreators.put(type, instanceCreatorMock);

        ObjectConstructor<Object> result = constructorConstructor.get(typeToken);

        assertEquals(instanceCreatorMock.createInstance(type), result.construct());
    }

    @Test
    public void testGet_withRawTypeInstanceCreator() {
        TypeToken<Object> typeToken = new TypeToken<Object>() {};
        Type type = typeToken.getType();
        Class<Object> rawType = typeToken.getRawType();

        when(instanceCreatorMock.createInstance(type)).thenReturn(new Object());
        constructorConstructor.instanceCreators.put(rawType, instanceCreatorMock);

        ObjectConstructor<Object> result = constructorConstructor.get(typeToken);

        assertEquals(instanceCreatorMock.createInstance(type), result.construct());
    }

    @Test
    public void testGet_withDefaultConstructor() {
        TypeToken<NoArgConstructorClass> typeToken = new TypeToken<NoArgConstructorClass>() {};
        Type type = typeToken.getType();
        Class<NoArgConstructorClass> rawType = typeToken.getRawType();

        ObjectConstructor<NoArgConstructorClass> result = constructorConstructor.get(typeToken);

        assertEquals(NoArgConstructorClass.class, result.construct().getClass());
    }

    @Test
    public void testGet_withDefaultImplementationConstructor() {
        TypeToken<Collection<Object>> typeToken = new TypeToken<Collection<Object>>() {};
        Type type = typeToken.getType();
        Class<Collection<Object>> rawType = typeToken.getRawType();

        ObjectConstructor<Collection<Object>> result = constructorConstructor.get(typeToken);

        assertEquals(ArrayList.class, result.construct().getClass());
    }

    @Test
    public void testGet_withEnumSet() {
        TypeToken<EnumSet<MockEnum>> typeToken = new TypeToken<EnumSet<MockEnum>>() {};
        Type type = typeToken.getType();
        Class<EnumSet<MockEnum>> rawType = typeToken.getRawType();

        ObjectConstructor<EnumSet<MockEnum>> result = constructorConstructor.get(typeToken);

        assertEquals(EnumSet.noneOf(MockEnum.class), result.construct());
    }

    @Test
    public void testGet_withSet() {
        TypeToken<Set<Object>> typeToken = new TypeToken<Set<Object>>() {};
        Type type = typeToken.getType();
        Class<Set<Object>> rawType = typeToken.getRawType();

        ObjectConstructor<Set<Object>> result = constructorConstructor.get(typeToken);

        assertEquals(LinkedHashSet.class, result.construct().getClass());
    }

    @Test
    public void testGet_withQueue() {
        TypeToken<Queue<Object>> typeToken = new TypeToken<Queue<Object>>() {};
        Type type = typeToken.getType();
        Class<Queue<Object>> rawType = typeToken.getRawType();

        ObjectConstructor<Queue<Object>> result = constructorConstructor.get(typeToken);

        assertEquals(LinkedList.class, result.construct().getClass());
    }

    @Test
    public void testGet_withSortedSet() {
        TypeToken<SortedSet<Object>> typeToken = new TypeToken<SortedSet<Object>>() {};
        Type type = typeToken.getType();
        Class<SortedSet<Object>> rawType = typeToken.getRawType();

        ObjectConstructor<SortedSet<Object>> result = constructorConstructor.get(typeToken);

        assertEquals(TreeSet.class, result.construct().getClass());
    }

    @Test
    public void testGet_withMap() {
        TypeToken<Map<Object, Object>> typeToken = new TypeToken<Map<Object, Object>>() {};
        Type type = typeToken.getType();
        Class<Map<Object, Object>> rawType = typeToken.getRawType();

        ObjectConstructor<Map<Object, Object>> result = constructorConstructor.get(typeToken);

        assertEquals(LinkedHashMap.class, result.construct().getClass());
    }

    @Test
    public void testGet_withSortedMap() {
        TypeToken<SortedMap<Object, Object>> typeToken = new TypeToken<SortedMap<Object, Object>>() {};
        Type type = typeToken.getType();
        Class<SortedMap<Object, Object>> rawType = typeToken.getRawType();

        ObjectConstructor<SortedMap<Object, Object>> result = constructorConstructor.get(typeToken);

        assertEquals(TreeMap.class, result.construct().getClass());
    }

    @Test
    public void testGet_withDefaultEnumSetType() {
        TypeToken<EnumSet<NoArgConstructorClass>> typeToken = new TypeToken<EnumSet<NoArgConstructorClass>>() {};
        Type type = typeToken.getType();
        Class<EnumSet<NoArgConstructorClass>> rawType = typeToken.getRawType();

        ObjectConstructor<EnumSet<NoArgConstructorClass>> result = constructorConstructor.get(typeToken);

        assertNull(result);
    }

    @Test
    public void testGet_withDefaultImplementationMapType() {
        TypeToken<Map<String, NoArgConstructorClass>> typeToken = new TypeToken<Map<String, NoArgConstructorClass>>() {};
        Type type = typeToken.getType();
        Class<Map<String, NoArgConstructorClass>> rawType = typeToken.getRawType();

        ObjectConstructor<Map<String, NoArgConstructorClass>> result = constructorConstructor.get(typeToken);

        assertEquals(LinkedHashMap.class, result.construct().getClass());
    }

    @Test
    public void testGet_withUnsafeAllocator() {
        TypeToken<Object> typeToken = new TypeToken<Object>() {
        };
        Type type = typeToken.getType();
        Class<Object> rawType = typeToken.getRawType();

        ObjectConstructor<Object> result = constructorConstructor.get(typeToken);

        assertEquals(NoArgConstructorClass.class, result.construct().getClass());
    }
}