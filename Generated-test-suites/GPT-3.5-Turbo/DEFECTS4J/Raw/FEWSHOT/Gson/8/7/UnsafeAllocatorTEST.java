import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.io.ObjectInputStream;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() {
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
            assertNotNull(allocator.newInstance(Object.class));
            assertNotNull(allocator.newInstance(String.class));
            assertNotNull(allocator.newInstance(Integer.class));
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreate() {
        try {
            Method getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
            getConstructorId.setAccessible(true);
            final int constructorId = (Integer) getConstructorId.invoke(null, Object.class);
            final Method newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, int.class);
            newInstance.setAccessible(true);
            UnsafeAllocator allocator = new UnsafeAllocator() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T newInstance(Class<T> c) throws Exception {
                    return (T) newInstance.invoke(null, c, constructorId);
                }
            };
            assertNotNull(allocator.newInstance(Object.class));
            assertNotNull(allocator.newInstance(String.class));
            assertNotNull(allocator.newInstance(Integer.class));
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateOldDalvik() {
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
            assertNotNull(allocator.newInstance(Object.class));
            assertNotNull(allocator.newInstance(String.class));
            assertNotNull(allocator.newInstance(Integer.class));
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedClass() {
        UnsafeAllocator allocator = new UnsafeAllocator() {
            @Override
            public <T> T newInstance(Class<T> c) {
                throw new UnsupportedOperationException("Cannot allocate " + c);
            }
        };
        allocator.newInstance(Object.class);
    }
}