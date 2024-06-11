package com.google.gson.internal;

import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testCreate() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);
    }

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        
        // Test with a class that can be instantiated
        ClassA objA = allocator.newInstance(ClassA.class);
        assertNotNull(objA);
        assertEquals("ClassA", objA.getName());
        
        // Test with a class that cannot be instantiated
        try {
            allocator.newInstance(ClassB.class);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }
    }

    // Test classes
    private static class ClassA {
        private final String name;
        
        public ClassA() {
            this.name = "ClassA";
        }
        
        public String getName() {
            return name;
        }
    }
    
    private static abstract class ClassB {
        
    }
}