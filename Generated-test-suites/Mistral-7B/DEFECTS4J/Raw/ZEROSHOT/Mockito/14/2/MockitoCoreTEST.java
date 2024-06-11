package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.exceptions.misusing.NotAMockException;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Object spy;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock = Mockito.mock(Object.class);
 spy = Mockito.spy(Object.class);
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testMockCreation() {
 Object result = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mockitoCore).mockingStarted(ArgumentMatchers.eq(mock), ArgumentMatchers.eq(Object.class), ArgumentMatchers.isA(MockSettingsImpl.class));
 assertNotNull(result);
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithNullClass() {
 mockitoCore.mock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithNotAMock() {
 Object notAMock = new Object();
 mockitoCore.mock(notAMock, new MockSettingsImpl());
 }

 @Test
 public void testStubbing() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn(42);
 Object result = mockitoCore.verify(mock).someMethod();
 verify(mock).someMethod();
 verify(mock).someMethod();
 verify(mock).someMethod(); // this should use the stubbed return value
 assertEquals(42, result);
 }

 @Test
 public void testOngoingStubbingWithMethodCall() {
 Object methodCall = new Object();
 IOngoingStubbing stubbing = mockitoCore.stub(methodCall);
 stubbing.thenReturn(42);
 Object result = mockitoCore.verify(mock).someMethod(methodCall);
 verify(mock).someMethod(ArgumentMatchers.eq(methodCall));
 assertEquals(42, result);
 }

 @Test
 public void testOngoingStubbingWithVoidMethod() {
 Object mockObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
 MockitoCore stubbingCore = mockitoCore;
 MockHandlerInterface<Object> mockHandler = mockitoCore.mockUtil.getMockHandler(mockObject);
 VoidMethodStubbable<Object> stub = mockitoCore.stubVoid(mockObject);
 stub.thenCallRealMethod();
 stubbingCore.mockingProgress.stubbingStarted();
 mockHandler.voidMethod(mockObject);
 }

 @Test
 public void testVerification() {
 mockitoCore.verify(mock, VerificationMode.times(1));
 }

 @Test(expected = NullPointerException.class)
 public void testVerificationWithNullMock() {
 mockitoCore.verify(null, VerificationMode.times(1));
 }

 @Test(expected = NotAMockException.class)
 public void testVerificationWithNonMock() {
 Object nonMock = new Object();
 mockitoCore.verify(nonMock, VerificationMode.times(1));
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNoMoreInteractionsWithNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsWithNonMock() {
 Object nonMock = new Object();mockitoCore.verifyNoMoreInteractions(nonMock);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = inOrder(mock1, mock2);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test
 public void testAnswer() {
 Answer<Integer> answer = invocation -> 42;
 Stubber stubber = mockitoCore.doAnswer(answer);
 stubber.when(mock).someMethod();
 Object result = mockitoCore.verify(mock).someMethod();
 assertEquals(42, result);
 }
}