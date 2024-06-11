package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

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

import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceWithNullTypeToken() {
    ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
    assertNull(constructor.get(null));
  }

  @Test
  public void testGetInstanceWithNullInstanceCreatorMap() {
    ConstructorConstructor constructor = new ConstructorConstructor(null);
    assertNull(constructor.get(new TypeToken<ArrayList<String>>(){}.getType()));
  }

  @Test
  public void testGetInstanceWithDefaultConstructor() {
    ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<ArrayList<String>> objConstructor = constructor.get(new TypeToken<ArrayList<String>>(){}.getType());
    assertNotNull(objConstructor);
  }

  @Test
  public void testGetInstanceWithInstanceCreator() {
    InstanceCreator<LinkedList<String>> instanceCreator = new InstanceCreator<LinkedList<String>>() {
      @Override public LinkedList<String> createInstance(Type type) {
        return new LinkedList<String>();
      }
    };
    ConstructorConstructor constructor = new ConstructorConstructor(
        new HashMap<Type, InstanceCreator<?>>() {{
          put(LinkedList.class, instanceCreator);
        }});
    ObjectConstructor<LinkedList<String>> objConstructor = constructor.get(new TypeToken<LinkedList<String>>(){}.getType());
    assertNotNull(objConstructor);
  }

  @Test
  public void testGetInstanceWithDefaultImplementationConstructor() {
    ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<SortedSet<String>> objConstructor = constructor.get(new TypeToken<SortedSet<String>>(){}.getType());
    assertNotNull(objConstructor);
  }

  @Test
  public void testGetInstanceWithUnsafeConstructor() {
    ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<TreeMap<String, Integer>> objConstructor = constructor.get(new TypeToken<TreeMap<String, Integer>>(){}.getType());
    assertNotNull(objConstructor);
  }

  @Test(expected = JsonIOException.class)
  public void testGetInstanceWithInvalidEnumSetType() {
    ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<EnumSet<String>> objConstructor = constructor.get(new TypeToken<EnumSet<String>>(){}.getType());
    objConstructor.construct();
  }

  @Test(expected = JsonIOException.class)
  public void testGetInstanceWithInvalidMapType() {
    ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());
    ObjectConstructor<Map<Object, Double>> objConstructor = constructor.get(new TypeToken<Map<Object, Double>>(){}.getType());
    objConstructor.construct();
  }
}