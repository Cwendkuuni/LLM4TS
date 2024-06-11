package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.experimental.annotations.EdgeCases;
import org.junit.experimental.annotations.ExpectedException;

@EdgeCases(Test.class)
public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @Test
 public void testNewInstance_NullOuterClassInstance_ReturnsInstanceFromNoArgConstructor() {
     Class<DummyClass> dummyClass = DummyClass.class;
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
     Object result = constructorInstantiator.newInstance(dummyClass);
     Object noArgInstance = ConstructorInstantiator.noArgConstructor(dummyClass);
     assertInstanceOf(DummyClass.class, result);
     assertEquals(noArgInstance, result);
 }

 @Test
 public void testNewInstance_ValidOuterClassInstance_ReturnsInstanceWithOuterClass() {
     DummyOuterClass outerClass = new DummyOuterClass();
     Class<DummyClass> dummyClass = DummyClass.class;
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClass);
     Object result = constructorInstantiator.newInstance(dummyClass);
     assertInstanceOf(DummyClass.class, result);
     assertEquals(outerClass, getInternalState(result, "outerClassInstance"));
 }

 @Test(expected = InstantiationException.class)
 @ExpectedException(InstantiationException.class)
 public void testNewInstance_InvalidClass_ThrowsException() {
     Class<String> stringClass = String.class;
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
     constructorInstantiator.newInstance(stringClass);
 }

 @Test(expected = InstantiationException.class)
 @ExpectedException(InstantiationException.class)
 public void testNewInstance_NoArgConstructorThrowsException_ThrowsException() {
     Class<DummyClassWithNoArgConstructorException> dummyClass = DummyClassWithNoArgConstructorException.class;
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
     constructorInstantiator.newInstance(dummyClass);
 }

 @Test(expected = InstantiationException.class)
 @ExpectedException(InstantiationException.class)
 public void testWithOuterClass_InvalidClass_ThrowsException() {
     Class<String> stringClass = String.class;
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
     constructorInstantiator.withOuterClass(stringClass);
 }

 @Test(expected = InstantiationException.class)
 @ExpectedException(InstantiationException.class)
 public void testWithOuterClass_NoDeclaredConstructor_ThrowsException() {
     Class<DummyClassWithNoDeclaredConstructor> dummyClass = DummyClassWithNoDeclaredConstructor.class;
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
     constructorInstantiator.withOuterClass(dummyClass);
 }

 @Test(expected = InstantiationException.class)
 @ExpectedException(InstantiationException.class)
 public void testWithOuterClass_ConstructorThrowsException_ThrowsException() {
     Class<DummyClassWithConstructorException> dummyClass = DummyClassWithConstructorException.class;
     Constructor<DummyClassWithConstructorException> constructor = null;
     try {
         constructor = dummyClass.getDeclaredConstructor(Object.class);
     } catch (NoSuchMethodException e) {
         throw new RuntimeException(e);
     }
     ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
     constructorInstantiator.withOuterClass(dummyClass);
 }

 @Test
 public void testParamsException_NullClass_ReturnsInstanceWithMessage() {
     Class<String> stringClass = null;
     InstantiationException exception = ConstructorInstantiator.paramsException(stringClass, new RuntimeException());
     String expectedMessage = "Unable to create mock instance of 'null'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";
     assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testParamsException_NullException_ReturnsInstanceWithMessage() {
     Class<String> stringClass = String.class;
     InstantiationException exception = ConstructorInstantiator.paramsException(stringClass, null);
     String expectedMessage = "Unable to create mock instance of 'String'.\nPlease ensure it has parameter-less constructor.";
     assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testNoArgConstructor_Success() {
     Class<DummyClass> dummyClass = DummyClass.class;
     Object result = ConstructorInstantiator.noArgConstructor(dummyClass);
     assertInstanceOf(DummyClass.class, result);
 }
}

 class DummyClass {
 }

 class DummyClassWithNoArgConstructorException {
 }

 class DummyClassWithNoDeclaredConstructor {
 }

 class DummyClassWithConstructorException {
 public DummyClassWithConstructorException(Object outerClassInstance) {
 }
 }

 class DummyOuterClass {
 }