package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CallsRealMethodsTest {

 private CallsRealMethods answer;
 private Object target;

 @Before
 public void setup() {
 target = mock(TestClass.class);
 answer = new CallsRealMethods();
 }

 @Test
 public void testAnswerCallRealMethod() {
 TestClass testClass = (TestClass) target;
 ReflectUtil reflectUtil = mock(ReflectUtil.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(testClass);
 invocation.setMethod(Mockito.method(TestClass.class, "method1", ArgumentMatchers.anyInt()));

 when(reflectUtil.invokeMethod(ArgumentMatchers.eq(testClass), ArgumentMatchers.eq("method1"), ArgumentMatchers.any(Class.class), ArgumentMatchers.anyArray(Object.class))).thenReturn(new Object());

 Object result = answer.answer(invocation);

 assertEquals(new Object(), result);
 verify(testClass).method1(ArgumentMatchers.anyInt());
 }

 @Test(expected = Throwable.class)
 public void testAnswerCallRealMethodThrowsException() {
 TestClass testClass = (TestClass) target;
 ReflectUtil reflectUtil = mock(ReflectUtil.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(testClass);
 invocation.setMethod(Mockito.method(TestClass.class, "method2", ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble()));

 when(reflectUtil.invokeMethod(ArgumentMatchers.eq(testClass), ArgumentMatchers.eq("method2"), ArgumentMatchers.eq(double.class), ArgumentMatchers.anyArray(Object.class))).thenThrow(new RuntimeException());

 try {
 answer.answer(invocation);
 fail("Exception should be thrown");
 } catch (Throwable e) {
 assertEquals(RuntimeException.class, e.getClass());
 }

 verifyNoMoreInteractions(testClass);
 }

 @Test
 public void testAnswerCallRealMethodWithArguments() {
 TestClass testClass = (TestClass) target;
 ReflectUtil reflectUtil = mock(ReflectUtil.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(testClass);
 invocation.setMethod(Mockito.method(TestClass.class, "method3", ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()));
 Object arg1 = new Object();
 Object arg2 = new Object();

 when(reflectUtil.invokeMethod(ArgumentMatchers.eq(testClass), ArgumentMatchers.eq("method3"), ArgumentMatchers.eq(int.class), ArgumentMatchers.anyArray(Object.class))).thenReturn(arg1);

 Object result = answer.answer(invocation, new Object[]{arg2});

 assertEquals(arg1, result);
 verify(testClass).method3(ArgumentMatchers.eq(1), ArgumentMatchers.eq(arg2));
 }
}

class TestClass {
 public Object method1(int arg) {
 return null;
 }

 public Object method2(int arg1, double arg2) throws Exception {
 throw new RuntimeException();
 }

 public Object method3(int arg1, Object arg2) {
 return arg1;
 }
}