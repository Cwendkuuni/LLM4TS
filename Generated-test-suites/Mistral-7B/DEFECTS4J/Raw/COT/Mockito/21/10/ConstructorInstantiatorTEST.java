package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InstantiationException;

public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @Test
 public void testConstructorWithNullOuterClassInstance() {
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
 Assert.assertNotNull(constructorInstantiator.newInstance(String.class));
 }

 @Test
 public void testConstructorWithValidOuterClassInstanceAndParamelessClass() {
 outerClassInstance = new Object();
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
 String result = constructorInstantiator.newInstance(String.class);
 Assert.assertNotNull(result);
 Assert.assertEquals("", result.toString());
 }

 @Test
 public void testConstructorWithValidOuterClassInstanceAndConstructedClass() {
 outerClassInstance = new Object();
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
 Class<TestClass> testClass = TestClass.class;
 TestClass result = constructorInstantiator.newInstance(testClass);
 Assert.assertNotNull(result);
 }

 @Test(expected = InstantiationException.class)
 public void testConstructorWithNullClass() {
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
 constructorInstantiator.newInstance(null);
 }

 @Test(expected = InstantiationException.class)
 public void testConstructorWithNonParamelessClass() {
 outerClassInstance = new Object();
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
 Class<TestClassWithConstructor> testClass = TestClassWithConstructor.class;
 constructorInstantiator.newInstance(testClass);
 }

 @Test(expected = InstantiationException.class)
 public void testConstructorWithOuterClassInstanceOfDifferentType() {
 outerClassInstance = new Object();
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
 Class<TestClassWithDifferentOuterClass> testClass = TestClassWithDifferentOuterClass.class;
 constructorInstantiator.newInstance(testClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidClass() {
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
 constructorInstantiator.newInstance(String[].class);
 }

 @Test(expected = InvocationTargetException.class)
 public void testExceptionInConstructor() {
 outerClassInstance = new Object();
 Constructor<TestClassWithExceptionInConstructor> testClass = TestClassWithExceptionInConstructor.class;
 try {
 constructorInstantiator.withOuterClass(testClass);
 } catch (InstantiationException e) {
 throw e.getCause();
 }
 }

 @Test(expected = InstantiationException.class)
 public void testExceptionInNoArgConstructor() {
 try {
 ConstructorInstantiator.noArgConstructor(TestClassWithPrivateConstructor.class);
 } catch (IllegalAccessException e) {
 throw e;
 } catch (InvocationTargetException e) {
 throw e.getCause();
 } catch (InstantiationException e) {
 throw e;
 }
 }

 private static class TestClass {
 }

 private static class TestClassWithConstructor {
 TestClassWithConstructor(int i) {
 }
 }

 private static class TestClassWithDifferentOuterClass {
 TestClassWithDifferentOuterClass() {
 }
 }

 private static class TestClassWithExceptionInConstructor {
 TestClassWithExceptionInConstructor() throws Exception {
 throw new Exception("Exception in constructor");
 }
 }

 private static class TestClassWithPrivateConstructor {
 private TestClassWithPrivateConstructor() {
 }
 }

}