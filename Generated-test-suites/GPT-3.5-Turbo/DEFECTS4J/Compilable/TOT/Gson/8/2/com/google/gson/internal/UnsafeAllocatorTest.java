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
    public void testNewInstance_WithJVM() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        assertNotNull(unsafeAllocator);

        // Typical use case
        String stringInstance = unsafeAllocator.newInstance(String.class);
        assertNotNull(stringInstance);
        assertEquals("", stringInstance);

        // Edge case
        Integer integerInstance = unsafeAllocator.newInstance(Integer.class);
        assertNotNull(integerInstance);
        assertEquals(0, integerInstance.intValue());

        // Error scenario
        try {
            unsafeAllocator.newInstance(TestClass.class);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testNewInstance_WithDalvikVM_PostGingerbread() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        assertNotNull(unsafeAllocator);

        // Typical use case
        Object objectInstance = unsafeAllocator.newInstance(Object.class);
        assertNotNull(objectInstance);

        // Edge case
        Class<?> classInstance = unsafeAllocator.newInstance(Class.class);
        assertNotNull(classInstance);
        assertEquals(Object.class, classInstance);

        // Error scenario
        try {
            unsafeAllocator.newInstance(TestClass.class);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testNewInstance_WithDalvikVM_PreGingerbread() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        assertNotNull(unsafeAllocator);

        // Typical use case
        TestClass testClassInstance = unsafeAllocator.newInstance(TestClass.class);
        assertNotNull(testClassInstance);

        // Edge case
        TestInterface testInterfaceInstance = unsafeAllocator.newInstance(TestInterface.class);
        assertNotNull(testInterfaceInstance);

        // Error scenario
        try {
            unsafeAllocator.newInstance(TestAbstractClass.class);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    public static class TestClass {}

    public interface TestInterface {}

    public abstract static class TestAbstractClass {}
}