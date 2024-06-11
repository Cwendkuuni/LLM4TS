package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import java.util.Queue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreatorsMap = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreatorsMap);

    // Test for a simple type
    TypeNumber typeNumber = new TypeNumber();
    ObjectConstructor<TypeNumber> constructor = constructorConstructor.get(TypeToken.get(TypeNumber.class));
    assertNotNull(constructor);
    TypeNumber instance = constructor.construct();
    assertEquals(TypeNumber.ZERO, instance);

    // Test for a Collection type
    TypeCollection<Integer> typeCollection = new TypeCollection<>();
    ObjectConstructor<TypeCollection<Integer>> collectionConstructor = constructorConstructor.get(TypeToken.get(TypeCollection.class));
    assertNotNull(collectionConstructor);
    TypeCollection<Integer> collection = collectionConstructor.construct();
    assertNotNull(collection);
    assertTrue(collection.isEmpty());

    // Test for an EnumSet type
    TypeEnumSet<DaysOfWeek> typeEnumSet = new TypeEnumSet<>();
    ObjectConstructor<TypeEnumSet<DaysOfWeek>> enumSetConstructor = constructorConstructor.get(TypeToken.get(TypeEnumSet.class));
    assertNotNull(enumSetConstructor);
    TypeEnumSet<DaysOfWeek> enumSet = enumSetConstructor.construct();
    assertNotNull(enumSet);
    assertTrue(enumSet.isEmpty());

    // Test for a Set type
    TypeSet<String> typeSet = new TypeSet<>();
    ObjectConstructor<TypeSet<String>> setConstructor = constructorConstructor.get(TypeToken.get(TypeSet.class));
    assertNotNull(setConstructor);
    TypeSet<String> set = setConstructor.construct();
    assertNotNull(set);
    assertTrue(set.isEmpty());

    // Test for a Queue type
    TypeQueue<String> typeQueue = new TypeQueue<>();
    ObjectConstructor<TypeQueue<String>> queueConstructor = constructorConstructor.get(TypeToken.get(TypeQueue.class));
    assertNotNull(queueConstructor);
    TypeQueue<String> queue = queueConstructor.construct();
    assertNotNull(queue);
    assertTrue(queue.isEmpty());

    // Test for a Map type
    TypeMap<String, Integer> typeMap = new TypeMap<>();
    ObjectConstructor<TypeMap<String, Integer>> mapConstructor = constructorConstructor.get(TypeToken.get(TypeMap.class));
    assertNotNull(mapConstructor);
    TypeMap<String, Integer> map = mapConstructor.construct();
    assertNotNull(map);
    assertTrue(map.isEmpty());

    // Test for a SortedMap type
    TypeSortedMap<String, Integer> typeSortedMap = new TypeSortedMap<>();
    ObjectConstructor<TypeSortedMap<String, Integer>> sortedMapConstructor = constructorConstructor.get(TypeToken.get(TypeSortedMap.class));
    assertNotNull(sortedMapConstructor);
    TypeSortedMap<String, Integer> sortedMap = sortedMapConstructor.construct();
    assertNotNull(sortedMap);
    assertTrue(sortedMap.isEmpty());

    // Test for a SortedSet type
    TypeSortedSet<String> typeSortedSet = new TypeSortedSet<>();
    ObjectConstructor<TypeSortedSet<String>> sortedSetConstructor = constructorConstructor.get(TypeToken.get(TypeSortedSet.class));
    assertNotNull(sortedSetConstructor);
    TypeSortedSet<String> sortedSet = sortedSetConstructor.construct();
    assertNotNull(sortedSet);
    assertTrue(sortedSet.isEmpty());
  }

  @Test
  public void testGetNullForUnknownType() {
    Map<Type, InstanceCreator<?>> instanceCreatorsMap = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreatorsMap);

    ObjectConstructor<String> stringConstructor = constructorConstructor.get(TypeToken.get(String.class));
    assertNull(stringConstructor);
  }

  @Test(expected = RuntimeException.class)
  public void testThrowRuntimeExceptionForUnknownType() {
    Map<Type, InstanceCreator<?>> instanceCreatorsMap = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreatorsMap);

    ObjectConstructor<String> stringConstructor = constructorConstructor.get(TypeToken.get(Integer.class));
  }

  @Test
  public void testToString() {
    Map<Type, InstanceCreator<?>> instanceCreatorsMap = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreatorsMap);

    assertEquals("{@com.google.gson.internal.InstanceCreator@1d56f60}", constructorConstructor.toString());
  }
}