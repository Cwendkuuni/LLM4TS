import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }

  @Test
  public void testNewInstance() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Class<?> clazz = MyClass.class;
    MyClass instance = allocator.newInstance(clazz);
    assertNotNull(instance);
    assertEquals(clazz, instance.getClass());
  }
  
  // Potential edge cases and exception handling scenarios:
  // - When the class constructor throws an exception
  
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_withException() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Class<?> clazz = ExceptionClass.class;
    allocator.newInstance(clazz);
  }
}