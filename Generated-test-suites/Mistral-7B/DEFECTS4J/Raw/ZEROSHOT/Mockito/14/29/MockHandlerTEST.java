import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class MockHandlerTest {

 private MockHandler<MockitoInvocationHandler> mockHandler;
 private MockSettingsImpl mockSettings;
 private InvocationContainer invocationContainer;
 private List<Answer> answers;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 invocationContainer = mockHandler.getInvocationContainer();
 answers = mock(List.class);
 }

 @Test
 public void testHandleInvocation_noStubbingOrVerification() {
 Object mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = mock(Invocation.class);

 when(mockHandler.handle(invocation)).thenReturn(42);

 int result = (int) mockHandler.handle(invocation);

 assert result == 42;
 }

 @Test
 public void testHandleInvocation_stubbingVoidMethod() {
 Object mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = mock(Invocation.class);
 ArgumentMatcher argumentMatcher = ArgumentMatchers.anyString();

 mockHandler.voidMethodStubbable(mock).stubVoid(invocation, argumentMatcher);

 verify(mock).voidMethod(ArgumentMatchers.anyString());

 mockHandler.handle(invocation);
 }

 @Test
 public void testHandleInvocation_stubbingNonVoidMethod() {
 Object mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = mock(Invocation.class);
 ArgumentMatcher argumentMatcher = ArgumentMatchers.anyString();
 Answer answer = mock(Answer.class);

 mockHandler.voidMethodStubbable(mock).stub(invocation, argumentMatcher).toAnswer(answer);

 Object result = mockHandler.handle(invocation);

 verify(answer).answer(invocation);

 assert result == null;
 }

 @Test
 public void testHandleInvocation_stubbingMultipleAnswers() {
 Object mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = mock(Invocation.class);
 ArgumentMatcher argumentMatcher = ArgumentMatchers.anyString();
 Answer answer1 = mock(Answer.class);
 Answer answer2 = mock(Answer.class);

 mockHandler.voidMethodStubbable(mock).stub(invocation, argumentMatcher).toAnswers(answers);

 for (Answer answer : answers) {
 answer.answer(invocation);
 }

 Object result = mockHandler.handle(invocation);

 verify(answer1).answer(invocation);
 verify(answer2).answer(invocation);

 assert result == null;
 }

 @Test
 public void testHandleInvocation_verification() {
 Object mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = mock(Invocation.class);
 ArgumentMatcher argumentMatcher = ArgumentMatchers.anyString();

 mockHandler.voidMethodStubbable(mock).stubVoid(invocation, argumentMatcher);

 VerificationMode verificationMode = Mockito.verify(mock).method(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());

 mockHandler.handle(invocation);

 verify(mock).method(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(1));
 }

 @Test
 public void testHandleInvocation_verificationMultipleCalls() {
 Object mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = mock(Invocation.class);
 ArgumentMatcher argumentMatcher = ArgumentMatchers.anyString();

 mockHandler.voidMethodStubbable(mock).stubVoid(invocation, argumentMatcher);

 VerificationMode verificationMode = Mockito.verify(mock, VerificationMode.times(2)).method(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());

 mockHandler.handle(invocation);
 mockHandler.handle(invocation);

 verify(mock).method(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(1));
 verify(mock).method(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(1));
 }

 @Test
 public void testHandleInvocation_verificationNoInteractions() {
 Object mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = mock(Invocation.class);
 ArgumentMatcher argumentMatcher = ArgumentMatchers.anyString();

 mockHandler.voidMethodStubbable(mock).stubVoid(invocation, argumentMatcher);

 VerificationMode verificationMode = Mockito.verifyNoMoreInteractions(mock);

 mockHandler.handle(invocation);
 }

 @Test
 public void testHandleInvocation_verificationZeroInteractions() {
 Object mock = spy(MockitoInvocationHandler.class);
 Invocation invocation = mock(Invocation.class);
 ArgumentMatcher argumentMatcher = ArgumentMatchers.anyString();

 verifyZeroInteractions(mock);
 mockHandler.voidMethodStubbable(mock).stubVoid(invocation, argumentMatcher);
 mockHandler.handle(invocation);
 verifyZeroInteractions(mock);
 }

 @Test
 public void testHandleInvocation_exception() {
 Object mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = mock(Invocation.class);
 ArgumentMatcher argumentMatcher = ArgumentMatchers.anyString();

 doThrow(new RuntimeException("Test Exception")).when(mockHandler).handle(invocation);

 try {
 mockHandler.handle(invocation);
 } catch (RuntimeException e) {
 // Silence the exception
 }

 verify(mock).method(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
 }
}