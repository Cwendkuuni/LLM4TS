package com.google.gson.internal;

import static com.google.gson.reflect.TypeToken.get;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceForCollection() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<Collection<Integer>> collectionConstructor = constructorConstructor.get(get(Collection.class, int.class));
    assertNotNull(collectionConstructor);
    Collection<Integer> collection = collectionConstructor.construct();
    assertNull(collection.isEmpty());
  }

  @Test
  public void testGetInstanceForSortedSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<SortedSet<Integer>> sortedSetConstructor = constructorConstructor.get(get(SortedSet.class, int.class));
    assertNotNull(sortedSetConstructor);
    SortedSet<Integer> sortedSet = sortedSetConstructor.construct();
    assertNull(sortedSet.isEmpty());
  }

  @Test
  public void testGetInstanceForEnumSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<EnumSet<Annotation>> enumSetConstructor = constructorConstructor.get(get(EnumSet.class, Annotation.class));
    assertNotNull(enumSetConstructor);
    EnumSet<Annotation> enumSet = enumSetConstructor.construct();
    assertNull(enumSet.isEmpty());
  }

  @Test
  public void testGetInstanceForSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<Set<String>> setConstructor = constructorConstructor.get(get(Set.class, String.class));
    assertNotNull(setConstructor);
    Set<String> set = setConstructor.construct();
    assertNull(set.isEmpty());
  }

  @Test
  public void testGetInstanceForQueue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<Queue<String>> queueConstructor = constructorConstructor.get(get(Queue.class, String.class));
    assertNotNull(queueConstructor);
    Queue<String> queue = queueConstructor.construct();
    assertNull(queue.isEmpty());
  }

  @Test
  public void testGetInstanceForMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<Map<String, Integer>> mapConstructor = constructorConstructor.get(get(Map.class, String.class, int.class));
    assertNotNull(mapConstructor);
    Map<String, Integer> map = mapConstructor.construct();
    assertNull(map.isEmpty());
  }

  @Test
  public void testGetInstanceForSortedMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<SortedMap<String, Integer>> sortedMapConstructor = constructorConstructor.get(get(SortedMap.class, String.class, int.class));
    assertNotNull(sortedMapConstructor);
    SortedMap<String, Integer> sortedMap = sortedMapConstructor.construct();
    assertNull(sortedMap.isEmpty());
  }

  @Test (expected = RuntimeException.class)
  public void testGetInstanceForUnknownType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<UnknownType> unknownTypeConstructor = constructorConstructor.get(get(UnknownType.class));
    unknownTypeConstructor.construct();
  }
}