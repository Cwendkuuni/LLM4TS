package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock1;
 private Object mock2;
 private Object mock3;
 private Object spy;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock1 = mock(Object.class);
 mock2 = mock(Object.class);
 mock3 = mock(Object.class);
 spy = mockitoCore.mock(Object.class, new MockSettingsImpl());
 }

 @Test
 public void testMock() {
 Object mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 assertSame(String.class, mock.getClass());
 }

 @Test
 public void testStubbingStarted() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 assertSame(stubbing, mockitoCore.stub());
 }

 @Test(expected = IllegalStateException.class)
 public void testStubbingStartedTwice() {
 mockitoCore.stub();
 mockitoCore.stub();
 }

 @Test
 public void testWhen() {
 when(mock1).thenReturn("foo");
 assertEquals("foo", mockitoCore.verify(mock1));
 }

 @Test
 public void testWhenThrows() {
 when(mock1).thenThrow(new RuntimeException());
 try {
 mockitoCore.verify(mock1);
 } catch (Throwable e) {
 assertEquals(RuntimeException.class, e.getClass());
 }
 }

 @Test
 public void testAnswer() {
 Answer answer = new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 42;
 }
 };
 when(mock1).thenAnswer(answer);
 assertEquals(42, mockitoCore.verify(mock1).getIntValue());
 }

 @Test
 public void testVerify() {
 verify(mock1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPassedToVerify() {
 mockitoCore.verify(null);
 }

 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testNotAMockPassedToVerify() {
 verify(new Object());
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(mock1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testNotAMockPassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock1, mock2, mock3);
 verify(mock1, never()).anyMethod();
 verify(mock2, never()).anyMethod();
 verify(mock3, never()).anyMethod();
 }

 @Test
 public void testVerifyNoMoreInteractionsList() {
 Object[] mocks = { mock1, mock2, mock3 };
 mockitoCore.verifyNoMoreInteractions(mocks);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMocksHaveToBePassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test
 public void testInOrder() {
 Object[] mocks = { mock1, mock2 };
 InOrder inOrder = mockitoCore.inOrder(mocks);
 inOrder.verify(mock1).method1();
 inOrder.verify(mock2).method2();
 verifyNoMoreInteractions(mocks);
 }

 @Test
 public void testDoAnswer() {
 Answer<Integer> answer = new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 };
 when(mock1).thenAnswer(answer);
 assertEquals(1, mockitoCore.verify(mock1).getInteger());
 }

 @Test
 public void testStubVoid() {
 mockitoCore.stubVoid(mock1);
 verify(mock1).voidMethod();
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object[] mocks = { mock1, mock2 };
 InOrder inOrder = mockitoCore.inOrder(mocks);
 inOrder.verify(mock1).method1();
 inOrder.verify(mock2).method2();
 verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 when(mock1).thenReturn("foo");
 Invocation invocation = mockitoCore.stub().thenReturn("bar");
 assertEquals("bar", mockitoCore.getLastInvocation());
 }
}