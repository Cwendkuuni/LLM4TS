import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;

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
  public void testNewInstance_Interface() {
    try {
      allocator.newInstance(Runnable.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_AbstractClass() {
    try {
      allocator.newInstance(AbstractClass.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_ThrowingConstructor() {
    try {
      Class<?> throwingClass = Class.forName("com.google.gson.internal.ThrowingConstructor");
      allocator.newInstance(throwingClass);
      fail("Expected Exception");
    } catch (Exception e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_ThrowingConstructor_MultipleCatchBlocks() {
    try {
      Class<?> multipleCatchClass = Class.forName("com.google.gson.internal.MultipleCatchBlocks");
      allocator.newInstance(multipleCatchClass);
      fail("Expected Exception");
    } catch (Exception e) {
      if (e instanceof UndeclaredThrowableException) {
        Throwable cause = e.getCause();
        if (cause instanceof RuntimeException) {
          throw (RuntimeException) cause;
        } else if (cause instanceof Error) {
          throw (Error) cause;
        } else {
          throw new AssertionError("Unexpected cause: " + cause.getClass().getName());
        }
      } else if (e instanceof InvocationTargetException) {
        Throwable cause = e.getCause();
        if (cause instanceof RuntimeException) {
          throw (RuntimeException) cause;
        } else if (cause instanceof Error) {
          throw (Error) cause;
        } else {
          throw new AssertionError("Unexpected cause: " + cause.getClass().getName());
        }
      } else {
        throw e;
      }
    }
  }

  private static class AbstractClass {
  }

  private static class ThrowingConstructor {
    private ThrowingConstructor() {
      throw new RuntimeException("Thrown from constructor");
    }
  }

  private static class MultipleCatchBlocks {
    private MultipleCatchBlocks() {
      try {
        throw new IllegalArgumentException("Argument is invalid");
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("Exception thrown from try block", e);
      }
    }
  }
}