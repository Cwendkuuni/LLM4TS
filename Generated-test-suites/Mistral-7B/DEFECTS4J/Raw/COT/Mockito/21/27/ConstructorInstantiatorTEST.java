package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.Assert.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInstantiatorTest {

 private Object outerClassInstance; // Assuming you provide a valid outerClassInstance for testing

 @Test
 public void testNewInstance_NullOuterClassInstance_ReturnsInstanceFromNoArgConstructor() {
     Class<SomeClass> cls = SomeClass.class;
     ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
     SomeClass actual = instantiator.newInstance(cls);
     SomeClass expected = ConstructorInstantiator.noArgConstructor(cls).newInstance();
     assertEquals(expected, actual);
 }

 @Test
 public void testNewInstance_ValidOuterClassInstance_ReturnsInstanceFromConstructorWithOuterClass() {
     outerClassInstance = new Object();
     Class<SomeClass> cls = SomeClass.class;
     ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
     SomeClass actual = instantiator.newInstance(cls);
     SomeClass expected = createSomeClassWithOuterInstance(outerClassInstance, cls);
     assertEquals(expected, actual);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstance_ClassWithNoArgConstructor_ThrowsExceptionWhenOuterClassInstanceIsNull() {
     Class<SomeClass> cls = SomeClass.class;
     ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
     instantiator.newInstance(cls);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstance_ClassWithConstructor_ThrowsExceptionWhenOuterClassInstanceIsNull() {
     Class<SomeClass> cls = SomeClassWithOuterClassConstructor.class;
     ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
     instantiator.newInstance(cls);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstance_ClassWithParamsConstructor_ThrowsExceptionWhenExceptionIsThrown() {
     Class<SomeClassWithExceptionConstructor> cls = SomeClassWithExceptionConstructor.class;
     ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
     try {
         instantiator.newInstance(cls);
     } catch (InstantiationException e) {
         throw new InstantiationException("Unexpected error.", e); // this test case checks if the expected InstantiationException is thrown
     }
 }

 @Test
 public void testParamsException_ClassWithNoArgConstructor_ReturnsExpectedMessage() {
     Class<SomeClass> cls = SomeClass.class;
     InstantiationException exception = ConstructorInstantiator.paramsException(cls, new InstantiationException(""));
     String expectedMessage = "Unable to create mock instance of 'SomeClass'.\nPlease ensure it has parameter-less constructor.";
     String actualMessage = exception.getMessage();
     assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testParamsException_ClassWithParamsConstructor_ReturnsExpectedMessage() {
     Class<SomeClassWithParamsConstructor> cls = SomeClassWithParamsConstructor.class;
     InstantiationException exception = ConstructorInstantiator.paramsException(cls, new InstantiationException(""));
     String expectedMessage = "Unable to create mock instance of 'SomeClassWithParamsConstructor'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";
     String actualMessage = exception.getMessage();
     assertEquals(expectedMessage, actualMessage);
 }

 private SomeClass createSomeClassWithOuterInstance(Object outerClassInstance, Class<SomeClass> cls) {
     try {
         Constructor<SomeClass> constructor = cls.getDeclaredConstructor(outerClassInstance.getClass());
         return constructor.newInstance(outerClassInstance);
     } catch (Exception e) {
         throw new RuntimeException(e); // use a RuntimeException here to avoid testing the exception handling mechanism inside ConstructorInstantiator
     }
 }

}