package com.google.gson.internal;

import static com.google.gson.internal.Util.TypeToken;
import static org.junit.Assert.*;

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

import org.junit.Test;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstance() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    // Test getting an instance creator for a type
    Type intType = TypeToken.getIntType();
    ObjectConstructor<Integer> intConstructor = constructorConstructor.get(TypeToken.get(Integer.class));
    assertEquals(intType, intConstructor.getType());
    assertEquals(Integer.class, intConstructor.getRawType());
    assertEquals(Integer.class, intConstructor.construct());

    // Test getting an instance creator for a raw type
    Type intRawType = Integer.TYPE;
    ObjectConstructor<Integer> intRawConstructor = constructorConstructor.get(TypeToken.get(intRawType));
    assertEquals(intType, intRawConstructor.getType());
    assertEquals(Integer.class, intRawConstructor.getRawType());
    assertEquals(Integer.class, intRawConstructor.construct());

    // Test getting the default constructor for a type
    Type stringType = TypeToken.getStringType();
    ObjectConstructor<String> stringConstructor = constructorConstructor.get(TypeToken.get(String.class));
    assertNull(stringConstructor);

    // Test getting the default constructor for a raw type
    Type stringRawType = String.class;
    ObjectConstructor<String> stringRawConstructor = constructorConstructor.get(TypeToken.get(stringRawType));
    assertNotNull(stringRawConstructor);
    assertEquals(String.class, stringRawConstructor.getRawType());
    assertEquals("", stringRawConstructor.construct());

    // Test getting the default implementation constructor for a Map<String, Integer>
    Type mapType = TypeToken.getParameterized(Map.class, String.class, Integer.class);
    ObjectConstructor<Map<String, Integer>> mapConstructor = constructorConstructor.get(mapType);
    assertNotNull(mapConstructor);
    assertEquals(Map.class, mapConstructor.getRawType());
    assertEquals(Map.class, mapConstructor.getType().getComponentType().getComponentType());
    assertEquals(LinkedHashMap.class, mapConstructor.construct().getClass());

    // Test getting the default implementation constructor for a SortedMap<String, Integer>
    Type sortedMapType = TypeToken.getParameterized(SortedMap.class, String.class, Integer.class);
    ObjectConstructor<SortedMap<String, Integer>> sortedMapConstructor = constructorConstructor.get(sortedMapType);
    assertNotNull(sortedMapConstructor);
    assertEquals(SortedMap.class, sortedMapConstructor.getRawType());
    assertEquals(TreeMap.class, sortedMapConstructor.construct().getClass());

    // Test getting the default implementation constructor for a List<String>
    Type listType = TypeToken.getParameterized(List.class, String.class);
    ObjectConstructor<List<String>> listConstructor = constructorConstructor.get(listType);
    assertNotNull(listConstructor);
    assertEquals(List.class, listConstructor.getRawType());
    assertEquals(ArrayList.class, listConstructor.construct().getClass());

    // Test getting the default implementation constructor for a Set<String>
    Type setType = TypeToken.getParameterized(Set.class, String.class);
    ObjectConstructor<Set<String>> setConstructor = constructorConstructor.get(setType);
    assertNotNull(setConstructor);
    assertEquals(Set.class, setConstructor.getRawType());
    assertEquals(LinkedHashSet.class, setConstructor.construct().getClass());

    // Test getting the default implementation constructor for an EnumSet<String>
    Type enumSetType = TypeToken.getParameterized(EnumSet.class, String.class);
    ObjectConstructor<EnumSet<String>> enumSetConstructor = constructorConstructor.get(enumSetType);
    assertNotNull(enumSetConstructor);
    assertEquals(EnumSet.class, enumSetConstructor.getRawType());
    assertNotNull(enumSetConstructor.construct());

    // Test getting the default implementation constructor for a Queue<String>
    Type queueType = TypeToken.getParameterized(Queue.class, String.class);
    ObjectConstructor<Queue<String>> queueConstructor = constructorConstructor.get(queueType);
    assertNotNull(queueConstructor);
    assertEquals(Queue.class, queueConstructor.getRawType());
    assertEquals(LinkedList.class, queueConstructor.construct().getClass());
  }

  @Test(expected = RuntimeException.class)
  public void testUnsupportedType() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Type unsupportedType = new TypeToken<Object>() {}.getType();
    ObjectConstructor<Object> unsupportedConstructor = constructorConstructor.get(TypeToken.get(unsupportedType));
    assertNull(unsupportedConstructor);
  }
}