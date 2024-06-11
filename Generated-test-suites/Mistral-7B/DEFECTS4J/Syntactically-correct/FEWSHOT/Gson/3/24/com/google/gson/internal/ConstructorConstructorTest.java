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

import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceWithInstanceCreator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<String> stringCreator = new InstanceCreator<String>() {
      @Override
      public String createInstance(Type type) {
        return "default";
      }
    };
    instanceCreators.put(String.class, stringCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    Constructor<String> stringConstructor = String.class.getDeclaredConstructor();
    TypeToken<String> stringToken = new TypeToken<String>() {};

    ObjectConstructor<String> constructor = constructorConstructor.get(stringToken);

    assertNotNull(constructor);
    assertEquals("default", constructor.construct());
  }

  @Test
  public void testGetInstanceWithRawTypeInstanceCreator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<Object> objectCreator = new InstanceCreator<Object>() {
      @Override
      public Object createInstance(Type type) {
        return new Object() {};
      }
    };
    instanceCreators.put(Object.class, objectCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<String> stringToken = new TypeToken<String>() {};
    ObjectConstructor<String> constructor = constructorConstructor.get(stringToken);

    assertNotNull(constructor);
    assertNull(constructor.construct());
  }

  @Test
  public void testGetInstanceWithDefaultConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    TypeToken<ArrayList<Integer>> arrayListIntToken = new TypeToken<ArrayList<Integer>>() {};
    ObjectConstructor<ArrayList<Integer>> constructor = constructorConstructor.get(arrayListIntToken);

    assertNotNull(constructor);
    assertEquals(new ArrayList<Integer>(), constructor.construct());
  }

  @Test
  public void testGetInstanceWithDefaultImplementationConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Type type = new TypeToken<SortedMap<String, Object>>() {}.getType();
    ObjectConstructor<SortedMap<String, Object>> constructor = constructorConstructor.get(new TypeToken<SortedMap<String, Object>>() {}.getType());

    assertNotNull(constructor);
    assertEquals(new TreeMap<String, Object>(), constructor.construct());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceWithNoDefaultConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Type type = new TypeToken<String>() {}.getType();
    ObjectConstructor<String> constructor = constructorConstructor.get(new TypeToken<String>() {}.getType());

    assertNull(constructor);
  }
}