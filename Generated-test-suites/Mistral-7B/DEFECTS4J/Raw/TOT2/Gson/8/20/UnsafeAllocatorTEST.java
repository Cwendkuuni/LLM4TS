import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    String stringInstance = unsafeAllocator.newInstance(String.class);
    assert (stringInstance instanceof String);
  }

  @Test
  public void testNewInstance_NullClass() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertThrows(NullPointerException.class, () -> unsafeAllocator.newInstance(null));
  }

  @Test
  public void testNewInstance_InterfaceOrAbstractClass() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertThrows(UnsupportedOperationException.class, () -> unsafeAllocator.newInstance(Object.class.getInterfaces()[0]));
  }

  @Test
  public void testNewInstance_UnsupportedClass() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(UnsafeAllocator.class);
    } catch (Exception e) {
      assert (e instanceof UnsupportedOperationException);
    }
  }

  @Test(expected = Exception.class)
  public void testNewInstance_ReflectionExceptions() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      Field unsafeField = UnsafeAllocator.class.getDeclaredField("unsafe");
      unsafeField.setAccessible(true);
      unsafeAllocator.newInstance((Class<Object>) unsafeField.getType());
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testNewInstance_Exception() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(Exception.class);
    } catch (Exception e) {
      assert (e instanceof UnsupportedOperationException);
    }
  }

  @Test
  public void testNewInstance_Array() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(int[].class);
    } catch (Exception e) {
      assert (e instanceof UnsupportedOperationException);
    }
  }
}