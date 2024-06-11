package com.google.gson.internal;

import static org.junit.Assert.*;

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
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstance() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeStringTypeAdapter stringTypeAdapter = new TypeStringTypeAdapter();
    InstanceCreator<String> stringInstanceCreator = stringTypeAdapter::new;
    instanceCreators.put(String.class, stringInstanceCreator);

    assertEquals("hello", constructorConstructor.get(TypeToken.get(String.class)).construct());
  }

  @Test
  public void testGetInstanceForRawType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeStringTypeAdapter stringTypeAdapter = new TypeStringTypeAdapter();
    InstanceCreator<String> stringInstanceCreator = stringTypeAdapter::new;
    instanceCreators.put(String.class, stringInstanceCreator);

    assertEquals("hello", constructorConstructor.get(TypeToken.get(String.class.getRawType())).construct());
  }

  @Test
  public void testGetInstanceWithNoCreator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    assertNull(constructorConstructor.get(TypeToken.get(Integer.class)).construct());
  }

  @Test
  public void testGetInstanceForMap() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    assertEquals(new LinkedHashMap<Object, Object>(), constructorConstructor.get(TypeToken.get(Map.class)).construct());
  }

  @Test
  public void testGetInstanceForSortedMap() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    assertEquals(new TreeMap<Object, Object>(), constructorConstructor.get(TypeToken.get(SortedMap.class)).construct());
  }

  @Test
  public void testGetInstanceForSet() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    assertEquals(new LinkedHashSet<Object>(), constructorConstructor.get(TypeToken.get(Set.class)).construct());
  }

  @Test
  public void testGetInstanceForSortedSet() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    assertEquals(new TreeSet<Object>(), constructorConstructor.get(TypeToken.get(SortedSet.class)).construct());
  }

  @Test
  public void testGetInstanceForCollection() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    assertEquals(new ArrayList<Object>(), constructorConstructor.get(TypeToken.get(Collection.class)).construct());
  }

  @Test
  public void testGetInstanceForQueue() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    assertEquals(new LinkedList<Object>(), constructorConstructor.get(TypeToken.get(Queue.class)).construct());
  }

  @Test
  public void testGetInstanceForEnumSet() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    assertEquals(EnumSet.noneOf(Foo.class), constructorConstructor.get(TypeToken.get(EnumSet.class)).construct());
  }

  @Test(expected = JsonIOException.class)
  public void testGetInstanceForInvalidEnumSetType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    constructorConstructor.get(TypeToken.get(Map.class));
  }

  @Test
  public void testGetInstanceForCustomClass() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    MyCustomClass myCustomClass = new MyCustomClass();
    InstanceCreator<MyCustomClass> myCustomClassInstanceCreator = () -> myCustomClass;
    instanceCreators.put(MyCustomClass.class, myCustomClassInstanceCreator);

    assertEquals(myCustomClass, constructorConstructor.get(TypeToken.get(MyCustomClass.class)).construct());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceWithUnsafeAllocatorFailure() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    constructorConstructor.get(TypeToken.get(UnreachableClass.class));
  }
}

class TypeStringTypeAdapter {
  public TypeStringTypeAdapter() {}
}

class MyCustomClass {
  public MyCustomClass() {}
}

class UnreachableClass {
  private UnreachableClass() {}
}