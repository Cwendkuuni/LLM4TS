package org.mockito.internal.MockitoCoreTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractionsInOrder;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Object mock2;
 private Object stub;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 }

 @Test
 public void testMockCreation() {
 Object result = mockitoCore.mock(Object.class, new MockSettingsImpl());
 assertSame(mock, result);
 }

 @Test(expected = MockitoException.class)
 public void testMockCreationWithNullClass() {
 mockitoCore.mock(null, new MockSettingsImpl());
 }

 @Test(expected = MockitoException.class)
 public void testMockCreationWithNullMockSettings() {
 mockitoCore.mock(Object.class, null);
 }

 @Test
 public void testStubbing() {
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.stubbingStarted();
 when(mock).thenReturn("foo");
 assertEquals("foo", mockitoCore.verify(mock).foo());
 }

 @Test
 public void testDeprecatedStubbing() {
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.stubbingStarted();
 mockitoCore.stub(mock).foo().thenReturn("foo");
 assertEquals("foo", mockitoCore.verify(mock).foo());
 }

 @Test
 public void testOngoingStubbing() {
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.stubbingStarted();
 when(mock).thenReturn("foo");
 assertEquals("foo", ongoingStubbing.thenReturn("bar").get());
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock, mock2);
 verify(mock, never()).foo();
 verify(mock2, never()).foo();
 }

 @Test
 public void testVerification() {
 verify(mock).foo();
 }

 @Test(expected = MockitoException.class)
 public void testVerificationWithNullMock() {
 mockitoCore.verify(null, ArgumentMatchers.any(VerificationMode.class));
 }

 @Test(expected = MockitoException.class)
 public void testVerificationWithNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verify(notAMock, ArgumentMatchers.any(VerificationMode.class));
 }

 @Test
 public void testVerificationNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = MockitoException.class)
 public void testVerificationNoMoreInteractionsWithNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerificationNoMoreInteractionsWithMock() {
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerificationNoMoreInteractionsInOrder() {
 InOrder inOrder = inOrder(mock, mock2);
 when(mock.foo()).thenReturn("foo1");
 when(mock2.foo()).thenReturn("foo2");
 inOrder.verify(mock).foo();
 inOrder.verify(mock).foo();
 inOrder.verify(mock2).foo();
 inOrder.verify(mock2).foo();
 verifyNoMoreInteractionsInOrder(Arrays.asList(mock, mock2), inOrder);
 }

 @Test
 public void testVerificationModeFactory() {
 verify(mock, times(3)).foo();
 }

 @Test
 public void testAnswer() {
 Answer<String> answer = new Answer<String>() {
 @Override public String answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 };
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.stubbingStarted();
 when(mock).thenAnswer(answer);
 assertEquals("answer", mockitoCore.verify(mock).foo());
 }

 @Test
 public void testStubVoid() {
 mockitoCore.stubbingStarted();
 mockitoCore.stubVoid(mock).foo();
 // Cannot test this method as it is void
 }

 @Test
 public void testGetLastInvocation() {
 Invocation invocation = mockitoCore.getLastInvocation();
 assertNull(invocation);
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = MockitoException.class)
 public void testValidateMockitoUsageWithNullMock() {
 mockitoCore.validateMockitoUsage(null);
 }
}