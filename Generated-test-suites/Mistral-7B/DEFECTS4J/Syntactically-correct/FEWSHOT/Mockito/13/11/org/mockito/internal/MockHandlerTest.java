package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.matchers.Arguments.argThat;
import static org.mockito.internal.matchers.Arguments.isA;
import static org.mockito.internal.verification.VerificationModeFactory.verifyNoMoreInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.verifyZeroInteractions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
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
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Answer2;
import org.mockito.stubbing.Answer3;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

 private MockHandler<Object> mockHandler;
 private MockSettingsImpl mockSettings;
 private InvocationContainer invocationContainer;
 private Invocation invocation;
 private InvocationMatcher invocationMatcher;
 private StubbedInvocationMatcher stubbedInvocation;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 invocationContainer = mockHandler.getInvocationContainer();
 }

 @Test
 public void testHandleNullInvocation() {
 assertNull(mockHandler.handle(null));
 }

 @Test
 public void testHandleNoStubbing() throws Throwable {
 invocation = new Invocation(null, null, new Object[0], null);

 // no verification mode
 mockingProgress.setVerificationMode(null);

 Object result = mockHandler.handle(invocation);

 // no verification
 verifyZeroInteractions(mockHandler);

 assertEquals(mockSettings.getDefaultAnswer().answer(invocation), result);
 }

 @Test
 public void testHandleStubbing() throws Throwable {
 invocation = new Invocation(null, null, new Object[0], null);

 // verification mode
 MockAwareVerificationMode verificationMode = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 // stubbing
 Answer answer = org.mockito.mockito.mock(Answer.class);
 List<Answer> answers = Collections.singletonList(answer);
 mockHandler.setAnswersForStubbing(answers);

 invocationMatcher = mock(InvocationMatcher.class);
 invocationContainer.setInvocationForPotentialStubbing(invocationMatcher);

 // handling
 Object result = mockHandler.handle(invocation);

 // verify
 verifyNoMoreInteractions(verificationMode.getMock());

 assertNull(result);
 }

 @Test
 public void testHandleStubbingWithAnswer() throws Throwable {
 invocation = new Invocation(null, null, new Object[0], null);

 // verification mode
 MockAwareVerificationMode verificationMode = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 // stubbing
 Answer answer = org.mockito.mockito.mock(Answer.class);
 List<Answer> answers = Collections.singletonList(answer);
 mockHandler.setAnswersForStubbing(answers);

 // invocation matcher
 invocationMatcher = argThat(i -> i.getMethod().getName().equals("testMethod"));
 invocationContainer.setInvocationForPotentialStubbing(invocationMatcher);

 // handling
 stubbedInvocation = new StubbedInvocationMatcher(invocationMatcher, answer);
 invocationContainerImpl.setAnswerFor(stubbedInvocation);

 // handling
 Object result = mockHandler.handle(invocation);

 // verify
 verifyNoMoreInteractions(verificationMode.getMock());

 assertEquals(answer.answer(invocation), result);
 }

 @Test
 public void testHandleStubbingWithAnswer2() throws Throwable {
 invocation = new Invocation(null, null, new Object[0], null);

 // verification mode
 MockAwareVerificationMode verificationMode = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 // stubbing
 Answer2<Integer, String> answer = org.mockito.mockito.mock(Answer2.class);
 List<Answer> answers = Arrays.asList(answer);
 mockHandler.setAnswersForStubbing(answers);

 // invocation matcher
 invocationMatcher = argThat(i -> i.getMethod().getName().equals("testMethod"));
 invocationContainer.setInvocationForPotentialStubbing(invocationMatcher);

 // handling
 stubbedInvocation = new StubbedInvocationMatcher(invocationMatcher, answer);
 invocationContainerImpl.setAnswerFor(stubbedInvocation);

 // handling
 Object[] arguments = new Object[] {"foo", "bar"};
 Object result = mockHandler.handle(invocation);

 // verify
 verifyNoMoreInteractions(verificationMode.getMock());

 assertEquals(answer.answer(invocation, "foo", "bar"), result);
 }

 @Test
 public void testHandleStubbingWithAnswer3() throws Throwable {
 invocation = new Invocation(null, null, new Object[0], null);

 // verification mode
 MockAwareVerificationMode verificationMode = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 // stubbing
 Answer3<Integer, String, List<String>> answer = org.mockito.mockito.mock(Answer3.class);
 List<Answer> answers = Arrays.asList(answer);
 mockHandler.setAnswersForStubbing(answers);

 // invocation matcher
 invocationMatcher = argThat(i -> i.getMethod().getName().equals("testMethod"));
 invocationContainer.setInvocationForPotentialStubbing(invocationMatcher);

 // handling
 stubbedInvocation = new StubbedInvocationMatcher(invocationMatcher, answer);
 invocationContainerImpl.setAnswerFor(stubbedInvocation);

 // handling
 Object[] arguments = new Object[] {"foo", "bar", Arrays.asList("one", "two")};
 Object result = mockHandler.handle(invocation);

 // verify
 verifyNoMoreInteractions(verificationMode.getMock());

 assertEquals(answer.answer(invocation, "foo", "bar", Arrays.asList("one", "two")), result);
 }

 @Test
 public void testHandleVerification() throws Throwable {
 invocation = new Invocation(null, null, new Object[0], null);

 // verification mode
 MockAwareVerificationMode verificationMode = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 // handling
 Object result = mockHandler.handle(invocation);

 // verify
 verify(verificationMode.getMock()).testMethod();

 assertNull(result);
 }

 @Test
 public void testHandleVerificationWithAnswer() throws Throwable {
 invocation = new Invocation(null, null, new Object[0], null);

 // verification mode
 MockAwareVerificationMode verificationMode = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 // handling
 Object result = mockHandler.handle(invocation);

 // verify
 verify(verificationMode.getMock()).testMethod();

 assertEquals(mockSettings.getDefaultAnswer().answer(invocation), result);
 }

 @Test
 public void testHandleVerificationWithAnswerAndAnswer2() throws Throwable {
 invocation = new Invocation(null, null, new Object[0], null);

 // verification mode
 MockAwareVerificationMode verificationMode = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 // handling
 Object result = mockHandler.handle(invocation);

 // verify
 verify(verificationMode.getMock()).testMethod(eq("foo"), eq("bar"));

 assertNull(result);
 }

 @Test
 public void testHandleVerificationWithAnswer3() throws Throwable {
 invocation = new Invocation(null, null, new Object[0], null);

 // verification mode
 MockAwareVerificationMode verificationMode = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 // handling
 Object result = mockHandler.handle(invocation);

 // verify
 verify(verificationMode.getMock()).testMethod(eq("foo"), eq("bar"), argThat(isA(List.class)));

 assertNull(result);
 }

 @Test
 public void testHandleVerificationWithAnswerAndAnswerWithArgumentMatchers() throws Throwable {
 invocation = new Invocation(null, null, new Object[] {"foo"}, null);

 // verification mode
 MockAwareVerificationMode verificationMode = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 // handling
 Object result = mockHandler.handle(invocation);

 // verify
 verify(verificationMode.getMock()).testMethod(argThat(argThat(a -> a.equals("foo"))));

 assertNull(result);
 }

 @Test
 public void testHandleVerificationWithAnswer2WithArgumentMatchers() throws Throwable {
 invocation = new Invocation(null, null, new Object[] {"foo", "bar"}, null);

 // verification mode
 MockAwareVerificationMode verificationMode = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 // handling
 Object result = mockHandler.handle(invocation);

 // verify
 verify(verificationMode.getMock()).testMethod(argThat(argThat(argument -> argument.get(0).equals("foo"))), argThat(argThat(argument -> argument.get(1).equals("bar"))));

 assertNull(result);
 }

 @Test
 public void testHandleVerificationWithAnswer3WithArgumentMatchers() throws Throwable {
 invocation = new Invocation(null, null, new Object[] {"foo", "bar", Arrays.asList("one", "two")}, null);

 // verification mode
 MockAwareVerificationMode verificationMode = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 // handling
 Object result = mockHandler.handle(invocation);

 // verify
 verify(verificationMode.getMock()).testMethod(argThat(argThat(argument -> argument.get(0).equals("foo"))), argThat(argThat(argument -> argument.get(1).equals("bar"))), argThat(argThat(list -> list.contains("one"))));

 assertNull(result);
 }

 @Test
 public void testHandleVerificationWithVerificationModeChange() throws Throwable {
 invocation = new Invocation(null, null, new Object[0], null);

 // verification mode 1
 MockAwareVerificationMode verificationMode1 = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 Object result1 = mockHandler.handle(invocation);

 // verification mode 2
 MockAwareVerificationMode verificationMode2 = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 // verify 1
 verify(verificationMode1.getMock()).testMethod();

 // verify 2
 verifyNoMoreInteractions(verificationMode2.getMock());

 assertNull(result1);
 }

 @Test
 public void testHandleVerificationWithVerificationModeChangeAndAnswer() throws Throwable {
 invocation = new Invocation(null, null, new Object[0], null);

 // verification mode 1
 MockAwareVerificationMode verificationMode1 = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 Object result1 = mockHandler.handle(invocation);

 // verification mode 2
 MockAwareVerificationMode verificationMode2 = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 // handling
 Object answer = "answer";
 Answer answerAnswer = org.mockito.mockito.mock(Answer.class);
 answerAnswer.answer(invocation) -> answer;
 List<Answer> answers = Arrays.asList(answerAnswer);
 mockHandler.setAnswersForStubbing(answers);

 // handling
 stubbedInvocation = new StubbedInvocationMatcher(invocationMatcher, answerAnswer);
 invocationContainerImpl.setAnswerFor(stubbedInvocation);

 // verification mode 3
 MockAwareVerificationMode verificationMode3 = mockingProgress.setVerificationMode(argThat(v -> v instanceof MockAwareVerificationMode));

 // verify 1
 verify(verificationMode1.getMock()).testMethod();

 // verify 2
 verifyNoMoreInteractions(verificationMode2.getMock());

 // verify 3
 verify(verificationMode3.getMock()).testMethod();

 assertNull(result1);
 }

}