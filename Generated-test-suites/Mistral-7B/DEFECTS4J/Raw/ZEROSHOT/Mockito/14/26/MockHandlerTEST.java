package org.mockito.internal.test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

 private MockHandler<MockitoTestClass> mockHandler;
 private InvocationOnMock mockInvocation;
 private MockSettingsImpl mockSettings;
 private InvocationContainer invocationContainer;
 private StubbedInvocationMatcher stubbedInvocation;
 private ArgumentCaptor<String> argumentCaptor;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 invocationContainer = mockHandler.getInvocationContainer();
 mockInvocation = mock(InvocationOnMock.class);
 argumentCaptor = argumentCaptor(String.class);
 }

 @Test
 public void testHandle_StubbingVoidMethods() throws Throwable {
 // given
 doAnswer(invocation -> { /* some code */ }).when(mockInvocation).voidMethod();

 // when
 mockHandler.handle(mockInvocation);

 // then
 verifyZeroInteractions(mockInvocation);
 }

 @Test
 public void testHandle_VerifyInteractions() throws Throwable {
 // given
 when(mockInvocation.stringMethod(eq("arg"))).thenReturn("result");

 // when
 mockHandler.handle(mockInvocation);

 // then
 verify(mockInvocation).stringMethod(argumentCaptor.capture("argument"));
 ArgumentCaptor<String> capturedArgument = argumentCaptor;
 verify(mockInvocation).stringMethod(isA(String.class));
 verify(mockInvocation).stringMethod(argThat(containsString("arg")));

 String argument = capturedArgument.getValue();
 assertEquals("arg", argument);
 assertEquals("result", mockHandler.handle(mockInvocation));
 }

 @Test
 public void testHandle_VerifyInteractionsAndStubbing() throws Throwable {
 // given
 when(mockInvocation.stringMethod(eq("arg"))).thenReturn("result1");
 doAnswer(invocation -> "result2").when(mockInvocation).voidMethod();

 // when
 mockHandler.handle(mockInvocation);

 // then
 verify(mockInvocation).stringMethod(argumentCaptor.capture("argument1"));
 ArgumentCaptor<String> argumentCaptor1 = argumentCaptor;
 verify(mockInvocation).voidMethod();

 String argument1 = argumentCaptor1.getValue();
 assertEquals("arg", argument1);
 assertEquals("result2", mockHandler.handle(mockInvocation));

 // verify again
 verify(mockInvocation).stringMethod(isA(String.class));
 verify(mockInvocation).voidMethod();
 }

 @Test
 public void testHandle_StubbingWithAnswers() throws Throwable {
 // given
 List<Answer> answers = new java.util.ArrayList<>();
 answers.add(invocation -> "answer1");
 answers.add(invocation -> "answer2");
 mockHandler.setAnswersForStubbing(answers);

 // when
 mockHandler.handle(mockInvocation);

 // then
 verifyZeroInteractions(mockInvocation);
 assertEquals("answer1", mockHandler.handle(mockInvocation));
 assertEquals("answer2", mockHandler.handle(mockInvocation));
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_VerifyInteractionsAfterStubbing() throws Throwable {
 // given
 doAnswer(invocation -> {}).when(mockInvocation).voidMethod();

 // when, then
 mockHandler.handle(mockInvocation);
 verify(mockInvocation).voidMethod(); // should fail
 }

 @Test
 public void testHandle_VerifyInteractionsAndStubbingWithOngoingStubbing() throws Throwable {
 // given
 OngoingStubbingImpl<MockitoTestClass> ongoingStubbing = mock(OngoingStubbingImpl.class);
 when(invocationContainer.getOngoingStubbing()).thenReturn(ongoingStubbing);
 doAnswer(invocation -> "answer1").when(mockInvocation).stringMethod(eq("arg1"));
 doAnswer(invocation -> "answer2").when(mockInvocation).stringMethod(eq("arg2"));

 // when
 mockHandler.handle(mockInvocation);

 // then
 verify(mockInvocation).stringMethod(eq("arg1")).thenReturn("answer1");
 verify(mockInvocation).stringMethod(eq("arg2")).thenReturn("answer2");
 }
}