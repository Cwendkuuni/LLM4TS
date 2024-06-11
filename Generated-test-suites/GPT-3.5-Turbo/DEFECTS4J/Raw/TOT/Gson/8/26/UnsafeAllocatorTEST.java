import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertNotNull(unsafeAllocator);
  }

  @Test
  public void testNewInstance_jvm() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      TestClassA instance = unsafeAllocator.newInstance(TestClassA.class);
      assertNotNull(instance);
      assertTrue(instance instanceof TestClassA);
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_dalvikvm_postGingerbread() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      TestClassB instance = unsafeAllocator.newInstance(TestClassB.class);
      assertNotNull(instance);
      assertTrue(instance instanceof TestClassB);
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_dalvikvm_preGingerbread() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      TestClassC instance = unsafeAllocator.newInstance(TestClassC.class);
      assertNotNull(instance);
      assertTrue(instance instanceof TestClassC);
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_unsupported() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    unsafeAllocator.newInstance(TestClassD.class);
  }

  interface TestClassA {}

  abstract static class TestClassB {}

  static class TestClassC {}

  interface TestClassD {}
}