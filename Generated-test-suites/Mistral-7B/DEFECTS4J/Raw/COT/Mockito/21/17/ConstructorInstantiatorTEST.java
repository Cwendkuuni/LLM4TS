package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit. Assert;
import org.mockito.internal.exceptions.base.InstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 Assert.assertNotNull(instantiator.newInstance(String.class));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstanceWithNullOuterClassInstanceAndConstructorWithParameter() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 try {
 instantiator.newInstance(Object[].class);
 Assert.fail("Expected InstantiationException");
 } catch (InstantiationException e) {
 //Assertions pass if InstantiationException is thrown
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstanceWithValidOuterClassInstanceAndParameterlessClass() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Assert.assertNotNull(instantiator.newInstance(String.class));
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testNewInstanceWithValidOuterClassInstanceAndConstructorWithParameter() {
 outerClassInstance = new Object();
 Constructor<String[]> stringArrayConstructor = String[].class.getDeclaredConstructor(Object.class);
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 String[] result = (String[]) instantiator.newInstance(String[].class);
 Assert.assertNotNull(result);
 Assert.assertEquals(outerClassInstance.getClass(), result.getClass().getComponentType().getComponentType());
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testNewInstanceWithValidOuterClassInstanceAndNonExistentConstructor() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 instantiator.newInstance(Integer.class);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public void testNewInstanceWithNullOuterClassInstanceAndNonExistentConstructor() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 instantiator.newInstance(Integer.class);
 }

 @Test
 public void testParamsExceptionWithParameterlessClass() {
 try {
 ConstructorInstantiator.paramsException(String.class, new InstantiationException(""));
 Assert.fail("Expected no exception");
 } catch (InstantiationException e) {
 //Assertions pass if InstantiationException is thrown
 }
 }

 @Test
 public void testParamsExceptionWithNonParameterlessClass() {
 try {
 ConstructorInstantiator.paramsException(Integer.class, new InstantiationException(""));
 Assert.fail("Expected no exception");
 } catch (InstantiationException e) {
 //Assertions pass if InstantiationException is thrown
 }
 }

 @Test(expected = InstantiationException.class)
 public void testNoArgConstructorWithNonParameterlessClass() {
 try {
 ConstructorInstantiator.noArgConstructor(Integer.class);
 Assert.fail("Expected InstantiationException");
 } catch (InstantiationException e) {
 //Assertions pass if InstantiationException is thrown
 }
 }

 @Test
 public void testNoArgConstructorWithParameterlessClass() {
 String result = (String) ConstructorInstantiator.noArgConstructor(String.class);
 Assert.assertNotNull(result);
 }
}