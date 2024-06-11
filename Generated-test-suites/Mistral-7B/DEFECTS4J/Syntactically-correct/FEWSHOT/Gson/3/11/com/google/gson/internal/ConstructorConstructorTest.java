package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
  public void testGet_CollectionType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

    ObjectConstructor<Collection<String>> collectionConstructor = constructorConstructor.get(TypeToken.get(Collection.class, String.class));
    assertNotNull(collectionConstructor);

    Collection<String> collection = collectionConstructor.construct();
    assertNotNull(collection);
  }

  @Test
  public void testGet_SortedSetType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

    ObjectConstructor<SortedSet<String>> sortedSetConstructor = constructorConstructor.get(TypeToken.get(SortedSet.class, String.class));
    assertNotNull(sortedSetConstructor);

    SortedSet<String> sortedSet = sortedSetConstructor.construct();
    assertNotNull(sortedSet);
  }

  @Test
  public void testGet_EnumSetType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

    ObjectConstructor<EnumSet<String>> enumSetConstructor = constructorConstructor.get(new TypeToken<EnumSet<String>>(){});
    assertNotNull(enumSetConstructor);

    EnumSet<String> enumSet = enumSetConstructor.construct();
    assertNotNull(enumSet);
  }

  @Test
  public void testGet_SetType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

    ObjectConstructor<Set<String>> setConstructor = constructorConstructor.get(TypeToken.get(Set.class, String.class));
    assertNotNull(setConstructor);

    Set<String> set = setConstructor.construct();
    assertNotNull(set);
  }

  @Test
  public void testGet_QueueType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

    ObjectConstructor<Queue<String>> queueConstructor = constructorConstructor.get(TypeToken.get(Queue.class, String.class));
    assertNotNull(queueConstructor);

    Queue<String> queue = queueConstructor.construct();
    assertNotNull(queue);
  }

  @Test
  public void testGet_MapType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

    ObjectConstructor<Map<String, String>> mapConstructor = constructorConstructor.get(TypeToken.get(Map.class, String.class, String.class));
    assertNotNull(mapConstructor);

    Map<String, String> map = mapConstructor.construct();
    assertNotNull(map);
  }

  @Test
  public void testGet_SortedMapType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

    ObjectConstructor<SortedMap<String, String>> sortedMapConstructor = constructorConstructor.get(TypeToken.get(SortedMap.class, String.class, String.class));
    assertNotNull(sortedMapConstructor);

    SortedMap<String, String> sortedMap = sortedMapConstructor.construct();
    assertNotNull(sortedMap);
  }

  @Test
  public void testGet_UnknownType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

    ObjectConstructor<UnknownType> unknownTypeConstructor = constructorConstructor.get(new TypeToken<UnknownType>(){});
    assertNull(unknownTypeConstructor);
  }
}