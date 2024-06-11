package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @Test
 public void testConstructorWithNullOuterClassInstanceAndNoArgConstructor() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 assertNull(instantiator.newInstance(String.class));
 }

 @Test
 public void testConstructorWithNullOuterClassInstanceAndConstructorWithParam() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 assertThrows(InstantiationException.class, () -> instantiator.newInstance(Number[].class));
 }

 @Test
 public void testConstructorWithNullOuterClassInstanceAndHasParameterlessConstructor() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 assertInstanceOf(Integer.class, instantiator.newInstance(Integer.class));
 }

 @Test
 public void testConstructorWithNotNullOuterClassInstanceAndNoArgConstructor() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 assertInstanceOf(Integer.class, instantiator.newInstance(Integer.class));
 }

 @Test
 public void testConstructorWithNotNullOuterClassInstanceAndConstructorWithParam() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Constructor<Number[]> constructor = Number[].class.getDeclaredConstructor(outerClassInstance.getClass());
 Number[] expected = new Number[]{1, 2, 3};
 Number[] actual = (Number[]) instantiator.newInstance(Number[].class);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = InstantiationException.class)
 public void testConstructorWithNotNullOuterClassInstanceAndNoSuchConstructor() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 instantiator.newInstance(String[].class);
 }

 @Test
 public void testConstructorWithNotNullOuterClassInstanceAndInvalidClass() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 assertNull(instantiator.newInstance(Object.class));
 }

 @Test
 public void testParamsException() {
 Class<String> cls = String.class;
 InstantiationException e = new InstantiationException("message", new RuntimeException());
 InstantiationException actual = ConstructorInstantiator.paramsException(cls, e);
 assertInstanceOf(InstantiationException.class, actual);
 assertInstanceOf(RuntimeException.class, actual.getCause());
 assertFalse(actual.getMessage().isEmpty());
 }

 @Test
 public void testNoArgConstructorException() {
 Class<String> cls = String.class;
 InstantiationException e = new InstantiationException("message", new RuntimeException());
 InstantiationException actual = ConstructorInstantiator.noArgConstructor(cls);
 assertInstanceOf(InstantiationException.class, actual);
 assertInstanceOf(RuntimeException.class, actual.getCause());
 assertFalse(actual.getMessage().isEmpty());
 }

}