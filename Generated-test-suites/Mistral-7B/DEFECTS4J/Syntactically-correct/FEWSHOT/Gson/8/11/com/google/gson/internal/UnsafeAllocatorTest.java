package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCannotAllocateInterface() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNull(allocator.newInstance(String.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCannotAllocateAbstractClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNull(allocator.newInstance(AbstractClass.class));
    }

    @Test
    public void testAllocateSimpleClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        SimpleClass obj = allocator.newInstance(SimpleClass.class);
        assertInstanceOf(SimpleClass.class, obj);
    }

    @Test
    public void testAllocateClassWithCustomConstructor() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        CustomClass obj = allocator.newInstance(CustomClass.class);
        assertInstanceOf(CustomClass.class, obj);
    }

    @Test(expected = Exception.class)
    public void testFailedToAllocateObject() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        allocator.newInstance(FailedToAllocateObject.class);
    }

    private static class SimpleClass {
        SimpleClass() {}
    }

    private static abstract class AbstractClass {
    }

    private static class CustomClass {
        CustomClass(int i) {}
    }

    private static class FailedToAllocateObject {
        private static FailedToAllocateObject instance = new FailedToAllocateObject();

        private FailedToAllocateObject() {
            throw new RuntimeException("Failed to allocate object");
        }

        private static FailedToAllocateObject getInstance() {
            return instance;
        }
    }
}