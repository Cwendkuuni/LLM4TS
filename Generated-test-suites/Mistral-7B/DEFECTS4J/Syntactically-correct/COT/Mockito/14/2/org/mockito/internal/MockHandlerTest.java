import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stub;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoInvocationHandler> mockHandler;
 private MockSettingsImpl mockSettings;
 private InvocationContainer invocationContainer;
 private Invocation invocation;
 private Stub<MockitoInvocationHandler> mockitoInvocationHandlerStub;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 invocationContainer = mockHandler.getInvocationContainer();
 mockitoInvocationHandlerStub = Mockito.stub(MockitoInvocationHandler.class);
 }

 @Test
 public void testHandle_StubbingVoidMethods() throws Throwable {
 InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
 when(mockitoInvocationHandlerStub.handleCall(ArgumentMatchers.any(InvocationOnMock.class)))
 .thenCallRealMethod();

 Answer answer = Mockito.answer(new Object() {});
 when(mockHandler.handle(ArgumentMatchers.eq(invocationOnMock)))
 .thenReturn(answer.getAnswer());

 mockitoInvocationHandlerStub.handleCall(invocationOnMock);
 verify(invocationContainer).setAnswerFor(ArgumentMatchers.any(InvocationMatcher.class));
 }

 @Test(expected = Throwable.class)
 public void testHandle_VerificationModeNotEmpty() throws Throwable {
 VerificationMode verificationMode = Mockito.verifyNoMoreInteractions(mockitoInvocationHandlerStub);
 when(mockHandler.handle(ArgumentMatchers.any(Invocation.class)))
 .thenThrow(new Exception("Test Exception"));
 mockHandler.handle(invocation);
 verificationMode.verify(ArgumentMatchers.any(VerificationDataImpl.class));
 }

 @Test
 public void testHandle_NoVerificationModeAndNoAnswer() throws Throwable {
 when(mockHandler.handle(ArgumentMatchers.any(Invocation.class)))
 .thenCallRealMethod();
 Object result = mockHandler.handle(invocation);
 verifyNoInteractions(invocationContainer);
 assertNull(result);
 }

 @Test
 public void testHandle_AnswerPresent() throws Throwable {
 Answer answer = Mockito.answer(new Object() {});
 when(mockHandler.handle(ArgumentMatchers.any(Invocation.class)))
 .thenReturn(answer.getAnswer());
 Object result = mockHandler.handle(invocation);
 verify(invocationContainer).setAnswerFor(ArgumentMatchers.any(InvocationMatcher.class));
 assertEquals(answer.getAnswer(), result);
 }

 @Test
 public void testHandle_AnswerWithArgumentsCapture() throws Throwable {
 InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
 when(mockitoInvocationHandlerStub.handleCall(ArgumentMatchers.any(InvocationOnMock.class)))
 .thenCallRealMethod();
 Answer answer = Mockito.answer(new Object() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 Object[] capturedArgs = invocation.getArgs();
 return capturedArgs[0];
 }
 });
 when(mockHandler.handle(ArgumentMatchers.any(Invocation.class)))
 .thenReturn(answer.getAnswer());
 Object result = mockHandler.handle(invocation);
 verify(invocationContainer).setAnswerFor(ArgumentMatchers.any(InvocationMatcher.class));
 assertNotNull(result);
 assertEquals("expectedArg", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHandle_NullInvocation() throws Throwable {
 mockHandler.handle(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHandle_NullInvocationMatcher() throws Throwable {
 mockHandler.handle(invocation, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_MultipleOngoingStubbings() throws Throwable {
 OngoingStubbingImpl<Object> ongoingStubbing1 = new OngoingStubbingImpl<>();
 OngoingStubbingImpl<Object> ongoingStubbing2 = new OngoingStubbingImpl<>();
 mockingProgress.reportOngoingStubbing(ongoingStubbing1);
 mockingProgress.reportOngoingStubbing(ongoingStubbing2);
 when(mockHandler.handle(ArgumentMatchers.any(Invocation.class)))
 .thenCallRealMethod();
 mockHandler.handle(invocation);
 }
}