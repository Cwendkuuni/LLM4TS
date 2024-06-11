package com.google.gson.internal;
import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testCreate() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);
    }

    @Test
    public void testCreateWithInvalidClassName() {
        try {
            Class<?> invalidClass = Class.forName("InvalidClassName");
            UnsafeAllocator allocator = UnsafeAllocator.create();
            allocator.newInstance(invalidClass);
            fail("Expected exception for invalid class name.");
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }

    @Test
    public void testCreateWithAbstractClass() {
        try {
            Class<AbstractClass> abstractClass = AbstractClass.class;
            UnsafeAllocator allocator = UnsafeAllocator.create();
            allocator.newInstance(abstractClass);
            fail("Expected exception for abstract class.");
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }

    private abstract class AbstractClass {}

}