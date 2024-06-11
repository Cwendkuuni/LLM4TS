import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

 private UnsafeAllocator allocator;

 @Test
 public void testInit() {
 allocator = UnsafeAllocator.create();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCannotAllocateInterface() {
 try {
 allocator.newInstance(Object.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCannotAllocateAbstractClass() {
 try {
 allocator.newInstance(AbstractClass.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testAllocateObject() {
 try {
 Object obj = allocator.newInstance(Object.class);
 assertNotNull(obj);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testAllocateCustomClass() {
 try {
 CustomClass customClass = allocator.newInstance(CustomClass.class);
 assertNotNull(customClass);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = InstantiationException.class)
 public void testCantAllocateExceptionClass() {
 try {
 allocator.newInstance(InstantiationException.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = NoSuchMethodException.class)
 public void testNoSuchMethodException() {
 try {
 allocator.newInstance(NoSuchMethodException.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = SecurityException.class)
 public void testSecurityException() {
 try {
 allocator.newInstance(SecurityException.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() {
 try {
 allocator.newInstance(IllegalArgumentException.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullPointerException() {
 try {
 allocator.newInstance(NullPointerException.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = LinkageError.class)
 public void testLinkageError() {
 try {
 allocator.newInstance(LinkageError.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgException() {
 try {
 allocator.newInstance(IllegalArgumentException.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = IllegalAccessError.class)
 public void testIllegalAccessError() {
 try {
 allocator.newInstance(IllegalAccessError.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = NoSuchFieldException.class)
 public void testNoSuchFieldException() {
 try {
 allocator.newInstance(NoSuchFieldException.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = ClassFormatError.class)
 public void testClassFormatError() {
 try {
 allocator.newInstance(ClassFormatError.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = IncompatibleClassChangeError.class)
 public void testIncompatibleClassChangeError() {
 try {
 allocator.newInstance(IncompatibleClassChangeError.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = TypeNotPresentException.class)
 public void testTypeNotPresentException() {
 try {
 allocator.newInstance(TypeNotPresentException.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = NoClassDefFoundError.class)
 public void testNoClassDefFoundError() {
 try {
 allocator.newInstance(NoClassDefFoundError.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = AbstractMethodError.class)
 public void testAbstractMethodError() {
 try {
 allocator.newInstance(AbstractMethodError.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = NegativeArraySizeException.class)
 public void testNegativeArraySizeException() {
 try {
 allocator.newInstance(NegativeArraySizeException.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = ArrayStoreException.class)
 public void testArrayStoreException() {
 try {
 allocator.newInstance(ArrayStoreException.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testArrayIndexOutOfBoundsException() {
 try {
 allocator.newInstance(ArrayIndexOutOfBoundsException.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = ArrayStoreException.class)
 public void testArrayStoreException2() {
 try {
 Object[] arr = (Object[]) allocator.newInstance(Object[].class);
 arr[0] = new Integer(1);
 arr[1] = new String("foo");
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testAllocateArray() {
 try {
 int[] arr = (int[]) allocator.newInstance(int[].class);
 assertNotNull(arr);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testAllocateCustomArray() {
 try {
 CustomClass[] arr = (CustomClass[]) allocator.newInstance(CustomClass[].class);
 assertNotNull(arr);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 private static class AbstractClass {}

 private static class CustomClass {}

 private static class InstantiationException extends Exception {}

 private static class NoSuchMethodException extends NoSuchMethodError {}

 private static class SecurityException extends Exception {}

 private static class IllegalArgumentException extends Exception {}

 private static class NullPointerException extends Exception {}

 private static class LinkageError extends Exception {}

 private static class IllegalArgException extends IllegalArgumentException {}

 private static class IllegalAccessError extends AccessControlException {}

 private static class NoSuchFieldException extends NoSuchFieldError {}

 private static class ClassFormatError extends LinkageError {}

 private static class IncompatibleClassChangeError extends LinkageError {}

 private static class TypeNotPresentException extends NoClassDefFoundError {}

 private static class NoClassDefFoundError extends ClassNotFoundException {}

 private static class AbstractMethodError extends IncompatibleClassChangeError {}

 private static class NegativeArraySizeException extends ArrayIndexOutOfBoundsException {}

 private static class ArrayStoreException extends RuntimeException {}
}