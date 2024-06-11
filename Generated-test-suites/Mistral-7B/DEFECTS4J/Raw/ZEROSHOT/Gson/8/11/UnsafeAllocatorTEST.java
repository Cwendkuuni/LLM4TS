import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.lang.reflect.InvocationTargetException;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorImpl;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

 private UnsafeAllocator unsafeAllocator;

 @Test
 public void testUnsafeAllocatorCreation() {
 unsafeAllocator = UnsafeAllocator.create();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCannotAllocateInterface() {
 Class<?> interfaceClass = java.io.Serializable.class;
 try {
 unsafeAllocator.newInstance(interfaceClass);
 } catch (Exception e) {
 throw new RuntimeException("Should have thrown UnsupportedOperationException", e);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCannotAllocateAbstractClass() {
 Class<?> abstractClass = java.lang.AbstractStringBuilder.class;
 try {
 unsafeAllocator.newInstance(abstractClass);
 } catch (Exception e) {
 throw new RuntimeException("Should have thrown UnsupportedOperationException", e);
 }
 }

 @Test(expected = InstantiationException.class)
 public void testCanAllocateSimpleClass() {
 Class<?> simpleClass = java.lang.String.class;
 try {
 String instance = unsafeAllocator.newInstance(simpleClass);
 } catch (Exception e) {
 throw new RuntimeException("Should have succeeded", e);
 }
 }

 @Test(expected = InvocationTargetException.class)
 public void testThrowsExpectedException() throws Exception {
 Class<?> throwableClass = java.lang.RuntimeException.class;
 try {
 UnsafeAllocatorImpl unsafeAllocatorImpl = (UnsafeAllocatorImpl) this.unsafeAllocator;
 Object instance = unsafeAllocatorImpl.newInstance((Class<?>) throwableClass.getConstructor().newInstance());
 throw new RuntimeException("Should have thrown an exception");
 } catch (InstantiationException e) {
 // expected
 } catch (InvocationTargetException e) {
 // expected, check the cause
 RuntimeException cause = (RuntimeException) e.getCause();
 assertEquals("Should have thrown the correct exception", "Test message", cause.getMessage());
 }
 }

 @Test(expected = NoSuchMethodException.class)
 public void testThrowsExpectedNoSuchMethodException() throws Exception {
 Class<?> throwableClass = java.lang.RuntimeException.class;
 Method getConstructor = throwableClass.getConstructor();
 try {
 UnsafeAllocatorImpl unsafeAllocatorImpl = (UnsafeAllocatorImpl) this.unsafeAllocator;
 unsafeAllocatorImpl.newInstance((Class<?>) throwableClass.getName());
 } catch (Exception e) {
 throw new RuntimeException("Should have thrown NoSuchMethodException", e);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testThrowsExpectedIllegalArgumentException() throws Exception {
 Class<?> nullClass = null;
 try {
 UnsafeAllocatorImpl unsafeAllocatorImpl = (UnsafeAllocatorImpl) this.unsafeAllocator;
 unsafeAllocatorImpl.newInstance((Class<?>) nullClass);
 } catch (Exception e) {
 throw new RuntimeException("Should have thrown IllegalArgumentException", e);
 }
 }

 @Test(expected = SecurityException.class)
 public void testThrowsExpectedSecurityException() throws Exception {
 Class<?> securityManagerClass = java.lang.SecurityManager.class;
 try {
 UnsafeAllocatorImpl unsafeAllocatorImpl = (UnsafeAllocatorImpl) this.unsafeAllocator;
 Object securityManager = System.getSecurityManager();
 if (securityManager != null) {
 System.setSecurityManager(null);
 }
 Object instance = unsafeAllocatorImpl.newInstance(securityManagerClass);
 if (securityManager != null) {
 System.setSecurityManager(securityManager);
 }
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testNullUnsafeAllocator() {
 UnsafeAllocator nullUnsafeAllocator = null;
 try {
 nullUnsafeAllocator.newInstance(String.class);
 } catch (Exception e) {
 // expected
 }
 }
}