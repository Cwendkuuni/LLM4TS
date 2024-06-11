import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testCreateUnsafeAllocator() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);
    }

    @Test
    public void testNewInstance() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        try {
            Object newInstance = allocator.newInstance(Object.class);
            assertNotNull(newInstance);
        } catch (Exception e) {
            fail("Exception thrown while creating new instance");
        }
    }

    @Test
    public void testNewInstanceUnsupported() {
        UnsafeAllocator allocator = new UnsafeAllocator() {
            @Override
            public <T> T newInstance(Class<T> c) {
                throw new UnsupportedOperationException("Cannot allocate " + c);
            }
        };

        try {
            allocator.newInstance(String.class);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testNewInstanceWithInterface() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        try {
            allocator.newInstance(Runnable.class);
            fail("UnsupportedOperationException not thrown for interface");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testNewInstanceWithAbstractClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        try {
            allocator.newInstance(AbstractClass.class);
            fail("UnsupportedOperationException not thrown for abstract class");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }

    private abstract class AbstractClass {
    }
}