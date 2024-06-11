import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NewInstanceTest {

  @Test
  public void testNewInstance() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    String stringInstance = unsafeAllocator.newInstance(String.class);
    assertEquals("", stringInstance);
  }
}