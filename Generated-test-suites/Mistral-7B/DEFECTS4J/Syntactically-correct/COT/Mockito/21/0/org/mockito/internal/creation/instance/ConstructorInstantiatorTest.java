package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstance_NullOuterClassInstance_ReturnsNoArgConstructor() {
     Class<ConstructorInstantiator> constructorInstantiatorClass = ConstructorInstantiator.class;
     Class<Integer> intClass = Integer.class;
     ConstructorInstantiator constructorInstantiator = null;
     try {
         constructorInstantiator = new ConstructorInstantiator(null);
         Integer intObject = constructorInstantiator.newInstance(intClass);
         Assert.assertNotNull(intObject);
     } catch (Exception e) {
         Assert.fail("Exception should not be thrown: " + e.getMessage());
     }
     Assert.assertEquals(noArgConstructor(intClass), intObject);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstance_NotNullOuterClassInstance_ReturnsConstructorWithOuterClass() {
     outerClassInstance = new Object();
     Class<Object> objectClass = Object.class;
     Class<String> stringClass = String.class;
     try {
         ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
         String stringObject = constructorInstantiator.newInstance(stringClass);
         Assert.assertNotNull(stringObject);
         Assert.assertSame(outerClassInstance, stringObject.getClass().getDeclaredField("outerClassInstance").get(stringObject));
     } catch (Exception e) {
         Assert.fail("Exception should not be thrown: " + e.getMessage());
     }
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testNewInstance_ClassWithNoArgConstructor_ThrowsException() {
     Class<ConstructorInstantiator> constructorInstantiatorClass = ConstructorInstantiator.class;
     Class<String> stringClass = String.class;
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
     try {
         constructorInstantiator.newInstance(stringClass);
         Assert.fail("Exception should have been thrown.");
     } catch (InstantiationException e) {
         // expected
     }
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testNewInstance_ClassWithParamConstructorAndNullOuterClassInstance_ThrowsException() {
     Class<ConstructorInstantiator> constructorInstantiatorClass = ConstructorInstantiator.class;
     Class<String> stringClass = String.class;
     Class<Object> objectClass = Object.class;
     try {
         ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
         Constructor<String> constructor = stringClass.getDeclaredConstructor(objectClass);
         constructor.setAccessible(true);
         constructorInstantiator.newInstance(stringClass);
         Assert.fail("Exception should have been thrown.");
     } catch (InstantiationException e) {
         // expected
     }
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testNewInstance_ClassWithParamConstructorAndNotNullOuterClassInstance_ThrowsException() {
     outerClassInstance = new Object();
     Class<ConstructorInstantiator> constructorInstantiatorClass = ConstructorInstantiator.class;
     Class<String> stringClass = String.class;
     Class<Object> objectClass = Object.class;
     Constructor<String> constructor = stringClass.getDeclaredConstructor(objectClass);
     constructor.setAccessible(true);
     try {
         ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
         constructorInstantiator.newInstance(stringClass);
         Assert.fail("Exception should have been thrown.");
     } catch (InstantiationException e) {
         // expected
     }
 }

 @Test
 public void testParamsException() {
     Class<ConstructorInstantiator> constructorInstantiatorClass = ConstructorInstantiator.class;
     Class<String> stringClass = String.class;
     try {
         new ConstructorInstantiator(null).newInstance(stringClass);
         Assert.fail("Exception should have been thrown.");
     } catch (InstantiationException e) {
         InstantiationException expectedException = new InstantiationException("Unable to create mock instance of 'String'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", new Exception());
         Assert.assertEquals("Unable to create mock instance of 'String'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", expectedException.getMessage(), e.getMessage());
     }
 }

 @Test
 public void testParamsException_WithInnerException() {
     Class<ConstructorInstantiator> constructorInstantiatorClass = ConstructorInstantiator.class;
     Class<String> stringClass = String.class;
     try {
         new ConstructorInstantiator(null).newInstance(stringClass);
     } catch (InstantiationException e) {
         try {
             throw e.getCause();
         } catch (UndeclaredThrowableException ute) {
             InstantiationException innerException = (InstantiationException) ute.getException();
             InstantiationException expectedException = new InstantiationException("Unable to create mock instance of 'String'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", new Exception("Inner exception message."));
             Assert.assertEquals("Unable to create mock instance of 'String'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", expectedException.getMessage(), innerException.getMessage());
         }
     }
 }

 private static <T> Object noArgConstructor(Class<T> cls) {
     return cls.newInstance();
 }
}