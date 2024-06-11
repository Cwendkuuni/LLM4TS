package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ConstructorConstructorTest {

    @Test
    public void testGet() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        InstanceCreator<String> stringInstanceCreator = mock(InstanceCreator.class);
        when(stringInstanceCreator.createInstance(TypeToken.get(String.class))).thenReturn("test");
        instanceCreators.put(TypeToken.get(String.class), stringInstanceCreator);

        InstanceCreator<ArrayList<Integer>> arrayListInstanceCreator = mock(InstanceCreator.class);
        when(arrayListInstanceCreator.createInstance(TypeToken.get(ArrayList.class))).thenReturn(new ArrayList<>());
        instanceCreators.put(ArrayList.class, arrayListInstanceCreator);

        InstanceCreator<LinkedHashMap<String, Object>> linkedHashMapInstanceCreator = mock(InstanceCreator.class);
        when(linkedHashMapInstanceCreator.createInstance(TypeToken.get(LinkedHashMap.class))).thenReturn(new LinkedHashMap<>());
        instanceCreators.put(LinkedHashMap.class, linkedHashMapInstanceCreator);

        Type typeTokenArrayListString = new TypeToken<ArrayList<String>>() {};
        ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(typeTokenArrayListString);
        assertNotNull(constructor);

        when(arrayListInstanceCreator.createInstance(TypeToken.get(ArrayList.class))).thenReturn(new ArrayList<Object>());
        ArrayList<String> testArrayList = constructor.construct();
        assertNotNull(testArrayList);
        assertNotNull(testArrayList.get(0));

        Type typeTokenLinkedHashMapStringObject = new TypeToken<LinkedHashMap<String, Object>>() {};
        ObjectConstructor<LinkedHashMap<String, Object>> constructorLinkedHashMap = constructorConstructor.get(typeTokenLinkedHashMapStringObject);
        assertNotNull(constructorLinkedHashMap);

        Object testLinkedHashMap = constructorLinkedHashMap.construct();
        assertNotNull(testLinkedHashMap);
        Map<String, Object> testMap = (Map<String, Object>) testLinkedHashMap;
        assertNotNull(testMap);
        assertNull(testMap.get("key"));

        Type typeTokenEnumSetString = new TypeToken<EnumSet<String>>() {};
        ObjectConstructor<EnumSet<String>> constructorEnumSet = constructorConstructor.get(typeTokenEnumSetString);
        assertNull(constructorEnumSet);

        Type typeTokenMapStringString = new TypeToken<Map<String, String>>() {};
        ObjectConstructor<Map<String, String>> constructorMap = constructorConstructor.get(typeTokenMapStringString);
        assertNull(constructorMap);

        Type typeTokenSortedMapStringInteger = new TypeToken<SortedMap<String, Integer>>() {};
        ObjectConstructor<SortedMap<String, Integer>> constructorSortedMap = constructorConstructor.get(typeTokenSortedMapStringInteger);
        assertNull(constructorSortedMap);
    }
}