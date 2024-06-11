import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }

  @Test
  public void testNewInstance() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      Object obj = allocator.newInstance(Object.class);
      assertNotNull(obj);
    } catch (Exception e) {
      fail("Exception thrown during newInstance test: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstanceWithUnsupportedClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(UnsupportedClass.class);
      fail("No exception thrown for unsupported class");
    } catch (UnsupportedOperationException e) {
      // Expected exception
    } catch (Exception e) {
      fail("Incorrect exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstanceWithAbstractClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(AbstractClass.class);
      fail("No exception thrown for abstract class");
    } catch (UnsupportedOperationException e) {
      // Expected exception
    } catch (Exception e) {
      fail("Incorrect exception thrown: " + e.getMessage());
    }
  }

  private static abstract class AbstractClass {
    public abstract void abstractMethod();
  }

  private static interface UnsupportedInterface {
    void unsupportedMethod();
  }

  private static class UnsupportedClass implements UnsupportedInterface {
    public void unsupportedMethod() {
      // Do nothing
    }
  }
}