import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UnsafeAllocatorTest {
  
  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }
  
  @Test
  public void testNewInstanceSuccess() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      Object object = allocator.newInstance(Object.class);
      assertNotNull(object);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceUnsupported() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    allocator.newInstance(String.class);
  }
}