
import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testCreate() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);
    }

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        // Test a class with a public constructor
        MyClass1 instance1 = allocator.newInstance(MyClass1.class);
        assertNotNull(instance1);

        // Test a class with a private constructor
        MyClass2 instance2 = allocator.newInstance(MyClass2.class);
        assertNotNull(instance2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceUnsupportedClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        allocator.newInstance(MyClass3.class);
    }

    @Test
    public void testCreateJvm() throws Exception {
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

        assertNotNull(allocator);
        MyClass1 instance1 = allocator.newInstance(MyClass1.class);
        assertNotNull(instance1);
        MyClass2 instance2 = allocator.newInstance(MyClass2.class);
        assertNotNull(instance2);
    }

    @Test
    public void testCreateDalvikPostGingerbread() throws Exception {
        Method getConstructorId = ObjectStreamClass.class
                .getDeclaredMethod("getConstructorId", Class.class);
        getConstructorId.setAccessible(true);
        final int constructorId = (Integer) getConstructorId.invoke(null, Object.class);
        final Method newInstance = ObjectStreamClass.class
                .getDeclaredMethod("newInstance", Class.class, int.class);
        newInstance.setAccessible(true);

        UnsafeAllocator allocator = new UnsafeAllocator() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> T newInstance(Class<T> c) throws Exception {
                return (T) newInstance.invoke(null, c, constructorId);
            }
        };

        assertNotNull(allocator);
        MyClass1 instance1 = allocator.newInstance(MyClass1.class);
        assertNotNull(instance1);
        MyClass2 instance2 = allocator.newInstance(MyClass2.class);
        assertNotNull(instance2);
    }

    @Test
    public void testCreateDalvikPreGingerbread() throws Exception {
        final Method newInstance = ObjectInputStream.class
                .getDeclaredMethod("newInstance", Class.class, Class.class);
        newInstance.setAccessible(true);

        UnsafeAllocator allocator = new UnsafeAllocator() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> T newInstance(Class<T> c) throws Exception {
                return (T) newInstance.invoke(null, c, Object.class);
            }
        };

        assertNotNull(allocator);
        MyClass1 instance1 = allocator.newInstance(MyClass1.class);
        assertNotNull(instance1);
        MyClass2 instance2 = allocator.newInstance(MyClass2.class);
        assertNotNull(instance2);
    }

    public static class MyClass1 {
        public MyClass1() {
        }
    }

    public static class MyClass2 {
        private MyClass2() {
        }
    }

    public interface MyClass3 {
    }
}

