package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.InstanceCreator;

import org.junit.Before;
import org.junit.Test;

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
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

  private ConstructorConstructor constructorConstructor;
  
  @Before
  public void setUp() {
    constructorConstructor = new ConstructorConstructor(null);
  }

  @Test
  public void testGet_withInstanceCreator_returnsInstanceCreatorConstructor() throws Exception {
    // Setup
    final TypeToken<String> typeToken = TypeToken.get(String.class);
    
    // Exercise
    ObjectConstructor<String> result = constructorConstructor.get(typeToken);
    
    // Verify
    Constructor constructor = String.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    String expectedResult = (String) constructor.newInstance();
    
    assertEquals(expectedResult, result.construct());
  }
  
  @Test
  public void testGet_withRawTypeInstanceCreator_returnsRawTypeInstanceCreatorConstructor() throws Exception {
    // Setup
    final TypeToken<StringBuffer> typeToken = TypeToken.get(StringBuffer.class);
    
    // Exercise
    ObjectConstructor<String> result = constructorConstructor.get(typeToken);
    
    // Verify
    Constructor constructor = StringBuffer.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    StringBuffer expectedResult = (StringBuffer) constructor.newInstance();
    
    assertEquals(expectedResult, result.construct());
  }
  
  @Test
  public void testGet_withDefaultConstructor_returnsDefaultConstructor() throws Exception {
    // Setup
    final TypeToken<ArrayList> typeToken = TypeToken.get(ArrayList.class);
    
    // Exercise
    ObjectConstructor<ArrayList> result = constructorConstructor.get(typeToken);
    
    // Verify
    Constructor constructor = ArrayList.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    ArrayList expectedResult = (ArrayList) constructor.newInstance();
    
    assertEquals(expectedResult, result.construct());
  }
  
  @Test
  public void testGet_withDefaultImplementationConstructor_returnsDefaultImplementationConstructor() throws Exception {
    // Setup
    final TypeToken<Set> typeToken = TypeToken.get(Set.class);
    
    // Exercise
    ObjectConstructor<Set> result = constructorConstructor.get(typeToken);
    
    // Verify
    LinkedHashSet expectedResult = new LinkedHashSet();
    
    assertEquals(expectedResult, result.construct());
  }
  
  @Test
  public void testGet_withInvalidEnumSetType_throwsJsonIOException() throws Exception {
    // Setup
    final TypeToken<EnumSet> typeToken = TypeToken.get(EnumSet.class);
    
    // Exercise and Verify
    try {
      constructorConstructor.get(typeToken);
    } catch (JsonIOException e) {
      assertEquals("Invalid EnumSet type: " + typeToken.getType().toString(), e.getMessage());
    }
  }
  
  @Test
  public void testGet_withUnknownType_returnsUnsafeAllocatorConstructor() throws Exception {
    // Setup
    final TypeToken<UnknownType> typeToken = TypeToken.get(UnknownType.class);
    
    // Exercise
    ObjectConstructor<UnknownType> result = constructorConstructor.get(typeToken);
    
    // Verify
    UnknownType expectedResult = new UnknownType();
    
    assertEquals(expectedResult, result.construct());
  }
  
  class UnknownType {
    public UnknownType() {}
  }
  
}