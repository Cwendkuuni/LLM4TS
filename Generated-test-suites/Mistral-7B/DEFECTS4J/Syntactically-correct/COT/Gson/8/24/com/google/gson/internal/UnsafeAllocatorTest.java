package com.google.gson.internal.test;

import static org.junit.Assert.*;

import com.google.gson.internal.UnsafeAllocator;
import java.lang.reflect.InvocationTargetException;

public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @SuppressWarnings("unchecked")
  @Test
  public <T> void testNewInstance() throws Exception {
    Class<T> stringClass = String.class;
    T instance = unsafeAllocator.newInstance(stringClass);
    assertNotNull(instance);
    assertEquals("", (String) instance);
  }

  @SuppressWarnings("unchecked")
  @Test(expected = UnsupportedOperationException.class)
  public <T> void testNewInstanceForInterface() throws Exception {
    Class<T> stringBufferClass = StringBuffer.class;
    unsafeAllocator.newInstance(stringBufferClass);
  }

  @SuppressWarnings("unchecked")
  @Test(expected = UnsupportedOperationException.class)
  public <T> void testNewInstanceForAbstractClass() throws Exception {
    Class<T> abstractClass = AbstractClass.class;
    unsafeAllocator.newInstance(abstractClass);
  }

  @SuppressWarnings("unchecked")
  @Test
  public <T> void testNewInstanceForException() throws Exception {
    Class<Exception> exceptionClass = Exception.class;
    try {
      unsafeAllocator.newInstance(exceptionClass);
      fail("Expected IllegalAccessException or InstantiationException");
    } catch (InstantiationException | IllegalAccessException e) {
      // expected
    }
  }

  private static class AbstractClass {
    // empty abstract class
  }
}