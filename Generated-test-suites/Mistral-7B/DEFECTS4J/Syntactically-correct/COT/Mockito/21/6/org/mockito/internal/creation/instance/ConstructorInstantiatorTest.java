package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.reflection.Whitebox.getInnerException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.experimental.annotations.EdgeCases;
import org.mockito.exceptions.base.InstantiationException;

@EdgeCases
public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @Test
 public void testConstructorWithNullOuterInstance() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 InstantiationException exception = assertThrows(InstantiationException.class, () -> instantiator.newInstance(String.class));
 assertTrue(exception.getMessage().contains("parameter-less constructor."));
 }

 @Test
 public void testNewInstanceWithNullClass() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
 assertNull(instantiator.newInstance(null));
 }

 @Test
 public void testNewInstanceWithValidClass() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
 String instance = instantiator.newInstance(String.class);
 assertInstanceOf(String.class, instance);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstanceWithClassWithoutNoArgConstructor() throws Exception {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
 Class<Throwable> cls = Throwable.class;
 instantiator.newInstance(cls);
 }

 @Test
 public void testWithOuterClassWithValidClass() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
 Class<String> cls = String.class;
 String instance = instantiator.newInstance(cls);
 assertInstanceOf(String.class, instance);
 }

 @Test(expected = InstantiationException.class)
 public void testWithOuterClassWithClassWithoutOuterClassConstructor() throws Exception {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
 Class<Throwable> cls = Throwable.class;
 instantiator.withOuterClass(cls);
 }

 @Test
 public void testParamsException() {
 InstantiationException exception = ConstructorInstantiator.paramsException(String.class, new RuntimeException());
 assertTrue(exception.getMessage().contains("Unable to create mock instance of 'String'."));
 }

 @Test
 public void testNoArgConstructor() {
 String instance = ConstructorInstantiator.noArgConstructor(String.class);
 assertInstanceOf(String.class, instance);
 }

 @Test(expected = InstantiationException.class)
 public void testNoArgConstructorWithClassWithoutNoArgConstructor() throws Exception {
 InstantiationException exception = assertThrows(InstantiationException.class, () -> ConstructorInstantiator.noArgConstructor(Throwable.class));
 assertTrue(exception.getMessage().contains("parameter-less constructor."));
 }

 @Test
 public void testConstructorWithOuterClassInstanceAndClassWithOuterClassConstructor() {
 Object outerInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
 Class<StringBuilder> cls = StringBuilder.class;
 StringBuilder instance = instantiator.newInstance(cls);
 assertInstanceOf(StringBuilder.class, instance);
 }

 @Test(expected = InstantiationException.class)
 public void testConstructorWithOuterClassInstanceAndClassWithoutOuterClassConstructor() throws Exception {
 Object outerInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
 Class<Throwable> cls = Throwable.class;
 instantiator.newInstance(cls);
 }

 @Test
 public void testConstructorWithOuterClassInstanceAndClassWithTwoArgConstructor() {
 Object outerInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
 Class<TwoArgsConstructor> cls = TwoArgsConstructor.class;
 TwoArgsConstructor instance = (TwoArgsConstructor) instantiator.newInstance(cls);
 assertInstanceOf(TwoArgsConstructor.class, instance);
 }

 @Test(expected = InstantiationException.class)
 public void testConstructorWithOuterClassInstanceAndClassWithWrongOuterClass() throws Exception {
 Object outerInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
 Class<StringBuilder> cls = StringBuilder.class;
 instantiator.withOuterClass(String.class);
 }

 @Test
 public void testsWithOuterClassWithClassWithTwoArgConstructor() {
 Object outerInstance = new Object();
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
 Class<TwoArgsConstructor> cls = TwoArgsConstructor.class;
 TwoArgsConstructor instance = instantiator.newInstance(cls);
 assertInstanceOf(TwoArgsConstructor.class, instance);
 }

 private static class TwoArgsConstructor {
 TwoArgsConstructor(int i, int j) {}
 }

}