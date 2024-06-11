package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstance_NullOuterClassInstance_ReturnsNewInstance() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 Class<String> stringClass = String.class;
 String instance = instantiator.newInstance(stringClass);
 assertNull(outerClassInstance);
 assertInstanceOf(String.class, instance);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstance_NotNullOuterClassInstance_ReturnsInstanceWithOuterClass() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<TestClass> testClass = TestClass.class;
 TestClass instance = instantiator.newInstance(testClass);
 assertInstanceOf(TestClass.class, instance);
 assertSame(outerClassInstance, getInternalState(instance, "outerInstance"));
 }

 @Test
 public void testNewInstance_ClassWithOuterClassConstructor_ReturnsNewInstance() {
 Class<TestOuterClass> outerClass = TestOuterClass.class;
 Class<TestClass> testClass = TestClass.class;
 outerClassInstance = new TestOuterClass();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 TestClass instance = instantiator.newInstance(testClass);
 assertInstanceOf(TestClass.class, instance);
 assertNull(getInternalState(instance, "outerInstance"));
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstance_ClassWithNoArgConstructor_ThrowsExceptionWhenNullOuterClassInstance() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 Class<String> stringClass = String.class;
 instantiator.newInstance(stringClass);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstance_ClassWithNoArgConstructor_ThrowsExceptionWhenNoArgConstructorNotFound() {
 Class<TestClassWithNoArgConstructor> testClass = TestClassWithNoArgConstructor.class;
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 instantiator.newInstance(testClass);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstance_ClassWithParamConstructor_ThrowsExceptionWhenOuterClassInstanceIsNull() {
 Class<TestClassWithParamConstructor> testClass = TestClassWithParamConstructor.class;
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 instantiator.newInstance(testClass);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstance_ClassWithParamConstructor_ThrowsExceptionWhenOuterClassInstanceDoesNotMatchType() {
 Class<TestClassWithParamConstructor> testClass = TestClassWithParamConstructor.class;
 ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
 instantiator.newInstance(testClass);
 }

 @Test
 public void testWithOuterClass_ClassWithOuterClassConstructor_ReturnsNewInstance() {
 Class<TestOuterClass> outerClass = TestOuterClass.class;
 Class<TestClass> testClass = TestClass.class;
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 TestClass instance = instantiator.withOuterClass(testClass);
 assertInstanceOf(TestClass.class, instance);
 assertNull(getInternalState(instance, "outerInstance"));
 }

 @Test(expected = InstantiationException.class)
 public void testWithOuterClass_ClassWithNoArgConstructor_ThrowsExceptionWhenNullOuterClassInstance() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 Class<TestClass> testClass = TestClass.class;
 instantiator.withOuterClass(testClass);
 }

 @Test(expected = InstantiationException.class)
 public void testWithOuterClass_ClassWithNoArgConstructor_ThrowsExceptionWhenNoArgConstructorNotFound() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<TestClassWithNoArgConstructor> testClass = TestClassWithNoArgConstructor.class;
 instantiator.withOuterClass(testClass);
 }

 @Test(expected = InstantiationException.class)
 public void testWithOuterClass_ClassWithParamConstructor_ThrowsExceptionWhenOuterClassInstanceIsNull() {
 Class<TestClassWithParamConstructor> testClass = TestClassWithParamConstructor.class;
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 instantiator.withOuterClass(testClass);
 }

 @Test(expected = InstantiationException.class)
 public void testWithOuterClass_ClassWithParamConstructor_ThrowsExceptionWhenOuterClassInstanceDoesNotMatchType() {
 Class<TestClassWithParamConstructor> testClass = TestClassWithParamConstructor.class;
 ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
 instantiator.withOuterClass(testClass);
 }

 @Test
 public void testParamsException_ClassWithNoArgConstructor_ReturnsExpectedMessage() {
 Class<TestClassWithNoArgConstructor> testClass = TestClassWithNoArgConstructor.class;
 InstantiationException exception = ConstructorInstantiator.paramsException(testClass, new RuntimeException());
 String expectedMessage = "Unable to create mock instance of 'TestClassWithNoArgConstructor'.\nPlease ensure it has parameter-less constructor.";
 assertInstanceOf(InstantiationException.class, exception);
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testParamsException_ClassWithParamConstructor_ReturnsExpectedMessage() {
 Class<TestClassWithParamConstructor> testClass = TestClassWithParamConstructor.class;
 InstantiationException exception = ConstructorInstantiator.paramsException(testClass, new InstantiationException(""));
 String expectedMessage = "Unable to create mock instance of 'TestClassWithParamConstructor'.\nPlease ensure it has parameter-less constructor.";
 assertInstanceOf(InstantiationException.class, exception);
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testNoArgConstructor_ReturnsNewInstance() {
 Class<TestClassWithNoArgConstructor> testClass = TestClassWithNoArgConstructor.class;
 TestClassWithNoArgConstructor instance = ConstructorInstantiator.noArgConstructor(testClass);
 assertInstanceOf(TestClassWithNoArgConstructor.class, instance);
 }

 @Test(expected = InstantiationException.class)
 public void testNoArgConstructor_ThrowsExceptionWhenClassHasNoArgConstructorNotFound() {
 Class<TestClassWithNoArgConstructor> testClass = TestClassWithNoArgConstructor.class;
 ConstructorInstantiator.noArgConstructor(testClass);
 }
}