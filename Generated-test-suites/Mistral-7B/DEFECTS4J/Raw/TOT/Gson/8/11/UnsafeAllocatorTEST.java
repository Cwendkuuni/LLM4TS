import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.Primitives;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstanceWithValidClass() {
    Class<String> stringClass = String.class;
    String instance = allocator.newInstance(stringClass);
    assertTrue(instance instanceof String);
  }

  @Test
  public void testNewInstanceWithArrayClass() {
    Class<int[]> intArrayClass = int[].class;
    int[] instance = allocator.newInstance(intArrayClass);
    assertArrayEquals(new int[0], instance);
  }

  @Test
  public void testNewInstanceWithPrimitiveClass() {
    Class<Integer> integerClass = Integer.class;
    Integer instance = allocator.newInstance(integerClass);
    assertEquals(0, instance.intValue());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithInterfaceClass() {
    Class<Runnable> runnableClass = Runnable.class;
    allocator.newInstance(runnableClass);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithAbstractClass() {
    Class<AbstractClass> abstractClass = AbstractClass.class;
    allocator.newInstance(abstractClass);
  }

  @Test
  public void testNewInstanceWithException() {
    try {
      Class<IllegalArgumentException> exceptionClass = IllegalArgumentException.class;
      allocator.newInstance(exceptionClass);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      assertEquals("Cannot allocate " + IllegalArgumentException.class, e.getMessage());
    }
  }

  @Test
  public void testNewInstanceWithNullClass() {
    try {
      allocator.newInstance(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("class", e.getPropertyName());
    }
  }

  private static class AbstractClass {
  }
}