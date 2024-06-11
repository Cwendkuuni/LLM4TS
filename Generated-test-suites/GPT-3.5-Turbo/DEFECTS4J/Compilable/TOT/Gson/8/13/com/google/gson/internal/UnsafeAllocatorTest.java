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
    public void testCreate_AllocateInstanceUsingUnsafe_UnsupportedOperationException() {
        // Prepare
        Class<UnsupportedClass> unsupportedClass = UnsupportedClass.class;

        // Execute & Assert
        try {
            unsafeAllocator.newInstance(unsupportedClass);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Test passed
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testCreate_AllocateInstanceUsingUnsafe_Success() {
        // Prepare
        Class<TestClass> testClass = TestClass.class;

        // Execute
        TestClass instance = null;
        try {
            instance = unsafeAllocator.newInstance(testClass);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }

        // Assert
        assertNotNull(instance);
    }

    @Test
    public void testCreate_AllocateInstanceUsingObjectStreamClass_Success() {
        // Prepare
        Class<TestClass> testClass = TestClass.class;

        // Execute
        TestClass instance = null;
        try {
            instance = unsafeAllocator.newInstance(testClass);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }

        // Assert
        assertNotNull(instance);
    }

    @Test
    public void testCreate_AllocateInstanceUsingObjectStreamClass_ExceptionThrown() {
        // Prepare
        Class<PrivateConstructorClass> privateConstructorClass = PrivateConstructorClass.class;

        // Execute & Assert
        try {
            unsafeAllocator.newInstance(privateConstructorClass);
            fail("Expected Exception");
        } catch (Exception e) {
            // Test passed
        }
    }

    @Test
    public void testCreate_AllocateInstanceUsingObjectInputStream_Success() {
        // Prepare
        Class<TestClass2> testClass2 = TestClass2.class;

        // Execute
        TestClass2 instance = null;
        try {
            instance = unsafeAllocator.newInstance(testClass2);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }

        // Assert
        assertNotNull(instance);
    }

    @Test
    public void testCreate_AllocateInstanceUsingObjectInputStream_ExceptionThrown() {
        // Prepare
        Class<PrivateConstructorClass> privateConstructorClass = PrivateConstructorClass.class;

        // Execute & Assert
        try {
            unsafeAllocator.newInstance(privateConstructorClass);
            fail("Expected Exception");
        } catch (Exception e) {
            // Test passed
        }
    }

    private static class TestClass {
        // Class for testing
        public TestClass() {
        }
    }

    private static class UnsupportedClass {
        // Class for testing unsupported exception
        public UnsupportedClass() {
            throw new UnsupportedOperationException();
        }
    }

    private static class TestClass2 {
        // Class for testing
        public TestClass2() {
        }
    }

    private static class PrivateConstructorClass {
        // Class with private constructor for testing
        private PrivateConstructorClass() {
        }
    }

}