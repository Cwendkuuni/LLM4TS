import org.junit.Test;
import org.junit.experimental.annotations.EdgeCases;
import org.junit.experimental.annotations.Unroll;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
@EdgeCases(ClassSource.class)
@Unroll(value = "knownConstructors.class")
public class UnsafeAllocatorTest {

  private final Class<?> clazz;
  private final UnsafeAllocator allocator;

  public UnsafeAllocatorTest(Class<?> clazz) {
    this.clazz = clazz;
    this.allocator = UnsafeAllocator.create();
  }

  @Parameters(name = "{index}: testing class {0}")
  public static Collection<Class<?>> knownClasses() {
    return Arrays.asList(String.class, Integer.class, Boolean.class, UnsafeAllocator.class);
  }

  @Test
  public void testNewInstance() throws Exception {
    @SuppressWarnings("unchecked")
    Object instance = allocator.newInstance(clazz);
    assertNotNull(instance);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUnsupportedClass() throws Exception {
    allocator.newInstance(UnsafeAllocatorTest.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullClass() throws Exception {
    allocator.newInstance(null);
  }

  @Test
  public void testInterfaceClass() throws Exception {
    assertFalse(clazz.isInstance(allocator.newInstance(clazz.getInterfaces()[0])));
  }

  @Test
  public void testAbstractClass() throws Exception {
    assertThrows(UnsupportedOperationException.class, () -> {
      allocator.newInstance(clazz.getSuperclass());
    });
  }

  @Test(expected = InstantiationException.class)
  public void testObjectStreamClass() throws Exception {
    Method newInstance = ObjectStreamClass.class
        .getDeclaredMethod("newInstance", Class.class, int.class);
    newInstance.setAccessible(true);
    allocator.newInstance(ObjectStreamClass.class);
  }

  @Test(expected = IllegalAccessException.class)
  public void testUnsafeFieldAccess() throws Exception {
    Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
    Field f = unsafeClass.getDeclaredField("theUnsafe");
    f.setAccessible(true);
    Object unsafe = f.get(null);
    Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
    allocateInstance.setAccessible(true);
    allocator.newInstance(clazz);
  }

  @Test(expected = NoSuchMethodException.class)
  public void testUnsupportedMethod() throws Exception {
    allocator.newInstance(int[].class);
  }

  @Test(expected = InvocationTargetException.class)
  public void testUnexpectedException() throws Exception {
    allocator.newInstance(Throwable.class);
  }
}

class ClassSource {
  // Marker annotation to allow JUnit to generate test cases for all known constructors
  @interface KnownConstructors {
  }

  @KnownConstructors
  public static class knownConstructors {
  }
}