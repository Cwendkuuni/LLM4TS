package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class UnsafeAllocatorTest {
  @Test
  public void testNewInstance_ValidClasses() {
    UnsafeAllocator allocator = UnsafeAllocator.create();

    Object instance = null;
    try {
      instance = allocator.newInstance(String.class);
      assertInstanceOf(String.class, instance);

      instance = allocator.newInstance(Integer.TYPE);
      assertInstanceOf(Integer.class, instance);

      instance = allocator.newInstance(ArrayList.class);
      assertInstanceOf(ArrayList.class, instance);

      instance = allocator.newInstance(HashMap.class);
      assertInstanceOf(HashMap.class, instance);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testNewInstance_Interface() {
    UnsafeAllocator allocator = UnsafeAllocator.create();

    assertThrows(UnsupportedOperationException.class, () -> {
      allocator.newInstance(Runnable.class);
    });
  }

  @Test
  public void testNewInstance_Abstract() {
    UnsafeAllocator allocator = UnsafeAllocator.create();

    assertThrows(UnsupportedOperationException.class, () -> {
      allocator.newInstance(AbstractList.class);
    });
  }
}