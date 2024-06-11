import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mock = Mockito.mock(Object.class);
 }

 @Test
 public void testMock() {
 Object returnedMock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Assert.assertNotNull(returnedMock);
 }

 @Test
 public void testStub() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn("testString");
 String returnedValue = (String) mockitoCore.when(mock).thenReturn("testString").getMock();
 Assert.assertEquals("testString", returnedValue);
 }

 @Test(expected = NullPointerException.class)
 public void testStubNullMock() {
 mockitoCore.stub().thenReturn("testString").getMock();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStubNotAMock() {
 mockitoCore.stub().thenReturn("testString").getMock();
 }

 @Test
 public void testVerify() {
 mockitoCore.verify(mock, VerificationMode.times(1));
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNullMock() {
 mockitoCore.verify(null, VerificationMode.times(1));
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNotAMock() {
 mockitoCore.verify(new Object(), VerificationMode.times(1));
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock);
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNoMoreInteractionsNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrderWithException() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 inOrder.verify(mock1).method1();
 }

 @Test
 public void testInOrder() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).method1();
 inOrder.verify(mock2).method1();
 }

 @Test(expected = NullPointerException.class)
 public void testInOrderNullMock() {
 InOrder inOrder = mockitoCore.inOrder();
 inOrder.verify(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInOrderNotAMock() {
 InOrder inOrder = mockitoCore.inOrder();
 inOrder.verify(new Object());
 }

 @Test
 public void testDoAnswer() {
 Answer<String> answer = ArgumentMatchers.argThat(new Answer<String>() {
 @Override public String answer(InvocationOnMock invocation) throws Throwable {
 return "testString";
 }
 });
 Stubber stubber = mockitoCore.doAnswer(answer);
 stubber.thenCallRealMethod();
 String returnedValue = (String) mockitoCore.when(mock).thenCallRealMethod().getMock();Assert.assertEquals("testString", returnedValue);
 }

 @Test
 public void testStubVoid() {
 mockitoCore.stubVoid(mock);
 mockitoCore.verify(mock).method1();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Object[] mocks = {mock};
 Invocation invocation = mockitoCore.getLastInvocation();
 Assert.assertNotNull(invocation);
 }

}