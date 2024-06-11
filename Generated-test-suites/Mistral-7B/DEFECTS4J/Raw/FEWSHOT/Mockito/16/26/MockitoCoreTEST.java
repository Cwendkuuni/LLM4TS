package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mockObject;
 private Object spyObject;
 private List<Invocation> invocations;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mockObject = mock(Object.class);
 spyObject = spy(Object.class);
 invocations = Arrays.asList((Invocation) mockitoCore.getLastInvocation());
 }

 @Test
 public void testMock() {
 Object mockedObject = mockitoCore.mock(Object.class);
 assertNull(invocations);
 assertSame(mockedObject, mockitoCore.verify(mockedObject));
 }

 @Test
 public void testMockWithSettings() {
 MockSettings mockSettings = new MockSettings();
 Object mockedObject = mockitoCore.mock(Object.class, mockSettings);
 assertNull(invocations);
 assertSame(mockedObject, mockitoCore.verify(mockedObject));
 }

 @Test
 public void testStub() {
 Object mockedObject = mockitoCore.mock(Object.class);
 when(mockedObject.someMethod()).thenReturn("someValue");
 assertEquals("someValue", mockedObject.someMethod());
 }

 @Test
 public void testVerify() {
 verify(mockObject).someMethod();
 assertNull(invocations);
 }

 @Test
 public void testVerifyWithMode() {
 verify(mockObject, Mockito.times(2)).someMethod();
 verify(mockObject, Mockito.atLeastOnce()).someMethod();
 verify(mockObject, Mockito.never()).someOtherMethod();
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mockObject);
 verify(mockObject, Mockito.never()).someMethod();
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mockedObject2 = mockitoCore.mock(Object.class);
 mockitoCore.verify(mockObject).someMethod();
 mockitoCore.verify(mockObject2).someMethod();
 mockitoCore.verifyNoMoreInteractions(mockObject);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNull() {
 mockitoCore.verify(null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNotAMock() {
 mockitoCore.verify(spyObject);
 }

 @Test
 public void testInOrder() {
 Object mock1 = mockitoCore.mock(Object.class);
 Object mock2 = mockitoCore.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someOtherMethod();
 }

 @Test
 public void testDoAnswer() {
 Object mock = mockitoCore.mock(Object.class);
 when(mock.someMethod()).thenAnswer(i -> "answer");
 assertEquals("answer", mock.someMethod());
 }

 @Test
 public void testVoidMethodStubbable() {
 Object mock = mockitoCore.mock(Object.class);
 mockitoCore.stubVoid(mock).someMethod();
 mock.someMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }
}