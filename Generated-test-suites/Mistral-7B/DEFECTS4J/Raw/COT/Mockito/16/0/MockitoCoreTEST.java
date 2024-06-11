import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.MockUtil;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.mock.MockHandler;
import org.mockito.stubbing.Answer;
import org.mockito.internal.verification.api.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockingProgress mockingProgress;
 private MockHandler<Object> mockHandler;
 private Object mock;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mockingProgress = mockitoCore.mockingProgress;
 mockHandler = mock(MockHandler.class);
 }

 @Test
 public void testMock() {
 mock = mockitoCore.mock(Object.class, null, false);
 assertNotNull(mock);
 }

 @Test(expected = IllegalStateException.class)
 public void testMock_invalidState() {
 mockitoCore.mock(Object.class, null, false);
 mockitoCore.mock(Object.class, null, false);
 mockitoCore.mock(Object.class, null, false);
 mockitoCore.mock(Object.class, null, false);
 }

 @Test
 public void testStub() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 assertNotNull(stubbing);
 }

 @Test(expected = IllegalStateException.class)
 public void testStub_invalidState() {
 mockitoCore.stub();
 mockitoCore.stub();
 }

 @Test
 public void testWhen() {
 Object arg = new Object();
 when(mock).thenReturn(arg);
 assertEquals(arg, mockitoCore.verify(mock));
 }

 @Test
 public void testVerify() {
 Object arg = new Object();
 mock = mockitoCore.mock(Object.class, null, false);
 verify(mock).toString();
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNullMock() {
 mockitoCore.verify(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNotAMock() {
 mockitoCore.verify(new Object());
 }

 @Test
 public void testReset() {
 Object arg = new Object();
 mock = mockitoCore.mock(Object.class, null, false);
 when(mock).thenReturn(arg);
 mockitoCore.reset(mock);
 verify(mock).toString();
 assertNull(mockitoCore.getLastInvocation());
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object arg = new Object();
 mock = mockitoCore.mock(Object.class, null, false);
 when(mock).thenReturn(arg);
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNoMoreInteractionsNull() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 mockitoCore.verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testInOrder() {
 Object arg1 = new Object();
 Object arg2 = new Object();
 mock = mockitoCore.mock(Object.class, null, false);
 mockitoCore.when(mock).thenReturn(arg1).thenReturn(arg2);
 InOrder inOrder = mockitoCore.inOrder(mock);
 inOrder.verify(mock).toString();
 inOrder.verify(mock).toString();
 }

 @Test(expected = NullPointerException.class)
 public void testInOrderNull() {
 mockitoCore.inOrder(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInOrderNotAMock() {
 mockitoCore.inOrder(new Object());
 }

 @Test
 public void testDoAnswer() {
 Answer answer = invocation -> invocation.getArgument(0);
 Stubber stubber = mockitoCore.doAnswer(answer);
 stubber.doReturn(new Object());
 Object arg = new Object();mock = mockitoCore.mock(Object.class, null, false);when(mock).thenAnswer(answer);assertEquals(arg, mockitoCore.verify(mock)); }

 @Test
 public void testStubVoid() {
 Object mock = mockitoCore.mock(Object.class, null, false);
 mockitoCore.stubVoid(mock);
 mockitoCore.verify(mock).toString();
 }

 @Test(expected = IllegalStateException.class)
 public void testStubVoid_invalidState() {
 mockitoCore.stubVoid(mockitoCore.mock(Object.class, null, false));
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Object arg = new Object();
 IOngoingStubbing stubbing = mockitoCore.stub();
 when(stubbing.getRegisteredInvocations().get(0)).thenReturn(ArgumentMatchers.argThat(i -> i.getMethod().getName().equals("toString")));
 when(mock).thenAnswer(i -> i.getArguments()[0]);
 mockitoCore.stub().thenReturn(stubbing);
 assertEquals(arg, mockitoCore.getLastInvocation().getArguments()[0]);
 }

}