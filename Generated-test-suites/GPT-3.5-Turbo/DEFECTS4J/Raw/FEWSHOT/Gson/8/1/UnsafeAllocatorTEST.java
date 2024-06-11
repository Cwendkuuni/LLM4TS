import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;


import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

public class UnsafeAllocatorTest {
    
    @Test
    public void testNewInstance_success() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        assertNotNull(unsafeAllocator.newInstance(Foo.class));
    }
    
    @Test
    public void testNewInstance_unsupportedOperationException() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> unsafeAllocator.newInstance(Bar.class));
    }

    interface Foo {
    }

    abstract class Bar {
    }
}