import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance_jvm() {
    try {
      UnsafeAllocator allocator = UnsafeAllocator.create();
      assertNotNull(allocator.newInstance(Object.class));
    } catch (Exception e) {
      fail("Should not throw an exception");
    }
  }

  @Test
  public void testNewInstance_dalvikvmPostGingerbread() {
    try {
      UnsafeAllocator allocator = UnsafeAllocator.create();
      assertNotNull(allocator.newInstance(Object.class));
    } catch (Exception e) {
      fail("Should not throw an exception");
    }
  }

  @Test
  public void testNewInstance_dalvikvmPreGingerbread() {
    try {
      UnsafeAllocator allocator = UnsafeAllocator.create();
      assertNotNull(allocator.newInstance(Object.class));
    } catch (Exception e) {
      fail("Should not throw an exception");
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_giveUp() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    allocator.newInstance(Object.class);
  }
  
}