import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public <T> void testNewInstance() throws Exception {
    Class<T> stringClass = String.class;
    T instance = unsafeAllocator.newInstance(stringClass);
    assertNotNull(instance);
  }
}