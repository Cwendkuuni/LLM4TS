import com.google.gson.internal.UnsafeAllocator;
import org.junit.Assert;
import org.junit.Test;

public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_JvmUnsafe_AllocateInstance() throws Exception {
    // Typical use case
    String str = unsafeAllocator.newInstance(String.class);
    Assert.assertNotNull(str);

    // Edge case
    Integer integer = unsafeAllocator.newInstance(Integer.class);
    Assert.assertNotNull(integer);

    // Error scenario
    try {
      // Instantiating interface should throw UnsupportedOperationException
      Runnable runnable = unsafeAllocator.newInstance(Runnable.class);
      Assert.fail("Expected exception was not thrown");
    } catch (UnsupportedOperationException e) {
      Assert.assertEquals("Cannot allocate interface", e.getMessage());
    }
  }

  @Test
  public void testNewInstance_DalvikvmPostGingerbread_ConstructorIdAndNewInstance() throws Exception {
    // Typical use case
    Object obj = unsafeAllocator.newInstance(Object.class);
    Assert.assertNotNull(obj);

    // Edge case
    Double dbl = unsafeAllocator.newInstance(Double.class);
    Assert.assertNotNull(dbl);

    // Error scenario
    try {
      // Instantiating abstract class should throw UnsupportedOperationException
      AbstractClass abstractClass = unsafeAllocator.newInstance(AbstractClass.class);
      Assert.fail("Expected exception was not thrown");
    } catch (UnsupportedOperationException e) {
      Assert.assertEquals("Cannot allocate abstract class", e.getMessage());
    }
  }

  @Test
  public void testNewInstance_DalvikvmPreGingerbread_NewInstance() throws Exception {
    // Typical use case
    MyClass myClass = unsafeAllocator.newInstance(MyClass.class);
    Assert.assertNotNull(myClass);

    // Edge case
    Long lng = unsafeAllocator.newInstance(Long.class);
    Assert.assertNotNull(lng);

    // Error scenario
    try {
      // Instantiating interface should throw UnsupportedOperationException
      Runnable runnable = unsafeAllocator.newInstance(Runnable.class);
      Assert.fail("Expected exception was not thrown");
    } catch (UnsupportedOperationException e) {
      Assert.assertEquals("Cannot allocate interface", e.getMessage());
    }
  }

  @Test
  public void testNewInstance_GiveUp() {
    // Error scenario
    try {
      // Instantiating abstract class should throw UnsupportedOperationException
      AbstractClass abstractClass = unsafeAllocator.newInstance(AbstractClass.class);
      Assert.fail("Expected exception was not thrown");
    } catch (UnsupportedOperationException e) {
      Assert.assertEquals("Cannot allocate abstract class", e.getMessage());
    }
  }

  private interface AbstractClass {}

  private static class MyClass {}

}