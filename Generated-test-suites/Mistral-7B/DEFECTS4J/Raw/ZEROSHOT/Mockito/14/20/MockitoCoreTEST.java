package org.mockito.internal.MockitoCoreTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import org.mockito.internal.MockitoCore;
import org.mockito.internal.MockSettings;
import org.mockito.internal.MockUtil;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.AnswerImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.internal.verification.api.VerificationModeFactory.VerificationModeBuilder;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyOrder;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockSettings mockSettings;
 private MockUtil mockUtil;
 private Object mock;
 private Object stub;
 private Object[] mocks;
 private List<Invocation> invocations;
 private VerificationModeBuilder verificationModeBuilder;
 private InOrder inOrder;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mockSettings = new MockSettingsImpl();
 mockUtil = new MockUtil();
 mock = mock(Object.class, mockSettings);
 stub = mockitoCore.stub();
 mocks = new Object[]{mock};
 invocations = Arrays.asList(new Invocation(null, null, null, null, null));
 verificationModeBuilder = VerificationModeFactory.build();
 }

 @Test
 public void testMockCreation() {
 T mockT = (T) mockitoCore.mock(Object.class, mockSettings);
 assertNotNull(mockT);
 }

 @Test
 public void testStubbing() {
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.stub();
 assertNotNull(ongoingStubbing);
 }

 @Test
 public void testStubbingWithDeprecatedMethod() {
 DeprecatedOngoingStubbing<Object> deprecatedOngoingStubbing = mockitoCore.stub(mock);
 assertNotNull(deprecatedOngoingStubbing);
 }

 @Test
 public void testStubbingWithOngoingStubbing() {
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
 assertNotNull(ongoingStubbing);
 }

 @Test
 public void testVerification() {
 verify(mock, verificationModeBuilder.atLeastOnce());
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNullMock() {
 verify(null, verificationModeBuilder.atLeastOnce());
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 verify(notAMock, verificationModeBuilder.atLeastOnce());
 }

 @Test
 public void testReset() {
 reset(mocks);
 }

 @Test
 public void testNoMoreInteractions() {
 verifyNoMoreInteractions(mocks);
 }

 @Test(expected = AssertionError.class)
 public void testNoMoreInteractionsWithNullMock() {
 verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testNoMoreInteractionsWithNotAMock() {
 Object notAMock = new Object();
 verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testNoMoreInteractionsInOrder() {
 InOrder inOrder = mockitoCore.inOrder(mocks);
 verifyOrder(inOrder);
 }

 @Test(expected = AssertionError.class)
 public void testNoMoreInteractionsInOrderWithNullMock() {
 InOrder inOrder = mockitoCore.inOrder(null);
 }

 @Test(expected = AssertionError.class)
 public void testNoMoreInteractionsInOrderWithNotAMock() {
 Object notAMock = new Object();
 InOrder inOrder = mockitoCore.inOrder(notAMock);
 }

 @Test
 public void testStubbingAnswer() {
 Answer<Integer> answer = answer(1);
 Stubber stubber = mockitoCore.doAnswer(answer);
 verify(mock).someMethod();
 stubber.thenReturn(2);
 verify(mock).someMethod().thenReturn(3);
 int result = stubber.answer();
 assertEquals(3, result);
 }

 @Test
 public void testStubbingVoidMethod() {
 mockitoCore.stubVoid(mock).someVoidMethod();
 verify(mock).someVoidMethod();
 }

 @Test
 public void testGetLastInvocation() {
 Invocation invocation = mockitoCore.getLastInvocation();
 assertNotNull(invocation);
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }
}