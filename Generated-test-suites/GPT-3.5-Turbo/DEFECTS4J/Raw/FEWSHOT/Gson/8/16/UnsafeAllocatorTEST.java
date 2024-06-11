import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testCreate() {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        assertNotNull(unsafeAllocator);
    }

    @Test
    public void testCreateWithNull() {
        try {
            UnsafeAllocator unsafeAllocator = UnsafeAllocator.create(null);
            fail("Should have thrown exception");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testCreateWithValidClass() {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create(Object.class);
        assertNotNull(unsafeAllocator);
    }

    @Test
    public void testCreateWithInterface() {
        try {
            UnsafeAllocator unsafeAllocator = UnsafeAllocator.create(Runnable.class);
            fail("Should have thrown exception");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testCreateWithAbstractClass() {
        try {
            UnsafeAllocator unsafeAllocator = UnsafeAllocator.create(AbstractClass.class);
            fail("Should have thrown exception");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testCreateWithPrivateConstructor() {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create(ClassWithPrivateConstructor.class);
        assertNotNull(unsafeAllocator);
    }

}

interface AbstractClass {}

class ClassWithPrivateConstructor {
    private ClassWithPrivateConstructor() {}
}