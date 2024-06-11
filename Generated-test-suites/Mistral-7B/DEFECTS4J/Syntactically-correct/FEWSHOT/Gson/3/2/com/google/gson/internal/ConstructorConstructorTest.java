package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
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

public class ConstructorConstructorTest {

  private ConstructorConstructor constructorConstructor;

  @SuppressWarnings("unchecked")
  @Before
  public void setup() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<ArrayList<String>> arrayListCreator = new ArrayListInstanceCreator<>();
    instanceCreators.put(new TypeToken<ArrayList<String>>(){}.getType(), arrayListCreator);
    InstanceCreator<LinkedHashMap<String, Object>> linkedHashMapCreator = new LinkedHashMapInstanceCreator<>();
    instanceCreators.put(new TypeToken<LinkedHashMap<String, Object>>(){}.getType(), linkedHashMapCreator);
    constructorConstructor = new ConstructorConstructor(instanceCreators);
  }

  @Test
  public void testGetInstance() {
    ObjectConstructor<ArrayList<String>> arrayListConstructor = constructorConstructor.get(new TypeToken<ArrayList<String>>(){}.getType());
    assertInstanceOf(ArrayList.class, arrayListConstructor.construct());
  }

  @Test
  public void testGetInstanceByRawType() {
    ObjectConstructor<ArrayList<String>> arrayListConstructor = constructorConstructor.get(new TypeToken<ArrayList<String>>(){}.getType());
    ObjectConstructor<ArrayList<Integer>> intArrayListConstructor = constructorConstructor.get(ArrayList.class);
    assertInstanceOf(ArrayList.class, arrayListConstructor.construct());
    assertInstanceOf(ArrayList.class, intArrayListConstructor.construct());
  }

  @Test
  public void testGetInstanceByInstanceCreator() {
    ObjectConstructor<LinkedHashMap<String, Object>> linkedHashMapConstructor = constructorConstructor.get(new TypeToken<LinkedHashMap<String, Object>>(){}.getType());
    assertInstanceOf(LinkedHashMap.class, linkedHashMapConstructor.construct());
  }

  @Test
  public void testGetInstanceByRawTypeInstanceCreator() {
    ObjectConstructor<LinkedHashMap<String, Object>> linkedHashMapConstructor = constructorConstructor.get(LinkedHashMap.class);
    assertInstanceOf(LinkedHashMap.class, linkedHashMapConstructor.construct());
  }

  @Test
  public void testGetDefaultConstructor() {
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(String.class);
    assertNotNull(stringConstructor);
    String result = stringConstructor.construct();
    assertNotNull(result);
  }

  @Test
  public void testGetDefaultImplementationConstructor() {
    ObjectConstructor<ArrayList<String>> arrayListConstructor = constructorConstructor.get(new TypeToken<ArrayList<String>>(){}.getType());
    assertNotNull(arrayListConstructor);
    ArrayList<String> result = (ArrayList<String>) arrayListConstructor.construct();
    assertNotNull(result);
    assertNotNull(result.isEmpty());
  }

  @Test
  public void testGetNull() {
    ObjectConstructor<String> nullConstructor = constructorConstructor.get(null);
    assertNull(nullConstructor);
  }

  @Test(expected = JsonIOException.class)
  public void testGetInvalidEnumSet() {
    ObjectConstructor<EnumSet<String>> enumSetConstructor = constructorConstructor.get(new TypeToken<EnumSet<String>>(){}.getType());
    assertNull(enumSetConstructor);
  }

  @Test(expected = JsonIOException.class)
  public void testGetInvalidMap() {
    ObjectConstructor<Map<String, Object>> mapConstructor = constructorConstructor.get(new TypeToken<Map<String, Object>>(){}.getType());
    assertNull(mapConstructor);
  }

  @Test(expected = RuntimeException.class)
  public void testGetNoArgsConstructorFailure() {
    ObjectConstructor<Object> objectConstructor = constructorConstructor.get(Object.class);
    objectConstructor.construct();
  }

  @Test
  public void testToString() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    InstanceCreator<ArrayList<String>> arrayListCreator = new ArrayListInstanceCreator<>();
    instanceCreators.put(new TypeToken<ArrayList<String>>(){}.getType(), arrayListCreator);
    InstanceCreator<LinkedHashMap<String, Object>> linkedHashMapCreator = new LinkedHashMapInstanceCreator<>();
    instanceCreators.put(new TypeToken<LinkedHashMap<String, Object>>(){}.getType(), linkedHashMapCreator);
    ConstructorConstructor constructor = new ConstructorConstructor(instanceCreators);
    String toString = constructor.toString();
    assertNotNull(toString);
  }
}

class ArrayListInstanceCreator implements InstanceCreator<ArrayList<String>> {
  @Override public ArrayList<String> createInstance(Type type) {
    return new ArrayList<String>();
  }
}

class LinkedHashMapInstanceCreator implements InstanceCreator<LinkedHashMap<String, Object>> {
  @Override public LinkedHashMap<String, Object> createInstance(Type type) {
    return new LinkedHashMap<String, Object>();
  }
}