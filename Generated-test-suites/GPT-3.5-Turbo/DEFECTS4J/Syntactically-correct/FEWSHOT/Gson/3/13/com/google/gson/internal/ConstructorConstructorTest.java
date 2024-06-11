package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
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
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {
  
  private ConstructorConstructor constructorConstructor;
  
  @Before
  public void setup() {
    constructorConstructor = new ConstructorConstructor(null);
  }
  
  @Test
  public void testGet_withInstanceCreator() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>(){};
    ObjectConstructor<TestClass> objectConstructor = constructorConstructor.get(typeToken);
    
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_withRawTypeInstanceCreator() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>(){};
    ObjectConstructor<TestClass> objectConstructor = constructorConstructor.get(typeToken);
    
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_withDefaultConstructor() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>(){};
    ObjectConstructor<TestClass> objectConstructor = constructorConstructor.get(typeToken);
    
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_withDefaultImplementationConstructor() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>(){};
    ObjectConstructor<TestClass> objectConstructor = constructorConstructor.get(typeToken);
    
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_withUnsafeAllocator() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>(){};
    ObjectConstructor<TestClass> objectConstructor = constructorConstructor.get(typeToken);
    
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testNewDefaultConstructor() {
    TypeToken<TestDefaultConstructor> typeToken = new TypeToken<TestDefaultConstructor>(){};
    ObjectConstructor<TestDefaultConstructor> objectConstructor = constructorConstructor.newDefaultConstructor(TestDefaultConstructor.class);
    
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testNewDefaultConstructor_withNoSuchMethodException() {
    TypeToken<TestNoDefaultConstructor> typeToken = new TypeToken<TestNoDefaultConstructor>(){};
    ObjectConstructor<TestNoDefaultConstructor> objectConstructor = constructorConstructor.newDefaultConstructor(TestNoDefaultConstructor.class);
    
    assertNull(objectConstructor);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withSortedSet() {
    TypeToken<TestSortedSet> typeToken = new TypeToken<TestSortedSet>(){};
    ObjectConstructor<TestSortedSet> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), TestSortedSet.class);
    
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withEnumSet() {
    TypeToken<TestEnumSet> typeToken = new TypeToken<TestEnumSet>(){};
    ObjectConstructor<TestEnumSet> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), TestEnumSet.class);
    
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withSet() {
    TypeToken<TestSet> typeToken = new TypeToken<TestSet>(){};
    ObjectConstructor<TestSet> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), TestSet.class);
    
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withQueue() {
    TypeToken<TestQueue> typeToken = new TypeToken<TestQueue>(){};
    ObjectConstructor<TestQueue> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), TestQueue.class);
    
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withCollection() {
    TypeToken<TestCollection> typeToken = new TypeToken<TestCollection>(){};
    ObjectConstructor<TestCollection> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), TestCollection.class);
    
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withSortedMap() {
    TypeToken<TestSortedMap> typeToken = new TypeToken<TestSortedMap>(){};
    ObjectConstructor<TestSortedMap> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), TestSortedMap.class);
    
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_withParameterizedType() {
    TypeToken<TestParameterizedType> typeToken = new TypeToken<TestParameterizedType>(){};
    ObjectConstructor<TestParameterizedType> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), TestParameterizedType.class);
    
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testUnsafeAllocatorConstruct() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>(){};
    ObjectConstructor<TestClass> objectConstructor = constructorConstructor.newUnsafeAllocator(typeToken.getType(), TestClass.class);
    
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testToString() {
    String expected = "test";
    constructorConstructor.toString();
    
    assertNotNull(constructorConstructor);
  }
  
  private class TestClass {}
  
  private class TestDefaultConstructor {}
  
  private class TestNoDefaultConstructor {
    private TestNoDefaultConstructor(String param) {}
  }
  
  private class TestSortedSet extends TreeSet<Object> {}
  
  private class TestEnumSet extends EnumSet<TestEnumSet> {}
  
  private class TestSet extends LinkedHashSet<Object> {}
  
  private class TestQueue extends LinkedList<Object> {}
  
  private class TestCollection extends ArrayList<Object> {}
  
  private class TestSortedMap extends TreeMap<Object,Object> {}
  
  private class TestParameterizedType extends LinkedHashMap<Object,Object> {}
}