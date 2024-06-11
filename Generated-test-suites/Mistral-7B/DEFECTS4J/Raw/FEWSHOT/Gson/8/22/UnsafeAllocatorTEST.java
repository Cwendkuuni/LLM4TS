package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstanceForObject() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Object obj = allocator.newInstance(Object.class);
    assertNotNull(obj);
  }

  @Test
  public void testNewInstanceForAbstractClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertThrows(UnsupportedOperationException.class, () -> {
      allocator.newInstance(AbstractClass.class);
    });
  }

  @Test
  public void testNewInstanceForInterface() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertThrows(UnsupportedOperationException.class, () -> {
      allocator.newInstance(Interface.class);
    });
  }

  @Test
  public void testNewInstanceForArray() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertThrows(UnsupportedOperationException.class, () -> {
      allocator.newInstance(int[].class);
    });
  }

  @Test
  public void testNewInstanceForCustomClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    CustomClass customClass = new CustomClass();
    try {
      CustomClass instance = (CustomClass) allocator.newInstance(CustomClass.class);
      assertEquals(customClass, instance);
    } catch (Exception e) {
      fail("Unexpected exception: " + e);
    }
  }

  static class CustomClass {
    // A simple custom class to be instantiated using UnsafeAllocator
  }

  interface Interface {}

  abstract static class AbstractClass {}
}