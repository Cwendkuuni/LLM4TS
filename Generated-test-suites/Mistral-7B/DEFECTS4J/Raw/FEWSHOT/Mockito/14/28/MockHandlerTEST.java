package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.mockito.invocation.Invocation;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbableImpl;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest<T> {

 MockHandler<T> mockHandler;
 MockSettingsImpl mockSettings;
 InvocationContainer invocationContainer;
 Invocation invocation;
 InvocationMatcher invocationMatcher;
 StubbedInvocationMatcher stubbedInvocation;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<T>(mockSettings);
 invocationContainer = mockHandler.getInvocationContainer();
 invocation = mock(Invocation.class);
 }

 @Test
 public void testHandle_noAnswersForStubbing() throws Throwable {
 // given
when(invocation.getTarget()).thenReturn(mock(T.class));
 when(invocation.getMethod()).thenReturn(mock(Class.class));
 when(invocation.getArguments()).thenReturn(new Object[0]);

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertNull(result);
 verifyZeroInteractions(invocationContainer);
 }

 @Test
 public void testHandle_answersForStubbing_voidMethod() throws Throwable {
 // given
 VoidMethodStubbable<T> voidMethodStubbable = mockHandler.voidMethodStubbable(mock(T.class));
 when(invocation.getTarget()).thenReturn(mock(T.class));
 when(invocation.getMethod()).thenReturn(mock(Class.class));
 when(invocation.getArguments()).thenReturn(new Object[0]);
 voidMethodStubbable.stubVoid();

 // when
 mockHandler.handle(invocation);

 // then
 verifyZeroInteractions(invocationContainer);
 }

 @Test
 public void testHandle_answersForStubbing_answer() throws Throwable {
 // given
 Answer<?> answer = mock(Answer.class);
 when(invocation.getTarget()).thenReturn(mock(T.class));
 when(invocation.getMethod()).thenReturn(mock(Class.class));
 when(invocation.getArguments()).thenReturn(new Object[0]);
 when(answer.answer(invocation)).thenReturn("answer");

 List<Answer> answers = Arrays.asList(answer);
 mockHandler.setAnswersForStubbing(answers);

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertEquals("answer", result);
 verify(answer).answer(invocation);
 }

 @Test
 public void testHandle_answersForStubbing_answerWithArguments() throws Throwable {
 // given
 Answer<?> answer = mock(Answer.class);
 when(invocation.getTarget()).thenReturn(mock(T.class));
 when(invocation.getMethod()).thenReturn(mock(Class.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ "arg1" });
 when(answer.answer(invocation)).thenReturn("answer" + Arrays.toString(invocation.getArguments()));

 List<Answer> answers = Arrays.asList(answer);
 mockHandler.setAnswersForStubbing(answers);

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertEquals("answer[arg1]", result.toString());
 verify(answer).answer(invocation);
 }

 @Test
 public void testHandle_answersForStubbing_answerWithArgumentMatcher() throws Throwable {
 // given
 Answer<?> answer = mock(Answer.class);
 when(invocation.getTarget()).thenReturn(mock(T.class));
 when(invocation.getMethod()).thenReturn(mock(Class.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ "arg1" });
 InvocationMatcher argumentMatcher = mock(InvocationMatcher.class);
 when(answer.answer(invocation)).thenReturn(Collections.singletonMap("key", "value"));
 when(argumentMatcher.matches(invocation)).thenReturn(true);

 List<Answer> answers = Arrays.asList(answer);
 mockHandler.setAnswersForStubbing(answers);

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertEquals("value", result);
 verify(answer).answer(invocation);
 verify(argumentMatcher).matches(invocation);
 }

 @Test
 public void testHandle_answersForStubbing_multipleAnswers() throws Throwable {
 // given
 Answer<?> answer1 = mock(Answer.class);
 Answer<?> answer2 = mock(Answer.class);
 when(invocation.getTarget()).thenReturn(mock(T.class));
 when(invocation.getMethod()).thenReturn(mock(Class.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ "arg1" });
 when(answer1.answer(invocation)).thenReturn("answer1");
 when(answer2.answer(invocation)).thenReturn("answer2");

 List<Answer> answers = Arrays.asList(answer1, answer2);
 mockHandler.setAnswersForStubbing(answers);

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertEquals("answer1", result);
 verify(answer1).answer(invocation);
 }

 @Test
 public void testHandle_answersForStubbing_multipleAnswersWithArgumentMatcher() throws Throwable {
 // given
 Answer<?> answer1 = mock(Answer.class);
 Answer<?> answer2 = mock(Answer.class);
 InvocationMatcher argumentMatcher1 = mock(InvocationMatcher.class);
 InvocationMatcher argumentMatcher2 = mock(InvocationMatcher.class);
 when(invocation.getTarget()).thenReturn(mock(T.class));
 when(invocation.getMethod()).thenReturn(mock(Class.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ "arg1" });
 when(answer1.answer(invocation)).thenReturn(Collections.singletonMap("key1", "value1"));
 when(answer2.answer(invocation)).thenReturn(Collections.singletonMap("key2", "value2"));
 when(argumentMatcher1.matches(invocation)).thenReturn(true);
 when(argumentMatcher2.matches(invocation)).thenReturn(false);

 List<Answer> answers = Arrays.asList(answer1, answer2);
 mockHandler.setAnswersForStubbing(answers);

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertEquals("value1", result);
 verify(answer1).answer(invocation);
 verify(argumentMatcher1).matches(invocation);
 verifyZeroInteractions(argumentMatcher2);
 }

 @Test
 public void testHandle_verificationMode() throws Throwable {
 // given
 when(invocation.getTarget()).thenReturn(mock(T.class));
 when(invocation.getMethod()).thenReturn(mock(Class.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ "arg1" });

 VerificationMode verificationMode = mock(VerificationMode.class);
 when(verificationMode.verify(any(VerificationDataImpl.class))).thenReturn(true);
 mockingProgress = new ThreadSafeMockingProgress();
 mockingProgress.pushVerificationMode(verificationMode);

 // when
 mockHandler.handle(invocation);

 // then
 verify(verificationMode).verify(any(VerificationDataImpl.class));
 }

 @Test
 public void testHandle_verificationMode_multipleInvocations() throws Throwable {
 // given
 when(invocation.getTarget()).thenReturn(mock(T.class));
 when(invocation.getMethod()).thenReturn(mock(Class.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ "arg1" });

 VerificationMode verificationMode = mock(VerificationMode.class);
 when(verificationMode.verify(any(VerificationDataImpl.class))).thenReturn(true);
 mockingProgress = new ThreadSafeMockingProgress();
 mockingProgress.pushVerificationMode(verificationMode);

 List<Invocation> invocations = Arrays.asList(invocation, invocation);
 invocationContainer.setInvocations(invocations);

 // when
 mockHandler.handle(invocation);
 mockHandler.handle(invocation);

 // then
 verify(verificationMode, times(2)).verify(any(VerificationDataImpl.class));
 }

 @Test
 public void testHandle_verificationMode_multipleVerificationModes() throws Throwable {
 // given
 when(invocation.getTarget()).thenReturn(mock(T.class));
 when(invocation.getMethod()).thenReturn(mock(Class.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ "arg1" });

 VerificationMode verificationMode1 = mock(VerificationMode.class);
 VerificationMode verificationMode2 = mock(VerificationMode.class);
 when(verificationMode1.verify(any(VerificationDataImpl.class))).thenReturn(true);
 when(verificationMode2.verify(any(VerificationDataImpl.class))).thenReturn(true);
 mockingProgress = new ThreadSafeMockingProgress();
 mockingProgress.pushVerificationMode(verificationMode1);
 mockingProgress.pushVerificationMode(verificationMode2);

 // when
 mockHandler.handle(invocation);

 // then
 verify(verificationMode1, times(1)).verify(any(VerificationDataImpl.class));
 verify(verificationMode2, times(0)).verify(any(VerificationDataImpl.class));
 }

 @Test
 public void testHandle_verificationMode_multipleVerificationModes_multipleInvocations() throws Throwable {
 // given
 when(invocation.getTarget()).thenReturn(mock(T.class));
 when(invocation.getMethod()).thenReturn(mock(Class.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ "arg1" });

 VerificationMode verificationMode1 = mock(VerificationMode.class);
 VerificationMode verificationMode2 = mock(VerificationMode.class);
 when(verificationMode1.verify(any(VerificationDataImpl.class))).thenReturn(true);
 when(verificationMode2.verify(any(VerificationDataImpl.class))).thenReturn(true);
 mockingProgress = new ThreadSafeMockingProgress();
 mockingProgress.pushVerificationMode(verificationMode1);
 mockingProgress.pushVerificationMode(verificationMode2);

 List<Invocation> invocations = Arrays.asList(invocation, invocation);
 invocationContainer.setInvocations(invocations);

 // when
 mockHandler.handle(invocation);
 mockHandler.handle(invocation);

 // then
 verify(verificationMode1, times(2)).verify(any(VerificationDataImpl.class));
 verify(verificationMode2, times(2)).verify(any(VerificationDataImpl.class));
 }

 @Test
 public void testHandle_stubbedInvocation() throws Throwable {
 // given
 when(invocation.getTarget()).thenReturn(mock(T.class));
 when(invocation.getMethod()).thenReturn(mock(Class.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ "arg1" });
 when(invocation.getMethod().getName()).thenReturn("testMethod");

 StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
 stubbedInvocation = new StubbedInvocationMatcher(stubbedInvocationMatcher, invocation);
 invocationContainer.setStubbedInvocation(stubbedInvocation);
 when(stubbedInvocationMatcher.matches(invocation)).thenReturn(true);
 when(stubbedInvocation.answer(invocation)).thenReturn("answer");

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertEquals("answer", result);
 verify(stubbedInvocation).answer(invocation);
 verify(stubbedInvocationMatcher).matches(invocation);
 }

 @Test
 public void testHandle_stubbedInvocation_multipleInvocations() throws Throwable {
 // given
 when(invocation.getTarget()).thenReturn(mock(T.class));
 when(invocation.getMethod()).thenReturn(mock(Class.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ "arg1" });
 when(invocation.getMethod().getName()).thenReturn("testMethod");

 List<StubbedInvocationMatcher> stubbedInvocationMatchers = Arrays.asList(mock(StubbedInvocationMatcher.class), mock(StubbedInvocationMatcher.class));
 stubbedInvocation = new StubbedInvocationMatcher(stubbedInvocationMatchers.get(0), invocation);
 invocationContainer.setStubbedInvocation(stubbedInvocation);
 when(stubbedInvocationMatchers.get(0).matches(invocation)).thenReturn(true);
 when(stubbedInvocation.answer(invocation)).thenReturn("answer1");

 stubbedInvocation = new StubbedInvocationMatcher(stubbedInvocationMatchers.get(1), invocation);
 invocationContainer.setStubbedInvocation(stubbedInvocation);
 when(stubbedInvocationMatchers.get(1).matches(invocation)).thenReturn(true);
 when(stubbedInvocation.answer(invocation)).thenReturn("answer2");

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertEquals("answer1", result);
 verify(stubbedInvocationMatchers.get(0)).matches(invocation);
 verify(stubbedInvocation).answer(invocation);
 verifyZeroInteractions(stubbedInvocationMatchers.get(1));
 }
}