package org.mockito.internal.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.Invocation;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.AnswerRunner;
import org.mockito.internal.stubbing.AnswerRunnerContext;
import org.mockito.internal.stubbing.Stub;
import org.mockito.internal.stubbing.VoidAnswer;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.InvocationContainer;
import org.mockito.internal.matchers.ArgumentMatcherStorage;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.VerificationDataImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyInteractions;

public class MockHandlerTest {

 private MockSettingsImpl mockSettings;
 private MockHandler<Foo> mockHandler;
 private Foo mock;
 private Invocation invocation;
 private StubbedInvocationMatcher invocationMatcher;
 private InvocationContainer invocationContainer;
 private Answer answer;
 private VerificationMode verificationMode;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 mock = mock(Foo.class);
 invocation = new Invocation(mock, "method", new Object[]{1, 2}, new Object[0]);
 invocationMatcher = new StubbedInvocationMatcher(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
 invocationContainer = mockHandler.getInvocationContainer();
 answer = new AnswerRunner(new VoidAnswer());
 }

 @Test
 public void testHandle_stubbing() throws Throwable {
 when(mockHandler.handle(invocation)).thenReturn(null);
 doAnswer(answer).when(mockHandler).handle(invocationMatcher);

 invocationContainer.setAnswersForStubbing(answers(answer));

 assertEquals(null, mockHandler.handle(invocation));
 verify(mockHandler).handle(invocationMatcher).answer(invocation);
 }

 @Test
 public void testHandle_verification() {
 doThrow(new RuntimeException("Exception")).when(mock).method();

 doNothing().when(mockHandler).handle(invocationMatcher);

 verificationMode = VerificationMode.times(1);
 mockingProgress.pushVerificationMode(verificationMode);

 mockHandler.handle(invocation);
 mockHandler.handle(invocation);

 verify(mock).method().throwsException(new RuntimeException("Exception"));
 verifyZeroInteractions(mock);
 }

 @Test
 public void testHandle_verification_multiple_mocks() {
 Foo mock1 = mock(Foo.class);
 Foo mock2 = mock(Foo.class);

 doThrow(new RuntimeException("Exception1")).when(mock1).method();
 doThrow(new RuntimeException("Exception2")).when(mock2).method();

 doNothing().when(mockHandler).handle(ArgumentMatchers.eq(mock1).or(ArgumentMatchers.eq(mock2))).thenCallRealMethod();

 verificationMode = VerificationMode.atLeast(1);
 mockingProgress.pushVerificationMode(verificationMode);

 mockHandler.handle(invocation);
 mockHandler.handle(invocation);
 mockHandler.handle(invocation);

 verifyInteractions(mock1, mock2);
 }

 @Test
 public void testHandle_verification_with_answers() {
 when(mock.method()).thenReturn(1);
 when(mockHandler.handle(invocationMatcher)).thenReturn(42);

 assertEquals(42, mockHandler.handle(invocation));
 verify(mock).method().thenReturn(1).thenReturn(42).thenCallRealMethod();
 }

 @Test
 public void testHandle_verification_with_stubbing() {
 when(mock.method()).thenReturn(1);
 doAnswer(answer).when(mockHandler).handle(invocationMatcher);

 assertEquals(null, mockHandler.handle(invocation));
 verify(mockHandler).handle(invocationMatcher).answer(invocation);
 }

 @Test
 public void testHandle_verification_with_multiple_stubbing() {
 when(mock.method()).thenReturn(1);
 doAnswer(answer).when(mockHandler).handle(invocationMatcher1).thenAnswer(answer2);

 assertEquals(null, mockHandler.handle(invocation1));
 assertEquals(null, mockHandler.handle(invocation2));
 verify(mockHandler).handle(invocationMatcher1).answer(invocation1);
 verify(mockHandler).handle(invocationMatcher2).answer(invocation2);
 }

 @Test
 public void testHandle_no_verification_or_stubbing() {
 assertEquals(1, mock.method().returnValue);
 assertEquals(1, mockHandler.handle(invocation).returnValue);
 }

 @Test
 public void testHandle_multiple_invocations() {
 when(mock.method()).thenReturn(1);
 doAnswer(answer).when(mockHandler).handle(invocationMatcher);
 invocationContainer.setAnswersForStubbing(answers(answer));

 assertEquals(1, mockHandler.handle(invocation));
 assertEquals(1, mockHandler.handle(invocation));
 assertEquals(1, mockHandler.handle(invocation));

 verify(mock).method().thenReturn(1).thenReturn(1).thenCallRealMethod();
 verify(mockHandler).handle(invocationMatcher).answer(invocation).answer(invocation).answer(invocation);
 }

 @Test
 public void testHandle_void_method_stubbing() {
 VoidMethodStubbable<Foo> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 doAnswer(answer).when(voidMethodStubbable).call();

 voidMethodStubbable.call();
 verify(mockHandler).handle(ArgumentMatchers.any(Invocation.class)).answer(ArgumentMatchers.isA(VoidAnswer.class));
 }

 private List<Answer> answers(Answer answer) {
 List<Answer> answers = new ArrayList<>();
 answers.add(answer);
 return answers;
 }
}