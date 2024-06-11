import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationOnMock;
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
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 assertNotNull(mock);
 }

 @Test
 public void testStubbing() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn(42);
 assertEquals(42, mockitoCore.verify(mock).methodCall());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStubbingWithNull() {
 mockitoCore.stub(null);
 }

 @Test
 public void testWhenMethod() {
 OngoingStubbing<Object> whenMethod = mockitoCore.when(mock);
 whenMethod.thenReturn(42);
 assertEquals(42, mockitoCore.verify(mock).methodCall());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWhenMethodWithNull() {
 mockitoCore.when(null);
 }

 @Test
 public void testVerify() {
 mockitoCore.verify(mock, VerificationMode.times(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyWithNull() {
 mockitoCore.verify(null, VerificationMode.times(1));
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsWithNull() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsInOrderWithNull() {
 mockitoCore.verifyNoMoreInteractionsInOrder(null, Mockito.inOrder(mock));
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock);
 assertNull(mockitoCore.getLastInvocation());
 }

 @Test
 public void testStubbingWithAnswer() {
 Answer<Integer> answer = arg -> arg.hashCode();
 OngoingStubbing<Object> whenMethod = mockitoCore.when(mock);
 whenMethod.thenAnswer(answer);
 assertEquals(mock.hashCode(), mockitoCore.verify(mock).methodCall());
 }

 @Test
 public void testStubbingVoidMethod() {
 Object mock = Mockito.mock(Object.class);
 Mockito.doNothing().when(mock);
 mockitoCore.verify(mock).methodCall();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastInvocationWithNoInvocations() {
 mockitoCore.verify(mock).methodCall();
 mockitoCore.reset(mock);
 mockitoCore.getLastInvocation();
 }

}