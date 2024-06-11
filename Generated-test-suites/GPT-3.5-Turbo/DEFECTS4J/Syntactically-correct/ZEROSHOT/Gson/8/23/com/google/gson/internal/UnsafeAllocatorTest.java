import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance_SunMiscUnsafe() {
        try {
            UnsafeAllocator allocator = UnsafeAllocator.create();
            assertNotNull(allocator.newInstance(TestClass.class));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testNewInstance_ObjectStreamClass() {
        try {
            UnsafeAllocator allocator = UnsafeAllocator.create();
            assertNotNull(allocator.newInstance(TestClass.class));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testNewInstance_ObjectInputStream() {
        try {
            UnsafeAllocator allocator = UnsafeAllocator.create();
            assertNotNull(allocator.newInstance(TestClass.class));
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testNewInstance_Unsupported() {
        try {
            UnsafeAllocator allocator = UnsafeAllocator.create();
            allocator.newInstance(TestClass.class);
            fail("Exception not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot allocate TestClass", e.getMessage());
        }
    }

    private static class TestClass {
    }

}