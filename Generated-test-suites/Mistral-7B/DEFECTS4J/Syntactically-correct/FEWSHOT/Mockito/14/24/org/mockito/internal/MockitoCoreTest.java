package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.MockHandler;
import org.mockito.internal.invocation.MockHandlerInterface;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbing;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationDataImpl;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;
import org.mockito.verification.api.InOrderContext;
import org.mockito.verification.api.VerificationDataInOrder;
import org.mockito.verification.api.VerificationDataInOrderImpl;

public class MockitoCoreTest {

 private Reporter reporter;
 private MockUtil mockUtil;
 private MockingProgress mockingProgress;
 private StubberImpl stubber;
 private IOngoingStubbing ongoingStubbing;
 private OngoingStubbing<Object> ongoingStubbingObject;
 private OngoingStubbing<VoidMethodStubbable<Object>> ongoingVoidStubbing;
 private Object mock;
 private Object mockWithSettings;
 private Object mockWithStubber;
 private Object mockWithInvocations;
 private Object mockWithAnswer;
 private Object mockWithInOrder;
 private Object mockWithVerificationMode;
 private Object mockWithNull;
 private Object mockWithNotAMock;
 private Object mockWithListMocks;
 private Object mockWithInOrderContext;
 private Object mockWithVerificationData;
 private Object mockWithVerificationDataInOrder;
 private Object mockWithInvocation;
 private Object mockWithMethodCall;
 private Object mockWithDeprecatedStubbing;
 private Object mockWithDeprecatedOngoingStubbing;
 private Object mockWithVoidMethodStubbable;

 @Before
 public void setup() {
 initMocks(this);
 reporter = new Reporter();
 mockUtil = new MockUtil();
 mockingProgress = new ThreadSafeMockingProgress();
 }

 @Test
 public void testMock() {
 mock = mock(Object.class);
 assertEquals(Object.class, mock.getClass());
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testMockWithSettings() {
 mockSettings = new MockSettingsImpl();
 mockWithSettings = mock(Object.class, mockSettings);
 assertEquals(Object.class, mockWithSettings.getClass());
 }

 @Test
 public void testStub() {
 stubber = new StubberImpl();
 ongoingStubbing = stubber.stub();
 assertEquals(OngoingStubbing.class, ongoingStubbing.getClass());
 }

 @Test
 public void testStubWithObject() {
 mockingProgress.reset();
 ongoingStubbingObject = when(mock).thenReturn(new Object());
 assertEquals(new Object(), mock.getClass().cast(mockingProgress.getLastInvocation().getArgument(0)));
 }

 @Test
 public void testStubWithVoidMethodStubbable() {
 mockingProgress.reset();
 ongoingVoidStubbing = when(mock).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 });
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerify() {
 verify(mock);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNull() {
 verify(null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNotAMock() {
 mockWithNotAMock = new Object();
 verify(mockWithNotAMock);
 reporter.notAMockPassedToVerify();
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNull() {
 verifyNoMoreInteractions(null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 mockWithNotAMock = new Object();
 verifyNoMoreInteractions(mockWithNotAMock);
 reporter.notAMockPassedToVerifyNoMoreInteractions();
 }

 @Test
 public void testReset() {
 mockingProgress.reset();
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 mockWithInvocations = spy(mock);
 List<Invocation> invocations = Arrays.asList(mockingProgress.getLastInvocation(), mockingProgress.getLastInvocation());
 InOrder inOrder = inOrder(mockWithInvocations);
 verifyNoMoreInteractionsInOrder(Arrays.asList(mockWithInvocations), inOrder);
 }

 @Test
 public void testInOrder() {
 mockWithInOrder = new Object[2];
 InOrder inOrder = inOrder(mockWithInOrder[0], mockWithInOrder[1]);
 assertSame(inOrder, inOrder.inOrder(mockWithInOrder[0]));
 assertSame(inOrder, inOrder.inOrder(mockWithInOrder[1]));
 }

 @Test
 public void testDoAnswer() {
 mockingProgress.reset();
 ongoingStubbing = when(mock).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgument(0);
 }
 });
 Object argument = new Object();
 mock.foo(argument);
 assertEquals(argument, mockingProgress.getLastInvocation().getArgument(0));
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testStubVoid() {
 mockingProgress.reset();
 ongoingVoidStubbing = stub(mock).thenCallRealMethod();
 mock.foo();
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testValidateMockitoUsage() {
 mockingProgress.validateState();
 }

 @Test
 public void testGetLastInvocation() {
 AtomicReference<Invocation> invocationRef = new AtomicReference<>();
 when(mock.foo(invocationRef)).thenCallRealMethod();
 mock.foo(new Object());
 Invocation invocation = mockingProgress.getLastInvocation();
 assertEquals(invocationRef.get(), invocation);
 }

 @Test
 public void testStubDeprecated() {
 mockingProgress.reset();
 ongoingStubbing = stub(mockWithMethodCall).thenCallRealMethod();
 mockWithMethodCall.foo();
 verifyNoMoreInteractions(mockWithMethodCall);
 }

 @Test
 public void testStubDeprecatedOngoing() {
 mockingProgress.reset();
 ongoingDeprecatedStubbing = stub(mockWithMethodCall).thenCallRealMethod();
 mockWithMethodCall.foo();
 IOngoingStubbing ongoingStubbing = ongoingStubbingObject();
 when(ongoingStubbing.thenCallRealMethod()).thenReturn(mockWithDeprecatedStubbing);
 mockWithDeprecatedStubbing.foo();
 verifyNoMoreInteractions(mockWithDeprecatedStubbing);
 }

 @Test
 public void testVoidMethodStubbable() {
 mockingProgress.reset();
 mockingProgress.stubbingStarted();
 mockWithVoidMethodStubbable = mock(VoidMethodStubbable.class);
 VoidMethodStubbable stubbable = mockingProgress.pullOngoingStubbing();
 assertEquals(VoidMethodStubbable.class, stubbable.getClass());
 mockingProgress.resetOngoingStubbing();
 }

}