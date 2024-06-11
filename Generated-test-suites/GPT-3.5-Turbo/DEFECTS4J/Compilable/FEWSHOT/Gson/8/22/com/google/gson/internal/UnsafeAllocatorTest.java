package com.google.gson.internal;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;


public class UnsafeAllocatorTest {
    @Test
    public void testCreate() {
        assertNotNull(UnsafeAllocator.create());
    }

    @Test
    public void testNewInstance() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(Exception.class, () -> {
            allocator.newInstance(Object.class);
        });
    }
}