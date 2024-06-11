import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWithCallRealMethod;
import org.mockito.stubbing.OngoingStubbing;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoTestClass> mockHandler;
 private MockitoTestClass mockObject;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>();
 mockObject = mock(MockitoTestClass.class);
 }

 @Test
 public void testHandleInvocationWithStubbingVoid() throws Exception {
 Invocation invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = argument(0);
 doAnswer(answer(1)).when(mockObject).methodToStub(invocationMatcher.capture());

 ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
 invocation.setArguments(new Object[]{1});

 mockHandler.handle(invocation);

 verify(mockObject).methodToStub(argumentCaptor.capture());
 assertEquals(1, argumentCaptor.getValue());
 }

 @Test
 public void testHandleInvocationWithVerification() throws Exception {
 Invocation invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = argument(0);
 when(mockObject.methodToVerify(invocationMatcher.capture())).thenReturn(1);

 mockHandler.handle(invocation);

 verify(mockObject).methodToVerify(invocationMatcher.capture());
 assertEquals(1, invocationMatcher.getValue());

 verifyNoMoreInteractions(mockObject);
 }

 @Test
 public void testHandleInvocationWithNoInteractions() throws Exception {
 Invocation invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = argument(0);

 mockHandler.handle(invocation);

 verifyZeroInteractions(mockObject);
 }

 @Test(expected = RuntimeException.class)
 public void testHandleInvocationWithInvalidMethodForStubbing() throws Exception {
 Invocation invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = argument(0);
 doAnswer(answer(1)).when(mockObject).methodToStub2(invocationMatcher.capture()); // different method

 mockHandler.handle(invocation);
 }

 @Test
 public void testHandleInvocationWithDefaultAnswer() throws Exception {
 Invocation invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = argument(0);

 mockHandler.handle(invocation);

 verifyZeroInteractions(mockObject);
 }

 @Test
 public void testHandleInvocationWithOngoingStubbing() throws Exception {
 Invocation invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = argument(0);
 OngoingStubbing<MockitoTestClass> stubbing = stub(mockObject.methodToStub3(ArgumentMatchers.anyInt()));

 mockHandler.handle(invocation);

 verify(mockObject).methodToStub3(ArgumentMatchers.anyInt());

 Answer answer = answer(2);
 stubbing.thenAnswer(answer);

 invocation.setArguments(new Object[]{3});

 mockHandler.handle(invocation);

 verify(mockObject).methodToStub3(ArgumentMatchers.eq(3));
 assertEquals(2, answer.getValue());
 }

 @Test
 public void testHandleInvocationWithAnswerWithCallRealMethod() throws Exception {
 Invocation invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = argument(0);
 AnswerWithCallRealMethod answer = answerWith(new AnswerWithCallRealMethod() {
 @Override public Object answer(InvocationOnMock invocation) throws Throwable {
 return invocation.callRealMethod();
 }
 });

 when(mockObject.methodToStub4(ArgumentMatchers.anyInt())).thenAnswer(answer);

 mockHandler.handle(invocation);

 assertEquals(invocation.callRealMethod(), mockObject.methodToStub4(ArgumentMatchers.eq(1)));
 }

 @Test
 public void testHandleInvocationWithInvocationContainerReset() throws Exception {
 Invocation invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = argument(0);

 doAnswer(answer(1)).when(mockObject).methodToStub(invocationMatcher.capture());
 doAnswer(answer(2)).when(mockObject).methodToStub2(invocationMatcher.capture());
invocation.setArguments(new Object[]{1});

 mockHandler.handle(invocation);
 assertEquals(1, invocationMatcher.getValue());
 assertEquals(1, argumentCaptor.getValue());

 invocation.setArguments(new Object[]{2});
 mockHandler.handle(invocation);
 assertEquals(2, invocationMatcher.getValue());
 assertEquals(2, argumentCaptor.getValue());

 reset(mockObject);
 mockHandler.handle(invocation);
 assertEquals(1, invocationMatcher.getValue());
 assertEquals(1, argumentCaptor.getValue());
 }

 @Test
 public void testHandleInvocationWithStubbingVoidWithVerification() throws Exception {
 Invocation invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = argument(0);
 doAnswer(answer(1)).when(mockObject).methodToStub(invocationMatcher.capture());

 ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
 invocation.setArguments(new Object[]{1});

 mockHandler.handle(invocation);

 verify(mockObject).methodToStub(argumentCaptor.capture());
 assertEquals(1, argumentCaptor.getValue());

 verifyNoMoreInteractions(mockObject);
 }
}

class MockitoTestClass {
 int methodToStub(int arg);
 int methodToVerify(int arg);
 int methodToStub2(int arg);
 int methodToStub3(int arg);
 int methodToStub4(int arg) throws Exception {
 throw new Exception("Expected exception in methodToStub4");
 }
}

class MockitoTestAnswer implements Answer {
 Object answer(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
}