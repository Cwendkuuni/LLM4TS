import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {
  
  @Test
  public void testNewInstance_JVM() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
    try {
      Object obj = allocator.newInstance(String.class);
      assertNotNull(obj);
      assertTrue(obj instanceof String);
    } catch (Exception e) {
      fail("Caught unexpected exception: " + e.getMessage());
    }
  }
  
  @Test
  public void testNewInstance_DalvikVM_PostGingerbread() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
    try {
      Object obj = allocator.newInstance(Integer.class);
      assertNotNull(obj);
      assertTrue(obj instanceof Integer);
    } catch (Exception e) {
      fail("Caught unexpected exception: " + e.getMessage());
    }
  }
  
  @Test
  public void testNewInstance_DalvikVM_PreGingerbread() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
    try {
      Object obj = allocator.newInstance(Double.class);
      assertNotNull(obj);
      assertTrue(obj instanceof Double);
    } catch (Exception e) {
      fail("Caught unexpected exception: " + e.getMessage());
    }
  }
  
}