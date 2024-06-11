import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

 private UnsafeAllocator allocator;

 @Test
 public void testNewInstance() {
 allocator = UnsafeAllocator.create();

 // Test normal use case
 assertNotNull(allocator.newInstance(String.class));

 // Test null parameter
 assertThrows(NullPointerException.class, () -> allocator.newInstance(null));

 // Test non-instantiable class
 assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(UnsafeAllocator.class));

 // Test interface
 assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(Runnable.class));

 // Test abstract class
 assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(AbstractClass.class));
 }

 // Replace AbstractClass with an abstract class you want to test
 static class AbstractClass {
 // Abstract class implementation
 }
}