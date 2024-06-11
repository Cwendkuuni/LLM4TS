package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal. MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.VerificationDataImpl;
import org.mockito.internal.verification.VerificationDataInOrder;
import org.mockito.internal.verification.VerificationDataInOrderImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockitoCoreTest {

 private Reporter reporter;
 private MockSettingsImpl mockSettings;
 private MockitoCore mockitoCore;
 private MockingProgress mockingProgress;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 mockingProgress = new ThreadSafeMockingProgress();
 reporter = new Reporter();
 mockitoCore = new MockitoCore() {
 private final MockUtil mockUtil = new MockUtil();

 @Override
 public <T> T mock(Class<T> classToMock, MockSettings mockSettings) {
 return mockitoCore.<T>mock(classToMock, mockSettings);
 }

 @Override
 public <T> IOngoingStubbing<T> stub() {
 return mockitoCore.<T>stub();
 }

 @Override
 public <T> DeprecatedOngoingStubbing<T> stub(T methodCall) {
 return mockitoCore.<T>stub(methodCall);
 }

 @Override
 public <T> OngoingStubbing<T> when(T methodCall) {
 return mockitoCore.<T>when(methodCall);
 }

 @Override
 public <T> T verify(T mock, VerificationMode mode) {
 return mockitoCore.<T>verify(mock, mode);
 }

 @Override
 public void reset(T... mocks) {
 mockitoCore.reset(mocks);
 }

 @Override
 public void verifyNoMoreInteractions(Object... mocks) {
 mockitoCore.verifyNoMoreInteractions(mocks);
 }

 @Override
 public void verifyNoMoreInteractionsInOrder(List<Object> mocks, InOrderContext inOrderContext) {
 mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
 }

 @Override
 public InOrder inOrder(Object... mocks) {
 return mockitoCore.inOrder(mocks);
 }

 @Override
 public Stubber doAnswer(Answer answer) {
 return mockitoCore.doAnswer(answer);
 }

 @Override
 public <T> VoidMethodStubbable<T> stubVoid(T mock) {
 return mockitoCore.<T>stubVoid(mock);
 }

 @Override
 public void validateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Override
 public Invocation getLastInvocation() {
 return mockitoCore.getLastInvocation();
 }
 };
 }

 @Test
 public void testMock() {
 Object mock = mockitoCore.mock(String.class, mockSettings);
 assertNull(mock);
 mock = mockitoCore.mock(String.class);
 assertTrue(mock instanceof String);
 }

 @Test
 public void testStub() {
 IOngoingStubbing stub = mockitoCore.stub();
 assertSame(stub, mockitoCore.stub());
 }

 @Test
 public void testStubWithArgument() {
 String arg = "arg";
 IOngoingStubbing<String> stub = mockitoCore.stub(arg);
 assertEquals(arg, stub.argThat(ArgumentMatchers.equalTo(arg)));
 }

 @Test
 public void testWhen() {
 String returnValue = "return value";
 String mock = mockitoCore.mock(String.class);
 OngoingStubbing<String> stub = mockitoCore.when(mock).thenReturn(returnValue);
 assertEquals(returnValue, stub.getMock().toString());
 }

 @Test
 public void testWhenWithArgument() {
 String arg = "arg";
 String mock = mockitoCore.mock(String.class);
 OngoingStubbing<String> stub = mockitoCore.when(mock).thenReturn(arg);
 assertEquals(arg, stub.getMock().toString());
 }

 @Test
 public void testWhenWithCallRealMethod() {
 String mock = mockitoCore.mock(String.class);
 String returnValue = "return value";
 OngoingStubbing<String> stub = mockitoCore.when(mock).thenCallRealMethod();
 assertEquals(returnValue, stub.getMock().toString());
 }

 @Test
 public void testWhenWithReturnVoid() {
 String mock = mockitoCore.mock(String.class);
 mockitoCore.when(mock).thenReturnVoid();
 verify(mock).toString();
 }

 @Test
 public void testWhenWithAnswer() {
 String mock = mockitoCore.mock(String.class);
 Answer answer = invocation -> "answer";
 OngoingStubbing<String> stub = mockitoCore.when(mock).thenAnswer(answer);
 assertEquals("answer", stub.getMock().toString());
 }

 @Test
 public void testStubVoid() {
 String mock = mockitoCore.mock(String.class);
 VoidMethodStubbable stub = mockitoCore.stubVoid(mock);
 stub.doReturn(null);
 verify(mock).toString();
 }

 @Test
 public void testVerify() {
 String mock = mockitoCore.mock(String.class);
 verify(mock).toString();
 }

 @Test
 public void testVerifyNullPassedToVerify() {
 assertNull(mockitoCore.verify(null, VerificationModeFactory.never()));
 }

 @Test
 public void testVerifyNotAMockPassedToVerify() {
 Object notAMock = new Object();
 assertNull(mockitoCore.verify(notAMock, VerificationModeFactory.never()));
 }

 @Test
 public void testVerifyNoInteractions() {
 String mock = mockitoCore.mock(String.class);
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoInteractionsWithMultipleMocks() {
 String mock1 = mockitoCore.mock(String.class);
 String mock2 = mockitoCore.mock(String.class);
 verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testVerifyInteractions() {
 String mock = mockitoCore.mock(String.class);
 when(mock).thenReturn("mock");
 verify(mock).toString();
 }

 @Test
 public void testVerifyOrder() {
 String mock1 = mockitoCore.mock(String.class);
 String mock2 = mockitoCore.mock(String.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.toString()).thenReturn("mock1");
 when(mock2.toString()).thenReturn("mock2");
 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).toString();
 }

 @Test
 public void testVerifyOrderWithExceptions() {
 String mock1 = mockitoCore.mock(String.class);
 String mock2 = mockitoCore.mock(String.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.toString()).thenThrow(new RuntimeException());
 when(mock2.toString()).thenReturn("mock2");
 try {
 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).toString();
 } catch (Exception e) {
 assertEquals("Expected interaction 'toString() of mock: <[mock1]>" +
         " but interaction did not occur.", e.getMessage());
 }
 }

 @Test
 public void testVerifyNoMoreInteractionsWithExceptions() {
 String mock = mockitoCore.mock(String.class);
 when(mock.toString()).thenThrow(new RuntimeException());
 try {
 mockitoCore.verifyNoMoreInteractions(mock);
 } catch (Exception e) {
 assertEquals("Unexpected interaction.", e.getMessage());
 }
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 String mock1 = mockitoCore.mock(String.class);
 String mock2 = mockitoCore.mock(String.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 InOrderContext inOrderContext = inOrder.inOrder();
 when(mock1.toString()).thenReturn("mock1");
 when(mock2.toString()).thenReturn("mock2");
 inOrderContext.verify(mock1).toString();
 inOrderContext.verify(mock2).toString();
 inOrder.verifyNoMoreInteractions();
 }

 @Test
 public void testInOrder() {
 String mock1 = mockitoCore.mock(String.class);
 String mock2 = mockitoCore.mock(String.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 assertEquals(Arrays.asList(mock1, mock2), inOrder.getMocks());
 }

 @Test
 public void testDoAnswer() {
 String mock = mockitoCore.mock(String.class);
 Answer answer = invocation -> invocation.getArgument(0) + " answer";
 OngoingStubbing<String> stub = mockitoCore.doAnswer(answer).when(mock).toString();
 assertEquals("mock answer", stub.getMock().toString());
 }

 @Test
 public void testReset() {
 String mock = mockitoCore.mock(String.class);
 mockitoCore.when(mock).thenReturn("mock");
 mockitoCore.reset(mock);
 assertNull(mockitoCore.verify(mock).toString());
 }

 @Test
 public void testResetMultipleMocks() {
 String mock1 = mockitoCore.mock(String.class);
 String mock2 = mockitoCore.mock(String.class);
 mockitoCore.when(mock1).thenReturn("mock1");
 mockitoCore.when(mock2).thenReturn("mock2");
 mockitoCore.reset(mock1, mock2);
 assertNull(mockitoCore.verify(mock1).toString());
 assertNull(mockitoCore.verify(mock2).toString());
 }

 @Test
 public void testResetWithNullMocks() {
 mockitoCore.reset();
 }

 @Test
 public void testVerifyMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 String mock = mockitoCore.mock(String.class);
 when(mock.toString()).thenReturn("mock");
 OngoingStubbing<String> stub = mockitoCore.when(mock).thenReturn("return value");
 Invocation invocation = mockitoCore.getLastInvocation();
 assertEquals("return value", invocation.getArgument(0));
 }
}