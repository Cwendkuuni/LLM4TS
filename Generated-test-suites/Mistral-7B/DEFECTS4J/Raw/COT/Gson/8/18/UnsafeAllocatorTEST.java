import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.lang.reflect.Modifier;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

 private UnsafeAllocator unsafeAllocator;

 @Before
 public void setUp() {
 unsafeAllocator = UnsafeAllocator.create();
 }

 @Test
 public void testNewInstance_Null() {
 assertNull(unsafeAllocator.newInstance(null));
 }

 @Test(expected = NullPointerException.class)
 public void testNewInstance_NullType() {
 unsafeAllocator.newInstance(null);
 }

 @Test
 public <T> void testNewInstance_ValidType() {
 Class<T> type = String.class;
 try {
 T instance = unsafeAllocator.newInstance(type);
 assertInstanceOf(String.class, instance);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_InterfaceType() {
 Class<Object> iface = Runnable.class;
 try {
 unsafeAllocator.newInstance(iface);
 assert False : "Should not reach this point.";
 } catch (Exception e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_AbstractType() {
 Class<Object> abstractType = AbstractClass.class;
 try {
 unsafeAllocator.newInstance(abstractType);
 assert False : "Should not reach this point.";
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testNewInstance_ThrowingException() {
 Class<ThrowingException> throwingException = ThrowingException.class;
 try {
 unsafeAllocator.newInstance(throwingException);
 assert False : "Should not reach this point.";
 } catch (Exception e) {
 if (e instanceof InstantiationException) {
 InstantiationException ie = (InstantiationException) e;
 ThrowingException te = null;
 try {
 te = (ThrowingException) ie.getCause();
 } catch (ClassCastException cce) {
 // Expected, cause not an instance of ThrowingException
 }
 assert instanceOf(ThrowingException.class, te);
 } else {
 throw new AssertionError("Unexpected exception: " + e);
 }
 }
 }

 @Test
 public void testNewInstance_CheckAccess() {
 try {
 Class<?> checkAccessClass = AccessCheck.class;
 UnsafeAllocator unsafeAllocatorCheckAccess = UnsafeAllocator.create();
 unsafeAllocatorCheckAccess.newInstance(checkAccessClass);
 assert False : "Should not reach this point.";
 } catch (IllegalAccessException iae) {
 // Expected
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = IllegalAccessError.class)
 public void testNewInstance_IllegalAccessError() {
 try {
 Field illegalAccessField = UnsafeAllocator.class.getDeclaredField("unsafe");
 illegalAccessField.setAccessible(true);
 Object unsafe = illegalAccessField.get(unsafeAllocator);
 Method allocateInstance = Unsafe.class.getDeclaredMethod("allocateInstance", Class.class);
 allocateInstance.setAccessible(true);
 allocateInstance.invoke(unsafe, String.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = InvocationTargetException.class)
 public void testNewInstance_InvocationTargetException() {
 try {
 Method getConstructorId = ObjectStreamClass.class
     .getDeclaredMethod("getConstructorId", Class.class);
 getConstructorId.setAccessible(true);
 final int constructorId = (Integer) getConstructorId.invoke(null, Object.class);
 Method newInstance = ObjectStreamClass.class
     .getDeclaredMethod("newInstance", Class.class, int.class);
 newInstance.setAccessible(true);
 newInstance.invoke(null, Object[].class, constructorId);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = UndeclaredThrowableException.class)
 public void testNewInstance_UndeclaredThrowableException() {
 try {
 Method newInstance = ObjectInputStream.class
     .getDeclaredMethod("newInstance", Class.class, Class.class);
 newInstance.setAccessible(true);
 newInstance.invoke(null, Object[].class, Object.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testNewInstance_Failure() {
 try {
 Class<?> failureClass = FailureClass.class;
 unsafeAllocator.newInstance(failureClass);
 assert False : "Should not reach this point.";
 } catch (Exception e) {
 // Expected
 }
 }

}