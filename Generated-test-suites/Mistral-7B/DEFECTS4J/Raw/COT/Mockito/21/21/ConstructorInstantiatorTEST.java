package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMockException;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @SuppressWarnings("unchecked")
 @Test
 public <T> void testNewInstance_NullOuterClassInstance_ReturnsInstanceFromNoArgConstructor() {
 outerClassInstance = null;
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 T result = instantiator.newInstance(mock(Class.class));
 assertInstanceOf(mock(Class.class).getClass(), result.getClass());
 }

 @SuppressWarnings("unchecked")
 @Test
 public <T> void testNewInstance_NotNullOuterClassInstance_ReturnsInstanceFromConstructorWithOuterClass() {
 outerClassInstance = mock(String.class);
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<String> stringClass = String.class;
 T result = instantiator.newInstance(stringClass);
 assertInstanceOf(String.class, result);
 assertSame(outerClassInstance, result);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstance_ClassWithoutNoArgConstructor_ThrowsException() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
 Class<Throwable> throwableClass = Throwable.class;
 instantiator.newInstance(throwableClass);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstance_ClassWithoutConstructorWithOuterClass_ThrowsException() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
 Class<InnerClass> innerClass = new InnerClass();
 instantiator.newInstance(innerClass.getClass());
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstance_ClassWithThrowingConstructor_ThrowsException() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
 Class<ThrowingClass> throwingClass = ThrowingClass.class;
 assertThrows(InstantiationException.class, () -> instantiator.newInstance(throwingClass));
 }

 @Test
 public void testWithOuterClass_ClassWithThrowingConstructor_ThrowsExceptionFromConstructor() {
 Class<?> outerClass = mock(Object.class);
 Class<ThrowingClass> throwingClass = ThrowingClass.class;
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClass);
 assertThrows(InvocationOnMockException.class, () -> instantiator.withOuterClass(throwingClass));
 }

 @Test
 public void testWithOuterClass_ClassWithConstructorWithOuterClass_ReturnsCorrectInstance() {
 Class<?> outerClass = mock(Object.class);
 Class<OuterClassWithOuterConstructor> outerClassWithOuterConstructor = OuterClassWithOuterConstructor.class;
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClass);
 T result = instantiator.withOuterClass(outerClassWithOuterConstructor);
 assertInstanceOf(OuterClassWithOuterConstructor.class, result);
 assertSame(outerClass, result.getOuterClass());
 }

 @Test
 public void testParamsException_ClassWithExceptionInNoArgConstructor_ThrowsExpectedException() {
 Class<ThrowingClass> throwingClass = ThrowingClass.class;
 assertThrows(InstantiationException.class, () -> ConstructorInstantiator.paramsException(throwingClass, new Exception()));
 }

 @Test
 public void testParamsException_ClassWithExceptionInConstructorWithOuterClass_ThrowsExpectedException() {
 Class<?> outerClass = mock(Object.class);
 Class<ThrowingClassWithOuterConstructor> throwingClassWithOuterConstructor = ThrowingClassWithOuterConstructor.class;
 assertThrows(InstantiationException.class, () -> ConstructorInstantiator.paramsException(throwingClassWithOuterConstructor, new Exception()));
 }

 private static class InnerClass {
 }

 private static class OuterClassWithOuterConstructor {
 private final Object outerClass;

 public OuterClassWithOuterConstructor(Object outerClass) {
 this.outerClass = outerClass;
 }

 public Object getOuterClass() {
 return outerClass;
 }
 }

 private static class ThrowingClass {
 @SuppressWarnings("unused")
 private ThrowingClass() throws Exception {
 throw new Exception();
 }
 }

 private static class ThrowingClassWithOuterConstructor {
 private final Object outerClass;

 @SuppressWarnings("unused")
 private ThrowingClassWithOuterConstructor(Object outerClass) throws Exception {
 this.outerClass = outerClass;
 }

 public Object getOuterClass() {
 return outerClass;
 }
 }

}