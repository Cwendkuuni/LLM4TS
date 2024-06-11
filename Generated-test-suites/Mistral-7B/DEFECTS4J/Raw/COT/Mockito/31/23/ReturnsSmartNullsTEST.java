package org.mockito.internal.stubbing.defaultanswers.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Method;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private ReturnsMoreEmptyValues returnsMoreEmptyValues;

 @Before
 public void setUp() {
 returnsSmartNulls = new ReturnsSmartNulls();
 returnsMoreEmptyValues = new ReturnsMoreEmptyValues();
 }

 // Test for the constructor
 @Test
 public void testReturnsSmartNullsConstructor() {
 ReturnsSmartNulls instance = new ReturnsSmartNulls();
 assertNotNull(instance);
 }

 // Test for the answer method that returns the delegated answer
 @Test
 public void testAnswerDelegatedAnswerIsNull() {
 InvocationOnMock invocationMock = mock(InvocationOnMock.class);
 when(invocationMock.getMethod()).thenReturn(new Method("testMethod", new Class[]{}));

 Object result = returnsSmartNulls.answer(invocationMock);
 assertNull(result);
 }

 // Test for the answer method that returns a smart null for unstubbed methods
 @Test(expected = org.mockito.exceptions.smart.SmartNullPointerException.class)
 public void testAnswerSmartNullForUnstubbedMethod() {
 Object mock = mock(String.class);
 when(mock.toString()).thenReturn(null);

 String smartNull = (String) returnsSmartNulls.answer(mock(MethodCall.class).argThat(invocation -> invocation.getMethod().getName().equals("toString")));
 assertEquals("SmartNull returned by unstubbed toString method on mock", smartNull);
 }

 // Test for the answer method that returns a smart null for a method with a non-mockable return type
 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testAnswerSmartNullForNonMockableReturnType() {
 Object mock = mock(String.class);
 when(mock.length()).thenReturn(null);

 String smartNull = (String) returnsSmartNulls.answer(mock(MethodCall.class).argThat(invocation -> invocation.getMethod().getName().equals("length")));
 fail("Expected SmartNullPointerException but got: " + smartNull);
 }

 // Test for the ThrowingInterceptor class
 @Test
 public void testThrowingInterceptor() {
 InvocationOnMock invocationMock = mock(InvocationOnMock.class);

 ThrowingInterceptor interceptor = new ReturnsSmartNulls.ThrowingInterceptor(invocationMock);
 Object result = interceptor.intercept(mock(Object.class), new Method(getClass().getName(), new Class[]{"MethodProxy", "Object[]", "Method"}), new Object[]{null, null, invocationMock}, new MethodProxy() {});
 assertNull(result);
 }

 // Test for the formatMethodCall method
 @Test
 public void testFormatMethodCall() {
 InvocationOnMock invocationMock = mock(InvocationOnMock.class);
 Method method = new Method("testMethod", new Class[]{});
 when(invocationMock.getMethod()).thenReturn(method);

 String result = returnsSmartNulls.new ThrowingInterceptor(invocationMock).formatMethodCall();
 assertEquals("testMethod", result);
 }
}