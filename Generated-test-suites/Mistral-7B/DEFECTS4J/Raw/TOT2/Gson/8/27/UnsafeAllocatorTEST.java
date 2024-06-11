import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance() {
    Object obj = unsafeAllocator.newInstance(String.class);
    assertNotNull(obj);
  }

  @Test
  public void testNewInstanceInterface() {
    try {
      unsafeAllocator.newInstance(Object.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test(expected = Exception.class)
  public void testNewInstanceFailure() {
    try {
      unsafeAllocator.newInstance(UnsafeAllocator.class);
    } catch (Exception e) {
      throw e;
    }
  }
}