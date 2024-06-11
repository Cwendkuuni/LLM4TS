import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.experimental.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class UnsafeAllocatorTest {

  @Nullable
  private static UnsafeAllocator unsafeAllocator;

  @SuppressWarnings("unchecked")
  @Test
  public void testNewInstance() {
    unsafeAllocator = UnsafeAllocator.create();
    for (Class<?> testClass : new Class<?>[]{String.class, Integer.class, Object.class}) {
      try {
        Object instance = unsafeAllocator.newInstance(testClass);
        assertNotNull(instance);
      } catch (Exception e) {
        if (e instanceof UnsupportedOperationException) {
          continue;
        }
        throw new AssertionError("Unexpected exception: " + e.getMessage());
      }
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithInterface() {
    unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(java.io.Serializable.class);
      fail("Expected UnsupportedOperationException");
    } catch (Exception e) {
      if (!(e instanceof UnsupportedOperationException)) {
        throw new AssertionError("Unexpected exception: " + e.getMessage());
      }
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithAbstractClass() {
    unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(AbstractClass.class);
      fail("Expected UnsupportedOperationException");
    } catch (Exception e) {
      if (!(e instanceof UnsupportedOperationException)) {
        throw new AssertionError("Unexpected exception: " + e.getMessage());
      }
    }
  }

  @Test(expected = InstantiationException.class)
  public void testNewInstanceWithException() throws Exception {
    @SuppressWarnings("unused")
    static class ExceptionClass {
      public ExceptionClass() throws Exception {
        throw new InstantiationException("Cannot instantiate this class");
      }
    }
    unsafeAllocator = UnsafeAllocator.create();
    unsafeAllocator.newInstance(ExceptionClass.class);
  }

  @Test(expected = InvocationTargetException.class)
  public void testNewInstanceWithInvocationTargetException() throws Exception {
    @SuppressWarnings("unused")
    static class InvocationTargetExceptionClass {
      public InvocationTargetExceptionClass() throws Exception {
        throw new InvocationTargetException(new Exception("Cannot instantiate this class"));
      }
    }
    unsafeAllocator = UnsafeAllocator.create();
    unsafeAllocator.newInstance(InvocationTargetExceptionClass.class);
  }

  @Test(expected = UndeclaredThrowableException.class)
  public void testNewInstanceWithUndeclaredThrowableException() throws Exception {
    @SuppressWarnings("unused")
    static class UndeclaredThrowableExceptionClass {
      public UndeclaredThrowableExceptionClass() throws Exception {
        throw new UndeclaredThrowableException(new Exception("Cannot instantiate this class"));
      }
    }
    unsafeAllocator = UnsafeAllocator.create();
    unsafeAllocator.newInstance(UndeclaredThrowableExceptionClass.class);
  }

  @Test(expected = NoClassDefFoundError.class)
  public void testNewInstanceWithNoClassDefFoundError() throws Exception {
    @SuppressWarnings("unused")
    static class NoClassDefFoundErrorClass {
      public NoClassDefFoundErrorClass() {
      }
    }
    unsafeAllocator = UnsafeAllocator.create();
    unsafeAllocator.newInstance(NoClassDefFoundErrorClass.class);
  }

  static class AbstractClass {
  }
}