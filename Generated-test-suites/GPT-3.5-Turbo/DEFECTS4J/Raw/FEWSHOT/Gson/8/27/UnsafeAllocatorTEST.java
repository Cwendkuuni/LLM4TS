
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
  public void testNewInstance() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      // Test for a class with public constructor
      TestClass1 obj1 = allocator.newInstance(TestClass1.class);
      assertNotNull(obj1);
      
      // Test for a class with private constructor
      TestClass2 obj2 = allocator.newInstance(TestClass2.class);
      assertNotNull(obj2);
      
      // Test for a class with protected constructor
      TestClass3 obj3 = allocator.newInstance(TestClass3.class);
      assertNotNull(obj3);
      
      // Test for a class with default constructor
      TestClass4 obj4 = allocator.newInstance(TestClass4.class);
      assertNotNull(obj4);
      
      // Test for an interface
      allocator.newInstance(TestInterface.class);
      fail("Expected UnsupportedOperationException");
    } catch (Exception e) {
      assertTrue(e instanceof UnsupportedOperationException);
    }
  }
  
  public static class TestClass1 {
    public TestClass1() {}
  }
  
  public static class TestClass2 {
    private TestClass2() {}
  }
  
  public static class TestClass3 {
    protected TestClass3() {}
  }
  
  public static class TestClass4 {
    TestClass4() {}
  }
  
  public interface TestInterface {
    
  }
}
