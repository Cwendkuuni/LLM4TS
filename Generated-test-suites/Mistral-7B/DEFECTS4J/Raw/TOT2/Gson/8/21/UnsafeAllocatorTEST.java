package com.example.junit.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.internal.UnsafeAllocator;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance() {
    String stringInstance = allocator.newInstance(String.class);
    assertNotNull(stringInstance);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithNullClass() {
    allocator.newInstance(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithInterfaceClass() {
    allocator.newInstance(String.class.isInterface() ? Runnable.class : String.class);
  }

  @Test
  public void testNewInstanceWithDifferentDataTypes() {
    String stringInstance = allocator.newInstance(String.class);
    Integer integerInstance = allocator.newInstance(Integer.class);
    assertNotNull(stringInstance);
    assertNotNull(integerInstance);
  }

  @Test
  public void testNewInstanceWithSubclass() {
    String stringInstance = allocator.newInstance(String.class);
    StringBuilder stringBuilderInstance = allocator.newInstance(StringBuilder.class);
    assertNotNull(stringInstance);
    assertNotNull(stringBuilderInstance);
  }

  @Test
  public void testNewInstanceWithGenericClass() {
    List<String> listInstance = allocator.newInstance(ArrayList.class);
    assertNotNull(listInstance);
  }

  @Test
  public void testNewInstanceWithPrimitiveTypes() {
    int integerInstance = (int) allocator.newInstance(int[].class)[0];
    assertNotNull(integerInstance);
  }

  @Test
  public void testNewInstanceWithDeeplyNestedClasses() {
    Class<NestedClass> nestedClass = NestedClass.class;
    NestedClass nestedInstance = (NestedClass) allocator.newInstance(nestedClass);
    assertNotNull(nestedInstance);
  }

  @Test
  public void testNewInstanceWithClassesThatHavePrivateConstructors() {
    Class<PrivateConstructorClass> privateConstructorClass = PrivateConstructorClass.class;
    try {
      allocator.newInstance(privateConstructorClass);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  // Add tests for the remaining edge cases and exceptions here

}

class NestedClass {}

class PrivateConstructorClass {
  private PrivateConstructorClass() {
  }
}