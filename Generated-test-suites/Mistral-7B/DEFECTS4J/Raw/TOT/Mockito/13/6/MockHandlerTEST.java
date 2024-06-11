package org.mockito.internal.test. MockHandlerTest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.*;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;
import org.mockito.invocation.MockitoInvocationHandler;
import org.mockito.MockitoSession;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.stubbing.AnswerWithCallsToRealMethod;
import org.mockito.internal.stubbing.Stubbing;
import org.mockito.internal.stubbing.VoidMethodStubbable;

import java.util.ArrayList;
import java.util.List;

public class MockHandlerTest {

 private MockHandler<Foo> mockHandler;
 private Foo mock;
 private List<Answer> answers;
 private Stubbing<VoidMethodStubbable<Foo>> voidMethodStubbable;

 @Before
 public void beforeEach() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<Foo>(mockSettings);
 mock = mock(Foo.class);
 answers = new ArrayList<>();
 voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 }

 @Test
 public void testHandleVoidMethodCall() {
 voidMethodStubbable.doReturn(true).when(mock);
 Invocation invocation = new Invocation(mock.getClass(), "someMethod", new Object[0]);
 assertNull(mockHandler.handle(invocation));
 verify(mock).someMethod();
 }

 @Test
 public void testHandleNonVoidMethodCallWithDefaultAnswer() {
 mockSettings.setDefaultAnswer(answer(eq(42)));
 Invocation invocation = new Invocation(mock.getClass(), "someMethod", new Object[0]);
 assertEquals(42, mockHandler.handle(invocation).intValue());
 }

 @Test
 public void testHandleNonVoidMethodCallWithCustomAnswer() {
 Answer customAnswer = answer(isA(String.class));
 mockHandler.setAnswersForStubbing(answers);
 answers.add(customAnswer);
 Invocation invocation = new Invocation(mock.getClass(), "someMethod", new Object[0]);
 assertInstanceOf(String.class, mockHandler.handle(invocation));
 verify(mock).someMethod();
 }

 @Test
 public void testHandleNonVoidMethodCallWithVerificationMode() {
 ArgumentMatcher<String> argumentMatcher = argThat(isA(String.class));
 VerificationMode verificationMode = verify(mock).someMethod(argumentMatcher);
 Invocation invocation = new Invocation(mock.getClass(), "someMethod", new Object[]{new String("test")});
 mockHandler.handle(invocation);
 assertEquals(verificationMode, verificationMode);
 }

 @Test
 public void testHandleNonVoidMethodCallWithTwoVerifications() {
 ArgumentMatcher<String> firstArgumentMatcher = argThat(isA(String.class));
 ArgumentMatcher<String> secondArgumentMatcher = argThat(isA(String.class));
 VerificationMode firstVerificationMode = verify(mock).someMethod(firstArgumentMatcher);
 VerificationMode secondVerificationMode = verify(mock).someMethod(secondArgumentMatcher);
 Invocation invocation1 = new Invocation(mock.getClass(), "someMethod", new Object[]{new String("test1")});
 Invocation invocation2 = new Invocation(mock.getClass(), "someMethod", new Object[]{new String("test2")});
 mockHandler.handle(invocation1);
 mockHandler.handle(invocation2);
 assertEquals(firstVerificationMode, firstVerificationMode);
 assertEquals(secondVerificationMode, secondVerificationMode);
 }

 @Test
 public void testHandleNonVoidMethodCallWithMixedVerificationAndStubbing() {
 ArgumentMatcher<String> argumentMatcher = argThat(isA(String.class));
 VerificationMode verificationMode = verify(mock).someMethod(argumentMatcher);
 mockHandler.handle(new Invocation(mock.getClass(), "someMethod", new Object[]{new String("test")}));
 mockHandler.setAnswersForStubbing(answers);
 answers.add(answer(eq("test answer")));
 Invocation invocation = new Invocation(mock.getClass(), "someMethod", new Object[0]);
 assertEquals("test answer", mockHandler.handle(invocation));
 assertEquals(verificationMode, verificationMode);
 }

 @Test
 public void testHandleNonVoidMethodCallWithMixedVerificationAndStubbing2() {
 ArgumentMatcher<String> argumentMatcher = argThat(isA(String.class));
 VerificationMode verificationMode = verify(mock).someMethod(argumentMatcher);
 mockHandler.handle(new Invocation(mock.getClass(), "someMethod", new Object[]{new String("test")}));
 mockHandler.setAnswersForStubbing(answers);
 answers.add(answer(isA(Integer.class).andReturn(42)));
 Invocation invocation = new Invocation(mock.getClass(), "someMethod", new Object[0]);
 assertEquals(42, mockHandler.handle(invocation).intValue());
 assertEquals(verificationMode, verificationMode);
 }

 @Test
 public void testHandleNonVoidMethodCallWithAnswerThatCallsRealMethod() {
 ArgumentMatcher<String> argumentMatcher = argThat(isA(String.class));
 Answer answer = new AnswerWithCallsToRealMethod();
 mockHandler.setAnswersForStubbing(answers);
 answers.add(answer);
 VerificationMode verificationMode = verify(mock).someMethod(argumentMatcher);
 Invocation invocation = new Invocation(mock.getClass(), "someMethod", new Object[]{new String("test")});
 mockHandler.handle(invocation);
 assertEquals(verificationMode, verificationMode);
 }

 private Answer answer(Object expectedValue) {
 return invocation -> expectedValue;
 }

 private AnswerWithCallsToRealMethod answerWithCallsToRealMethod() {
 return new AnswerWithCallsToRealMethod();
 }

 private static class Foo {
 void someMethod() {
 }
 }

 private MockSettingsImpl mockSettings;
}