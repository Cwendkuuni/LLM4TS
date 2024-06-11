import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mock = Mockito.mock(Object.class);
 }

 @Test
 public void testMock() {
 Object mock = mockitoCore.<Object>mock(Object.class);
 assertNotNull(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testMockWithNullClass() {
 mockitoCore.<Object>mock(null, new MockSettingsImpl());
 }

 @Test(expected = NullPointerException.class)
 public void testMockWithNullMockSettings() {
 mockitoCore.<Object>mock(Object.class, null);
 }

 @Test
 public void testStub() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn(42);
 assertEquals(42, mockitoCore.verify(mock).someMethod());
 }

 @Test
 public void testWhen() {
 Object arg = new Object();
 OngoingStubbing<Object> stubbing = mockitoCore.when(mock).thenReturn(arg);
 assertEquals(arg, mockitoCore.verify(mock).someMethod());
 }

 @Test
 public void testReset() {
 Object arg = new Object();
 OngoingStubbing<Object> stubbing = mockitoCore.when(mock).thenReturn(arg);
 mockitoCore.reset();
 assertEquals(null, mockitoCore.verify(mock).someMethod());
 }

 @Test
 public void testVerify() {
 mockitoCore.verify(mock).someMethod();
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNullMock() {
 mockitoCore.verify(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyOnNonMock() {
 mockitoCore.verify(new Object());
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.stub(mock).thenReturn(42);
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNoMoreInteractionsNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsNonMock() {
 mockitoCore.verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testInOrder() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 }

 @Test(expected = NullPointerException.class)
 public void testInOrderNullMock() {
 Object mock = Mockito.mock(Object.class);
 mockitoCore.inOrder(null).verify(mock).someMethod();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInOrderNonMock() {
 Object mock = new Object();
 mockitoCore.inOrder(mock).verify(mock).someMethod();
 }

 @Test
 public void testAnswer() {
 Answer answer = invocation -> invocation.getArgument(0);
 OngoingStubbing<Object> stubbing = mockitoCore.stub(mock).thenAnswer(answer);
 assertEquals(mock, stubbing.getMock());
 assertEquals(mock, stubbing.getMock()).someMethod();
 assertEquals(mock, mockitoCore.verify(mock).someMethod().getArgument(0));
 }

 @Test
 public void testVoidMethodStubbable() {
 Object mock = Mockito.mock(Object.class);
 mockitoCore.stubVoid(mock).someVoidMethod();
 mockitoCore.verify(mock).someVoidMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertNotNull(lastInvocation);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastInvocationAfterStubbingReset() {
 mockitoCore.stub(mock).thenReturn(42);
 mockitoCore.getLastInvocation();
 mockitoCore.reset();
 mockitoCore.getLastInvocation();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastInvocationAfterVerificationReset() {
 mockitoCore.stub(mock).thenReturn(42);
 mockitoCore.getLastInvocation();
 mockitoCore.verify(mock).someMethod();
 mockitoCore.getLastInvocation();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastInvocationAfterInOrderReset() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 mockitoCore.getLastInvocation();
 mockitoCore.reset();
 mockitoCore.getLastInvocation();
 }

 @Test(expected = AssertionError.class)
 public void testGetLastInvocationWithNoInvocations() {
 mockitoCore.getLastInvocation();
 }
}