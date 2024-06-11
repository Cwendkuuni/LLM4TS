package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationModeFactory;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockitoCoreTest {

 private Reporter reporter;
 private MockUtil mockUtil;
 private MockingProgress mockingProgress;
 private MockitoCore mockitoCore;

 @Before
 public void setup() {
 reporter = new Reporter();
 mockUtil = spy(new MockUtil());
 mockingProgress = new ThreadSafeMockingProgress();
 mockitoCore = new MockitoCore() {
 private final Reporter reporter = this.reporter;
 private final MockUtil mockUtil = this.mockUtil;
 private final MockingProgress mockingProgress = this.mockingProgress;

 @Override
 public <T> T mock(Class<T> classToMock, MockSettings mockSettings) {
 return super.mock(classToMock, mockSettings);
 }

 @Override
 public IOngoingStubbing stub() {
 return super.stub();
 }

 @Override
 public <T> DeprecatedOngoingStubbing<T> stub(T methodCall) {
 return super.stub(methodCall);
 }

 @Override
 public <T> OngoingStubbing<T> when(T methodCall) {
 return super.when(methodCall);
 }

 @Override
 public <T> T verify(T mock, VerificationMode mode) {
 return super.verify(mock, mode);
 }

 @Override
 public void reset(T... mocks) {
 super.reset(mocks);
 }

 @Override
 public void verifyNoMoreInteractions(Object... mocks) {
 super.verifyNoMoreInteractions(mocks);
 }

 @Override
 public void verifyNoMoreInteractionsInOrder(List<Object> mocks, InOrderContext inOrderContext) {
 super.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
 }

 @Override
 public InOrder inOrder(Object... mocks) {
 return super.inOrder(mocks);
 }

 @Override
 public Stubber doAnswer(Answer answer) {
 return super.doAnswer(answer);
 }

 @Override
 public <T> VoidMethodStubbable<T> stubVoid(T mock) {
 return super.stubVoid(mock);
 }

 @Override
 public void validateMockitoUsage() {
 super.validateMockitoUsage();
 }

 @Override
 public Invocation getLastInvocation() {
 return super.getLastInvocation();
 }
 };
 }

 @Test
 public void testMock() {
 Class<String> stringClass = String.class;
 String mock = mockitoCore.mock(stringClass, new MockSettingsImpl());
 assertNull(mock.length());
 assertSame(stringClass, mock.getClass());
 assertTrue(mockitoCore.mockingProgress.isMockingInProgress(mock));
 }

 @Test
 public void testStub() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn("mock return value");
 String returnedValue = (String) mockitoCore.verify(mock).when(mockitoCore.getLastInvocation()).thenReturn("mock return value").getMock();
 assertEquals("mock return value", returnedValue);
 }

 @Test
 public void testDeprecatedStub() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 DeprecatedOngoingStubbing<String> stubbing = mockitoCore.stub((String) ArgumentMatchers.anyString());
 stubbing.thenReturn("mock return value");
 String returnedValue = mockitoCore.verify(mock).when(mockitoCore.getLastInvocation()).thenReturn("mock return value").getMock();
 assertEquals("mock return value", returnedValue);
 }

 @Test
 public void testWhen() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 OngoingStubbing<Void> stubbing = mockitoCore.when((Void) ArgumentMatchers.any());
 stubbing.thenReturn(null);
 assertTrue(mockitoCore.verify(mock).when(mockitoCore.getLastInvocation()).thenReturn(null).called());
 }

 @Test
 public void testVerify() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mock).someMethod();
 assertTrue(mockitoCore.mockingProgress.isVerified(mock));
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object[] mocks = { mock1, mock2 };
 mockitoCore.verifyNoMoreInteractions(mocks);
 List<Invocation> invocationsMock1 = mockitoCore.getLastInvocation().getInvocationContainer().getInvocations();
 List<Invocation> invocationsMock2 = mockitoCore.getLastInvocation().getInvocationContainer().getInvocations();
 assertTrue(invocationsMock1.isEmpty());
 assertTrue(invocationsMock2.isEmpty());
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test
 public void testInOrder() {
 Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 }

 @Test
 public void testDoAnswer() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Answer<Integer> answer = invocation -> 42;
 mockitoCore.doAnswer(answer).when(mock).someMethod();
 assertEquals(42, mockitoCore.verify(mock).someMethod().getInvocation().getArgument(0));
 }

 @Test
 public void testStubVoid() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 VoidMethodStubbable<Void> stubbing = mockitoCore.stubVoid(mock);
 stubbing.then();
 mockitoCore.verify(mock).someVoidMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithNullMock() {
 mockitoCore.verify(null, VerificationModeFactory.instance().times(1));
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsWithNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsWithNonMockObject() {
 Object nonMockObject = new Object();
 mockitoCore.verifyNoMoreInteractions(nonMockObject);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsInOrderWithNullMock() {
 Object nonMockObject = new Object();
 InOrder inOrder = mockitoCore.inOrder(nonMockObject);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(nonMockObject), inOrder);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsInOrderWithNonMockObjects() {
 Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object nonMockObject = new Object();
 InOrder inOrder = mockitoCore.inOrder(mock1, nonMockObject);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, nonMockObject), inOrder);
 }

 @Test(expected = NotAMockException.class)
 public void testResetWithNullOrEmptyMockArray() {
 mockitoCore.reset();
 mockitoCore.reset(new Object[0]);
 }

 @Test(expected = NotAMockException.class)
 public void testResetWithNullOrEmptyMockList() {
 List<Object> mocks = Arrays.asList(new Object[0]);
 mockitoCore.reset(mocks.toArray(new Object[0]));
 }

 @Test(expected = NotAMockException.class)
 public void testResetWithNonMockObjects() {
 Object nonMockObject1 = new Object();
 Object nonMockObject2 = new Object();
 mockitoCore.reset(nonMockObject1, nonMockObject2);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithNonMockMock() {
 Object nonMockMock = new Object();
 mockitoCore.verify(nonMockMock, VerificationModeFactory.instance().times(1));
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithNullVerificationMode() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 mockitoCore.verify(mock, null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithNullVerificationData() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 VerificationDataImpl data = new VerificationDataImpl(null, null);
 mockitoCore.verify(mock, data);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithVerificationModeThatDoesNotMatchInvocations() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 VerificationDataImpl data = new VerificationDataImpl(Arrays.asList(new Invocation(1, "someMethod", new Object[0], null)), null);
 mockitoCore.verify(mock, VerificationModeFactory.instance().times(2));
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithVerificationModeThatDoesNotMatchInvocationsInOrder() {
 Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 VerificationDataInOrder data = new VerificationDataInOrderImpl(inOrder, Arrays.asList(new Invocation(1, "someMethod1", new Object[0], null), new Invocation(1, "someMethod2", new Object[0], null)), null);
 mockitoCore.verify(mock1, mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder).getActualData());
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 OngoingStubbing<String> stubbing = mockitoCore.when((String) ArgumentMatchers.anyString());
 stubbing.thenReturn("mock return value");
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertEquals("someMethod(String)", lastInvocation.getMethod().getName());
 assertEquals("mock return value", lastInvocation.getArguments()[0]);
 }
}