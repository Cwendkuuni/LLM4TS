import org.junit.Test;
import org.junit.Assert.*;
import com.google.gson.internal.UnsafeAllocator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class UnsafeAllocatorTest {

 @Test
 public void testNewInstance() {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

 // Test with a simple Java class
 Class<String> stringClass = String.class;
 String instance1 = unsafeAllocator.newInstance(stringClass);
 Assert.assertNotNull(instance1);
 Assert.assertEquals("", instance1);

 // Test with a complex Java class
 class TestComplexClass implements Cloneable {
 int id;
 String name;
 TestComplexClass(int id, String name) {
 this.id = id;
 this.name = name;
 }

 @Override
 public Object clone() throws CloneNotSupportedException {
 return super.clone();
 }
 }

 Class<TestComplexClass> testComplexClass = TestComplexClass.class;
 TestComplexClass instance2 = null;
 try {
 instance2 = unsafeAllocator.newInstance(testComplexClass);
 } catch (Exception e) {
 if (e instanceof InvocationTargetException) {
 throw ((InvocationTargetException) e).getTargetException();
 }
 if (e instanceof UndeclaredThrowableException) {
 throw ((UndeclaredThrowableException) e).getException();
 }
 throw e;
 }

 Assert.assertNotNull(instance2);
 Assert.assertNotNull(instance2.id);
 Assert.assertNotNull(instance2.name);
 Assert.assertEquals(0, instance2.id);
 Assert.assertNull(instance2.name);

 // Test with an interface
 interface TestInterface {
 void print();
 }

 Class<TestInterface> testInterface = (Class<TestInterface>) Class.forName("TestInterface");
 TestInterface instance3 = null;
 try {
 instance3 = (TestInterface) unsafeAllocator.newInstance(testInterface);
 } catch (Exception e) {
 if (e instanceof InvocationTargetException) {
 throw ((InvocationTargetException) e).getTargetException();
 }
 if (e instanceof UndeclaredThrowableException) {
 throw ((UndeclaredThrowableException) e).getException();
 }
 throw e;
 }

 Assert.fail("Expected an UnsupportedOperationException but got: " + instance3);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceWithAbstractClass() {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

 Class<AbstractClass> abstractClass = AbstractClass.class;
 try {
 unsafeAllocator.newInstance(abstractClass);
 } catch (Exception e) {
 if (e instanceof InvocationTargetException) {
 throw ((InvocationTargetException) e).getTargetException();
 }
 if (e instanceof UndeclaredThrowableException) {
 throw ((UndeclaredThrowableException) e).getException();
 }
 throw e;
 }

 fail("Expected an UnsupportedOperationException but got: " + abstractClass);
 }

 abstract class AbstractClass {
 // Class body
 }
}