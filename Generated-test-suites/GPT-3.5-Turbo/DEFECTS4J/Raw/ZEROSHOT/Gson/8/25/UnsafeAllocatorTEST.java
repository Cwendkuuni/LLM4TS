import com.google.gson.internal.UnsafeAllocator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    private UnsafeAllocator unsafeAllocator;

    @Before
    public void setUp() {
        unsafeAllocator = UnsafeAllocator.create();
    }

    @Test
    public void testNewInstance_withValidClass_shouldReturnInstance() throws Exception {
        // Arrange
        Class<String> clazz = String.class;

        // Act
        String instance = unsafeAllocator.newInstance(clazz);

        // Assert
        assertNotNull(instance);
        assertEquals(clazz, instance.getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_withUnsupportedClass_shouldThrowException() {
        // Arrange
        Class<UnsupportedClass> clazz = UnsupportedClass.class;

        // Act
        unsafeAllocator.newInstance(clazz);

        // Assert (Exception expected)
    }
}

class UnsupportedClass {
    // Not supported for allocation
}