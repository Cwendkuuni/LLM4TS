package com.google.gson.internal;

import org.junit.Assert;
import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance_SunMiscUnsafe() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

        class TestClass {
        }

        TestClass testClass = unsafeAllocator.newInstance(TestClass.class);

        Assert.assertNotNull(testClass);
        Assert.assertEquals(TestClass.class, testClass.getClass());
    }

    @Test
    public void testNewInstance_ObjectStreamClass() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

        class TestClass {
        }

        TestClass testClass = unsafeAllocator.newInstance(TestClass.class);

        Assert.assertNotNull(testClass);
        Assert.assertEquals(TestClass.class, testClass.getClass());
    }

    @Test
    public void testNewInstance_ObjectInputStream() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

        class TestClass {
        }

        TestClass testClass = unsafeAllocator.newInstance(TestClass.class);

        Assert.assertNotNull(testClass);
        Assert.assertEquals(TestClass.class, testClass.getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_Exception() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

        class TestClass {
        }

        TestClass testClass = unsafeAllocator.newInstance(TestClass.class);
    }

}