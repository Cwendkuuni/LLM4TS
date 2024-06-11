import com.google.gson.internal.UnsafeAllocator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator;

  @Before
  public void setUp() {
    unsafeAllocator = UnsafeAllocator.create();
  }

  @Test
  public void testNewInstance_Success() throws Exception {
    // Arrange
    Class<?> testClass = Class.forName("com.example.TestClass");

    // Act
    Object instance = unsafeAllocator.newInstance(testClass);

    // Assert
    assertNotNull(instance);
    assertTrue(testClass.isInstance(instance));
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_UnsupportedOperationExceptionThrown() {
    // Arrange
    Class<?> unsupportedClass = Class.forName("com.example.UnsupportedClass");
    
    // Act
    unsafeAllocator.newInstance(unsupportedClass);

    // Assert - Exception expected
  }
  
  // Add more test methods to cover all scenarios and edge cases.
  
}