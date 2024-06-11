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
  public void testGetConstructorForCollectionTypes() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    TypeToken<ArrayList<String>> stringArrayListTypeToken = TypeToken.getParameterized(ArrayList.class, String.class);
    ObjectConstructor<ArrayList<String>> actual = constructorConstructor.get(stringArrayListTypeToken);
    assertNotNull(actual);

    @SuppressWarnings("unchecked")
    ArrayList<String> expected = new ArrayList<>();
    assertEquals(expected, actual.construct());

    TypeToken<LinkedList<Integer>> integerLinkedListTypeToken = TypeToken.getParameterized(LinkedList.class, Integer.class);
    ObjectConstructor<LinkedList<Integer>> actualLinkedList = constructorConstructor.get(integerLinkedListTypeToken);
    assertNotNull(actualLinkedList);

    @SuppressWarnings("unchecked")
    LinkedList<Integer> expectedLinkedList = new LinkedList<>();
    assertEquals(expectedLinkedList, actualLinkedList.construct());

    TypeToken<LinkedHashSet<Character>> characterLinkedHashSetTypeToken = TypeToken.getParameterized(LinkedHashSet.class, Character.class);
    ObjectConstructor<LinkedHashSet<Character>> actualLinkedHashSet = constructorConstructor.get(characterLinkedHashSetTypeToken);
    assertNotNull(actualLinkedHashSet);

    @SuppressWarnings("unchecked")
    LinkedHashSet<Character> expectedLinkedHashSet = new LinkedHashSet<>();
    assertEquals(expectedLinkedHashSet, actualLinkedHashSet.construct());

    TypeToken<TreeSet<Double>> doubleTreeSetTypeToken = TypeToken.getParameterized(TreeSet.class, Double.class);
    ObjectConstructor<TreeSet<Double>> actualTreeSet = constructorConstructor.get(doubleTreeSetTypeToken);
    assertNotNull(actualTreeSet);

    @SuppressWarnings("unchecked")
    TreeSet<Double> expectedTreeSet = new TreeSet<>();
    assertEquals(expectedTreeSet, actualTreeSet.construct());

    TypeToken<EnumSet<Month>> monthEnumSetTypeToken = TypeToken.getParameterized(EnumSet.class, Month.class);
    ObjectConstructor<EnumSet<Month>> actualEnumSet = constructorConstructor.get(monthEnumSetTypeToken);
    assertNotNull(actualEnumSet);

    EnumSet<Month> expectedEnumSet = EnumSet.noneOf(Month.class);
    assertEquals(expectedEnumSet, actualEnumSet.construct());
  }

  @Test
  public void testGetConstructorForMapTypes() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    TypeToken<LinkedHashMap<String, Float>> stringFloatLinkedHashMapTypeToken = TypeToken.getParameterized(LinkedHashMap.class, String.class, Float.class);
    ObjectConstructor<LinkedHashMap<String, Float>> actual = constructorConstructor.get(stringFloatLinkedHashMapTypeToken);
    assertNotNull(actual);

    @SuppressWarnings("unchecked")
    LinkedHashMap<String, Float> expected = new LinkedHashMap<>();
    assertEquals(expected, actual.construct());

    TypeToken<TreeMap<Integer, Integer>> integerIntegerTreeMapTypeToken = TypeToken.getParameterized(TreeMap.class, Integer.class, Integer.class);
    ObjectConstructor<TreeMap<Integer, Integer>> actualTreeMap = constructorConstructor.get(integerIntegerTreeMapTypeToken);
    assertNotNull(actualTreeMap);

    @SuppressWarnings("unchecked")
    TreeMap<Integer, Integer> expectedTreeMap = new TreeMap<>();
    assertEquals(expectedTreeMap, actualTreeMap.construct());

    TypeToken<LinkedTreeMap<String, Boolean>> stringBooleanLinkedTreeMapTypeToken = TypeToken.getParameterized(LinkedTreeMap.class, String.class, Boolean.class);
    ObjectConstructor<LinkedTreeMap<String, Boolean>> actualLinkedTreeMap = constructorConstructor.get(stringBooleanLinkedTreeMapTypeToken);
    assertNotNull(actualLinkedTreeMap);

    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, Boolean> expectedLinkedTreeMap = new LinkedTreeMap<>();
    assertEquals(expectedLinkedTreeMap, actualLinkedTreeMap.construct());
  }

  @Test
  public void testGetConstructorForRawType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    TypeToken<Number> numberTypeToken = TypeToken.get(Number.class);
    ObjectConstructor<Number> actual = constructorConstructor.get(numberTypeToken);
    assertNotNull(actual);

    @SuppressWarnings("unchecked")
    Number expected = 0;
    assertEquals(expected, actual.construct());
  }

  @Test
  public void testGetConstructorForInstanceCreator() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    TestInstanceCreator testInstanceCreator = new TestInstanceCreator();
    constructorConstructor.instanceCreators.put(String.class, testInstanceCreator);
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    ObjectConstructor<String> actual = constructorConstructor.get(stringTypeToken);
    assertNotNull(actual);

    assertEquals("test", actual.construct());
  }

  @Test
  public void testGetConstructorForUnknownType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    TypeToken<UnknownType> unknownTypeToken = TypeToken.get(UnknownType.class);
    ObjectConstructor<UnknownType> actual = constructorConstructor.get(unknownTypeToken);
    assertNull(actual);
  }

  @Test
  public void testGetConstructorForNonEnumerableType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    TypeToken<NonEnumerableType> nonEnumerableTypeToken = TypeToken.get(NonEnumerableType.class);
    ObjectConstructor<NonEnumerableType> actual = constructorConstructor.get(nonEnumerableTypeToken);
    assertNull(actual);
  }

  @Test(expected = JsonIOException.class)
  public void testGetConstructorForInvalidEnumSetType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    TypeToken<EnumSet<String>> stringEnumSetTypeToken = TypeToken.getParameterized(EnumSet.class, String.class);
    ObjectConstructor<EnumSet<String>> actual = constructorConstructor.get(stringEnumSetTypeToken);
    assertNull(actual);
  }

  @Test(expected = JsonIOException.class)
  public void testGetConstructorForInvalidMapType() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
    TypeToken<Map<String, Integer>> stringIntegerMapTypeToken = TypeToken.getParameterized(Map.class, String.class, Integer.class);
    ObjectConstructor<Map<String, Integer>> actual = constructorConstructor.get(stringIntegerMapTypeToken);
    assertNull(actual);
  }

  private static class TestInstanceCreator implements InstanceCreator<String> {
    @Override public String createInstance(Type type) {
      return "test";
    }
  }

  private static class UnknownType {
  }

  private static class NonEnumerableType implements Enum {
  }

  private static enum Month {
    JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
  }
}