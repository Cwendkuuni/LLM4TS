import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.internal.UnsafeAllocator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

 @Test
 @Category(IntegrationTest.class)
 public void testNewInstanceWithObjectClass() throws Exception {
 UnsafeAllocator allocator = UnsafeAllocator.create();
 Object instance = allocator.newInstance(Object.class);
 assertNotNull(instance);
 assertEquals(instance.getClass(), Object.class);
 }

 @Test(expected = UnsupportedOperationException.class)
 @Category(IntegrationTest.class)
 public void testNewInstanceWithInterfaceClass() throws Exception {
 UnsafeAllocator allocator = UnsafeAllocator.create();
 assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(RuntimeException.class));
 }

 @Test(expected = UnsupportedOperationException.class)
 @Category(IntegrationTest.class)
 public void testNewInstanceWithAbstractClass() throws Exception {
 UnsafeAllocator allocator = UnsafeAllocator.create();
 assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(AbstractClass.class));
 }

 @Test
 @Category(UnitTest.class)
 public void testNewInstanceWithConcreteClass() throws Exception {
 UnsafeAllocator allocator = UnsafeAllocator.create();
 String instance = (String) allocator.newInstance(String.class);
 assertNotNull(instance);
 assertEquals(instance, "");
 }

 @Test(expected = NullPointerException.class)
 @Category(UnitTest.class)
 public void testNewInstanceWithNullClass() throws Exception {
 UnsafeAllocator allocator = UnsafeAllocator.create();
 assertThrows(NullPointerException.class, () -> allocator.newInstance(null));
 }
}

class AbstractClass {
 // abstract class
}