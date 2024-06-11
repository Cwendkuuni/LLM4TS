package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UnsafeAllocatorTest {

    @SuppressWarnings("unchecked")
    private static <T> UnsafeAllocator getUnsafeAllocator() {
        return (UnsafeAllocator) UnsafeAllocator.create();
    }

    @Test
    public void testNewInstance_Object() {
        UnsafeAllocator unsafeAllocator = getUnsafeAllocator();
        Object obj = unsafeAllocator.newInstance(Object.class);
        assertInstanceOf(Object.class, obj);
    }

    @Test
    public void testNewInstance_String() {
        UnsafeAllocator unsafeAllocator = getUnsafeAllocator();
        String str = (String) unsafeAllocator.newInstance(String.class);
        assertInstanceOf(String.class, str);
    }

    @Test
    public void testNewInstance_WrapClass() {
        UnsafeAllocator unsafeAllocator = getUnsafeAllocator();
        Integer intObj = (Integer) unsafeAllocator.newInstance(Integer.TYPE);
        assertInstanceOf(Integer.class, intObj);
    }

    @Test
    public void testNewInstance_Array() {
        UnsafeAllocator unsafeAllocator = getUnsafeAllocator();
        int length = 10;
        int[] intArray = (int[]) unsafeAllocator.newInstance(int[].class, length);
        assertInstanceOf(int[].class, intArray.getClass());
        assertArrayEquals(new int[length], intArray);
    }

    @Test
    public void testNewInstance_ClassWithInterface() {
        UnsafeAllocator unsafeAllocator = getUnsafeAllocator();
        try {
            unsafeAllocator.newInstance(Interface.class);
            assert false; // should throw UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testNewInstance_ClassWithAbstractMethod() {
        UnsafeAllocator unsafeAllocator = getUnsafeAllocator();
        try {
            unsafeAllocator.newInstance(AbstractClass.class);
            assert false; // should throw UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testNewInstance_Null() {
        UnsafeAllocator unsafeAllocator = getUnsafeAllocator();
        assertNull(unsafeAllocator.newInstance(null));
    }
}

class Interface {}

abstract class AbstractClass {}