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

/**
 * Tests for {@link ConstructorConstructor} class.
 */
public final class ConstructorConstructorTest {

  @Test
  public void testGetInstanceCreatorForCollection() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeCollection typeCollection = new TypeToken<Collection<String>>() {}.getType();

    ObjectConstructor<Collection<String>> actual = constructorConstructor.get(typeCollection);

    assertNotNull(actual);
  }

  @Test
  public void testGetInstanceCreatorForEnumSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeEnumSet typeEnumSet = new TypeToken<EnumSet<Color>>() {}.getType();

    ObjectConstructor<EnumSet<Color>> actual = constructorConstructor.get(typeEnumSet);

    assertNotNull(actual);
  }

  @Test
  public void testGetInstanceCreatorForMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeMap typeMap = new TypeToken<Map<Integer, String>>() {}.getType();

    ObjectConstructor<Map<Integer, String>> actual = constructorConstructor.get(typeMap);

    assertNotNull(actual);
  }

  @Test
  public void testGetInstanceCreatorForSortedMap() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeSortedMap typeSortedMap = new TypeToken<SortedMap<Integer, String>>() {}.getType();

    ObjectConstructor<SortedMap<Integer, String>> actual = constructorConstructor.get(typeSortedMap);

    assertNotNull(actual);
  }

  @Test
  public void testGetInstanceCreatorForSet() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeSet typeSet = new TypeToken<Set<Integer>>() {}.getType();

    ObjectConstructor<Set<Integer>> actual = constructorConstructor.get(typeSet);

    assertNotNull(actual);
  }

  @Test
  public void testGetInstanceCreatorForQueue() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeQueue typeQueue = new TypeToken<Queue<Integer>>() {}.getType();

    ObjectConstructor<Queue<Integer>> actual = constructorConstructor.get(typeQueue);

    assertNotNull(actual);
  }

  @Test
  public void testGetInstanceCreatorForMapWithStringKey() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeMapWithStringKey typeMapWithStringKey = new TypeToken<Map<String, String>>() {}.getType();

    ObjectConstructor<Map<String, String>> actual = constructorConstructor.get(typeMapWithStringKey);

    assertNotNull(actual);
  }

  @Test
  public void testGetInstanceCreatorForCollectionWithGenericType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeCollectionWithGenericType typeCollectionWithGenericType = new TypeToken<Collection<Person>>() {}.getType();

    ObjectConstructor<Collection<Person>> actual = constructorConstructor.get(typeCollectionWithGenericType);

    assertNotNull(actual);
  }

  @Test
  public void testGetInstanceCreatorForMapWithCustomKeyType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeMapWithCustomKeyType typeMapWithCustomKeyType = new TypeToken<Map<CustomKey, Person>>() {}.getType();

    ObjectConstructor<Map<CustomKey, Person>> actual = constructorConstructor.get(typeMapWithCustomKeyType);

    assertNotNull(actual);
  }

  @Test
  public void testGetInstanceCreatorForUnknownType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeUnknown typeUnknown = new TypeToken<UnknownType>() {}.getType();

    ObjectConstructor<UnknownType> actual = constructorConstructor.get(typeUnknown);

    assertNull(actual);
  }

  @Test
  public void testGetInstanceCreatorForUnknownRawType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeUnknownRawType typeUnknownRawType = new TypeToken<UnknownTypeRaw>() {}.getType();

    ObjectConstructor<UnknownTypeRaw> actual = constructorConstructor.get(typeUnknownRawType);

    assertNull(actual);
  }
}