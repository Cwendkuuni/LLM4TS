package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockingProgress mockingProgress;
 private Object mock;
 private Stubber stubber;
 private Answer answer;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mockingProgress = mockitoCore.mockingProgress;
 }

 @Test
 public void testMockCreation() {
 mock = mockitoCore.mock(String.class);
 assertNotNull(mock);
 }

 @Test
 public void testStubbingStarted() {
 stubber = mockitoCore.stub();
 answer = Mockito.answer(new Object());
 when(mock).thenAnswer(answer);
 verify(mockitoCore).stubbingStarted();
 }

 @Test
 public void testVerifyMock() {
 verify(mock).toString();
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNullMock() {
 verify(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 mockingProgress.reset();
 mockitoCore.verify(notAMock, Mockito.eq("expected"));
 }

 @Test
 public void testReset() {
 mock = mockitoCore.mock(String.class);
 when(mock).thenReturn("hello");
 String resetMock = mockitoCore.mockitoCore.reset(mock);
 verify(mockitoCore).reset();
 verify(mock).toString();
 verify(mock).toString(); // should return original value
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mock = mockitoCore.mock(String.class);
 mockitoCore.verifyNoMoreInteractions(mock);
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testInOrder() {
 Object mock1 = mockitoCore.mock(String.class);
 Object mock2 = mockitoCore.mock(Integer.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).intValue();
 }

 @Test
 public void testDoAnswer() {
 mock = mockitoCore.mock(String.class);
 answer = Mockito.answer(new Object());
 stubber = mockitoCore.mockitoCore.doAnswer(answer);
 stubber.doAnswer(answer).when(mock).toString();
 verify(mockitoCore).stubbingStarted();
 verify(mock).toString();
 }

 @Test
 public void testGetLastInvocation() {
 mock = mockitoCore.mock(String.class);
 Invocation lastInvocation = mockitoCore.mockitoCore.getLastInvocation();
 verify(mockitoCore).stubbingStarted();
 Mockito.when(mock.toString()).thenReturn("mockito");
 Mockito.when(mock.toString()).thenReturn("mockito2");
 verify(mockitoCore).stubbingStarted();
 verify(mock).toString();
 verify(mockitoCore).stubbingStarted();
 verify(mock).toString();
 assertEquals("mockito2", lastInvocation.getArguments()[0]);
 }

}