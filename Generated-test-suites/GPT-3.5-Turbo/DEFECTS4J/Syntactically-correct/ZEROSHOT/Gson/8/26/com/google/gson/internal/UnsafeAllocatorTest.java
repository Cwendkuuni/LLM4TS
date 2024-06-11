import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      Object obj = allocator.newInstance(String.class);
      assertTrue(obj instanceof String);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }
  
  @Test
  public void testUnsupportedAllocation() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(AbstractClass.class);
      fail("UnsupportedOperationException was not thrown");
    } catch (UnsupportedOperationException e) {
      // Expected exception, do nothing
    } catch (Exception e) {
      fail("Unexpected exception thrown: " + e.getMessage());
    }
  }
  
  private abstract class AbstractClass {
    public AbstractClass() {
    }
    
    public abstract void abstractMethod();
  }
}