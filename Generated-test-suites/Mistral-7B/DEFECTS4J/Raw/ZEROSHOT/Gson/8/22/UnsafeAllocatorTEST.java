import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class UnsafeAllocatorTest {

    private UnsafeAllocator unsafeAllocator;

    @Test
    public void testNewInstanceWithObjectClass() throws Exception {
        unsafeAllocator = UnsafeAllocator.create();
        Object instance = unsafeAllocator.newInstance(Object.class);
        assertNotNull(instance);
        assertInstanceOf(Object.class, instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceWithInterfaceClass() throws Exception {
        unsafeAllocator = UnsafeAllocator.create();
        unsafeAllocator.newInstance(Object.class); // initialize unsafeAllocator
        try {
            unsafeAllocator.newInstance(Comparable.class);
        } catch (InvocationTargetException e) {
            throw e.getCause(); // re-throw the target exception
        } catch (UndeclaredThrowableException e) {
            throw e.getThrowable(); // re-throw the underlying throwable
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceWithAbstractClass() throws Exception {
        unsafeAllocator = UnsafeAllocator.create();
        unsafeAllocator.newInstance(Object.class); // initialize unsafeAllocator
        try {
            unsafeAllocator.newInstance(AbstractClassTest.class);
        } catch (InvocationTargetException e) {
            throw e.getCause(); // re-throw the target exception
        } catch (UndeclaredThrowableException e) {
            throw e.getThrowable(); // re-throw the underlying throwable
        }
    }

    @Test
    public void testNewInstanceWithCustomClass() throws Exception {
        unsafeAllocator = UnsafeAllocator.create();
        CustomClass customClass = (CustomClass) unsafeAllocator.newInstance(CustomClass.class);
        assertNotNull(customClass);
        assertInstanceOf(CustomClass.class, customClass);
        customClass.printMessage();
    }

    private static class AbstractClassTest {
        public abstract void printMessage();
    }

    private static class CustomClass {
        public CustomClass() {
            printMessage();
        }

        public void printMessage() {
            System.out.println("CustomClass initialized.");
        }
    }
}