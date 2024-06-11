package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance_ClassThatCanBeInstantiated() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Serializable instance = allocator.newInstance(String.class);
    assertInstanceOf(String.class, instance);
  }

  @Test
  public void testNewInstance_ClassWithInterfaceOrAbstractModifiers_ThrowsException() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertThrows(UnsupportedOperationException.class, () -> {
      allocator.newInstance(Object.class);
    });

    assertThrows(UnsupportedOperationException.class, () -> {
      allocator.newInstance(Runnable.class);
    });
  }

  @Test
  public void testNewInstance_ClassWithNoDefaultConstructor_ThrowsException() throws Exception {
    Class<?> clazz = Class.forName("java.lang.StringBuffer");
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertThrows(InvocationTargetException.class, () -> {
      allocator.newInstance(clazz);
    });
  }

  @Test
  public void testNewInstance_ClassWithNoPublicConstructor_ThrowsException() throws Exception {
    Class<?> privateClass = new ClassPrivilegedAccess() {
      @Override
      public Class<?> getDeclaredClass(String name) throws ClassNotFoundException {
        return Class.forName(name);
      }
    }.getDeclaredClass("com.google.gson.internal.$$PrivatestClass$$");

    Method constructor = privateClass.getDeclaredConstructor(String.class);
    constructor.setAccessible(true);
    constructor.setModifiers(constructor.getModifiers() & ~Modifier.PRIVATE);

    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertThrows(InvocationTargetException.class, () -> {
      allocator.newInstance(privateClass);
    });
  }
}

class ClassPrivilegedAccess {
  @SuppressWarnings("unchecked")
  Class<?> getDeclaredClass(String name) throws ClassNotFoundException {
    return (Class<?>) Class.forName(name, true, ClassLoader.getSystemClassLoader());
  }
}