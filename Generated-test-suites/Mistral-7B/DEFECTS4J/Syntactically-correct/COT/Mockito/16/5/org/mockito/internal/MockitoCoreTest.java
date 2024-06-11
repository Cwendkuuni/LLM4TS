import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Object mock2;
 private Object mock3;
 private InOrder inOrder;
 private List<Invocation> invocations;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock = mock(Object.class);
 mock2 = mock(Object.class);
 mock3 = mock(Object.class);
 inOrder = inOrder(mock, mock2, mock3);
 }

 @Test
 public void testMock() {
 Object returnedMock = mockitoCore.mock(Object.class);
 assertNotNull(returnedMock);
 }

 @Test
 public void testMockWithSettings() {
 Object returnedMock = mockitoCore.mock(Object.class, new MockSettings().withName("MockObject"));
 assertNotNull(returnedMock);
 assertEquals("MockObject", mockitoCore.getLastInvocation().getArgument(0));
 }

 @Test
 public void testStub() {
 OngoingStubbing<Object> stub = mockitoCore.stub();
 stub.thenReturn("Return Value");
 assertEquals("Return Value", mockitoCore.verify(mock).someMethod());
 }

 @Test
 public void testDeprecatedStub() {
 mockitoCore.stub().thenReturn("Return Value");
 assertEquals("Return Value", mockitoCore.verify(mock).someMethod());
 }

 @Test
 public void testWhen() {
 OngoingStubbing<Object> whenStub = mockitoCore.when(mock);
 whenStub.thenReturn("Return Value");
 assertEquals("Return Value", mockitoCore.verify(mock).someMethod());
 }

 @Test
 public void testVerify() {
 verify(mock).someMethod();
 }

 @Test(expected = AssertionError.class)
 public void testNullPassedToVerify() {
 mockitoCore.verify(null);
 }

 @Test(expected = AssertionError.class)
 public void testNotAMockPassedToVerify() {
 mockitoCore.verify(new Object());
 }

 @Test
 public void testReset() {
 reset(mock, mock2, mock3);
 verify(mock).someMethod();
 verifyNoMoreInteractions(mock2, mock3);
 }

 @Test(expected = AssertionError.class)
 public void testNullPassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = NotAMockException.class)
 public void testNotAMockPassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testVerifyNoInteractions() {
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testInOrder() {
 mockitoCore.verify(mock).someMethod();
 mockitoCore.verify(mock2).someOtherMethod();
 inOrder.verify(mock).someMethod();
 inOrder.verify(mock2).someOtherMethod();
 }

 @Test
 public void testAnswer() {
 OngoingStubbing<Object> whenStub = mockitoCore.when(mock);
 whenStub.thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "Return Value";
 }
 });
 assertEquals("Return Value", mockitoCore.verify(mock).someMethod());
 }

 @Test
 public void testStubVoid() {
 mockitoCore.stubVoid(mock).someMethod();
 verify(mock).someMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertNotNull(lastInvocation);
 assertEquals(mock, lastInvocation.getMock());
 }
}