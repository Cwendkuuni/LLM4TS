import org.junit.Test;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance_sunMiscUnsafe() {
        // Arrange
        UnsafeAllocator allocator = UnsafeAllocator.create();

        // Act
        try {
            Object instance = allocator.newInstance(SunMiscUnsafe.class);

            // Assert
            assertNotNull(instance);
            assertTrue(instance instanceof SunMiscUnsafe);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testNewInstance_objectStreamClass() {
        // Arrange
        UnsafeAllocator allocator = UnsafeAllocator.create();

        // Act
        try {
            Object instance = allocator.newInstance(ObjectStreamClass.class);

            // Assert
            assertNotNull(instance);
            assertTrue(instance instanceof ObjectStreamClass);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testNewInstance_objectInputStream() {
        // Arrange
        UnsafeAllocator allocator = UnsafeAllocator.create();

        // Act
        try {
            Object instance = allocator.newInstance(ObjectInputStream.class);

            // Assert
            assertNotNull(instance);
            assertTrue(instance instanceof ObjectInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_unsupported() {
        // Arrange
        UnsafeAllocator allocator = UnsafeAllocator.create();

        // Act
        allocator.newInstance(UnsupportedClass.class);

        // Assert (Expected exception)
    }

    private static class SunMiscUnsafe {
        // Empty class for testing
    }

    private static abstract class UnsupportedClass {
        // Empty abstract class for testing
    }
}