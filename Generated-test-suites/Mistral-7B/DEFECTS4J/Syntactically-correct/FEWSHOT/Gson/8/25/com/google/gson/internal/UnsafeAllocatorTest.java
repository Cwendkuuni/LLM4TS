package com.google.gson.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testCanAllocateInterface() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertFalse(allocator.canAllocate(Object.class));
  }

  @Test
  public void testCanAllocateAbstractClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertFalse(allocator.canAllocate(AbstractClass.class));
  }

  @Test
  public void testCanAllocateFinalClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertTrue(allocator.canAllocate(FinalClass.class));
  }

  @Test
  public void testCanAllocateNormalClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertTrue(allocator.canAllocate(NormalClass.class));
  }

  @Test
  public void testAllocateInstanceThrowsException() {
    UnsafeAllocator allocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) {
        throw new UnsupportedOperationException("Cannot allocate " + c);
      }
    };

    assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(String.class));
  }

  @Test
  public void testAllocateInstanceWithNullClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertThrows(NullPointerException.class, () -> allocator.newInstance(null));
  }

  private static class AbstractClass {
    // abstract method
  }

  private static final class FinalClass {
  }

  private static class NormalClass {
  }
}

class UnsafeAllocatorTest_TestHelper {
  private static UnsafeAllocator allocator;

  static UnsafeAllocator getAllocator() {
    if (allocator == null) {
      allocator = UnsafeAllocator.create();
    }
    return allocator;
  }
}