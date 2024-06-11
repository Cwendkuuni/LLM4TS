package com.google.gson.internal;
import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

        try {
            Object instance = unsafeAllocator.newInstance(String.class);
            assertNotNull(instance);
        } catch (Exception e) {
            fail("Failed to create new instance using UnsafeAllocator");
        }
    }

    @Test
    public void testNewInstanceWithUnsupportedClass() {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

        try {
            unsafeAllocator.newInstance(AbstractClass.class);
            fail("Should have thrown UnsupportedOperationException");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testCreateUnsafeAllocator() {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        assertNotNull(unsafeAllocator);
    }

    private static abstract class AbstractClass {
    }

}