import com.google.gson.internal.UnsafeAllocator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    private UnsafeAllocator unsafeAllocator;

    @Before
    public void setUp() {
        unsafeAllocator = UnsafeAllocator.create();
    }

    @Test
    public void testNewInstance_JVM() throws Exception {
        // Arrange
        Class<A> c = A.class;
        
        // Act
        A instance = unsafeAllocator.newInstance(c);
        
        // Assert
        assertNotNull(instance);
        assertEquals(c, instance.getClass());
    }

    @Test
    public void testNewInstance_DalvikVMPostGingerbread() throws Exception {
        // Arrange
        Class<B> c = B.class;
        
        // Act
        B instance = unsafeAllocator.newInstance(c);
        
        // Assert
        assertNotNull(instance);
        assertEquals(c, instance.getClass());
    }

    @Test
    public void testNewInstance_DalvikVMPreGingerbread() throws Exception {
        // Arrange
        Class<C> c = C.class;
        
        // Act
        C instance = unsafeAllocator.newInstance(c);
        
        // Assert
        assertNotNull(instance);
        assertEquals(c, instance.getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_GiveUp() {
        // Arrange
        Class<D> c = D.class;
        
        // Act
        unsafeAllocator.newInstance(c);
        
        // Assert
        // Exception is thrown
    }

    private static class A {

    }

    private static class B {

    }

    private static class C {

    }

    private static abstract class D {

    }
}