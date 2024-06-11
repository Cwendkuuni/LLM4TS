package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.stubbing.Answer.returnValue;
import static org.mockito.internal.stubbing.Answer.throwsException;
import static org.mockito.internal.stubbing.Answer.stub;
import static org.mockito.internal.verification.VerificationMode.times;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import org.mockito.internal.MatchersBinder;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockingProgress;
import org.mockito.internal.ProgressPersister;
import org.mockito.internal.ProgressPersisterImpl;
import org.mockito.internal.StubbingContext;
import org.mockito.internal.ThreadSafeMockingProgress;
import org.mockito.internal.VerificationDataImpl;
import org.mockito.internal.VerificationMode;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.VerificationData;
import org.mockito.invocation.Invocation;

public class MockHandlerTest {

 private static final Object DEFAULT_ANSWER = "default answer";

 @Test
 public void testHandle_noAnswersForStubbing() throws Throwable {
 MockHandler<MockitoTestClass> mockHandler = new MockHandler<>();
 Invocation invocation = newInvocation(MockitoTestClass.class, "methodWithoutStubs", 1);

 Object result = mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testHandle_answersForStubbing() throws Throwable {
 MockHandler<MockitoTestClass> mockHandler = new MockHandler<>();
 setupAnswersForStubbing(mockHandler, newAnswer(1, "answer"));
 Invocation invocation = newInvocation(MockitoTestClass.class, "methodWithStubs", 1);

 Object result = mockHandler.handle(invocation);
 assertSame("answer", result);
 }

 @Test
 public void testHandle_verificationMode() throws Throwable {
 MockHandler<MockitoTestClass> mockHandler = new MockHandler<>();
 Invocation invocation = newInvocation(MockitoTestClass.class, "methodToVerify", 1, "arg1");

 mockHandler.handle(invocation);
 verify(mockHandler.getMockSettings().getMock()).methodToVerify(eq("arg1"), isA(Integer.class));
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_verificationMode_multipleMocks() throws Throwable {
 MockHandler<MockitoTestClass1> mockHandler1 = new MockHandler<>();
 MockHandler<MockitoTestClass2> mockHandler2 = new MockHandler<>();
 Invocation invocation = newInvocation(MockitoTestClass1.class, "methodToVerify", 1, "arg1");

 mockHandler1.handle(invocation);
 mockHandler2.handle(invocation);
 }

 @Test
 public void testHandle_verificationData() throws Throwable {
 MockHandler<MockitoTestClass> mockHandler = new MockHandler<>();
 VerificationData verificationData = new VerificationDataImpl(Arrays.asList(newInvocation(MockitoTestClass.class, "methodToVerify", 1)),
 new StubbedInvocationMatcher(Arrays.asList(eq("arg1")), Arrays.asList(isA(Integer.class))));

 mockHandler.handle(newInvocation(MockitoTestClass.class, "methodToVerify", 1));
 times(1).verify(mockHandler.getMockSettings().getMock()).methodToVerify(eq("arg1"), isA(Integer.class));
 }

 @Test
 public void testHandle_voidMethodStubbable() throws Throwable {
 MockHandler<MockitoTestClass> mockHandler = new MockHandler<>();
 VoidMethodStubbable<MockitoTestClass> voidMethodStubbable = mockHandler.voidMethodStubbable(mock(MockitoTestClass.class));
 voidMethodStubbable.doAnswer(stub(Void.TYPE, () -> {
 // Do something
 })).when(mockHandler.getMockSettings().getMock()).voidMethod();

 Invocation invocation = newInvocation(MockitoTestClass.class, "voidMethod");
 mockHandler.handle(invocation);
 }

 @Test
 public void testHandle_answersForStubbing_multipleMocks() throws Throwable {
 MockHandler<MockitoTestClass1> mockHandler1 = new MockHandler<>();
 MockHandler<MockitoTestClass2> mockHandler2 = new MockHandler<>();
 setupAnswersForStubbing(mockHandler1, newAnswer(1, "answer1"));
 setupAnswersForStubbing(mockHandler2, newAnswer(2, "answer2"));

 Invocation invocation = newInvocation(MockitoTestClass1.class, "methodWithStubs", 1);
 Object result1 = mockHandler1.handle(invocation);
 assertSame("answer1", result1);

 Invocation invocation2 = newInvocation(MockitoTestClass2.class, "methodWithStubs", 2);
 Object result2 = mockHandler2.handle(invocation2);
 assertSame("answer2", result2);
 }

 private void setupAnswersForStubbing(MockHandler<MockitoTestClass> mockHandler, Answer answer) {
 List<Answer> answers = Arrays.asList(answer);
 mockHandler.setAnswersForStubbing(answers);
 }

 private Invocation newInvocation(Class<?> clazz, String methodName, Object... args) {
 return new InvocationOnMock(clazz, methodName, args);
 }

 private Answer newAnswer(int index, Object answer) {
 return new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return answer;
 }

 @Override
 public int getInvocationOrder() {
 return index;
 }
 };
 }

 private VerificationMode times(int n) {
 return times(n);
 }
}