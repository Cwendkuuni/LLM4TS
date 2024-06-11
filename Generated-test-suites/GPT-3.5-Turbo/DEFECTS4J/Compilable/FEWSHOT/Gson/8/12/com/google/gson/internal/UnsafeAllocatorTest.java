package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {
    
    @Test
    public void testNewInstance() {
        try {
            UnsafeAllocator allocator = UnsafeAllocator.create();
            
            // Test for a class with a public constructor
            MyClass myClass = allocator.newInstance(MyClass.class);
            assertNotNull(myClass);
            
            // Test for a class with a private constructor
            MyPrivateClass myPrivateClass = allocator.newInstance(MyPrivateClass.class);
            assertNotNull(myPrivateClass);
            
            // Test for a class with no constructor
            MyClassNoConstructor myClassNoConstructor = allocator.newInstance(MyClassNoConstructor.class);
            assertNotNull(myClassNoConstructor);
            
            // Test for a class with an interface
            try {
                MyInterface myInterface = allocator.newInstance(MyInterface.class);
                fail("Expected UnsupportedOperationException");
            } catch (UnsupportedOperationException e) {
                // Expected exception
            }
            
            // Test for a class with an abstract modifier
            try {
                MyAbstractClass myAbstractClass = allocator.newInstance(MyAbstractClass.class);
                fail("Expected UnsupportedOperationException");
            } catch (UnsupportedOperationException e) {
                // Expected exception
            }
            
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    class MyClass {
        public MyClass() {
        }
    }
    
    class MyPrivateClass {
        private MyPrivateClass() {
        }
    }
    
    class MyClassNoConstructor {
        
    }
    
    interface MyInterface {
        
    }
    
    abstract class MyAbstractClass {
        
    }
}