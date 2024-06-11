import org.junit.*;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }

  @Test
  public void testNewInstance_Success() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Class<?> c = String.class;
    String instance = allocator.newInstance(c);
    assertNotNull(instance);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_UnsupportedOperationException() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Class<?> c = MyClass.class;
    allocator.newInstance(c);
  }

  private static class MyClass {}

}