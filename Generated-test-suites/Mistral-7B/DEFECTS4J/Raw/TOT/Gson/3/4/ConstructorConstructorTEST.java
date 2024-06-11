package com.example.junit4.constructorconstructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstanceOfArrayList() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new InstanceCreatorFactory());
    TypeToken<ArrayList<Integer>> typeToken = new TypeToken<ArrayList<Integer>>() {};
    ObjectConstructor<ArrayList<Integer>> constructor = constructorConstructor.get(typeToken);
    ArrayList<Integer> list = constructor.construct();
    assertNotNull(list);
    assertEquals(0, list.size());
  }

  @Test
  public void testGetInstanceOfTreeSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new InstanceCreatorFactory());
    TypeToken<TreeSet<String>> typeToken = new TypeToken<TreeSet<String>>() {};
    ObjectConstructor<TreeSet<String>> constructor = constructorConstructor.get(typeToken);
    TreeSet<String> set = constructor.construct();
    assertNotNull(set);
    assertEquals(0, set.size());
  }

  @Test
  public void testGetInstanceOfLinkedHashMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new InstanceCreatorFactory());
    Type type = new TypeToken<LinkedHashMap<String, Integer>>() {}.getType();
    ObjectConstructor<LinkedHashMap<String, Integer>> constructor = constructorConstructor.get(
        new TypeToken<LinkedHashMap<String, Integer>>() {}.getType());
    LinkedHashMap<String, Integer> map = constructor.construct();
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfNullType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new InstanceCreatorFactory());
    ObjectConstructor<Object> constructor = constructorConstructor.get(null);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfUnknownType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new InstanceCreatorFactory());
    TypeToken<Foo> typeToken = new TypeToken<Foo>() {};
    ObjectConstructor<Foo> constructor = constructorConstructor.get(typeToken);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstanceOfUnsupportedType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new InstanceCreatorFactory());
    TypeToken<SortedMap<String, ArrayList<Integer>>> typeToken = new TypeToken<SortedMap<String, ArrayList<Integer>>>() {};
    ObjectConstructor<SortedMap<String, ArrayList<Integer>>> constructor = constructorConstructor.get(typeToken);
  }

  @Test
  public void testGetInstanceOfRawType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new InstanceCreatorFactory());
    TypeToken<ArrayList> typeToken = new TypeToken<ArrayList>() {};
    ObjectConstructor<ArrayList> constructor = constructorConstructor.get(typeToken);
    ArrayList list = constructor.construct();
    assertNotNull(list);
    assertEquals(0, list.size());
  }

  @Test
  public void testGetInstanceOfRawTypeFromInstanceCreator() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new InstanceCreatorFactory());
    Type type = new TypeToken<ArrayList>() {}.getType();
    @SuppressWarnings("unchecked")
    InstanceCreator<ArrayList> instanceCreator = new InstanceCreator<ArrayList>() {
      @Override public ArrayList createInstance(Type type) {
        return new ArrayList<Object>();
      }
    };
    constructorConstructor.instanceCreators.put(type, instanceCreator);
    ObjectConstructor<ArrayList> constructor = constructorConstructor.get(typeToken);
    ArrayList list = constructor.construct();
    assertNotNull(list);
    assertEquals(0, list.size());
  }
}

class InstanceCreatorFactory implements InstanceCreator<Object> {
  @Override public Object createInstance(Type type) {
    return null;
  }
}