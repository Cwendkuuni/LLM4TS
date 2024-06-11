import static org.junit.Assert.*;

import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }

  @Test
  public void testNewInstance() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(Object.class);
    } catch (Exception e) {
      fail("Should not throw an exception");
    }
  }

  @Test
  public void testNewInstanceUnsupported() {
    UnsafeAllocator allocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) {
        throw new UnsupportedOperationException("Unsupported operation");
      }
    };
    try {
      allocator.newInstance(Object.class);
      fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Pass
    } catch (Exception e) {
      fail("Should throw UnsupportedOperationException");
    }
  }

  @Test
  public void testNewInstanceWithAbstractClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(AbstractTestClass.class);
      fail("Should throw an exception for abstract class");
    } catch (UnsupportedOperationException e) {
      // Pass
    } catch (Exception e) {
      fail("Should throw UnsupportedOperationException");
    }
  }

  @Test
  public void testNewInstanceWithInterface() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(TestInterface.class);
      fail("Should throw an exception for interface");
    } catch (UnsupportedOperationException e) {
      // Pass
    } catch (Exception e) {
      fail("Should throw UnsupportedOperationException");
    }
  }

  private abstract class AbstractTestClass {

  }

  private interface TestInterface {

  }
}