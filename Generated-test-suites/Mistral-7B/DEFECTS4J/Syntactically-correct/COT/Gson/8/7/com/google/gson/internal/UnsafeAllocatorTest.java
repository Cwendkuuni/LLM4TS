import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceThrowsExceptionForInterfaceOrAbstractClass() {
    Class<?> interfaceClass = java.io.Serializable.class;
    try {
      unsafeAllocator.newInstance(interfaceClass);
      assert false; // this line should never be executed
    } catch (Exception e) {
      if (e instanceof UnsupportedOperationException) {
        return;
      }
      throw e;
    }
  }

  @Test
  public <T> void testNewInstanceBasicFunctionality() {
    Class<T> stringClass = String.class;
    try {
      T instance = unsafeAllocator.newInstance(stringClass);
      assert instance != null;
      assert instance instanceof String;
    } catch (Exception e) {
      throw new RuntimeException("Failed to create instance of String using UnsafeAllocator", e);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewInstanceThrowsExceptionForNullClass() {
    try {
      unsafeAllocator.newInstance(null);
    } catch (Exception e) {
      if (e instanceof IllegalArgumentException) {
        return;
      }
      throw e;
    }
  }

  @Test(expected = RuntimeException.class)
  public void testNewInstanceThrowsRuntimeExceptionForException() {
    Class<Throwable> throwableClass = Throwable.class;
    try {
      unsafeAllocator.newInstance(throwableClass);
      assert false; // this line should never be executed
    } catch (Exception e) {
      if (e instanceof RuntimeException) {
        return;
      }
      throw new RuntimeException("Failed to create instance of Throwable using UnsafeAllocator", e);
    }
  }

  @Test(expected = LinkageError.class)
  public void testNewInstanceThrowsLinkageErrorForUnsupportedClass() {
    Class<?> unsupportedClass = UnsupportedOperationException.class;
    try {
      unsafeAllocator.newInstance(unsupportedClass);
    } catch (Exception e) {
      if (e instanceof LinkageError) {
        return;
      }
      throw e;
    }
  }

  @Test
  public void testNewInstanceReturnsExpectedObjectType() {
    Class<?> booleanWrapperClass = Boolean.class;
    try {
      Object instance = unsafeAllocator.newInstance(booleanWrapperClass);
      assert instance instanceof Boolean;
    } catch (Exception e) {
      throw new RuntimeException("Failed to create instance of Boolean using UnsafeAllocator", e);
    }
  }

  @Test
  public void testNewInstanceCanAllocateArrays() {
    Class<?> intArrayClass = int[].class;
    try {
      Object instance = unsafeAllocator.newInstance(intArrayClass);
      assert instance != null;
      assert instance.getClass().isArray();
      assert instance.getClass().getComponentType() == int.class;
    } catch (Exception e) {
      throw new RuntimeException("Failed to create instance of int[] using UnsafeAllocator", e);
    }
  }

  @Test(expected = NegativeArraySizeException.class)
  public void testNewInstanceThrowsNegativeArraySizeExceptionForNegativeLength() {
    Class<?> intArrayClass = int[].class;
    int length = -1;
    try {
      unsafeAllocator.newInstance(intArrayClass, length);
    } catch (IllegalArgumentException e) {
      if (e.getCause() instanceof NegativeArraySizeException) {
        return;
      }
      throw new RuntimeException("Unexpected exception when creating array of length -1", e);
    }
  }

  @Test
  public <T> void testNewInstanceCanAllocateNestedClasses() {
    Class<?> nestedClass = NestedClass.class;
    try {
      Object instance = unsafeAllocator.newInstance(nestedClass);
      assert instance != null;
      assert instance instanceof NestedClass;
    } catch (Exception e) {
      throw new RuntimeException("Failed to create instance of NestedClass using UnsafeAllocator", e);
    }
  }

  private static class NestedClass {
    // empty class
  }
}