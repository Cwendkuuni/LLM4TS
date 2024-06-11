package com.google.gson.internal;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UnsafeAllocatorTest {

  private UnsafeAllocator allocator;

  @Test
  public void setup() {
    allocator = UnsafeAllocator.create();
  }

  @Test
  public void testNewInstance_Object() {
    Object instance = allocator.newInstance(Object.class);
    assertNotNull(instance);
  }

  @Test
  public void testNewInstance_String() {
    try {
      allocator.newInstance(String.class);
      assertFalse(true); // Should throw exception
    } catch (Exception e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_Interface() {
    try {
      allocator.newInstance(String[].class);
      assertFalse(true); // Should throw exception
    } catch (Exception e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_AbstractClass() {
    try {
      allocator.newInstance(AbstractClassTest.class);
      assertFalse(true); // Should throw exception
    } catch (Exception e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_Throwable() {
    try {
      allocator.newInstance(Throwable.class);
      assertFalse(true); // Should throw exception
    } catch (Exception e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_Exception() {
    try {
      allocator.newInstance(Exception.class);
      assertFalse(true); // Should throw exception
    } catch (Exception e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_Array() {
    try {
      allocator.newInstance(int[].class);
      assertFalse(true); // Should throw exception
    } catch (Exception e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_CustomClass() {
    try {
      Class<CustomClassTest> customClass = CustomClassTest.class;
      allocator.newInstance(customClass);
      assertFalse(true); // Should throw exception
    } catch (Exception e) {
      // Expected
    }
  }

  static class AbstractClassTest {
    abstract void test();
  }

  static class CustomClassTest {
    CustomClassTest() {
      throw new RuntimeException("Custom constructor");
    }
  }
}