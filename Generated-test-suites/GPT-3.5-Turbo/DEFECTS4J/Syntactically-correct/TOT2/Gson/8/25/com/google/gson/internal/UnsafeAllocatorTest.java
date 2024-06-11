import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator;

  @Before
  public void setup() {
      unsafeAllocator = UnsafeAllocator.create();
  }

  @Test
  public void testCreateUnsafeAllocator() {
      assertNotNull(unsafeAllocator);
  }

  @Test
  public void testNewInstance() throws Exception {
      Object object = unsafeAllocator.newInstance(Object.class);
      assertNotNull(object);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUnsupportedOperationException() {
      unsafeAllocator.newInstance(UnsupportedClass.class);
  }

  @Test
  public void testDetermineObjectStreamClass() throws Exception {
      ObjectStreamClass objectStreamClass = new ObjectStreamClass(Object.class);
      int constructorId = objectStreamClass.getConstructorId(Object.class);

      Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, int.class);
      newInstanceMethod.setAccessible(true);

      Object newInstanceObject = newInstanceMethod.invoke(null, Object.class, constructorId);
      assertNotNull(newInstanceObject);
  }

  @Test
  public void testObjectInputStream() throws Exception {
      ObjectInputStream objectInputStream = new ObjectInputStream();
      Method newInstanceMethod = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
      newInstanceMethod.setAccessible(true);

      Object newInstanceObject = newInstanceMethod.invoke(null, Object.class, Object.class);
      assertNotNull(newInstanceObject);
  }

  static class UnsupportedClass {
  }

}