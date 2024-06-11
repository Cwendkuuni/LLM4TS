
import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {
    
    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator.newInstance(String.class));
    }
    
    @Test
    public void testCreate() {
        assertNotNull(UnsafeAllocator.create());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperation() {
        UnsafeAllocator allocator = new UnsafeAllocator() {
            @Override
            public <T> T newInstance(Class<T> c) {
                throw new UnsupportedOperationException("Test Exception");
            }
        };
        allocator.newInstance(Object.class);
    }
    
    @Test(expected = Exception.class)
    public void testException() throws Exception {
        UnsafeAllocator allocator = new UnsafeAllocator() {
            @Override
            public <T> T newInstance(Class<T> c) {
                throw new Exception("Test Exception");
            }
        };
        allocator.newInstance(Object.class);
    }
}
