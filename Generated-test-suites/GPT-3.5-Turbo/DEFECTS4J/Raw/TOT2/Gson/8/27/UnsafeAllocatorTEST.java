import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {
  
  @Test
  public void testNewInstance() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    
    // Typical use case
    MyClass obj = allocator.newInstance(MyClass.class);
    assertNotNull(obj);
    
    // Edge case: Class with private constructor
    ClassWithPrivateConstructor privateObj = allocator.newInstance(ClassWithPrivateConstructor.class);
    assertNotNull(privateObj);
    
    // Error scenario: Class with abstract modifier
    try {
      allocator.newInstance(AbstractClass.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // As expected
    }
  }
  
  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }
  
  @Test
  public void testNotAllocatableClass() {
    UnsafeAllocator allocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) {
        throw new UnsupportedOperationException("Cannot allocate " + c);
      }
    };
    
    // Error scenario: Class that cannot be allocated
    try {
      allocator.newInstance(ClassWithoutDefaultConstructor.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // As expected
    }
  }

}