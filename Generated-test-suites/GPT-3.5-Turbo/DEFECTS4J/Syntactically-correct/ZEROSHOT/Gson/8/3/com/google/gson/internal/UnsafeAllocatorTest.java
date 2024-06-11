import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }

  @Test
  public void testNewInstanceWithSafeClass() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    String testString = allocator.newInstance(String.class);
    assertNotNull(testString);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithInterfaceClass() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    allocator.newInstance(List.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithAbstractClass() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    allocator.newInstance(AbstractClass.class);
  }

  @Test(expected = Exception.class)
  public void testNewInstanceWithExceptionClass() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    allocator.newInstance(ExceptionClass.class);
  }
}

class List {}

abstract class AbstractClass {}

class ExceptionClass {
  ExceptionClass() throws Exception {
    throw new Exception();
  }
}