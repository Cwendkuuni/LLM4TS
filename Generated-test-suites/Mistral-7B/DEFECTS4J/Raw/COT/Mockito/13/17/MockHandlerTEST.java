package org.mockito.internal.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerUtils;
import org.mockito.stubbing.Stub;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(org.mockito.runners.JUnit4.class)
public class MockHandlerTest {

 private MockHandler<Object> mockHandler;
 private Object mock;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 mockHandler = new MockHandler<>();
 mock = mock(Object.class);
 }

 @Test
 public void testHandle_noAnswersOrVerifications_returnDefaultAnswer() throws Throwable {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);
 when(invocation.getMethod()).thenReturn(this.getClass().getMethod("testMethod", String.class));

 Object result = mockHandler.handle(invocation);

 verifyZeroInteractions(mockHandler.invocationContainerImpl);
 verifyZeroInteractions(mockHandler.matchersBinder);
 verifyZeroInteractions(mockHandler.mockingProgress);

 Assert.assertEquals(mockSettings.getDefaultAnswer().answer(invocation), result);
 }

 @Test
 public void testHandle_answersPresent_returnAnswer() throws Throwable {
 List<Answer> answers = new ArrayList<>();
 answers.add(AnswerUtils.returnValues(42));
 when(mockHandler.getInvocationContainer().getAnswersForStubbing()).thenReturn(answers);

 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);
 when(invocation.getMethod()).thenReturn(this.getClass().getMethod("testMethod", String.class));

 Object result = mockHandler.handle(invocation);

 verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
 verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(isNull());
 verify(mockHandler.matchersBinder).bindMatchers(isNull(), invocation);
 verifyNoMoreInteractions(mockHandler.invocationContainerImpl);
 verifyNoMoreInteractions(mockHandler.matchersBinder);

 Assert.assertEquals(42, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_verificationModeNotEmpty_throwException() throws Throwable {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);
 when(invocation.getMethod()).thenReturn(this.getClass().getMethod("testMethod", String.class));

 VerificationMode verificationMode = mock(VerificationMode.class);
 mockHandler.mockingProgress.pushVerificationMode(verificationMode);

 mockHandler.handle(invocation);
 }

 @Test
 public void testHandle_verificationModeAndAnswerPresent_verificationModeExecutedFirst() throws Throwable {
 List<Answer> answers = new ArrayList<>();
 answers.add(AnswerUtils.returnValues(42));

 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);
 when(invocation.getMethod()).thenReturn(this.getClass().getMethod("testMethod", String.class));

 VerificationMode verificationMode = mock(VerificationMode.class);
 mockHandler.mockingProgress.pushVerificationMode(verificationMode);
 when(mockHandler.getInvocationContainer().getAnswersForStubbing()).thenReturn(answers);

 mockHandler.handle(invocation);

 verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(isNull());
 verify(mockHandler.matchersBinder).bindMatchers(isNull(), invocation);
 verify(verificationMode).verify(eq(invocation));
 }

 @Test
 public void testHandle_verificationModeAndAnswerPresent_answerExecutedAfterVerification() throws Throwable {
 List<Answer> answers = new ArrayList<>();
 answers.add(AnswerUtils.returnValues(42));
 answers.add(AnswerUtils.throwException(new RuntimeException("Test exception")));

 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);
 when(invocation.getMethod()).thenReturn(this.getClass().getMethod("testMethod", String.class));

 VerificationMode verificationMode = mock(VerificationMode.class);
 mockHandler.mockingProgress.pushVerificationMode(verificationMode);
 when(mockHandler.getInvocationContainer().getAnswersForStubbing()).thenReturn(answers);

 try {
 mockHandler.handle(invocation);
 fail("Expected exception");
 } catch (RuntimeException e) {
 // Expected
 }

 verify(verificationMode).verify(eq(invocation));
 }

 @Test
 public void testVoidMethodStubbable() throws Throwable {
 VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

 verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(isNull());
 verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(isNull());
 verify(mockHandler.matchersBinder).bindMatchers(isNull(), isNull());

 voidMethodStubbable.stubVoid();

 verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(isA(List.class));
 verify(mockHandler.matchersBinder).bindMatchers(isNull(), isNull());
 }

}