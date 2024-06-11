import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerCallback;
import org.mockito.stubbing.AnswerCallable;
import org.mockito.stubbing.AnswerStub;
import org.mockito.stubbing.OngoingStubbing;

import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MockSettingsBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MockitoInvocationHandler;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.AnswerAnswer;
import org.mockito.stubbing.AnswerCallableAnswer;
import org.mockito.stubbing.AnswerStubAnswer;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MockHandlerTest {

 private MockSettingsImpl mockSettings;
 private MockHandler<DummyClass> mockHandler;
 private DummyClass mock;
 private DummyClass spy;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsBuilder().build();
 mockHandler = new MockHandler<>(mockSettings);
 mock = Mockito.mock(DummyClass.class, mockHandler);
 spy = spy(DummyClass.class, mockHandler);
 }

 @Test
 public void testHandleInvocationWithStubbing() {
 // given
 when(mock.doSomething(ArgumentMatchers.anyInt())).thenReturn(1);

 // when
 int result = mock.doSomething(42);

 // then
 assertEquals(1, result);
 }

 @Test
 public void testHandleInvocationWithVerification() {
 // given
 doAnswer(invocationOnMock -> 1).when(mock).doSomething(ArgumentMatchers.anyInt());

 // when
 mock.doSomething(42);

 // then
 verify(mock).doSomething(ArgumentMatchers.anyInt());
 }

 @Test
 public void testHandleInvocationWithMultipleVerifications() {
 // given
 doAnswer(invocationOnMock -> 1).when(mock).doSomething(ArgumentMatchers.anyInt());
 doAnswer(invocationOnMock -> 2).when(mock).doAnotherThing(ArgumentMatchers.anyInt());

 // when
 mock.doSomething(42);
 mock.doAnotherThing(42);

 // then
 verify(mock).doSomething(ArgumentMatchers.anyInt()).times(2);
 verify(mock).doAnotherThing(ArgumentMatchers.anyInt()).times(1);
 }

 @Test
 public void testHandleInvocationWithStubbingAndVerification() {
 // given
 doAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 }).when(mock).doSomething(ArgumentMatchers.anyInt());
 verify(mock, never()).doAnotherThing(ArgumentMatchers.anyInt());

 // when
 int result = mock.doSomething(42);

 // then
 assertEquals(1, result);
 }

 @Test
 public void testHandleInvocationWithVoidStubbing() {
 // given
 mockHandler.voidMethodStubbable(mock).doSomething();

 // when
 // no action is performed as handle method returns null

 // then
 // no verification or stubbing is performed as we are testing void method
 }

 @Test
 public void testHandleInvocationWithOngoingStubbing() {
 // given
 OngoingStubbing<DummyClass> stubbing = when(mock.doSomething(ArgumentMatchers.anyInt()));

 // when
 stubbing.thenReturn(1);
 int result = mock.doSomething(42);

 // then
 assertEquals(1, result);
 }

 @Test
 public void testHandleInvocationWithOngoingStubbingWithAnswer() {
 // given
 OngoingStubbing<DummyClass> stubbing = when(mock.doSomething(ArgumentMatchers.anyInt()));

 // when
 stubbing.thenAnswer(new AnswerAnswer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 });
 int result = mock.doSomething(42);

 // then
 assertEquals(1, result);
 }

 @Test
 public void testHandleInvocationWithOngoingStubbingWithAnswerCallable() {
 // given
 OngoingStubbing<DummyClass> stubbing = when(mock.doSomething(ArgumentMatchers.anyInt()));

 // when
 stubbing.thenAnswer(new AnswerCallableAnswer<Integer>() {
 @Override
 public Integer call(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 });
 int result = mock.doSomething(42);

 // then
 assertEquals(1, result);
 }

 @Test
 public void testHandleInvocationWithOngoingStubbingWithAnswerStub() {
 // given
 OngoingStubbing<DummyClass> stubbing = when(mock.doSomething(ArgumentMatchers.anyInt()));
 AnswerStub<Integer> answerStub = Mockito.answer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 });
 stubbing.thenAnswer(answerStub);
 int result = mock.doSomething(42);

 // then
 assertEquals(1, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandleInvocationWithIllegalStateException() {
 // given
 doAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 throw new IllegalStateException();
 }
 }).when(mock).doSomething(ArgumentMatchers.anyInt());

 // when
 // this test case should throw an IllegalStateException
 mock.doSomething(42);
 }

 @Test
 public void testHandleInvocationWithAnswerWithArgumentMatcher() {
 // given
 doAnswer(arg -> arg.getInt()).when(mock).doSomething(ArgumentMatchers.argThat(arg -> arg.getInt() == 42));

 // when
 int result = mock.doSomething(new DummyArg(42));

 // then
 assertEquals(42, result.getInt());
 }

 @Test
 public void testHandleInvocationWithAnswerWithMatcher() {
 // given
 InvocationMatcher matcher = ArgumentMatchers.argThat(arg -> arg.equals(42));
 doAnswer(answer -> 1).when(mock).doSomething(matcher);

 // when
 int result = mock.doSomething(42);

 // then
 assertEquals(1, result);
 }

 @Test
 public void testHandleInvocationWithAnswerWithMatcherAndCapture() {
 // given
 InvocationMatcher matcher = ArgumentMatchers.argThat(arg -> arg.equals(42));
 doAnswer(answer -> answer.getArgument(0)).when(mock).doSomething(matcher);

 // when
 DummyClass arg = mock.doSomething(42);

 // then
 assertEquals(42, arg.getIntValue());
 }

 @Test
 public void testHandleInvocationWithAnswerWithMatcherAndCaptureAll() {
 // given
 InvocationMatcher matcher = ArgumentMatchers.argThat(arg -> arg.equals(42));
 doAnswer(answer -> answer.getArguments()).when(mock).doSomething(matcher);

 // when
 Object[] result = mock.doSomething(42);

 // then
 assertEquals(42, result[0].getIntValue());
 assertEquals(42, result[1].getIntValue());
 }

 @Test
 public void testHandleInvocationWithAnswerWithMatcherAndCaptureAndArgumentTransformer() {
 // given
 InvocationMatcher matcher = ArgumentMatchers.argThat(arg -> arg.equals(42));
 doAnswer(answer -> new DummyClass(answer.getArguments()[0].getIntValue())).when(mock).doSomething(matcher);

 // when
 DummyClass result = mock.doSomething(42);

 // then
 assertEquals(42, result.getIntValue());
 }

 @Test
 public void testHandleInvocationWithAnswerWithMatcherAndCaptureAllAndArgumentTransformer() {
 // given
 InvocationMatcher matcher = ArgumentMatchers.argThat(arg -> arg.equals(42));
 doAnswer(answer -> new DummyClass((Object[]) answer.getArguments())).when(mock).doSomething(matcher);

 // when
 DummyClass result = mock.doSomething(42);

 // then
 assertEquals(42, result.getIntValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testHandleInvocationWithAnswerWithInvalidArgumentTransformer() {
 // given
 doAnswer(answer -> new DummyClass((String) answer.getArguments()[0])).when(mock).doSomething(ArgumentMatchers.anyInt());

 // when
 // this test case should throw an IllegalStateException as the argument transformer is not compatible with int argument
 mock.doSomething(42);
 }

 @Test
 public void testHandleInvocationWithAnswerWithCallback() {
 // given
 AnswerCallback<Integer> answerCallback = new AnswerCallback<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 };
 doAnswer(answerCallback).when(mock).doSomething(ArgumentMatchers.anyInt());

 // when
 int result = mock.doSomething(42);

 // then
 assertEquals(1, result);
 }

 @Test
 public void testHandleInvocationWithAnswerWithCallable() {
 // given
 AnswerCallable<Integer> answerCallable = new AnswerCallable<Integer>() {
 @Override
 public Integer call(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 };
 doAnswer(answerCallable).when(mock).doSomething(ArgumentMatchers.anyInt());

 // when
 int result = mock.doSomething(42);

 // then
 assertEquals(1, result);
 }

 @Test
 public void testHandleInvocationWithAnswerWithStub() {
 // given
 AnswerStub<Integer> answerStub = Mockito.answer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 });
 doAnswer(answerStub).when(mock).doSomething(ArgumentMatchers.anyInt());

 // when
 int result = mock.doSomething(42);

 // then
 assertEquals(1, result);
 }
}