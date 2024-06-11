package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private MyMock mock;

 @Before
 public void setup() {
 returnsSmartNulls = new ReturnsSmartNulls();
 mock = mock(MyMock.class, returnsSmartNulls);
 }

 @Test
 public void testSmartNullReturnedByUnstubbedMethod() {
 given(mock.unstubbedMethod(ArgumentMatchers.anyInt()))
 .willCallRealMethod();

 String result = mock.unstubbedMethod(1);
 assertEquals("SmartNull returned by unstubbed unstubbedMethod(int) method on mock", result);
 }

 @Test(expected = org.mockito.exceptions.base.NullPointerException.class)
 public void testNullReturnedByUnstubbedMethod() {
 given(mock.unstubbedMethod(ArgumentMatchers.anyInt()))
 .willCallRealMethod();

 mock.unstubbedMethod(1).toString();
 }

 @Test
 public void testDelegateAnswerReturnsValue() {
 given(mock.delegateMethod(ArgumentMatchers.anyInt()))
 .willReturn("value");

 assertEquals("value", mock.delegateMethod(1));
 }

 @Test(expected = org.mockito.exceptions.base.NullPointerException.class)
 public void testDelegateAnswerReturnsNull() {
 given(mock.delegateMethod(ArgumentMatchers.anyInt()))
 .willReturn(null);

 mock.delegateMethod(1).toString();
 }

 @Test
 public void testReturnsSmartNullForNonMockableType() {
 given(mock.nonMockableMethod(ArgumentMatchers.anyInt()))
 .willCallRealMethod();

 assertEquals(null, mock.nonMockableMethod(1));
 }

 @Test
 public void testReturnsSmartNullForNullReturnType() {
 given(mock.nullReturnTypeMethod())
 .willCallRealMethod();

 assertEquals(null, mock.nullReturnTypeMethod());
 }
}

class MyMock {
 Object unstubbedMethod(int arg);
 Object delegateMethod(int arg);
 Object nonMockableMethod(int arg);
 Object nullReturnTypeMethod();
}