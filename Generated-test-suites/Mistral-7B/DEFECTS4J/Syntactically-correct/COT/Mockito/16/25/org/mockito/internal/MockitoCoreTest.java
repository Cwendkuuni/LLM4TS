import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.api.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.doAnswer;

@RunWith(MockitoJUnitRunner.class)
public class MockitoCoreTest {

 private Reporter reporter;
 private MockUtil mockUtil;
 private MockingProgress mockingProgress;

 @Before
 public void setup() {
 reporter = new Reporter();
 mockUtil = new MockUtil();
 mockingProgress = new ThreadSafeMockingProgress();
 }

 // Test for the mock method
 @Test
 public <T> void testMock() {
 Class<T> classToMock = Mockito.mock(T.class);
 T mock = MockitoCore.mock(classToMock, new MockSettingsImpl(), false);
 Mockito.verify(mock).toString();
 }

 // Test for the stub method
 @Test
 public void testStub() {
 IOngoingStubbing stub = MockitoCore.stub();
 Mockito.when(mock(Object.class)).thenReturn("test");
 Object result = stub.thenReturn("testResult");
 Mockito.verify(mock(Object.class)).thenReturn("test");
 Mockito.verify(mock(Object.class)).thenReturn(result);
 }

 // Test for the when method
 @Test
 public <T> void testWhen() {
 T mock = mock(T.class);
 OngoingStubbing<T> when = MockitoCore.when(mock);
 when.thenReturn("test");
 Mockito.verify(mock).toString();
 Mockito.verify(mock).thenReturn("test");
 }

 // Test for the verify method
 @Test(expected = AssertionError.class)
 public <T> void testVerifyNull() {
 MockitoCore.verify(null, Mockito.eq("test"));
 }

 @Test
 public <T> void testVerifyNotAMock() {
 Object notAMock = new Object();
 MockitoCore.verify(notAMock, Mockito.eq("test"));
 }

 @Test
 public <T> void testVerify() {
 T mock = mock(T.class);
 MockitoCore.verify(mock, Mockito.eq("test"));
 }

 // Test for the reset method
 @Test
 public void testReset() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 MockitoCore.reset(mock1, mock2);
 Mockito.verifyNoMoreInteractions(mock1, mock2);
 }

 // Test for the verifyNoMoreInteractions method
 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNull() {
 MockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 Object notAMock = new Object();
 MockitoCore.verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = mock(Object.class);
 MockitoCore.verifyNoMoreInteractions(mock);
 }

 // Test for the inOrder method
 @Test
 public void testInOrder() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InOrder inOrder = MockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).toString();
 }

 // Test for the doAnswer method
 @Test
 public void testDoAnswer() {
 Object mock = mock(Object.class);
 Answer answer = invocation -> "test";
 Stubber stubber = MockitoCore.doAnswer(answer);
 stubber.thenReturn("testResult");
 Mockito.verify(mock).toString();
 Mockito.verify(mock).thenAnswer(answer).thenReturn("testResult");
 }

 // Test for the stubVoid method
 @Test
 public void testStubVoid() {
 Object mock = mock(Object.class);
 StubberImpl stubber = new StubberImpl();
 VoidMethodStubbable voidMethodStubbable = MockitoCore.stubVoid(mock);
 voidMethodStubbable.answer(stubber.thenCallRealMethod());
 stubber.thenAnswer(invocation -> null);
 Mockito.verify(mock).toString();
 Mockito.verify(mock).thenAnswer(invocation -> null);
 }

 // Test for the validateMockitoUsage method
 @Test
 public void testValidateMockitoUsage() {
 MockitoCore.validateMockitoUsage();
 }

 // Test for the getLastInvocation method
 @Test
 public void testGetLastInvocation() {
 Object mock = mock(Object.class);
 IOngoingStubbing stub = MockitoCore.stub();
 stub.thenReturn("test");
 Invocation lastInvocation = MockitoCore.getLastInvocation();
 Mockito.verify(mock).thenReturn("test");
 Mockito.verify(mock).thenReturn(lastInvocation.getArgument());
 }

 // Test for the edge case: mocking progress is not reset
 @Test(expected = AssertionError.class)
 public void testResetMocksAfterStubbing() {
 Object mock = mock(Object.class);
 MockitoCore.stub(mock).thenReturn("test");
 MockitoCore.reset(mock);
 Mockito.verify(mock, Mockito.never()).thenReturn("test");
 }

 // Test for the edge case: verify more interactions than registered
 @Test(expected = AssertionError.class)
 public void testVerifyMoreInteractionsThanRegistered() {
 Object mock = mock(Object.class);
 MockitoCore.stub(mock).thenReturn("test1");
 MockitoCore.stub(mock).thenReturn("test2");
 MockitoCore.verify(mock, Mockito.times(3, "toString"));
 }

 // Test for the exception handling scenario: NotAMockException
 @Test(expected = NotAMockException.class)
 public void testNotAMockException() {
 Object notAMock = new Object();
 MockitoCore.verify(notAMock).toString();
 }

}