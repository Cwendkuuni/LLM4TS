package org.mockito.internal.MockHandlerTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.verifyZeroInteractions;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.mockito.internal.MockHandler;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MockSettingsImpl.AnswerType;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.stubbing.AnswerImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

 private MockHandler<MockitoTestClass> mockHandler;
 private MockitoTestClass mockitoTestClass;
 private Object mockObject;
 private InvocationOnMock invocation;
 private InvocationMatcher invocationMatcher;
 private StubbedInvocationMatcher stubbedInvocation;
 private List<Answer> answers;
 private Answer answer;
 private VerifyModeStub verifyModeStub;

 @Before
 public void setUp() {
 mockHandler = new MockHandler<>();
 mockitoTestClass = mock(MockitoTestClass.class);
 mockObject = mockitoTestClass;
 invocation = mock(InvocationOnMock.class);
 invocationMatcher = mock(InvocationMatcher.class);
 stubbedInvocation = mock(StubbedInvocationMatcher.class);
 answers = Arrays.asList(new AnswerImpl(mockitoTestClass), new AnswerImpl(mockitoTestClass));
 answer = new AnswerImpl(mockitoTestClass);
 verifyModeStub = mock(VerifyModeStub.class);
 }

 @Test
 public void handle_noAnswersForStubbing_returnDefaultAnswer() throws Throwable {
 when(mockSettings.getDefaultAnswer()).thenReturn(answer);
 when(mockingProgress.pullVerificationMode()).thenReturn(verifyModeStub);
 when(mockingProgress.getArgumentMatcherStorage()).thenReturn(Arrays.asList(invocationMatcher));

 when(invocation.getMock()).thenReturn(mockitoTestClass);

 Object result = mockHandler.handle(invocation);

 verify(mockingProgress).pullVerificationMode();
 verify(mockingProgress).getArgumentMatcherStorage().contains(invocationMatcher);
 verify(mockingProgress).validateState();
 verify(mockingProgress).reportOngoingStubbing(Mockito.any(OngoingStubbingImpl.class));

 verifyZeroInteractions(stubbedInvocation);
 verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);

 assertEquals(answer.answer(invocation), result);
 }

 @Test
 public void handle_answersForStubbing_returnStubbedAnswer() throws Throwable {
 when(mockSettings.getDefaultAnswer()).thenReturn(answer);
 when(mockingProgress.pullVerificationMode()).thenReturn(null);
 when(mockingProgress.getArgumentMatcherStorage()).thenReturn(Arrays.asList(invocationMatcher));
 when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
 when(stubbedInvocation.answer(invocation)).thenReturn("stubbed answer");

 when(invocation.getMock()).thenReturn(mockitoTestClass);

 Object result = mockHandler.handle(invocation);

 verify(mockingProgress).pullVerificationMode();
 verify(mockingProgress).getArgumentMatcherStorage().contains(invocationMatcher);
 verify(mockingProgress).validateState();
 verify(mockingProgress).reportOngoingStubbing(Mockito.any(OngoingStubbingImpl.class));
 verify(stubbedInvocation).captureArgumentsFrom(invocation);
 verify(stubbedInvocation).answer(invocation).eq("stubbed answer");

 verifyZeroInteractions(invocationContainerImpl);

 assertEquals("stubbed answer", result);
 }

 @Test
 public void handle_verificationModeIsNotEmpty_validateVerificationMode() throws Throwable {
 when(mockingProgress.pullVerificationMode()).thenReturn(verifyModeStub);
 when(verifyModeStub.getMock()).thenReturn(mockitoTestClass);
 when(verifyModeStub.getVerificationMode()).thenReturn(verifyZeroInteractions());

 when(invocation.getMock()).thenReturn(mockitoTestClass);

 mockHandler.handle(invocation);

 verify(mockingProgress).pullVerificationMode();
 verify(mockingProgress).validateState();
 verify(mockingProgress).reportOngoingStubbing(Mockito.any(OngoingStubbingImpl.class));
 verify(verifyModeStub).getMock().eq(mockitoTestClass);
 verify(verifyModeStub).getVerificationMode().verify(Mockito.any(VerificationDataImpl.class));
 }

 @Test
 public void handle_verificationModeIsEmpty_doNotValidateVerificationMode() throws Throwable {
 when(mockingProgress.pullVerificationMode()).thenReturn(null);

 when(invocation.getMock()).thenReturn(mockitoTestClass);

 mockHandler.handle(invocation);

 verify(mockingProgress).pullVerificationMode();
 verify(mockingProgress).validateState();
 verify(mockingProgress).reportOngoingStubbing(Mockito.any(OngoingStubbingImpl.class));
 verifyNoInteractions(verifyModeStub);
 }

 @Test (expected = AssertionError.class)
 public void handle_stubbingOnDifferentMock_throwsAssertionError() throws Throwable {
 when(mockingProgress.pullVerificationMode()).thenReturn(verifyZeroInteractions());
 when(invocation.getMock()).thenReturn(mockitoTestClass2);

 mockHandler.handle(invocation);
 }
}

class VerifyModeStub implements VerificationMode {
 @Override public void verify(VerificationData verificationData) {
 // Empty stubbed method
 }
}