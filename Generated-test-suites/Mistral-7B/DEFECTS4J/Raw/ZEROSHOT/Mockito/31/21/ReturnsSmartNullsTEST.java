package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object mock;

 @Before
 public void setUp() {
 returnsSmartNulls = new ReturnsSmartNulls();
 mock = mock(Object.class, returnsSmartNulls);
 }

 @Test
 public void testReturnsSmartNullForUnstubbedMethod() {
 InvocationOnMock invocation = Mockito.mock(new InvocationOnMock() {
 @Override
 public Object[] getArgs() {
 return new Object[0];
 }

 @Override
 public Method getMethod() {
 return Mockito.method(Object.class, "unstubbedMethod");
 }
 });

 Object result = returnsSmartNulls.answer(invocation);

 Assert.assertEquals("SmartNull returned by unstubbed unstubbedMethod() method on mock", result);
 }

 @Test
 public void testReturnsDefaultValueIfMethodReturnTypeIsNotMockable() {
 InvocationOnMock invocation = Mockito.mock(new InvocationOnMock() {
 @Override
 public Object[] getArgs() {
 return new Object[0];
 }

 @Override
 public Method getMethod() {
 return Mockito.method(Object.class, "unstubbedMethod", int.class);
 }
 });

 Object result = returnsSmartNulls.answer(invocation);

 Assert.assertNull(result);
 }

 @Test
 public void testReturnsDefaultValueIfMethodReturnsNull() {
 InvocationOnMock invocation = Mockito.mock(new InvocationOnMock() {
 @Override
 public Object[] getArgs() {
 return new Object[0];
 }

 @Override
 public Method getMethod() {
 return Mockito.method(Object.class, "returnsNull");
 }
 });

 Object result = returnsSmartNulls.answer(invocation);

 Assert.assertNull(result);
 }

 @Test
 public void testVerifiesCallToUnstubbedMethod() {
 ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
 Mockito.doCallRealMethod().when(mock).unstubbedMethod(Mockito.any());
 returnsSmartNulls = new ReturnsSmartNulls();

 mock.unstubbedMethod(Mockito.any());

 verify(mock).unstubbedMethod(argumentCaptor.capture());

 Assert.assertEquals("SmartNull", argumentCaptor.getValue());
 }

 @Test
 public void testReturnsDefaultValueIfMethodReturnsMoreEmptyValues() {
 ReturnsMoreEmptyValues returnsMoreEmptyValues = new ReturnsMoreEmptyValues();
 returnsSmartNulls = new ReturnsSmartNulls();

 InvocationOnMock invocation = Mockito.mock(new InvocationOnMock() {
 @Override
 public Object[] getArgs() {
 return new Object[0];
 }

 @Override
 public Method getMethod() {
 return Mockito.method(Object[].class, "methodThatReturnsEmptyArray");
 }
 });

 Object result = returnsSmartNulls.answer(invocation);

 Assert.assertArrayEquals(new Object[0], result);
 }
}