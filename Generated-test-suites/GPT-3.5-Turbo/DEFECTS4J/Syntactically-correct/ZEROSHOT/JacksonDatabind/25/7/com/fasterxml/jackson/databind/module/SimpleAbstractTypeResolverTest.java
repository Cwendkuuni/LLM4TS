package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {
  
  @Test
  public void testAddMapping() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    resolver.addMapping(Collection.class, LinkedList.class);
    resolver.addMapping(List.class, LinkedList.class);
    assertTrue(resolver._mappings.containsKey(new ClassKey(Collection.class)));
    assertTrue(resolver._mappings.containsKey(new ClassKey(List.class)));
    assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
    assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(List.class)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithSameClass() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    resolver.addMapping(Collection.class, Collection.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNonSubType() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    resolver.addMapping(List.class, ArrayList.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNonAbstractSuperType() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    resolver.addMapping(String.class, String.class);
  }

  @Test
  public void testFindTypeMapping() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    resolver.addMapping(Collection.class, LinkedList.class);
    resolver.addMapping(List.class, LinkedList.class);
    DeserializationConfig config = new DeserializationConfig();
    JavaType type = config.constructType(Collection.class);
    JavaType mappedType = resolver.findTypeMapping(config, type);
    assertNotNull(mappedType);
    assertEquals(LinkedList.class, mappedType.getRawClass());
  }

  @Test
  public void testResolveAbstractType() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    DeserializationConfig config = new DeserializationConfig();
    JavaType type = config.constructType(Collection.class);
    JavaType resolvedType = resolver.resolveAbstractType(config, type);
    assertNull(resolvedType);
  }
}