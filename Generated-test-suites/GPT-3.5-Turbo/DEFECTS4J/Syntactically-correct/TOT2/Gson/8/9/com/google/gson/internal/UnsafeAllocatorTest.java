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
    Object newObj = allocator.newInstance(String.class);
    assertNotNull(newObj);
    assertEquals("java.lang.String", newObj.getClass().getName());
  }

  @Test
  public void testNewInstanceException() {
    UnsafeAllocator allocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) throws Exception {
        throw new UnsupportedOperationException("Test Exception");
      }
    };
    try {
      allocator.newInstance(String.class);
      fail("Expected exception not thrown");
    } catch (Exception e) {
      assertEquals("Test Exception", e.getMessage());
    }
  }

  @Test
  public void testUnsupportedOperationException() {
    UnsafeAllocator allocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) {
        throw new UnsupportedOperationException("Unsupported operation");
      }
    };
    try {
      allocator.newInstance(String.class);
      fail("Expected exception not thrown");
    } catch (UnsupportedOperationException e) {
      assertEquals("Unsupported operation", e.getMessage());
    }
  }

}