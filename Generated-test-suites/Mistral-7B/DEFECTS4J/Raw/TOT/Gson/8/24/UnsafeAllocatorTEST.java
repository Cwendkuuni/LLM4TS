import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

@RunWith(Parameterized.class)
@Category(IntegrationTest.class)
public class UnsafeAllocatorTest {

  private final Class<?> testClass;

  public UnsafeAllocatorTest(Class<?> testClass) {
    this.testClass = testClass;
  }

  @Parameters(name = "{index}: testing UnsafeAllocator for class {0}")
  public static Iterable<Class<?>> data() {
    return TestUtil.getTestClasses();
  }

  @Test
  public void testNewInstance() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Object instance;

    if (Modifier.isAbstract(testClass.getModifiers()) || Modifier.isInterface(testClass.getModifiers())) {
      throw new java.lang.UnsupportedOperationException("Cannot allocate " + testClass);
    }

    try {
      instance = allocator.newInstance(testClass);
      TestUtil.assertNotNull(instance);
    } catch (Exception e) {
      TestUtil.assertFalse(e.getMessage().contains("Cannot allocate"));
      throw e;
    }

    TestUtil.runMethodOnObject(instance, "someMethod"); // replace with actual method name
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceThrowsExceptionForAbstractOrInterfaceClasses() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(Object.class);
    } catch (Exception e) {
      TestUtil.assertEquals("Cannot allocate " + Object.class, e.getMessage());
      throw e;
    }
  }

  @Test(expected = java.lang.reflect.InvocationTargetException.class)
  public void testNewInstanceThrowsReflectionException() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Class<?> errorClass = Class.forName("java.lang.NoSuchMethodException");
    try {
      allocator.newInstance(errorClass);
    } catch (Exception e) {
      TestUtil.assertInstanceOf(e.getCause(), errorClass);
      throw e;
    }
  }
}