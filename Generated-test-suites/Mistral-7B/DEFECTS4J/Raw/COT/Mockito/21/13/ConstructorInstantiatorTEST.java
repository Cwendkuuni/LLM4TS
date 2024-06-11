package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InstantiationException;

public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstance_NullOuterClassInstance_ReturnsNoArgConstructor() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 Class<String> stringClass = String.class;
 String result = instantiator.newInstance(stringClass);
 Assert.assertNotNull(result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstance_NotNullOuterClassInstance_ReturnsWithOuterClassConstructor() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<TestClass> testClass = TestClass.class;
 TestClass result = instantiator.newInstance(testClass);
 Assert.assertNotNull(result);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testWithOuterClass_ClassWithoutNoArgConstructor_ThrowsInstantiationException() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<TestClassWithNoArgConstructor> testClassWithNoArgConstructor = TestClassWithNoArgConstructor.class;
 instantiator.withOuterClass(testClassWithNoArgConstructor);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testWithOuterClass_ClassWithParamsConstructor_ThrowsInstantiationException() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<TestClassWithParamsConstructor> testClassWithParamsConstructor = TestClassWithParamsConstructor.class;
 instantiator.withOuterClass(testClassWithParamsConstructor);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testWithOuterClass_ClassWithThrowingConstructor_ThrowsInstantiationException() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<TestClassWithThrowingConstructor> testClassWithThrowingConstructor = TestClassWithThrowingConstructor.class;
 try {
 instantiator.withOuterClass(testClassWithThrowingConstructor);
 } catch (InvocationTargetException e) {
 throw new InstantiationException("Unexpected exception while creating an instance.", e.getCause());
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testParamsException_NullClass_ThrowsNullPointerException() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 try {
 instantiator.paramsException(null, new Exception("test"));
 } catch (InstantiationException e) {
 Assert.assertEquals("Unexpected InstantiationException message.", "Unable to create mock instance of null. Please ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testParamsException_ClassWithThrowingConstructor_ThrowsInstantiationException() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<TestClassWithThrowingConstructor> testClassWithThrowingConstructor = TestClassWithThrowingConstructor.class;
 try {
 instantiator.paramsException(testClassWithThrowingConstructor, new Exception("test"));
 } catch (InstantiationException e) {
 Assert.assertEquals("Unexpected InstantiationException message.", "Unable to create mock instance of 'TestClassWithThrowingConstructor'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
 }
}

 class TestClass {
 }

 class TestClassWithNoArgConstructor {
 TestClassWithNoArgConstructor() {
 }
 }

 class TestClassWithParamsConstructor {
 TestClassWithParamsConstructor(int i) {
 }
 }

 class TestClassWithThrowingConstructor {
 TestClassWithThrowingConstructor() throws Exception {
 throw new Exception();
 }
 }

}