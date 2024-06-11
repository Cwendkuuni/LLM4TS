package com.example.junit4.tests;

import static org.junit.Assert.*;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConstructorConstructorTest {

  @org.junit.Before
  public void setup() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    // Add some instance creators for testing
    instanceCreators.put(new TypeToken<ArrayList<Integer>>() {}, new ArrayListInstanceCreator());
    instanceCreators.put(new TypeToken<TreeSet<String>>() {}, new TreeSetInstanceCreator());

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
  }

  @org.junit.Test
  public void testGetInstance_ArrayList() {
    TypeToken<ArrayList<Integer>> typeToken = new TypeToken<ArrayList<Integer>>() {};
    ObjectConstructor constructor = constructorConstructor.get(typeToken);
    ArrayList<Integer> instance = constructor.construct();
    assertNotNull(instance);
    assertTrue(instance instanceof ArrayList);
    assertEquals(0, instance.size());
  }

  @org.junit.Test
  public void testGetInstance_TreeSet() {
    TypeToken<TreeSet<String>> typeToken = new TypeToken<TreeSet<String>>() {};
    ObjectConstructor constructor = constructorConstructor.get(typeToken);
    TreeSet<String> instance = constructor.construct();
    assertNotNull(instance);
    assertTrue(instance instanceof TreeSet);
    assertTrue(instance.isEmpty());
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testGetInstance_UnknownType() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    constructorConstructor.get(typeToken);
  }

  @org.junit.Test
  public void testGetInstance_RawType() {
    Type type = ArrayList.class;
    ObjectConstructor constructor = constructorConstructor.get(TypeToken.get(type));
    ArrayList instance = constructor.construct();
    assertNotNull(instance);
    assertTrue(instance instanceof ArrayList);
    assertEquals(0, instance.size());
  }

  @org.junit.Test
  public void testGetInstance_DefaultConstructor() {
    Type type = HashMap.class;
    ObjectConstructor constructor = constructorConstructor.newDefaultConstructor((Class)type);
    HashMap instance = constructor.construct();
    assertNotNull(instance);
    assertTrue(instance instanceof HashMap);
    assertEquals(0, instance.size());
  }

  @org.junit.Test
  public void testGetInstance_DefaultImplementation_ArrayList() {
    Type type = Collection.class;
    ObjectConstructor constructor = constructorConstructor.newDefaultImplementationConstructor(type, ArrayList.class);
    ArrayList instance = constructor.construct();
    assertNotNull(instance);
    assertTrue(instance instanceof ArrayList);
    assertEquals(0, instance.size());
  }

  @org.junit.Test
  public void testGetInstance_DefaultImplementation_Map() {
    Type type = Map.class;
    ObjectConstructor constructor = constructorConstructor.newDefaultImplementationConstructor(type, LinkedTreeMap.class);
    Map instance = constructor.construct();
    assertNotNull(instance);
    assertTrue(instance instanceof LinkedTreeMap);
    assertEquals(0, instance.size());
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testGetInstance_UnsupportedType() {
    Type type = EnumSet.class;
    constructorConstructor.get(TypeToken.get(type));
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testGetInstance_UnsafeAllocator() {
    Type type = Object.class;
    constructorConstructor.newUnsafeAllocator(type, Object.class);
  }

  static class ArrayListInstanceCreator implements InstanceCreator<ArrayList<Object>> {
    @Override public ArrayList<Object> createInstance(Type type) {
      return new ArrayList<Object>();
    }
  }

  static class TreeSetInstanceCreator implements InstanceCreator<TreeSet<String>> {
    @Override public TreeSet<String> createInstance(Type type) {
      return new TreeSet<String>();
    }
  }
}