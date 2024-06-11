import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private static final UnsafeAllocator UNSAFE_ALLOCATOR = UnsafeAllocator.create();

  @Test(expected = UnsupportedOperationException.class)
  public void testCannotAllocateInterface() {
    try {
      UNSAFE_ALLOCATOR.newInstance(Object.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCannotAllocateAbstractClass() {
    try {
      UNSAFE_ALLOCATOR.newInstance(AbstractClass.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testCanAllocateSimpleClass() {
    try {
      SimpleClass simpleClass = UNSAFE_ALLOCATOR.newInstance(SimpleClass.class);
      // Add assertions here if needed
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testCanAllocateArray() {
    try {
      int[] intArray = (int[]) UNSAFE_ALLOCATOR.newInstance(int[].class);
      // Add assertions here if needed
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullClass() {
    try {
      UNSAFE_ALLOCATOR.newInstance(null);
    } catch (Exception e) {
      if (e instanceof IllegalArgumentException) {
        throw (IllegalArgumentException) e;
      } else if (e instanceof InvocationTargetException) {
        Throwable cause = ((InvocationTargetException) e).getCause();
        if (cause instanceof IllegalArgumentException) {
          throw (IllegalArgumentException) cause;
        } else if (cause instanceof UndeclaredThrowableException) {
          Throwable cause2 = ((UndeclaredThrowableException) cause).getThrowable();
          if (cause2 instanceof IllegalArgumentException) {
            throw (IllegalArgumentException) cause2;
          }
        }
      }
      throw new RuntimeException(e);
    }
  }

  private static class AbstractClass {
  }

  private static class SimpleClass {
  }
}