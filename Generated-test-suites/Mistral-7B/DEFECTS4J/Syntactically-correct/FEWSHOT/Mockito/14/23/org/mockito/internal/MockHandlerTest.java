package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.mockito.internal.verification.VerificationModeFactory.never;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito. ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.AnswerStub;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

 private MockSettingsImpl mockSettings;
 private MockHandler<Object> mockHandler;
 private InvocationContainer invocationContainer;
 private VoidMethodStubbable<Object> voidMethodStubbable;
 private StubbedInvocationMatcher stubbedInvocationMatcher;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 invocationContainer = mockHandler.getInvocationContainer();
 voidMethodStubbable = mockHandler.voidMethodStubbable(Mockito.mock(Object.class));
 }

 @Test
 public void testHandle_stubbingVoid() {
 // given
 AnswerStub answerStub = AnswerStub.returnValues(42);
 mockHandler.setAnswersForStubbing(Arrays.asList(answerStub));

 // when
 voidMethodStubbable.stubVoid();

 // then
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.doNothing().when(invocation);
 InvocationMatcher invocationMatcher = Mockito.argumentThat(ArgumentMatchers.isInstance(invocation));
 invocationContainer.setMethodForStubbing(invocationMatcher);

 // verify
 Mockito.verify(invocation).proceed();
 Mockito.verify(voidMethodStubbable).voidMethod();
 }

 @Test
 public void testHandle_doAnswer() {
 // given
 ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
 AnswerStub answerStub = AnswerStub.callRealMethod();
 answerStub.givenAnswer(invocation -> integerArgumentCaptor.getValue(invocation).intValue() + 1);

 // when
 Object result = voidMethodStubbable.doAnswer(answerStub).when(Mockito.mock(Object.class)).method(ArgumentMatchers.eq(1));

 // then
 Mockito.verify(voidMethodStubbable).method(ArgumentMatchers.eq(1));
 Mockito.verify(voidMethodStubbable).method(ArgumentMatchers.eq(1)).thenReturn(result);
 Mockito.verify(voidMethodStubbable).method(ArgumentMatchers.eq(1)).thenReturn(result);
 }

 @Test
 public void testHandle_verify() {
 // given
 Object mock = Mockito.mock(Object.class);
 VerificationDataImpl verificationData = new VerificationDataImpl(Arrays.asList(
 new InvocationOnMock(Mockito.mock(InvocationOnMock.class), Arrays.asList(mock))),
 ArgumentMatchers.eq(1));

 // when
 VerificationMode verificationMode = times(2);
 mockHandler.handle(Mockito.mock(InvocationOnMock.class));

 // then
 Mockito.verify(mock, verificationMode).method(ArgumentMatchers.eq(1));
 Mockito.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testHandle_verify_differentMock() {
 // given
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 VerificationDataImpl verificationData1 = new VerificationDataImpl(Arrays.asList(
 new InvocationOnMock(Mockito.mock(InvocationOnMock.class), Arrays.asList(mock1))),
 ArgumentMatchers.eq(1));
 VerificationDataImpl verificationData2 = new VerificationDataImpl(Arrays.asList(
 new InvocationOnMock(Mockito.mock(InvocationOnMock.class), Arrays.asList(mock2))),
 ArgumentMatchers.eq(1));

 // when
 VerificationMode verificationMode = times(2);
 mockHandler.handle(Mockito.mock(InvocationOnMock.class));

 // then
 Mockito.verify(mock1, verificationMode).method(ArgumentMatchers.eq(1));
 Mockito.verify(mock2, never()).method(ArgumentMatchers.eq(1));
 }

 @Test
 public void testHandle_setAnswersForStubbing() {
 // given
 AnswerStub answerStub = AnswerStub.callRealMethod();

 // when
 mockHandler.setAnswersForStubbing(Arrays.asList(answerStub));

 // then
 InvocationMatcher invocationMatcher = Mockito.argumentThat(ArgumentMatchers.isInstance(Mockito.mock(InvocationOnMock.class)));
 StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(invocationMatcher);
 Invocation invocation = Mockito.mock(InvocationOnMock.class);
 invocationContainer.setAnswerFor(stubbedInvocationMatcher, answerStub);
 Mockito.verify(invocation).proceed();
 }

 @Test
 public void testHandle_setAnswersForStubbing_multipleAnswers() {
 // given
 AnswerStub answer1 = AnswerStub.returnValues(1);
 AnswerStub answer2 = AnswerStub.returnValues(2);

 // when
 mockHandler.setAnswersForStubbing(Arrays.asList(answer1, answer2));

 // then
 InvocationMatcher invocationMatcher = Mockito.argumentThat(ArgumentMatchers.isInstance(Mockito.mock(InvocationOnMock.class)));
 StubbedInvocationMatcher stubbedInvocationMatcher1 = new StubbedInvocationMatcher(invocationMatcher);
 StubbedInvocationMatcher stubbedInvocationMatcher2 = new StubbedInvocationMatcher(invocationMatcher);
 Invocation invocation = Mockito.mock(InvocationOnMock.class);
 invocationContainer.setAnswerFor(stubbedInvocationMatcher1, answer1);
 invocationContainer.setAnswerFor(stubbedInvocationMatcher2, answer2);

 Mockito.verify(invocation).proceed();
 Mockito.verify(invocation).proceed();
 }

 @Test
 public void testHandle_resetInvocationForPotentialStubbing() {
 // given
 InvocationMatcher invocationMatcher = Mockito.argumentThat(ArgumentMatchers.isInstance(Mockito.mock(InvocationOnMock.class)));
 StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(invocationMatcher);
 invocationContainer.setInvocationForPotentialStubbing(stubbedInvocationMatcher);

 // when
 mockHandler.handle(Mockito.mock(InvocationOnMock.class));
 invocationContainer.resetInvocationForPotentialStubbing(stubbedInvocationMatcher);

 // then
 Invocation invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.verify(invocation).proceed();
 Mockito.verify(invocation).proceed();
 }
}