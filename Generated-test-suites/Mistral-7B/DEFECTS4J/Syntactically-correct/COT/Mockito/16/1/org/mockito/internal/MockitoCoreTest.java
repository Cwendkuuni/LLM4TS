import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.invoker.MethodStubbable;
import org.mockito.internal.verification.api.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class MockitoCoreTest {

 private MockitoCore mockitoCore;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMock() {
 Class<String> stringClass = String.class;
 String mock = mockitoCore.mock(stringClass, new MockSettingsImpl());
 assert(mock != null);
 }

 @Test
 public void testMockWithSettings() {
 Class<String> stringClass = String.class;
 MockSettings mockSettings = new MockSettingsImpl();
 String mock = mockitoCore.mock(stringClass, mockSettings);
 assert(mock != null);
 }

 @Test
 public void testStub() {
 Object mock = mock(Object.class);
 IOngoingStubbing stubbing = mockitoCore.stub();
 when(mock.toString()).thenReturn("test");
 assert(stubbing != null);
 assert(mock.toString().equals("test"));
 }

 @Test
 public void testStubDeprecated() {
 Object mock = mock(Object.class);
 DeprecatedOngoingStubbing<Object> stubbing = mockitoCore.stub(mock);
 when(mock.toString()).thenReturn("test");
 assert(stubbing != null);
 assert(mock.toString().equals("test"));
 }

 @Test
 public void testWhen() {
 Object mock = mock(Object.class);
 when(mock.toString()).thenReturn("test");
 assert(mock.toString().equals("test"));
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNullMock() {
 mockitoCore.verify(null, VerificationMode.RETURN);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verify(notAMock, VerificationMode.RETURN);
 }

 @Test
 public void testReset() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mockitoCore.reset(mock1, mock2);
 verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testVerifyNoMoreInteractionsNull() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerifyNoMoreInteractionsEmptyArray() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test
 public void testVerifyNoMoreInteractionsWithMocks() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsWithNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testInOrder() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 }

 @Test(expected = AssertionError.class)
 public void testInOrderNull() {
 mockitoCore.inOrder(null);
 }

 @Test(expected = AssertionError.class)
 public void testInOrderNotAMock() {
 Object notAMock = new Object();
 mockitoCore.inOrder(notAMock);
 }

 @Test(expected = AssertionError.class)
 public void testInOrderEmptyArray() {
 mockitoCore.inOrder();
 }

 @Test
 public void testAnswer() {
 Object mock = mock(Object.class);
 Answer<Object> answer = invocation -> "test";
 mockitoCore.doAnswer(answer).when(mock).toString();
 assert(mock.toString().equals("test"));
 }

 @Test
 public void testVoidMethodStubbable() {
 Object mock = mock(Object.class);
 MethodStubbable<Object> methodStubbable = mockitoCore.stubVoid(mock);
 methodStubbable.when(mock).voidMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = mock(Object.class);
 when(mock.toString()).thenReturn("test");
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.then(invocation -> mockitoCore.getLastInvocation());
 when(mock.toString()).thenReturn("test2");
 Object invocation = mockitoCore.getLastInvocation();
 assert(invocation != null);
 assert(invocation.getMethod().getName().equals("toString"));
 assert(invocation.getArguments().length == 0);
 assert(invocation.getTarget().equals(mock));
 }

}