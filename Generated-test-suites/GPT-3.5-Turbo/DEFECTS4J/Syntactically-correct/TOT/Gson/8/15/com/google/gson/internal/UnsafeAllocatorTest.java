import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testCreateSunMiscUnsafe() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);

        // Test typical use case
        Class<?> c = String.class;
        String instance = allocator.newInstance(c);
        assertNotNull(instance);

        // Test edge case
        c = UnsafeAllocator.class;
        UnsafeAllocator allocatorInstance = allocator.newInstance(c);
        assertNotNull(allocatorInstance);

        // Test error scenario
        c = Object.class;
        try {
            allocator.newInstance(c);
            fail("Exception not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot allocate " + c, e.getMessage());
        }
    }

    @Test
    public void testCreateObjectStreamClass() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);

        // Test typical use case
        Class<?> c = Integer.class;
        Integer instance = allocator.newInstance(c);
        assertNotNull(instance);

        // Test edge case
        c = UnsafeAllocator.class;
        UnsafeAllocator allocatorInstance = allocator.newInstance(c);
        assertNotNull(allocatorInstance);

        // Test error scenario
        c = Object.class;
        try {
            allocator.newInstance(c);
            fail("Exception not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot allocate " + c, e.getMessage());
        }
    }

    @Test
    public void testCreateObjectInputStream() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);

        // Test typical use case
        Class<?> c = Float.class;
        Float instance = allocator.newInstance(c);
        assertNotNull(instance);

        // Test edge case
        c = UnsafeAllocator.class;
        UnsafeAllocator allocatorInstance = allocator.newInstance(c);
        assertNotNull(allocatorInstance);

        // Test error scenario
        c = Object.class;
        try {
            allocator.newInstance(c);
            fail("Exception not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot allocate " + c, e.getMessage());
        }
    }
}