package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class CallsRealMethodsTest {

 private CallsRealMethods answer;
 private Object targetObject;

 @Before
 public void setup() {
 answer = new CallsRealMethods();
 targetObject = mock(TestClass.class);
 }

 @Test
 public void testAnswer_nullArgument_returnsNull() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 assertEquals(null, answer.answer(invocation));
 }

 @Test
 public void testAnswer_validArgument_returnsRealMethodResult() {
 when(targetObject.method1(ArgumentMatchers.anyInt())).thenReturn(1);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock().method1(1);
 Mockito.verify(targetObject).method1(ArgumentMatchers.eq(1));
 assertEquals(1, answer.answer(invocation));
 }

 @Test(expected = Throwable.class)
 public void testAnswer_throwsException_propagatesException() throws Throwable {
 when(targetObject.method2(ArgumentMatchers.anyInt())).thenThrow(new RuntimeException());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock().method2(1);
 Mockito.verify(targetObject).method2(ArgumentMatchers.eq(1));
 answer.answer(invocation);
 }

 @Test
 public void testAnswer_staticMethod_callsRealMethod() {
 when(TestClass.staticMethod1()).thenReturn("expectedResult");
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock().staticMethod1();
 Mockito.verify(TestClass.class).staticMethod1();
 assertEquals("expectedResult", answer.answer(invocation));
 }

 @Test
 public void testAnswer_voidMethod_returnsNull() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 answer.answer(invocation);
 // assertNull is used to check that no exception was thrown
 Mockito.verifyNoMoreInteractions(targetObject);
 assertNull(answer.answer(invocation));
 }

 @Test
 public void testAnswer_methodWithArrayArgument_callsRealMethod() {
 Object[] expectedArray = new Object[1];
 expectedArray[0] = "expectedResult";
 when(targetObject.method3(ArgumentMatchers.any())).thenReturn(expectedArray);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object[] actualArray = (Object[]) invocation.getMock().method3(new Object[]{new Object()});
 Mockito.verify(targetObject).method3(ArgumentMatchers.eq(new Object[]{new Object()}));
 assertEquals(expectedArray, answer.answer(invocation));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAnswer_invalidArgumentType_throwsIllegalArgumentException() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 answer.answer(invocation);
 // assertThrows is used to check that an exception is thrown
 Mockito.verifyNoMoreInteractions(targetObject);
 Mockito.verifyNoInteractions(invocation.getMock());
 assertThrows(IllegalArgumentException.class, () -> answer.answer(invocation));
 }
}