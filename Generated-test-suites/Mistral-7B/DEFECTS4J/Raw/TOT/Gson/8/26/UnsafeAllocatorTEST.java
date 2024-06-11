import org.junit.Test;
import org.junit.Assert.*;
import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_String() {
    String expected = new String("Hello World");
    String actual;

    try {
      actual = (String) unsafeAllocator.newInstance(String.class);
      Assert.assertNotNull(actual);
      Assert.assertEquals(expected, actual);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_Integer() {
    Integer expected = new Integer(42);
    Integer actual;

    try {
      actual = (Integer) unsafeAllocator.newInstance(Integer.class);
      Assert.assertNotNull(actual);
      Assert.assertEquals(expected, actual);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_Exception() {
    try {
      unsafeAllocator.newInstance(UnsupportedOperationException.class);
      fail("Expected UnsupportedOperationException but got instance instead.");
    } catch (UnsupportedOperationException e) {
      // Expected behavior
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_Interface() {
    unsafeAllocator.newInstance(Runnable.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() {
    unsafeAllocator.newInstance(AbstractClassTest.class);
  }
}

class AbstractClassTest {
  abstract void testMethod();
}