import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    private UnsafeAllocator unsafeAllocator;

    @Before
    public void setup() {
        unsafeAllocator = UnsafeAllocator.create();
    }

    @Test
    public void testNewInstance() throws Exception {
        // Arrange
        Class<?> c = Object.class;

        // Act
        Object instance = unsafeAllocator.newInstance(c);

        // Assert
        assertNotNull(instance);
        assertTrue(c.isInstance(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_UnsupportedOperation() throws Exception {
        // Arrange
        Class<?> c = SomeInterface.class;

        // Act
        unsafeAllocator.newInstance(c);
    }

    @Test
    public void testCreate() {
        // Arrange

        // Act
        UnsafeAllocator allocator = UnsafeAllocator.create();

        // Assert
        assertNotNull(allocator);
    }

    @Test
    public void testCreate_Unsupported() {
        // Arrange
        UnsafeAllocator originalAllocator = unsafeAllocator;

        try {
            // Act
            unsafeAllocator = null;
            unsafeAllocator = UnsafeAllocator.create();

            // Assert
            assertNotEquals(originalAllocator, unsafeAllocator);
        } finally {
            unsafeAllocator = originalAllocator;
        }
    }

    private interface SomeInterface {
        void someMethod();
    }

    // 
    // Add additional test cases for edge cases and exception handling scenarios
    // 
}