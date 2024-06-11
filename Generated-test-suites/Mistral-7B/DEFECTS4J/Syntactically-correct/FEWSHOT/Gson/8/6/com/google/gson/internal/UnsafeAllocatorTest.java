package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class UnsafeAllocatorTest {

  private static final UnsafeAllocator UNSAFE_ALLOCATOR = UnsafeAllocator.create();

  @Test
  public void testNewInstance_Object() {
    assertInstanceOf(Object.class, UNSAFE_ALLOCATOR.newInstance(Object.class));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_Interface() {
    UNSAFE_ALLOCATOR.newInstance(String.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() {
    UNSAFE_ALLOCATOR.newInstance(AbstractClass.class);
  }

  @Test
  public void testNewInstance_CustomClass1() {
    class CustomClass1 {
      // empty class
    }

    CustomClass1 instance = UNSAFE_ALLOCATOR.newInstance(CustomClass1.class);
    assertInstanceOf(CustomClass1.class, instance);
  }

  @Test
  public void testNewInstance_CustomClass2() {
    class CustomClass2 {
      CustomClass2() {
        // constructor
      }
    }

    assertThrows(UnsupportedOperationException.class, () -> UNSAFE_ALLOCATOR.newInstance(CustomClass2.class));
  }

  @Test
  public void testNewInstance_CustomClass3() {
    class CustomClass3 {
      CustomClass3(int i) {
        // constructor
      }
    }

    assertThrows(UnsupportedOperationException.class, () -> UNSAFE_ALLOCATOR.newInstance(CustomClass3.class));
  }

  @Test
  public void testNewInstance_CustomClass4() {
    class CustomClass4 {
      private CustomClass4() {
        // private constructor
      }

      public static CustomClass4 getInstance() {
        return new CustomClass4();
      }
    }

    assertInstanceOf(CustomClass4.class, UNSAFE_ALLOCATOR.newInstance(CustomClass4.class));
  }

  private static class AbstractClass {
    // abstract class
  }

  private static class CustomClass1 {
    // empty class
  }

  private static class CustomClass2 {
    // class with a constructor
  }

  private static class CustomClass3 {
    // class with a constructor with parameters
    CustomClass3(int i) {
    }
  }

  private static class CustomClass4 {
    private CustomClass4() {
    }

    public static CustomClass4 getInstance() {
      return new CustomClass4();
    }
  }
}