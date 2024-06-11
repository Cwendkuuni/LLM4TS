package com.google.gson.internal;
import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {
  @Test
  public void testNewInstance_unsafeClass() throws Exception {
    // Setup
    Class<T> c = T.class;
    UnsafeAllocator allocator = UnsafeAllocator.create();

    // Execute
    T newInstance = allocator.newInstance(c);

    // Verify
    assertNotNull(newInstance);
    assertEquals(c, newInstance.getClass());
  }

  @Test
  public void testNewInstance_ObjectStreamClass() throws Exception {
    // Setup
    Class<T> c = T.class;
    UnsafeAllocator allocator = UnsafeAllocator.create();

    // Execute
    T newInstance = allocator.newInstance(c);

    // Verify
    assertNotNull(newInstance);
    assertEquals(c, newInstance.getClass());
  }

  @Test
  public void testNewInstance_ObjectInputStream() throws Exception {
    // Setup
    Class<T> c = T.class;
    UnsafeAllocator allocator = UnsafeAllocator.create();

    // Execute
    T newInstance = allocator.newInstance(c);

    // Verify
    assertNotNull(newInstance);
    assertEquals(c, newInstance.getClass());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_UnsupportedOperationException() throws Exception {
    // Setup
    Class<T> c = T.class;
    UnsafeAllocator allocator = UnsafeAllocator.create();

    // Execute
    allocator.newInstance(c);
  }

  // Helper class for testing
  private static class T {
    public T() {
      // constructor
    }
  }
}