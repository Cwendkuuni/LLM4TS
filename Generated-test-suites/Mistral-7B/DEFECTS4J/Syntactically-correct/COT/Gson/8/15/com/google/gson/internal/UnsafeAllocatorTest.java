import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_ValidClass() throws Exception {
    // Test with Object as an example
    Object instance = allocator.newInstance(Object.class);
    assertNotNull(instance);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_InterfaceClass() throws Exception {
    // Test with an interface
    allocator.newInstance(Runnable.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() throws Exception {
    // Test with an abstract class
    allocator.newInstance(AbstractClassTest.class);
  }

  @Test
  public void testNewInstance_ThrowException() throws Exception {
    try {
      allocator.newInstance(Exception.class);
    } catch (InvocationTargetException e) {
      throw e.getTargetException();
    } catch (UndeclaredThrowableException e) {
      throw e.getThrowable();
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_NullClass() throws Exception {
    allocator.newInstance(null);
  }

  @Test
  public void testNewInstance_PrimitiveTypes() throws Exception {
    assertEquals(true, allocator.newInstance(Boolean.TYPE));
    assertEquals(42, allocator.newInstance(Integer.TYPE).intValue());
  }

  // Add more test cases as needed for other edge cases or custom exceptions

}

class AbstractClassTest {
  // An abstract class for testing
}