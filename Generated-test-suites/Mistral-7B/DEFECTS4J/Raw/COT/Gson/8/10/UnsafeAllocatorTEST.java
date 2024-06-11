import org.junit.Test;
import org.junit.Assert;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorInstance;
import java.lang.reflect.IllegalAccessException;
import java.lang.reflect.InvocationTargetException;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance_NormalScenario() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    UnsafeAllocatorInstance unsafeAllocatorInstance = unsafeAllocator.newInstance(String.class);
    Assert.assertNotNull(unsafeAllocatorInstance);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_UnsupportedClass() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    UnsafeAllocatorInstance unsafeAllocatorInstance = unsafeAllocator.newInstance(UnsafeAllocator.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewInstance_NullClass() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    unsafeAllocator.newInstance(null);
  }

  @Test(expected = InstantiationException.class)
  public void testNewInstance_FailedToAllocateInstance() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(ObjectInputStream.class);
    } catch (Exception e) {
      throw new InstantiationException("Failed to allocate instance by UnsafeAllocator", e);
    }
  }

  @Test(expected = IllegalAccessException.class)
  public void testNewInstance_IllegalAccess() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      Field field = UnsafeAllocator.class.getDeclaredField("unsafeInstance");
      field.setAccessible(true);
      UnsafeAllocatorInstance unsafeAllocatorInstance = (UnsafeAllocatorInstance) field.get(unsafeAllocator);
      unsafeAllocatorInstance.newInstance(Object[].class);
    } catch (NoSuchFieldException e) {
      throw new IllegalAccessException("UnsafeAllocator does not have a field named 'unsafeInstance'", e);
    }
  }

  @Test(expected = InvocationTargetException.class)
  public void testNewInstance_InvocationTargetException() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      UnsafeAllocatorInstance unsafeAllocatorInstance = unsafeAllocator.newInstance(ObjectStreamClass.class);
      ObjectStreamClass objectStreamClass = (ObjectStreamClass) Class.forName("java.io.ObjectStreamClass");
      Method getConstructorId = objectStreamClass.getDeclaredMethod("getConstructorId", Class.class);
      getConstructorId.setAccessible(true);
      Method newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, int.class);
      newInstance.setAccessible(true);
      int constructorId = (Integer) getConstructorId.invoke(null, ObjectInputStream.class);
      Object inputStream = new ObjectInputStream(new ByteArrayInputStream(new byte[0]));
      Object obj = newInstance.invoke(null, ObjectInputStream.class, constructorId);
      unsafeAllocatorInstance.newInstance((Class<Object>) obj.getClass());
    } catch (Exception e) {
      throw new InvocationTargetException(e);
    }
  }
}