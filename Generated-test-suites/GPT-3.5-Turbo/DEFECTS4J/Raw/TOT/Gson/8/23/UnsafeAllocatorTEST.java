import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertNotNull(unsafeAllocator);
  }

  @Test
  public void testNewInstanceWithJVM() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    if (unsafeAllocator instanceof JVMUnsafeAllocator) {
      ClassWithConstructor classWithConstructor = unsafeAllocator.newInstance(ClassWithConstructor.class);
      assertNotNull(classWithConstructor);
      assertNotNull(classWithConstructor.getConstructor());
    } else {
      // Ignore the test if unsafe allocator is not JVMUnsafeAllocator
      assertTrue(true);
    }
  }

  @Test
  public void testNewInstanceWithDalvikPostGingerbread() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    if (unsafeAllocator instanceof PostGingerbreadUnsafeAllocator) {
      ClassWithConstructor classWithConstructor = unsafeAllocator.newInstance(ClassWithConstructor.class);
      assertNotNull(classWithConstructor);
      assertNotNull(classWithConstructor.getConstructor());
    } else {
      // Ignore the test if unsafe allocator is not PostGingerbreadUnsafeAllocator
      assertTrue(true);
    }
  }

  @Test
  public void testNewInstanceWithDalvikPreGingerbread() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    if (unsafeAllocator instanceof PreGingerbreadUnsafeAllocator) {
      ClassWithConstructor classWithConstructor = unsafeAllocator.newInstance(ClassWithConstructor.class);
      assertNotNull(classWithConstructor);
      assertNotNull(classWithConstructor.getConstructor());
    } else {
      // Ignore the test if unsafe allocator is not PreGingerbreadUnsafeAllocator
      assertTrue(true);
    }
  }

  private static class ClassWithConstructor {
    private String constructor;

    public ClassWithConstructor() {
      constructor = "Constructor called.";
    }

    public String getConstructor() {
      return constructor;
    }
  }
}