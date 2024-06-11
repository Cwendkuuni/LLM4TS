package com.google.gson.internal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.internal.UnsafeAllocator;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private static final UnsafeAllocator UNSAFE_ALLOCATOR = UnsafeAllocator.create();

  @Test
  public void testNewInstance() throws Exception {
    /* Add your test code here */
    Object instance = UNSAFE_ALLOCATOR.newInstance(String.class);
    // You can test the instance here, for example, by checking if it has the expected methods or properties
  }
}