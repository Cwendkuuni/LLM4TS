package com.google.gson.internal;
import static org.junit.Assert.*;
import org.junit.Test;

import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }

  @Test
  public void testNewInstance() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    
    // Test with a class that can be instantiated
    String s = allocator.newInstance(String.class);
    assertNotNull(s);
    
    // Test with a class that cannot be instantiated
    try {
      allocator.newInstance(UnsupportedClass.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected exception
    }
  }
  
  // Helper class for testing unsupported instantiation
  private abstract class UnsupportedClass {
    
  }
}