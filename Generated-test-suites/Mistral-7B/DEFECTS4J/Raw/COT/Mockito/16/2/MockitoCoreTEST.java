import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.*;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockingProgress mockingProgress;
 private Object mock;
 private Object mock2;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 mockingProgress = mockitoCore.mockingProgress;
 }

 @Test
 public void testMock() {
 Class<Object> mockClass = Object.class;
 MockSettings mockSettings = new MockSettingsImpl();
 Object result = mockitoCore.mock(mockClass, mockSettings, false);
 Assert.assertNotNull(result);
 Assert.assertTrue(mockitoCore.mockUtil.isMock(result));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMockWithNullMockSettings() {
 Class<Object> mockClass = Object.class;
 Object result = mockitoCore.mock(mockClass, null, false);
 }

 @Test
 public void testStub() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 Assert.assertNotNull(stubbing);
 }

 @Test(expected = IllegalStateException.class)
 public void testStubbingWhenNoOngoingStubbing() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn(null);
 mockitoCore.stubbingStarted();
 mockitoCore.stubbingStarted(); // should throw an exception
 }

 @Test(expected = RuntimeException.class)
 public void testWhenWithNullMock() {
 Object mock = null;
 mockitoCore.when(mock).thenReturn(null);
 }

 @Test
 public void testWhenWithMock() {
 mock = Mockito.mock(Object.class);
 OngoingStubbing<Object> whenStub = mockitoCore.when(mock);
 whenStub.thenReturn(null);
 Object result = whenStub.getMock();
 Assert.assertNotNull(result);
 Assert.assertEquals(mock, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWhenWithNullArgument() {
 Object mock = Mockito.mock(Object.class);
 mockitoCore.when((Object) null).thenReturn(mock);
 }

 @Test
 public void testVerify() {
 mock = Mockito.mock(Object.class);
 mockitoCore.verify(mock, ArgumentMatchers.isA(Object.class));
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyWithNullMock() {
 mockitoCore.verify(null, ArgumentMatchers.isA(Object.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyWithNonMockObject() {
 Object nonMockObject = new Object();
 mockitoCore.verify(nonMockObject, ArgumentMatchers.isA(Object.class));
 }

 @Test
 public void testReset() {
 mock = Mockito.mock(Object.class);
 mockitoCore.reset(mock);
 Assert.assertFalse(mockitoCore.mockUtil.isMock(mock));
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mock = Mockito.mock(Object.class);
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsWithNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithMultipleMocks() {
 mock = Mockito.mock(Object.class);
 mock2 = Mockito.mock(Object.class);
 List<Object> mocks = Arrays.asList(mock, mock2);
 mockitoCore.verifyNoMoreInteractions(mocks.toArray(new Object[0]));
 }

 @Test
 public void testInOrder() {
 mock = Mockito.mock(Object.class);
 mock2 = Mockito.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock, mock2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInOrderWithNullMock() {
 mockitoCore.inOrder(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInOrderWithNullMockInList() {
 List<Object> mocks = Arrays.asList(mock, null);
 mockitoCore.inOrder(mocks.toArray(new Object[0]));
 }

 @Test
 public void testDoAnswer() {
 Answer<Integer> answer = invocation -> 42;
 Stubber stubber = mockitoCore.doAnswer(answer);
 Object mock = mockitoCore.mock(Integer.class);
 stubber.doAnswer(answer).when(mock).getMock();
 Assert.assertEquals(42, mockitoCore.getLastInvocation().getArgument(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoAnswerWithNullAnswer() {
 mockitoCore.doAnswer(null);
 }

 @Test
 public void testStubVoid() {
 mock = Mockito.mock(Object.class);
 StubberImpl stubberImpl = (StubberImpl) mockitoCore.doAnswer(ArgumentCaptor.instanceOf(Answer.class));
 MockHandlerInterface<Object> handler = mockitoCore.mockUtil.getMockHandler(mock);
 handler.voidMethodStubbable(mock).andDo(stubberImpl.doNothing());
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastInvocationWhenNoInvocation() {
 mockitoCore.getLastInvocation();
 }
}