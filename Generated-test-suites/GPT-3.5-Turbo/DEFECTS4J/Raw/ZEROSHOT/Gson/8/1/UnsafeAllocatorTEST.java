import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertNotNull(unsafeAllocator);
  }

  @Test
  public void testNewInstance_success() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

    try {
      MyClass obj = unsafeAllocator.newInstance(MyClass.class);
      assertNotNull(obj);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_unsupportedClass() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

    unsafeAllocator.newInstance(AbstractClass.class);
  }

  class MyClass {
  }

  abstract class AbstractClass {
  }
}