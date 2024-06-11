import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoInvocationHandler> mockHandler;
 private MockitoInvocationHandler mockitoInvocationHandler;
 private Answer answer;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>(new MockSettingsImpl());
 mockitoInvocationHandler = mock(MockitoInvocationHandler.class);
 answer = Mockito.answer(invocation -> 42);
 }

 @Test
 public void testHandleInvocationWithNoAnswersOrVerifications() {
 Object result = mockHandler.handle(new InvocationOnMock(mockitoInvocationHandler, "methodName", new Object[0]));
 assertNull(result);
 }

 @Test
 public void testHandleInvocationWithAnswer() {
 when(mockHandler.getInvocationContainer().findAnswerFor(ArgumentMatchers.any())).thenReturn(answer);
 Object result = mockHandler.handle(new InvocationOnMock(mockitoInvocationHandler, "methodName", new Object[0]));
 assertEquals(42, result);
 }

 @Test
 public void testHandleInvocationWithVerification() {
 List<Answer> answers = new ArrayList<>();
 answers.add(answer);
 mockHandler.setAnswersForStubbing(answers);

 verify(mockitoInvocationHandler).methodName( ArgumentMatchers.anyInt());
 mockHandler.handle(new InvocationOnMock(mockitoInvocationHandler, "methodName", new Object[]{1}));
 }

 @Test(expected = IllegalStateException.class)
 public void testHandleInvocationWithVerificationAndAnswer() {
 List<Answer> answers = new ArrayList<>();
 answers.add(answer);
 mockHandler.setAnswersForStubbing(answers);

 try {
 verify(mockitoInvocationHandler).methodName( ArgumentMatchers.anyInt());
 mockHandler.handle(new InvocationOnMock(mockitoInvocationHandler, "methodName", new Object[]{1}));
 } catch (Throwable e) {
 // expected
 }

 verify(mockitoInvocationHandler).methodName(ArgumentMatchers.argThat(i -> i.equals(1)));
 }

 @Test(expected = IllegalStateException.class)
 public void testHandleInvocationWithAnswerAndAnotherAnswer() {
 when(mockHandler.getInvocationContainer().findAnswerFor(ArgumentMatchers.any())).thenReturn(answer);
 when(mockHandler.getInvocationContainer().findAnswerFor(ArgumentMatchers.any())).thenReturn(answer);

 try {
 mockHandler.handle(new InvocationOnMock(mockitoInvocationHandler, "methodName", new Object[0]));
 } catch (Throwable e) {
 // expected
 }

 verify(mockitoInvocationHandler, Mockito.atLeastOnce()).methodName(ArgumentMatchers.any());
 }

 @Test
 public void testHandleInvocationWithVerificationAndAnotherVerification() {
 List<Answer> answers = new ArrayList<>();
 answers.add(answer);
 mockHandler.setAnswersForStubbing(answers);

 verify(mockitoInvocationHandler).methodName(ArgumentMatchers.anyInt());
 verify(mockitoInvocationHandler).methodName(ArgumentMatchers.eq(1));
 mockHandler.handle(new InvocationOnMock(mockitoInvocationHandler, "methodName", new Object[]{1}));
 }

 @Test
 public void testHandleInvocationWithVerificationAndNoAnswer() {
 verify(mockitoInvocationHandler).methodName(ArgumentMatchers.anyInt());
 mockHandler.handle(new InvocationOnMock(mockitoInvocationHandler, "methodName", new Object[]{1}));
 }

 @Test
 public void testHandleInvocationWithVerificationAndThrowableAnswer() {
 List<Answer> answers = new ArrayList<>();
 answers.add(answer);
 answers.add(Mockito.answer(invocation -> { throw new RuntimeException(); }));
 mockHandler.setAnswersForStubbing(answers);

 try {
 mockHandler.handle(new InvocationOnMock(mockitoInvocationHandler, "methodName", new Object[]{1}));
 } catch (RuntimeException e) {
 // expected
 }

 verify(mockitoInvocationHandler).methodName(ArgumentMatchers.anyInt());
 }

 @Test
 public void testHandleInvocationWithVerificationAndNullAnswer() {
 List<Answer> answers = new ArrayList<>();
 mockHandler.setAnswersForStubbing(answers);

 verify(mockitoInvocationHandler).methodName(ArgumentMatchers.anyInt());
 mockHandler.handle(new InvocationOnMock(mockitoInvocationHandler, "methodName", new Object[]{1}));
 }
}