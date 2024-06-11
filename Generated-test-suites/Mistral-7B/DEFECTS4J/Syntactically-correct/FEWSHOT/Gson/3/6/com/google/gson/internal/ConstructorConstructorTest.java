package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

/**
 * Test class for ConstructorConstructor
 */
public final class ConstructorConstructorTest {

  private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

  private static final Type EMPTY_TYPE = new TypeToken<Object>() {}.getType();

  @Test
  public void testGetInstanceCreatorMapIsNotNull() {
    // Given a ConstructorConstructor instance
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());

    // Then the instanceCreators map is not null
    assertNotNull(constructorConstructor.instanceCreators);
  }

  @Test(expected = NullPointerException.class)
  public void testGetNullInstanceCreatorMap() {
    // Given a ConstructorConstructor instance with null instanceCreators map
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);

    // Then a NullPointerException is thrown
    constructorConstructor.get(null);
  }

  @Test
  public void testGetTypeTokenInstanceCreator() {
    // Given a ConstructorConstructor instance and a TypeToken
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};

    // When get method is called with the TypeToken
    ObjectConstructor<String> constructor = constructorConstructor.get(stringTypeToken);

    // Then a constructor instance is returned
    assertNotNull(constructor);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidTypeToken() {
    // Given a ConstructorConstructor instance and an invalid TypeToken
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeToken<Object> invalidTypeToken = new TypeToken<Object>() {};

    // When get method is called with the invalid TypeToken
    constructorConstructor.get(invalidTypeToken);

    // Then an IllegalArgumentException is thrown
  }

  @Test
  public void testGetTypeInstanceCreator() {
    // Given a ConstructorConstructor instance and a Type
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    Type stringType = String.class;

    // When get method is called with the Type
    ObjectConstructor<String> constructor = constructorConstructor.get(new TypeToken<String>() {}.getType());

    // Then a constructor instance is returned
    assertNotNull(constructor);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidType() {
    // Given a ConstructorConstructor instance and an invalid Type
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    Type invalidType = new Object() {};

    // When get method is called with the invalid Type
    constructorConstructor.get(invalidType);

    // Then an IllegalArgumentException is thrown
  }

  @Test
  public void testGetRawTypeInstanceCreator() {
    // Given a ConstructorConstructor instance and a TypeToken with raw type
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    TypeToken<ArrayList<String>> listTypeToken = new TypeToken<ArrayList<String>>() {};

    // When get method is called with the TypeToken
    ObjectConstructor<ArrayList<String>> constructor = constructorConstructor.get(listTypeToken);

    // Then a constructor instance is returned
    assertNotNull(constructor);
  }

  @Test
  public void testGetDefaultConstructor() {
    // Given a ConstructorConstructor instance and a Type
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    Type intType = Integer.TYPE;

    // When get method is called with the Type
    ObjectConstructor<Integer> constructor = constructorConstructor.get(new TypeToken<Integer>() {}.getType());

    // Then a default constructor instance is returned if available
    assertNotNull(constructor);
  }

  @Test
  public void testGetDefaultImplementationConstructor() {
    // Given a ConstructorConstructor instance and a Type
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    Type collectionType = Collection.class;

    // When get method is called with the Type
    ObjectConstructor<Collection<?>> constructor = constructorConstructor.get(new TypeToken<Collection<?>>() {}.getType());

    // Then a default implementation constructor instance is returned if available
    assertNotNull(constructor);
  }

  @Test
  public void testGetUnsafeAllocator() {
    // Given a ConstructorConstructor instance and a Type
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    Type intType = Integer.TYPE;

    // When get method is called with the Type
    ObjectConstructor<Integer> constructor = constructorConstructor.get(new TypeToken<Integer>() {}.getType());

    // Then an unsafe allocator instance is returned if no other constructor is found
    assertNotNull(constructor);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetInvalidConstructor() {
    // Given a ConstructorConstructor instance and an invalid Type
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    Type invalidType = new Object() {};

    // When get method is called with the invalid Type
    ObjectConstructor<Object> constructor = constructorConstructor.get(invalidType);

    // Then an IllegalStateException is thrown
  }

  @Test
  public void testToString() {
    // Given a ConstructorConstructor instance
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());

    // When toString method is called
    String result = constructorConstructor.toString();

    // Then the result is a string representation of the instanceCreators map
    assertNotNull(result);
  }
}