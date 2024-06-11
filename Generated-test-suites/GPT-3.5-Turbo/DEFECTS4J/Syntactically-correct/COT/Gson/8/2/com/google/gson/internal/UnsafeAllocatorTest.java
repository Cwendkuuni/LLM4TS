import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() {
        UnsafeAllocator allocator = new UnsafeAllocatorImpl();

        try {
            Object instance = allocator.newInstance(Object.class);

            assertNotNull(instance);
            assertEquals(Object.class, instance.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}