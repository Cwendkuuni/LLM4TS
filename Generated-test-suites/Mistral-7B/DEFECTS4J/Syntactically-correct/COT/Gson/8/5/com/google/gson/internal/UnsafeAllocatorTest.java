package com.google.gson.internal.test;

import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstanceNullClass() {
    try {
      unsafeAllocator.newInstance(null);
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstanceEmptyClass() {
    try {
      unsafeAllocator.newInstance(Object.class);
      Assert.fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstanceAbstractClass() {
    try {
      unsafeAllocator.newInstance(AbstractClass.class);
      Assert.fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstanceInterfaceClass() {
    try {
      unsafeAllocator.newInstance(InterfaceClass.class);
      Assert.fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstanceSuccess() throws Exception {
    final Class<ConcreteClass> concreteClass = ConcreteClass.class;
    final ConcreteClass instance = unsafeAllocator.newInstance(concreteClass);
    Assert.assertNotNull(instance);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewInstanceFailedToAccessUnsafe() throws Exception {
    final UnsafeAllocator unsafeAllocator = new UnsafeAllocator() {
      @Override
      @SuppressWarnings("unchecked")
      public <T> T newInstance(Class<T> c) throws Exception {
        throw new IllegalArgumentException("Failed to access unsafe");
      }
    };
    unsafeAllocator.newInstance(Object.class);
  }

  @Test(expected = InvocationTargetException.class)
  public void testNewInstanceFailedToInvokeAllocateInstance() throws Exception {
    final UnsafeAllocator unsafeAllocator = new UnsafeAllocator() {
      @Override
      @SuppressWarnings("unchecked")
      public <T> T newInstance(Class<T> c) throws Exception {
        final Method allocateInstance = Class.forName("sun.misc.Unsafe").getMethod("allocateInstance", Class.class);
        return (T) allocateInstance.invoke(null, c);
      }

      @Override
      public void setAccessible(Field field, boolean b) throws IllegalAccessException {
        throw new UnsupportedOperationException("setAccessible is not supported");
      }
    };
    unsafeAllocator.newInstance(Object.class);
  }

  @Test(expected = InvocationTargetException.class)
  public void testNewInstanceFailedToInvokeGetConstructorId() throws Exception {
    final UnsafeAllocator unsafeAllocator = new UnsafeAllocator() {
      @Override
      @SuppressWarnings("unchecked")
      public <T> T newInstance(Class<T> c) throws Exception {
        final Method getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
        final int constructorId = (Integer) getConstructorId.invoke(null, Object.class);
        final Method newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, int.class);
        return (T) newInstance.invoke(null, c, constructorId);
      }

      @Override
      public void setAccessible(Field field, boolean b) throws IllegalAccessException {
        throw new UnsupportedOperationException("setAccessible is not supported");
      }

      @Override
      public void setAccessible(Method method, boolean b) throws IllegalAccessException {
        throw new UnsupportedOperationException("setAccessible is not supported");
      }
    };
    unsafeAllocator.newInstance(Object.class);
  }

  @Test(expected = InvocationTargetException.class)
  public void testNewInstanceFailedToInvokeNewInstance() throws Exception {
    final UnsafeAllocator unsafeAllocator = new UnsafeAllocator() {
      @Override
      @SuppressWarnings("unchecked")
      public <T> T newInstance(Class<T> c) throws Exception {
        final Method newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
        return (T) newInstance.invoke(null, c, Object.class);
      }

      @Override
      public void setAccessible(Field field, boolean b) throws IllegalAccessException {
        throw new UnsupportedOperationException("setAccessible is not supported");
      }
    };
    unsafeAllocator.newInstance(Object.class);
  }

  @Test(expected = UndeclaredThrowableException.class)
  public void testNewInstanceThrowsException() throws Exception {
    final Class<ExceptionClass> exceptionClass = ExceptionClass.class;
    try {
      unsafeAllocator.newInstance(exceptionClass);
    } catch (InvocationTargetException e) {
      throw e.getTargetException();
    }
    Assert.fail("Expected ExceptionClass instance but got: " + unsafeAllocator.newInstance(exceptionClass).getClass());
  }

  static class AbstractClass {
  }

  static interface InterfaceClass {
  }

  static class ConcreteClass {
  }

  static class ExceptionClass extends RuntimeException {
  }
}