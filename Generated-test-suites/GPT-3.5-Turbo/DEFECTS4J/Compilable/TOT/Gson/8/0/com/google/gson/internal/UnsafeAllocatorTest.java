package com.google.gson.internal;
import com.google.gson.internal.UnsafeAllocator;
import org.junit.Assert;
import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testCreate() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        Assert.assertNotNull(allocator);
    }

    @Test
    public void testAllocateInstance() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        String string = allocator.newInstance(String.class);
        Assert.assertNotNull(string);
        Assert.assertEquals("", string);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAllocateUnsupportedClass() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        allocator.newInstance(Object.class);
    }

    @Test
    public void testAllocateAbstractClass() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        AbstractClass instance = allocator.newInstance(AbstractClass.class);
        Assert.assertNotNull(instance);
        Assert.assertTrue(instance instanceof AbstractClass);
    }

    @Test
    public void testAllocateInterface() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        InterfaceClass instance = allocator.newInstance(InterfaceClass.class);
        Assert.assertNotNull(instance);
        Assert.assertTrue(instance instanceof InterfaceClass);
    }
}

abstract class AbstractClass {}
interface InterfaceClass {}