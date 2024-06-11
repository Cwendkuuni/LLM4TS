package org.mockito.internal.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWithContext;

import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;

public class MockHandlerTest {

 private MockHandler<Foo> mockHandler;
 private Foo mock;

 @Before
 public void setUp() {
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<Foo>(mockSettings);
 mock = mock(Foo.class);
 }

 @Test
 public void testHandle_noStubbingOrVerification_returnsDefaultAnswer() throws Throwable {
 Foo input = new Foo();
 Invocation invocation = new Invocation(Foo.class, "methodWithoutStubOrVerification", input);
 Object result = mockHandler.handle(invocation);
 Assert.assertEquals(mockSettings.getDefaultAnswer(), result);
 }

 @Test
 public void testHandle_stubbing_returnsStubbedAnswer() throws Throwable {
 Foo input = new Foo();
 Object expectedAnswer = "stubbed answer";
 Answer answer = Mockito.answer(invocation -> expectedAnswer);

 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl(Arrays.asList(answer)));

 Invocation invocation = new Invocation(Foo.class, "methodWithStub", input);
 Object result = mockHandler.handle(invocation);
 Assert.assertEquals(expectedAnswer, result);
 }

 @Test
 public void testHandle_verification_doesNotThrowException() throws Throwable {
 Foo input = new Foo();
 Invocation invocation = new Invocation(Foo.class, "methodWithVerification", input);

 VerificationMode verificationMode = Mockito.verify(mock, Mockito.times(1)).methodWithVerification(input);

 mockHandler = new MockHandler<Foo>(mockHandler); // reuse the same mockHandler instance

 Object result = mockHandler.handle(invocation);
 Assert.assertNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_verification_throwsException_onDifferentMock() throws Throwable {
 Foo mockA = mock(Foo.class);
 Foo mockB = mock(Foo.class);

 VerificationMode verificationModeA = Mockito.verify(mockA, Mockito.times(1)).methodWithVerification(new Foo());
 VerificationMode verificationModeB = Mockito.verify(mockB, Mockito.times(1)).methodWithVerification(new Foo());

 mockHandler = new MockHandler<Foo>(mockHandler); // reuse the same mockHandler instance

 Invocation invocation = new Invocation(Foo.class, "methodWithVerification", new Foo());
 mockHandler.handle(invocation);

 // Verify on the wrong mock
 verify(mockB, Mockito.times(1)).methodWithVerification(new Foo());
 }

 @Test
 public void testHandle_exception_isThrown() throws Throwable {
 Foo input = new Foo();
 Invocation invocation = new Invocation(Foo.class, "methodWithException", input);

 when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(new Throwable("Expected exception"));

 Object result = mockHandler.handle(invocation);
 Assert.assertThrows(Throwable.class, () -> mockHandler.handle(invocation));
 }

 @Test
 public void testVoidMethodStubbable_stubsVoidMethod() {
 VoidMethodStubbable<Foo> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 voidMethodStubbable.doAnswer(Mockito.mock(Answer.class));
 }

 @Test
 public void testGetMockSettings() {
 MockSettingsImpl mockSettings = mockHandler.getMockSettings();
 // Additional tests for getMockSettings() method, if needed
 }

 @Test
 public void testSetAnswersForStubbing() {
 List<Answer> answers = Arrays.asList(Mockito.mock(Answer.class), Mockito.mock(Answer.class));
 mockHandler.setAnswersForStubbing(answers);
 // Additional tests for setAnswersForStubbing() method, if needed
 }

 @Test
 public void testGetInvocationContainer() {
 InvocationContainer invocationContainer = mockHandler.getInvocationContainer();
 // Additional tests for getInvocationContainer() method, if needed
 }
}