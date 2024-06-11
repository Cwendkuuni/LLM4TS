
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    private UnsafeAllocator unsafeAllocator;

    @Test
    public void testNewInstance_WhenClassWithUnsafeAllocation_ReturnsInstance() throws Exception {
        unsafeAllocator = UnsafeAllocator.create();
        ClassToInstantiate classToInstantiate = new ClassToInstantiate();
        ClassToInstantiate newInstance = unsafeAllocator.newInstance(classToInstantiate.getClass());
        assertNotNull(newInstance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_WhenUnsupportedClass_ThrowsUnsupportedOperationException() {
        unsafeAllocator = UnsafeAllocator.create();
        unsupportedClassType instance = unsafeAllocator.newInstance(unsupportedClassType.class);
    }

    private static class ClassToInstantiate {
    }

    private interface unsupportedClassType {
    }
}

