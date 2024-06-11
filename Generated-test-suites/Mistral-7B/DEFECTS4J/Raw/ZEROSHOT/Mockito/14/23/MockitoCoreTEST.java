package org.mockito.internal.MockitoCoreTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerCalledWith;
import org.mockito.stubbing.AnswerReturns;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock1;
 private Object mock2;
 private InOrder inOrder;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mock1 = mock(Object.class);
 mock2 = mock(Object.class);
 inOrder = mockitoCore.inOrder(mock1, mock2);
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, MockSettings.none());
 verify(mockitoCore).mock(ArgumentMatchers.eq(Object.class), ArgumentMatchers.eq(MockSettings.none()));
 }

 @Test
 public void testStubbingAndVerification() {
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.stub();
 when(mock1).thenReturn("result");
 verify(mock1).toString();
 ongoingStubbing.thenReturn("stubbed result");
 verify(mock1).toString();
 assertEquals("result", mock1.toString());
 assertEquals("stubbed result", mock1.toString());
 }

 @Test
 public void testVerifyMethod() {
 verify(mock1, VerificationMode.times(1));
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNullMock() {
 verify(null, VerificationMode.times(1));
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 verify(new Object(), VerificationMode.times(1));
 }

 @Test
 public void testReset() {
 OngoingStubbing<Object> stubbing = mockitoCore.stub();
 when(mock1).thenReturn("result");
 stubbing.thenReturn("stubbed result");
 mockitoCore.reset(mock1);
 verify(mock1, never()).toString();
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 verifyNoInteractions(mock1, mock2);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNullMocks() {
 verifyNoMoreInteractions();
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 when(mock1.method1()).thenReturn("result1");
 when(mock1.method1()).thenReturn("result2");
 when(mock2.method2()).thenReturn("result2");
 inOrder.verify(mock1).method1();
 inOrder.verify(mock1).method1();
 inOrder.verify(mock2).method2();
 inOrder.verifyNoMoreInteractions();
 }

 @Test
 public void testAnswer() {
 Object result = new Object();
 Answer answer = new AnswerCalledWith("arg") {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 result = invocation.getArgument(0);
 return null;
 }
 };
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.stub();
 ongoingStubbing.thenAnswer(answer);
 verify(mock1).method1("arg");
 assertEquals("arg", result);
 }

 @Test
 public void testAnswerReturns() {
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.stub();
 when(mock1.method1()).thenReturn("result");
 ongoingStubbing.thenReturn(new String("stubbed result"));
 assertEquals("result", mock1.method1());
 assertEquals("stubbed result", mock1.method1());
 }

 @Test
 public void testVoidMethodStubbable() {
 VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(mock1);
 voidMethodStubbable.doCall();
 verify(mock1).method1();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.stub();when(mock1.method1()).thenCallRealMethod();
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertEquals(lastInvocation.getMethod(), mock1.method1().getClass().getDeclaredMethod("method1", new Class[0]));
 }
}