import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testCreate() {
    assertNotNull(unsafeAllocator);
  }

  @Test(expected = UnsupportedOperationException.class)
  public <T> void testNewInstanceForInterface() {
    unsafeAllocator.newInstance(Object.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public <T> void testNewInstanceForAbstractClass() {
    unsafeAllocator.newInstance(AbstractClass.class);
  }

  @Test
  public <T> void testNewInstanceForConcreteClass() throws Exception {
    Class<ConcreteClass> concreteClass = ConcreteClass.class;
    T instance = unsafeAllocator.newInstance(concreteClass);
    assertNotNull(instance);
  }

  @Test(expected = InstantiationException.class)
  public <T> void testNewInstanceForExceptionClass() throws Exception {
    Class<ExceptionClass> exceptionClass = ExceptionClass.class;
    unsafeAllocator.newInstance(exceptionClass);
  }

  @Test(expected = IllegalArgumentException.class)
  public <T> void testNewInstanceForNullClass() {
    unsafeAllocator.newInstance(null);
  }

  @Test(expected = IllegalAccessException.class)
  public <T> void testNewInstanceForPrivateConstructor() throws Exception {
    Class<PrivateConstructorClass> privateConstructorClass = PrivateConstructorClass.class;
    if (Modifier.isPrivate(privateConstructorClass.getDeclaredConstructor().getModifiers())) {
      unsafeAllocator.newInstance(privateConstructorClass);
    }
  }

  private static class AbstractClass {}
  private static class ConcreteClass {}
  private static class ExceptionClass extends Exception {}
  private static class PrivateConstructorClass {
    private PrivateConstructorClass() {}
  }
}