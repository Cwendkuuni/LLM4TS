
package com.google.gson.internal;

import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testCreate() {
        assertNotNull(UnsafeAllocator.create());       
    }

    @Test
    public void testNewInstance() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        try {
            Object obj = allocator.newInstance(String.class);
            assertTrue(obj instanceof String);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

}
