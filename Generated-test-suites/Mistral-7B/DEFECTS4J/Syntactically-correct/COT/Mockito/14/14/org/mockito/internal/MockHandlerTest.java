package org.mockito.internal.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class MockHandlerTest {

 private MockHandler<MockitoInvocationHandler> mockHandler;
 private MockSettingsImpl mockSettings;
 private InvocationContainer invocationContainer;
 private Invocation invocation;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 invocationContainer = mockHandler.getInvocationContainer();
 invocation = mock(InvocationOnMock.class);
 }

 @Test
 public void testHandle_stubbingVoidMethods() {
 doAnswer(answers(1)).when(mockHandler).handle(eq(invocation));
 invocation.callRealMethod();
 verify(invocationContainer).setAnswersForStubbing(answers(1));
 verifyNoMoreInteractions(invocationContainer);
 }

 @Test
 public void testHandle_verificationModeNotNull() {
 doReturn(new VerificationDataImpl(Arrays.asList(new VerificationDataImpl.VerificationData("mockHandler", new InvocationOnMock(null))),
 new StubbedInvocationMatcher(invocation, eq("any method")))).when(mockingProgress).pullVerificationMode();
 doAnswer(answers(2)).when(mockHandler).handle(eq(invocation));
 mockingProgress.pullVerificationMode();
 verify(invocationContainer).setInvocations(Arrays.asList(new VerificationDataImpl.VerificationData("mockHandler", new InvocationOnMock(null))));
 verify(invocationContainer).setMatchers(new StubbedInvocationMatcher(invocation, eq("any method")));
 verifyNoMoreInteractions(invocationContainer);
 }

 @Test
 public void testHandle_verificationModeNull() {
 doReturn(null).when(mockingProgress).pullVerificationMode();
 doAnswer(answers(3)).when(mockHandler).handle(eq(invocation));
 verifyNoMoreInteractions(invocationContainer);
 }

 @Test(expected = RuntimeException.class)
 public void testHandle_throwException() {
 doThrow(new RuntimeException()).when(mockHandler).handle(eq(invocation));
 invocation.callRealMethod();
 }

 @Test
 public void testHandle_defaultAnswer() {
 Answer answer = mock(Answer.class);
 doReturn(answer).when(mockSettings).getDefaultAnswer();
 Object result = mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testHandle_setAnswersForStubbing() {
 List<Answer> answers = Arrays.asList(answers(4), answers(5));
 mockHandler.setAnswersForStubbing(answers);
 assertEquals(answers, invocationContainer.getAnswersForStubbing());
 }

 @Test
 public void testVoidMethodStubbable_stubbingVoidMethod() {
 VoidMethodStubbable<MockitoInvocationHandler> stub = mockHandler.voidMethodStubbable(mock(MockitoInvocationHandler.class));
 doAnswer(answers(6)).when(stub).stubVoid();
 stub.stubVoid();
 verify(invocationContainer).setAnswersForStubbing(answers(6));
 verifyNoMoreInteractions(invocationContainer);
 }

 @Test
 public void testGetMockSettings() {
 assertSame(mockSettings, mockHandler.getMockSettings());
 }

 @Test
 public void testSetAnswersForStubbing_resettingInvocationForPotentialStubbing() {
 List<Answer> answers = Arrays.asList(answers(7), answers(8));
 doAnswer(answers(7)).when(mockHandler).handle(eq(invocation));
 invocationContainer.setInvocationForPotentialStubbing(stubbedInvocation(eq("methodToStub"), eq("arg1"), anyList()));
 doAnswer(answers(8)).when(mockHandler).handle(eq(invocation));
 mockingProgress.reportOngoingStubbing(new OngoingStubbingImpl<MockitoInvocationHandler>(invocationContainer));
 mockingProgress.validateState();
 invocationContainer.resetInvocationForPotentialStubbing(stubbedInvocation(eq("methodToStub"), eq("arg1"), anyList()));
 verifyNoMoreInteractions(invocationContainer);
 }

 private Answer answers(int answerIndex) {
 return new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return answerIndex;
 }
 };
}

 private StubbedInvocationMatcher stubbedInvocation(Object methodToStub, Object arg1, List<Object> argList) {
 return new StubbedInvocationMatcher(invocation, argThat(invocation -> invocation.getMethod().getName().equals(methodToStub.toString()) && invocation.getArguments()[0].equals(arg1) && invocation.getArguments().subList(1, invocation.getArguments().size()).equals(argList)));
}

}