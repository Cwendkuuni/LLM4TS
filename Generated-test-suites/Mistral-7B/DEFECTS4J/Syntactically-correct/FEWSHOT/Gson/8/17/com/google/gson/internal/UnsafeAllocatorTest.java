package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class UnsafeAllocatorTest {
  private static UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstanceForObject() {
    assertInstanceOf(Object.class, unsafeAllocator.newInstance(Object.class));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceForInterface() {
    unsafeAllocator.newInstance(Comparable.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceForAbstractClass() {
    unsafeAllocator.newInstance(AbstractClass.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceForFinalClass() {
    unsafeAllocator.newInstance(FinalClass.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceForAccessibleClass() {
    final class AccessibleClass {}
    unsafeAllocator.newInstance(AccessibleClass.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceForPrivateConstructorClass() {
    final class PrivateConstructorClass {
      private PrivateConstructorClass() {}
    }
    unsafeAllocator.newInstance(PrivateConstructorClass.class);
  }

  @Test
  public void testNewInstanceForPublicConstructorClass() {
    final class PublicConstructorClass {
      PublicConstructorClass() {}
    }
    assertInstanceOf(PublicConstructorClass.class, unsafeAllocator.newInstance(PublicConstructorClass.class));
  }

  @Test
  public void testNewInstanceForClassWithLongConstructor() {
    final class LongConstructorClass {
      LongConstructorClass(long l) {}
    }
    assertInstanceOf(LongConstructorClass.class, unsafeAllocator.newInstance(LongConstructorClass.class));
  }

  @Test
  public void testNewInstanceForClassWithGenericType() {
    final class GenericType<T> {
      GenericType(T t) {}
    }
    assertInstanceOf(GenericType.class, unsafeAllocator.newInstance((Class<GenericType<Integer>>) GenericType.class));
  }

  @Test
  public void testNewInstanceForClassWithSuperClass() {
    final class SuperClass {}
    final class SubClass extends SuperClass {}
    assertInstanceOf(SubClass.class, unsafeAllocator.newInstance(SubClass.class));
  }

  @Test
  public void testNewInstanceForClassWithGenericSuperClass() {
    final class SuperClass<T> {}
    final class SubClass<S> extends SuperClass<S> {}
    assertInstanceOf(SubClass.class, unsafeAllocator.newInstance((Class<SubClass<Integer>>) SubClass.class));
  }
}