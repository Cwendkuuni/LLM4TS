import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_NullClass() {
    try {
      allocator.newInstance(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_PrimitiveTypes() {
    try {
      allocator.newInstance(int[].class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_InterfaceType() {
    try {
      allocator.newInstance(String.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_AbstractClassType() {
    try {
      allocator.newInstance(AbstractClass.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_NormalClass() throws Exception {
    ConcreteClass concrete = allocator.newInstance(ConcreteClass.class);
    // Add assertions here
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_FailedToAllocate() throws Exception {
    allocator.newInstance(FailedToAllocate.class);
  }

  @Test(expected = InvocationTargetException.class)
  public void testNewInstance_ThrowsException() throws Exception {
    try {
      allocator.newInstance(ThrowsException.class);
    } catch (InvocationTargetException e) {
      // Expected
      throw e.getCause();
    }
  }

  @Test(expected = UndeclaredThrowableException.class)
  public void testNewInstance_ThrowsHiddenException() throws Exception {
    try {
      allocator.newInstance(ThrowsHiddenException.class);
    } catch (UndeclaredThrowableException e) {
      // Expected
      throw e.getException();
    }
  }

  // Add more test cases as needed

  static class AbstractClass {}
  static class ConcreteClass {}
  static class FailedToAllocate {}
  static class ThrowsException extends Exception {}
  static class ThrowsHiddenException extends Exception {
    private static final long serialVersionUID = 1L;

    ThrowsHiddenException() {
      throw new RuntimeException("Hidden exception");
    }
  }
}