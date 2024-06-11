import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstanceWithUnsafe() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        assertNotNull(unsafeAllocator);

        // Test allocation of a class with interface
        try {
            unsafeAllocator.newInstance(InterfaceClass.class);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception thrown
        }

        // Test allocation of a class with abstract modifier
        try {
            unsafeAllocator.newInstance(AbstractClass.class);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception thrown
        }

        // Test allocation of a normal class
        NormalClass normalClass = unsafeAllocator.newInstance(NormalClass.class);
        assertNotNull(normalClass);
    }

    interface InterfaceClass {
        void method();
    }

    abstract class AbstractClass {
        abstract void method();
    }

    class NormalClass {
        void method() {}
    }
}