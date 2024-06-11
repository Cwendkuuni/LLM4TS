import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

 private UnsafeAllocator allocator;

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedInstance() {
 allocator = UnsafeAllocator.create();
 try {
 allocator.newInstance(String[].class);
 } catch (Exception e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test
 public void testNewInstanceWithClassObject() {
 allocator = UnsafeAllocator.create();
 try {
 Object obj = allocator.newInstance(Object.class);
 assertNotNull(obj);
 } catch (Exception e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test(expected = Exception.class)
 public void testNewInstanceWithNullClass() {
 allocator = UnsafeAllocator.create();
 try {
 allocator.newInstance(null);
 } catch (NullPointerException e) {
 // Expected
 } catch (Exception e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewInstanceWithInvalidClass() {
 allocator = UnsafeAllocator.create();
 try {
 allocator.newInstance(UnsafeAllocator.class);
 } catch (Exception e) {
 if (e.getCause() instanceof IllegalArgumentException) {
 // Expected
 } else {
 throw new AssertionError("Unexpected exception", e);
 }
 }
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstanceWithFailedAllocation() {
 allocator = UnsafeAllocator.create();
 try {
 allocator.newInstance(UnsafeAllocator.class);
 } catch (Exception e) {
 if (e instanceof InstantiationException) {
 // Expected
 } else {
 throw new AssertionError("Unexpected exception", e);
 }
 }
 }

 @Test(expected = IllegalAccessException.class)
 public void testNewInstanceWithFailedAccess() {
 allocator = UnsafeAllocator.create();
 try {
 Object obj = allocator.newInstance(PrivilegedAccessClass.class);
 } catch (Exception e) {
 if (e instanceof IllegalAccessException) {
 // Expected
 } else if (e instanceof InvocationTargetException) {
 IllegalAccessException iae = ((InvocationTargetException) e).getTargetException();
 if (iae instanceof IllegalAccessException) {
 // Expected
 } else {
 throw new AssertionError("Unexpected exception", e);
 }
 } else if (e instanceof UndeclaredThrowableException) {
 Throwable cause = ((UndeclaredThrowableException) e).getThrown();
 if (cause instanceof IllegalAccessException) {
 // Expected
 } else {
 throw new AssertionError("Unexpected exception", e);
 }
 } else {
 throw new AssertionError("Unexpected exception", e);
 }
 }
 }

 static class PrivilegedAccessClass {
 private PrivilegedAccessClass() {
 }

 public static int getAccess() {
 return 0;
 }
 }

}