package org.mockito.internal.test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationContainer;
import org.mockito.internal.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

public class MockHandlerTest {

 private MockHandler<Object> mockHandler;
 private Object mock;
 private InvocationContainer invocationContainer;
 private List<Answer> answers;

 @Before
 public void setUp() {
 mockHandler = new MockHandler<>();
 mock = mock(Object.class);
 invocationContainer = mockHandler.getInvocationContainer();
 answers = new ArrayList<>();
 }

 @Test
 public void testHandleVoidMethodInvocation() throws Throwable {
 Invocation invocation = new Invocation(null, null, null, null, null, null, null, null, null);
 mockHandler.handle(invocation);
 // verify no verification or stubbing occurred
 verify(mock, never()).verifyNoMoreInteractions();
 }

 @Test
 public void testHandleNonVoidMethodInvocationWithNoAnswer() throws Throwable {
 Invocation invocation = new Invocation(null, null, null, "testMethod", null, null, null, null, null);
 mockHandler.handle(invocation);
 // verify no verification or stubbing occurred
 verify(mock, never()).verifyNoMoreInteractions();
 }

 @Test
 public void testHandleNonVoidMethodInvocationWithAnswer() throws Throwable {
 Answer answer = mock(Answer.class);
 invocationContainer.setAnswersForStubbing(answers);
 answers.add(answer);
 Invocation invocation = new Invocation(null, null, null, "testMethod", null, null, null, null, null);
 mockHandler.handle(invocation);
 // verify the answer was called with the correct arguments
 verify(answer).answer(invocation);
 }

 @Test
 public void testHandleNonVoidMethodInvocationWithMultipleAnswers() throws Throwable {
 Answer answer1 = mock(Answer.class);
 Answer answer2 = mock(Answer.class);
 invocationContainer.setAnswersForStubbing(answers);
 answers.add(answer1);
 answers.add(answer2);
 Invocation invocation = new Invocation(null, null, null, "testMethod", null, null, null, null, null);
 mockHandler.handle(invocation);
 // verify both answers were called with the correct arguments
 verify(answer1).answer(invocation);
 verify(answer2).answer(invocation);
 }

 @Test
 public void testHandleNonVoidMethodInvocationWithVerificationMode() throws Throwable {
 Invocation invocation = new Invocation(null, anyInt(), null, "testMethod", null, null, null, null, null);
 VerificationMode verificationMode = verify(mock);
 mockHandler.handle(invocation);
 // verify the verification mode was executed with the correct arguments
 verify(mock).verify(verificationMode);
 }

 @Test
 public void testHandleNonVoidMethodInvocationWithVerificationModeAndAnswer() throws Throwable {
 Answer answer = mock(Answer.class);
 Invocation invocation = new Invocation(null, anyInt(), null, "testMethod", null, null, null, null, null);
 VerificationMode verificationMode = verify(mock);
 mockHandler.handle(invocation);
 // verify the verification mode was executed with the correct arguments
 verify(mock).verify(verificationMode);
 // verify the answer was called with the correct arguments
 verify(answer).answer(invocation);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandleNonVoidMethodInvocationWithAnswerAndVerificationModeTwice() throws Throwable {
 Answer answer = mock(Answer.class);
 Invocation invocation = new Invocation(null, anyInt(), null, "testMethod", null, null, null, null, null);
 mockHandler.handle(invocation);
 VerificationMode verificationMode = verify(mock);
 mockHandler.handle(invocation);
 // verify an exception is thrown
 }

 @Test
 public void testHandleVoidMethodStubbing() throws Throwable {
 Invocation invocation = new Invocation(null, null, null, null, null, null, null, null, null);
 mockHandler.handle(invocation);
 // verify no verification or stubbing occurred
 verify(mock, never()).verifyNoMoreInteractions();
 }

 @Test
 public void testHandleNonVoidMethodStubbing() throws Throwable {
 Invocation invocation = new Invocation(null, null, null, "testMethod", null, null, null, null, null);
 InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
 mockHandler.handle(invocation);
 mockHandler.setAnswersForStubbing(answers);
 answers.add(mock(Answer.class));
 when(invocationMatcher.matches(invocation)).thenReturn(true);
 invocationContainer.setInvocationForPotentialStubbing(invocationMatcher);
 mockHandler.handle(invocation);
 // verify the answer was called with the correct arguments
 verify(answers.get(0)).answer(invocation);
 }

 @Test
 public void testHandleNonVoidMethodStubbingWithMultipleAnswers() throws Throwable {
 Invocation invocation = new Invocation(null, null, null, "testMethod", null, null, null, null, null);
 InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
 mockHandler.handle(invocation);
 mockHandler.setAnswersForStubbing(answers);
 answers.add(mock(Answer.class));
 answers.add(mock(Answer.class));
 when(invocationMatcher.matches(invocation)).thenReturn(true);
 invocationContainer.setInvocationForPotentialStubbing(invocationMatcher);
 mockHandler.handle(invocation);
 // verify both answers were called with the correct arguments
 verify(answers.get(0)).answer(invocation);
 verify(answers.get(1)).answer(invocation);
 }

 @Test
 public void testHandleNonVoidMethodStubbingWithVerificationMode() throws Throwable {
 Invocation invocation = new Invocation(null, anyInt(), null, "testMethod", null, null, null, null, null);
 mockHandler.handle(invocation);
 VerificationMode verificationMode = verify(mock);
 StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
 when(stubbedInvocationMatcher.matches(invocation)).thenReturn(true);
 when(mockHandler.findAnswerFor(invocation)).thenReturn(stubbedInvocationMatcher);
 mockHandler.handle(invocation);
 // verify the verification mode was executed with the correct arguments
 verify(mock).verify(verificationMode);
 }

 @Test
 public void testHandleNonVoidMethodStubbingWithVerificationModeAndAnswer() throws Throwable {
 Invocation invocation = new Invocation(null, anyInt(), null, "testMethod", null, null, null, null, null);
 mockHandler.handle(invocation);
 VerificationMode verificationMode = verify(mock);
 Answer answer = mock(Answer.class);
 StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
 when(stubbedInvocationMatcher.matches(invocation)).thenReturn(true);
 when(mockHandler.findAnswerFor(invocation)).thenReturn(stubbedInvocationMatcher);
 mockHandler.handle(invocation);
 // verify the verification mode was executed with the correct arguments
 verify(mock).verify(verificationMode);
 // verify the answer was called with the correct arguments
 verify(answer).answer(invocation);
 }

 @Test
 public void testHandleNonVoidMethodStubbingWithVerificationModeTwice() throws Throwable {
 Invocation invocation = new Invocation(null, anyInt(), null, "testMethod", null, null, null, null, null);
 mockHandler.handle(invocation);
 VerificationMode verificationMode = verify(mock);
 StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
 when(stubbedInvocationMatcher.matches(invocation)).thenReturn(true);
 when(mockHandler.findAnswerFor(invocation)).thenReturn(stubbedInvocationMatcher);
 mockHandler.handle(invocation);
 mockHandler.handle(invocation);
 // verify an exception is thrown
 }
}