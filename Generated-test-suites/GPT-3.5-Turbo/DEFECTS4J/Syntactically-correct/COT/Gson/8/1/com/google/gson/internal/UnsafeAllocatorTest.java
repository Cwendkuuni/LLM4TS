import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator.newInstance(String.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_Unsupported() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        allocator.newInstance(AbstractClass.class);
    }


    public static abstract class AbstractClass {

    }
}