
import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertNotNull(unsafeAllocator); // Test that create() method returns a non-null instance of UnsafeAllocator
    // Add more test cases to check the behavior of create() method
  }

  @Test
  public void testNewInstance() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertNotNull(unsafeAllocator); // Test that create() method returns a non-null instance of UnsafeAllocator
    Class<T> testClass = T.class; // Replace T with a class for testing
    T instance = unsafeAllocator.newInstance(testClass);
    assertNotNull(instance); // Test that newInstance() method returns a non-null instance
    assertTrue(instance instanceof testClass); // Test that newInstance() method returns an instance of the specified class
    // Add more test cases to check the behavior of newInstance() method
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceUnsupported() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Class<T> unsupportedClass = T.class; // Replace T with a class that is not supported
    unsafeAllocator.newInstance(unsupportedClass);
  }

  // Add more test methods to cover all other methods in the UnsafeAllocator class

}
