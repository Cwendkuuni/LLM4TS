package org.mockito.internal.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWithNull;
import org.mockito.stubbing.AnswerWithThrowable;
import org.mockito.stubbing.AnswerWithValue;
import org.mockito.stubbing.OngoingStubbing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 private DummyClass mock;

 @Before
 public void setUp() {
 mock = mock(DummyClass.class);
 mockHandler = new MockHandler<DummyClass>(new MockSettingsImpl());
 }

 @Test
 public void testHandleVoidMethodInvocationWithNoStubbing() {
 // given
 when(mock.method1(ArgumentMatchers.anyString())).thenReturn("result");

 // when
 Object returnedValue = mockHandler.handle(new InvocationOnMock(mock, "method1", new String[]{"arg1"}));

 // then
 verify(mock).method1("arg1");
 assertNull(returnedValue);
 }

 @Test
 public void testHandleVoidMethodInvocationWithStubbing() {
 // given
 when(mock.method1(ArgumentMatchers.anyString())).thenCallRealMethod();
 OngoingStubbing<DummyClass> ongoingStubbing = Mockito.doNothing().when(mock).method2();

 // when
 mockHandler.handle(new InvocationOnMock(mock, "method1", new String[]{"arg1"}));
 mockHandler.handle(new InvocationOnMock(mock, "method2"));

 // then
 verify(mock).method1("arg1");
 verifyNoInteractions(mock).method2();
 }

 @Test(expected = IllegalStateException.class)
 public void testHandleMethodInvocationWithVerificationMode() {
 // given
 when(mock.method1(ArgumentMatchers.anyString())).thenCallRealMethod();

 // when
 mockHandler.handle(new InvocationOnMock(mock, "method1", new String[]{"arg1"}));
 verify(mock).method1("arg1"); // verifying after invoking handler

 // then
 // exception is thrown due to verification mode being non-null
 }

 @Test
 public void testHandleMethodInvocationWithAnswer() {
 // given
 when(mock.method1(ArgumentMatchers.anyString())).thenAnswer(new AnswerWithValue("result"));

 // when
 Object returnedValue = mockHandler.handle(new InvocationOnMock(mock, "method1", new String[]{"arg1"}));

 // then
 assertEquals("result", returnedValue);
 }

 @Test
 public void testHandleMethodInvocationWithAnswerWithNull() {
 // given
 when(mock.method1(ArgumentMatchers.anyString())).thenAnswer(new AnswerWithNull());

 // when
 Object returnedValue = mockHandler.handle(new InvocationOnMock(mock, "method1", new String[]{"arg1"}));

 // then
 assertNull(returnedValue);
 }

 @Test
 public void testHandleMethodInvocationWithAnswerWithThrowable() {
 // given
 when(mock.method1(ArgumentMatchers.anyString())).thenAnswer(new AnswerWithThrowable(new RuntimeException()));

 // when
 try {
 mockHandler.handle(new InvocationOnMock(mock, "method1", new String[]{"arg1"}));
 } catch (Throwable e) {
 // then
 assertEquals("expected exception message", e.getMessage());
 }
 }

 @Test
 public void testHandleMethodInvocationWithDefaultAnswer() {
 // given
 when(mock.method1(ArgumentMatchers.anyString())).thenCallRealMethod();

 // when
 Object returnedValue = mockHandler.handle(new InvocationOnMock(mock, "method1", new String[]{"arg1"}));

 // then
 ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
 verify(mock).method2(argumentCaptor.capture("arg2"));
 assertEquals("expected arg2 value", argumentCaptor.getValue());
 assertEquals(returnedValue, mock.method1("arg1"));
 }
}

class DummyClass {
 void method1(String arg) {
 }
 void method2(String arg) {
 }
}