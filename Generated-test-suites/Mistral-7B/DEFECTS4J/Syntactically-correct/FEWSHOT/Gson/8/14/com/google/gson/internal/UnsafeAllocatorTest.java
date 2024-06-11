package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

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
        assertInstanceOf(Object.class, obj);
    }

    @Test
    public void testNewInstanceForString() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        String str = null;
        try {
            str = (String) allocator.newInstance(String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertInstanceOf(String.class, str);
    }

    @Test
    public void testNewInstanceForAbstractClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> {
            allocator.newInstance(AbstractClass.class);
        });
    }

    @Test
    public void testNewInstanceForInterface() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> {
            allocator.newInstance(Interface.class);
        });
    }
}

class AbstractClass {
    // an abstract class
}

interface Interface {
    // an interface
}