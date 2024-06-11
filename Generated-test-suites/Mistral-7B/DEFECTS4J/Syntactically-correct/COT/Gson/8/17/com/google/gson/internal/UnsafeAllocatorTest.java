package com.google.gson.internal;

import org.junit.Test;
import org.junit.experimental.annotations.EdgeCases;
import org.junit.experimental.annotations.ExpectedException;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@EdgeCases
public class UnsafeAllocatorTest {

  private static UnsafeAllocator unsafeAllocator;

  @Test
  public void testNewInstance_ValidClass() throws Exception {
    unsafeAllocator = UnsafeAllocator.create();
    Object obj = unsafeAllocator.newInstance(String.class);
    assertNotNull(obj);
  }

  @Test
  public void testNewInstance_NullClass() {
    unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_ThrowableThrown() throws Exception {
    unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(Throwable.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  @ExpectedException(UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() throws Exception {
    unsafeAllocator = UnsafeAllocator.create();
    unsafeAllocator.newInstance(AbstractClass.class);
  }

  @Test
  @ExpectedException(UnsupportedOperationException.class)
  public void testNewInstance_Interface() throws Exception {
    unsafeAllocator = UnsafeAllocator.create();
    unsafeAllocator.newInstance(Interface.class);
  }

  @Test
  public void testNewInstance_ClassWithPrivateConstructor() throws Exception {
    class PrivateConstructorClass {
      private PrivateConstructorClass() {}
    }
    unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(PrivateConstructorClass.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_ClassWithProtectedConstructor() throws Exception {
    class ProtectedConstructorClass {
      ProtectedConstructorClass() {}
      protected ProtectedConstructorClass(int i) {}
    }
    unsafeAllocator = UnsafeAllocator.create();
    Object obj = unsafeAllocator.newInstance(ProtectedConstructorClass.class);
    assertNotNull(obj);
  }

  @Test
  public void testNewInstance_ClassWithDefaultConstructor() throws Exception {
    unsafeAllocator = UnsafeAllocator.create();
    Object obj = unsafeAllocator.newInstance(DefaultConstructorClass.class);
    assertNotNull(obj);
  }

  @Test
  public void testNewInstance_ClassWithParameterizedConstructor() throws Exception {
    class ParameterizedConstructorClass<T> {
      ParameterizedConstructorClass(T t) {}
    }
    unsafeAllocator = UnsafeAllocator.create();
    Object obj = unsafeAllocator.newInstance(ParameterizedConstructorClass.class);
    fail("Expected UnsupportedOperationException");
  }

  @Test
  public void testNewInstance_ClassWithPrivateParameterizedConstructor() throws Exception {
    class PrivateParameterizedConstructorClass<T> {
      private PrivateParameterizedConstructorClass(T t) {}
    }
    unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(PrivateParameterizedConstructorClass.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_ClassWithConstructorThatThrowsException() throws Exception {
    class ConstructorThrowingExceptionClass {
      ConstructorThrowingExceptionClass() throws Exception {
        throw new Exception("Constructor threw an exception");
      }
    }
    unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(ConstructorThrowingExceptionClass.class);
      fail("Expected InvocationTargetException");
    } catch (InvocationTargetException e) {
      // Expected
      assertNotNull(e.getCause());
    }
  }

  static class AbstractClass {}

  static interface Interface {}

  static class DefaultConstructorClass {}

  static class PrivateConstructorClass {
    private PrivateConstructorClass() {}
  }

  static class ProtectedConstructorClass {
    protected ProtectedConstructorClass() {}
  }

  static class ParameterizedConstructorClass<T> {
    ParameterizedConstructorClass(T t) {}
  }

  static class PrivateParameterizedConstructorClass<T> {
    private PrivateParameterizedConstructorClass(T t) {}
  }

  static class ConstructorThrowingExceptionClass {
    ConstructorThrowingExceptionClass() throws Exception {
      throw new Exception("Constructor threw an exception");
    }
  }
}