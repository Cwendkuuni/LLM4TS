package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
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
 public void testConstructorWithNullOuterClassInstance() {
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
 assertThrows(IllegalArgumentException.class, () -> constructorInstantiator.newInstance(String.class));
 }

 @Test
 public void testNewInstanceWithNullClass() {
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
 assertThrows(NullPointerException.class, () -> constructorInstantiator.newInstance(null));
 }

 @Test
 public void testNewInstanceWithValidClassAndNullOuterClassInstance() {
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
 assertInstanceOf(String.class, constructorInstantiator.newInstance(String.class));
 }

 @Test
 public void testNewInstanceWithValidClassAndValidOuterClassInstance() {
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
 outerClassInstance = getInternalState(constructorInstantiator, "outerClassInstance");

 Class<String> stringClass = String.class;
 String expectedString = "testString";
 String resultString = constructorInstantiator.newInstance(stringClass);
 assertInstanceOf(String.class, resultString);
 assertInstanceOf(expectedString.getClass(), resultString.getClass());
 }

 @Test(expected = InstantiationException.class)
 public void testWithOuterClassThrowsExceptionWhenNoConstructorFound() {
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
 assertThrows(InstantiationException.class, () -> constructorInstantiator.withOuterClass(Object.class));
 }

 @Test(expected = InstantiationException.class)
 public void testWithOuterClassThrowsExceptionWhenInvalidConstructorSignature() {
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
 outerClassInstance = getInternalState(constructorInstantiator, "outerClassInstance");

 Class<Foo> fooClass = Foo.class;
 assertThrows(InstantiationException.class, () -> constructorInstantiator.withOuterClass(fooClass));
 }

 @Test(expected = InstantiationException.class)
 public void testNoArgConstructorThrowsExceptionWhenClassHasConstructor() {
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
 assertThrows(InstantiationException.class, () -> constructorInstantiator.noArgConstructor(Foo.class));
 }

 @Test(expected = InstantiationException.class)
 public void testNoArgConstructorThrowsExceptionWhenClassHasThrowingConstructor() {
 Class<Bar> barClass = Bar.class;
 try {
 Bar constructorInstance = ConstructorInstantiator.noArgConstructor(barClass);
 } catch (InstantiationException e) {
 throw new AssertionError("Unexpected InstantiationException", e);
 }

 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
 assertThrows(InstantiationException.class, () -> constructorInstantiator.noArgConstructor(barClass));
 }

 @Test(expected = InstantiationException.class)
 public void testWithOuterClassThrowsExceptionWhenConstructorThrowsException() throws Exception {
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
 outerClassInstance = getInternalState(constructorInstantiator, "outerClassInstance");

 Class<Baz> bazClass = Baz.class;
 Constructor<Baz> constructor = bazClass.getDeclaredConstructor(outerClassInstance.getClass());
 constructor.setAccessible(true);
 constructor.setExceptionOnThrow(new RuntimeException("Test exception"));

 assertThrows(InstantiationException.class, () -> constructorInstantiator.withOuterClass(bazClass));
 }
}