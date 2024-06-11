
package com.google.gson.internal;

import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance_Unsafe() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        ClassA classA = unsafeAllocator.newInstance(ClassA.class);

        assertNotNull(classA);
        assertEquals(ClassA.class, classA.getClass());
    }

    @Test
    public void testNewInstance_Valat() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        ClassB classB = unsafeAllocator.newInstance(ClassB.class);

        assertNotNull(classB);
        assertEquals(ClassB.class, classB.getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_Interface() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        unsafeAllocator.newInstance(InterfaceA.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_Abstract() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        unsafeAllocator.newInstance(AbstractClassA.class);
    }

    private static class ClassA {
        public ClassA() {
            // default constructor
        }
    }

    private static class ClassB {
        private final String data;

        public ClassB(String data) {
            this.data = data;
        }
    }

    private interface InterfaceA {
        // empty interface

    }

    private static abstract class AbstractClassA {
        // empty abstract class
    }
}
