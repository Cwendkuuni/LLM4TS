import com.google.gson.internal.UnsafeAllocator;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {
    
    @Test
    public void testNewInstance_JVM() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        MyClass myObj = allocator.newInstance(MyClass.class);
        assertNotNull(myObj);
    }
    
    @Test
    public void testNewInstance_DalvikVM_PostGingerbread() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        OtherClass obj = allocator.newInstance(OtherClass.class);
        assertNotNull(obj);
    }
    
    @Test
    public void testNewInstance_DalvikVM_PreGingerbread() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        AnotherClass anotherObj = allocator.newInstance(AnotherClass.class);
        assertNotNull(anotherObj);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_Unsupported() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        allocator.newInstance(SomeClass.class);
    }
    
}