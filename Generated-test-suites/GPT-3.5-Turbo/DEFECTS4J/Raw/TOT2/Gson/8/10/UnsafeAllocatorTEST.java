import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance_JVM() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        MyClass obj = allocator.newInstance(MyClass.class);
        assertNotNull(obj);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_GiveUp() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        Object obj = allocator.newInstance(Object.class);
    }

    @Test
    public void testCreate_JVM() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);
    }

    @Test
    public void testCreate_NoUnsafeClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);
    }
}