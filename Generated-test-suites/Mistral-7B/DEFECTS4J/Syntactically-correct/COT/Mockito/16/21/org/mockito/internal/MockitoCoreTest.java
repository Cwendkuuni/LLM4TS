import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.mock.MockHandlerInterface;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockitoAnnotations mockitoAnnotations;
 private MockHandlerInterface mockHandler;
 private Answer answer;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mockitoAnnotations = MockitoAnnotations.initMocks(this);
 mockHandler = mockitoCore.mockUtil.getMockHandler(Mockito.mock(Object.class));
 answer = arg -> arg.hashCode();
 }

 @Test
 public void testMockMethod() {
 Object mock = mockitoCore.mock(Object.class);
 assertEquals(mock, mockitoCore.verify(mock, VerificationMode.RETURN));
 }

 @Test(expected = AssertionError.class)
 public void testNullMockPassedToVerify() {
 mockitoCore.verify(null, VerificationMode.RETURN);
 }

 @Test(expected = AssertionError.class)
 public void testNotAMockPassedToVerify() {
 Object notAMock = new Object();
 mockitoCore.verify(notAMock, VerificationMode.RETURN);
 }

 @Test
 public void testReset() {
 Object mock1 = mockitoCore.mock(Object.class);
 Object mock2 = mockitoCore.mock(Object.class);
 mockitoCore.reset(mock1, mock2);
 verify(mock1, never()).methodCall();
 verify(mock2, never()).methodCall();
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = mockitoCore.mock(Object.class);
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = AssertionError.class)
 public void testNullPassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testNotAMockPassedToVerifyNoMoreInteractions() {
 Object notAMock = new Object();
 mockitoCore.verifyNoMoreInteractions(notAMock);
 }

 @Test(expected = AssertionError.class)
 public void testMocksHaveToBePassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test
 public void testInOrder() {
 Object mock1 = mockitoCore.mock(Object.class);
 Object mock2 = mockitoCore.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).methodCall();
 inOrder.verify(mock2).methodCall();
 }

 @Test(expected = AssertionError.class)
 public void testNullPassedWhenCreatingInOrder() {
 mockitoCore.inOrder(null);
 }

 @Test(expected = AssertionError.class)
 public void testNotAMockPassedWhenCreatingInOrder() {
 Object notAMock = new Object();
 mockitoCore.inOrder(notAMock);
 }

 @Test
 public void testStub() {
 Object mock = mockitoCore.mock(Object.class);
 when(mock.methodCall()).thenReturn("stubbed result");
 assertEquals("stubbed result", mock.methodCall());
 }

 @Test(expected = AssertionError.class)
 public void testMissingMethodInvocation() {
 mockitoCore.stub();
 }

 @Test
 public void testWhen() {
 Object mock = mockitoCore.mock(Object.class);
 when(mock.methodCall()).thenReturn("stubbed result");
 assertEquals("stubbed result", mock.methodCall());
 }

 @Test(expected = AssertionError.class)
 public void testStubbingStartedTwice() {
 mockitoCore.when(mockitoCore.mock(Object.class).methodCall()).thenReturn("stubbed result");
 mockitoCore.when(mockitoCore.mock(Object.class).methodCall()).thenReturn("stubbed result 2");
 }

 @Test
 public void testDoAnswer() {
 Object mock = mockitoCore.mock(Object.class);
 when(mock.methodCall()).thenAnswer(answer);
 assertEquals(mock.hashCode(), answer.getAnswer(null));
 }

 @Test
 public void testVoidMethodStubbable() {
 Object mock = mockitoCore.mock(Object.class);
 mockitoCore.stubVoid(mock).methodCall();
 verify(mock).methodCall();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = mockitoCore.mock(Object.class);
 when(mock.methodCall()).thenReturn("stubbed result");
 Invocation invocation = mockitoCore.getLastInvocation();
 assertEquals("stubbed result", invocation.getArgument(0));
 }
}