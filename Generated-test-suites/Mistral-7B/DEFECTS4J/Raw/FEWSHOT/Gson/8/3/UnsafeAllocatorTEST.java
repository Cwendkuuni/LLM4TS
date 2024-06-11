package com.google.gson.internal;

import static org.junit.Assert.assertinstanceof;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstanceForObject() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Object obj = allocator.newInstance(Object.class);
    assertNotNull(obj);
    assertinstanceof(Object.class, obj);
  }

  @Test
  public void testNewInstanceForString() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    String str = (String) allocator.newInstance(String.class);
    assertNotNull(str);
    assertinstanceof(String.class, str);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceForInterface() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    allocator.newInstance(Object.class.getInterfaces()[0]);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceForAbstractClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    allocator.newInstance(AbstractClass.class);
  }

  private static abstract class AbstractClass {
  }
}