package org.mockito.internal.stubbing.defaultanswers.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

public class ReturnsSmartNullsTest {

 // Testing constructor and get serialVersionUID
 @Test
 public void testConstructor() {
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 assertEquals(7618312406617949441L, returnsSmartNulls.getSerialVersionUID());
 }

 // Testing answer method with null return value
 @Test(expected = org.mockito.exceptions.smart.SmartNullPointerException.class)
 public void testAnswerWithNullReturnValue() {
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Answer<Object> answer = mock(Answer.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.answer(invocation)).thenReturn(null);
 ReturnsSmartNulls smartNullsAnswer = new ReturnsSmartNulls();
 smartNullsAnswer.delegate = answer;
 Object result = smartNullsAnswer.answer(invocation);
 fail("An exception should have been thrown");
 }

 // Testing answer method with non-mockable return type
 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testAnswerWithNonMockableReturnType() {
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Answer<Object> answer = mock(Answer.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.answer(invocation)).thenReturn(new String("Test String"));
 ReturnsSmartNulls smartNullsAnswer = new ReturnsSmartNulls();
 smartNullsAnswer.delegate = answer;
 try {
 smartNullsAnswer.answer(invocation);
 fail("An exception should have been thrown");
 } catch (NullPointerException e) {
 // Expected NullPointerException as the String cannot be imposterised
 }
 }

 // Testing answer method with ordinary return value
 @Test
 public void testAnswerWithOrdinaryReturnValue() {
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Answer<Object> answer = mock(Answer.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.answer(invocation)).thenReturn("Test String");
 ReturnsSmartNulls smartNullsAnswer = new ReturnsSmartNulls();
 smartNullsAnswer.delegate = answer;
 String result = (String) smartNullsAnswer.answer(invocation);
 assertNotNull(result);
 }

 // Testing ThrowingInterceptor's formatMethodCall method
 @Test
 public void testFormatMethodCall() {
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMethod().getName()).thenReturn("testMethod");
 when(invocation.getArguments()).thenReturn(new Object[]{1, 2, 3});
 String result = returnsSmartNulls.new ThrowingInterceptor(invocation).formatMethodCall();
 String expected = "testMethod(1, 2, 3)";
 assertEquals(expected, result);
 }

 // Testing ThrowingInterceptor's intercept method with toString method
 @Test
 public void testInterceptWithToStringMethod() {
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Object[] args = new Object[]{1, 2, 3};
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMethod().getName()).thenReturn("toString");
 when(invocation.getArguments()).thenReturn(args);
 String result = (String) returnsSmartNulls.new ThrowingInterceptor(invocation).intercept(null, invocation.getMethod(), args, mock(MethodProxy.class));
 String expected = "SmartNull returned by unstubbed toString method on mock";
 assertEquals(expected, result.toString());
 }

 // Testing ThrowingInterceptor's intercept method with non-toString method
 @Test(expected = org.mockito.exceptions.smart.SmartNullPointerException.class)
 public void testInterceptWithNonToStringMethod() {
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Object[] args = new Object[]{1, 2, 3};
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMethod().getName()).thenReturn("testMethod");
 when(invocation.getArguments()).thenReturn(args);
 String result = (String) returnsSmartNulls.new ThrowingInterceptor(invocation).intercept(null, invocation.getMethod(), args, mock(MethodProxy.class));
 fail("An exception should have been thrown");
 }

}