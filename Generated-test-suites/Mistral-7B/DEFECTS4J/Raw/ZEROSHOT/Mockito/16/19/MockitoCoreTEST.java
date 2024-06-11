package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Answer answer;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mock = mock(Object.class);
 answer = arg -> arg.toString();
 }

 @Test
 public void testMockCreation() {
 Object mockedObject = mockitoCore.mock(Object.class);
 verify(mockedObject).toString();
 }

 @Test
 public void testStubbing() {
 mockitoCore.stub().thenReturn("mocked value");
 verify(mock).toString();
 verify(mock).toString().equals("mocked value");
 }

 @Test
 public void testDeprecatedStubbing() {
 mockitoCore.stub(mock).thenReturn("mocked value");
 verify(mock).toString().equals("mocked value");
 }

 @Test
 public void testOngoingStubbing() {
 mockitoCore.when(mock).thenReturn("mocked value");
 verify(mock).toString().equals("mocked value");
 }

 @Test
 public void testVerify() {
 mockitoCore.verify(mock);
 }

 @Test
 public void testVerifyWithMode() {
 mockitoCore.verify(mock, Mockito.times(1));
 }

 @Test
 public void testVerifyNoInteractions() {
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithMultipleMocks() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testInOrder() {
 Object mockOrder = mock(Object.class);
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mockOrder, mock1, mock2);
 mockitoCore.when(mock1.toString()).thenReturn("mock1 value");
 mockitoCore.when(mock2.toString()).thenReturn("mock2 value");
 inOrder.verify(mockOrder).doSomething();
 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).toString();
 inOrder.verify(mockOrder).doAnotherThing();
 }

 @Test
 public void testAnswer() {
 mockitoCore.doAnswer(answer).when(mock).toString();
 verify(mock).toString().equals("mocked value");
 }

 @Test
 public void testVoidStubbing() {
 Object mockVoid = mock(Object.class);
 mockitoCore.stubVoid(mockVoid).thenCallRealMethod();
 mockitoCore.verify(mockVoid).doSomething();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastInvocation() {
 mockitoCore.getLastInvocation();
 }

 @Test
 public void testVerifyUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testStubbingWithSameMockTwice() {
 mockitoCore.when(mock).thenReturn("mock value");
 mockitoCore.when(mock).thenReturn("mock value");
 }
}