package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Test;

public class TypeBindingsTest {
  
  @Test
  public void testEmptyBindings() {
    TypeBindings bindings = TypeBindings.emptyBindings();
    assertEquals(0, bindings.size());
    assertTrue(bindings.isEmpty());
  }
  
  @Test
  public void testCreateWithNoTypes() {
    TypeBindings bindings = TypeBindings.create(String.class, null);
    assertEquals(0, bindings.size());
    assertTrue(bindings.isEmpty());
  }
  
  @Test
  public void testCreateWithOneType() {
    JavaType stringType = new JavaType(String.class);
    TypeBindings bindings = TypeBindings.create(String.class, stringType);
    assertEquals(1, bindings.size());
    assertEquals(stringType, bindings.getBoundType(0));
  }
  
  @Test
  public void testCreateWithTwoTypes() {
    JavaType stringType = new JavaType(String.class);
    JavaType intType = new JavaType(Integer.class);
    TypeBindings bindings = TypeBindings.create(String.class, stringType, intType);
    assertEquals(2, bindings.size());
    assertEquals(stringType, bindings.getBoundType(0));
    assertEquals(intType, bindings.getBoundType(1));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateMismatchingNamesAndTypes() {
    JavaType stringType = new JavaType(String.class);
    TypeBindings.create(String.class, stringType, stringType);
  }
  
  @Test
  public void testCreateIfNeededWithNoTypeParameters() {
    JavaType stringType = new JavaType(String.class);
    TypeBindings bindings = TypeBindings.createIfNeeded(String.class, stringType);
    assertTrue(bindings.isEmpty());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateIfNeededWithInvalidTypeParameterCount() {
    JavaType stringType = new JavaType(String.class);
    JavaType intType = new JavaType(Integer.class);
    TypeBindings.createIfNeeded(String.class, stringType, intType);
  }
}