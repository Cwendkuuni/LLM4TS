package com.google.gson.internal;
import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Class<?> testClass = TestClass.class;
    try {
      Object instance = unsafeAllocator.newInstance(testClass);
      assertNotNull(instance);
      assertTrue(instance instanceof TestClass);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceUnsupported() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Class<?> unsupportedClass = UnsupportedClass.class;
    try {
      unsafeAllocator.newInstance(unsupportedClass);
      fail("UnsupportedOperationException should have been thrown");
    } catch (UnsupportedOperationException e) {
      assertEquals("Cannot allocate " + unsupportedClass, e.getMessage());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
}

class TestClass {
}

interface UnsupportedClass {
}