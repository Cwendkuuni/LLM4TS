package org.mockito.internal.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMockException;
import org.mockito.Mockito;
import org.mockito.MockitoCore;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.OngoingStubbing;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrder;
import org.mockito.internal.verification.api.VerificationDataImpl;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationModeFactory.VerificationModeType;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockingProgress mockingProgress;
 private Stubber stubber;
 private InOrder inOrder;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mockingProgress = mockitoCore.new ThreadSafeMockingProgress();
 stubber = mockitoCore.stub();
 inOrder = mockitoCore.inOrder();
 }

 @Test
 public void testMockCreation() {
 MockSettings mockSettings = new MockSettingsImpl();
 Object mock = mockitoCore.mock(String.class, mockSettings);
 Assert.assertNotNull(mock);
 }

 @Test
 public void testMockSettings() {
 MockSettings mockSettings = new MockSettingsImpl();
 Object mock = mockitoCore.mock(String.class, mockSettings);
 Assert.assertEquals(mockSettings, mockitoCore.getMockSettings(mock));
 }

 @Test
 public void testStubbing() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 OngoingStubbing stubbing = mockitoCore.stub(mock);
 stubbing.thenReturn("Hello World");
 String result = (String) mockitoCore.verify(mock).toString();
 Assert.assertEquals("Hello World", result);
 }

 @Test
 public void testDeprecatedStubbing() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 DeprecatedOngoingStubbing<String> stubbing = mockitoCore.stub(mock);
 stubbing.thenReturn("Hello World");
 String result = (String) mockitoCore.verify(mock).toString();
 Assert.assertEquals("Hello World", result);
 }

 @Test
 public void testOngoingStubbing() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 OngoingStubbing<String> stubbing = mockitoCore.when(mock);
 stubbing.thenReturn("Hello World");
 String result = (String) mockitoCore.verify(mock).toString();
 Assert.assertEquals("Hello World", result);
 }

 @Test
 public void testVerificationModeFactory() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 VerificationMode verificationMode = VerificationModeFactory.times(1);
 mockitoCore.verify(mock, verificationMode);
 }

 @Test
 public void testVerificationModeFactoryWithType() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 VerificationMode verificationMode = VerificationModeFactory.times(1, VerificationModeType.NO_MORE_INTERACTIONS);
 mockitoCore.verify(mock, verificationMode);
 }

 @Test(expected = InvocationOnMockException.class)
 public void testVerifyNullMock() throws Exception {
 mockitoCore.verify(null, VerificationModeFactory.times(1));
 }

 @Test(expected = InvocationOnMockException.class)
 public void testNotAMockPassedToVerify() throws Exception {
 Object notAMock = new Object();
 mockitoCore.verify(notAMock, VerificationModeFactory.times(1));
 }

 @Test
 public void testVerifyNoMoreInteractions() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 String value1 = "value1";
 String value2 = "value2";
 mockitoCore.when(mock).toString().thenReturn(value1);
 mockitoCore.when(mock).toString().thenReturn(value2);
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithInOrder() throws Exception {
 Object mock1 = mockitoCore.mock(String.class);
 Object mock2 = mockitoCore.mock(String.class);
 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).toString();
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test
 public void testReset() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 String value1 = "value1";
 String value2 = "value2";
 mockitoCore.when(mock).toString().thenReturn(value1);
 mockitoCore.when(mock).toString().thenReturn(value2);
 mockitoCore.reset(mock);
 String result = (String) mockitoCore.verify(mock).toString();
 Assert.assertNull(result);
 }

 @Test(expected = InvocationOnMockException.class)
 public void testVerifyNullVerificationData() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 mockitoCore.verify(mock, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyArgumentNullMockHandler() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 Object mockHandler = null;
 mockitoCore.verify(mock, mockHandler, VerificationModeFactory.times(1));
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNotAMockPassedToVerifyNoMoreInteractions() throws Exception {
 Object notAMock = new Object();
 mockitoCore.verifyNoMoreInteractions(notAMock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsWithNullArgument() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 mockitoCore.verifyNoMoreInteractions((Object[]) null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsWithEmptyArray() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 mockitoCore.verifyNoMoreInteractions(new Object[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsWithNullInOrderContext() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 List<Object> mocks = Arrays.asList(mock);
 InOrderContext inOrderContext = null;
 mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsWithNoInvocations() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsWithUnmatchedInvocations() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 List<Invocation> invocations = Arrays.asList(new Invocation(new Object(), "mockMethod", new Object[0], null, null));
 VerificationDataInOrder data = new VerificationDataInOrderImpl(inOrder, invocations, null);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock), data);
 }

 @Test
 public void testAnswer() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 Answer<String> answer = invocation -> "Hello World";
 OngoingStubbing<String> stubbing = mockitoCore.doAnswer(answer);
 String result = (String) mockitoCore.verify(mock).toString();
 Assert.assertEquals("Hello World", result);
 }

 @Test
 public void testVoidMethodStubbable() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 VoidMethodStubbable<String> stubbing = mockitoCore.stubVoid(mock);
 stubbing.thenCallRealMethod();
 mockitoCore.verify(mock).toString();
 }

 @Test
 public void testValidateMockitoUsage() throws Exception {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() throws Exception {
 Object mock = mockitoCore.mock(String.class);
 OngoingStubbing<String> stubbing = mockitoCore.when(mock).toString();
 stubbing.thenReturn("Hello World");
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 Assert.assertEquals("toString()", lastInvocation.getMethod().getName());
 Assert.assertEquals("mock", lastInvocation.getMock());
 }
}