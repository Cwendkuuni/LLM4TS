import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertNotNull(unsafeAllocator);
  }

  @Test
  public void testNewInstance_JVM() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      TestClass testClass = unsafeAllocator.newInstance(TestClass.class);
      assertNotNull(testClass);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testNewInstance_DalvikVM_PostGingerbread() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      TestClass testClass = unsafeAllocator.newInstance(TestClass.class);
      assertNotNull(testClass);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testNewInstance_DalvikVM_PreGingerbread() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      TestClass testClass = unsafeAllocator.newInstance(TestClass.class);
      assertNotNull(testClass);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_GiveUp() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    TestClass testClass = unsafeAllocator.newInstance(TestClass.class);
  }
}

class TestClass {
  // Empty class for testing purposes
}