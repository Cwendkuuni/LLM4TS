import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstanceForObject() {
    Object obj = unsafeAllocator.newInstance(Object.class);
    assertNotNull(obj);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceForInterfaceOrAbstractClass() {
    unsafeAllocator.newInstance(String.class);
  }

  @Test
  public void testGetUnsupportedClassException() {
    try {
      unsafeAllocator.newInstance(Object[].class);
    } catch (Exception e) {
      if (e instanceof InvocationTargetException) {
        Throwable cause = e.getCause();
        if (cause instanceof UndeclaredThrowableException) {
          assertNull(unsafeAllocator.newInstance(Object[].class));
          assertNull(cause.getCause());
        }
      }
      throw e;
    }
  }

  @Test
  public void testGetNullPointerException() {
    try {
      Field f = UnsafeAllocator.class.getDeclaredField("unsafe");
      f.setAccessible(true);
      Object unsafe = f.get(null);
      Method allocateInstance = Unsafe.class.getMethod("allocateInstance", Class.class);
      unsafeAllocator = new UnsafeAllocator() {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T newInstance(Class<T> c) throws Exception {
          return (T) allocateInstance.invoke(unsafe, c);
        }
      };
      unsafe = null;
      unsafeAllocator.newInstance(Object.class);
    } catch (Exception e) {
      assertNotNull(e);
    }
  }
}