
import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance_createsInstance() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      SomeClass object = allocator.newInstance(SomeClass.class);
      assertNotNull(object);
      assertTrue(object instanceof SomeClass);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_throwsUnsupportedOperationException() {
    UnsafeAllocator allocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) {
        throw new UnsupportedOperationException("Cannot allocate " + c);
      }
    };
    allocator.newInstance(SomeClass.class);
  }
}
