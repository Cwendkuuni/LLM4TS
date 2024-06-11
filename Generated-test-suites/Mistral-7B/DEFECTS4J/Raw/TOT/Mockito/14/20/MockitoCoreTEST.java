package org.mockito.internal.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoCore;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.verification.api.InOrder;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyOrder;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Object stub;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock = mock(Object.class, new MockSettingsImpl());
 stub = mockitoCore.stub();
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 assert mock != null;
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithNullClass() {
 mockitoCore.mock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithNullSettings() {
 mockitoCore.mock(Object.class, null);
 }

 @Test
 public void testStubbing() {
 when(mock).thenReturn("Test");
 String result = (String) mockitoCore.verify(mock);
 assert result.equals("Test");
 }

 @Test
 public void testStubbingWithAnswer() {
 when(mock).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 42;
 }
 });
 int result = (int) mockitoCore.verify(mock).someMethod();
 assert result == 42;
 }

 @Test
 public void testStubbingWithArgumentMatcher() {
 when(mock).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Test" + invocation.getArgument(0);
 }
 }).given(ArgumentMatchers.anyInt());
 String result = (String) mockitoCore.verify(mock).someMethod(1);
 assert result.equals("Test1");
 }

 @Test
 public void testVerification() {
 mockitoCore.verify(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testVerificationWithNullMock() {
 mockitoCore.verify(null);
 }

 @Test
 public void testVerificationWithNullVerificationMode() {
 mockitoCore.verify(mock, null);
 }

 @Test
 public void testVerificationMode() {
 mockitoCore.verify(mock).someMethod();
 mockitoCore.verify(mock).someMethod();
 verify(mock).someMethod();
 verify(mock).someMethod();
 }

 @Test
 public void testVerificationOrder() {
 Object mock1 = mock(Object.class, new MockSettingsImpl());
 Object mock2 = mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.someMethod()).thenReturn(1);
 when(mock2.someMethod()).thenReturn(2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 }

 @Test
 public void testVerificationNoInteractions() {
 Object mock = mock(Object.class, new MockSettingsImpl());
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerificationNoInteractionsWithNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerificationNoInteractionsWithArray() {
 Object mock1 = mock(Object.class, new MockSettingsImpl());
 Object mock2 = mock(Object.class, new MockSettingsImpl());
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 }

 @Test(expected = NotAMockException.class)
 public void testVerificationNoInteractionsWithNonMock() {
 Object nonMock = new Object();
 mockitoCore.verifyNoMoreInteractions(nonMock);
 }

 @Test
 public void testVerificationInOrder() {
 Object mock1 = mock(Object.class, new MockSettingsImpl());
 Object mock2 = mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.someMethod()).thenReturn(1);
 when(mock2.someMethod()).thenReturn(2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 }

 @Test(expected = NotAMockException.class)
 public void testVerificationInOrderWithNullMock() {
 Object mock1 = mock(Object.class, new MockSettingsImpl());
 Object mock2 = mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, null);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 }

 @Test
 public void testVerificationInOrderWithEmptyList() {
 InOrder inOrder = mockitoCore.inOrder();
 inOrder.verify(mock).someMethod();
 }

 @Test
 public void testReset() {
 Object mock = mock(Object.class, new MockSettingsImpl());
 mockitoCore.reset(mock);
 verifyNoInteractions(mock);
 }

 @Test
 public void testResetWithArray() {
 Object mock1 = mock(Object.class, new MockSettingsImpl());
 Object mock2 = mock(Object.class, new MockSettingsImpl());
 mockitoCore.reset(mock1, mock2);
 verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testResetWithNullArray() {
 mockitoCore.reset();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 when(mock.someMethod()).thenReturn(1);
 when(mock.someMethod()).thenReturn(2);
 Invocation invocation = mockitoCore.getLastInvocation();
 assert invocation.getMethod().getName().equals("someMethod");
 assert invocation.getMock().equals(mock);
 }
}