import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Object otherMock;
 private List<Object> mocks;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock = Mockito.mock(Object.class);
 otherMock = Mockito.mock(Object.class);
 mocks = Arrays.asList(mock, otherMock);
 }

 @Test
 public void testMockCreation() {
 T mockedType = (T) mockitoCore.mock(Object.class);
 // typical usage
 verify(mockedType).methodCall();

 // edge case: null argument
 verify(mockitoCore).nullPassedToMock(null);

 // error scenario: not a mock passed as argument
 verify(mockitoCore).notAMockPassedToMock(new Object());
 }

 @Test
 public void testStubbing() {
 mockitoCore.stub().thenReturn("return value");
 verify(mock).methodCall();
 verify(mock).methodCall().thenReturn("another return value");

 // edge case: null argument
 verify(mockitoCore).nullPassedToStubbing();

 // error scenario: not a mock passed as argument
 verify(mockitoCore).notAMockPassedToStubbing(new Object());
 }

 @Test
 public void testVerification() {
 mockitoCore.verify(mock).methodCall();
 verify(mock).methodCall().times(1);
 verify(mock).methodCall().never();

 // edge case: null argument
 verify(mockitoCore).nullPassedToVerify(null);

 // error scenario: not a mock passed as argument
 verify(mockitoCore).notAMockPassedToVerify(new Object());
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock, otherMock);
 verify(mock).methodCall();
 verify(otherMock).methodCall();
 verifyNoMoreInteractions(mock, otherMock);
 }

 @Test
 public void testInOrder() {
 Object firstMock = Mockito.mock(Object.class);
 Object secondMock = Mockito.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(firstMock, secondMock);
 firstMock.methodCall();
 secondMock.methodCall();
 firstMock.methodCall();
 inOrder.verify(firstMock).methodCall().before(secondMock.methodCall());
 }

 @Test
 public void testAnswer() {
 Answer<Object> answer = invocation -> "answer value";
 mockitoCore.doAnswer(answer).when(mock).methodCall();
 verify(mock).methodCall().then().returnValue("return value");

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 answer.answer(invocation);
 verify(mock).methodCall().thenThrows(new RuntimeException());
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 verifyNoMoreInteractions(mocks);
 }

 @Test
 public void testAssertMocksNotEmpty() {
 mockitoCore.assertMocksNotEmpty(new Object[0]);
 }
}