package com.example.unsafeallocator;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class UnsafeAllocatorTest {
  @Test
  public void testNewInstance() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Object result = unsafeAllocator.newInstance(String.class);
    assertNotNull(result);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithInterface() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    unsafeAllocator.newInstance(Runnable.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithPrimitiveType() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(int.class);
    } catch (Exception e) {
      Assert.assertNotNull(e.getMessage());
    }
  }

  @Test(expected = NullPointerException.class)
  public void testNewInstanceWithNullClass() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    unsafeAllocator.newInstance(null);
  }

  @Test
  public void testCreate() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertNotNull(unsafeAllocator);
  }
}