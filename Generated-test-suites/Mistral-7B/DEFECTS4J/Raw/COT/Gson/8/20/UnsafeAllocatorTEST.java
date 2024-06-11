package com.google.gson.internal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class UnsafeAllocatorTest {

 private UnsafeAllocator unsafeAllocator;

 @Before
 public void setup() {
 unsafeAllocator = UnsafeAllocator.create();
 }

 @Test
 public void testNewInstance_ValidClass() {
 Class<String> stringClass = String.class;
 String result = unsafeAllocator.newInstance(stringClass);
 assertNotNull(result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_InterfaceClass() {
 Class<Runnable> runnableClass = Runnable.class;
 unsafeAllocator.newInstance(runnableClass);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_AbstractClass() {
 Class<AbstractClass> abstractClass = AbstractClass.class;
 unsafeAllocator.newInstance(abstractClass);
 }

 @Test(expected = Exception.class)
 public void testNewInstance_ThrowingConstructor() throws Exception {
 Class<ThrowingClass> throwingClass = ThrowingClass.class;
 assertThrows(Exception.class, () -> unsafeAllocator.newInstance(throwingClass));
 }

 @Test(expected = Exception.class)
 public void testNewInstance_IllegalAccessException() throws Exception {
 Class<IllegalAccessClass> illegalAccessClass = IllegalAccessClass.class;
 Method constructor = illegalAccessClass.getDeclaredMethod("privateConstructor");
 constructor.setAccessible(true);
 assertThrows(IllegalAccessException.class, () -> unsafeAllocator.newInstance(illegalAccessClass));
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstance_InstantiationException() throws Exception {
 Class<InstantiationExceptionClass> instantiationExceptionClass = InstantiationExceptionClass.class;
 assertThrows(InstantiationException.class, () -> unsafeAllocator.newInstance(instantiationExceptionClass));
 }

 @Test(expected = NoSuchMethodException.class)
 public void testNewInstance_NoSuchMethodException() throws Exception {
 Class<NoSuchMethodExceptionClass> noSuchMethodExceptionClass = NoSuchMethodExceptionClass.class;
 assertThrows(NoSuchMethodException.class, () -> unsafeAllocator.newInstance(noSuchMethodExceptionClass));
 }

 @Test(expected = InvocationTargetException.class)
 public void testNewInstance_InvocationTargetException() throws Exception {
 Class<InvocationTargetExceptionClass> invocationTargetExceptionClass = InvocationTargetExceptionClass.class;
 Method constructor = invocationTargetExceptionClass.getDeclaredMethod("constructorWithCheckedException");
 constructor.setAccessible(true);
 InvocationTargetException exception = (InvocationTargetException) constructor.invoke(null);
 assertNotNull(exception.getCause());
 unsafeAllocator.newInstance(invocationTargetExceptionClass);
 }

 @Test(expected = UndeclaredThrowableException.class)
 public void testNewInstance_UndeclaredThrowableException() throws Exception {
 Class<UndeclaredThrowableExceptionClass> undeclaredThrowableExceptionClass = UndeclaredThrowableExceptionClass.class;
 Method constructor = undeclaredThrowableExceptionClass.getDeclaredMethod("constructorWithUncheckedException");
 constructor.setAccessible(true);
 UndeclaredThrowableException exception = (UndeclaredThrowableException) constructor.invoke(null);
 assertNotNull(exception.getThrowable());
 unsafeAllocator.newInstance(undeclaredThrowableExceptionClass);
 }

 @Test
 public void testNewInstance_NullClass() {
 assertThrows(NullPointerException.class, () -> unsafeAllocator.newInstance(null));
 }

}