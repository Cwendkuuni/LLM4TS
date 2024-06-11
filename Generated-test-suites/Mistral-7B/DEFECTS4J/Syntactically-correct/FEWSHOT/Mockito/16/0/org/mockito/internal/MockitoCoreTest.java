package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MockitoCoreTest {

 private Reporter reporter;
 private MockUtil mockUtil;
 private ThreadSafeMockingProgress mockingProgress;
 private MockSettings mockSettings;
 private Object mockObject;
 private Invocation lastInvocation;

 @Before
 public void setUp() {
 reporter = new Reporter();
 mockUtil = new MockUtil();
 mockingProgress = new ThreadSafeMockingProgress();
 mockSettings = new MockSettingsImpl();
 mockObject = mock(Object.class, mockSettings);
 }

 @Test
 public void testMock() {
 Object mockedObject = MockitoCore.mock(Object.class, mockSettings);
 assertSame(mockedObject, mockObject);
 }

 @Test
 public void testMockWithSettings() {
 Object mockedObject = MockitoCore.mock(Object.class, mockSettings);
 assertSame(mockedObject, mockObject);
 }

 @Test
 public void testStub() {
 IOngoingStubbing stubbing = MockitoCore.stub();
 assertSame(stubbing, MockitoCore.stub());
 }

 @Test
 public void testWhen() {
 Object returnValue = "return value";
 IOngoingStubbing stubbing = MockitoCore.when(mockObject).thenReturn(returnValue);
 assertEquals(returnValue, MockitoCore.verify(mockObject).someMethodCall());
 }

 @Test
 public void testReset() {
 IOngoingStubbing stubbing = MockitoCore.stub();
 MockitoCore.reset(mockObject);
 assertNull(stubbing.thenReturn(null));
 }

 @Test
 public void testVerify() {
 verify(mockObject);
 }

 @Test
 public void testVerifyNull() {
 MockitoCore.verify(null);
 }

 @Test
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 MockitoCore.verify(notAMock);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object[] mocks = new Object[] { mockObject };
 MockitoCore.verifyNoMoreInteractions(mocks);
 }

 @Test
 public void testVerifyNoMoreInteractionsNull() {
 MockitoCore.verifyNoMoreInteractions();
 }

 @Test
 public void testVerifyNoMoreInteractionsNotAMock() {
 Object notAMock = new Object();
 MockitoCore.verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testInOrder() {
 Object[] mocks = new Object[] { mockObject, mockObject };
 InOrder inOrder = MockitoCore.inOrder(mocks);
 }

 @Test
 public void testAnswer() {
 Object[] args = new Object[] { 1 };
 Answer<Object> answer = args -> args[0].toString();
 Object returnValue = MockitoCore.doAnswer(answer).when(mockObject).someMethodCall(args[0]);
 assertEquals("1", returnValue);
 }

 @Test
 public void testVoidMethodStubbable() {
 Object mock = mock(Object.class);
 MockitoCore.stubVoid(mock).someVoidMethod();
 verify(mock).someVoidMethod();
 }

 @Test
 public void testGetLastInvocation() {
 Object[] args = new Object[] { "arg1" };
 when(mockObject.someMethod(any())).thenReturn("return value");
 lastInvocation = MockitoCore.getLastInvocation();
 assertEquals("return value", lastInvocation.getArgument(0));
 }
}