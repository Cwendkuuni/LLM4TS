import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

  @Test
  public void testCreateUnsafeAllocator() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }

  @Test
  public void testNewInstanceJVM() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);

    String testString = "Test";
    String result = allocator.newInstance(String.class);
    assertNotEquals(testString, result);
  }

  @Test
  public void testNewInstanceDalvik() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);

    Integer testInt = 123;
    Integer result = allocator.newInstance(Integer.class);
    assertNotEquals(testInt, result);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceUnsupported() {
    UnsafeAllocator allocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) {
        throw new UnsupportedOperationException("Unsupported operation exception");
      }
    };

    allocator.newInstance(Object.class);
  }

  @Test
  public void testNewInstanceException() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);

    try {
      allocator.newInstance(null);
      fail("Exception not thrown");
    } catch (Exception e) {
      assertEquals("Cannot allocate null", e.getMessage());
    }
  }

  @Test
  public void testCheckInterfaceOrAbstract() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);

    try {
      allocator.newInstance(AbstractClass.class);
      fail("Exception not thrown");
    } catch (UnsupportedOperationException e) {
      assertEquals("Cannot instantiate abstract class", e.getMessage());
    }
  }

  private abstract class AbstractClass {
    public void method() {}
  }
}