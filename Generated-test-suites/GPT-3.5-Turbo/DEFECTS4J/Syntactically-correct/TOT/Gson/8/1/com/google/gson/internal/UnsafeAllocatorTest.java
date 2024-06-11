
import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance_JVM_Typical() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        Object instance = unsafeAllocator.newInstance(Object.class);
        // Ensure that the allocated instance is not null
        assertNotNull(instance);
        // Ensure that the allocated instance is an object of the specified class
        assertTrue(Object.class.isInstance(instance));
    }

    @Test
    public void testNewInstance_JVM_Error() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        // Try to allocate an instance of a non-existing class and verify if an exception is thrown
        assertThrows(Exception.class, () -> unsafeAllocator.newInstance(NonExistingClass.class));
    }

    @Test
    public void testNewInstance_Dalvik_PostGingerbread_Typical() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        Object instance = unsafeAllocator.newInstance(String.class);
        // Ensure that the allocated instance is not null
        assertNotNull(instance);
        // Ensure that the allocated instance is an object of the specified class
        assertTrue(String.class.isInstance(instance));
    }

    @Test
    public void testNewInstance_Dalvik_PreGingerbread_Error() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        // Try to allocate an instance of a non-existing class and verify if an exception is thrown
        assertThrows(Exception.class, () -> unsafeAllocator.newInstance(NonExistingClass.class));
    }

    @Test
    public void testNewInstance_GiveUp_Error() {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        // Try to allocate an instance of a non-existing class and verify if an exception is thrown
        assertThrows(UnsupportedOperationException.class, () -> unsafeAllocator.newInstance(Object.class));
    }
}
