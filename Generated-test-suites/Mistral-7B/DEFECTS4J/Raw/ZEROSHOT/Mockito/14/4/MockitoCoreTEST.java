package org.mockito.internal.MockitoCoreTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.mockito.internal.verification.api.Stubbing.doAnswer;
import static org.mockito.internal.verification.api.Stubbing.verifyNoMoreInteractions;
import static org.mockito.internal.verification.api.Stubbing.verifyOrder;

import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.reset;

import static org.mockito.internal.util.MockUtil.isMock;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private InOrder inOrder;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock = mock(Object.class);
 inOrder = mockitoCore.inOrder(mock);
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, null);
 verify(mockitoCore).mockingStarted(mock, Object.class, null);
 }

 @Test
 public void testStubbing() {
 when(mock).thenReturn("test");
 String result = (String) mock.someMethod();
 verify(mock).someMethod();
 verify(mock).someMethod(); // should not throw an error
 assertEquals("test", result);
 }

 @Test
 public void testOngoingStubbing() {
 OngoingStubbing ongoingStubbing = mockitoCore.stub();
 when(mock).then(ongoingStubbing.answer(ArgumentMatchers.anyInt()));
 int result = mock.someMethod(1);
 int arg = 1;
 verify(mock).someMethod(arg);
 assertEquals(arg, result);
 }

 @Test
 public void testNoAnswer() {
 OngoingStubbing ongoingStubbing = mockitoCore.stub();
 when(mock).then(ongoingStubbing.doNothing());
 mock.someMethod();
 verify(mock).someMethod();
 }

 @Test
 public void testAnswer() {
 OngoingStubbing ongoingStubbing = mockitoCore.stub();
 Answer<String> answer = new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "test";
 }
 };
 when(mock).thenAnswer(answer);
 String result = mock.someMethod();
 verify(mock).someMethod();
 assertEquals("test", result);
 }

 @Test
 public void testReset() {
 OngoingStubbing ongoingStubbing = mockitoCore.stub();
 when(mock).then(ongoingStubbing.answer(ArgumentMatchers.anyInt()));
 int result1 = mock.someMethod(1);
 int arg1 = 1;
 verify(mock).someMethod(arg1);
 assertEquals(arg1, result1);

 reset(mock);
 int result2 = mock.someMethod(1);
 int arg2 = 1;
 verify(mock, Mockito.never()).someMethod(arg1);
 verify(mock).someMethod(arg2);
 assertEquals(arg2, result2);
 }

 @Test
 public void testVerify() {
 verify(mock);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNull() {
 verify(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 verify(notAMock);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNull() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testVerifyOrder() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 when(mock1).thenReturn(1);
 when(mock2).thenReturn(2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 verifyOrder(inOrder);
 }

 @Test
 public void testVerifyOrderThrowsException() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 when(mock1).thenReturn(1);
 when(mock2).thenReturn(2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 inOrder.verify(mock1).someMethod(); // should throw an exception
 verifyOrder(inOrder);
 }

 @Test
 public void testGetLastInvocation() {
 OngoingStubbing ongoingStubbing = mockitoCore.stub();
 when(mock.someMethod()).then(ongoingStubbing.answer(ArgumentMatchers.anyInt()));
 mock.someMethod();
 mock.someMethod();
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertEquals("someMethod(int)", lastInvocation.getMethod().getName());
 }
}