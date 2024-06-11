
import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {
    @Test
    public void testCreate() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);
    }
    
    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        ClassWithPublicConstructor obj1 = allocator.newInstance(ClassWithPublicConstructor.class);
        assertNotNull(obj1);
        
        ClassWithPrivateConstructor obj2 = allocator.newInstance(ClassWithPrivateConstructor.class);
        assertNotNull(obj2);
        
        ClassWithoutConstructor obj3 = allocator.newInstance(ClassWithoutConstructor.class);
        assertNotNull(obj3);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedClass() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        allocator.newInstance(UnsupportedClass.class);
    }
    
    public static class ClassWithPublicConstructor {
        public ClassWithPublicConstructor() {}
    }
    
    public static class ClassWithPrivateConstructor {
        private ClassWithPrivateConstructor() {}
    }
    
    public static class ClassWithoutConstructor {
        private ClassWithoutConstructor() {}
    }
    
    public interface UnsupportedClass {}
}