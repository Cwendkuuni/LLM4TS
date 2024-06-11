package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class UnsafeAllocatorTest {
  
    @Test
    public void testNewInstance_UsingUnsafe() {
        try {
            UnsafeAllocator allocator = UnsafeAllocator.create();
            DummyClass dummy = allocator.newInstance(DummyClass.class);
            assertNotNull(dummy);
        } catch (Exception e) {
            fail("Failed to create instance using Unsafe allocator");
        }
    }
  
    @Test
    public void testNewInstance_NoUnsafe() {
        try {
            UnsafeAllocator allocator = new UnsafeAllocator() {
                @Override
                public <T> T newInstance(Class<T> c) {
                    throw new UnsupportedOperationException("Cannot allocate " + c);
                }
            };
            DummyClass dummy = allocator.newInstance(DummyClass.class);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot allocate class com.google.gson.internal.DummyClass", e.getMessage());
        } catch (Exception e) {
            fail("Should have thrown UnsupportedOperationException");
        }
    }
  
    static class DummyClass {
    }
}