import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyOrder;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Object mock2;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock = mock(Object.class);
 mock2 = mock(Object.class);
 }

 @Test
 public void testMockCreation() {
 Object returnedMock = mockitoCore.mock(Object.class, null);
 assert(returnedMock instanceof Object);
 }

 @Test(expected = NullPointerException.class)
 public void testMockCreationWithNullClass() {
 mockitoCore.mock(null, null);
 }

 @Test
 public void testStubbing() {
 OngoingStubbing stubbing = mockitoCore.stub();
 when(mock).thenReturn("test");
 String result = (String) mockitoCore.verify(mock);
 assert(result.equals("test"));
 }

 @Test
 public void testDeprecatedOngoingStubbing() {
 DeprecatedOngoingStubbing<Object> stubbing = mockitoCore.stub((Object) mock);
 when(mock).thenReturn("test");
 String result = (String) mockitoCore.verify(mock);
 assert(result.equals("test"));
 }

 @Test
 public void testOngoingStubbing() {
 OngoingStubbing<Object> stubbing = mockitoCore.when((Object) mock);
 stubbing.thenReturn("test");
 String result = (String) mockitoCore.verify(mock);
 assert(result.equals("test"));
 }

 @Test
 public void testVerify() {
 Object verifyResult = mockitoCore.verify(mock, Mockito.verifyNoMoreInteractions());
 assert(verifyResult == mock);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyWithNullMock() {
 mockitoCore.verify(null, Mockito.verifyNoMoreInteractions());
 }

 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testVerifyWithNonMockObject() {
 Object nonMockObject = new Object();
 mockitoCore.verify(nonMockObject, Mockito.verifyNoMoreInteractions());
 }

 @Test
 public void testReset() {
 Object resetMock = mockitoCore.mock(Object.class, null);
 mockitoCore.reset(resetMock);
 verifyNoMoreInteractions(resetMock);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithNullObjectArray() {
 mockitoCore.verifyNoMoreInteractions((Object[]) null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsWithEmptyObjectArray() {
 mockitoCore.verifyNoMoreInteractions(new Object[0]);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 List<Object> mocks = Arrays.asList(mock, mock2);
 InOrder inOrder = mockitoCore.inOrder(mocks.toArray(new Object[0]));

 when(mock.hashCode()).thenReturn(1);
 when(mock2.hashCode()).thenReturn(2);

 mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrder);

 inOrder.verify(mock).hashCode();
 inOrder.verify(mock2).hashCode();
 }

 @Test
 public void testAnswer() {
 Answer<String> answer = Mockito.answer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 });
 Stubber stubber = mockitoCore.doAnswer(answer);
 stubber.stub(mock);
 String result = (String) mockitoCore.verify(mock);
 assert(result.equals("answer"));
 }

 @Test
 public void testStubVoid() {
 VoidMethodStubbable<Object> stubbing = mockitoCore.stubVoid(mock);
 stubbing.doCall((Void) null);
 verify(mock).voidMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 OngoingStubbingImpl ongoingStubbing = (OngoingStubbingImpl) mockitoCore.when((Object) mock);
 ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
 when(mock.add(integerArgumentCaptor.capture(intValue))).thenReturn(1);
 int value = 2;
 mockitoCore.verify(mock).add(value);
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 InvocationOnMock invocationOnMock = (InvocationOnMock) lastInvocation;
 assert(intValue.getValue().intValue() == value);
 }

}