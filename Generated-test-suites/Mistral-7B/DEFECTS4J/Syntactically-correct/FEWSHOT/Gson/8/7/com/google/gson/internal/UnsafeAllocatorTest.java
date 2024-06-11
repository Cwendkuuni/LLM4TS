package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstanceForObject() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertNotNull(unsafeAllocator);
    Object obj = unsafeAllocator.newInstance(Object.class);
    assertInstanceOf(Object.class, obj);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceForInterface() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertNotNull(unsafeAllocator);
    unsafeAllocator.newInstance(Object.class.getInterfaces()[0]);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceForAbstractClass() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertNotNull(unsafeAllocator);
    unsafeAllocator.newInstance(AbstractClass.class);
  }

  private static abstract class AbstractClass {
  }
}