import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_ValidClass() {
    // Test with Object class as an example
    Object instance = allocator.newInstance(Object.class);
    assert (instance != null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_InterfaceOrAbstractClass() {
    Class<?> interfaceClass = Runnable.class;
    allocator.newInstance(interfaceClass);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_ArrayClass() {
    Class<?> arrayClass = int[].class;
    allocator.newInstance(arrayClass);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_PrimitiveClass() {
    Class<?> primitiveClass = int.class;
    allocator.newInstance(primitiveClass);
  }

  @Test
  public void testNewInstance_ThrowException() {
    try {
      Class<?> throwableClass = RuntimeException.class;
      allocator.newInstance(throwableClass);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    } catch (Exception e) {
      throw new AssertionError("Unexpected exception", e);
    }
  }

  @Test(expected = InstantiationException.class)
  public void testNewInstance_ClassWithNoDefaultConstructor() throws Exception {
    Class<?> innerClass = InnerClass.class;
    allocator.newInstance(innerClass);
  }

  @Test
  public void testNewInstance_ClassWithPrivateConstructor() throws Exception {
    Class<?> privateConstructorClass = PrivateConstructorClass.class;
    Object instance = allocator.newInstance(privateConstructorClass);
    assert (instance != null);
  }

  @Test(expected = IllegalAccessException.class)
  public void testNewInstance_ReflectiveAccessToPrivateConstructor() throws Exception {
    Class<?> reflectiveAccessToPrivateConstructorClass = ReflectiveAccessToPrivateConstructorClass.class;
    Method constructor = reflectiveAccessToPrivateConstructorClass.getDeclaredConstructor();
    constructor.setAccessible(true);
    constructor.invoke(null);
  }

  @Test
  public void testNewInstance_ClassWithPublicConstructor() throws Exception {
    Class<?> publicConstructorClass = PublicConstructorClass.class;
    Object instance = allocator.newInstance(publicConstructorClass);
    assert (instance != null);
  }

  static class InnerClass {
    private InnerClass() {
    }
  }

  static class PrivateConstructorClass {
    private PrivateConstructorClass() {
    }

    public static void main(String[] args) {
      new PrivateConstructorClass();
    }
  }

  static class ReflectiveAccessToPrivateConstructorClass {
    private ReflectiveAccessToPrivateConstructorClass() {
    }

    private ReflectiveAccessToPrivateConstructorClass(int i) {
    }
  }

  static class PublicConstructorClass {
    public PublicConstructorClass() {
    }

    public static void main(String[] args) {
      new PublicConstructorClass();
    }
  }
}