import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

 private UnsafeAllocator unsafeAllocator;

 @Before
 public void setUp() {
 unsafeAllocator = UnsafeAllocator.create();
 }

 @Test
 public void testNewInstance_ValidClass() throws Exception {
 Object obj = unsafeAllocator.newInstance(Object.class);
 assertNotNull(obj);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_InterfaceClass() throws Exception {
 assertNull(unsafeAllocator.newInstance(Comparable.class));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_AbstractClass() throws Exception {
 assertNull(unsafeAllocator.newInstance(AbstractClass.class));
 }

 @Test(expected = Exception.class)
 public void testNewInstance_ThrowingClass() throws Exception {
 try {
 Object obj = unsafeAllocator.newInstance(ThrowingClass.class);
 } catch (InvocationTargetException e) {
 assertNull(e.getTarget());
 throw e.getCause();
 } catch (UndeclaredThrowableException e) {
 throw e.getCause();
 }
 }

 @Test
 public void testNewInstance_NullClass() throws Exception {
 assertNull(unsafeAllocator.newInstance(null));
 }

 @Test
 public void testNewInstance_UnknownClass() throws Exception {
 assertNull(unsafeAllocator.newInstance(UnknownClass.class));
 }

}

 class AbstractClass {
 // an abstract class
 }

 class Comparable<T> {
 // a Java interface
 }

 interface ThrowingInterface {
 void throwException();
 }

 class ThrowingClass implements ThrowingInterface {
 @Override
 public void throwException() {
 throw new RuntimeException();
 }
 }

 class UnknownClass {
 // a non-existent class
 }