import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {
  
  private UnsafeAllocator unsafeAllocator;
  
  @Before
  public void setUp() {
    unsafeAllocator = UnsafeAllocator.create();
  }
  
  @Test
  public void testNewInstanceWithValidClass() {
    try {
      Object instance = unsafeAllocator.newInstance(String.class);
      assertTrue(instance instanceof String);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithUnsupportedClass() throws Exception {
    unsafeAllocator.newInstance(Void.class);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithInterfaceClass() throws Exception {
    unsafeAllocator.newInstance(List.class);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithAbstractClass() throws Exception {
    unsafeAllocator.newInstance(AbstractClass.class);
  }
  
  @Test
  public void testCreate() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    assertNotNull(allocator);
  }
}



// Generated test cases go here

