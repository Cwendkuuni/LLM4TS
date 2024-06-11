package org.mockito.internal.tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MockSettingsImplTest;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.AnswerMockito;
import org.mockito.stubbing.AnswerStub;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

 private MockHandler<TestMock> mockHandler;
 private TestMock testMock;
 private MockSettingsImpl mockSettings;
 private MockingProgress mockingProgress;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImplTest().getMockSettings();
 mockingProgress = new ThreadSafeMockingProgress();
 mockHandler = new MockHandler<>(mockSettings);
 testMock = mock(TestMock.class, mockSettings);
 }

 @Test
 public void testHandleVoidMethods() {
 VoidMethodStubbable<TestMock> voidMethodStubbable = mockHandler.voidMethodStubbable(testMock);
 voidMethodStubbable.doReturn(null).when(testMock).voidMethod();

 verify(testMock).voidMethod(); // should call the stubbed method
 }

 @Test
 public void testHandleMethodsWithAnswer() {
 when(testMock.methodWithAnswer(1)).thenAnswer(AnswerMockito.returns(2));

 assertEquals(2, testMock.methodWithAnswer(1));
 }

 @Test
 public void testHandleMethodsWithAnswerAndArguments() {
 List<Answer> answers = Arrays.asList(AnswerStub.withAnswer(3), AnswerStub.withAnswer(4));
 mockHandler.setAnswersForStubbing(answers);

 when(testMock.methodWithAnswerAndArguments(1, anyInt(), anyInt()))
 .thenAnswer(AnswerStub.withArgumentsAndAnswer(1, 2, 3, 4));

 assertEquals(3, testMock.methodWithAnswerAndArguments(1, 2, 4));
 }

 @Test
 public void testHandleMethodsWithStubbedInvocation() {
 Invocation invocation = new Invocation(TestMock.class, "methodWithStubbedInvocation", new Object[] { "arg1" });
 InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
 StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(invocationMatcher);
 mockHandler.invocationContainer().setAnswerFor(stubbedInvocationMatcher, AnswerStub.withAnswer(1));

 when(testMock.methodWithStubbedInvocation("arg1")).thenAnswer(AnswerMockito.callRealMethod());

 assertEquals(1, testMock.methodWithStubbedInvocation("arg1"));
 }

 @Test
 public void testHandleMethodsWithVerificationMode() {
 VerificationMode verificationMode = mock(MockAwareVerificationMode.class);
 verify(verificationMode).verify(new VerificationDataImpl(mockHandler.invocationContainer(), invocationMatcher("methodWithVerificationMode", anyInt())));
 }

 @Test(expected = IllegalStateException.class)
 public void testHandleMethodsWithNullVerificationMode() {
 VerificationMode verificationMode = null;
 try {
 verify(verificationMode).verify(new VerificationDataImpl(mockHandler.invocationContainer(), invocationMatcher("methodWithNullVerificationMode", anyInt())));
 } catch (IllegalStateException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test
 public void testHandleMethodsWithAnswerAndArgumentsAndVerificationMode() {
 VerificationMode verificationMode = mock(MockAwareVerificationMode.class);
 List<Answer> answers = Arrays.asList(AnswerStub.withAnswer(1), AnswerStub.withAnswer(2));
 mockHandler.setAnswersForStubbing(answers);

 when(testMock.methodWithAnswerAndArgumentsAndVerificationMode(1, anyInt(), anyInt()))
 .thenAnswer(AnswerStub.withArgumentsAndAnswer(1, 2, 3, 4, verificationMode));

 assertEquals(4, testMock.methodWithAnswerAndArgumentsAndVerificationMode(1, 2, 4));
 verify(verificationMode).verify(new VerificationDataImpl(mockHandler.invocationContainer(), invocationMatcher("methodWithAnswerAndArgumentsAndVerificationMode", anyInt())));
 }
}

class TestMock {
 void voidMethod() {}
 int methodWithAnswer(int arg) { return arg; }
 int methodWithAnswerAndArguments(int arg1, int arg2) { return arg1 + arg2; }
 int methodWithStubbedInvocation(String arg) {}
 int methodWithVerificationMode(int arg) {}
 int methodWithAnswerAndArgumentsAndVerificationMode(int arg1, int arg2, int arg3) {}
}