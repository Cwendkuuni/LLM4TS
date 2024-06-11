package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance_success() {
    try {
      UnsafeAllocator allocator = UnsafeAllocator.create();
      assertNotNull(allocator.newInstance(Object.class));
      assertNotNull(allocator.newInstance(String.class));
      assertNotNull(allocator.newInstance(Integer.class));
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_failure() {
    try {
      UnsafeAllocator allocator = UnsafeAllocator.create();
      allocator.newInstance(UnsafeAllocator.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      assertEquals("Cannot allocate class com.google.gson.internal.UnsafeAllocator", e.getMessage());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }
}