import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorInterface;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance_Object() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Object instance = unsafeAllocator.newInstance(Object.class);
    assertNotNull(instance);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_Interface() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertInstanceOf(UnsafeAllocatorInterface.class, unsafeAllocator);
    try {
      unsafeAllocator.newInstance(Object.class);
    } catch (UnsupportedOperationException e) {
      throw e;
    } finally {
      // interface cannot be instantiated
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertInstanceOf(UnsafeAllocatorInterface.class, unsafeAllocator);
    try {
      unsafeAllocator.newInstance(AbstractClass.class);
    } catch (UnsupportedOperationException e) {
      throw e;
    } finally {
      // abstract class cannot be instantiated
    }
  }

  @Test
  public void testNewInstance_ClassWithConstructor() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Class<TestClassWithConstructor> testClassWithConstructor = TestClassWithConstructor.class;
    TestClassWithConstructor instance = (TestClassWithConstructor) unsafeAllocator.newInstance(testClassWithConstructor);
    assertNotNull(instance);
  }

  @Test(expected = Exception.class)
  public void testNewInstance_ClassWithoutNoArgsConstructor() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Class<TestClassWithoutConstructor> testClassWithoutConstructor = TestClassWithoutConstructor.class;
    unsafeAllocator.newInstance(testClassWithoutConstructor);
  }

  @Test
  public void testNewInstance_ClassWithPrivateConstructor() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Class<TestClassWithPrivateConstructor> testClassWithPrivateConstructor = TestClassWithPrivateConstructor.class;
    TestClassWithPrivateConstructor instance = (TestClassWithPrivateConstructor) unsafeAllocator.newInstance(testClassWithPrivateConstructor);
    assertNotNull(instance);
  }

  private static class TestClassWithConstructor {
    public TestClassWithConstructor() {
    }
  }

  private static class TestClassWithoutConstructor {
    private TestClassWithoutConstructor() {
    }
  }

  private static class TestClassWithPrivateConstructor {
    private TestClassWithPrivateConstructor() {
    }

    private static class InnerClass {
      public InnerClass() {
      }
    }
  }

  private static abstract class AbstractClass {
  }
}