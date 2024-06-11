package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.reflection.Whitebox.invokeConstructor;
import static org.mockito.internal.util.reflection.Whitebox.newInstance;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.exceptions.base.InstantiationException;

@Category(org.mockito.internal.junit.JMockito)
public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstance_ParameterlessConstructor() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 Class<String> stringClass = String.class;
 String instance = instantiator.newInstance(stringClass);
 assertEquals("", instance.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstance_WithOuterClassInstance_ParameterlessConstructor() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<String> stringClass = String.class;
 String instance = instantiator.newInstance(stringClass);
 assertEquals("", instance.toString());
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testNewInstance_WithOuterClassInstance_ConstructorWithOneArgument() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<String> stringClass = String.class;
 Constructor<String> constructor = stringClass.getDeclaredConstructor(String.class);
 invokeConstructor(constructor, instantiator, "Test");
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testNewInstance_WithNullOuterClassInstance() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 Class<String> stringClass = String.class;
 assertThrows(InstantiationException.class, () -> instantiator.newInstance(stringClass));
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testNewInstance_WithClassWithoutNoArgConstructor() {
 Class<NoArgConstructorException> exceptionClass = NoArgConstructorException.class;
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 assertThrows(InstantiationException.class, () -> instantiator.newInstance(exceptionClass));
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testWithOuterClass_WithClassWithoutOneArgConstructor() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<NoArgConstructorException> exceptionClass = NoArgConstructorException.class;
 assertThrows(InstantiationException.class, () -> instantiator.withOuterClass(exceptionClass));
 }

 @Test
 public void testParamsException_WithNullClass() {
 assertNull(ConstructorInstantiator.paramsException(null, new Exception()));
 }

 @Test
 public void testParamsException_WithNullException() {
 Class<String> stringClass = String.class;
 assertThrows(NullPointerException.class, () -> ConstructorInstantiator.paramsException(stringClass, null));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testNoArgConstructor_WithClassWithoutNoArgConstructor() {
 Class<NoArgConstructorException> exceptionClass = NoArgConstructorException.class;
 assertThrows(InstantiationException.class, () -> ConstructorInstantiator.noArgConstructor(exceptionClass));
 }

}

 class NoArgConstructorException {

 }