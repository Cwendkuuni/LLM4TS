package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockitoCore;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractionsInOrder;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.doAnswer;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Object otherMock;
 private InOrder order;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mock = mock(Object.class);
 otherMock = mock(Object.class);
 order = inOrder(mock, otherMock);
 }

 @Test
 public void testMockCreation() {
 Object mockedObject = mockitoCore.mock(Object.class, null);

 assert(mockedObject instanceof Object);
 }

 @Test
 public void testStubbing() {
 IOngoingStubbing stubbing = mockitoCore.stub();

 stubbing.thenReturn("test");
 String returnedValue = (String) mockitoCore.when(mock).thenReturn("test").getMock();

 assertEquals("test", returnedValue);
 }

 @Test
 public void testStubbingVoidMethod() {
 VoidMethodStubbable voidStubbable = mockitoCore.stubVoid(mock);

 voidStubbable.stub();

 mockitoCore.verify(mock).voidMethod();
 }

 @Test
 public void testAnswer() {
 Answer answer = ArgumentMatchers.anyInt().thenReturn(42);

 assertEquals(42, mockitoCore.when(mock).thenAnswer(answer).getMock().someMethod(1));
 }

 @Test
 public void testVerification() {
 mockitoCore.verify(mock);
 }

 @Test
 public void testVerificationMode() {
 mockitoCore.verify(mock, Mockito.atLeastOnce());
 }

 @Test
 public void testVerificationWithInOrder() {
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock, otherMock), order);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithNullMock() {
 mockitoCore.verify(null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithNotAMock() {
 mockitoCore.verify(new Object());
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock, otherMock);

 mockitoCore.verifyNoMoreInteractions(mock, otherMock);
 }

 @Test
 public void testGetLastInvocation() {
 Invocation invocation = mockitoCore.getLastInvocation();

 assertNotNull(invocation);
 }
}