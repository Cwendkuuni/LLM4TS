import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {
  
  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }
  
  @Test
  public void testNewInstance() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
    Object obj = allocator.newInstance(Object.class);
    assertNotNull(obj);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceUnsupported() throws Exception {
    UnsafeAllocator allocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) {
        throw new UnsupportedOperationException("Test exception");
      }
    };
    allocator.newInstance(Object.class);
  }
}