package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {
  
  private SimpleAbstractTypeResolver resolver;
  
  @Before
  public void setUp() {
    resolver = new SimpleAbstractTypeResolver();
  }
  
  @Test
  public void testAddMapping() {
    Class<Collection> superType = Collection.class;
    Class<LinkedList> subType = LinkedList.class;
    
    SimpleAbstractTypeResolver result = resolver.addMapping(superType, subType);
    
    assertNotNull(result);
    assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAddMapping_sameClass() {
    Class<Collection> superClass = Collection.class;
    Class<Collection> subClass = Collection.class;
    
    resolver.addMapping(superClass, subClass);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAddMapping_nonSubType() {
    Class<List> superClass = List.class;
    Class<LinkedList> subClass = LinkedList.class;
    
    resolver.addMapping(superClass, subClass);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAddMapping_nonAbstract() {
    Class<List> superClass = List.class;
    Class<LinkedList> subClass = LinkedList.class;
    
    resolver.addMapping(superClass, subClass);
  }
  
  @Test
  public void testFindTypeMapping() {
    DeserializationConfig config = null;
    JavaType type = new JavaType();
    Class<?> src = type.getRawClass();
    Class<?> dst = resolver._mappings.get(new ClassKey(src));
    
    JavaType result = resolver.findTypeMapping(config, type);
    
    assertNull(result);
  }
  
  @Test
  public void testResolveAbstractType() {
    DeserializationConfig config = null;
    JavaType type = new JavaType();
    
    JavaType result = resolver.resolveAbstractType(config, type);
    
    assertNull(result);
  }
}