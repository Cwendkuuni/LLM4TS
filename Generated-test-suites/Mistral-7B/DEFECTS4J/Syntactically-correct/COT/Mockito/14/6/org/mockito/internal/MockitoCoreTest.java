import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractionsInOrder;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock1;
 private Object mock2;
 private Object mock3;
 private InOrder inOrder;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock1 = mock(Object.class);
 mock2 = mock(Object.class);
 mock3 = mock(Object.class);
 inOrder = inOrder(mock1, mock2, mock3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMockNullClass() {
 mockitoCore.mock(null, new MockSettingsImpl());
 }

 @Test
 public void testMock() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mock).toString();
 }

 @Test
 public void testStub() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn("Hello World");
 String returnedValue = (String) mockitoCore.when(mock1).toString();
 verify(mock1).toString();
 verify(mock1).thenReturn("Hello World");
 assertEquals("Hello World", returnedValue);
 }

 @Test
 public void testStubDeprecated() {
 DeprecatedOngoingStubbing<Object> stubbing = mockitoCore.stub(mock1);
 stubbing.thenReturn("Hello World");
 String returnedValue = (String) mockitoCore.when(mock1).toString();
 verify(mock1).toString();
 verify(mock1).thenReturn("Hello World");
 assertEquals("Hello World", returnedValue);
 }

 @Test
 public void testWhen() {
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock1);
 ongoingStubbing.thenReturn("Hello World");
 String returnedValue = (String) mockitoCore.when(mock1).toString();
 verify(mock1).toString();
 verify(mock1).thenReturn("Hello World");
 assertEquals("Hello World", returnedValue);
 }

 @Test
 public void testVerify() {
 Object mock = mock(Object.class);
 verify(mock).toString();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNullMock() {
 mockitoCore.verify(null, VerificationMode.times(1));
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verify(notAMock, VerificationMode.times(1));
 }

 @Test
 public void testVerifyNoInteractions() {
 mockitoCore.verifyNoMoreInteractions(mock1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoInteractionsNull() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerifyNoInteractionsWithOrder() {
 List<Object> mocks = Arrays.asList(mock1, mock2, mock3);
 verifyNoMoreInteractionsInOrder(mocks, inOrder);
 }

 @Test
 public void testReset() {
 Object mock = mock(Object.class);
 mockitoCore.reset(mock);
 verify(mock, never()).toString();
 }

 @Test
 public void testVerifyInteractions() {
 Object mock = mock(Object.class);
 when(mock.toString()).thenReturn("Hello World");
 mockitoCore.verify(mock).toString();
 }

 @Test
 public void testAnswer() {
 Object mock = mock(Object.class);
 Answer<String> answer = new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Answer";
 }
 };
 Stubber stubber = mockitoCore.doAnswer(answer);
 stubber.thenReturn(mock);
 String returnedValue = (String) mockitoCore.when(mock).toString();
 assertEquals("Answer", returnedValue);
 }

 @Test
 public void testStubVoid() {
 Object mock = mock(Object.class);
 mockitoCore.stubVoid(mock).toString();
 mockitoCore.verify(mock).toString();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 OngoingStubbingImpl ongoingStubbing = (OngoingStubbingImpl) mockitoCore.mockingProgress.pullOngoingStubbing();
 when(mock1.toString()).thenReturn("MockitoCoreTest");
 Invocation invocation = mockitoCore.getLastInvocation();
 assertEquals(ArgumentMatchers.isA(InvocationOnMock.class), invocation.getClass());
 assertEquals("toString", invocation.getMethod().getName());
 assertEquals(mock1, invocation.getMock());
 }

}