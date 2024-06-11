package com.google.gson.internal;
import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }

  @Test
  public void testNewInstance() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
    String testString = allocator.newInstance(String.class);
    assertNotNull(testString);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUnsupportedOperation() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
    allocator.newInstance(InterfaceClass.class);
  }

  @Test(expected = Exception.class)
  public void testExceptionThrown() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
    allocator.newInstance(ExceptionClass.class);
  }

  private interface InterfaceClass {
    // Interface for testing unsupported operation
  }

  private abstract class AbstractClass {
    // Abstract class for testing unsupported operation
  }

  private class ExceptionClass {
    ExceptionClass() throws Exception {
      throw new Exception();
    }
  }
}