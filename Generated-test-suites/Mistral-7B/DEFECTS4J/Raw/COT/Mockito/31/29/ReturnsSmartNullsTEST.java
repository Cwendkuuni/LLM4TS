package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

import java.lang.reflect.Method;

public class ReturnsSmartNullsTest {

 private static final String FORMAT_METHOD_CALL = "formatMethodCall";

 @Test
 public void testAnswerWithDefaultReturnValue() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  Answer<Object> delegate = Mockito.spy(new ReturnsMoreEmptyValues());
  ReturnsSmartNulls testObject = new ReturnsSmartNulls() {
   {
    this.delegate = delegate;
   }
  };
  Object defaultReturnValue = new Object();

  InvocationOnMock invocation = Mockito.mock(Object.class);
  Method method = Mockito.method(Object.class, "someMethod");

  when(delegate.answer(invocation)).thenReturn(defaultReturnValue);

  Object result = testObject.answer(invocation);

  Assert.assertNotNull(result);
  Assert.assertEquals(defaultReturnValue, result);
 }

 @Test(expected = org.mockito.exceptions.smart.SmartNullPointerException.class)
 public void testAnswerWithNullReturnValue() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  InvocationOnMock invocation = Mockito.mock(Object.class);
  Method method = Mockito.method(Object.class, "someMethod");

  Object result = returnsSmartNulls.answer(invocation);
  Assert.assertNull(result);
 }

 @Test(expected = org.mockito.exceptions.smart.SmartNullPointerException.class)
 public void testToStringMethodCall() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  InvocationOnMock invocation = Mockito.mock(String.class);

  String result = (String) returnsSmartNulls.answer(invocation);
  Assert.assertEquals("SmartNull returned by unstubbed someMethod() method on mock", result);
 }

 @Test(expected = org.mockito.exceptions.smart.SmartNullPointerException.class)
 public void testAnswerWithNonMockableType() {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  InvocationOnMock invocation = Mockito.mock(Long.class);

  long result = (long) returnsSmartNulls.answer(invocation);
  Assert.assertEquals(0, result); // as null is returned for non-mockable types
 }

 @Test
 public void testAnswerWithThrowingInterceptor() throws Exception {
  ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
  InvocationOnMock invocation = Mockito.mock(Object.class);
  Method method = Mockito.method(Object.class, "someMethod", new Class[0]);

  MethodInterceptor interceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
  Object result = interceptor.intercept(null, method, new Object[0], null);

  Assert.assertNotNull(result);
  Assert.assertEquals("SmartNull returned by unstubbed someMethod() method on mock", result.toString());
 }

}