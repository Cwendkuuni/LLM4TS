package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoCore;
import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock
 private Reporter reporter;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore() {
 @Override
 public Reporter getReporter() {
 return reporter;
 }
 };
 }

 @Test
 public void testMock() {
 Class<?> mockType = Mockito.class;
 Object mock = mockitoCore.mock(mockType, new MockSettingsImpl());
 verify(mockitoCore).mockingStarted(mock, mockType, new MockSettingsImpl());
 }

 @Test(expected = IllegalStateException.class)
 public void testMockNullMockSettings() {
 Object mock = mockitoCore.mock(Mockito.class, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testMockNullMockType() {
 Object mock = mockitoCore.mock(null, new MockSettingsImpl());
 }

 @Test
 public void testStub() {
 Object mock = mockitoCore.mock(Mockito.class, new MockSettingsImpl());
 IOngoingStubbing stubbing = mockitoCore.stub();
 verify(mockitoCore).mockingProgress().pullOngoingStubbing();
 }

 @Test(expected = IllegalStateException.class)
 public void testStubNull() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 verify(mockitoCore).mockingProgress().pullOngoingStubbing();
 mockitoCore.stub(null);
 }

 @Test
 public void testWhen() {
 Object mock = mockitoCore.mock(Mockito.class, new MockSettingsImpl());
 Object returnValue = new Object();
 OngoingStubbing<Object> stubbing = mockitoCore.when(mock);
 stubbing.thenReturn(returnValue);
 verify(mockitoCore).mockingProgress().pullOngoingStubbing();
 verify(mock).equals(returnValue);
 }

 @Test
 public void testWhenNull() {
 Object mock = mockitoCore.mock(Mockito.class, new MockSettingsImpl());
 OngoingStubbing<Object> stubbing = mockitoCore.when(mock);
 stubbing.thenReturn(null);
 verify(mockitoCore).mockingProgress().pullOngoingStubbing();
 }

 @Test
 public void testDoAnswer() {
 Object mock = mockitoCore.mock(Mockito.class, new MockSettingsImpl());
 Answer<Integer> answer = org.mockito.internal.stubbing.answers.Returns.returns(1);
 Stubber doAnswer = mockitoCore.doAnswer(answer);
 doAnswer.when(mock).someMethod();
 Integer result = mockitoCore.verify(mock).someMethod();
 verify(answer).answeredWith(result);
 }

 @Test
 public void testInOrder() {
 Object mock1 = mockitoCore.mock(Mockito.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Mockito.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 }

 @Test
 public void testVerify() {
 Object mock = mockitoCore.mock(Mockito.class, new MockSettingsImpl());
 verify(mockitoCore).verify(mock, Mockito.any());
 }

 @Test
 public void testVerifyNull() {
 verify(mockitoCore).verify(null);
 }

 @Test
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 verify(mockitoCore).verify(notAMock, Mockito.any());
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock1 = mockitoCore.mock(Mockito.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Mockito.class, new MockSettingsImpl());
 verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testVerifyNoMoreInteractionsNull() {
 verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerifyNoMoreInteractionsEmptyList() {
 Object[] mocks = {};
 verifyNoMoreInteractions(mocks);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock1 = mockitoCore.mock(Mockito.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Mockito.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test
 public void testVerifyNoMoreInteractionsOrderContextNull() {
 Object[] mocks = {};
 verifyNoMoreInteractionsInOrder(mocks, null);
 }

 @Test
 public void testVerifyNoMoreInteractionsOrderContextEmptyList() {
 Object[] mocks = {};
 InOrder order = mockitoCore.inOrder(mocks);
 verifyNoMoreInteractionsInOrder(Arrays.asList(mocks), order);
 }

 @Test
 public void testReset() {
 Object mock = mockitoCore.mock(Mockito.class, new MockSettingsImpl());
 mockitoCore.reset(mock);
 verify(mockitoCore).mockingProgress().reset();
 verify(mockitoCore).mockUtil().resetMock(mock);
 }

 @Test
 public void testResetNull() {
 mockitoCore.reset(null);
 }

 @Test
 public void testResetEmptyArray() {
 mockitoCore.reset();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = IllegalStateException.class)
 public void testStubVoidNullMock() {
 Object mock = mockitoCore.mock(Mockito.class, new MockSettingsImpl());
 mockitoCore.stubVoid(null);
 }
}