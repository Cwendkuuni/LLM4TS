package org.mockito.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.mockito.internal.verification.VerificationModeFactory.never;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.invocation.StubbedInvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbableImpl;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

 private MockHandler<Foo> mockHandler;
 private Foo mock;
 private Invocation invocation;
 private InvocationMatcher invocationMatcher;
 private List<Answer> answers;

 @Before
 public void setup() {
 mock = mock(Foo.class);
 mockHandler = new MockHandler<>();
 invocation = mock(Invocation.class);
 invocationMatcher = mock(InvocationMatcher.class);
 answers = new ArrayList<>();
 }

 @Test
 public void testHandle_NoAnswersOrVerification_ReturnsDefaultAnswer() throws Throwable {
 // given
 when(mockHandler.handle(invocation)).thenAnswer(invocation -> mockSettings.getDefaultAnswer().answer(invocation));

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertNotNull(result);
 }

 @Test
 public void testHandle_AnswersForStubbing_ReturnsAnswer() throws Throwable {
 // given
 when(mockHandler.handle(invocation)).thenReturn(null);
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(new StubbedInvocationMatcher(invocationMatcher, answers));

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertNull(result);
 }

 @Test
 public void testHandle_VerificationMode_VerifiesInvocation() throws Throwable {
 // given
 VerificationMode verificationMode = times(1);
 when(mockHandler.handle(invocation)).thenCallRealMethod();
 when(mockHandler.handle(invocation)).thenAnswer(invocation -> {
 throw new RuntimeException();
 });
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(verificationMode);

 // when & then
 verify(mock, verificationMode).methodCalling();
 }

 @Test
 public void testHandle_VerificationMode_ThrowsExceptionIfNotSameMock() throws Throwable {
 // given
 VerificationMode verificationMode = times(1);
 Foo otherMock = mock(Foo.class);
 when(mockHandler.handle(invocation)).thenCallRealMethod();
 when(mockHandler.handle(invocation)).thenAnswer(invocation -> {
 throw new RuntimeException();
 });
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(verificationMode);

 // when
 try {
 mockHandler.handle(otherMock.getInvocationForTestPurposes());
 fail();
 } catch (AssertionError e) {
 // then
 // expected
 }
 }

 @Test
 public void testHandle_AnswersForStubbing_ReturnsAnswerWithCapturedArguments() throws Throwable {
 // given
 when(mockHandler.handle(invocation)).thenReturn(null);
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
 when(mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher)).thenReturn(true);
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
 when(mockSettings.getDefaultAnswer()).thenReturn(answers.get(0));
 when(answers.get(0).answer(invocation)).thenReturn("answer");

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertEquals("answer", result);
 }

 @Test
 public void testHandle_AnswersForStubbing_ReturnsNullIfNoAnswer() throws Throwable {
 // given
 when(mockHandler.handle(invocation)).thenReturn(null);
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
 when(mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher)).thenReturn(true);
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertNull(result);
 }

 @Test
 public void testHandle_AnswersForStubbing_ResetsInvocationForPotentialStubbing() throws Throwable {
 // given
 when(mockHandler.handle(invocation)).thenReturn(null);
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
 when(mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher)).thenReturn(true);
 when(mockHandler.invocationContainerImpl.setAnswersForStubbing(answers)).thenReturn(true);
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(new StubbedInvocationMatcher(invocationMatcher, answers.get(0)));
 when(mockHandler.invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher)).thenReturn(true);

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertNull(result);
 }

 @Test
 public void testHandle_AnswersForStubbing_ResetsInvocationForPotentialStubbing_ReturnsAnswer() throws Throwable {
 // given
 when(mockHandler.handle(invocation)).thenReturn(null);
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
 when(mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher)).thenReturn(true);
 when(mockHandler.invocationContainerImpl.setAnswersForStubbing(answers)).thenReturn(true);
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(new StubbedInvocationMatcher(invocationMatcher, answers.get(0)));
 when(mockHandler.invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher)).thenReturn(false);
 when(mockSettings.getDefaultAnswer()).thenReturn(answers.get(1));
 when(answers.get(1).answer(invocation)).thenReturn("answer2");

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertNull(result);

 // reset invocation for potential stubbing and try again
 mockHandler.invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);

 // when
 Object result2 = mockHandler.handle(invocation);

 // then
 assertEquals("answer2", result2);
 }

 @Test
 public void testHandle_AnswerDoesNotCaptureArguments() throws Throwable {
 // given
 when(mockHandler.handle(invocation)).thenReturn(null);
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "answer";
 }
 });

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertEquals("answer", result);
 }

 @Test
 public void testHandle_AnswerCapturesArguments() throws Throwable {
 // given
 when(mockHandler.handle(invocation)).thenReturn(null);
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return invocation.getArguments()[0];
 }
 });

 // when
 Object result = mockHandler.handle(new Object[]{"arg"});

 // then
 assertEquals("arg", result);
 }

 @Test
 public void testHandle_ReturnsNullForVoidMethods() throws Throwable {
 // given
 when(mockHandler.handle(invocation)).thenCallRealMethod();
 when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(never());

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertNull(result);
 }

 @Test
 public void testHandle_StubsVoidMethods() throws Throwable {
 // given
 when(mockHandler.handle(invocation)).thenCallRealMethod();
 when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
 when(mockHandler.invocationContainerImpl.setMethodForStubbing(invocationMatcher)).thenCallRealMethod();

 // when
 mockHandler.handle(invocation);

 // then
 // do nothing
 }

 @Test
 public void testHandle_ArgumentsAreCapturedForAnswer() throws Throwable {
 // given
 when(mockHandler.handle(invocation)).thenReturn(null);
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return invocation.getArguments()[0];
 }
 });

 // when
 Object result = mockHandler.handle(new Object[]{"arg"});

 // then
 assertEquals("arg", result);
 }

 @Test
 public void testHandle_AnswerDoesNotCaptureArgumentsForVoidMethods() throws Throwable {
 // given
 when(mockHandler.handle(invocation)).thenCallRealMethod();
 when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
 when(mockHandler.invocationContainerImpl.setMethodForStubbing(invocationMatcher)).thenCallRealMethod();
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return null;
 }
 });

 // when
 mockHandler.handle(invocation);

 // then
 // do nothing
 }

 @Test
 public void testHandle_AnswerDoesNotCaptureArgumentsForNonVoidMethods() throws Throwable {
 // given
 when(mockHandler.handle(invocation)).thenReturn(null);
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return null;
 }
 });

 // when
 Object result = mockHandler.handle(new Object[]{"arg"});

 // then
 assertNull(result);
 }

 @Test
 public void testHandle_AnswerCapturesArgumentsForNonVoidMethods() throws Throwable {
 // given
 when(mockHandler.handle(invocation)).thenReturn(null);
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return invocation.getArguments()[0];
 }
 });

 // when
 Object result = mockHandler.handle(new Object[]{"arg"});

 // then
 assertEquals("arg", result);
 }
}