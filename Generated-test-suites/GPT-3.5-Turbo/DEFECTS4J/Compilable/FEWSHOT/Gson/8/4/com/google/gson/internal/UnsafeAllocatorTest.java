package com.google.gson.internal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class UnsafeAllocatorTest {
    private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

    @Test
    public void testNewInstance() {
        try {
            // Test with a class that does not have a constructor
            ClassWithoutConstructor obj1 = unsafeAllocator.newInstance(ClassWithoutConstructor.class);
            assertNotNull(obj1);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        try {
            // Test with a class that has a constructor
            ClassWithConstructor obj2 = unsafeAllocator.newInstance(ClassWithConstructor.class);
            assertNotNull(obj2);
            assertEquals("default", obj2.getName());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreate() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);
    }

    @Test
    public void testCreate_UnsupportedOperationException() {
        try {
            ClassWithInterface obj = unsafeAllocator.newInstance(ClassWithInterface.class);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("UnsupportedOperationException should be thrown");
        }

        try {
            ClassWithAbstractModifier obj = unsafeAllocator.newInstance(ClassWithAbstractModifier.class);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("UnsupportedOperationException should be thrown");
        }
    }

    private class ClassWithoutConstructor {

    }

    private class ClassWithConstructor {
        private String name;

        public ClassWithConstructor() {
            this.name = "default";
        }

        public String getName() {
            return name;
        }
    }

    private interface ClassWithInterface {

    }

    private abstract class ClassWithAbstractModifier {

    }
}