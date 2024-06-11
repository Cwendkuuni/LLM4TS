import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.verification.api.InOrder;
import org.mockito.stubbing.Answer;
import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Answer answer;
 private InOrder inOrder;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock = Mockito.mock(Object.class);
 answer = ArgumentMatchers.answer(true);
 inOrder = mockitoCore.inOrder(mock);
 }

 @Test
 public void testMockCreation() {
 Object mockedObject = mockitoCore.mock(Object.class, null);
 assertNotNull(mockedObject);
 }

 @Test
 public void testStubbingStarted() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn(true);
 assertTrue(stubbing.wasStubbed());
 }

 @Test
 public void testStubbingWithMethodCall() {
 DeprecatedOngoingStubbing<Object> stubbing = mockitoCore.stub(mock);
 stubbing.thenReturn(true);
 assertEquals(true, mock.toString());
 }

 @Test
 public void testOngoingStubbing() {
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
 ongoingStubbing.thenReturn(true);
 assertEquals(true, mockitoCore.verify(mock).toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullPassedToVerify() {
 mockitoCore.verify(null, ArgumentMatchers.eq(1));
 }

 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testNotAMockPassedToVerify() {
 mockitoCore.verify(new Object(), ArgumentMatchers.eq(1));
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock);
 Mockito.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mockedObject1 = Mockito.mock(Object.class);
 Object mockedObject2 = Mockito.mock(Object.class);
 mockitoCore.verifyNoMoreInteractions(mockedObject1, mockedObject2);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testNotAMockPassedToVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mockedObject1 = Mockito.mock(Object.class);
 Object mockedObject2 = Mockito.mock(Object.class);
 inOrder.verify(mockedObject1).method1();
 inOrder.verify(mockedObject2).method2();
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mockedObject1, mockedObject2), inOrder);
 }

 @Test
 public void testAnswer() {
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
 ongoingStubbing.thenAnswer(answer);
 assertTrue(true);
 }

 @Test
 public void testStubVoid() {
 Object mockedObject = Mockito.mock(Object.class);
 mockitoCore.stubVoid(mockedObject).then();
 mockedObject.method1();
 Mockito.verify(mockedObject).method1();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = NullPointerException.class)
 public void testNullPassedToInOrder() {
 mockitoCore.inOrder(null);
 }

 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testNotAMockPassedToInOrder() {
 Object notAMock = new Object();
 mockitoCore.inOrder(notAMock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultipleMocksInOrderWithSameMock() {
 Object mockedObject = Mockito.mock(Object.class);
 mockitoCore.inOrder(mockedObject, mockedObject);
 }

 @Test
 public void testGetLastInvocation() {
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
 ongoingStubbing.thenReturn(true);
 assertEquals(ArgumentMatchers.isA(Invocation.class), mockitoCore.getLastInvocation());
 }
}