import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator unsafeAllocator = new UnsafeAllocator() {
            @Override
            public <T> T newInstance(Class<T> c) throws Exception {
              // Test basic functionality
              return super.newInstance(c);
            }
        };

        // Test typical use case
        assertNotNull(unsafeAllocator.newInstance(String.class));
    }

    @Test
    public void testCreate() {
        // Test creation of UnsafeAllocator instance
        assertNotNull(UnsafeAllocator.create());
    }

    @Test
    public void testNewInstanceAbstractClass() {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

        // Test edge case: Trying to create an instance of an abstract class
        try {
            unsafeAllocator.newInstance(AbstractClass.class);
            fail("Expected exception for abstract class");
        } catch (UnsupportedOperationException e) {
            // This exception is expected
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testNewInstanceInterface() {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

        // Test edge case: Trying to create an instance of an interface
        try {
            unsafeAllocator.newInstance(Interface.class);
            fail("Expected exception for interface");
        } catch (UnsupportedOperationException e) {
            // This exception is expected
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    private static abstract class AbstractClass {}

    private static interface Interface {}
}