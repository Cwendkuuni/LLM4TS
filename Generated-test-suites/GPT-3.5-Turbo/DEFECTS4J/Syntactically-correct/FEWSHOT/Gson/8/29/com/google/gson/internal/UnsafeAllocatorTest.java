import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {
    @Test
    public void testNewInstance_AvailableUnsafe() {
        try {
            UnsafeAllocator allocator = UnsafeAllocator.create();
            assertNotNull(allocator.newInstance(Object.class));
        } catch (Exception e) {
            fail("Exception thrown when using UnsafeAllocator");
        }
    }

    @Test
    public void testNewInstance_UnavailableUnsafe() {
        try {
            UnsafeAllocator allocator = new UnsafeAllocator() {
                @Override
                public <T> T newInstance(Class<T> c) {
                    throw new UnsupportedOperationException("Cannot allocate " + c);
                }
            };
            allocator.newInstance(Object.class);
            fail("Exception not thrown when using UnsafeAllocator");
        } catch (UnsupportedOperationException e) {
            // expected
        } catch (Exception e) {
            fail("Unexpected exception thrown when using UnsafeAllocator");
        }
    }

    @Test
    public void testNewInstance_ClassObject() {
        try {
            UnsafeAllocator allocator = UnsafeAllocator.create();
            assertNotNull(allocator.newInstance(Class.class));
            assertNotNull(allocator.newInstance(String.class));
            assertNotNull(allocator.newInstance(Integer.class));
            assertNotNull(allocator.newInstance(Double.class));
        } catch (Exception e) {
            fail("Exception thrown when using UnsafeAllocator");
        }
    }
}