package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstanceForObject() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        Object obj = allocator.newInstance(Object.class);
        assertNotNull(obj);
        assertInstanceOf(Object.class, obj);
    }

    @Test
    public void testNewInstanceForString() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        String str = (String) allocator.newInstance(String.class);
        assertNotNull(str);
        assertInstanceOf(String.class, str);
    }

    @Test
    public void testNewInstanceForArray() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        int size = 10;
        int[] intArray = (int[]) allocator.newInstance(int[].class);
        assertNotNull(intArray);
        assertInstanceOf(int[].class, intArray);
        for (int i = 0; i < size; i++) {
            intArray[i] = i;
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceForAbstractClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(AbstractClass.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceForInterface() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(Interface.class));
    }
}

class AbstractClass {
    // abstract class
}

interface Interface {
    // interface
}