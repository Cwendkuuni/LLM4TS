
package com.google.gson.internal;

import org.junit.Test;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {
    @Test
    public void testCreate() {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        assertNotNull(unsafeAllocator);
    }

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        assertNotNull(unsafeAllocator);

        TestClass obj = unsafeAllocator.newInstance(TestClass.class);
        assertNotNull(obj);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedNewInstance() throws Exception {
        UnsafeAllocator unsafeAllocator = new UnsafeAllocator() {
            @Override
            public <T> T newInstance(Class<T> c) {
                throw new UnsupportedOperationException("Cannot allocate " + c);
            }
        };
        assertNotNull(unsafeAllocator);

        unsafeAllocator.newInstance(TestClass.class);
    }

    @Test
    public void testCheckInstantiableClass() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        assertNotNull(unsafeAllocator);

        try {
            unsafeAllocator.newInstance(AbstractClass.class);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot allocate class com.google.gson.internal.AbstractClass", e.getMessage());
        }

        try {
            unsafeAllocator.newInstance(Interface.class);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot allocate class com.google.gson.internal.Interface", e.getMessage());
        }

        TestClass obj = unsafeAllocator.newInstance(TestClass.class);
        assertNotNull(obj);
    }

    static class TestClass {
        public TestClass() {
        }
    }

    abstract static class AbstractClass {
        public AbstractClass() {
        }
    }

    interface Interface {
    }
}

