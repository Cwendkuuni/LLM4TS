package com.example.junit4.constructorconstructor;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
import com.google.gson.UnsafeAllocator;

import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceOfBasicTypes() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<Integer> intConstructor = constructorConstructor.get(TypeToken.get(Integer.class));
    Object obj = intConstructor.construct();
    assertEquals(0, (int) obj);
  }

  @Test
  public void testGetInstanceOfCollectionTypes() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<ArrayList<Integer>> listConstructor = constructorConstructor.get(TypeToken.get(ArrayList.class));
    Object obj = listConstructor.construct();
    assertTrue(obj instanceof ArrayList);
  }

  @Test
  public void testGetInstanceOfMapTypes() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<LinkedHashMap<String, Object>> mapConstructor = constructorConstructor.get(
        TypeToken.getParameterized(Map.class, String.class, Object.class));
    Object obj = mapConstructor.construct();
    assertTrue(obj instanceof LinkedHashMap);
  }

  @Test
  public void testGetInstanceOfSortedMapTypes() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<TreeMap<String, Object>> sortedMapConstructor = constructorConstructor.get(
        TypeToken.getParameterized(SortedMap.class, String.class, Object.class));
    Object obj = sortedMapConstructor.construct();
    assertTrue(obj instanceof TreeMap);
  }

  @Test
  public void testGetInstanceOfEnumSetTypes() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<EnumSet<DaysOfWeek>> enumSetConstructor = constructorConstructor.get(TypeToken.get(EnumSet.class)
        .getType());
    Object obj = enumSetConstructor.construct();
    assertTrue(obj instanceof EnumSet);
  }

  @Test
  public void testGetInstanceOfSortedSetTypes() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<TreeSet<Integer>> sortedSetConstructor = constructorConstructor.get(TypeToken.get(TreeSet.class));
    Object obj = sortedSetConstructor.construct();
    assertTrue(obj instanceof TreeSet);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfUnknownTypeThrowsException() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<UnknownType> unknownTypeConstructor = constructorConstructor.get(TypeToken.get(UnknownType.class));
    unknownTypeConstructor.construct();
  }

  @Test
  public void testGetInstanceOfRawType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<Object> objectConstructor = constructorConstructor.get(TypeToken.get(Object.class));
    Object obj = objectConstructor.construct();
    assertNotNull(obj);
  }

  @Test
  public void testGetInstanceOfInterfaceType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<Map<String, Object>> mapConstructor = constructorConstructor.get(Map.class);
    Object obj = mapConstructor.construct();
    assertTrue(obj instanceof Map);
  }

  @Test
  public void testGetInstanceOfInstanceCreator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<ArrayList<String>> listCreator = new ArrayListCreator<>();
    instanceCreators.put(TypeToken.get(ArrayList.class), listCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<ArrayList<String>> listConstructor = constructorConstructor.get(TypeToken.get(ArrayList.class));
    Object obj = listConstructor.construct();
    assertTrue(obj instanceof ArrayList);
  }

  @Test
  public void testGetInstanceOfDefaultImplementation() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<SortedMap<String, Object>> sortedMapConstructor = constructorConstructor.get(SortedMap.class);
    Object obj = sortedMapConstructor.construct();
    assertTrue(obj instanceof TreeMap);
  }

  @Test
  public void testGetInstanceOfDefaultImplementationWithCustomClass() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    ObjectConstructor<MyCustomMap<String, Object>> myCustomMapConstructor = constructorConstructor.get(TypeToken.getParameterized(Map.class, String.class, Object.class)
        .getType());
    Object obj = myCustomMapConstructor.construct();
    assertTrue(obj instanceof MyCustomMap);
  }

  @Test
  public void testGetInstanceOfUnsafeAllocator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<MyCustomClass> myCustomClassConstructor = constructorConstructor.get(TypeToken.get(MyCustomClass.class));
    Object obj = myCustomClassConstructor.construct();
    assertNotNull(obj);
  }

  private static class ArrayListCreator implements InstanceCreator<ArrayList<String>> {
    @Override public ArrayList<String> createInstance(Type type) {
      return new ArrayList<String>();
    }
  }

  private static class MyCustomMap<K, V> extends LinkedTreeMap<K, V> {
  }

  private static class MyCustomClass {
  }

  private static class UnsafeAllocator {
    static UnsafeAllocator create() {
      try {
        return new SunMicrosystemsUnsafe();
      } catch (Exception e) {
        throw new RuntimeException("Failed to create Unsafe instance", e);
      }
    }

    Object newInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
      return clazz.newInstance();
    }
  }

  private static class SunMicrosystemsUnsafe {}
}