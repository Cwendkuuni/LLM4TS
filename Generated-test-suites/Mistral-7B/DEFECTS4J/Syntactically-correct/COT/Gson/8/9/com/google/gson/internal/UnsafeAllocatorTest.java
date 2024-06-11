package com.google.gson.internal;

import org.junit.Test;
import org.junit.experimental.annotations.EdgeCase;
import org.junit.experimental.annotations.ExpectedException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

public class UnsafeAllocatorTest {

  private static UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public <T> void testNewInstance_NormalCase() throws Exception {
    Class<T> type = String.class;
    T instance = unsafeAllocator.newInstance(type);
    assertInstanceOf(String.class, instance);
  }

  @Test
  public void testNewInstance_NullClass() {
    assertNull(unsafeAllocator.newInstance(null));
  }

  @Test
  @EdgeCase
  public void testNewInstance_AbstractClass() {
    Class<AbstractClass> abstractClass = AbstractClass.class;
    ExpectedException exception = ExpectedException.none();
    exception.expect(UnsupportedOperationException.class);
    exception.expectMessage("Cannot allocate " + abstractClass);
    unsafeAllocator.newInstance(abstractClass);
  }

  @Test
  @EdgeCase
  public void testNewInstance_Interface() {
    Class<MyInterface> interfaceType = MyInterface.class;
    ExpectedException exception = ExpectedException.none();
    exception.expect(UnsupportedOperationException.class);
    exception.expectMessage("Cannot allocate " + interfaceType);
    unsafeAllocator.newInstance(interfaceType);
  }

  @Test
  @EdgeCase
  public void testNewInstance_FailedToAllocate() {
    Class<FailedToAllocate> failedToAllocate = FailedToAllocate.class;
    ExpectedException exception = ExpectedException.none();
    exception.expect(UnsupportedOperationException.class);
    exception.expectMessage("Cannot allocate " + failedToAllocate);
    unsafeAllocator.newInstance(failedToAllocate);
  }

  @Test
  @ExpectedException(UnsupportedOperationException.class)
  public void testNewInstance_ThrowsException() throws Exception {
    Class<ThrowsException> throwsException = ThrowsException.class;
    unsafeAllocator.newInstance(throwsException);
  }

  @Test
  public void testNewInstance_ReflectionError() {
    Class<ReflectionError> reflectionError = ReflectionError.class;
    ExpectedException exception = ExpectedException.none();
    exception.expect(ReflectionError.class);
    exception.expectMessage("reflect: ReflectionError");
    unsafeAllocator.newInstance(reflectionError);
  }

  @Test
  public void testNewInstance_InvocationTargetException() {
    Class<InvocationTargetException> invocationTargetException = InvocationTargetException.class;
    ExpectedException exception = ExpectedException.none();
    exception.expect(InvocationTargetException.class);
    exception.expectCause(new InvocationTargetException(new RuntimeException()));
    unsafeAllocator.newInstance(InvocationTargetException.class);
  }

  @Test
  public void testNewInstance_UndeclaredThrowableException() {
    Class<UndeclaredThrowableException> undeclaredThrowableException = UndeclaredThrowableException.class;
    ExpectedException exception = ExpectedException.none();
    exception.expect(UndeclaredThrowableException.class);
    exception.expectCause(new RuntimeException());
    unsafeAllocator.newInstance(UndeclaredThrowableException.class);
  }

  private static class AbstractClass {
  }

  private static class MyInterface {
  }

  private static class FailedToAllocate {
  }

  private static class ThrowsException {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
      throw new RuntimeException();
    }
  }

  private static class ReflectionError extends Exception {
    public ReflectionError(String message) {
      super(message);
    }

    public ReflectionError(String message, Throwable cause) {
      super(message, cause);
    }
  }

  private static class InvocationTargetException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvocationTargetException(Throwable cause) {
      super("reflect: InvocationTargetException", cause);
    }
  }

  private static class UndeclaredThrowableException extends LinkageError {
    private static final long serialVersionUID = 1L;

    public UndeclaredThrowableException(Throwable cause) {
      super("reflect: UndeclaredThrowableException", cause);
    }
  }
}