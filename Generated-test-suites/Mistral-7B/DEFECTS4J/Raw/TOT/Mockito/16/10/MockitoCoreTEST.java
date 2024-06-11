import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

 @Mock
 private MockingProgress mockingProgress;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testMockCreation() {
 Class<String> stringClass = String.class;
 String mock = Mockito.mock(stringClass, new MockSettingsImpl());
 assertNotNull(mock);
 }

 @Test
 public void testStubbing() {
 Object mock = Mockito.mock(Object.class);
 Object argument = new Object();
 when(mock.someMethod(any(Object.class))).thenReturn(argument);
 assertEquals(argument, mock.someMethod(new Object()));
 }

 @Test
 public void testVerification() {
 Object mock = Mockito.mock(Object.class);
 verify(mock).someMethod(any(Object.class));
 }

 @Test(expected = AssertionError.class)
 public void testVerificationWithNullMock() {
 verify(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerificationWithNotAMock() {
 Object notAMock = new Object();
 verify(notAMock).someMethod(any(Object.class));
 }

 @Test
 public void testReset() {
 Object mock = Mockito.mock(Object.class);
 when(mock.someMethod(any(Object.class))).thenReturn(1);
 reset(mock);
 verify(mock).someMethod(any(Object.class)).times(0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsWithNullMocks() {
 verifyNoMoreInteractions();
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsWithNotAMock() {
 Object notAMock = new Object();
 verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = Mockito.mock(Object.class);
 mock.someMethod(any(Object.class));
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testInOrder() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 InOrder inOrder = inOrder(mock1, mock2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 }

 @Test
 public void testAnswer() {
 Object mock = Mockito.mock(Object.class);
 Answer<Integer> answer = invocation -> 42;
 doAnswer(answer).when(mock).someMethod(any(Object.class));
 assertEquals(42, mock.someMethod(new Object()));
 }

 @Test
 public void testVoidMethodStubbing() {
 Object mock = Mockito.mock(Object.class);
 doAnswer(arg -> {}).when(mock).someVoidMethod();
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = Mockito.mock(Object.class);
 Invocation invocation = mock.someMethod(new Object());
 Invocation lastInvocation = MockitoCore.getInstance().getLastInvocation();
 assertEquals(invocation, lastInvocation);
 }

 @Test
 public void testValidateMockitoUsage() {
 MockitoCore core = MockitoCore.getInstance();
 core.validateMockitoUsage();
 }
}