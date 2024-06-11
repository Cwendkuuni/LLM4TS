import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.DeprecatedOngoingStubbing;
import org.mockito.internal.stubbing.OngoingStubbing;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.internal.util.MockUtil;
import org.mockito.mock.MockHandlerImpl;
import org.mockito.stubbing.Stub;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockUtil mockUtil;
 private Stubber stubber;
 private Object mock;
 private Answer answer;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mockUtil = mockitoCore.mockUtil;
 stubber = mockitoCore.stubber();
 mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 answer = Answer.returnValue("Hello, World!");
 }

 @Test
 public void testMock() {
 T mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 assertNotNull(mock);
 }

 @Test
 public void testStub() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 OngoingStubbing<String> stubbingString = (OngoingStubbing<String>) stubbing;
 stubbingString.thenReturn("Mockito");
 String result = (String) mockitoCore.when(mock).thenReturn("Mockito").getMock();
 assertEquals("Mockito", result);
 }

 @Test
 public void testVerify() {
 mockitoCore.verify(mock, ArgumentMatchers.eq("Hello, World!"));
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyException() {
 mockitoCore.verify(new Object());
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

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsException() {
 mockitoCore.verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 List<Object> mocks = Arrays.asList(mock, new Object());
 InOrder inOrder = mockitoCore.inOrder(mocks.toArray());
 mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrder);
 }

 @Test
 public void testInOrder() {
 InOrder inOrder = mockitoCore.inOrder(mock);
 }

 @Test
 public void testDoAnswer() {
 Stubber stubber = mockitoCore.doAnswer(answer);
 stubber.doAnswer(answer).when(mock).someMethod();
 String result = (String) mockitoCore.when(mock.someMethod()).thenReturn("Result").getMock();
 assertEquals("Hello, World!", result);
 }

 @Test
 public void testStubVoid() {
 VoidMethodStubbable<String> stubVoid = mockitoCore.stubVoid(mock);
 stubVoid.thenCallRealMethod();
 stubVoid.thenAnswer(answer);
 String result = (String) mockitoCore.when(mock.toString()).thenReturn("Mockito").getMock();
 assertEquals("Mockito", result);
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertNotNull(lastInvocation);
 }

 @Test
 public void testStubMethodWithArgument() {
 Callable<Integer> callable = () -> 1;
 stubber.thenCallRealMethod();
 stubber.thenAnswer(Answer.callRealMethod());
 stubber.thenAnswer(Answer.returnValue(2));
 Stub<Integer> stub = stubber.stub(callable);
 Integer result = stub.getMock().call();
 assertEquals(2, result.intValue());
 }

 @Test
 public void testStubMethodWithArgumentAndReturnType() {
 Callable<String> callable = () -> "test";
 stubber.thenCallRealMethod();
 stubber.thenAnswer(Answer.returnValue("Mockito"));
 Stub<String> stub = stubber.stub(callable);
 String result = stub.getMock().call();
 assertEquals("Mockito", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStubMethodWithArgumentAndWrongReturnType() {
 Callable<Integer> callable = () -> 1;
 stubber.thenCallRealMethod();
 stubber.thenAnswer(Answer.returnValue("Mockito"));
 Stub<String> stub = stubber.stub(callable);
 String result = stub.getMock().call();
 fail();
 }
}