import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

    private UnsafeAllocator allocator;

    @Before
    public void setup() {
        allocator = UnsafeAllocator.create();
    }

    @Test
    public void testNewInstance_SunMiscUnsafeClass_ExceptionThrown() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            final Object unsafe = f.get(null);
            final Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
            UnsafeAllocator allocator = new UnsafeAllocator() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T newInstance(Class<T> c) throws Exception {
                    return (T) allocateInstance.invoke(unsafe, c);
                }
            };

            // Create instance of a class that throws an exception in its constructor
            // This should throw an exception as well
            allocator.newInstance(ClassWithExceptionConstructor.class);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
            assertEquals("Constructor exception", e.getMessage());
        }
    }

    @Test
    public void testNewInstance_ObjectInputStreamClass_ExceptionThrown() {
        try {
            final Method newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
            newInstance.setAccessible(true);
            UnsafeAllocator allocator = new UnsafeAllocator() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T newInstance(Class<T> c) throws Exception {
                    return (T) newInstance.invoke(null, c, Object.class);
                }
            };

            // Create instance of a class with a private constructor
            // This should throw an exception as well
            allocator.newInstance(ClassWithPrivateConstructor.class);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
            assertEquals("Constructor exception", e.getMessage());
        }
    }

    @Test
    public void testNewInstance_DefaultConstructor_Success() {
        try {
            UnsafeAllocator allocator = UnsafeAllocator.create();

            // Create instance of a class with a default constructor
            ClassWithDefaultConstructor instance = allocator.newInstance(ClassWithDefaultConstructor.class);
            assertNotNull(instance);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testNewInstance_NullClass_ExceptionThrown() {
        try {
            allocator.newInstance(null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
            assertEquals("Cannot allocate null", e.getMessage());
        }
    }

    private static class ClassWithExceptionConstructor {
        public ClassWithExceptionConstructor() throws Exception {
            throw new Exception("Constructor exception");
        }
    }

    private static class ClassWithPrivateConstructor {
        private ClassWithPrivateConstructor() {
        }
    }

    private static class ClassWithDefaultConstructor {
        public ClassWithDefaultConstructor() {
        }
    }
}