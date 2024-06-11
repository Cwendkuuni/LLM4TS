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

import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testGet_Map() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Object, Object>() {});
    ObjectConstructor<Map<String, Integer>> mapConstructor = constructorConstructor.get(TypeToken.get(Map.class, String.class, Integer.class));
    assertNotNull(mapConstructor);
  }

  @Test
  public void testGet_SortedMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new TreeMap<Object, Object>() {});
    ObjectConstructor<SortedMap<String, Integer>> sortedMapConstructor = constructorConstructor.get(TypeToken.get(SortedMap.class, String.class, Integer.class));
    assertNotNull(sortedMapConstructor);
  }

  @Test
  public void testGet_EnumSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(EnumSet.noneOf(String.class));
    ObjectConstructor<EnumSet<String>> enumSetConstructor = constructorConstructor.get(TypeToken.get(EnumSet.class, String.class));
    assertNotNull(enumSetConstructor);
  }

  @Test
  public void testGet_ArrayList() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new ArrayList<Object>() {});
    ObjectConstructor<ArrayList<String>> arrayListConstructor = constructorConstructor.get(TypeToken.get(ArrayList.class, String.class));
    assertNotNull(arrayListConstructor);
  }

  @Test
  public void testGet_LinkedList() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedList<Object>() {});
    ObjectConstructor<LinkedList<String>> linkedListConstructor = constructorConstructor.get(TypeToken.get(LinkedList.class, String.class));
    assertNotNull(linkedListConstructor);
  }

  @Test
  public void testGet_LinkedHashMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Object, Object>() {});
    ObjectConstructor<LinkedHashMap<String, Integer>> linkedHashMapConstructor = constructorConstructor.get(TypeToken.get(LinkedHashMap.class, String.class, Integer.class));
    assertNotNull(linkedHashMapConstructor);
  }

  @Test
  public void testGet_TreeMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new TreeMap<Object, Object>() {});
    ObjectConstructor<TreeMap<String, Integer>> treeMapConstructor = constructorConstructor.get(TypeToken.get(TreeMap.class, String.class, Integer.class));
    assertNotNull(treeMapConstructor);
  }

  @Test
  public void testGet_TreeSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new TreeSet<Object>() {});
    ObjectConstructor<TreeSet<String>> treeSetConstructor = constructorConstructor.get(TypeToken.get(TreeSet.class, String.class));
    assertNotNull(treeSetConstructor);
  }

  @Test
  public void testGet_LinkedHashSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashSet<Object>() {});
    ObjectConstructor<LinkedHashSet<String>> linkedHashSetConstructor = constructorConstructor.get(TypeToken.get(LinkedHashSet.class, String.class));
    assertNotNull(linkedHashSetConstructor);
  }

  @Test
  public void testGet_ClassWithDefaultConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(TypeToken.get(String.class));
    assertNotNull(stringConstructor);
  }

  @Test
  public void testGet_ClassWithNoDefaultConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<Integer> integerConstructor = constructorConstructor.get(TypeToken.get(Integer.class));
    assertNull(integerConstructor);
  }

  @Test
  public void testGet_ClassWithParameterizedConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<Tuple2<String, Integer>> tupleConstructor = constructorConstructor.get(TypeToken.get(Tuple2.class, String.class, Integer.class));
    assertNotNull(tupleConstructor);
  }

  @Test(expected = RuntimeException.class)
  public void testGet_ClassWithNoConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<Object> objectConstructor = constructorConstructor.get(TypeToken.getType(Object.class));
    assertNull(objectConstructor);
  }
}