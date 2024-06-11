import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance_tryJvmSuccess() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        try {
            TestClass obj = allocator.newInstance(TestClass.class);
            assertNotNull(obj);
            assertEquals(TestClass.class, obj.getClass());
        } catch (Exception ex) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testNewInstance_tryJvmUnsupportedClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        try {
            allocator.newInstance(UnsupportedClass.class);
            fail("Should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            // Expected
        } catch (Exception ex) {
            fail("Should not throw any other exception");
        }
    }

    @Test
    public void testNewInstance_tryDalvikvmPostGingerbreadSuccess() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        try {
            TestClass obj = allocator.newInstance(TestClass.class);
            assertNotNull(obj);
            assertEquals(TestClass.class, obj.getClass());
        } catch (Exception ex) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testNewInstance_tryDalvikvmPostGingerbreadUnsupportedClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        try {
            allocator.newInstance(UnsupportedClass.class);
            fail("Should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            // Expected
        } catch (Exception ex) {
            fail("Should not throw any other exception");
        }
    }

    @Test
    public void testNewInstance_tryDalvikvmPreGingerbreadSuccess() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        try {
            TestClass obj = allocator.newInstance(TestClass.class);
            assertNotNull(obj);
            assertEquals(TestClass.class, obj.getClass());
        } catch (Exception ex) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testNewInstance_tryDalvikvmPreGingerbreadUnsupportedClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        try {
            allocator.newInstance(UnsupportedClass.class);
            fail("Should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            // Expected
        } catch (Exception ex) {
            fail("Should not throw any other exception");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_giveUp() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        allocator.newInstance(TestClass.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_giveUpUnsupportedClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        allocator.newInstance(UnsupportedClass.class);
    }

    public static class TestClass {
    }

    public static abstract class UnsupportedClass {
    }
}