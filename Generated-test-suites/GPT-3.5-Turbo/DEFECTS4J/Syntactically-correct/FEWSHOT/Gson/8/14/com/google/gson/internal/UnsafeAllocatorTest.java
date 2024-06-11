package com.google.gson.internal;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UnsafeAllocatorTest {

    private UnsafeAllocator unsafeAllocator;

    @Before
    public void setup() {
        unsafeAllocator = UnsafeAllocator.create();
    }

    @Test
    public void testNewInstance_Success() throws Exception {
        Class<TestObject> testClass = TestObject.class;
        TestObject newInstance = unsafeAllocator.newInstance(testClass);
        assertNotNull(newInstance);
        assertEquals(testClass, newInstance.getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_NotInstantiableClass() {
        unsafeAllocator.newInstance(NotInstantiableClass.class);
    }

    private static class TestObject {
    }

    private interface NotInstantiableClass {
    }
}