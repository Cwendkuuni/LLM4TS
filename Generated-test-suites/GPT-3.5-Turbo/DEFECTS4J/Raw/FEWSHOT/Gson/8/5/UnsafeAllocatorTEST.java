
import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {
   
    @Test
    public void testCreate() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);
    }

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        Class<?> c = String.class;
        String instance = allocator.newInstance(c);
        assertNotNull(instance);
        assertEquals("", instance); // Additional test to make sure instance is empty string
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceUnsupported() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        Class<?> c = Class.class;
        allocator.newInstance(c);
    }
}
