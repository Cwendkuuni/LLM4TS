package com.google.gson.internal;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstanceForObject() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNull(allocator.newInstance(Object.class));
  }

  @Test
  public void testNewInstanceForInterface() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(java.io.Serializable.class));
  }

  @Test
  public void testNewInstanceForAbstractClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(java.lang.AbstractString.class));
  }

  @Test
  public void testNewInstanceForClassWithPrivateConstructor() {
    try {
      // Create a simple class with a private constructor
      class TestPrivateConstructor {
        private TestPrivateConstructor() {}
      }

      UnsafeAllocator allocator = UnsafeAllocator.create();
      assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(TestPrivateConstructor.class));
    } catch (Exception e) {
      // Ignore the exception, the test should fail due to UnsupportedOperationException
    }
  }

  @Test
  public void testNewInstanceForClassWithPublicConstructor() {
    try {
      // Create a simple class with a public constructor
      class TestPublicConstructor {
        public TestPublicConstructor() {}
      }

      UnsafeAllocator allocator = UnsafeAllocator.create();
      TestPublicConstructor instance = allocator.newInstance(TestPublicConstructor.class);
      assertNull(instance); // Unsafe allocator doesn't invoke constructors
    } catch (Exception e) {
      // Ignore the exception, the test should not throw any exception
    }
  }
}