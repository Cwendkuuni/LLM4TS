package com.google.gson.internal;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {
  private UnsafeAllocator unsafeAllocator;

  @Before
  public void setUp() {
    unsafeAllocator = UnsafeAllocator.create();
  }

  @Test
  public void testCreate() {
    assertNotNull(unsafeAllocator);
  }

  @Test
  public void testNewInstance_Success() {
    try {
      Object instance = unsafeAllocator.newInstance(String.class);
      assertNotNull(instance);
      assertTrue(instance instanceof String);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_UnsupportedClassException() {
    try {
      unsafeAllocator.newInstance(Object.class);
      fail("Expected UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      assertEquals("Cannot allocate class java.lang.Object", e.getMessage());
    } catch (Exception e) {
      fail("Unexpected Exception thrown: " + e.getMessage());
    }
  }

  // Test cases for JVM

  @Test
  public void testNewInstance_JVM_Success() {
    try {
      Object instance = unsafeAllocator.newInstance(ExampleClass.class);
      assertNotNull(instance);
      assertTrue(instance instanceof ExampleClass);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  // Test cases for dalvikvm post-gingerbread

  @Test
  public void testNewInstance_Dalvikvm_PostGingerbread_Success() {
    try {
      Object instance = unsafeAllocator.newInstance(DalvikvmPostGingerbreadClass.class);
      assertNotNull(instance);
      assertTrue(instance instanceof DalvikvmPostGingerbreadClass);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  // Test cases for dalvikvm pre-gingerbread

  @Test
  public void testNewInstance_Dalvikvm_PreGingerbread_Success() {
    try {
      Object instance = unsafeAllocator.newInstance(DalvikvmPreGingerbreadClass.class);
      assertNotNull(instance);
      assertTrue(instance instanceof DalvikvmPreGingerbreadClass);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_Dalvikvm_PreGingerbread_UnsupportedClassException() {
    try {
      unsafeAllocator.newInstance(DalvikvmPreGingerbreadUnsupportedClass.class);
      fail("Expected UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      assertEquals("Cannot allocate class com.example.DalvikvmPreGingerbreadUnsupportedClass", e.getMessage());
    } catch (Exception e) {
      fail("Unexpected Exception thrown: " + e.getMessage());
    }
  }

  // Helper classes for testing

  private static class ExampleClass { }

  private static class DalvikvmPostGingerbreadClass { }

  private static class DalvikvmPreGingerbreadClass { }

  private static class DalvikvmPreGingerbreadUnsupportedClass { }
}