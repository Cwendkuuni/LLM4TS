package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class UnsafeAllocatorTest {
  private static UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstanceObject() {
    Object obj = allocator.newInstance(Object.class);
    assertInstanceOf(Object.class, obj);
  }

  @Test
  public void testNewInstanceString() {
    String str = allocator.newInstance(String.class);
    assertInstanceOf(String.class, str);
  }

  @Test
  public void testNewInstanceInterface() {
    assertThrows(UnsupportedOperationException.class, () -> {
      allocator.newInstance(Object.class.getInterfaces()[0]);
    });
  }

  @Test
  public void testNewInstanceAbstractClass() {
    assertThrows(UnsupportedOperationException.class, () -> {
      allocator.newInstance(AbstractClass.class);
    });
  }

  @Test
  public void testNewInstancePrivateClass() {
    assertThrows(IllegalAccessError.class, () -> {
      Class<PrivateClass> privateClass = PrivateClass.class;
      allocator.newInstance(privateClass);
    });
  }

  @Test
  public void testNewInstanceNull() {
    assertThrows(NullPointerException.class, () -> {
      allocator.newInstance(null);
    });
  }

  private static abstract class AbstractClass {
  }

  private static class PrivateClass {
  }
}