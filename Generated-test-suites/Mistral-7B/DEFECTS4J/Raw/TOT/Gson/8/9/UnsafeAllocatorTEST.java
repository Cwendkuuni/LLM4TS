import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_Object() {
    try {
      Object obj = allocator.newInstance(Object.class);
      assertNotNull(obj);
    } catch (Exception e) {
      fail("Failed to allocate instance of Object: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_String() {
    try {
      String str = (String) allocator.newInstance(String.class);
      assertNotNull(str);
      assertEquals("", str);
      str = (String) allocator.newInstance(String.class, new Object[]{"Hello World"});
      assertEquals("Hello World", str);
    } catch (Exception e) {
      fail("Failed to allocate instance of String: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_Exception() {
    try {
      ArithmeticException exception = (ArithmeticException) allocator.newInstance(ArithmeticException.class);
      assertNotNull(exception);
      exception.printStackTrace();
    } catch (Exception e) {
      fail("Failed to allocate instance of ArithmeticException: " + e.getMessage());
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_Interface() {
    try {
      Runnable runnable = (Runnable) allocator.newInstance(Runnable.class);
      fail("Should not be able to allocate instance of Runnable");
    } catch (Exception e) {
      if (e instanceof UnsupportedOperationException) {
        // Expected
      } else {
        fail("Unexpected exception: " + e.getMessage());
      }
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() {
    try {
      AbstractClass abstractClass = (AbstractClass) allocator.newInstance(AbstractClass.class);
      fail("Should not be able to allocate instance of AbstractClass");
    } catch (Exception e) {
      if (e instanceof UnsupportedOperationException) {
        // Expected
      } else {
        fail("Unexpected exception: " + e.getMessage());
      }
    }
  }

  @Test
  public void testNewInstance_InnerClass() {
    try {
      OuterClass outer = new OuterClass();
      InnerClass inner = outer.new InnerClass();
      UnsafeAllocator innerAllocator = UnsafeAllocator.create();
      InnerClass allocatedInner = (InnerClass) innerAllocator.newInstance(InnerClass.class, new Object[]{outer});
      assertEquals(inner, allocatedInner.outer);
    } catch (Exception e) {
      fail("Failed to allocate instance of InnerClass: " + e.getMessage());
    }
  }

  @Test(expected = InstantiationException.class)
  public void testNewInstance_Throwable() {
    try {
      allocator.newInstance(InstantiationException.class);
      fail("Should not be able to allocate instance of InstantiationException");
    } catch (Exception e) {
      if (e instanceof InstantiationException) {
        // Expected
      } else if (e instanceof InvocationTargetException) {
        InstantiationException cause = ((InvocationTargetException) e).getCause();
        if (cause instanceof InstantiationException) {
          // Expected
        } else {
          fail("Unexpected cause: " + cause.getMessage());
        }
      } else if (e instanceof UndeclaredThrowableException) {
        InstantiationException cause = ((UndeclaredThrowableException) e).getThrowable();
        if (cause instanceof InstantiationException) {
          // Expected
        } else {
          fail("Unexpected cause: " + cause.getMessage());
        }
      } else {
        fail("Unexpected exception: " + e.getMessage());
      }
    }
  }

  private static class OuterClass {
    public class InnerClass {
      Object outer;
    }
  }
}