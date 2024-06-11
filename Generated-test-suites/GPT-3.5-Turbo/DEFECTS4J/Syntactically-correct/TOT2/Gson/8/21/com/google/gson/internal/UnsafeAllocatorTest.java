import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        
        try {
            Object instance = allocator.newInstance(String.class);
            assertNotNull(instance);
            assertTrue(instance instanceof String);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testCreateUnsafeAllocator() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        
        assertNotNull(allocator);
    }
    
    @Test
    public void testUnsupportedOperation() {
        UnsafeAllocator allocator = new UnsafeAllocator() {
            @Override
            public <T> T newInstance(Class<T> c) {
              throw new UnsupportedOperationException("Cannot allocate " + c);
            }
        };
        
        try {
            allocator.newInstance(String.class);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }
}