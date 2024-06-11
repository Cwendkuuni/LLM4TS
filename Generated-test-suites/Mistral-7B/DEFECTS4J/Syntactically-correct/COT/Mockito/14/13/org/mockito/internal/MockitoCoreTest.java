import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Answer answer;
 private OngoingStubbing ongoingStubbing;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock = Mockito.mock(Object.class);
 answer = ArgumentMatchers.answer(true);
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, null);
 Assert.assertNotNull(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testNullClassToMock() {
 mockitoCore.mock(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMockSettingsNull() {
 mockitoCore.mock(Object.class, null);
 }

 @Test
 public void testStubbing() {
 ongoingStubbing = mockitoCore.stub();
 Assert.assertNotNull(ongoingStubbing);
 }

 @Test(expected = IllegalStateException.class)
 public void testStubbingAfterVerification() {
 mockitoCore.verify(mock);
 mockitoCore.stub();
 }

 @Test
 public void testStubbingWithAnswer() {
 ongoingStubbing = mockitoCore.stub(mock);
 ongoingStubbing.thenAnswer(answer);
 Mockito.verify(mock).someMethod();
 }

 @Test
 public void testVerificationModeIsNull() {
 mockitoCore.verify(mock, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullMockPassedToVerify() {
 mockitoCore.verify(null, Mockito.verifyNoMoreInteractions());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullVerificationMode() {
 mockitoCore.verify(mock, null);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.when(mock).someMethod();
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsWithNullMockArray() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithEmptyMockArray() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test
 public void testVerifyInteractionsInOrder() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 mockitoCore.when(mock1).someMethod();
 mockitoCore.when(mock2).someOtherMethod();
 mockitoCore.verify(mock1).someMethod();
 mockitoCore.verify(mock2).someOtherMethod();
 mockitoCore.verify(mock1).someMethod();
 mockitoCore.verify(mock2).someOtherMethod();
 inOrder.verify();
 }

 @Test
 public void testVerifyInteractionsInOrderWithException() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 mockitoCore.when(mock1).someMethod().thenThrow(new RuntimeException());
 InOrderContext inOrderContext = inOrder.inOrder(mock1, mock2);
 try {
 mockitoCore.verify(mock1).someMethod();
 mockitoCore.verify(mock2).someOtherMethod();
 } catch (VerificationException e) {
 inOrderContext.verify();
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInOrderWithNullMockArray() {
 mockitoCore.inOrder(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInOrderWithEmptyMockArray() {
 mockitoCore.inOrder();
 }

 @Test
 public void testDoAnswer() {
 ongoingStubbing = mockitoCore.doAnswer(answer).when(mock).someMethod();
 Mockito.verify(mock).someMethod();
 assertTrue(answer.callRealMethod());
 }

 @Test
 public void testStubbingVoidMethod() {
 mockitoCore.stubVoid(mock).someMethod();
 Mockito.verify(mock).someMethod();
 }

 @Test(expected = IllegalStateException.class)
 public void testStubbingAfterVerificationWithAnswer() {
 mockitoCore.verify(mock).someMethod();mockitoCore.doAnswer(answer).when(mock).someMethod(); }

 @Test
 public void testStubbingAfterVerificationWithVoidMethod() { mockitoCore.verify(mock).someMethod();mockitoCore.stubVoid(mock).someMethod(); }

 @Test(expected = IllegalStateException.class)
 public void testStubbingAfterReset() { mockitoCore.reset(mock);mockitoCore.stub(); }

 @Test
 public void testValidateMockitoUsage() { mockitoCore.validateMockitoUsage(); }

 @Test(expected = NullPointerException.class)
 public void testGetLastInvocationWithNullOngoingStubbing() { mockitoCore.getLastInvocation(); }

 @Test
 public void testGetLastInvocation() { Invocation invocation = mockitoCore.getLastInvocation(); Assert.assertNotNull(invocation); }
}