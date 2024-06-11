package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.Assert.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.lang.reflect.Constructor;

public class ConstructorInstantiatorTest {

 private Object outerClassInstance; // Assuming this is provided in the test setup

 @Test
 public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
     Class<Object> cls = Object.class;
     try {
         ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
         Object result = instantiator.newInstance(cls);
         Assert.assertNull(result);
     } catch (InstantiationException e) {
         Assert.assertEquals("Unable to create mock instance of 'Object'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
     }
 }

 @Test
 public <T> void testNewInstanceWithNullOuterClassInstanceAndConstructedClassHasConstructor() {
     Class<SomeClass> cls = SomeClass.class;
     ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
     try {
         instantiator.newInstance(cls);
         fail("Expected InstantiationException");
     } catch (InstantiationException e) {
         // Expected exception
     }
 }

 @Test
 public <T> void testNewInstanceWithValidOuterClassInstanceAndParameterlessClass() {
     Class<Object> cls = Object.class;
     Object outerClassInstance = new Object();
     ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
     Object result = instantiator.newInstance(cls);
     Assert.assertNotNull(result);
 }

 @Test(expected = InstantiationException.class)
 public <T> void testWithOuterClassThrowsExceptionWithNullOuterClassInstance() {
     Class<SomeClass> cls = SomeClass.class;
     ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
     instantiator.withOuterClass(cls);
 }

 @Test(expected = InstantiationException.class)
 public <T> void testWithOuterClassThrowsExceptionWithNoConstructor() {
     Class<SomeClass> cls = SomeClass.class;
     Object outerClassInstance = new Object();
     ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
     try {
         instantiator.withOuterClass(cls);
         fail("Expected InstantiationException");
     } catch (InstantiationException e) {
         // Expected exception
     }
 }

 @Test(expected = InstantiationException.class)
 public <T> void testWithOuterClassThrowsExceptionWithWrongOuterClassType() {
     Class<SomeClass> cls = SomeClass.class;
     Object outerClassInstance = new DifferentClass();
     ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
     try {
         instantiator.withOuterClass(cls);
         fail("Expected InstantiationException");
     } catch (InstantiationException e) {
         // Expected exception
     }
 }

 @Test
 public <T> void testWithOuterClassWithValidConstructor() {
     Class<SomeClass> cls = SomeClass.class;
     Object outerClassInstance = new Object();
     Constructor<SomeClass> constructor = cls.getDeclaredConstructor(outerClassInstance.getClass());
     SomeClass expected = new SomeClass();
     Object result = new ConstructorInstantiator(outerClassInstance).withOuterClass(cls);
     assertEquals(expected, result);
 }

 @Test(expected = InvocationTargetException.class)
 public <T> void testWithOuterClassThrowsRuntimeExceptionFromConstructor() throws Exception {
     Class<SomeClass> cls = SomeClass.class;
     Object outerClassInstance = new Object();
     Constructor<SomeClass> constructor = cls.getDeclaredConstructor(outerClassInstance.getClass());
     constructor.setAccessible(true);
     constructor.setException(new RuntimeException("Test Exception"));
     ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
     try {
         instantiator.withOuterClass(cls);
         fail("Expected InvocationTargetException");
     } catch (InvocationTargetException e) {
         Assert.assertTrue(e.getCause() instanceof RuntimeException);
         Assert.assertEquals("Test Exception", e.getCause().getMessage());
     }
 }

 @Test(expected = UndeclaredThrowableException.class)
 public <T> void testWithOuterClassThrowsCheckedExceptionFromConstructor() throws Exception {
     Class<SomeClass> cls = SomeClass.class;
     Object outerClassInstance = new Object();
     Constructor<SomeClass> constructor = cls.getDeclaredConstructor(outerClassInstance.getClass());
     constructor.setAccessible(true);
     constructor.setException(new IOException("Test IOException"));
     ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
     try {
         instantiator.withOuterClass(cls);
         fail("Expected UndeclaredThrowableException");
     } catch (UndeclaredThrowableException e) {
         Assert.assertTrue(e.getCause() instanceof IOException);
         Assert.assertEquals("Test IOException", e.getCause().getMessage());
     }
 }

 @Test
 public <T> void testParamsExceptionWithNullClass() {
     ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
     try {
         instantiator.paramsException(null, new Exception());
         fail("Expected InstantiationException");
     } catch (InstantiationException e) {
         // Expected exception
     }
 }

 @Test
 public <T> void testParamsExceptionWithException() {
     Class<SomeClass> cls = SomeClass.class;
     ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
     InstantiationException exception = instantiator.paramsException(cls, new Exception());
     String expectedMessage = "Unable to create mock instance of 'SomeClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";
     String actualMessage = exception.getMessage();
     Assert.assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public <T> void testNoArgConstructorWithException() {
     Class<SomeClass> cls = SomeClass.class;
     ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
     try {
         instantiator.noArgConstructor(cls);
         fail("Expected InstantiationException");
     } catch (InstantiationException e) {
         String expectedMessage = "Unable to create mock instance of 'SomeClass'.\nPlease ensure it has parameter-less constructor.";
         String actualMessage = e.getMessage();
         Assert.assertEquals(expectedMessage, actualMessage);
     }
 }

}