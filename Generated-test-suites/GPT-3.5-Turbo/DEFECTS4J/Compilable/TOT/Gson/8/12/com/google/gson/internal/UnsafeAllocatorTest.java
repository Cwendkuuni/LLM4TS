package com.google.gson.internal;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {
  
  @Test
  public void testNewInstance_JvmClass() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    MyClass instance = allocator.newInstance(MyClass.class);
    assertNotNull(instance);
    assertTrue(instance instanceof MyClass);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_DoesNotSupportInterface() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    InterfaceClass instance = allocator.newInstance(InterfaceClass.class);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_DoesNotSupportAbstractClass() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    AbstractClass instance = allocator.newInstance(AbstractClass.class);
  }
  
  @Test
  public void testNewInstance_DalvikPostGingerbread() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    MyClass instance = allocator.newInstance(MyClass.class);
    assertNotNull(instance);
    assertTrue(instance instanceof MyClass);
  }
  
  @Test
  public void testNewInstance_DalvikPreGingerbread() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    MyClass instance = allocator.newInstance(MyClass.class);
    assertNotNull(instance);
    assertTrue(instance instanceof MyClass);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_GiveUp() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    UnsupportedClass instance = allocator.newInstance(UnsupportedClass.class);
  }
  
  private static class MyClass {
    // implementation of MyClass
  }
  
  private static interface InterfaceClass {
    // interface definition
  }
  
  private static abstract class AbstractClass {
    // abstract class definition
  }
  
  private static class UnsupportedClass {
    // implementation of an unsupported class
  }
  
}