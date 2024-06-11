import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {
    
    // Test case for newInstance() method
    @Test
    public void testNewInstance() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        
        // Test with simple class
        try {
            SimpleClass instance = allocator.newInstance(SimpleClass.class);
            assertNotNull(instance);
            assertTrue(instance instanceof SimpleClass);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test with class that throws an exception in constructor
        try {
            ExceptionClass instance = allocator.newInstance(ExceptionClass.class);
            assertNotNull(instance);
            assertTrue(instance instanceof ExceptionClass);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Edge case for newInstance() method
    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceEdgeCase() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        
        // Test with interface
        allocator.newInstance(InterfaceClass.class);
        
        // Test with abstract class
        allocator.newInstance(AbstractClass.class);
    }
    
    // Test case for create() method
    @Test
    public void testCreate() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);
    }
    
    // Helper classes for testing
    
    public static class SimpleClass {
        public SimpleClass() {
        }
    }
    
    public static class ExceptionClass {
        public ExceptionClass() {
            throw new RuntimeException();
        }
    }
    
    public interface InterfaceClass {
        
    }
    
    public abstract class AbstractClass {
        
    }
}


