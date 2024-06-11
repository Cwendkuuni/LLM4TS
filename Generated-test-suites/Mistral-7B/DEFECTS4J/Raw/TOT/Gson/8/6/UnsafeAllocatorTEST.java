import org.junit.Test;
import org.junit.experimental.categories.Category;
import com.google.gson.internal.UnsafeAllocator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.*;

@Category(IntegrationTests.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_String() {
    try {
      String instance = allocator.newInstance(String.class);
      assertNotNull(instance);
      assertEquals("", instance);
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_Integer() {
    try {
      Integer instance = allocator.newInstance(Integer.class);
      assertNotNull(instance);
      assertEquals(0, instance.intValue());
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_FailedCreationException() {
    try {
      allocator.newInstance(FailedCreationException.class);
      fail("Expected FailedCreationException, but got " +
          "an instance instead.");
    } catch (InvocationTargetException e) {
      assertInstanceOf(FailedCreationException.class, e.getCause());
    } catch (UndeclaredThrowableException e) {
      assertInstanceOf(FailedCreationException.class, e.getThrownable());
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() {
    allocator.newInstance(AbstractClass.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_Interface() {
    allocator.newInstance(Interface.class);
  }

  @Test
  public void testNewInstance_CustomClass() {
    try {
      CustomClass instance = allocator.newInstance(CustomClass.class);
      assertNotNull(instance);
      assertEquals("CustomInstance", instance.name);
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }
}

class FailedCreationException {
  // Empty construction error throwing class
}

abstract class AbstractClass {}

interface Interface {}

class CustomClass {
  private final String name;

  CustomClass() {
    this.name = "CustomInstance";
  }

  CustomClass(String name) {
    this.name = name;
  }
}