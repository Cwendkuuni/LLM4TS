import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.MockSettings;
import org.mockito.internal.MockitoInternalVerifications;
import org.mockito.internal.stubbing.AnswerWithThrows;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.VerificationModeImpl;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MockHandlerTest {

 private MockSettings mockSettings;
 private MockHandler<Object> mockHandler;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 }

 @Test
 public void testHandle_whenInvocationMatchesStubbing_thenReturnNull() {
 InvocationOnMock invocation = Mockito.mock(Object.class);
 InvocationMatcher invocationMatcher = Mockito.argThat(Mockito.isNull());
 mockHandler.invocationContainerImpl.setAnswerFor(invocationMatcher, new AnswerWithThrows() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException();
 }
 });

 Object result = mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testHandle_whenInvocationDoesNotMatchAnyStubbing_thenInvokeDefaultAnswer() {
 InvocationOnMock invocation = Mockito.mock(Object.class);
 Object result = mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test(expected = RuntimeException.class)
 public void testHandle_whenVerificationModeIsNotEmptyAndNotOnCorrectMock_thenThrowException() {
 VerificationMode verificationMode = MockitoInternalVerifications.verifyNoMoreInteractions(Mockito.mock(Object.class));
 mockHandler.handle(Mockito.mock(Object.class));
 }

 @Test
 public void testHandle_whenVerificationModeIsEmptyOrOnCorrectMock_thenVerifyTheInvocation() {
 Object mock = Mockito.mock(Object.class);
 VerificationMode verificationMode = new VerificationModeImpl(VerificationModeFactory.times(1));
 mockHandler.mockingProgress.pushVerificationMode(verificationMode);
 mockHandler.handle(Mockito.argThat(Mockito.isInstance(mock)));
 }

 @Test(expected = NullPointerException.class)
 public void testHandle_whenInvocationMatcherIsNull_thenThrowException() {
 mockHandler.invocationContainerImpl.setAnswerFor(null, new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 });
 mockHandler.handle(Mockito.mock(Object.class));
 }

 @Test
 public void testHandle_whenAnswerIsReturned_thenReturnTheAnswer() {
 InvocationOnMock invocation = Mockito.mock(Object.class);
 Answer answer = Mockito.answer(new Object() {});
 mockHandler.invocationContainerImpl.setAnswerFor(Mockito.argThat(Mockito.isNull()), answer);
 Object result = mockHandler.handle(invocation);
 assertSame(answer.getAnswer(), result);
 }

 @Test
 public void testHandle_whenVoidAnswerIsReturned_thenReturnNull() {
 mockHandler.voidMethodStubbable(Mockito.mock(Object.class)).stubVoid();
 Object mock = Mockito.mock(Object.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 mockHandler.handle(invocation);
 assertNull(mockHandler.handle(invocation));
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_whenStubbingIsAlreadySetForPreviousInvocation_thenThrowIllegalStateException() {
 InvocationOnMock invocation1 = Mockito.mock(Object.class);
 InvocationOnMock invocation2 = Mockito.mock(Object.class);
 mockHandler.handle(invocation1);
 mockHandler.invocationContainerImpl.setAnswerFor(Mockito.argThat(Mockito.isInstance(invocation1.getMock())), Mockito.answer(new Object() {}));
 mockHandler.handle(invocation2);
 }
}