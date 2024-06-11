package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationModeFactory;

public class MockHandlerTest {

 MockHandler<Object> handler;
 MockSettingsImpl mockSettings = new MockSettingsImpl();

 @Test
 public void testHandleNullInvocation() {
 handler = new MockHandler<>(mockSettings);
 assertNull(handler.handle(null));
 }

 @Test
 public void testHandleInvocationWithNoAnswers() {
 handler = new MockHandler<>(mockSettings);
 Invocation invocation = mock(Invocation.class);
 Object result = handler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testHandleInvocationWithStubbedAnswer() {
 handler = new MockHandler<>(mockSettings);
 Invocation invocation = mock(Invocation.class);
 Answer answer = mock(Answer.class);
 when(handler.getInvocationContainer().findAnswerFor(invocation)).thenReturn(new StubbedInvocationMatcher(answer));
 Object result = handler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testHandleInvocationWithDefaultAnswer() {
 handler = new MockHandler<>(mockSettings);
 Invocation invocation = mock(Invocation.class);
 Object answer = mockSettings.getDefaultAnswer().answer(invocation);
 Object result = handler.handle(invocation);
 assertSame(answer, result);
 }

 @Test
 public void testHandleInvocationWithVerificationMode() {
 handler = new MockHandler<>(mockSettings);
 Invocation invocation = mock(Invocation.class);
 VerificationMode verificationMode = mock(VerificationMode.class);
 VerificationModeFactory verificationModeFactory = mock(VerificationModeFactory.class);
 when(verificationModeFactory.verify()).thenReturn(verificationMode);
 when(handler.mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
 handler.handle(invocation);
 }

 @Test
 public void testHandleInvocationWithVerificationModeOnDifferentMock() {
 handler = new MockHandler<>(mockSettings);
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 Invocation invocation = mock(Invocation.class);
 VerificationMode verificationMode = mock(VerificationMode.class);
 VerificationModeFactory verificationModeFactory = mock(VerificationModeFactory.class);
 when(verificationModeFactory.verify()).thenReturn(verificationMode);
 when(handler.mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
 when(invocation.getMock()).thenReturn(mock2);
 handler.handle(invocation);
 verifyNoInteractions(mock1);
 }

 @Test
 public void testHandleInvocationWithMultipleVerificationModes() {
 handler = new MockHandler<>(mockSettings);
 Object mock = mock(Object.class);
 Invocation invocation = mock(Invocation.class);
 VerificationMode verificationMode1 = mock(VerificationMode.class);
 VerificationMode verificationMode2 = mock(VerificationMode.class);
 VerificationModeFactory verificationModeFactory = mock(VerificationModeFactory.class);
 when(verificationModeFactory.verify()).thenReturn(verificationMode1);
 when(handler.mockingProgress.getArgumentMatcherStorage()).thenReturn(Collections.singletonList(mock));
 when(handler.mockingProgress.pullVerificationMode()).thenReturn(verificationMode1);
 handler.handle(invocation);
 when(verificationModeFactory.verify()).thenReturn(verificationMode2);
 handler.handle(invocation);
 verify(mock).methodCall();
 }

 @Test
 public void testHandleInvocationWithMultipleAnswers() {
 handler = new MockHandler<>(mockSettings);
 Object mock = mock(Object.class);
 Invocation invocation = mock(Invocation.class);
 List<Answer> answers = Arrays.asList(mock(Answer.class), mock(Answer.class));
 when(handler.getInvocationContainer().setAnswersForStubbing(answers)).thenCallRealMethod();
 when(handler.getInvocationContainer().findAnswerFor(invocation)).thenReturn(null);
 handler.handle(invocation);
 handler.handle(invocation);
 Object result1 = mockSettings.getDefaultAnswer().answer(invocation);
 Object result2 = mockSettings.getDefaultAnswer().answer(invocation);
 assertSame(result1, result2);
 }

 @Test
 public void testHandleInvocationWithOngoingStubbing() {
 handler = new MockHandler<>(mockSettings);
 Object mock = mock(Object.class);
 Invocation invocation = mock(Invocation.class);
 Object returnValue = "return value";
 when(handler.getInvocationContainer().setAnswersForStubbing(Collections.singletonList(mock(Answer.class))))
 .thenCallRealMethod();
 when(handler.getInvocationContainer().findAnswerFor(invocation)).thenReturn(null);
 OngoingStubbingImpl<Object> ongoingStubbing = mock(OngoingStubbingImpl.class);
 when(handler.getInvocationContainer().reportOngoingStubbing(ongoingStubbing)).thenCallRealMethod();
 when(ongoingStubbing.answer(invocation)).thenReturn(returnValue);
 handler.handle(invocation);
 assertSame(returnValue, handler.handle(invocation));
 }
}