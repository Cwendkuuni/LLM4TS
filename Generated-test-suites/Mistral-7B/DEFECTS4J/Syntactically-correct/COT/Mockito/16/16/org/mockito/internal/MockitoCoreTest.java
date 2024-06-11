import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockUtil;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockingProgress mockingProgress;
 private MockUtil mockUtil;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mockingProgress = mockitoCore.mockingProgress;
 mockUtil = mockitoCore.mockUtil;
 }

 @Test
 public void testMock() {
 Class<String> stringClass = String.class;
 MockSettings mockSettings = new MockSettingsImpl();
 String mock = mockitoCore.mock(stringClass, mockSettings, false);
 assertNotNull(mock);
 }

 @Test(expected = IllegalStateException.class)
 public void testMockWithMoockingProgressNotInitialized() {
 mockitoCore.mock(String.class, null, false);
 }

 @Test
 public void testStub() {
 Object mock = Mockito.mock(Object.class);
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn("Return Value");
 String returnValue = (String) mockitoCore.when(mock).thenReturn("Return Value").getMock();
 assertEquals("Return Value", returnValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testStubWithoutStubbingStarted() {
 Object mock = Mockito.mock(Object.class);
 mockitoCore.when(mock).thenReturn("Return Value");
 }

 @Test
 public void testVerify() {
 Object mock = Mockito.mock(Object.class);
 mockitoCore.verify(mock, Mockito.verifyNoMoreInteractions(mock));
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyWithNullMock() {
 mockitoCore.verify(null, Mockito.verifyNoMoreInteractions(null));
 }

 @Test(expected = AssertionError.class)
 public void testVerifyWithNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verify(notAMock, Mockito.verifyNoMoreInteractions(notAMock));
 }

 @Test
 public void testReset() {
 Object mock = Mockito.mock(Object.class);
 Object resetMock = Mockito.mock(Object.class);
 mockitoCore.reset(mock, resetMock);
 Mockito.verifyNoMoreInteractions(mock, resetMock);
 }

 @Test(expected = NullPointerException.class)
 public void testResetWithNullMock() {
 mockitoCore.reset(null);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = Mockito.mock(Object.class);
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNoMoreInteractionsWithNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testInOrder() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).method1();
 inOrder.verify(mock2).method2();
 }

 @Test(expected = NullPointerException.class)
 public void testInOrderWithNullMock() {
 InOrder inOrder = mockitoCore.inOrder(null);
 }

 @Test
 public void testDoAnswer() {
 Object mock = Mockito.mock(Object.class);
 Answer<Integer> answer = invocation -> 42;
 Stubber stubber = mockitoCore.doAnswer(answer);
 stubber.thenReturn(mock);
 assertEquals(42, (int) mockitoCore.when(mock).thenAnswer(answer).getMock().method1());
 }

 @Test(expected = NullPointerException.class)
 public void testDoAnswerWithNullAnswer() {
 Stubber stubber = mockitoCore.doAnswer(null);
 }

 @Test
 public void testStubVoid() {
 Object mock = Mockito.mock(Object.class);
 Stubber stubber = mockitoCore.stubVoid(mock);
 stubber.thenReturnVoid();
 mockitoCore.when(mock).method1().thenReturnVoid();
 mockitoCore.verify(mock).method1();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = Mockito.mock(Object.class);
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn("Return Value");
 String returnValue = (String) mockitoCore.when(mock).thenReturn("Return Value").getMock();
 mockitoCore.verify(mock).method1();
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertEquals("method1", lastInvocation.getMethod().getName());
 assertEquals(mock, lastInvocation.getMock());
 }
}