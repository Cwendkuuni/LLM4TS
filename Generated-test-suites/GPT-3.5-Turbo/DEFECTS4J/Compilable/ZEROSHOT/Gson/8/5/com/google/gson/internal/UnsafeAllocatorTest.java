package com.google.gson.internal;
import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        // Test if allocator can create an instance of a specific class
        MyClass obj = allocator.newInstance(MyClass.class);
        assertNotNull(obj);

        // Test if allocator throws an exception for abstract classes
        try {
           allocator.newInstance(AbstractClass.class);
           fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
           // Exception thrown as expected
        }

        // Test if allocator throws an exception for classes with interfaces
        try {
           allocator.newInstance(InterfaceClass.class);
           fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
           // Exception thrown as expected
        }
    }
}

class MyClass {
    // Class for testing purposes
}

abstract class AbstractClass {
    // Class for testing purposes
}

interface InterfaceClass {
    // Class for testing purposes
}