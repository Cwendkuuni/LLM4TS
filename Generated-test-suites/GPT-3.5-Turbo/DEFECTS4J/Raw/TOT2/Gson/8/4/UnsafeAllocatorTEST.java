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

    Object obj = allocator.newInstance(String.class);
    assertNotNull(obj);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithUnsupportedClass() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);

    allocator.newInstance(Interface.class);
  }

  @Test
  public void testNewInstanceWithNullClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);

    try {
      allocator.newInstance(null);
      fail("Expected an exception");
    } catch (Exception e) {
      assertTrue(e instanceof UnsupportedOperationException);
    }
  }

  private interface Interface {
  }

}