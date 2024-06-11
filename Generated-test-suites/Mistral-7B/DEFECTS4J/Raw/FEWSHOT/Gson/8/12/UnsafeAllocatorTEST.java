package com.google.gson.internal;

import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import java.lang.Class;
import java.lang.reflect.InvocationTargetException;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        // Test with a simple Java class
        Class<Object> objectClass = Object.class;
        Object instance = allocator.newInstance(objectClass);
        assert (instance != null);

        // Test with a custom class
        class TestClass {}
        Class<TestClass> testClass = TestClass.class;
        TestClass instanceTestClass = allocator.newInstance(testClass);
        assert (instanceTestClass != null);

        // Test with an interface
        interface TestInterface {}
        Class<TestInterface> testInterface = TestInterface.class;
        try {
            allocator.newInstance(testInterface);
            assert false : "Should not be able to allocate an instance of an interface";
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test with an abstract class
        abstract class TestAbstractClass {}
        Class<TestAbstractClass> testAbstractClass = TestAbstractClass.class;
        try {
            allocator.newInstance(testAbstractClass);
            assert false : "Should not be able to allocate an instance of an abstract class";
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceUnableToAllocate() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        Class<?> failClass = Class.forName("java.lang.String");
        allocator.newInstance(failClass);
    }
}