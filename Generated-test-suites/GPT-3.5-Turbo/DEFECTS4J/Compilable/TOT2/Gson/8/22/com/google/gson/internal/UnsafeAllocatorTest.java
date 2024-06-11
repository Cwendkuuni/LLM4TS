package com.google.gson.internal;
import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {
  
  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }
  
  @Test
  public void testNewInstance() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    
    // Typical use case
    String str = allocator.newInstance(String.class);
    assertNotNull(str);
    
    // Edge case: testing allocation of an array
    int[] arr = allocator.newInstance(int[].class);
    assertNotNull(arr);
    assertEquals(0, arr.length);
    
    // Error scenario: testing allocation of an interface (should throw UnsupportedOperationException)
    try {
      allocator.newInstance(Runnable.class);
      fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }
}