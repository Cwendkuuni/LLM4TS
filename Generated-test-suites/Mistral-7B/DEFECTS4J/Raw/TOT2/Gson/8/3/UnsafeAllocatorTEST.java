import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    String expected = "Test String";
    String actual = unsafeAllocator.newInstance(String.class);
    assertEquals(expected, actual);
  }
}