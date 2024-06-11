package com.google.gson.internal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.internal.UnsafeAllocator;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {
  UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_ValidClass() throws Exception {
    Object obj = allocator.newInstance(String.class);
    // Add your assertions here
  }
}