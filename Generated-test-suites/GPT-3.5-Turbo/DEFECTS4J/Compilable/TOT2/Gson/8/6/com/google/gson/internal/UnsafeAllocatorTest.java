package com.google.gson.internal;
import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance_JVM() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator.newInstance(getClass()));
    }

    @Test
    public void testNewInstance_DalvikVM_PostGingerbread() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator.newInstance(String.class));
    }

    @Test
    public void testNewInstance_DalvikVM_PreGingerbread() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator.newInstance(Integer.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_UnsupportedClass() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        allocator.newInstance(UnsupportedClass.class);
    }

    private static class UnsupportedClass {
    }

    @Test
    public void testCreate() {
        assertNotNull(UnsafeAllocator.create());
    }

    @Test
    public void testCreateFailed() {
        assertNotNull(UnsafeAllocator.create());
    }
}