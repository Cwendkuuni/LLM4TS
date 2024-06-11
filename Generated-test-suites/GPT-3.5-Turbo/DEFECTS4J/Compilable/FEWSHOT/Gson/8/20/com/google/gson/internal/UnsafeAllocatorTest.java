package com.google.gson.internal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

    @Test
    public void testCreate() {
        try {
            UnsafeAllocator allocator = UnsafeAllocator.create();
            assertNotNull(allocator);
        } catch (Exception e) {
            fail("Exception occurred while creating Allocator");
        }
    }

    @Test
    public void testNewInstance() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        try {
            MyClass1 obj1 = allocator.newInstance(MyClass1.class);
            assertEquals(MyClass1.class, obj1.getClass());

            MyClass2 obj2 = allocator.newInstance(MyClass2.class);
            assertEquals(MyClass2.class, obj2.getClass());

            MyClass3 obj3 = allocator.newInstance(MyClass3.class);
            assertEquals(MyClass3.class, obj3.getClass());

            MyClass4 obj4 = allocator.newInstance(MyClass4.class);
            assertEquals(MyClass4.class, obj4.getClass());
        } catch (Exception e) {
            fail("Exception occurred while creating instance using Allocator");
        }
    }

    @Test
    public void testNewInstanceWithUnsupportedClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        try {
            allocator.newInstance(MyClass5.class);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    class MyClass1 {}

    class MyClass2 {}

    class MyClass3 {}

    class MyClass4 {}

    interface MyClass5 {}
}