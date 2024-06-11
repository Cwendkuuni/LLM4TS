import org.junit.Test;
import org.junit. Assert;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @SuppressWarnings("unchecked")
 @Test
 public <T> void testNewInstanceWithNullOuterClassInstanceAndParamelessClass() {
 T result = new ConstructorInstantiator(null).newInstance(testClass);
 Assert.assertNotNull(result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public <T> void testNewInstanceWithNullOuterClassInstanceAndParamelessClassThrowsException() {
 try {
 new ConstructorInstantiator(null).newInstance(NoArgConstructorClass);
 Assert.fail();
 } catch (InstantiationException e) {
 Assert.assertEquals("Unable to create mock instance of 'NoArgConstructorClass'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public <T> void testNewInstanceWithNullOuterClassInstanceAndClassWithConstructor() {
 try {
 new ConstructorInstantiator(null).newInstance(ConstructorWithParamClass);
 Assert.fail();
 } catch (InstantiationException e) {
 Assert.assertEquals("Unable to create mock instance of 'ConstructorWithParamClass'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public <T> void testNewInstanceWithNotNullOuterClassInstanceAndParamelessClass() {
 outerClassInstance = new Object();
 T result = new ConstructorInstantiator(outerClassInstance).newInstance(testClass);
 Assert.assertNotNull(result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public <T> void testNewInstanceWithNotNullOuterClassInstanceAndClassWithConstructor() {
 outerClassInstance = new Object();
 T result = new ConstructorInstantiator(outerClassInstance).newInstance(ConstructorWithParamClass);
 Assert.assertNotNull(result);
 Whitebox.invokeMethod(result, "someMethodWithOuterClassInstance", outerClassInstance);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public <T> void testNewInstanceWithNotNullOuterClassInstanceAndClassWithNoConstructor() {
 outerClassInstance = new Object();
 new ConstructorInstantiator(outerClassInstance).newInstance(NoConstructorClass);
 }

 @SuppressWarnings("unchecked")
 @Test
 public <T> void testWithOuterClass() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
 Constructor<ConstructorWithParamClass> constructor = ConstructorWithParamClass.getDeclaredConstructor(Object.class);
 T result = new ConstructorInstantiator(new Object()).withOuterClass(ConstructorWithParamClass);
 ConstructorWithParamClass instance = constructor.newInstance(result);
 Whitebox.invokeMethod(instance, "someMethodWithOuterClassInstance", outerClassInstance);
 }

 @Test
 public void testParamsExceptionMessageFormat() {
 InstantiationException e = new InstantiationException("Unexpected error message", new RuntimeException("Runtime error"));
 String expectedMessage = "Unable to create mock instance of 'UnexpectedClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.\nUnexpected error message";
 Assert.assertEquals(expectedMessage, e.getMessage());
 }

 @Test
 public void testParamsExceptionNoOuterClass() {
 InstantiationException e = new ConstructorInstantiator(null).paramsException(TestClassWithConstructor.class, new RuntimeException("Runtime error"));
 String expectedMessage = "Unable to create mock instance of 'TestClassWithConstructor'.\nPlease ensure it has parameter-less constructor.\nRuntime error";
 Assert.assertEquals(expectedMessage, e.getMessage());
 }

 @Test
 public void testParamsExceptionWithOuterClass() {
 InstantiationException e = new ConstructorInstantiator(new Object()).paramsException(TestClassWithConstructor.class, new RuntimeException("Runtime error"));
 String expectedMessage = "Unable to create mock instance of 'TestClassWithConstructor'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.\nRuntime error";
 Assert.assertEquals(expectedMessage, e.getMessage());
 }

 private static class TestClassWithConstructor {
 TestClassWithConstructor() {
 }

 public void someMethodWithOuterClassInstance(Object outerClassInstance) {
 }
 }

 private static class NoArgConstructorClass {
 NoArgConstructorClass() {
 }
 }

 private static class ConstructorWithParamClass {
 ConstructorWithParamClass(Object outerClassInstance) {
 }

 public void someMethodWithOuterClassInstance(Object outerClassInstance) {
 }
 }
}