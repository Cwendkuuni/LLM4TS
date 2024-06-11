package com.google.gson.internal;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }

  @Test
  public void testCreateWithInvalidClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(InvalidClass.class);
      fail("Expected exception to be thrown");
    } catch (Exception e) {
      // Expected exception to be thrown
      assertTrue(e instanceof UnsupportedOperationException);
    }
  }

  @Test
  public void testCreateWithClassWithConstructor() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(ClassWithConstructor.class);
    } catch (Exception e) {
      fail("Expected no exception, but got " + e.getClass().getSimpleName());
    }
  }

  @Test
  public void testCreateWithClassWithInterface() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(ClassWithInterface.class);
      fail("Expected exception to be thrown");
    } catch (Exception e) {
      // Expected exception to be thrown
      assertTrue(e instanceof UnsupportedOperationException);
    }
  }

  @Test
  public void testCreateWithClassWithAbstractModifier() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(ClassWithAbstractModifier.class);
      fail("Expected exception to be thrown");
    } catch (Exception e) {
      // Expected exception to be thrown
      assertTrue(e instanceof UnsupportedOperationException);
    }
  }

  private static class InvalidClass {
    // Invalid class without default constructor
    public InvalidClass(int param) {
    }
  }

  private static class ClassWithConstructor {
    // Class with constructor
    public ClassWithConstructor() {
    }
  }

  private interface ClassWithInterface {
    // Interface
  }

  private abstract class ClassWithAbstractModifier {
    // Class with abstract modifier
  }

}