import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

   @Test
   public void testCreate() {
      UnsafeAllocator allocator = UnsafeAllocator.create();
      assertNotNull(allocator);
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testNewInstance() throws Exception {
      UnsafeAllocator allocator = UnsafeAllocator.create();
      allocator.newInstance(Object.class);
   }

}