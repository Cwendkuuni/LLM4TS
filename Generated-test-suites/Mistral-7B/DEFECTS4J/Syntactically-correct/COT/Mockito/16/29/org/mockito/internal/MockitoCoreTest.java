import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.MockitoInternalVerifications;
import org.mockito.MockitoSession;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(org.mockito.runners.JUnit4.class)
public class MockitoCoreTest {

 private Reporter reporter;
 private MockUtil mockUtil;
 private MockingProgress mockingProgress;
 private MockSettings mockSettings;
 private Object mock;
 private IOngoingStubbing ongoingStubbing;
 private Invocation lastInvocation;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 reporter = MockitoCore.new Reporter();
 mockUtil = MockitoCore.new MockUtil();
 mockingProgress = MockitoCore.new ThreadSafeMockingProgress();
 }

 @Test
 public void testMock() {
 mock = MockitoCore.mock(String.class, new MockSettingsImpl(), false);
 assert mock != null;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMockNullClass() {
 MockitoCore.mock(null, new MockSettingsImpl(), false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMockNullSettings() {
 MockitoCore.mock(String.class, null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMockResetOngoingStubbingTrue() {
 mockingProgress.resetOngoingStubbing();
 mock = MockitoCore.mock(String.class, new MockSettingsImpl(), true);
 }

 @Test
 public void testStub() {
 ongoingStubbing = MockitoCore.stub();
 assert ongoingStubbing != null;
 }

 @Test(expected = IllegalStateException.class)
 public void testStubNullOngoingStubbing() {
 mockingProgress.reset();
 ongoingStubbing = MockitoCore.stub();
 }

 @Test
 public void testWhen() {
 when(mock).thenReturn("test");
 String result = (String) mock.hashCode();
 assert result.equals("test");
 }

 @Test(expected = IllegalStateException.class)
 public void testWhenNullMock() {
 when(null).thenReturn("test");
 }

 @Test(expected = IllegalStateException.class)
 public void testWhenNullMethodCall() {
 when(mock, null).thenReturn("test");
 }

 @Test
 public void testVerify() {
 verify(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNullMock() {
 verify(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 verify(new Object());
 }

 @Test
 public void testReset() {
 verify(mock).hashCode();
 MockitoCore.reset(mock);
 verifyNoInteractions(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResetNullMocks() {
 MockitoCore.reset(null);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mock = mock(String.class, new MockSettingsImpl(), false);
 verifyNoMoreInteractions(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsNullMocks() {
 verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testInOrder() {
 mock = mock(String.class, new MockSettingsImpl(), false);
 mock = Mockito.mock(String[].class, new MockSettingsImpl(), false);
 InOrder inOrder = Mockito.inOrder(mock, (Object) mock);
 inOrder.verify(mock).hashCode();
 inOrder.verify(mock).toString();
 inOrder.verify(mock).equals(mock);
 inOrder.verify(mockArray).length();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInOrderNullMocks() {
 InOrder inOrder = Mockito.inOrder(null);
 }

 @Test
 public void testAnswer() throws Exception {
 Answer<String> answer = new Answer<String>() {
 @Override public String answer(InvocationOnMock invocation) throws Throwable {
 return "test";
 }
 };
 doAnswer(answer).when(mock).hashCode();
 String result = (String) mock.hashCode();
 assert result.equals("test");
 }

 @Test
 public void testVoidMethodStubbable() {
 mock = mock(String.class, new MockSettingsImpl(), false);
 MockitoCore.stubVoid(mock).hashCode();
 }

 @Test(expected = IllegalStateException.class)
 public void testStubVoidNullMock() {
 MockitoCore.stubVoid(null);
 }

 @Test(expected = AssertionError.class)
 public void testValidateMockitoUsage() {
 MockitoCore validateMockitoUsage = new MockitoCore();
 validateMockitoUsage.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 mock = mock(String.class, new MockSettingsImpl(), false);
 when(mock).hashCode();
 ongoingStubbing = MockitoCore.stub();
 doAnswer(new Answer<Void>() {
 @Override public Void answer(InvocationOnMock invocation) throws Throwable {
 lastInvocation = invocation;
 return null;
 }
 }).when(mock).hashCode();
 mockingProgress.pushOngoingStubbing(ongoingStubbing);
 verify(mock).hashCode();
 verify(mock).hashCode();
 MockitoCore.reset();
 assert lastInvocation.getArgumentAt(0) == null;
 assert lastInvocation.getMock() == mock;
 }

 @Test
 public void testVerifyStubbing() {
 mock = mock(String.class, new MockSettingsImpl(), false);
 when(mock).thenThrow(new RuntimeException());
 try {
 mock.hashCode();
 } catch (RuntimeException e) {
 }
 verify(mock).hashCode();
 verify(mock).hashCode();
 }

 @Test(expected = RuntimeException.class)
 public void testVerifyStubbingThrowsException() {
 mock = mock(String.class, new MockSettingsImpl(), false);
 when(mock).thenThrow(new RuntimeException());
 try {
 mock.hashCode();
 } catch (RuntimeException e) {
 throw e;
 }
 }

 @Test
 public void testVerifyStubbingVerificationMode() {
 mock = mock(String.class, new MockSettingsImpl(), false);
 when(mock).thenThrow(new RuntimeException());
 try {
 MockitoCore.verify(mock, Mockito.times(Mockito.never()));
 } catch (AssertionError e) {
 }
 }

 @Test
 public void testVerifyStubbingMultipleMocks() {
 mock = mock(String.class, new MockSettingsImpl(), false);
 String mockString = mock(String.class, new MockSettingsImpl(), false);
 when(mock).thenReturn("test1");
 when(mockString).thenReturn("test2");
 verify(mock).hashCode();
 verify(mockString).hashCode();
 verify(mock).equals(mockString);
 }

 @Test
 public void testVerifyStubbingListOfMocks() {
 List<Object> mocks = Arrays.asList(mock, mock);
 verify(mocks.toArray()).length();
 }

 @Test
 public void testVerifyStubbingArrayOfMocks() {
 Object[] mocks = new Object[]{mock, mock};
 verify(mocks).length();
 }

 @Test
 public void testVerifyStubbingListOfMocksWithNull() {
 List<Object> mocks = Arrays.asList(mock, null, mock);
 verify(mocks.toArray()).length();
 }

 @Test
 public void testVerifyStubbingArrayOfMocksWithNull() {
 Object[] mocks = new Object[]{mock, null, mock};
 verify(mocks).length();
 }

 @Test
 public void testVerifyStubbingListOfMocksWithEmptyList() {
 List<Object> mocks = Arrays.asList();
 verify(mocks.toArray()).length();
 }

 @Test
 public void testVerifyStubbingArrayOfMocksWithEmptyArray() {
 Object[] mocks = new Object[0];
 verify(mocks).length();
 }
}