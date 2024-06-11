import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorImpl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_Object() throws Exception {
    Object obj = unsafeAllocator.newInstance(Object.class);
    assertNotNull(obj);
  }

  @Test
  public void testNewInstance_CustomClass() throws Exception {
    class TestClass {}
    TestClass testClass = (TestClass) unsafeAllocator.newInstance(TestClass.class);
    assertInstanceOf(TestClass.class, testClass);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_Interface() throws Exception {
    interface TestInterface {}
    TestInterface testInterface = (TestInterface) unsafeAllocator.newInstance(TestInterface.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() throws Exception {
    abstract class TestAbstractClass {}
    TestAbstractClass testAbstractClass = (TestAbstractClass) unsafeAllocator.newInstance(TestAbstractClass.class);
  }

  @Test
  public void testNewInstance_Throwable() throws Exception {
    try {
      unsafeAllocator.newInstance(Throwable.class);
    } catch (Exception e) {
      // expected
    }
  }

  @Test
  public void testNewInstance_Null() throws Exception {
    try {
      unsafeAllocator.newInstance(null);
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
}

class UnsafeAllocatorImpl extends UnsafeAllocator {
  @SuppressWarnings("unchecked")
  @Override
  public <T> T newInstance(Class<T> c) throws Exception {
    return (T) super.newInstance(c);
  }
}