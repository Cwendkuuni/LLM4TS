package com.google.gson.internal;

import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_ValidClass() throws Exception {
    Object obj = unsafeAllocator.newInstance(Object.class);
    assertNotNull(obj);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_InterfaceClass() throws Exception {
    unsafeAllocator.newInstance(String.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() throws Exception {
    unsafeAllocator.newInstance(AbstractClass.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_ExceptionClass() throws Exception {
    unsafeAllocator.newInstance(Exception.class);
  }

  @Test
  public void testNewInstance_NullClass() throws Exception {
    try {
      unsafeAllocator.newInstance(null);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test(expected = Exception.class)
  public void testNewInstance_FailedToAllocate() throws Exception {
    try {
      unsafeAllocator.newInstance(FailedToAllocateClass.class);
    } catch (UnsupportedOperationException e) {
      throw new AssertionError("Unexpected UnsupportedOperationException", e);
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      if (cause instanceof InstantiationException) {
        // Expected
      } else {
        throw new AssertionError("Unexpected exception", cause);
      }
    } catch (UndeclaredThrowableException e) {
      Throwable cause = e.getThrowable();
      if (cause instanceof InstantiationException) {
        // Expected
      } else {
        throw new AssertionError("Unexpected exception", cause);
      }
    }
  }

  @Test(expected = NoClassDefFoundError.class)
  public void testNewInstance_MissingClass() throws Exception {
    unsafeAllocator.newInstance(MissingClass.class);
  }

  static class FailedToAllocateClass {
    private static final long serialVersionUID = 1L;
  }

  static class AbstractClass implements Runnable {
    @Override
    public void run() {
      // empty
    }
  }

  static class MissingClass {
    // no definition provided
  }
}