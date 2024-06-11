package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.Assert.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstance_NullOuterClassInstance_ReturnsNewInstanceWithoutOuterClass() {
     Class<String> stringClass = String.class;
     ConstructorInstantiator constructorInstantiator = null;
     try {
         constructorInstantiator = new ConstructorInstantiator(new Object());
     } catch (Exception e) {
         //Expected
     }
     String result = constructorInstantiator.newInstance(stringClass);
     assertNotNull(result);
     assertFalse(result instanceof ConstructorInstantiator);
     assertInstanceOf(String.class, result);
  }

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstance_NotNullOuterClassInstance_ReturnsNewInstanceWithOuterClass() {
     outerClassInstance = new Object();
     Class<String> stringClass = String.class;
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
     String result = constructorInstantiator.newInstance(stringClass);
     assertNotNull(result);
     assertInstanceOf(String.class, result);
     assertTrue(result instanceof org.mockito.internal.creation.instance.ConstructorInstantiator$1);
     assertEquals(outerClassInstance, ((org.mockito.internal.creation.instance.ConstructorInstantiator$1)result).outerInstance);
 }

 @Test(expected = InstantiationException.class)
 @SuppressWarnings("unchecked")
 public void testNewInstance_ClassWithNoArgConstructor_ThrowsExceptionWhenClassHasNoArgConstructor() {
     Class<String> stringClass = String.class;
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
     try {
         constructorInstantiator.newInstance(stringClass);
     } catch (InstantiationException e) {
         //Expected
         assertEquals("Unable to create mock instance of 'String'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
     }
 }

 @Test(expected = InstantiationException.class)
 @SuppressWarnings("unchecked")
 public void testNewInstance_ClassWithArgConstructor_ThrowsExceptionWhenClassHasArgConstructor() {
     Class<Pair> pairClass = Pair.class;
     Object outerClassInstance = new Object();
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
     try {
         constructorInstantiator.newInstance(pairClass);
     } catch (InstantiationException e) {
         //Expected
         assertEquals("Unable to create mock instance of 'Pair'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
     }
 }

 @Test(expected = InstantiationException.class)
 @SuppressWarnings("unchecked")
 public void testNewInstance_ClassWithThrowingConstructor_ThrowsCorrectExceptionMessage() {
     Class<ThrowingClass> throwingClass = ThrowingClass.class;
     Object outerClassInstance = new Object();
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
     try {
         constructorInstantiator.newInstance(throwingClass);
     } catch (InstantiationException e) {
         //Expected
         assertTrue(e.getCause() instanceof InvocationTargetException);
         InvocationTargetException ite = (InvocationTargetException) e.getCause();
         assertTrue(ite.getTargetException() instanceof RuntimeException);
         assertEquals("Exception message from ThrowingClass constructor", ite.getTargetException().getMessage());
     }
 }

 @Test(expected = InstantiationException.class)
 @SuppressWarnings("unchecked")
 public void testWithOuterClass_ClassWithNoArgConstructor_ThrowsExceptionWhenClassHasNoArgConstructor() {
     Class<String> stringClass = String.class;
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
     try {
         constructorInstantiator.withOuterClass(stringClass);
     } catch (InstantiationException e) {
         //Expected
         assertEquals("Unable to create mock instance of 'String'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
     }
 }

 @Test(expected = InstantiationException.class)
 @SuppressWarnings("unchecked")
 public void testWithOuterClass_ClassWithArgConstructor_ThrowsExceptionWhenClassHasArgConstructor() {
     Class<Pair> pairClass = Pair.class;
     Object outerClassInstance = new Object();
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
     try {
         constructorInstantiator.withOuterClass(pairClass);
     } catch (InstantiationException e) {
         //Expected
         assertEquals("Unable to create mock instance of 'Pair'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
     }
 }

 @Test(expected = InstantiationException.class)
 @SuppressWarnings("unchecked")
 public void testWithOuterClass_ClassWithThrowingConstructor_ThrowsCorrectExceptionMessage() {
     Class<ThrowingClass> throwingClass = ThrowingClass.class;
     Object outerClassInstance = new Object();
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
     try {
         constructorInstantiator.withOuterClass(throwingClass);
     } catch (InstantiationException e) {
         //Expected
         assertTrue(e.getCause() instanceof InvocationTargetException);
         InvocationTargetException ite = (InvocationTargetException) e.getCause();
         assertTrue(ite.getTargetException() instanceof RuntimeException);
         assertEquals("Exception message from ThrowingClass constructor", ite.getTargetException().getMessage());
     }
 }

 @Test(expected = InstantiationException.class)
 @SuppressWarnings("unchecked")
 public void testParamsException_HasCorrectMessage() {
     Class<String> stringClass = String.class;
     try {
         new ConstructorInstantiator(null).newInstance(stringClass);
     } catch (InstantiationException e) {
         ConstructorInstantiator.paramsException(stringClass, e);
         //Expected
         assertEquals("Unable to create mock instance of 'String'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
     }
 }

 @Test(expected = InstantiationException.class)
 @SuppressWarnings("unchecked")
 public void testParamsException_HasCorrectInnerException() {
     Class<String> stringClass = String.class;
     try {
         new ConstructorInstantiator(null).newInstance(stringClass);
     } catch (InstantiationException e) {
         try {
             ConstructorInstantiator.paramsException(stringClass, e);
         } catch (UndeclaredThrowableException ute) {
             Throwable cause = ute.getThrowable();
             //Expected
             assertTrue(cause instanceof InvocationTargetException);
             InvocationTargetException ite = (InvocationTargetException) cause;
             assertTrue(ite.getTargetException() instanceof InstantiationException);
         }
     }
 }

}