import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InternalVerificationMode;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.api.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyOrder;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock private Object mock1;
 @Mock private Object mock2;
 @Mock private Object mock3;
 @Mock private Reporter reporter;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 Mockito.doReturn(reporter).when(mockitoCore).new Reporter();
 }

 @Test
 public void testMock() {
 Object mockedObject = mockitoCore.mock(String.class, MockSettings.none());
 verify(mockedObject).toString();
 }

 @Test(expected = MockitoException.class)
 public void testMockWithNullMockSettings() {
 mockitoCore.mock(String.class, null);
 }

 @Test
 public void testStub() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn("Hello");
 String result = mockitoCore.verify(mock1).someMethod();
 verify(mock1).someMethod();
 verify(mock1).someMethod();
 verify(mock1).someMethod();
 expect(result).toEqualignoringCase("hello");
 }

 @Test(expected = MockitoException.class)
 public void testStubWithNullMethodCall() {
 mockitoCore.stub().thenReturn(null);
 }

 @Test
 public void testWhen() {
 Object mock = mockitoCore.mock(String.class, MockSettings.none());
 IOngoingStubbing stubbing = mockitoCore.when(mock);
 stubbing.thenReturn("Hello");
 String result = (String) mockitoCore.verify(mock).someMethod();
 expect(result).toEqualignoringCase("hello");
 }

 @Test(expected = MockitoException.class)
 public void testWhenWithNullMethodCallAndNullMock() {
 mockitoCore.when(null).someMethod();
 }

 @Test
 public void testVerify() {
 mockitoCore.verify(mock1, InternalVerificationMode.STRICT);
 }

 @Test(expected = MockitoException.class)
 public void testVerifyWithNullMock() {
 mockitoCore.verify(null);
 }

 @Test
 public void testReset() {
 Object mock = mockitoCore.mock(String.class, MockSettings.none());
 mockitoCore.reset(mock1, mock2, mock3);
 verifyNoInteractions(mock1, mock2, mock3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsWithNull() {
 mockitoCore.verifyNoMoreInteractions((Object) null);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(mock1, mock2, mock3);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock = mockitoCore.mock(String.class, MockSettings.none());
 InOrder inOrder = mockitoCore.inOrder(mock);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock), inOrder);
 }

 @Test
 public void testInOrder() {
 Object mock = mockitoCore.mock(String.class, MockSettings.none());
 InOrder inOrder = mockitoCore.inOrder(mock);
 ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
 mockitoCore.verify(mock).someMethod(stringArgumentCaptor.capture());
 expect(stringArgumentCaptor.getValue()).toEqualignoringCase("expectedValue");
 verifyOrder(inOrder);
 }

 @Test
 public void testDoAnswer() {
 Object mock = mockitoCore.mock(String.class, MockSettings.none());
 Answer<Integer> answer = invocation -> 42;
 mockitoCore.doAnswer(answer).when(mock).someMethod();
 int result = mockitoCore.verify(mock).someMethod();
 expect(result).toEqualignoringCase(42);
 }

 @Test
 public void testStubVoid() {
 Object mock = mockitoCore.mock(String.class, MockSettings.none());
 mockitoCore.stubVoid(mock).someMethod();
 mockitoCore.verify(mock).someMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastInvocationAfterStubbing() {
 mockitoCore.when(mockitoCore.mock(String.class, MockSettings.none())).thenReturn("Hello");
 Invocation invocation = mockitoCore.getLastInvocation();
 expect(invocation.getMethod().getName()).toEqualignoringCase("thenReturn");
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastInvocationBeforeStubbing() {
 Invocation invocation = mockitoCore.getLastInvocation();
 }
}