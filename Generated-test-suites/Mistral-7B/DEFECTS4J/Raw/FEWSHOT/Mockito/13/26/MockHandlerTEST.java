package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.verification.VerificationModeFactory.verifyNoMoreInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.verifyZeroInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.verifyZeroInteractionsInOrder;

import java.util.List;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.stubbing.Answer2;
import org.mockito.stubbing.Answer3;
import org.mockito.stubbing.Answer4;
import org.mockito.stubbing.Answer5;
import org.mockito.stubbing.Answer6;
import org.mockito.stubbing.Answer7;
import org.mockito.stubbing.Answer8;
import org.mockito.stubbing.Answer9;
import org.mockito.stubbing.Answer10;

public class MockHandlerTest {

 private MockHandler<MockitoTestClass> mockHandler;
 private MockitoTestClass mockitoTestClass;

 @Before
 public void setup() {
 mockitoTestClass = new MockitoTestClass();
 mockHandler = new MockHandler<MockitoTestClass>(new MockSettingsImpl());
 }

 @Test
 public void testHandleVoidMethod() throws Throwable {
 Invocation invocation = new Invocation(null, null, null, null, null, null, null);
 mockHandler.handle(invocation);
 assertNull(invocation.getAnswer());
 }

 @Test
 public void testHandleStubbingVoidMethod() throws Throwable {
 VoidMethodStubbable<MockitoTestClass> stub = mockHandler.voidMethodStubbable(mockitoTestClass);
 stub.thenReturn(1);
 Invocation invocation = new Invocation(null, null, null, null, null, null, null);
 mockHandler.handle(invocation);
 assertEquals(1, invocation.getAnswer());
 }

 @Test
 public void testHandleStubbingInvocationWithAnswer() throws Throwable {
 Answer answer = new Answer2<Integer, Integer>() {
 @Override
 public Integer answer(Invocation<Integer, Integer> invocation) throws Throwable {
 return invocation.getArgument(0) * 2;
 }
 };
 mockHandler.setAnswersForStubbing(new Answer[]{answer});
 Invocation invocation = new Invocation(null, null, null, 3, null, null, null);
 mockHandler.handle(invocation);
 assertEquals(6, invocation.getAnswer());
 }

 @Test
 public void testHandleStubbingInvocationWithAnswer1() throws Throwable {
 Answer answer = new Answer<Integer>() {
 @Override
 public Integer answer(Invocation<Integer, Integer> invocation) throws Throwable {
 return 1;
 }
 };
 Invocation invocation = new Invocation(null, null, null, 1, null, null, null);
 mockHandler.handle(invocation);
 mockHandler.setAnswersForStubbing(new Answer[]{answer});
 mockHandler.handle(invocation);
 assertEquals(1, invocation.getAnswer());
 }

 @Test
 public void testHandleStubbingInvocationWithAnswer2() throws Throwable {
 Invocation invocation = new Invocation(null, null, null, 1, null, null, null);
 mockHandler.handle(invocation);
 mockHandler.setAnswersForStubbing(new Answer[]{new Answer<Integer>() {
 @Override
 public Integer answer(Invocation<Integer, Integer> invocation) throws Throwable {
 return 2;
 }
 }, new Answer<Integer>() {
 @Override
 public Integer answer(Invocation<Integer, Integer> invocation) throws Throwable {
 return 3;
 }
 }});
 mockHandler.handle(invocation);
 assertEquals(3, invocation.getAnswer());
 }

 @Test
 public void testHandleStubbingWithMultipleAnswers() throws Throwable {
 Invocation invocation = new Invocation(null, null, null, null, null, 1, null);
 mockHandler.handle(invocation);
 mockHandler.setAnswersForStubbing(new Answer[]{new Answer<Integer>() {
 @Override
 public Integer answer(Invocation<Integer, Integer> invocation) throws Throwable {
 if (invocation.getArgument(0) < 3) {
 return invocation.getArgument(0) * 2;
 } else {
 return 42;
 }
 }
 }, new Answer<Integer>() {
 @Override
 public Integer answer(Invocation<Integer, Integer> invocation) throws Throwable {
 return 7;
 }
 }});
 mockHandler.handle(invocation);
 mockHandler.handle(invocation);
 mockHandler.handle(invocation);
 assertEquals(6, invocation.getAnswer());
 assertEquals(7, invocation.getAnswer());
 assertEquals(42, invocation.getAnswer());
 }

 @Test
 public void testHandleStubbingWithAnswerThatThrowsException() throws Throwable {
 Answer answer = new Answer<Integer>() {
 @Override
 public Integer answer(Invocation<Integer, Integer> invocation) throws Throwable {
 throw new RuntimeException("Test exception.");
 }
 };
mockHandler.setAnswersForStubbing(new Answer[]{answer});
 Invocation invocation = new Invocation(null, null, null, 1, null, null, null);
 try {
 mockHandler.handle(invocation);
 } catch (Throwable e) {
 // expected
 }
 assertNull(invocation.getAnswer());
 }

 @Test
 public void testHandleVerification() throws Throwable {
 Invocation invocation = new Invocation(mockitoTestClass, "someMethod", new Object[]{1}, null, null, null, null);
 mockingProgress.setVerificationMode(new MockAwareVerificationMode(invocation));
 mockHandler.handle(invocation);
 verifyZeroInteractions(mockitoTestClass);
 }

 @Test
 public void testHandleVerificationWithVerifyZeroInteractionsInOrder() throws Throwable {
 Invocation invocation1 = new Invocation(mockitoTestClass, "someMethod", new Object[]{1}, null, null, null, null);
 Invocation invocation2 = new Invocation(mockitoTestClass, "someMethod", new Object[]{2}, null, null, null, null);
 mockingProgress.setVerificationMode(new MockAwareVerificationMode(invocation1));
 mockHandler.handle(invocation1);
 mockingProgress.setVerificationMode(new MockAwareVerificationMode(invocation2));
 mockHandler.handle(invocation2);
 verifyZeroInteractionsInOrder(mockitoTestClass);
 }

 @Test
 public void testHandleVerificationWithVerifyNoMoreInteractions() throws Throwable {
 Invocation invocation1 = new Invocation(mockitoTestClass, "someMethod", new Object[]{1}, null, null, null, null);
 Invocation invocation2 = new Invocation(mockitoTestClass, "someMethod", new Object[]{2}, null, null, null, null);
 mockingProgress.setVerificationMode(new MockAwareVerificationMode(invocation1));
 mockHandler.handle(invocation1);
 mockingProgress.setVerificationMode(new MockAwareVerificationMode(invocation2));
 mockHandler.handle(invocation2);
 verifyNoMoreInteractions(mockitoTestClass);
 }

 @Test
 public void testHandleVerificationWithVerify() throws Throwable {
 Invocation invocation = new Invocation(mockitoTestClass, "someMethod", new Object[]{1}, null, null, null, null);
 mockingProgress.setVerificationMode(new MockAwareVerificationMode(invocation));
 mockHandler.handle(invocation);
 verify(mockitoTestClass).someMethod(eq(1));
 }

 @Test
 public void testHandleVerificationWithVerifyOrder() throws Throwable {
 Invocation invocation1 = new Invocation(mockitoTestClass, "someMethod", new Object[]{1}, null, null, null, null);
 Invocation invocation2 = new Invocation(mockitoTestClass, "someMethod", new Object[]{2}, null, null, null, null);
 mockingProgress.setVerificationMode(new MockAwareVerificationMode(invocation1));
 mockHandler.handle(invocation1);
 mockingProgress.setVerificationMode(new MockAwareVerificationMode(invocation2));
 mockHandler.handle(invocation2);
 verify(mockitoTestClass).someMethod(eq(1));
 verify(mockitoTestClass).someMethod(eq(2));
 }
}