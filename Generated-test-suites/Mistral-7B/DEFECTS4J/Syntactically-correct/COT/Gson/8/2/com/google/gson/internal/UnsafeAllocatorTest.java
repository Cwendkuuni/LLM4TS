package com.google.gson.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnsafeAllocatorTest {
  private UnsafeAllocator unsafeAllocator;

  @Before
  public void setUp() {
    unsafeAllocator = UnsafeAllocator.create();
  }

  @Test
  public void testNewInstance_NullClass() {
    try {
      unsafeAllocator.newInstance(null);
      Assert.fail("Expected IllegalArgumentException but was null");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testNewInstance_ValidClass() {
    String result = unsafeAllocator.newInstance(String.class).toString();
    Assert.assertNotNull(result);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_InterfaceClass() {
    unsafeAllocator.newInstance(Object.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() {
    unsafeAllocator.newInstance(AbstractClass.class);
  }

  @Test
  public void testNewInstance_ThrowingConstructor() {
    try {
      Class<ThrowingClass> throwingClass = ThrowingClass.class;
      ThrowingClass instance = (ThrowingClass) unsafeAllocator.newInstance(throwingClass);
      Assert.fail("Expected IllegalStateException but was " + instance.getClass().getName());
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test(expected = InstantiationException.class)
  public void testNewInstance_ExceptionInConstructor() {
    try {
      Class<ExceptionInConstructorClass> exceptionInConstructorClass =
          ExceptionInConstructorClass.class;
      ExceptionInConstructorClass instance =
          (ExceptionInConstructorClass) unsafeAllocator.newInstance(exceptionInConstructorClass);
      Assert.fail("Expected InstantiationException but was " + instance.getClass().getName());
    } catch (InstantiationException e) {
      // expected
    }
  }

  @Test(expected = InvocationTargetException.class)
  public void testNewInstance_RuntimeExceptionInConstructor() {
    try {
      Class<RuntimeExceptionInConstructorClass> runtimeExceptionInConstructorClass =
          RuntimeExceptionInConstructorClass.class;
      RuntimeExceptionInConstructorClass instance =
          (RuntimeExceptionInConstructorClass) unsafeAllocator.newInstance(runtimeExceptionInConstructorClass);
      Assert.fail("Expected InvocationTargetException but was " + instance.getClass().getName());
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      Assert.assertTrue(cause instanceof RuntimeException);
    }
  }

  @Test(expected = UndeclaredThrowableException.class)
  public void testNewInstance_ErrorInConstructor() {
    try {
      Class<ErrorInConstructorClass> errorInConstructorClass = ErrorInConstructorClass.class;
      ErrorInConstructorClass instance =
          (ErrorInConstructorClass) unsafeAllocator.newInstance(errorInConstructorClass);
      Assert.fail("Expected UndeclaredThrowableException but was " + instance.getClass().getName());
    } catch (UndeclaredThrowableException e) {
      Throwable cause = e.getCause();
      Assert.assertTrue(cause instanceof Error);
    }
  }

  private static class AbstractClass {}

  private static class ThrowingClass {
    private ThrowingClass() {
      throw new IllegalStateException("Cannot instantiate throwing class");
    }
  }

  private static class ExceptionInConstructorClass {
    public ExceptionInConstructorClass() throws InstantiationException {
      throw new InstantiationException("Cannot instantiate exception in constructor class");
    }
  }

  private static class RuntimeExceptionInConstructorClass {
    public RuntimeExceptionInConstructorClass() throws RuntimeException {
      throw new RuntimeException("Cannot instantiate runtime exception in constructor class");
    }
  }

  private static class ErrorInConstructorClass {
    public ErrorInConstructorClass() throws Error {
      throw new Error("Cannot instantiate error in constructor class");
    }
  }
}