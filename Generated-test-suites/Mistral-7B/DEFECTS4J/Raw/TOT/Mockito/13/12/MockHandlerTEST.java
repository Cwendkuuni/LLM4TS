package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import static org.mockito.Mockito.*; // for static imports

public class MockHandlerTest {

 private MockHandler<MockitoTestClass> mockHandler;
 private MockitoTestClass mockitoTestClass;

 @Before
 public void setup() {
 mockitoTestClass = mock(MockitoTestClass.class);
 mockHandler = new MockHandler<>(mockitoTestClass);
 }

 @Test
 public void testHandle_void_method_stubbing() {
 // given
 when(mockitoTestClass.voidMethod()).thenReturn(42);

 // when
 Object result = mockHandler.handle(ArgumentCaptor.invokeCapture(InvocationOnMock.class).capture().getTarget());

 // then
 InvocationOnMock invocation = ArgumentCaptor.getArgumentCaptor(InvocationOnMock.class).getValue();
 assertEquals(42, invocation.getMock().voidMethod());
 assertEquals(null, result);
 }

 @Test
 public void testHandle_void_method_invocation() {
 // given
 when(mockitoTestClass.voidMethod()).thenThrow(new RuntimeException("Test error"));

 // when
 try {
 mockHandler.handle(ArgumentCaptor.invokeCapture(InvocationOnMock.class).capture().getTarget());
 fail("Expected exception");
 } catch (RuntimeException e) {
 // then
 assertEquals("Test error", e.getMessage());
 }
 }

 @Test
 public void testHandle_non_void_method_invocation_with_answer() {
 // given
 when(mockitoTestClass.nonVoidMethod(ArgumentMatchers.eq("test"))).thenReturn("Test result");

 // when
 String result = (String) mockHandler.handle(ArgumentCaptor.invokeCapture(InvocationOnMock.class).capture().getTarget())
 .invokeMethod(Mockito.method(MockitoTestClass.class, "nonVoidMethod", ArgumentMatchers.eq("test")));

 // then
 InvocationOnMock invocation = ArgumentCaptor.getArgumentCaptor(InvocationOnMock.class).getValue();
 assertEquals("Test result", result);
 assertEquals("test", invocation.getArguments()[0]);
 }

 @Test
 public void testHandle_non_void_method_invocation_with_any_answers() {
 // given
 when(mockitoTestClass.nonVoidMethod(ArgumentMatchers.anyString())).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArguments()[0] + " Test result";
 }
 });

 // when
 String result = (String) mockHandler.handle(ArgumentCaptor.invokeCapture(InvocationOnMock.class).capture().getTarget())
 .invokeMethod(Mockito.method(MockitoTestClass.class, "nonVoidMethod", ArgumentMatchers.eq("test")))[0];

 // then
 InvocationOnMock invocation = ArgumentCaptor.getArgumentCaptor(InvocationOnMock.class).getValue();
 assertEquals("test Test result", result);
 assertEquals("test", invocation.getArguments()[0]);
 }

 @Test
 public void testHandle_non_void_method_invocation_with_stub() {
 // given
 when(mockitoTestClass.nonVoidMethod(ArgumentMatchers.anyString())).thenCallRealMethod();
 doReturn("Test result").when(mockitoTestClass).nonVoidMethod("test");

 // when
 String result = (String) mockHandler.handle(ArgumentCaptor.invokeCapture(InvocationOnMock.class).capture().getTarget())
 .invokeMethod(Mockito.method(MockitoTestClass.class, "nonVoidMethod", ArgumentMatchers.eq("test")))[0];

 // then
 InvocationOnMock invocation = ArgumentCaptor.getArgumentCaptor(InvocationOnMock.class).getValue();
 assertEquals("Test result", result);
 assertEquals("test", invocation.getArguments()[0]);
 }

 @Test
 public void testHandle_non_void_method_invocation_with_spy() {
 // given
 MockitoTestClass spy = spy(MockitoTestClass.class);
 when(spy.nonVoidMethod(ArgumentMatchers.anyString())).thenReturn("Test result");
 OngoingStubbing<MockitoTestClass> stubbing = stub(spy, "nonVoidMethod");
 stubbing.withArguments(ArgumentMatchers.eq("test")) // or any other matcher
 .thenReturn("Test result from stubbing");

 // when
 String result1 = (String) mockHandler.handle(spy).invokeMethod(Mockito.method(MockitoTestClass.class, "nonVoidMethod", ArgumentMatchers.eq("test")))[0];
 String result2 = stubbing.get();

 // then
 assertEquals("Test result", result1);
 assertEquals("Test result from stubbing", result2);
 }
}

class MockitoTestClass {
 void voidMethod() {}
 String nonVoidMethod(String arg) { return arg; }
}