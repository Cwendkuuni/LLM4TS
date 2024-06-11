package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;

public class UnsafeAllocatorTest {
  
  @Test
  public void testNewInstance() {
    try {
      UnsafeAllocator allocator = UnsafeAllocator.create();
      assertNotNull(allocator.newInstance(TestClass.class));
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithUnsupportedClass() {
    try {
      UnsafeAllocator allocator = UnsafeAllocator.create();
      allocator.newInstance(UnsupportedClass.class);
    } catch (UnsupportedOperationException e) {
      throw e;
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  private static class TestClass {}
  
  private interface UnsupportedClass {}
}