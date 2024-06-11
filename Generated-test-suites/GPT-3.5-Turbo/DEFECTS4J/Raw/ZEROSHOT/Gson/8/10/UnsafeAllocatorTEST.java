package com.google.gson.internal;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    private UnsafeAllocator unsafeAllocator;

    @Before
    public void setUp() {
        unsafeAllocator = UnsafeAllocator.create();
    }

    @Test
    public void testNewInstance_JVM() throws Exception {
        Class<?> c = TestClass.class;
        TestClass newInstance = unsafeAllocator.newInstance(c);
        assertNotNull(newInstance);
        assertEquals(c, newInstance.getClass());
    }

    @Test
    public void testNewInstance_Dalvikvm_PostGingerbread() throws Exception {
        Class<?> c = TestClass.class;
        TestClass newInstance = unsafeAllocator.newInstance(c);
        assertNotNull(newInstance);
        assertEquals(c, newInstance.getClass());
    }

    @Test
    public void testNewInstance_Dalvikvm_PreGingerbread() throws Exception {
        Class<?> c = TestClass.class;
        TestClass newInstance = unsafeAllocator.newInstance(c);
        assertNotNull(newInstance);
        assertEquals(c, newInstance.getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_GiveUp() {
        Class<?> c = TestClass.class;
        unsafeAllocator.newInstance(c);
    }

    static class TestClass {
    }
}