import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }

  @Test
  public void testNewInstance() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Class<?> testClass = TestClass.class;
    TestClass instance = allocator.newInstance(testClass);
    assertNotNull(instance);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceUnsupported() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Class<?> unsupportedClass = UnsupportedClass.class;
    allocator.newInstance(unsupportedClass);
  }

  public static class TestClass {
  }

  public interface UnsupportedClass {
  }
}