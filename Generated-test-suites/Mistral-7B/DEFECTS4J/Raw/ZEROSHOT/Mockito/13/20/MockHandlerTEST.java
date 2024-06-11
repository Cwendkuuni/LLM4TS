import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 @Mock
 private DummyClass mock;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>();
 }

 @Test
 public void testHandle_voidMethodStubbing() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 VerificationMode verificationMode = Mockito.verifyNoMoreInteractions(mock);
 mockHandler.voidMethodStubbable(mock).stubVoid();
 mockHandler.handle(invocation);
 verify(mock).voidMethod(); // should be stubbed
 }

 @Test
 public void testHandle_answer() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object answer = new Object();
 Answer answerMock = Mockito.mock(Answer.class);
 when(answerMock.answers(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(answer);
 mockHandler.setAnswersForStubbing(Arrays.asList(answerMock));
 InvocationMatcher invocationMatcher = ArgumentMatchers.any();
 InvocationOnMock invocationToStub = Mockito.mock(InvocationOnMock.class);
 invocationMatcher.matches(invocationToStub);
 invocationToStub.getMock().methodToStub();
 invocation.setMethod(Mockito.method(DummyClass.class, "methodToStub", new Class[0]));
 invocationMatcher.matches(invocation);
 Object result = mockHandler.handle(invocation);
 verify(answerMock).answers(invocation);
 verify(mock).methodToStub();
 assertEquals(answer, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_multipleAnswersForSameInvocation() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 mockHandler.setAnswersForStubbing(Arrays.asList(Mockito.mock(Answer.class), Mockito.mock(Answer.class)));
 invocationMatcher.matches(invocation);
 mockHandler.handle(invocation);
 }

 @Test
 public void testHandle_verificationMode() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = ArgumentMatchers.any();
 invocationMatcher.matches(invocation);
 VerificationMode verificationMode = Mockito.verify(mock).methodToStub();
 mockHandler.handle(invocation);
 verify(mock).methodToStub(); // should not invoke the verificationMode again
 }

 @Test
 public void testHandle_nullInvocation() throws Throwable {
 mockHandler.handle(null); // should not throw an exception
 }
}

class DummyClass {
 void voidMethod() {
 }

 void methodToStub() {
 }
}