import static org.junit.Assert.*;
import org.junit.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertNotNull(unsafeAllocator);
  }

  @Test
  public void testNewInstanceJVM() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Class<?> c = Class.forName("com.google.gson.internal.UnsafeAllocator$1");
    Method allocateInstance = c.getDeclaredMethod("allocateInstance", Class.class);

    assertEquals(unsafeAllocator.newInstance(c), allocateInstance.invoke(unsafeAllocator, c));
  }

  @Test
  public void testNewInstanceDalvikvmPostGingerbread() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Class<?> c = Class.forName("com.google.gson.internal.UnsafeAllocator$2");
    Method newInstance = c.getDeclaredMethod("newInstance", Class.class, int.class);

    assertEquals(unsafeAllocator.newInstance(c), newInstance.invoke(unsafeAllocator, c, 0));
  }

  @Test
  public void testNewInstanceDalvikvmPreGingerbread() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Class<?> c = Class.forName("com.google.gson.internal.UnsafeAllocator$3");
    Method newInstance = c.getDeclaredMethod("newInstance", Class.class, Class.class);

    assertEquals(unsafeAllocator.newInstance(c), newInstance.invoke(unsafeAllocator, c, Object.class));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceUnsupported() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Class<?> c = Class.forName("com.google.gson.internal.UnsafeAllocator$4");

    unsafeAllocator.newInstance(c);
  }
}