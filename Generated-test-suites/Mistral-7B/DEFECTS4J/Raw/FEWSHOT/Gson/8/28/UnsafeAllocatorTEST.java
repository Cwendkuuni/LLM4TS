package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance_Objects() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        Object obj = allocator.newInstance(Object.class);
        assertInstanceOf(Object.class, obj);
    }

    @Test
    public void testNewInstance_String() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        String str = (String) allocator.newInstance(String.class);
        assertInstanceOf(String.class, str);
    }

    @Test
    public void testNewInstance_Throwable() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(Throwable.class));
    }

    @Test
    public void testNewInstance_Interface() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(Runnable.class));
    }

    @Test
    public void testNewInstance_AbstractClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(AbstractClass.class));
    }

    @Test
    public void testNewInstance_ThrowingConstructor() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        Class<?> throwingClass = ThrowingConstructorClass.class;
        Object instance = allocator.newInstance(throwingClass);
        assertInstanceOf(ThrowingConstructorClass.class, instance);
        assertThrows(RuntimeException.class, () -> {
            ThrowingConstructorClass obj = (ThrowingConstructorClass) instance;
            obj.throwingMethod();
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_UnsupportedClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        allocator.newInstance(UnsupportedClass.class);
    }

    private static class AbstractClass {}

    private static class ThrowingConstructorClass implements Throwable {
        public void throwingMethod() throws RuntimeException {
            throw new RuntimeException();
        }
    }

    private static class UnsupportedClass {
        // this class cannot be instantiated
    }

    private static interface Runnable {}
}