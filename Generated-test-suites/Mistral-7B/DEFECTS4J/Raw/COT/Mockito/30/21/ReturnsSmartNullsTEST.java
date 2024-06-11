package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object mock;

 @Before
 public void setup() {
 returnsSmartNulls = new ReturnsSmartNulls();
 mock = mock(Object.class, returnsSmartNulls);
 }

 @Test
 public void testAnswerNullMethod() {
 assertNull(returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class)));
 }

 @Test
 public void testAnswerNotNullMethod() {
 Answer<Object> answer = Mockito.mock(Answer.class);
 Answer<Object> delegate = Mockito.spy(new ReturnsMoreEmptyValues());
 ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
 smartNulls.delegate = delegate;
 Object result = smartNulls.answer(Mockito.mock(InvocationOnMock.class));
 assertNotNull(result);
 }

 @Test
 public void testAnswerNotMockableType() {
 assertNull(returnsSmartNulls.answer(Mockito.mock(String.class)));
 }

 @Test(expected = NullPointerException.class)
 public void testAnswerToStringMethod() {
 returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class, ArgumentMatchers.any(Method.class)));
 }

 @Test(expected = NullPointerException.class)
 public void testInterceptorToStringMethod() {
 new ReturnsSmartNulls().new ThrowingInterceptor(Mockito.mock(InvocationOnMock.class));
 }

 @Test
 public void testInterceptorNotToStringMethod() {
 new ReturnsSmartNulls().new ThrowingInterceptor(Mockito.mock(InvocationOnMock.class, ArgumentMatchers.any(Method.class)));
 }
}