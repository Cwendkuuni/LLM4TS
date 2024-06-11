package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

import org.junit.Before;
import org.junit.Test;

public class ConstructorConstructorTest {
  private Map<Type, InstanceCreator<?>> instanceCreators;

  @Before
  public void setUp() {
    instanceCreators = new LinkedHashMap<>();
  }

  @Test
  public void testGetInstanceCreatorForPrimitiveTypes() {
    InstanceCreator<Boolean> booleanInstanceCreator = mock(InstanceCreator.class);
    when(booleanInstanceCreator.createInstance(TypeToken.get(Boolean.TYPE))).thenReturn(Boolean.TRUE);

    instanceCreators.put(TypeToken.get(Boolean.TYPE), booleanInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<Boolean> booleanConstructor = constructorConstructor.get(TypeToken.get(Boolean.TYPE));

    Boolean result = booleanConstructor.construct();

    assertNotNull(result);
    assertTrue(result);
  }

  @Test
  public void testGetInstanceCreatorForWrapperTypes() {
    InstanceCreator<Boolean> booleanInstanceCreator = mock(InstanceCreator.class);
    when(booleanInstanceCreator.createInstance(TypeToken.get(Boolean.class))).thenReturn(Boolean.TRUE);

    instanceCreators.put(TypeToken.get(Boolean.class), booleanInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<Boolean> booleanConstructor = constructorConstructor.get(TypeToken.get(Boolean.class));

    Boolean result = booleanConstructor.construct();

    assertNotNull(result);
    assertTrue(result);
  }

  @Test
  public void testGetInstanceCreatorForString() {
    InstanceCreator<String> stringInstanceCreator = mock(InstanceCreator.class);
    when(stringInstanceCreator.createInstance(TypeToken.get(String.class))).thenReturn("test");

    instanceCreators.put(TypeToken.get(String.class), stringInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(TypeToken.get(String.class));

    String result = stringConstructor.construct();

    assertNotNull(result);
    assertEquals("test", result);
  }

  @Test
  public void testGetInstanceCreatorForList() {
    InstanceCreator<ArrayList<String>> listInstanceCreator = mock(InstanceCreator.class);
    when(listInstanceCreator.createInstance(TypeToken.get(ArrayList.class).getType()))
        .thenReturn(new ArrayList<String>());

    instanceCreators.put(TypeToken.get(ArrayList.class).getType(), listInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<ArrayList<String>> listConstructor = constructorConstructor.get(TypeToken.get(ArrayList.class));

    ArrayList<String> result = listConstructor.construct();

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testGetInstanceCreatorForArrayListOfString() {
    InstanceCreator<ArrayList<String>> listInstanceCreator = mock(InstanceCreator.class);
    when(listInstanceCreator.createInstance(TypeToken.get(ArrayList.class).getType()))
        .thenReturn(new ArrayList<String>());

    instanceCreators.put(TypeToken.get(ArrayList.class).getType(), listInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<ArrayList<String>> listConstructor = constructorConstructor.get(TypeToken.get(List.class).getType());

    ArrayList<String> result = listConstructor.construct();

    assertNotNull(result);
    assertTrue(result instanceof ArrayList);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testGetInstanceCreatorForMap() {
    InstanceCreator<LinkedHashMap<String, Integer>> mapInstanceCreator = mock(InstanceCreator.class);
    when(mapInstanceCreator.createInstance(TypeToken.get(LinkedHashMap.class).getType()))
        .thenReturn(new LinkedHashMap<String, Integer>());

    instanceCreators.put(TypeToken.get(LinkedHashMap.class).getType(), mapInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<LinkedHashMap<String, Integer>> mapConstructor = constructorConstructor.get(TypeToken.get(Map.class).getType());

    Map<String, Integer> result = mapConstructor.construct();

    assertNotNull(result);
    assertTrue(result instanceof LinkedHashMap);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testGetInstanceCreatorForTreeMap() {
    InstanceCreator<TreeMap<String, Integer>> mapInstanceCreator = mock(InstanceCreator.class);
    when(mapInstanceCreator.createInstance(TypeToken.get(TreeMap.class).getType()))
        .thenReturn(new TreeMap<String, Integer>());

    instanceCreators.put(TypeToken.get(TreeMap.class).getType(), mapInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<TreeMap<String, Integer>> mapConstructor = constructorConstructor.get(TypeToken.get(SortedMap.class).getType());

    SortedMap<String, Integer> result = mapConstructor.construct();

    assertNotNull(result);
    assertTrue(result instanceof TreeMap);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testGetInstanceCreatorForLinkedHashSet() {
    InstanceCreator<LinkedHashSet<String>> setInstanceCreator = mock(InstanceCreator.class);
    when(setInstanceCreator.createInstance(TypeToken.get(LinkedHashSet.class).getType()))
        .thenReturn(new LinkedHashSet<String>());

    instanceCreators.put(TypeToken.get(LinkedHashSet.class).getType(), setInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<LinkedHashSet<String>> setConstructor = constructorConstructor.get(TypeToken.get(Set.class).getType());

    Set<String> result = setConstructor.construct();

    assertNotNull(result);
    assertTrue(result instanceof LinkedHashSet);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testGetInstanceCreatorForEnumSet() {
    String myEnum = "MyEnum";
    Class<? extends Enum<MyEnum>> enumClass = MyEnum.class;

    InstanceCreator<EnumSet<MyEnum>> enumSetInstanceCreator = mock(InstanceCreator.class);
    when(enumSetInstanceCreator.createInstance(TypeToken.get(EnumSet.class).getType()))
        .thenReturn(EnumSet.noneOf(enumClass));

    instanceCreators.put(TypeToken.get(EnumSet.class).getType(), enumSetInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<EnumSet<MyEnum>> enumSetConstructor = constructorConstructor.get(TypeToken.get(Set.class).getType());

    Set<MyEnum> result = enumSetConstructor.construct();

    assertNotNull(result);
    assertTrue(result instanceof EnumSet);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testGetInstanceCreatorForSortedSet() {
    String myEnum = "MyEnum";
    Class<? extends Enum<MyEnum>> enumClass = MyEnum.class;

    InstanceCreator<TreeSet<MyEnum>> treeSetInstanceCreator = mock(InstanceCreator.class);
    when(treeSetInstanceCreator.createInstance(TypeToken.get(TreeSet.class).getType()))
        .thenReturn(new TreeSet<MyEnum>());

    instanceCreators.put(TypeToken.get(TreeSet.class).getType(), treeSetInstanceCreator);

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
    ObjectConstructor<TreeSet<MyEnum>> treeSetConstructor = constructorConstructor.get(TypeToken.get(SortedSet.class).getType());

    SortedSet<MyEnum> result = treeSetConstructor.construct();

    assertNotNull(result);
    assertTrue(result instanceof TreeSet);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testGetInstanceCreatorForNull() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    assertNull(constructorConstructor.get(null));
  }
}