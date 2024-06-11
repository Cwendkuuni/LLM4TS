package org.mockito.internal.stubbing.defaultanswers.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;

 @Before
 public void setUp() {
 returnsSmartNulls = new ReturnsSmartNulls();
 }

 @Test
 public void testAnswer_ReturnsDefaultValue() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object defaultReturnValue = returnsSmartNulls.answer(invocation);
 assertNull(defaultReturnValue);
 }

 @Test
 public void testAnswer_ReturnsSmartNullForToString() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.doAnswer(returnsSmartNulls).when(invocation).answer(ArgumentMatchers.any());
 String result = (String) invocation.answer();
 assertEquals("SmartNull returned by unstubbed method on mock", result);
 }

 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testAnswer_ThrowsExceptionForUnstubbedMethod() throws Exception {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 returnsSmartNulls.answer(invocation);
 }

 @Test
 public void testAnswer_ReturnsNullForNonMockableType() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object nonMockableType = new Object();
 Mockito.doAnswer(returnsSmartNulls).when(invocation).answer(ArgumentMatchers.any());
 assertNull(invocation.answer((Class<?>) nonMockableType.getClass()));
 }

 @Test
 public void testAnswer_ReturnsSmartNullForUnstubbedMethodWithLocationInfo() throws Exception {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Class<?> unstubbedMethodClass = getClass().getDeclaredMethod("testAnswer_ThrowsExceptionForUnstubbedMethod").getDeclaringClass();
 Mockito.doAnswer(returnsSmartNulls).when(invocation).answer(ArgumentMatchers.any());
 assertNull(invocation.answer(unstubbedMethodClass.getMethod("testAnswer_ThrowsExceptionForUnstubbedMethod", new Class[0])));
 }
}