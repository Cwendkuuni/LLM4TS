import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testCreateUnsafeAllocator() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }

  @Test
  public void testNewInstance() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    MyClass instance = allocator.newInstance(MyClass.class);
    assertNotNull(instance);
  }

  @Test
  public void testNewInstanceWithUnsupportedClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      allocator.newInstance(AbstractClass.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // expected
    } catch (Exception e) {
      fail("Expected UnsupportedOperationException, got " + e.getClass());
    }
  }

  @Test
  public void testNewInstanceForDalvikVM() throws Exception {
    UnsafeAllocator allocator = new DalvikVMUnsafeAllocator();
    MyClass instance = allocator.newInstance(MyClass.class);
    assertNotNull(instance);
  }

  @Test
  public void testNewInstanceForDalvikVMEarlierVersion() throws Exception {
    UnsafeAllocator allocator = new DalvikVMEarlierUnsafeAllocator();
    MyClass instance = allocator.newInstance(MyClass.class);
    assertNotNull(instance);
  }

  private static class MyClass {
  }

  private abstract static class AbstractClass {
  }
}