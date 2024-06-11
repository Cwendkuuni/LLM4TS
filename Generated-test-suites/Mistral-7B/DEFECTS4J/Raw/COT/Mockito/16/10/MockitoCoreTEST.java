import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.mock.MockHandlerInterface;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.Stubber;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockHandlerInterface mockHandler;
 private Stubber stubber;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mockHandler = Mockito.mock(MockHandlerInterface.class);
 stubber = mockitoCore.doAnswer(answers("return null;"))
 .when(mockHandler).voidMethod();
 }

 @Test
 public void testMock() {
 Class<Object> clazz = Object.class;
 Object mock = mockitoCore.mock(clazz, new MockSettingsImpl(), false);
 assertNotNull(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testMockNullClass() {
 Class<?> nullClass = null;
 mockitoCore.mock(nullClass, new MockSettingsImpl(), false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMockInvalidClass() {
 Class<String> stringClass = String.class;
 mockitoCore.mock(stringClass, new MockSettingsImpl(), false);
 }

 @Test
 public void testStub() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 assertNotNull(stubbing);
 }

 @Test(expected = IllegalStateException.class)
 public void testStubNullMock() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn(null);
 mockitoCore.verify(null);
 }

 @Test
 public void testWhen() {
 Object mock = mockitoCore.mock(Object.class);
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
 ongoingStubbing.thenReturn("test");
 String result = (String) mockitoCore.verify(mock).someMethod();
 assertEquals("test", result);
 }

 @Test(expected = IllegalStateException.class)
 public void testWhenNullMock() {
 Object mock = mockitoCore.mock(Object.class);
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(null);
 ongoingStubbing.thenReturn("test");
 }

 @Test
 public void testVerify() {
 Object mock = mockitoCore.mock(Object.class);
 mockitoCore.verify(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNullMock() {
 Object mock = mockitoCore.mock(Object.class);
 mockitoCore.verify(null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNotAMock() {
 Object fake = new Object();
 mockitoCore.verify(fake);
 }

 @Test
 public void testReset() {
 Object mock = mockitoCore.mock(Object.class);
 mockitoCore.verify(mock).someMethod();
 mockitoCore.reset(mock);
 mockitoCore.verify(mock).someMethod();
 assertTrue(mockitoCore.getLastInvocation() == null);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock1 = mockitoCore.mock(Object.class);
 Object mock2 = mockitoCore.mock(Object.class);
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNoMoreInteractionsNullMock() {
 Object mock = mockitoCore.mock(Object.class);
 mockitoCore.verifyNoMoreInteractions(mock);
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 Object mock = new Object();
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testInOrder() {
 Object mock1 = mockitoCore.mock(Object.class);
 Object mock2 = mockitoCore.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 }

 @Test(expected = NullPointerException.class)
 public void testInOrderNullMock() {
 Object mock = mockitoCore.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock);
 inOrder.verify(mock).someMethod();
 inOrder.verify(null).someMethod();
 }

 @Test(expected = NotAMockException.class)
 public void testInOrderNotAMock() {
 Object mock = new Object();
 InOrder inOrder = mockitoCore.inOrder(mock);
 inOrder.verify(mock).someMethod();
 inOrder.verify(new Object()).someMethod();
 }

 @Test
 public void testAnswer() {
 Object mock = mockitoCore.mock(Object.class);
 Answer<Integer> answer = answers("1");
 Stubber stubber = mockitoCore.doAnswer(answer).when(mock).someMethod();
 Integer result = (Integer) mockitoCore.verify(mock).someMethod();
 assertEquals(1, result.intValue());
 }

 @Test
 public void testStubVoid() {
 Object mock = mockitoCore.mock(Object.class);
 mockitoCore.stubVoid(mock).someMethod();
 mockitoCore.verify(mock).someMethod();
 }

 @Test(expected = NullPointerException.class)
 public void testStubVoidNullMock() {
 Object mock = mockitoCore.mock(Object.class);
 mockitoCore.stub(mock).someMethod();
 mockitoCore.stubVoid(null);
 }

 @Test(expected = NotAMockException.class)
 public void testStubVoidNotAMock() {
 Object mock = new Object();
 mockitoCore.stubVoid(mock);
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = mockitoCore.mock(Object.class);
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock).someMethod();
 ongoingStubbing.thenReturn("test");
 String result = (String) mockitoCore.verify(mock).someMethod();
 assertEquals("test", result);
 assertEquals("test", mockitoCore.getLastInvocation().getMethod().getName());
 }
}