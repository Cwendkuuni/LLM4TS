import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorImpl;

import java.io.IOException;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalAccessException;
import java.lang.NoSuchMethodException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_NullClass() {
    try {
      unsafeAllocator.newInstance(null);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_PrimitiveTypes() {
    try {
      @SuppressWarnings("unchecked")
      Integer i = (Integer) unsafeAllocator.newInstance(int.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_Interface() {
    try {
      Class<Runnable> runnableClass = Runnable.class;
      @SuppressWarnings("unchecked")
      Runnable runnable = (Runnable) unsafeAllocator.newInstance(runnableClass);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_AbstractClass() {
    try {
      Class<AbstractClass> abstractClass = AbstractClass.class;
      @SuppressWarnings("unchecked")
      AbstractClass abstractInstance = (AbstractClass) unsafeAllocator.newInstance(abstractClass);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_ArrayType() {
    try {
      @SuppressWarnings("unchecked")
      int[] intArray = (int[]) unsafeAllocator.newInstance(int[].class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_UnknownClass() {
    try {
      Class<?> unknownClass = Class.forName("UnknownClass");
      @SuppressWarnings("unchecked")
      Object unknownInstance = unsafeAllocator.newInstance((Class<Object>) unknownClass);
      fail("Expected IllegalAccessException or InstantiationException");
    } catch (ClassNotFoundException e) {
      // Expected
    } catch (IllegalAccessException e) {
      // Expected
    } catch (InstantiationException e) {
      // Expected
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_Throwable() {
    try {
      @SuppressWarnings("unchecked")
      Throwable throwable = (Throwable) unsafeAllocator.newInstance(Throwable.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_CustomClass() {
    try {
      Class<CustomClass> customClass = CustomClass.class;
      @SuppressWarnings("unchecked")
      CustomClass customInstance = (CustomClass) unsafeAllocator.newInstance(customClass);
      assertEquals("Expected", "Actual", customInstance.getValue());
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  static class AbstractClass {}

  static class CustomClass {
    private final String value;

    public CustomClass(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}