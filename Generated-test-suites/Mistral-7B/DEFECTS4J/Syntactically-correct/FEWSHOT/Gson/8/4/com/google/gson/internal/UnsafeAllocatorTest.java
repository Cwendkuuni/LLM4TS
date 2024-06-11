package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance() {
    UnsafeAllocator allocator = UnsafeAllocator.create();

    Object object = allocator.newInstance(Object.class);
    assertNotNull(object);
    assertInstanceOf(Object.class, object);

    String string = allocator.newInstance(String.class);
    assertNotNull(string);
    assertInstanceOf(String.class, string);

    Number number = allocator.newInstance(Number.class);
    assertNotNull(number);
    assertInstanceOf(Number.class, number);

    // Test for interface and abstract classes
    assertThrows(UnsupportedOperationException.class, () -> {
      allocator.newInstance(java.io.Serializable.class);
    });

    assertThrows(UnsupportedOperationException.class, () -> {
      allocator.newInstance(java.lang.AbstractClassType.class);
    });
  }
}