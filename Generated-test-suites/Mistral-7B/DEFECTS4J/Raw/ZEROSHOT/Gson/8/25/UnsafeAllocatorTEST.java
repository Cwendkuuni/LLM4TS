import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocatorImpl unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstanceNullClass() {
    assertThrows(NullPointerException.class, () -> unsafeAllocator.newInstance(null));
  }

  @Test
  public void testNewInstanceBasicTypes() {
    assertNotNull(unsafeAllocator.newInstance(Integer.TYPE));
    assertNotNull(unsafeAllocator.newInstance(Boolean.TYPE));
    assertNotNull(unsafeAllocator.newInstance(Character.TYPE));
    assertNotNull(unsafeAllocator.newInstance(Byte.TYPE));
    assertNotNull(unsafeAllocator.newInstance(Short.TYPE));
    assertNotNull(unsafeAllocator.newInstance(Double.TYPE));
    assertNotNull(unsafeAllocator.newInstance(Long.TYPE));
    assertNotNull(unsafeAllocator.newInstance(Float.TYPE));
    assertNotNull(unsafeAllocator.newInstance(Void.TYPE));
  }

  @Test
  public void testNewInstancePrimitiveWrapper() {
    assertNotNull(unsafeAllocator.newInstance(Integer.class));
    assertNotNull(unsafeAllocator.newInstance(Boolean.class));
    assertNotNull(unsafeAllocator.newInstance(Character.class));
    assertNotNull(unsafeAllocator.newInstance(Byte.class));
    assertNotNull(unsafeAllocator.newInstance(Short.class));
    assertNotNull(unsafeAllocator.newInstance(Double.class));
    assertNotNull(unsafeAllocator.newInstance(Long.class));
    assertNotNull(unsafeAllocator.newInstance(Float.class));
  }

  @Test
  public void testNewInstanceArray() {
    assertNotNull(unsafeAllocator.newInstance(int[].class));
    assertNotNull(unsafeAllocator.newInstance(boolean[].class));
    assertNotNull(unsafeAllocator.newInstance(char[].class));
    assertNotNull(unsafeAllocator.newInstance(byte[].class));
    assertNotNull(unsafeAllocator.newInstance(short[].class));
    assertNotNull(unsafeAllocator.newInstance(double[].class));
    assertNotNull(unsafeAllocator.newInstance(long[].class));
    assertNotNull(unsafeAllocator.newInstance(float[].class));
  }

  @Test
  public void testNewInstanceClassWithConstructor() throws Exception {
    Class<?> testClass = Class.forName("java.lang.String");
    String instance = (String) unsafeAllocator.newInstance(testClass);
    assertNotNull(instance);
    assertFalse(instance.isEmpty());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceInterface() {
    unsafeAllocator.newInstance(Runnable.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceAbstractClass() {
    unsafeAllocator.newInstance(AbstractClassTest.class);
  }

  @Test
  public void testNewInstanceException() {
    try {
      Class<?> testClass = Class.forName("java.io.IOException");
      unsafeAllocator.newInstance(testClass);
    } catch (Exception e) {
      if (e instanceof InvocationTargetException) {
        Throwable cause = e.getCause();
        if (cause instanceof UndeclaredThrowableException) {
          Throwable rootCause = cause.getCause();
          assertThrows(rootCause.getClass(), () -> unsafeAllocator.newInstance(testClass));
        }
      }
      throw e;
    }
  }

  static class AbstractClassTest {
    abstract void test();
  }
}