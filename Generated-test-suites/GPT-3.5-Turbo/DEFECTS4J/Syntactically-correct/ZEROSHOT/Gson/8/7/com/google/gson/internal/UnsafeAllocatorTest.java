import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        
        Class<?> c = SomeClass.class;
        SomeClass instance = allocator.newInstance(c);
        
        assertNotNull(instance);
        assertEquals(c, instance.getClass());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_Unsupported() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        
        Class<?> c = UnsupportedClass.class;
        allocator.newInstance(c);
    }
    
    private static class SomeClass {
        public SomeClass() {}
    }
    
    private static abstract class UnsupportedClass {
        public UnsupportedClass() {
            throw new UnsupportedOperationException();
        }
    }
}