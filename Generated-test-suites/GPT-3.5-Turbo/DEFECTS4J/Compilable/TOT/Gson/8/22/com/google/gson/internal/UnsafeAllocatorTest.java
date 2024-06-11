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
  public void testNewInstance_JVM() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();

    // Typical use case
    MyClass myClass = allocator.newInstance(MyClass.class);
    assertNotNull(myClass);

    // Edge case: Class with default constructor
    MyClassWithDefaultConstructor myClassDefault = 
        allocator.newInstance(MyClassWithDefaultConstructor.class);
    assertNotNull(myClassDefault);

    // Error scenario: Class with private constructor
    try {
      MyClassWithPrivateConstructor myClassPrivate = 
          allocator.newInstance(MyClassWithPrivateConstructor.class);
      fail("Expected exception not thrown");
    } catch (UnsupportedOperationException e) {
      // Expected exception
      assertEquals("Cannot allocate " + MyClassWithPrivateConstructor.class, e.getMessage());
    }
  }

  @Test
  public void testNewInstance_DalvikvmPostGingerbread() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();

    // Typical use case
    MyClass myClass = allocator.newInstance(MyClass.class);
    assertNotNull(myClass);

    // Edge case: Class with default constructor
    MyClassWithDefaultConstructor myClassDefault = 
        allocator.newInstance(MyClassWithDefaultConstructor.class);
    assertNotNull(myClassDefault);

    // Error scenario: Class with private constructor
    try {
      MyClassWithPrivateConstructor myClassPrivate = 
          allocator.newInstance(MyClassWithPrivateConstructor.class);
      fail("Expected exception not thrown");
    } catch (UnsupportedOperationException e) {
      // Expected exception
      assertEquals("Cannot allocate " + MyClassWithPrivateConstructor.class, e.getMessage());
    }
  }

  @Test
  public void testNewInstance_DalvikvmPreGingerbread() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();

    // Typical use case
    MyClass myClass = allocator.newInstance(MyClass.class);
    assertNotNull(myClass);

    // Edge case: Class with default constructor
    MyClassWithDefaultConstructor myClassDefault = 
        allocator.newInstance(MyClassWithDefaultConstructor.class);
    assertNotNull(myClassDefault);

    // Error scenario: Class with private constructor
    try {
      MyClassWithPrivateConstructor myClassPrivate = 
          allocator.newInstance(MyClassWithPrivateConstructor.class);
      fail("Expected exception not thrown");
    } catch (UnsupportedOperationException e) {
      // Expected exception
      assertEquals("Cannot allocate " + MyClassWithPrivateConstructor.class, e.getMessage());
    }
  }

  @Test
  public void testNewInstance_GiveUp() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();

    // Typical use case
    MyClass myClass = allocator.newInstance(MyClass.class);
    assertNotNull(myClass);

    // Edge case: Class with default constructor
    MyClassWithDefaultConstructor myClassDefault = 
        allocator.newInstance(MyClassWithDefaultConstructor.class);
    assertNotNull(myClassDefault);

    // Error scenario: Class with private constructor
    try {
      MyClassWithPrivateConstructor myClassPrivate = 
          allocator.newInstance(MyClassWithPrivateConstructor.class);
      fail("Expected exception not thrown");
    } catch (UnsupportedOperationException e) {
      // Expected exception
      assertEquals("Cannot allocate " + MyClassWithPrivateConstructor.class, e.getMessage());
    }
  }

  /**
   * Inner classes for testing
   */

  // Test class with a public constructor
  public static class MyClass {
    public MyClass() {}
  }

  // Test class with a default (public) constructor
  public static class MyClassWithDefaultConstructor {
    public MyClassWithDefaultConstructor() {}
  }

  // Test class with a private constructor
  public static class MyClassWithPrivateConstructor {
    private MyClassWithPrivateConstructor() {}
  }

}