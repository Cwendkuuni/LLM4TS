import org.junit.Test;
import org.junit.Assert;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorImpl;
import java.lang.reflect.InvocationTargetException;

public class UnsafeAllocatorTest {

 private UnsafeAllocator allocator;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 allocator = UnsafeAllocator.create();
 }

 @Test
 public void testNewInstance_Object() {
 Object obj = allocator.newInstance(Object.class);
 Assert.assertNotNull(obj);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_Interface() {
 try {
 allocator.newInstance(String.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 Assert.fail("Expected UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_AbstractClass() {
 try {
 allocator.newInstance(AbstractClass.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 Assert.fail("Expected UnsupportedOperationException");
 }

 @Test
 public void testNewInstance_ThrowingExceptionClass() throws Exception {
 Class<?> exceptionClass = Exception.class;
 Object obj = allocator.newInstance(exceptionClass);
 Assert.assertNotNull(obj);
 Assert.assertTrue(obj instanceof Exception);
 }

 @Test(expected = RuntimeException.class)
 public void testNewInstance_NullClass() {
 try {
 allocator.newInstance(null);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 Assert.fail("Expected RuntimeException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewInstance_UnknownClass() {
 try {
 allocator.newInstance(Class.forName("com.unknown.UnknownClass"));
 } catch (Exception e) {
 if (e.getCause() instanceof IllegalArgumentException) {
 return;
 }
 throw new RuntimeException(e);
 }
 Assert.fail("Expected IllegalArgumentException");
 }

 @Test
 public void testNewInstance_Array() throws Exception {
 Class<?> arrayClass = int[].class;
 Object obj = allocator.newInstance(arrayClass);
 Assert.assertNotNull(obj);
 Assert.assertTrue(obj.getClass().isArray());
 }

 @Test
 public void testNewInstance_ArrayWithComponentTypeThrowingException() throws Exception {
 Class<?> arrayClass = Exception[].class;
 Object obj = allocator.newInstance(arrayClass);
 Assert.assertNotNull(obj);
 Assert.assertTrue(obj.getClass().isArray());
 for (Object element : (Object[]) obj) {
 Assert.assertTrue(element instanceof Exception);
 }
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstance_FailingClass() throws Exception {
 Class<?> failingClass = FailingClass.class;
 try {
 allocator.newInstance(failingClass);
 } catch (Exception e) {
 if (e.getCause() instanceof InstantiationException) {
 return;
 }
 throw new RuntimeException(e);
 }
 Assert.fail("Expected InstantiationException");
 }

 @Test(expected = InvocationTargetException.class)
 public void testNewInstance_ClassWithPrivateConstructor() throws Exception {
 Class<?> privateConstructorClass = PrivateConstructorClass.class;
 Object obj = allocator.newInstance(privateConstructorClass);
 Assert.assertNotNull(obj);
 Assert.assertTrue(obj instanceof PrivateConstructorClass);
 }

 @Test(expected = IllegalAccessException.class)
 public void testNewInstance_ClassWithPrivateConstructorAndNoPublicNoArgConstructor() throws Exception {
 Class<?> privateConstructorAndNoPublicNoArgConstructorClass =
 PrivateConstructorAndNoPublicNoArgConstructorClass.class;
 try {
 allocator.newInstance(privateConstructorAndNoPublicNoArgConstructorClass);
 } catch (Exception e) {
 if (e.getCause() instanceof IllegalAccessException) {
 return;
 }
 throw new RuntimeException(e);
 }
 Assert.fail("Expected IllegalAccessException");
 }

 @Test
 public void testNewInstance_ClassWithDefaultConstructor() throws Exception {
 Class<?> defaultConstructorClass = DefaultConstructorClass.class;
 Object obj = allocator.newInstance(defaultConstructorClass);
 Assert.assertNotNull(obj);
 Assert.assertTrue(obj instanceof DefaultConstructorClass);
 }
}

 class FailingClass {
 public FailingClass() throws InstantiationException {
 throw new InstantiationException();
 }
 }

 class PrivateConstructorClass {
 private PrivateConstructorClass() {}
 }

 class PrivateConstructorAndNoPublicNoArgConstructorClass {
 private PrivateConstructorAndNoPublicNoArgConstructorClass(int i) {}
 }

 class DefaultConstructorClass {
 }