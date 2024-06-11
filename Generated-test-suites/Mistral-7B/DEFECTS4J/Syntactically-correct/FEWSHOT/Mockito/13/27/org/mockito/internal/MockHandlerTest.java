package org.mockito.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
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
import org.mockito.stubbing.AnswerWith;
import org.mockito.stubbing.AnswerWithThrowable;
import org.mockito.stubbing.AnswerWithValue;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

 private MockHandler<Object> handler;
 private MockSettingsImpl mockSettings;
 private Invocation invocation;
 private InvocationMatcher invocationMatcher;
 private InvocationContainer invocationContainer;
 private StubbedInvocationMatcher stubbedInvocation;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 handler = new MockHandler<Object>(mockSettings);
 invocation = mock(Invocation.class);
 invocationContainer = mock(InvocationContainer.class);
 }

 @Test
 public void testHandle_Stubbing() throws Throwable {
 when(handler.getInvocationContainer()).thenReturn(invocationContainer);
 when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);

 Answer answer = new AnswerWithValue(1);
 InvocationMatcher mockMatcher = mock(InvocationMatcher.class);

 handler.setAnswersForStubbing(answer);
 stubbedInvocation = new StubbedInvocationMatcher(invocationMatcher, new AnswerWithValue(2));
 invocationContainer.setAnswerFor(invocationMatcher, stubbedInvocation);

 verify(invocationContainer).setAnswerFor(invocationMatcher);

 Object result = handler.handle(invocation);

 assertNull(result);
 }

 @Test
 public void testHandle_Verification() throws Throwable {
 when(handler.getMockingProgress()).thenReturn(new ThreadSafeMockingProgress());
 when(handler.getMockSettings()).thenReturn(mockSettings);
 when(handler.getInvocationContainer()).thenReturn(invocationContainer);

 VerificationMode verificationMode = mock(VerificationMode.class);
 VerificationDataImpl verificationData = new VerificationDataImpl(invocationContainer, mockMatcher);

 handler.mockingProgress.pushVerificationMode(verificationMode);

 handler.handle(invocation);

 verify(verificationMode).verify(verificationData);
 }

 @Test
 public void testHandle_Verification_DifferentMock() throws Throwable {
 Object otherMock = mock(Object.class);
 VerificationMode verificationMode = mock(VerificationMode.class);
 VerificationDataImpl verificationData = new VerificationDataImpl(invocationContainer, mockMatcher);
 otherMock.handle(invocation);
 handler.mockingProgress.pushVerificationMode(verificationMode);

 assertFalse(verificationMode.verify(verificationData).isSatisfied());
 }

 @Test
 public void testHandle_VoidMethodStubbable() throws Throwable {
 VoidMethodStubbable<Object> voidMethodStubbable = handler.voidMethodStubbable(mock(Object.class));
 voidMethodStubbable.doAnswer(new AnswerWithThrowable(new RuntimeException("Test Exception"))).when(mock(Object.class));

 handler.handle(invocation);

 verifyZeroInteractions(mock(Object.class));
 }

 @Test
 public void testHandle_Answer() throws Throwable {
 Answer answer = new AnswerWithValue(1);
 handler.setAnswersForStubbing(answer);

 Object result = handler.handle(invocation);

 assertEquals(1, result);
 }

 @Test
 public void testHandle_AnswerWithThrowable() throws Throwable {
 Answer answer = new AnswerWithThrowable(new RuntimeException("Test Exception"));
 handler.setAnswersForStubbing(answer);

 try {
 handler.handle(invocation);
 fail();
 } catch (RuntimeException e) {
 // expected
 assertEquals("Test Exception", e.getMessage());
 }
 }

 @Test
 public void testHandle_AnswerWithValue() throws Throwable {
 Answer answer = new AnswerWithValue("Test String");
 handler.setAnswersForStubbing(answer);

 assertEquals("Test String", handler.handle(invocation));
 }
}