package org.mockito.internal.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.stubbing.AnswerWithMockito;
import org.mockito.stubbing.AnswerWrapper;
import org.mockito.stubbing.Stub;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<SomeMockType> mockHandler;
 private ThreadSafeMockingProgress mockingProgress;
 private InvocationContainer invocationContainer;

 @Before
 public void setUp() {
 mockingProgress = new ThreadSafeMockingProgress();
 invocationContainer = new InvocationContainerImpl(mockingProgress);
 mockHandler = new MockHandler<SomeMockType>(new MockSettingsImpl()) {
 @Override
 public InvocationContainer getInvocationContainer() {
 return invocationContainer;
 }
 };
 }

 @Test
 public void testStubbingWithAnswer() {
 SomeMockType mock = mock(SomeMockType.class);
 when(mock.someMethod()).thenAnswer(new AnswerWithMockito<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "Hello World";
 }
 });

 assertEquals("Hello World", mock.someMethod());
 }

 @Test
 public void testStubbingWithStaticAnswer() {
 SomeMockType mock = mock(SomeMockType.class);
 when(mock.someMethod()).thenReturn("Hello World");

 assertEquals("Hello World", mock.someMethod());
 }

 @Test
 public void testStubbingWithNullAnswer() {
 SomeMockType mock = mock(SomeMockType.class);
 when(mock.someMethod()).thenReturn(null);

 assertNull(mock.someMethod());
 }

 @Test
 public void testStubbingWithThrowingAnswer() {
 SomeMockType mock = mock(SomeMockType.class);
 when(mock.someMethod()).thenThrow(new RuntimeException("Test Exception"));

 assertThrows(RuntimeException.class, () -> mock.someMethod());
 }

 @Test
 public void testStubbingWithStub() {
 SomeMockType mock = mock(SomeMockType.class);
 Stub stub = Mockito.stub(mock.getClass(), "someMethod");
 stub.returns("Hello World");

 assertEquals("Hello World", mock.someMethod());
 }

 @Test
 public void testStubbingWithVoidAnswer() {
 SomeMockType mock = mock(SomeMockType.class);
 doAnswer(new AnswerWrapper<Void>(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 System.out.println("Stubbed void method.");
 return null;
 }
 })).when(mock).someVoidMethod();

 mock.someVoidMethod();

 verify(mock).someVoidMethod();
 }

 @Test
 public void testVerification() {
 SomeMockType mock = mock(SomeMockType.class);
 mock.someMethod();
 mock.someMethod();

 verify(mock, ArgumentMatchers.atLeastOnce()).someMethod();
 }

 @Test
 public void testVerificationWithEmptyList() {
 SomeMockType mock = mock(SomeMockType.class);

 verifyZeroInteractions(mock);
 }

 @Test
 public void testVerificationWithVerificationMode() {
 SomeMockType mock = mock(SomeMockType.class);
 mock.someMethod();
 mock.someMethod();

 verify(mock, ArgumentMatchers.atLeast(2, mock.getClass(), "someMethod"));
 }

 @Test
 public void testVerificationWithOrder() {
 SomeMockType mock1 = mock(SomeMockType.class);SomeMockType mock2 = mock(SomeMockType.class);
 mock1.someMethod();
 mock2.someMethod();
 mock1.someMethod();

 verify(mock1).someMethod();
 verify(mock2).someMethod();
 verifyNoMoreInteractions(mock1, mock2);
 }

 @Test(expected = AssertionError.class)
 public void testVerificationWithIncorrectMock() {
 SomeOtherMockType mock = mock(SomeMockType.class);
 mock.someMethod();

 verify(mock, ArgumentMatchers.atLeastOnce()).someMethod();
 }

 @Test(expected = AssertionError.class)
 public void testVerificationWithTimeout() {
 SomeMockType mock = mock(SomeMockType.class, new MockitoSettings().withTimeout(100));
 mock.someMethod();
 Thread.sleep(200);

 verify(mock, ArgumentMatchers.atLeastOnce()).someMethod();
 }

 @Test
 public void testVerificationWithAnswerAndVerification() {
 SomeMockType mock = mock(SomeMockType.class);
 List<Answer<?>> answers = Collections.singletonList(new AnswerWithMockito<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "Answer";
 }
 });
 doAnswer(new AnswerWrapper<Object>(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 verify(mock).someMethod();
 return null;
 }
 })).when(mock).someMethod().thenAnswer(new AnswerWithMockito<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "Answer from answer";
 }
 }).thenAnswer(answers.get(0));

 assertEquals("Answer from answer", mock.someMethod());
 }

 @Test
 public void testVerificationWithVerificationAndAnswer() {
 SomeMockType mock = mock(SomeMockType.class);
 List<Answer<?>> answers = Collections.singletonList(new AnswerWithMockito<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "Answer";
 }
 });
 doAnswer(new AnswerWrapper<Object>(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 verify(mock).someMethod();
 return null;
 }
 })).when(mock).someMethod();
 doAnswer(answers.get(0)).when(mock).someOtherMethod();

 assertEquals("Answer", mock.someOtherMethod());
 }

 @Test
 public void testVerificationWithVerificationAndAnswerAndArgumentMatcher() {
 SomeMockType mock = mock(SomeMockType.class);
 List<Answer<?>> answers = Collections.singletonList(new AnswerWithMockito<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "Answer";
 }
 });
 doAnswer(new AnswerWrapper<Object>(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 verify(mock, ArgumentMatchers.eq("argument"));
 return null;
 }
 })).when(mock).someMethod(ArgumentMatchers.eq("argument"));
 doAnswer(answers.get(0)).when(mock).someOtherMethod();

 assertEquals("Answer", mock.someOtherMethod());
 }
}