package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.experimental.annotations.EdgeCases;
import org.mockito.exceptions.base.InstantiationException;

@EdgeCases
public class ConstructorInstantiatorTest {

 private Object outerInstance;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 outerInstance = mock(Object.class);
 }

 // Test newInstance method with valid arguments
 @Test
 public <T> void testNewInstanceWithValidArguments() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
 Class<T> cls = mock(Class.class);
 T instance = instantiator.newInstance(cls);
 assertNotNull(instance);
 }

 // Test noArgConstructor method
 @Test
 public <T> void testNoArgConstructor() {
 Class<T> cls = mock(Class.class);
 InstantiationException exception = assertThrows(InstantiationException.class, () -> ConstructorInstantiator.noArgConstructor(cls));
 assertEquals("Unable to create mock instance of 'Class<T>'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
 }

 // Test withOuterClass method with valid arguments
 @Test
 public <T> void testWithOuterClassWithValidArguments() {
 Class<T> cls = mock(Class.class);
 Constructor<T> constructor = mock(Constructor.class);
 when(cls.getDeclaredConstructor(outerInstance.getClass())).thenReturn(constructor);
 when(constructor.newInstance(outerInstance)).thenReturn(mock(T.class));
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
 T instance = instantiator.newInstance(cls);
 assertNotNull(instance);
 }

 // Test withOuterClass method with outerClassInstance as null
 @Test(expected = InstantiationException.class)
 public <T> void testWithOuterClassWithNullOuterInstance() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 Class<T> cls = mock(Class.class);
 instantiator.newInstance(cls);
 }

 // Test withOuterClass method with no-args constructor
 @Test
 public <T> void testWithOuterClassWithNoArgsConstructor() {
 Class<T> cls = mock(Class.class);
 Constructor<T> constructor = mock(Constructor.class);
 when(cls.getDeclaredConstructor()).thenReturn(constructor);
 when(constructor.newInstance()).thenReturn(mock(T.class));
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
 T instance = instantiator.newInstance(cls);
 assertNotNull(instance);
 }

 // Test withOuterClass method with constructor that throws exception
 @Test(expected = InstantiationException.class)
 public <T> void testWithOuterClassWithException() {
 Class<T> cls = mock(Class.class);
 Constructor<T> constructor = mock(Constructor.class);
 when(cls.getDeclaredConstructor(outerInstance.getClass())).thenThrow(new InstantiationException("Exception message"));
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
 instantiator.newInstance(cls);
 }

 // Test paramsException method
 @Test
 public <T> void testParamsException() {
 Class<T> cls = mock(Class.class);
 Constructor<T> constructor = mock(Constructor.class);
 when(constructor.newInstance(outerInstance)).thenReturn(mock(T.class));
 when(cls.getDeclaredConstructor(outerInstance.getClass())).thenReturn(constructor);
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
 InstantiationException exception = instantiator.paramsException(cls, new InstantiationException("Exception message"));
 assertEquals("Unable to create mock instance of 'Class<T>'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
 }

}