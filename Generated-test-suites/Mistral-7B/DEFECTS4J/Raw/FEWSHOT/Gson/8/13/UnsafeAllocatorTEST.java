package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class UnsafeAllocatorTest {
  @Test
  public void testCanAllocateObject() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Object obj = unsafeAllocator.newInstance(Object.class);
    assertInstanceOf(Object.class, obj);
  }

  @Test
  public void testCannotAllocateInterface() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertThrows(UnsupportedOperationException.class, () -> {
      unsafeAllocator.newInstance(Object.class.getInterfaces()[0]);
    });
  }

  @Test
  public void testCannotAllocateAbstractClass() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertThrows(UnsupportedOperationException.class, () -> {
      unsafeAllocator.newInstance(AbstractClass.class);
    });
  }

  static abstract class AbstractClass {}

  static interface Interface {}
}