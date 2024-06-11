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

public final class ConstructorConstructorTest {

  @SuppressWarnings("unchecked")
  private static InstanceCreator<ArrayList<String>> listStringInstanceCreator =
      new InstanceCreator<ArrayList<String>>() {
        @Override public ArrayList<String> createInstance(Type type) {
          return new ArrayList<String>();
        }
      };

  @SuppressWarnings("unchecked")
  private static InstanceCreator<LinkedHashMap<String, Object>> mapInstanceCreator =
      new InstanceCreator<LinkedHashMap<String, Object>>() {
        @Override public LinkedHashMap<String, Object> createInstance(Type type) {
          return new LinkedHashMap<String, Object>();
        }
      };

  @SuppressWarnings("unchecked")
  private static InstanceCreator<TreeMap<String, Object>> sortedMapInstanceCreator =
      new InstanceCreator<TreeMap<String, Object>>() {
        @Override public TreeMap<String, Object> createInstance(Type type) {
          return new TreeMap<String, Object>();
        }
      };

  @SuppressWarnings("unchecked")
  private static InstanceCreator<LinkedHashSet<Object>> setInstanceCreator =
      new InstanceCreator<LinkedHashSet<Object>>() {
        @Override public LinkedHashSet<Object> createInstance(Type type) {
          return new LinkedHashSet<Object>();
        }
      };

  @SuppressWarnings("unchecked")
  private static InstanceCreator<LinkedList<Object>> queueInstanceCreator =
      new InstanceCreator<LinkedList<Object>>() {
        @Override public LinkedList<Object> createInstance(Type type) {
          return new LinkedList<Object>();
        }
      };

  @SuppressWarnings("unchecked")
  private static InstanceCreator<TreeSet<Object>> sortedSetInstanceCreator =
      new InstanceCreator<TreeSet<Object>>() {
        @Override public TreeSet<Object> createInstance(Type type) {
          return new TreeSet<Object>();
        }
      };

  @SuppressWarnings("unchecked")
  private static InstanceCreator<EnumSet<Object>> enumSetInstanceCreator =
      new InstanceCreator<EnumSet<Object>>() {
        @Override public EnumSet<Object> createInstance(Type type) {
          if (type instanceof ParameterizedType) {
            Type elementType = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (elementType instanceof Class) {
              return EnumSet.noneOf((Class<?>) elementType);
            } else {
              throw new JsonIOException("Invalid EnumSet type: " + type.toString());
            }
          } else {
            throw new JsonIOException("Invalid EnumSet type: " + type.toString());
          }
        }
      };

  @SuppressWarnings("unchecked")
  private static InstanceCreator<ArrayList<Object>> defaultArrayListInstanceCreator =
      new InstanceCreator<ArrayList<Object>>() {
        @Override public ArrayList<Object> createInstance(Type type) {
          return new ArrayList<Object>();
        }
      };

  @SuppressWarnings("unchecked")
  private static InstanceCreator<LinkedList<Object>> defaultLinkedListInstanceCreator =
      new InstanceCreator<LinkedList<Object>>() {
        @Override public LinkedList<Object> createInstance(Type type) {
          return new LinkedList<Object>();
        }
      };

  @SuppressWarnings("unchecked")
  private static InstanceCreator<LinkedHashMap<Object, Object>> defaultLinkedHashMapInstanceCreator =
      new InstanceCreator<LinkedHashMap<Object, Object>>() {
        @Override public LinkedHashMap<Object, Object> createInstance(Type type) {
          return new LinkedHashMap<Object, Object>();
        }
      };

  @SuppressWarnings("unchecked")
  private static InstanceCreator<TreeMap<Object, Object>> defaultTreeMapInstanceCreator =
      new InstanceCreator<TreeMap<Object, Object>>() {
        @Override public TreeMap<Object, Object> createInstance(Type type) {
          return new TreeMap<Object, Object>();
        }
      };

  @SuppressWarnings("unchecked")
  private static InstanceCreator<ArrayList<Object>> defaultArrayListInstanceCreatorWithStringKey =
      new InstanceCreator<ArrayList<Object>>() {
        @Override public ArrayList<Object> createInstance(Type type) {
          return new ArrayList<Object>();
        }
      };

  @Test
  public void testGetInstanceWithArrayListTypeToken() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new HashMap<Type, InstanceCreator<?>>() {{
          put(new TypeToken<ArrayList<Object>>(){}, listStringInstanceCreator);
          put(new TypeToken<ArrayList<String>>(){}, listStringInstanceCreator);
          put(new TypeToken<ArrayList<Integer>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Double>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Boolean>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Character>>(){}, defaultArrayListInstanceCreator);
        }});

    ObjectConstructor<ArrayList<Object>> constructor = constructorConstructor.get(
        new TypeToken<ArrayList<Object>>(){});
    assertNotNull(constructor);
    assertNull(constructor.construct());
  }

  @Test
  public void testGetInstanceWithArrayListStringTypeToken() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new HashMap<Type, InstanceCreator<?>>() {{
          put(new TypeToken<ArrayList<Object>>(){}, listStringInstanceCreator);
          put(new TypeToken<ArrayList<String>>(){}, listStringInstanceCreator);
          put(new TypeToken<ArrayList<Integer>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Double>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Boolean>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Character>>(){}, defaultArrayListInstanceCreator);
        }});

    ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(
        new TypeToken<ArrayList<String>>(){});
    assertNotNull(constructor);
    assertNull(constructor.construct());
  }

  @Test
  public void testGetInstanceWithArrayListIntegerTypeToken() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new HashMap<Type, InstanceCreator<?>>() {{
          put(new TypeToken<ArrayList<Object>>(){}, listStringInstanceCreator);
          put(new TypeToken<ArrayList<String>>(){}, listStringInstanceCreator);
          put(new TypeToken<ArrayList<Integer>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Double>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Boolean>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Character>>(){}, defaultArrayListInstanceCreator);
        }});

    ObjectConstructor<ArrayList<Integer>> constructor = constructorConstructor.get(
        new TypeToken<ArrayList<Integer>>(){});
    assertNotNull(constructor);
    assertNull(constructor.construct());
  }

  @Test
  public void testGetInstanceWithArrayListDoubleTypeToken() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new HashMap<Type, InstanceCreator<?>>() {{
          put(new TypeToken<ArrayList<Object>>(){}, listStringInstanceCreator);
          put(new TypeToken<ArrayList<String>>(){}, listStringInstanceCreator);
          put(new TypeToken<ArrayList<Integer>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Double>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Boolean>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Character>>(){}, defaultArrayListInstanceCreator);
        }});

    ObjectConstructor<ArrayList<Double>> constructor = constructorConstructor.get(
        new TypeToken<ArrayList<Double>>(){});
    assertNotNull(constructor);
    assertNull(constructor.construct());
  }

  @Test
  public void testGetInstanceWithArrayListBooleanTypeToken() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new HashMap<Type, InstanceCreator<?>>() {{
          put(new TypeToken<ArrayList<Object>>(){}, listStringInstanceCreator);
          put(new TypeToken<ArrayList<String>>(){}, listStringInstanceCreator);
          put(new TypeToken<ArrayList<Integer>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Double>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Boolean>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Character>>(){}, defaultArrayListInstanceCreator);
        }});

    ObjectConstructor<ArrayList<Boolean>> constructor = constructorConstructor.get(
        new TypeToken<ArrayList<Boolean>>(){});
    assertNotNull(constructor);
    assertNull(constructor.construct());
  }

  @Test
  public void testGetInstanceWithArrayListCharacterTypeToken() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new HashMap<Type, InstanceCreator<?>>() {{
          put(new TypeToken<ArrayList<Object>>(){}, listStringInstanceCreator);
          put(new TypeToken<ArrayList<String>>(){}, listStringInstanceCreator);
          put(new TypeToken<ArrayList<Integer>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Double>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Boolean>>(){}, defaultArrayListInstanceCreator);
          put(new TypeToken<ArrayList<Character>>(){}, defaultArrayListInstanceCreator);
        }});

    ObjectConstructor<ArrayList<Character>> constructor = constructorConstructor.get(
        new TypeToken<ArrayList<Character>>(){});
    assertNotNull(constructor);
    assertNull(constructor.construct());
  }

  // Test other types similarly here...

  @Test
  public void testGetInstanceWithMapTypeToken() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new HashMap<Type, InstanceCreator<?>>() {{
          put(new TypeToken<Map<Object, Object>>(){}, defaultLinkedHashMapInstanceCreator);
          put(new TypeToken<Map<Object, Object>>(){}, defaultTreeMapInstanceCreator);
          put(new TypeToken<Map<Object, Object>>(){}, defaultLinkedHashMapInstanceCreator);
          put(new TypeToken<Map<String, Object>>(){}, mapInstanceCreator);
          put(new TypeToken<Map<String, String>>(){}, mapInstanceCreator);
          put(new TypeToken<Map<String, Integer>>(){}, mapInstanceCreator);
          put(new TypeToken<Map<String, Double>>(){}, mapInstanceCreator);
          put(new TypeToken<Map<String, Boolean>>(){}, mapInstanceCreator);
          put(new TypeToken<Map<String, Character>>(){}, mapInstanceCreator);
        }});

    ObjectConstructor<Map<Object, Object>> constructor = constructorConstructor.get(
        new TypeToken<Map<Object, Object>>(){});
    assertNotNull(constructor);
    assertNull(constructor.construct());
  }

  // Test other Map types similarly here...

  @Test
  public void testGetInstanceWithSortedMapTypeToken() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new HashMap<Type, InstanceCreator<?>>() {{
          put(new TypeToken<SortedMap<Object, Object>>(){}, defaultTreeMapInstanceCreator);
          put(new TypeToken<SortedMap<Object, Object>>(){}, sortedMapInstanceCreator);
          put(new TypeToken<SortedMap<Object, Object>>(){}, defaultTreeMapInstanceCreator);
          put(new TypeToken<SortedMap<String, Object>>(){}, sortedMapInstanceCreator);
          put(new TypeToken<SortedMap<String, String>>(){}, sortedMapInstanceCreator);
          put(new TypeToken<SortedMap<String, Integer>>(){}, sortedMapInstanceCreator);
          put(new TypeToken<SortedMap<String, Double>>(){}, sortedMapInstanceCreator);
          put(new TypeToken<SortedMap<String, Boolean>>(){}, sortedMapInstanceCreator);
          put(new TypeToken<SortedMap<String, Character>>(){}, sortedMapInstanceCreator);
        }});

    ObjectConstructor<SortedMap<Object, Object>> constructor = constructorConstructor.get(
        new TypeToken<SortedMap<Object, Object>>(){});
    assertNotNull(constructor);
    assertNull(constructor.construct());
  }

  // Test other SortedMap types similarly here...

  @Test
  public void testGetInstanceWithSetTypeToken() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new HashMap<Type, InstanceCreator<?>>() {{
          put(new TypeToken<Set<Object>>(){}, setInstanceCreator);
          put(new TypeToken<Set<Object>>(){}, sortedSetInstanceCreator);
          put(new TypeToken<Set<Object>>(){}, setInstanceCreator);
          put(new TypeToken<Set<String>>(){}, setInstanceCreator);
          put(new TypeToken<Set<String>>(){}, sortedSetInstanceCreator);
          put(new TypeToken<Set<Integer>>(){}, setInstanceCreator);
          put(new TypeToken<Set<Double>>(){}, setInstanceCreator);
          put(new TypeToken<Set<Boolean>>(){}, setInstanceCreator);
          put(new TypeToken<Set<Character>>(){}, setInstanceCreator);
        }});

    ObjectConstructor<Set<Object>> constructor = constructorConstructor.get(
        new TypeToken<Set<Object>>(){});
    assertNotNull(constructor);
    assertNull(constructor.construct());
  }

  // Test other Set types similarly here...

  @Test
  public void testGetInstanceWithQueueTypeToken() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new HashMap<Type, InstanceCreator<?>>() {{
          put(new TypeToken<Queue<Object>>(){}, queueInstanceCreator);
          put(new TypeToken<Queue<Object>>(){}, queueInstanceCreator);
          put(new TypeToken<Queue<Object>>(){}, queueInstanceCreator);
          put(new TypeToken<Queue<String>>(){}, queueInstanceCreator);
          put(new TypeToken<Queue<String>>(){}, queueInstanceCreator);
          put(new TypeToken<Queue<Integer>>(){}, queueInstanceCreator);
          put(new TypeToken<Queue<Double>>(){}, queueInstanceCreator);
          put(new TypeToken<Queue<Boolean>>(){}, queueInstanceCreator);
          put(new TypeToken<Queue<Character>>(){}, queueInstanceCreator);
        }});

    ObjectConstructor<Queue<Object>> constructor = constructorConstructor.get(
        new TypeToken<Queue<Object>>(){});
    assertNotNull(constructor);
    assertNull(constructor.construct());
  }

  // Test other Queue types similarly here...

  @Test
  public void testGetInstanceWithEnumSetTypeToken() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        new HashMap<Type, InstanceCreator<?>>() {{
          put(new TypeToken<EnumSet<Object>>(){}, enumSetInstanceCreator);
          put(new TypeToken<EnumSet<String>>(){}, enumSetInstanceCreator);
          put(new TypeToken<EnumSet<Integer>>(){}, enumSetInstanceCreator);
          put(new TypeToken<EnumSet<Double>>(){}, enumSetInstanceCreator);
          put(new TypeToken<EnumSet<Boolean>>(){}, enumSetInstanceCreator);
          put(new TypeToken<EnumSet<Character>>(){}, enumSetInstanceCreator);
        }});

    ObjectConstructor<EnumSet<Object>> constructor = constructorConstructor.get(
        new TypeToken<EnumSet<Object>>(){});
    assertNotNull(constructor);
    assertNull(constructor.construct());
  }

  // Test other EnumSet types similarly here...
}