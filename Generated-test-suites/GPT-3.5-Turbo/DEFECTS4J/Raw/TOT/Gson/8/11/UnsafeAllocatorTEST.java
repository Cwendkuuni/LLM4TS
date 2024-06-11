import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance_jvmAllocation() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        try {
            DummyClass instance = allocator.newInstance(DummyClass.class);
            assertTrue(instance instanceof DummyClass);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testNewInstance_dalvikvmPostGingerbreadAllocation() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        try {
            GingerbreadDummyClass instance = allocator.newInstance(GingerbreadDummyClass.class);
            assertTrue(instance instanceof GingerbreadDummyClass);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testNewInstance_dalvikvmPreGingerbreadAllocation() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        try {
            PreGingerbreadDummyClass instance = allocator.newInstance(PreGingerbreadDummyClass.class);
            assertTrue(instance instanceof PreGingerbreadDummyClass);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testNewInstance_unsupportedOperation() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        try {
            allocator.newInstance(UnsupportedDummyClass.class);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot allocate " + UnsupportedDummyClass.class, e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testNewInstance_customConstructor() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        try {
            DummyClassWithConstructor instance = allocator.newInstance(DummyClassWithConstructor.class);
            assertNotNull(instance);
            assertEquals("Default Value", instance.getValue());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    private static class DummyClass {
    }

    private static class GingerbreadDummyClass {
    }

    private static class PreGingerbreadDummyClass {
    }

    private static class UnsupportedDummyClass {
    }

    private static class DummyClassWithConstructor {
        private String value;

        public DummyClassWithConstructor() {
            this.value = "Default Value";
        }

        public String getValue() {
            return value;
        }
    }
}