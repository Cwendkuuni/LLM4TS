import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.InOrder;
import org.mockito.mock.MockHandlerInterface;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.ArrayList;

public class MockitoCoreTest {

 private MockHandlerInterface<Object> mockHandler;
 private InOrder inOrder;
 private Object mock;
 private List<Invocation> invocations;

 @Before
 public void setUp() {
 mock = Mockito.mock(Object.class);
 mockHandler = Mockito.mock(MockHandlerInterface.class);
 invocations = new ArrayList<>();
 Mockito.doReturn(invocations).when(mockHandler).getInvocationContainer();
 }

 @Test
 public void testMock() {
 MockitoCore mockitoCore = new MockitoCore();
 Object mockObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Assert.assertNotNull(mockObject);
 Mockito.verifyNoMoreInteractions(mockObject);
 }

 @Test(expected = NullPointerException.class)
 public void testMockWithNullClass() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.mock(null, new MockSettingsImpl());
 }

 @Test
 public void testStub() {
 MockitoCore mockitoCore = new MockitoCore();
 IOngoingStubbing stubbing = mockitoCore.stub();
 Assert.assertNotNull(stubbing);
 }

 @Test(expected = IllegalStateException.class)
 public void testStubWhenNoOngoingStubbing() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.stub();
 mockitoCore.stub();
 }

 @Test
 public void testWhen() {
 MockitoCore mockitoCore = new MockitoCore();
 IOngoingStubbing stubbing = mockitoCore.when(mock);
 Assert.assertNotNull(stubbing);
 }

 @Test(expected = IllegalStateException.class)
 public void testWhenWhenNoOngoingStubbing() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.when(mock);
 mockitoCore.when(mock);
 }

 @Test
 public void testVerify() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.verify(mock, Mockito.eq(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyWithNullMock() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.verify(null, Mockito.any(VerificationMode.class));
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithNonMock() {
 MockitoCore mockitoCore = new MockitoCore();
 Object nonMock = new Object();
 mockitoCore.verify(nonMock, Mockito.eq(null));
 }

 @Test
 public void testReset() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.reset(mock);
 Mockito.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNoMoreInteractionsWithNullMock() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 MockitoCore mockitoCore = new MockitoCore();
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNoMoreInteractionsInOrderWithNullMocks() {
 MockitoCore mockitoCore = new MockitoCore();
 InOrder inOrder = mockitoCore.inOrder();
 mockitoCore.verifyNoMoreInteractionsInOrder(null, inOrder);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsInOrderWithEmptyMocks() {
 MockitoCore mockitoCore = new MockitoCore();
 InOrder inOrder = mockitoCore.inOrder();
 mockitoCore.verifyNoMoreInteractionsInOrder(new Object[0], inOrder);
 }

 @Test
 public void testInOrder() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 MockitoCore mockitoCore = new MockitoCore();
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 Assert.assertNotNull(inOrder);
 }

 @Test(expected = NullPointerException.class)
 public void testInOrderWithNullMocks() {
 MockitoCore mockitoCore = new MockitoCore();
 InOrder inOrder = mockitoCore.inOrder(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInOrderWithEmptyMocks() {
 MockitoCore mockitoCore = new MockitoCore();
 InOrder inOrder = mockitoCore.inOrder(new Object[0]);
 }

 @Test
 public void testDoAnswer() {
 Answer answer = Mockito.answer(Mockito.mock(Answer.class));
 MockitoCore mockitoCore = new MockitoCore();
 Stubber stubber = mockitoCore.doAnswer(answer);
 Assert.assertNotNull(stubber);
 }

 @Test
 public void testStubVoid() {
 Object mock = Mockito.mock(Object.class);
 MockitoCore mockitoCore = new MockitoCore();
 VoidMethodStubbable<Object> stubbable = mockitoCore.stubVoid(mock);
 Assert.assertNotNull(stubbable);
 }

 @Test
 public void testValidateMockitoUsage() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastInvocationWhenNoOngoingStubbing() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.getLastInvocation();
 }

 @Test
 public void testGetLastInvocation() {
 MockitoCore mockitoCore = new MockitoCore();
 IOngoingStubbing stubbing = mockitoCore.stub();
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 Assert.assertNotNull(lastInvocation);
 }

}