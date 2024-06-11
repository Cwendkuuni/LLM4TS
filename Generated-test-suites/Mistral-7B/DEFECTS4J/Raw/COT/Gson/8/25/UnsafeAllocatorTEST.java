import org.junit.Test;
import org.junit.experimental.annotations.SuppressWarnings;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorInstance;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@SuppressWarnings("unchecked")
public class UnsafeAllocatorTest {

  private UnsafeAllocator getUnsafeAllocatorInstance() {
    return UnsafeAllocator.create();
  }

  @Test
  public void testNewInstance_ValidClass() throws Exception {
    UnsafeAllocator unsafeAllocator = getUnsafeAllocatorInstance();
    String stringInstance = unsafeAllocator.newInstance(String.class);
    assertNotNull(stringInstance);
  }

  @Test
  public void testNewInstance_AbstractClass() throws Exception {
    UnsafeAllocator unsafeAllocator = getUnsafeAllocatorInstance();
    Object abstractClassInstance = unsafeAllocator.newInstance(AbstractClass.class);
    assertNull(abstractClassInstance);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_Interface() throws Exception {
    UnsafeAllocator unsafeAllocator = getUnsafeAllocatorInstance();
    unsafeAllocator.newInstance(Interface.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_NullClass() throws Exception {
    UnsafeAllocator unsafeAllocator = getUnsafeAllocatorInstance();
    unsafeAllocator.newInstance(null);
  }

  @Test
  public void testNewInstance_ThrowingConstructor() throws Exception {
    Class<?> throwingClass = ThrowingClass.class;
    UnsafeAllocator unsafeAllocator = getUnsafeAllocatorInstance();

    try {
      unsafeAllocator.newInstance(throwingClass);
      assertFalse(true);
    } catch (Exception e) {
      assertNotNull(e.getCause());
    }
  }

  @Test
  public void testNewInstance_PrivateConstructor() throws Exception {
    Class<?> privateConstructorClass = PrivateConstructorClass.class;
    UnsafeAllocator unsafeAllocator = getUnsafeAllocatorInstance();

    try {
      unsafeAllocator.newInstance(privateConstructorClass);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }

    Constructor<PrivateConstructorClass> constructor = privateConstructorClass.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object privateConstructorInstance = constructor.newInstance();

    UnsafeAllocator unsafeAllocatorWithPrivateInstance = new UnsafeAllocatorInstance() {
      @Override
      public <T> T newInstance(Class<T> c) throws Exception {
        return (T) privateConstructorInstance;
      }
    };

    Object instanceUsingPrivateAllocator = unsafeAllocatorWithPrivateInstance.newInstance(privateConstructorClass);
    assertNotNull(instanceUsingPrivateAllocator);
  }

  @Test
  public void testNewInstance_Array() throws Exception {
    UnsafeAllocator unsafeAllocator = getUnsafeAllocatorInstance();
    int[] arrayInstance = (int[]) unsafeAllocator.newInstance(int[].class);
    assertNotNull(arrayInstance);
  }

  @Test
  public void testNewInstance_GenericArray() throws Exception {
    UnsafeAllocator unsafeAllocator = getUnsafeAllocatorInstance();
    @SuppressWarnings("unchecked")
    Integer[] genericArrayInstance = (Integer[]) unsafeAllocator.newInstance(Integer[].class);
    assertNotNull(genericArrayInstance);
  }

  @Test
  public void testNewInstance_CustomClassWithNoArgsConstructor() throws Exception {
    UnsafeAllocator unsafeAllocator = getUnsafeAllocatorInstance();
    CustomClassWithNoArgsConstructor instance = (CustomClassWithNoArgsConstructor) unsafeAllocator.newInstance(CustomClassWithNoArgsConstructor.class);
    assertNotNull(instance);
  }

  @Test
  public void testNewInstance_CustomClassWithArgsConstructor() throws Exception {
    UnsafeAllocator unsafeAllocator = getUnsafeAllocatorInstance();
    int arg1 = 1;
    int arg2 = 2;
    CustomClassWithArgsConstructor instance = (CustomClassWithArgsConstructor) unsafeAllocator.newInstance(CustomClassWithArgsConstructor.class, arg1, arg2);
    assertNotNull(instance);
  }

  @Test(expected = InstantiationException.class)
  public void testNewInstance_NonPublicConstructor_WithReflect() throws Exception {
    Class<?> nonPublicConstructorClass = NonPublicConstructorClass.class;
    Constructor<NonPublicConstructorClass> constructor = nonPublicConstructorClass.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object nonPublicInstance = constructor.newInstance();

    UnsafeAllocator unsafeAllocator = getUnsafeAllocatorInstance();
    unsafeAllocator.newInstance(NonPublicConstructorClass.class);
  }

  @Test(expected = InstantiationException.class)
  public void testNewInstance_NonPublicConstructor_WithoutReflect() throws Exception {
    UnsafeAllocator unsafeAllocator = getUnsafeAllocatorInstance();
    unsafeAllocator.newInstance(NonPublicConstructorClass.class);
  }

  static class AbstractClass {}
  static class Interface {}
  static class ThrowingClass extends Exception {}
  static class PrivateConstructorClass {
    private PrivateConstructorClass() {}
  }
  static class CustomClassWithNoArgsConstructor {}
  static class CustomClassWithArgsConstructor {
    int arg1;
    int arg2;

    CustomClassWithArgsConstructor(int arg1, int arg2) {
      this.arg1 = arg1;
      this.arg2 = arg2;
    }
  }
  static class NonPublicConstructorClass {
    NonPublicConstructorClass() {}

    private NonPublicConstructorClass(int arg) {}
  }
}