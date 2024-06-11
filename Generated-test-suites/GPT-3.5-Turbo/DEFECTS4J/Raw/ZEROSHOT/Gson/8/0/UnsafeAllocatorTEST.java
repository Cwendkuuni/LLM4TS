import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class UnsafeAllocatorTest {

    private UnsafeAllocator unsafeAllocator;

    @Before
    public void setUp() {
        unsafeAllocator = UnsafeAllocator.create();
    }

    @Test
    public void testCreate() {
        assertNotNull(unsafeAllocator);
    }

    @Test
    public void testNewInstance() throws Exception {
        Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
        Field f = unsafeClass.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        final Object unsafe = f.get(null);
        final Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
        Class<UnsafeAllocator> unsafeAllocatorClass = UnsafeAllocator.class;
        Object instance = unsafeAllocator.newInstance(unsafeAllocatorClass);
        assertEquals(unsafeAllocatorClass, instance.getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceUnsupported() throws Exception {
        Class<?> unsupportedClass = Object.class;
        unsafeAllocator.newInstance(unsupportedClass);
    }

    @Test
    public void testCanInstantiateClass() {
        Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
        Class<UnsafeAllocator> unsafeAllocatorClass = UnsafeAllocator.class;
        assertTrue(Modifier.isAbstract(unsafeAllocatorClass.getModifiers()));
        assertFalse(Modifier.isInterface(unsafeAllocatorClass.getModifiers()));
    }
}