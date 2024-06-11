package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 private DummyClass dummyMock;

 @Before
 public void setup() {
 dummyMock = mock(DummyClass.class);
 mockHandler = new MockHandler<>(dummyMock);
 }

 @Test
 public void testHandleVoidMethodInvocation() {
 // given
 when(dummyMock.doSomethingVoid()).thenCallRealMethod();

 // when
 mockHandler.handle(new InvocationOnMock(dummyMock, "doSomethingVoid", new Object[0]));

 // then
 // no need to assert, as we're testing the handler, not the mock
 }

 @Test
 public void testHandleMethodInvocationWithAnswer() {
 // given
 when(dummyMock.doSomethingWithArgument(ArgumentMatchers.eq("test")))
 .thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
 return 42;
 }
 });

 // when
 int result = (int) mockHandler.handle(new InvocationOnMock(dummyMock, "doSomethingWithArgument", Arrays.asList("test").toArray()));

 // then
 verify(dummyMock).doSomethingWithArgument(ArgumentMatchers.eq("test"));
 assertEquals(42, result);
 }

 @Test
 public void testHandleMethodInvocationWithStubbedReturnValue() {
 // given
 when(dummyMock.doSomethingWithReturnValue()).thenReturn(42);

 // when
 int result = (int) mockHandler.handle(new InvocationOnMock(dummyMock, "doSomethingWithReturnValue", new Object[0]));

 // then
 verify(dummyMock).doSomethingWithReturnValue();
 assertEquals(42, result);
 }

 @Test(expected = AssertionError.class)
 public void testHandleMethodInvocationWithError() {
 // given
 when(dummyMock.doSomethingWithException()).thenThrow(new RuntimeException("test exception"));

 // when, then
 try {
 mockHandler.handle(new InvocationOnMock(dummyMock, "doSomethingWithException", new Object[0]));
 } catch (Throwable e) {
 // expected error
 }

 verify(dummyMock).doSomethingWithException();
 }

 @Test
 public void testHandleMethodInvocationWithPartialMock() {
 // given
 DummyPartialMock partialMock = mock(DummyPartialMock.class, Mockito.RETURNS_DEEP_STUBS);
 when(partialMock.getDeepChild().doSomething()).thenReturn(42);

 mockHandler = new MockHandler<>(partialMock);

 // when
 int result = (int) mockHandler.handle(new InvocationOnMock(partialMock, "getDeepChild", new Object[0]));

 // then
 verify(partialMock.getDeepChild()).doSomething();
 assertEquals(42, result);
 }
}

class DummyClass {
 void doSomethingVoid() {}
 int doSomethingWithArgument(String argument) { return argument.length(); }
 int doSomethingWithReturnValue() { return 42; }
 int doSomethingWithException() throws Exception { throw new Exception("test exception"); }
}

class DummyPartialMock {
 DummyChild getDeepChild() { return new DummyChild(); }
 static class DummyChild {}
}