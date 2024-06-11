import org.junit.Test;
import org.junit.Assert.*;
import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

  private UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_ClassWithNoConstructor() {
    try {
      Class<String> stringClass = String.class;
      String instance = allocator.newInstance(stringClass);
      Assert.assertNotNull(instance);
      Assert.assertEquals("", instance);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_ClassWithPublicConstructor() {
    try {
      Class<Integer> integerClass = Integer.class;
      Integer instance = allocator.newInstance(integerClass);
      Assert.assertNotNull(instance);
      Assert.assertEquals(0, instance.intValue());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_ClassWithPrivateConstructor() {
    try {
      Class<PrivateConstructorClass> privateConstructorClass = PrivateConstructorClass.class;
      PrivateConstructorClass instance = allocator.newInstance(privateConstructorClass);
      Assert.assertNotNull(instance);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_Interface() {
    try {
      Class<Runnable> runnableClass = Runnable.class;
      allocator.newInstance(runnableClass);
    } catch (Exception e) {
      throw e;
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() {
    try {
      Class<AbstractClass> abstractClass = AbstractClass.class;
      allocator.newInstance(abstractClass);
    } catch (Exception e) {
      throw e;
    }
  }

  @Test
  public void testNewInstance_ClassWithNoDefaultConstructor() {
    try {
      Class<NoDefaultConstructorClass> noDefaultConstructorClass = NoDefaultConstructorClass.class;
      NoDefaultConstructorClass instance = allocator.newInstance(noDefaultConstructorClass);
      Assert.assertNotNull(instance);
      // assert instance fields here
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  static class PrivateConstructorClass {
    private PrivateConstructorClass() {}
  }

  static abstract class AbstractClass {}

  static class NoDefaultConstructorClass {
    private int a;
    private int b;

    NoDefaultConstructorClass(int a, int b) {
      this.a = a;
      this.b = b;
    }
  }
}