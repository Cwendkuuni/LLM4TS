package com.google.gson.internal;
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }

  @Test
  public void testNewInstance() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Object instance = allocator.newInstance(Object.class);
    assertNotNull(instance);
  }

  @Test
  public void testNewInstanceWithNullClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(null);
      fail("Should have thrown an exception");
    } catch (Exception e) {
      assertTrue(e instanceof NullPointerException);
    }
  }

  @Test
  public void testNewInstanceWithUnsupportedClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(UnsupportedClass.class);
      fail("Should have thrown an exception");
    } catch (Exception e) {
      assertTrue(e instanceof UnsupportedOperationException);
    }
  }

  @Test
  public void testNewInstanceWithAbstractClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(AbstractClass.class);
      fail("Should have thrown an exception");
    } catch (Exception e) {
      assertTrue(e instanceof UnsupportedOperationException);
    }
  }

  private static abstract class UnsupportedClass {
    // Empty class used for testing unsupported instances
  }

  private static abstract class AbstractClass {
    // Empty class used for testing abstract instances
  }
}