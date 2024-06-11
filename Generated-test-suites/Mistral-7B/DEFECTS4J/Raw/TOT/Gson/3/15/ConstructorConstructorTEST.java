package com.example.junit4;

import static org.junit.Assert.*;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

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
  public void testGetInstanceOfString() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<String> typeToken = new TypeToken<String>() {};
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(typeToken);
    String string = stringConstructor.construct();
    assertNotNull(string);
    assertEquals("", string);
  }

  @Test
  public void testGetInstanceOfArrayList() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<ArrayList<Integer>> typeToken = new TypeToken<ArrayList<Integer>>() {};
    ObjectConstructor<ArrayList<Integer>> arrayListConstructor = constructorConstructor.get(typeToken);
    ArrayList<Integer> arrayList = arrayListConstructor.construct();
    assertNotNull(arrayList);
    assertTrue(arrayList.isEmpty());
  }

  @Test
  public void testGetInstanceOfLinkedList() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<LinkedList<Long>> typeToken = new TypeToken<LinkedList<Long>>() {};
    ObjectConstructor<LinkedList<Long>> linkedListConstructor = constructorConstructor.get(typeToken);
    LinkedList<Long> linkedList = linkedListConstructor.construct();
    assertNotNull(linkedList);
    assertTrue(linkedList.isEmpty());
  }

  @Test
  public void testGetInstanceOfTreeMap() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeTreeMap<String, Integer> typeToken = new TypeToken<TreeMap<String, Integer>>() {};
    ObjectConstructor<TreeMap<String, Integer>> treeMapConstructor = constructorConstructor.get(typeToken);
    TreeMap<String, Integer> treeMap = treeMapConstructor.construct();
    assertNotNull(treeMap);
    assertEquals(0, treeMap.size());
  }

  @Test
  public void testGetInstanceOfArrayListWithNoInstanceCreatorOrDefaultConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    ObjectConstructor<ArrayList<String>> arrayListConstructor = constructorConstructor.get(typeToken);
    if (arrayListConstructor == null) {
      throw new AssertionError("Expected default constructor for ArrayList<String>");
    }
    ArrayList<String> arrayList = arrayListConstructor.construct();
    assertNotNull(arrayList);
    assertTrue(arrayList.isEmpty());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfUnknownType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<UnknownType> typeToken = new TypeToken<UnknownType>() {};
    ObjectConstructor<UnknownType> unknownTypeConstructor = constructorConstructor.get(typeToken);
    if (unknownTypeConstructor != null) {
      fail("Unexpected default constructor for UnknownType");
    }
  }
}