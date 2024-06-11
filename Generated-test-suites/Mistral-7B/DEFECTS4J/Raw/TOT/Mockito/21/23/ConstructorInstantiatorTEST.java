package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InstantiationStrategy;
import org.mockito.Mockito;
import org.mockito.exceptions.base.InstantiationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @Test
 public void testNewInstanceWithNullOuterClass() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 Class<DummyClass> cls = DummyClass.class;
 T result = instantiator.newInstance(cls);
 assertEquals(null, result);
 }

 @Test
 public void testNewInstanceWithArgConstructedClass() {
 outerClassInstance = mock(OuterClass.class, InstantiationStrategy.CALLS_REAL_CONSTRUCTOR);
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<DummyClass> cls = DummyClass.class;
 T result = instantiator.newInstance(cls);
 assertNotNull(result);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstanceWithClassHavingNoArgConstructorButThrowsException() {
 outerClassInstance = mock(OuterClass.class, InstantiationStrategy.CALLS_REAL_CONSTRUCTOR);
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<DummyClassThrowsException> cls = DummyClassThrowsException.class;
 instantiator.newInstance(cls);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstanceWithClassHavingNonArgConstructorButThrowsException() {
 outerClassInstance = mock(OuterClass.class, InstantiationStrategy.CALLS_REAL_CONSTRUCTOR);
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<DummyClassWithNoArgConstructor> cls = DummyClassWithNoArgConstructor.class;
 instantiator.newInstance(cls);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstanceWithClassHavingWrongOuterClassType() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(WrongOuterClassType.class, InstantiationStrategy.CALLS_REAL_CONSTRUCTOR));
 Class<DummyClass> cls = DummyClass.class;
 instantiator.newInstance(cls);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstanceWithClassHavingParameterizedConstructor() {
 outerClassInstance = mock(OuterClassWithParamConstructor.class, InstantiationStrategy.CALLS_REAL_CONSTRUCTOR);
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Class<DummyClassWithParamConstructor> cls = DummyClassWithParamConstructor.class;
 instantiator.newInstance(cls);
 }

 @Test
 public void testNewInstanceWithClassHavingParameterizedConstructor_WhenOuterClassInstanceIsProvided() {
 outerClassInstance = mock(OuterClassWithParamConstructor.class, InstantiationStrategy.CALLS_REAL_CONSTRUCTOR);
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 Object arg = new Object();
 Class<DummyClassWithParamConstructor> cls = DummyClassWithParamConstructor.class;
 T result = instantiator.newInstance(cls, arg);
 assertNotNull(result);
 }

 private static class DummyClass {}
 private static class DummyClassThrowsException extends Exception {}
 private static class DummyClassWithNoArgConstructor {}
 private static class DummyClassWithParamConstructor {
 DummyClassWithParamConstructor(Object arg) {}
 }
 private static class OuterClass {}
 private static class OuterClassWithParamConstructor {
 OuterClassWithParamConstructor(Object arg) {}
 }
 private static class WrongOuterClassType {}
}