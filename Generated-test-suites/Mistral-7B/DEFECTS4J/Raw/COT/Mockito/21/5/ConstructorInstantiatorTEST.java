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
 public void testNewInstance_WithNullOuterClassInstance_ReturnsInstanceFromNoArgConstructor() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 Class<String> stringClass = String.class;
 String instance = instantiator.newInstance(stringClass);
 Assert.assertNotNull(instance);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstance_WithNullOuterClassInstance_ThrowsExceptionForClassWithoutNoArgConstructor() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 Class<NoArgConstructorException> exceptionClass = NoArgConstructorException.class;
 Assert.assertThrows(InstantiationException.class, () -> instantiator.newInstance(exceptionClass));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstance_WithNonNullOuterClassInstance_ReturnsInstanceFromConstructorWithOuterClass() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<WithOuterClass> withOuterClass = WithOuterClass.class;
 WithOuterClass instance = instantiator.newInstance(withOuterClass);
 Assert.assertNotNull(instance);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testNewInstance_WithNonNullOuterClassInstance_ThrowsExceptionForClassWithNoConstructor() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<NoConstructorClass> noConstructorClass = NoConstructorClass.class;
 instantiator.newInstance(noConstructorClass);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testNewInstance_WithNonNullOuterClassInstance_ThrowsParamsException() {
 outerClassInstance = new Object();
 Class<WithParamsExceptionClass> withParamsExceptionClass = WithParamsExceptionClass.class;
 Constructor<WithParamsExceptionClass> constructor = null;
 try {
 constructor = withParamsExceptionClass.getDeclaredConstructor(outerClassInstance.getClass());
 } catch (NoSuchMethodException e) {
 // expected
 }
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 try {
 instantiator.withOuterClass(withParamsExceptionClass);
 fail("Should have thrown an InstantiationException.");
 } catch (InstantiationException e) {
 // expected
 }
 }

 private static class NoArgConstructorException {}

 private static class WithOuterClass {}

 private static class NoConstructorClass {}

 private static class WithParamsExceptionClass {
 private final Object outerClassInstance;

 public WithParamsExceptionClass(Object outerClassInstance) {
 this.outerClassInstance = outerClassInstance;
 }
 }
}