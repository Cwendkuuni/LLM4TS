package com.google.gson.internal;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class UnsafeAllocatorTest {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

    @Test
    public void testNewInstance_validClass() {
        try {
            Object newInstance = unsafeAllocator.newInstance(TestClass.class);
            assertTrue(newInstance instanceof TestClass);
        } catch (Exception e) {
            fail("An exception occurred");
        }
    }

    @Test
    public void testNewInstance_abstractClass() {
        try {
            unsafeAllocator.newInstance(AbstractTestClass.class);
            fail("An exception should be thrown");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testNewInstance_interface() {
        try {
            unsafeAllocator.newInstance(InterfaceTestClass.class);
            fail("An exception should be thrown");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    static class TestClass {}

    abstract static class AbstractTestClass {}

    interface InterfaceTestClass {}
}