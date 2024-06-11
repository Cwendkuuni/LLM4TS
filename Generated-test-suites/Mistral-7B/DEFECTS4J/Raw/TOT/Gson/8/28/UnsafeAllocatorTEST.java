import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance() throws Exception {
    // Test with valid classes
    String str = unsafeAllocator.newInstance(String.class);
    Assert.assertNotNull(str);
    Assert.assertEquals("", str);

    Object obj = unsafeAllocator.newInstance(Object.class);
    Assert.assertNotNull(obj);

    // Test with interface
    Assert.assertThrows(UnsupportedOperationException.class, () -> unsafeAllocator.newInstance(Runtime.class));

    // Test with abstract class
    Assert.assertThrows(UnsupportedOperationException.class, () -> unsafeAllocator.newInstance(AbstractClass.class));

    // Test with error scenarios
    try {
      unsafeAllocator.newInstance(null);
      Assert.fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }

    try {
      unsafeAllocator.newInstance(new Class[0].getClass());
      Assert.fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testReflection() throws Exception {
    Class<Object> objClass = Object.class;
    Constructor<Object> constructor = objClass.getDeclaredConstructor();
    constructor.setAccessible(true);

    Object instance = unsafeAllocator.newInstance(objClass);

    // Check if instances are the same
    Assert.assertEquals(instance, constructor.newInstance());
  }

  static class AbstractClass {
    // Abstract class for testing purposes
  }
}