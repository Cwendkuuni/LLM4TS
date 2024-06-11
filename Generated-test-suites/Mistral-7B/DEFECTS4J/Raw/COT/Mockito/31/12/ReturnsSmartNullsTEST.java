package org.mockito.internal.stubbing.defaultanswers.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object mockObject;

 @Before
 public void setUp() {
 mockObject = mock(Object.class, new ReturnsSmartNulls());
 returnsSmartNulls = Mockito.spy(mockObject);
 }

 @Test
 public void testAnswerNullableMethod() {
 when(mockObject.someMethod()).thenReturn("Some value");

 String result = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));

 assertEquals("Some value", result);
 }

 @Test(expected = org.mockito.exceptions.base.SmartNullPointerException.class)
 public void testAnswerNullableMethodThatReturnsNull() {
 when(mockObject.anotherMethod()).thenReturn(null);

 Object result = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));

 // This test will fail with NPE if ReturnsSmartNulls does not work correctly
 assertEquals(null, result);
 }

 @Test
 public void testAnswerMethodWithToString() throws Exception {
 Method method = mockObject.getClass().getMethod("toString");
 when(mockObject.toString()).thenCallRealMethod();

 Object result = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class, method));

 ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
 verify(Mockito.mock(Reporter.class)).smartNullPointerException(argumentCaptor.capture());

 assertEquals("SmartNull returned by unstubbed toString method on mock", argumentCaptor.getValue().getMessage());
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testAnswerMethodWithNonMockableType() throws Exception {
 when(mockObject.getClass().getMethod("finalMethod", String[].class)).thenThrow(new IllegalArgumentException());

 try {
 ClassImposterizer.INSTANCE.imposterise(new ThrowingInterceptor(Mockito.mock(InvocationOnMock.class)), mockObject.getClass());
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testAnswerMethodWithFinalClass() throws Exception {
 Class finalClass = finalizeObject.class;
 when(mockObject.getClass().getName()).thenReturn(finalClass.getName());
 when(finalClass.isFinal()).thenReturn(true);

 try {
 ClassImposterizer.INSTANCE.imposterise(new ThrowingInterceptor(Mockito.mock(InvocationOnMock.class)), finalClass);
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test
 public void testAnswerMethodWithThrowingInterceptor() throws Exception {
 Method method = mockObject.getClass().getMethod("throwingMethod", int.class);
 when(mockObject.throwingMethod(anyInt())).thenThrow(new RuntimeException());

 try {
 Object result = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class, method));
 fail("Should have thrown an exception");
 } catch (RuntimeException e) {
 // Expected
 }

 verify(mockObject).throwingMethod(anyInt());
 }

}