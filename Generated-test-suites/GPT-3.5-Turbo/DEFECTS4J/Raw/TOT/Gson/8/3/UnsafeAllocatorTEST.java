import com.google.gson.internal.UnsafeAllocator;
import org.junit.Assert;
import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Assert.assertNotNull(allocator);
  }

  @Test
  public void testUnsafeAllocatorWithExistingClass() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    MyClass myClass = allocator.newInstance(MyClass.class);
    Assert.assertNotNull(myClass);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUnsafeAllocatorWithInterfaceClass() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    MyInterface myInterface = allocator.newInstance(MyInterface.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUnsafeAllocatorWithAbstractClass() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    MyAbstractClass myAbstractClass = allocator.newInstance(MyAbstractClass.class);
  }

  @Test
  public void testUnsafeAllocatorWithConstructorException() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      ConstructErrorExceptionClass errorClass = allocator.newInstance(ConstructErrorExceptionClass.class);
      Assert.fail("Expected an exception");
    } catch (Exception e) {
      Assert.assertEquals(IllegalAccessException.class, e.getCause().getClass());
    }
  }

}

interface MyInterface {
  void doSomething();
}

abstract class MyAbstractClass {
  abstract void doSomething();
}

class MyClass {
  public void doSomething() {
  }
}

class ConstructErrorExceptionClass {
  public ConstructErrorExceptionClass() throws IllegalAccessException {
    throw new IllegalAccessException();
  }
}