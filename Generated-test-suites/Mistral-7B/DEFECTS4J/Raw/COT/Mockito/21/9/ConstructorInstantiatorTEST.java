import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import java.lang.reflect.invocationtargetexception;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.IllegalAccessException;
import java.lang.reflect.UndeclaredThrowableException;

public class ConstructorInstantiatorTest {

 private Object outerClassInstance; // for testing purposes

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstance_outerClassInstanceIsNull_returnsInstanceFromNoArgConstructor() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 Class<String> stringClass = String.class;
 String instance = instantiator.newInstance(stringClass);
 Assert.assertNotNull(instance);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstance_outerClassInstanceIsNotNull_callsConstructorWithOuterClass() throws Exception {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<TestClass> testClass = TestClass.class;
 Constructor<TestClass> constructor = testClass.getDeclaredConstructor(Object.class);
 TestClass expectedInstance = new TestClass(outerClassInstance);
 TestClass instance = instantiator.newInstance(testClass);
 Assert.assertEquals(expectedInstance, instance);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testNewInstance_outerClassInstanceIsNotNull_throwsExceptionIfConstructorDoesNotExist() throws Exception {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<NonExistentConstructorTestClass> testClass = NonExistentConstructorTestClass.class;
 instantiator.newInstance(testClass);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testWithOuterClass_throwsExceptionIfNoArgConstructorThrowsException() throws Exception {
 Class<ThrowsExceptionTestClass> testClass = ThrowsExceptionTestClass.class;
 Assert.assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(null).withOuterClass(testClass));
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testWithOuterClass_throwsExceptionIfParamConstructorThrowsException() throws Exception {
 outerClassInstance = new Object();
 Class<ThrowsExceptionTestClass> testClass = ThrowsExceptionTestClass.class;
 Constructor<ThrowsExceptionTestClass> constructor = testClass.getDeclaredConstructor(Object.class);
 constructor.setAccessible(true);
 Assert.assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(outerClassInstance).withOuterClass(testClass));
 }

 @Test
 public void testParamsException_withExpectedMessage() {
 Class<TestClass> testClass = TestClass.class;
 InstantiationException exception = new ConstructorInstantiator(null).paramsException(testClass, new Exception());
 String expectedMessage = "Unable to create mock instance of 'TestClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";
 Assert.assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testParamsException_withActualExceptionMessage() {
 Class<TestClass> testClass = TestClass.class;
 Exception actualException = new Exception("Test exception message");
 InstantiationException exception = new ConstructorInstantiator(null).paramsException(testClass, actualException);
 Assert.assertEquals("Unable to create mock instance of 'TestClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
 }

 @Test
 public void testNoArgConstructor_throwsExceptionIfInstanceCreationFails() throws Exception {
 Class<ThrowsExceptionDuringConstructionTestClass> testClass = ThrowsExceptionDuringConstructionTestClass.class;
 Assert.assertThrows(InstantiationException.class, ConstructorInstantiator::noArgConstructor);
 }
}

 class TestClass {
 TestClass(Object outerClassInstance) {}
 }

 class NonExistentConstructorTestClass {}

 class ThrowsExceptionTestClass {
 ThrowsExceptionTestClass(Object outerClassInstance) throws Exception {}
 }

 class ThrowsExceptionDuringConstructionTestClass {
 ThrowsExceptionDuringConstructionTestClass() throws Exception {
 throw new Exception("Test exception message");
 }
}