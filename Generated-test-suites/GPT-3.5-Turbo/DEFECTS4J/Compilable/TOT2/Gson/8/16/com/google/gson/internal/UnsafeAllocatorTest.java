package com.google.gson.internal;
import static org.junit.Assert.*;

import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testCreateUnsafeAllocator() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }

  @Test
  public void testNewInstance() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);

    // Test typical use case
    Object obj = allocator.newInstance(Object.class);
    assertNotNull(obj);
    
    // Test edge case for non-instantiable class
    try {
      allocator.newInstance(AbstractClass.class);
      fail("Exception not thrown for abstract class");
    } catch (UnsupportedOperationException e) {
      // Expected
    }

    // Test error scenario
    try {
      allocator.newInstance(NonExistentClass.class);
      fail("Exception not thrown for non-existent class");
    } catch (Exception e) {
      // Expected
    }
  }

  // Helper classes for test cases
  private abstract class AbstractClass {}
  private class NonExistentClass {}

}