package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import org.mockito.internal.MockHandler;

@RunWith(MockitoJUnitRunner.class)
public class MockHandlerTest {

 MockHandler<Object> mockHandler;
 InvocationContainer invocationContainer;
 MockSettingsImpl mockSettings;
 Invocation invocation;
 InvocationMatcher invocationMatcher;
 StubbedInvocationMatcher stubbedInvocation;

 @Test
 public void testInitialization() {
 mockHandler = new MockHandler<>();
 assertNull(mockHandler.getMockSettings());
 assertNull(mockHandler.getInvocationContainer());
 }

 @Test
 public void testInitializationWithMockSettings() {
 mockSettings = mock(MockSettingsImpl.class);
 mockHandler = new MockHandler(mockSettings);
 assertSame(mockSettings, mockHandler.getMockSettings());
 assertNull(mockHandler.getInvocationContainer());
 }

 @Test
 public void testInitializationWithOldMockHandler() {
 MockHandler<Object> oldMockHandler = mock(MockHandler.class);
 mockHandler = new MockHandler(oldMockHandler);
 assertSame(oldMockHandler.getMockSettings(), mockHandler.getMockSettings());
 assertNull(mockHandler.getInvocationContainer());
 }

 @Test
 public void testHandleInvocationWithNoVerificationOrStubbing() throws Throwable {
 mockHandler = new MockHandler<>();
 invocation = mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("testMethod", String.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ new String("test") });

 Object result = mockHandler.handle(invocation);
 assertEquals(mockSettings.getDefaultAnswer().answer(invocation), result);
 }

 @Test
 public void testHandleInvocationWithStubbing() throws Throwable {
 mockHandler = new MockHandler<>();
 invocation = mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("testMethod", String.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ new String("test") });

 InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
 mockHandler.matchersBinder.bindMatchers(Collections.emptyList(), invocation).thenReturn(invocationMatcher);
 mockHandler.invocationContainerImpl.setAnswersForStubbing(Arrays.asList(createAnswer()));

 mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testHandleInvocationWithVerification() throws Throwable {
 mockHandler = new MockHandler<>();
 invocation = mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("testMethod", String.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ new String("test") });

 InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
 mockHandler.matchersBinder.bindMatchers(Collections.emptyList(), invocation).thenReturn(invocationMatcher);

 VerificationMode verificationMode = verify(any(Object.class)).testMethod("test");
 mockHandler.mockingProgress.pullVerificationMode().thenReturn(verificationMode);

 mockHandler.handle(invocation);
 assertNull(result);

 verify(any(Object.class)).testMethod("test");
 }

 @Test
 public void testHandleInvocationWithVerificationAndStubbing() throws Throwable {
 mockHandler = new MockHandler<>();
 invocation = mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("testMethod", String.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ new String("test") });

 InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
 mockHandler.matchersBinder.bindMatchers(Collections.emptyList(), invocation).thenReturn(invocationMatcher);

 VerificationMode verificationMode = verify(any(Object.class)).testMethod("test");
 mockHandler.mockingProgress.pullVerificationMode().thenReturn(verificationMode);

 stubbedInvocation = mock(StubbedInvocationMatcher.class);
 mockHandler.invocationContainerImpl.setAnswersForStubbing(Arrays.asList(new Answer() {
 @Override public Object answer(Invocation invocation) throws Throwable {
 return "stubbed result";
 }
 })).thenReturn(stubbedInvocation);
 stubbedInvocation.captureArgumentsFrom(invocation).thenReturn(new Object[]{ new String("test2") });

 Object result = mockHandler.handle(invocation);
 assertEquals("stubbed result", result);

 verify(any(Object.class)).testMethod("test");
 }

 @Test
 public void testHandleInvocationWithVerificationAndNoAnswer() throws Throwable {
 mockHandler = new MockHandler<>();
 invocation = mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("testMethod", String.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ new String("test") });

 InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
 mockHandler.matchersBinder.bindMatchers(Collections.emptyList(), invocation).thenReturn(invocationMatcher);

 VerificationMode verificationMode = verify(any(Object.class)).testMethod("test");
 mockHandler.mockingProgress.pullVerificationMode().thenReturn(verificationMode);

 mockHandler.handle(invocation);
 assertNull(result);

 verify(any(Object.class)).testMethod("test");
 }

 @Test
 public void testHandleInvocationWithVerificationAndMultipleAnswers() throws Throwable {
 mockHandler = new MockHandler<>();
 invocation = mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("testMethod", String.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ new String("test") });

 InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
 mockHandler.matchersBinder.bindMatchers(Collections.emptyList(), invocation).thenReturn(invocationMatcher);

 VerificationMode verificationMode = verify(any(Object.class)).testMethod("test");
 mockHandler.mockingProgress.pullVerificationMode().thenReturn(verificationMode);

 List<Answer> answers = Arrays.asList(new Answer() {
 @Override public Object answer(Invocation invocation) throws Throwable {
 return "answer1";
 }
 }, new Answer() {
 @Override public Object answer(Invocation invocation) throws Throwable {
 return "answer2";
 }
 });
 mockHandler.invocationContainerImpl.setAnswersForStubbing(answers).thenReturn(stubbedInvocation);

 Object result = mockHandler.handle(invocation);
 assertEquals("answer2", result);

 verify(any(Object.class)).testMethod("test");
 }

 @Test
 public void testSetAnswersForStubbing() {
 mockHandler = new MockHandler<>();
 List<Answer> answers = Arrays.asList(new Answer() {
 @Override public Object answer(Invocation invocation) throws Throwable {
 return "answer1";
 }
 }, new Answer() {
 @Override public Object answer(Invocation invocation) throws Throwable {
 return "answer2";
 }
 });
 mockHandler.setAnswersForStubbing(answers);
 assertEquals(answers, mockHandler.invocationContainerImpl.getAnswersForStubbing());
 }

 @Test
 public void testHandleInvocationWithNullInvocation() throws Throwable {
 mockHandler = new MockHandler<>();
 invocation = null;
 mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testHandleInvocationWithNullAnswer() throws Throwable {
 mockHandler = new MockHandler<>();
 invocation = mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("testMethod", String.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ new String("test") });

 List<Answer> answers = Arrays.asList(new Answer() {
 @Override public Object answer(Invocation invocation) throws Throwable {
 return null;
 }
 });
 mockHandler.setAnswersForStubbing(answers).thenReturn(stubbedInvocation);
 mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testHandleInvocationWithNoMatchingInvocationForStubbing() throws Throwable {
 mockHandler = new MockHandler<>();
 invocation = mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("testMethod", String.class));
 when(invocation.getArguments()).thenReturn(new Object[]{ new String("test") });

 InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
 mockHandler.matchersBinder.bindMatchers(Collections.emptyList(), invocation).thenReturn(invocationMatcher);

 mockHandler.setAnswersForStubbing(Arrays.asList(createAnswer()));
 mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testVoidMethodStubbable() {
 VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock(Object.class));
 assertSame(mockHandler, voidMethodStubbable.getMockHandler());
 }

 @Test
 public void testHandleInvocationWithVerificationAndStubbingAndMultipleInvocations() throws Throwable {
 mockHandler = new MockHandler<>();
 Invocation invocation1 = mock(Invocation.class);
 when(invocation1.getMethod()).thenReturn(Object.class.getMethod("testMethod", String.class));
 when(invocation1.getArguments()).thenReturn(new Object[]{ new String("test1") });

 Invocation invocation2 = mock(Invocation.class);
 when(invocation2.getMethod()).thenReturn(Object.class.getMethod("testMethod", String.class));
 when(invocation2.getArguments()).thenReturn(new Object[]{ new String("test2") });

 InvocationMatcher invocationMatcher1 = mock(InvocationMatcher.class);
 mockHandler.matchersBinder.bindMatchers(Collections.emptyList(), invocation1).thenReturn(invocationMatcher1);

 InvocationMatcher invocationMatcher2 = mock(InvocationMatcher.class);
 mockHandler.matchersBinder.bindMatchers(Collections.emptyList(), invocation2).thenReturn(invocationMatcher2);

 VerificationMode verificationMode = verify(any(Object.class)).testMethod("test1");
 mockHandler.mockingProgress.pullVerificationMode().thenReturn(verificationMode);

 List<Answer> answers = Arrays.asList(new Answer() {
 @Override public Object answer(Invocation invocation) throws Throwable {
 return "answer1";
 }
 });
 mockHandler.invocationContainerImpl.setAnswersForStubbing(answers).thenReturn(stubbedInvocation);

 Object result1 = mockHandler.handle(invocation1);
 assertEquals("answer1", result1);

 verify(any(Object.class)).testMethod("test1");

 verificationMode = verify(any(Object.class)).testMethod("test2");
 mockHandler.mockingProgress.pullVerificationMode().thenReturn(verificationMode);

 result1 = mockHandler.handle(invocation2);
 assertEquals("answer1", result1);

 verify(any(Object.class)).testMethod("test2");
 }

 @Test
 public void testHandleInvocationWithVerificationAndStubbingAndMultipleAnswers() throws Throwable {
 mockHandler = new MockHandler<>();
 Invocation invocation1 = mock(Invocation.class);
 when(invocation1.getMethod()).thenReturn(Object.class.getMethod("testMethod", String.class));
 when(invocation1.getArguments()).thenReturn(new Object[]{ new String("test1") });

 Invocation invocation2 = mock(Invocation.class);
 when(invocation2.getMethod()).thenReturn(Object.class.getMethod("testMethod", String.class));
 when(invocation2.getArguments()).thenReturn(new Object[]{ new String("test2") });

 InvocationMatcher invocationMatcher1 = mock(InvocationMatcher.class);
 mockHandler.matchersBinder.bindMatchers(Collections.emptyList(), invocation1).thenReturn(invocationMatcher1);

 InvocationMatcher invocationMatcher2 = mock(InvocationMatcher.class);
 mockHandler.matchersBinder.bindMatchers(Collections.emptyList(), invocation2).thenReturn(invocationMatcher2);

 VerificationMode verificationMode = verify(any(Object.class)).testMethod("test1");
 mockHandler.mockingProgress.pullVerificationMode().thenReturn(verificationMode);

 List<Answer> answers1 = Arrays.asList(new Answer() {
 @Override public Object answer(Invocation invocation) throws Throwable {
 return "answer1.1";
 }
 }, new Answer() {
 @Override public Object answer(Invocation invocation) throws Throwable {
 return "answer1.2";
 }
 });
 mockHandler.invocationContainerImpl.setAnswersForStubbing(answers1).thenReturn(stubbedInvocation);

 Object result1 = mockHandler.handle(invocation1);
 assertEquals("answer1.2", result1);

 verify(any(Object.class)).testMethod("test1");

 verificationMode = verify(any(Object.class)).testMethod("test2");
 mockHandler.mockingProgress.pullVerificationMode().thenReturn(verificationMode);

 List<Answer> answers2 = Arrays.asList(new Answer() {
 @Override public Object answer(Invocation invocation) throws Throwable {
 return "answer2.1";
 }
 }, new Answer() {
 @Override public Object answer(Invocation invocation) throws Throwable {
 return "answer2.2";
 }
 });
 mockHandler.invocationContainerImpl.setAnswersForStubbing(answers2).thenReturn(stubbedInvocation);

 Object result2 = mockHandler.handle(invocation2);
 assertEquals("answer2.1", result2);

 verify(any(Object.class)).testMethod("test2");
 }

 private Answer createAnswer() {
 return new Answer() {
 @Override public Object answer(Invocation invocation) throws Throwable {
 return "answer";
 }
 };
 }
}