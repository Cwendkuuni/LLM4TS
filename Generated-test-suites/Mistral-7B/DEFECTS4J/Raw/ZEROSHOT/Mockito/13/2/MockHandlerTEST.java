import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerConsumer;
import org.mockito.stubbing.AnswerStub;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoTestClass> mockHandler;
 private MockSettingsImpl mockSettings;
 private MockitoTestClass mockitoTestClass;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 mockitoTestClass = mock(MockitoTestClass.class);
 }

 @Test
 public void testHandle_whenStubbingVoidMethod_thenReturnsNull() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 AnswerStub answerStub = AnswerStub.returning(null);

 mockHandler.setAnswersForStubbing(Arrays.asList(answerStub));

 mockHandler.handle(invocation);

 verify(invocation).isStub();
 }

 @Test
 public void testHandle_whenVerifyingInvocation_thenVerifiesCorrectly() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(mockitoTestClass.someMethod(ArgumentMatchers.anyString())).thenReturn("test");

 mockHandler.handle(invocation);

 verify(mockitoTestClass).someMethod(ArgumentMatchers.eq("test"));
 }

 @Test
 public void testHandle_whenVerifyingMultipleInvocations_thenVerifiesCorrectly() {
 InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
 when(mockitoTestClass.someMethod(ArgumentMatchers.anyString())).thenReturn("test1").thenReturn("test2");

 mockHandler.handle(invocation1);
 mockHandler.handle(invocation2);

 verify(mockitoTestClass).someMethod(ArgumentMatchers.eq("test1"));
 verify(mockitoTestClass).someMethod(ArgumentMatchers.eq("test2"));
 }

 @Test
 public void testHandle_whenNoVerificationOrStubbing_thenDoesNothing() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 mockHandler.handle(invocation);

 verifyNoInteractions(mockitoTestClass);
 }

 @Test
 public void testHandle_whenInvokingVoidMethodWithVoidStub_thenReturnsNull() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 mockHandler.voidMethodStubbable(mockitoTestClass).stub().when(mockitoTestClass).someVoidMethod();

 mockHandler.handle(invocation);

 verify(invocation).isStub();
 }

 @Test
 public void testHandle_whenInvokingVoidMethodWithoutStub_thenCallsDefaultAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Answer answer = Answer.returning(null);

 mockHandler.setAnswersForStubbing(Arrays.asList(answer));
 mockHandler.handle(invocation);

 verify(mockitoTestClass).someVoidMethod();
 }

 @Test
 public void testHandle_whenInvokingMethodWithAnswer_thenReturnsAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Answer answer = Answer.returning("answer");
 AnswerConsumer answerConsumer = Mockito.mock(AnswerConsumer.class);

 when(answerConsumer.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(answer);
 mockHandler.setAnswersForStubbing(Arrays.asList(answerConsumer));

 mockHandler.handle(invocation);

 verify(answerConsumer).answer(invocation);
 }

 @Test
 public void testHandle_whenInvokingMethodWithNoAnswersForStubbing_thenCallsDefaultAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 mockHandler.handle(invocation);

 verifyZeroInteractions(mockitoTestClass);
 }
}