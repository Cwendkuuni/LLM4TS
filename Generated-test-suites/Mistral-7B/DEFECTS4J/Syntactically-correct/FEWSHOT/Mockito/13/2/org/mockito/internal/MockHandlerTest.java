package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.mockito.internal.Invocation;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationContainer;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

 private MockHandler<Object> mockHandler;
 private InvocationContainer invocationContainer;
 private List<Answer> answers;

 @Before
 public void setUp() {
 mockHandler = new MockHandler<>();
 invocationContainer = mockHandler.getInvocationContainer();
 answers = new ArrayList<>();
 }

 @Test
 public void testHandle_noAnswersForStubbing() throws Throwable {
 Object result = mockHandler.handle(createMockInvocation(null, new Object[0]));
 verifyZeroInteractions(invocationContainer);
 assertEquals(mockSettings.getDefaultAnswer().answer(invocation), result);
 }

 @Test
 public void testHandle_answersForStubbing() throws Throwable {
 when(mockHandler.getInvocationContainer().findAnswerFor(createMockInvocation(null, new Object[0]))).thenReturn(answers.get(0));
 when(answers.get(0).answer(any(Invocation.class))).thenReturn("answer");

 Object result = mockHandler.handle(createMockInvocation(null, new Object[0]));
 verify(invocationContainer).setInvocationForPotentialStubbing(eq(createMockInvocationMatcher(null, new Object[0])));
 verifyNoMoreInteractions(invocationContainer);
 assertEquals("answer", result);
 }

 @Test
 public void testHandle_verificationModeIsNotNull() throws Throwable {
 VerificationMode verificationMode = mock(VerificationMode.class);
 when(mockHandler.getMockingProgress().pullVerificationMode()).thenReturn(verificationMode);
 when(verificationMode.verify(any(VerificationDataImpl.class))).thenReturn(true);

 Object mock = mock(Object.class);
 mockHandler = new MockHandler<>(mock);
 Invocation invocation = createMockInvocation(mock, new Object[0]);
 InvocationMatcher invocationMatcher = createMockInvocationMatcher(mock, new Object[0]);

 mockHandler.handle(invocation);
 verify(mock).someMethod();
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_verificationModeIsNotNull_verifyingOnDifferentMock() throws Throwable {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mockHandler = new MockHandler<>(mock1);
 VerificationMode verificationMode = mock(VerificationMode.class);
 when(mockHandler.getMockingProgress().pullVerificationMode()).thenReturn(verificationMode);
 when(verificationMode.verify(any(VerificationDataImpl.class))).thenReturn(true);

 Invocation invocation = createMockInvocation(mock2, new Object[0]);
 InvocationMatcher invocationMatcher = createMockInvocationMatcher(mock1, new Object[0]);

 mockHandler.handle(invocation);
 }

 @Test
 public void testHandle_answersForStubbing_voidMethod() throws Throwable {
 VoidMethodStubbable voidMethodStubbable = mockHandler.voidMethodStubbable(mock(Object.class));
 voidMethodStubbable.stubVoid();
 Object mock = mock(Object.class);
 when(mock.someMethod()).thenCallRealMethod();
 Invocation invocation = createMockInvocation(mock, new Object[0]);

 Object result = mockHandler.handle(invocation);
 verify(invocationContainer).setInvocationForPotentialStubbing(eq(invocationMatcher));
 verify(invocationContainer).setMethodForStubbing(eq(invocationMatcher));
 verifyNoMoreInteractions(invocationContainer);
 assertEquals(null, result);
 }

 @Test
 public void testHandle_answersForStubbing_answeringWithAnswer() throws Throwable {
 final Answer answer = mock(Answer.class);
 when(answer.answer(any(Invocation.class))).thenReturn("answer");
 mockHandler.setAnswersForStubbing(answers);
 answers.add(answer);
 Object mock = mock(Object.class);
 Invocation invocation = createMockInvocation(mock, new Object[0]);
 InvocationMatcher invocationMatcher = createMockInvocationMatcher(mock, new Object[0]);
 when(mockHandler.getInvocationContainer().findAnswerFor(invocation)).thenReturn(answer);

 Object result = mockHandler.handle(invocation);
 verify(invocationContainer).setInvocationForPotentialStubbing(eq(invocationMatcher));
 verifyNoMoreInteractions(invocationContainer);
 assertEquals("answer", result);
 }

 private Invocation createMockInvocation(Object mock, Object[] args) {
 return new Invocation(mock, null, new Object[0], mockHandler, args, null, null);
 }

 private InvocationMatcher createMockInvocationMatcher(Object mock, Object[] args) {
 return mockitoMockitoMatchers.anyObject().eq(mock);
 }
}