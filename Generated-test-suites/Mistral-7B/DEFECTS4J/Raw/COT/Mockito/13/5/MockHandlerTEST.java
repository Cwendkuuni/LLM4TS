import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 private DummyClass mock;

 @Before
 public void setUp() {
 mock = Mockito.mock(DummyClass.class);
 mockHandler = new MockHandler<>(new MockSettingsImpl());
 }

 @Test
 public void testHandle_NoAnswersOrVerification_ReturnsDefaultAnswer() {
 Invocation invocation = Mockito.mock(InvocationOnMock.class);

 when(mockHandler.handle(invocation)).thenReturn("default answer");

 String result = (String) mockHandler.handle(invocation);

 assert "default answer".equals(result);
 }

 @Test
 public void testHandle_AnswersForStubbing_ReturnsAnswer() {
 List<Answer> answers = new ArrayList<>();
 answers.add(invocation -> "answer");

 mockHandler.setAnswersForStubbing(answers);

 Invocation invocation = Mockito.mock(InvocationOnMock.class);

 String result = (String) mockHandler.handle(invocation);

 assert "answer".equals(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_AnswerAndVerification_ThrowException() {
 List<Answer> answers = new ArrayList<>();
 answers.add(invocation -> "answer");

 mockHandler.setAnswersForStubbing(answers);

 Invocation invocation = Mockito.mock(InvocationOnMock.class);

 mockingProgress.clear();
 VerificationMode verificationMode = Mockito.verify(mock, Mockito.times(1));

 try {
 mockHandler.handle(invocation);
 } catch (Throwable e) {
 throw new IllegalStateException("Unexpected exception", e);
 }
 }

 @Test
 public void testHandle_VerificationMode_VerifiesInvocation() {
 List<Answer> answers = new ArrayList<>();
 answers.add(invocation -> null);

 mockHandler.setAnswersForStubbing(answers);

 Invocation invocation = Mockito.mock(InvocationOnMock.class);

 VerificationMode verificationMode = Mockito.verify(mock, Mockito.times(1));
 mockHandler.handle(invocation);

 verify(mock).methodToBeVerified(ArgumentMatchers.any());
 }

 @Test
 public void testHandle_VerificationMode_ReturnsNull() {
 List<Answer> answers = new ArrayList<>();
 answers.add(invocation -> null);

 mockHandler.setAnswersForStubbing(answers);

 Invocation invocation = Mockito.mock(InvocationOnMock.class);

 VerificationMode verificationMode = Mockito.verify(mock, Mockito.times(1));
 String result = (String) mockHandler.handle(invocation);

 assert result == null;
 }

 @Test
 public void testHandle_VerificationMode_MultipleInvocations() {
 List<Answer> answers = new ArrayList<>();
 answers.add(invocation -> null);
 answers.add(invocation -> "second answer");

 mockHandler.setAnswersForStubbing(answers);

 Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
 Invocation invocation2 = Mockito.mock(InvocationOnMock.class);

 VerificationMode verificationMode1 = Mockito.verify(mock, Mockito.times(2));
 mockHandler.handle(invocation1);
 mockHandler.handle(invocation2);

 verify(mock).methodToBeVerified(ArgumentMatchers.any(), ArgumentMatchers.any());
 }

 @Test
 public void testHandle_NoVerificationMode_DoesNotVerifyInvocation() {
 List<Answer> answers = new ArrayList<>();
 answers.add(invocation -> null);

 mockHandler.setAnswersForStubbing(answers);

 Invocation invocation = Mockito.mock(InvocationOnMock.class);

 mockHandler.handle(invocation);

 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testHandle_NoVerificationMode_ReturnsNull() {
 List<Answer> answers = new ArrayList<>();
 answers.add(invocation -> null);

 mockHandler.setAnswersForStubbing(answers);

 Invocation invocation = Mockito.mock(InvocationOnMock.class);

 String result = (String) mockHandler.handle(invocation);

 assert result == null;
 }

 @Test
 public void testHandle_NoVerificationMode_MultipleInvocations() {
 List<Answer> answers = new ArrayList<>();
 answers.add(invocation -> null);
 answers.add(invocation -> "second answer");

 mockHandler.setAnswersForStubbing(answers);

 Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
 Invocation invocation2 = Mockito.mock(InvocationOnMock.class);

 mockHandler.handle(invocation1);
 mockHandler.handle(invocation2);

 verifyZeroInteractions(mock);
 }
}