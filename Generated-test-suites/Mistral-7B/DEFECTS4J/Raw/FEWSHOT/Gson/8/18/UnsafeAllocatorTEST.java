package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstanceForObject() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        Object obj = null;
        try {
            obj = allocator.newInstance(Object.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(obj);
        assertInstanceOf(Object.class, obj);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceForInterface() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> {
            allocator.newInstance(Comparable.class);
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceForAbstractClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> {
            allocator.newInstance(AbstractClass.class);
        });
    }

    @Test
    public void testNewInstanceForNormalClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        MyClass myClass = null;
        try {
            myClass = (MyClass) allocator.newInstance(MyClass.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(myClass);
        assertInstanceOf(MyClass.class, myClass);
    }

    private static class MyClass {
        // normal class
    }

    private abstract static class AbstractClass {
        // abstract class
    }

    private interface Comparable<T> {
        // interface
    }
}